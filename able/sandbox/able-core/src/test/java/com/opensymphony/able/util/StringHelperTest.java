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

import com.opensymphony.able.util.StringHelper;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 *
 * @version $Revision$
 */
public class StringHelperTest {
    
    @Test
    public void testCapitalize() throws Exception {
        assertEquals(StringHelper.capitalize("fooBar"), "FooBar");
        assertEquals(StringHelper.capitalize("FooBar"), "FooBar");
    }
    
    @Test
    public void testDapitalize() throws Exception {
        assertEquals(StringHelper.decapitalize("fooBar"), "fooBar");
        assertEquals(StringHelper.decapitalize("FooBar"), "fooBar");
    }

    @Test
    public void testSplitCamelCase() throws Exception {
        assertEquals(StringHelper.splitCamelCase("fooBarWhatnot"), "Foo Bar Whatnot");
    }
}
