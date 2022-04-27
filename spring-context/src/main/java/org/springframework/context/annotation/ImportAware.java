/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

/**
 *由任何@{@link Configuration}类实现的接口 可以由@Configuration注解的类实现的接口，他会给使用@import导入这个类的注解的信息注入到当前配置类：
 * 比如说你在一个 @Configuration配置类上使用了 @EnableTransactionManagement 然后再@EnableTransactionManagement使用了
 * @Import导入一个类，那么导入的这个类可以实现这个接口，他会将EnableTransactionManagement的注解信息注入到 使用@Import导入的这个类
 @{@code Configuration}的{@link AnnotationMetadata}被注入
 导入它的类。与注释一起使用
  *使用@{@link Import}作为元注解。
 * @author Chris Beams
 * @since 3.1
 */
public interface ImportAware extends Aware {

	/**
	 * Set the annotation metadata of the importing @{@code Configuration} class.
	 */
	void setImportMetadata(AnnotationMetadata importMetadata);

}
