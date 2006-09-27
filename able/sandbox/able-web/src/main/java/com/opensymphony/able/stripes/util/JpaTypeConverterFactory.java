/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.stripes.util;

import com.opensymphony.able.entity.EntityInfo;
import net.sourceforge.stripes.integration.spring.SpringHelper;
import net.sourceforge.stripes.validation.TypeConverter;

import javax.persistence.Entity;
import javax.servlet.ServletContext;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;

public class JpaTypeConverterFactory extends SpringTypeConverterFactory {

    private Map<Class, TypeConverter> cache = new IdentityHashMap<Class, TypeConverter>();

    public TypeConverter getTypeConverter(Class forType, Locale locale) throws Exception {
        if (forType.getAnnotation(Entity.class) != null) {
            synchronized (cache) {
                TypeConverter answer = cache.get(forType);
                if (answer == null) {
                    answer = createJpaTypeConverter(forType, locale);
                    cache.put(forType, answer);
                }
                return answer;
            }
        }
        return super.getTypeConverter(forType, locale);
    }

    protected TypeConverter createJpaTypeConverter(Class forType, Locale locale) {
        EntityInfo info = EntityInfo.newInstance(forType);
        JpaTypeConverter converter = new JpaTypeConverter(info);

        ServletContext servletContext = getConfiguration().getServletContext();
        SpringHelper.injectBeans(converter, servletContext);

        return converter;
    }

}
