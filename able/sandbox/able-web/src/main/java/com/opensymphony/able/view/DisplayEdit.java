/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the metadata for the edit form for a POJO.
 *  
 * @version $Revision$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
@Inherited
@Documented
public @interface DisplayEdit {
    
    /**
     * Specifies the sort order of the properties of the bean, listing the properties which come first. 
     * You do not have to list every property explicitly, just the ones which you wish to come first.
     * You can then include or exclude properties using the {@link #includes()} and {{@link #excludes()} methods.
     */
    String[] sortOrder() default {};

    /**
     * The list of properties to be explicitly included in views by default. Properties not listed in this list are excluded
     * unless this value is left blank which means include everything apart from those properties explicitly excluded
     * 
     * Can only be specified if {@link #excludes()} is not specified.
     */
    String[] includes() default {};
    
    /**
     * The list of properties explicitly excluded from the view. Can only be specified if {@link #includes()} is not specified.
     */
    String[] excludes() default {};
}
