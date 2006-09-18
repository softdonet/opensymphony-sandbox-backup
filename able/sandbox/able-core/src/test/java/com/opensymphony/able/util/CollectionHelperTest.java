/**
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

import java.util.*;

import org.testng.annotations.Test;

import com.opensymphony.able.util.CollectionHelper;

import static org.testng.Assert.*;

/**
 * 
 * version $Revision$
 */
public class CollectionHelperTest {
	@SuppressWarnings("unchecked")
	@Test
	public void testSize() throws Exception {
		String[] array = new String[] { "a", "b", "c" };
		assertSize(array, 3);
		List list = new ArrayList(Arrays.asList(array));
		assertSize(list, 3);
	}

	private void assertSize(Object value, int expected) {
		int size = CollectionHelper.size(value);
		assertEquals(size, expected, "size of: " + value);
	}

}
