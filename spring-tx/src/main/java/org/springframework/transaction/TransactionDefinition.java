/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction;

import java.sql.Connection;

import org.springframework.lang.Nullable;

/**
 *定义与spring兼容的事务属性的接口。

 *基于与EJB CMT属性类似的传播行为定义。

 注意隔离级别和超时设置将不会被应用，除非

 一个实际的新事务开始了。只有{@link #PROPAGATION_REQUIRED}，

 * {@link #PROPAGATION_REQUIRES_NEW}和{@link #PROPAGATION_NESTED}可能导致

 在其他情况下，指定这些设置通常没有意义。

 *此外，要知道不是所有的事务管理器都支持这些

 *高级特性，因此可能会抛出相应的异常

 *非默认值。
 {@link #isReadOnly() read-only标志}适用于任何事务上下文，

 *是否支持实际的资源事务或非事务操作

 *在资源层面。在后一种情况下，该标志将只应用于managed

 *资源，例如Hibernate {@code Session}。
 *
 * @author Juergen Hoeller
 * @since 08.05.2003
 * @see PlatformTransactionManager#getTransaction(TransactionDefinition)
 * @see org.springframework.transaction.support.DefaultTransactionDefinition
 * @see org.springframework.transaction.interceptor.TransactionAttribute
 */
/*事务的一些常量，事务隔离级别，事务传播行为常量*/
public interface TransactionDefinition {

	/**
	 * 支持当前事务;如果不存在，创建一个新的。
	 */
	int PROPAGATION_REQUIRED = 0;

	/**
	 *支持当前事务;如果不存在，则以非事务方式执行。--一般不使用。对于具有事务同步的事务管理器
	 * 相同的资源(一个JDBC {@code Connection}，一个
	 * * Hibernate {@code Session}，等等)将共享整个指定的
	 * *范围。请注意，具体的行为取决于实际的同步
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setTransactionSynchronization
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
	 */
	int PROPAGATION_SUPPORTS = 1;

	/**
	 * 强制性的
	 *支持当前事务;如果没有当前事务，则抛出异常
	 *的存在。类似于具有相同名称的EJB事务属性。
	 注意在一个{@code PROPAGATION_MANDATORY}中的事务同步
	 范围总是由周围的事务驱动。
	 */
	int PROPAGATION_MANDATORY = 2;
	/**
	 * 不管有没有事务都新建一个事务，如果有事务都就挂起
	 创建一个新的事务，如果当前事务存在，挂起当前事务。
	 *类似于同名的EJB事务属性。
	 注意:实际的事务挂起不会开箱即用
	 *所有事务管理器。这尤其适用于
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 *它需要{@code javax.transaction。TransactionManager},
	 *提供给它(在标准Java EE中是特定于服务器的)。
	 A {@code PROPAGATION_REQUIRES_NEW}作用域总是定义自己的
	 *事务同步。现有的同步将被暂停
	 *和适当地恢复。
	 */
	int PROPAGATION_REQUIRES_NEW = 3;

	/**
	 *不支持当前事务;而是始终以非事务方式执行。
	 *类似于同名的EJB事务属性。
	 注意:实际的事务挂起不会开箱即用
	 *所有事务管理器。这尤其适用于
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 *它需要{@code javax.transaction。TransactionManager},
	 *提供给它(在标准Java EE中是特定于服务器的)。
	 注意事务同步是而不是
	 * {@code PROPAGATION_NOT_SUPPORTED}范围。现有的同步
	 *将暂停并适当恢复。
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	int PROPAGATION_NOT_SUPPORTED = 4;

	/**
	 *不支持当前事务;如果当前事务存在，则抛出异常
	 *的存在。类似于具有相同名称的EJB事务属性。
	 注意事务同步是而不是
	 * {@code PROPAGATION_NEVER}范围。
	 */
	int PROPAGATION_NEVER = 5;

