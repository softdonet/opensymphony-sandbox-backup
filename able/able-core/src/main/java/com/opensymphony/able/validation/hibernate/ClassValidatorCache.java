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
package com.opensymphony.able.validation.hibernate;

import org.hibernate.validator.ClassValidator;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A simple cache of {@link ClassValidator} instances
 * 
 * @version $Revision$
 */
public class ClassValidatorCache {

    private static final ClassValidatorCache singleton = new ClassValidatorCache();

    private Map<Class, ClassValidator> map = new WeakHashMap<Class, ClassValidator>();
    
    public static ClassValidatorCache getInstance() {
        return singleton;
    }
    
    @SuppressWarnings("unchecked")
    public synchronized ClassValidator getValidator(Class type) {
        ClassValidator answer = map.get(type);
        if (answer == null) {
            answer = new ClassValidator(type);
            map.put(type, answer);
        }
        return answer;
    }
}
