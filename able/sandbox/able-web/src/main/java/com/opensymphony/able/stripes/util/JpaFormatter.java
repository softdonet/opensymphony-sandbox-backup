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
import net.sourceforge.stripes.format.Formatter;

import java.util.Locale;

/**
 * A JPA based formatter which turns an entity object into its string based primary key value
 * 
 * @version $Revision$
 */
public class JpaFormatter implements Formatter {
    private EntityInfo entityInfo;
    private String formatType;
    private String formatPattern;
    private Locale locale;
    private JpaFormatterFactory formatterFactory;

    public JpaFormatter(JpaFormatterFactory formatterFactory, Class entityType) {
        this.formatterFactory = formatterFactory;
        this.entityInfo = EntityInfo.newInstance(entityType);
    }

    public void setFormatType(String formatType) {

        this.formatType = formatType;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void init() {
    }

    public String format(Object input) {
        Object primaryKey = entityInfo.getIdValue(input);
        if (primaryKey != null) {
            Formatter formatter = formatterFactory.getFormatter(primaryKey.getClass(), locale, formatType, formatPattern);
            if (formatter != null) {
                return formatter.format(primaryKey);
            }
            return primaryKey.toString();
        }
        return "";
    }
}