	/**
	 如果当前事务存在，则在嵌套事务中执行;
	 *行为类似{@link #PROPAGATION_REQUIRED}否则。没有
	 EJB中类似的特性。
	 注意:嵌套事务的实际创建将只在
	 *特定的事务管理器。开箱即用，这只适用于JDBC
	 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager}
	 *当工作在JDBC 3.0驱动程序。一些JTA提供者可能会支持
	 *嵌套事务。
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	int PROPAGATION_NESTED = 6;


	/**
	 *使用底层数据存储的默认隔离级别。
	 *所有其他级别对应JDBC隔离级别。
	 * 默认是使用JDBC的隔离级别
	 * @see java.sql.Connection
	 */
	int ISOLATION_DEFAULT = -1;

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * can occur.
	 * <p>This level allows a row changed by one transaction to be read by another
	 * transaction before any changes in that row have been committed (a "dirty read").
	 * If any of the changes are rolled back, the second transaction will have
	 * retrieved an invalid row.
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 */
	/*读未提交*/
	int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;

	/**
	 * Indicates that dirty reads are prevented; non-repeatable reads and
	 * phantom reads can occur.
	 * <p>This level only prohibits a transaction from reading a row
	 * with uncommitted changes in it.
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 */
	/*读已提交*/
	int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;

	/**
	 * Indicates that dirty reads and non-repeatable reads are prevented;
	 * phantom reads can occur.
	 * <p>This level prohibits a transaction from reading a row with uncommitted changes
	 * in it, and it also prohibits the situation where one transaction reads a row,
	 * a second transaction alters the row, and the first transaction re-reads the row,
	 * getting different values the second time (a "non-repeatable read").
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 */
	/*可重复读*/
	int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * are prevented.
	 * <p>This level includes the prohibitions in {@link #ISOLATION_REPEATABLE_READ}
	 * and further prohibits the situation where one transaction reads all rows that
	 * satisfy a {@code WHERE} condition, a second transaction inserts a row
	 * that satisfies that {@code WHERE} condition, and the first transaction
	 * re-reads for the same condition, retrieving the additional "phantom" row
	 * in the second read.
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 */
	/*串行化*/
	int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;


	/**
	 *使用底层事务系统的默认超时时间，
	 *或none，如果不支持超时。
	 */
	int TIMEOUT_DEFAULT = -1;


	/**
	 *返回传播行为。
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isActualTransactionActive()
	 */
	int getPropagationBehavior();

	/**
	 *返回隔离级别。
	 * 这些常数是设计好的
	 * *匹配{@link java.sql.Connection}中相同常量的值。
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setValidateExistingTransaction
	 */
	int getIsolationLevel();

	/**
	 返回事务超时。
	 必须返回秒数，或者{@link #TIMEOUT_DEFAULT}。
	 专为使用{@link #PROPAGATION_REQUIRED}或
	 * {@link #PROPAGATION_REQUIRES_NEW}因为它只适用于新启动的
	 *交易。
	 注意，不支持超时的事务管理器将抛出异常
	 *当给出任何其他超时时异常，而不是{@link #TIMEOUT_DEFAULT}。
	 * @返回事务超时
	 */
	int getTimeout();

	/**
	 返回是否优化为只读事务。
	 read-only标志适用于任何事务上下文，无论是否支持
	 *通过一个实际的资源事务({@link #PROPAGATION_REQUIRED}/
	 * {@link #PROPAGATION_REQUIRES_NEW})或非事务操作
	 *资源级别({@link #PROPAGATION_SUPPORTS})。在后一种情况下，
	 *该标志将只应用于应用程序中的托管资源，
	 *例如Hibernate {@code Session}。
	 这只是作为实际事务子系统的提示;
	 不一定导致写访问失败。
	 一个不能解释只读提示的事务管理器可以
	 * not当请求只读事务时抛出异常。
	 * @return {@code true}如果事务被优化为只读
	 * @see org.springframework.transaction.support.TransactionSynchronization#beforeCommit(boolean)
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isCurrentTransactionReadOnly()
	 */
	boolean isReadOnly();

	/**
	 返回该事务的名称。可以是{@code null}。
	 这将被用作在a中显示的事务名称
	 *事务监视器，如果适用(例如，WebLogic的)。
	 对于Spring的声明性事务，公开的名称将是
	 {@code完全限定类名+ "。"+方法名}(默认情况下)。
	 * @返回事务的名称
	 * @see org.springframework.transaction.interceptor.TransactionAspectSupport
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#getCurrentTransactionName()
	 */
	@Nullable
	String getName();

}
