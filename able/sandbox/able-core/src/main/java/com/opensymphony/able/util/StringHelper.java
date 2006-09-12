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
package com.opensymphony.able.util;

import java.beans.Introspector;

/**
 *
 * @version $Revision$
 */
public class StringHelper {

    public static String splitCamelCase(String name) {
        StringBuilder builder = new StringBuilder();
        boolean lastUpperCase = true;
        for (int i = 0, size = name.length(); i < size; i++) {
            char ch = name.charAt(i);
            if (i == 0) {
                ch = Character.toUpperCase(ch);
            }
            else {
                if (Character.isUpperCase(ch)) {
                    if (!lastUpperCase) {
                        builder.append(' ');
                        // TODO - convert to lower case?
                    }
                }
            }
            builder.append(ch);
            lastUpperCase = Character.isUpperCase(ch);
        }
        return builder.toString();
    }

    public static String capitalize(String text) {
        if (text != null && text.length() > 0) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }
    public static String decapitalize(String text) {
        return Introspector.decapitalize(text);
    }
}
