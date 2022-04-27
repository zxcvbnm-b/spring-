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

import java.io.Flushable;

/**
 事务状态的表示。
 事务性代码可以使用它来检索状态信息，
 *并以编程方式请求回滚(而不是抛出
 *导致隐式回滚的异常)。
 包含{@link SavepointManager}接口来提供访问
 *保存点管理设施。请注意保存点管理
 *仅在底层事务管理器支持的情况下可用。
 * @author Juergen Hoeller
 * @since 27.03.2003
 * @see #setRollbackOnly()
 * @see PlatformTransactionManager#getTransaction
 * @see org.springframework.transaction.support.TransactionCallback#doInTransaction
 * @see org.springframework.transaction.interceptor.TransactionInterceptor#currentTransactionStatus()
 */
public interface TransactionStatus extends SavepointManager, Flushable {

	/**
	 *返回当前事务是否为新事务;否则参与
	 *，或者可能没有在实际的
	 首先是交易。
	 */
	boolean isNewTransaction();

	/**
	 *返回事务内部是否携带保存点，
	 也就是说，已经创建为基于保存点的嵌套事务。
	 *
	 此方法主要用于诊断目的，以及
	 * {@link # isNewTransaction()}。用于自定义的编程处理
	 *保存点，使用{@link SavepointManager}提供的操作。
	 * @see #isNewTransaction()
	 * @see #createSavepoint()
	 * @see #rollbackToSavepoint(Object)
	 * @see #releaseSavepoint(Object)
	 */
	boolean hasSavepoint();

	/**
	 * 触发回滚
	 *
	 *设置事务只回滚。这将指示事务管理器
	 事务的唯一可能的结果可能是回滚，如
	 *替代抛出一个异常，这将反过来触发回滚。
	 主要用于管理的事务
	 * {@link org.springframework.transaction.support.TransactionTemplate} or
	 * {@link org.springframework.transaction.interceptor.TransactionInterceptor},
	 * where the actual commit/rollback decision is made by the container.
	 * @see org.springframework.transaction.support.TransactionCallback#doInTransaction
	 * @see org.springframework.transaction.interceptor.TransactionAttribute#rollbackOn
	 */
	void setRollbackOnly();

	/**
	 返回事务是否被标记为仅回滚
	 */
	boolean isRollbackOnly();

	/**
	 将底层会话刷新到数据存储，如果适用:
	 *例如，所有受影响的Hibernate/JPA会话。
	 这实际上只是一个提示，如果底层的
	 事务管理器没有flush的概念。同向信号可能
	 * get应用到主资源或事务同步，
	 *取决于底层资源。
	 */
	@Override
	void flush();

	/**
	 *返回该事务是否已完成，即
	 *是否已经提交或回滚。
	 * @see PlatformTransactionManager#commit
	 * @see PlatformTransactionManager#rollback
	 */
	boolean isCompleted();

}
