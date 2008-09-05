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
package com.opensymphony.able.introspect;

import java.beans.PropertyDescriptor;

import javax.persistence.Id;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * 
 * @version $Revision: 161 $
 */
public class PropertyInfoTest {

    @Test
    public void testFindAnnotation() throws Exception {
        final Class<Id> annotationClass = javax.persistence.Id.class;
        // this instance of PropertyInfo is created for the sake of accessing findAnnotation.
        final PropertyInfo propertyInfo = new PropertyInfo(new EntityInfo(DomainObject.class), 
                                                           new PropertyDescriptor("id", DomainObject.class), 
                                                           DomainObject.class);
        assertNotNull(propertyInfo.findAnnotation(annotationClass));
        assertTrue(propertyInfo.findAnnotation(annotationClass) instanceof Id);
    }

    //////////////////////////////////////////////////
    // The following classes are used when testing. //
    //////////////////////////////////////////////////
    private abstract class AbstractDomainObject<T>{
        abstract T getId();
        abstract void setId(T id);
    }

    private class DomainObject 
        extends AbstractDomainObject<Long>{
        
        private Long id;

        public DomainObject(final Long id) {
            this.id = id;
        }
        
        @Id
        public Long getId(){
            return this.id;
        }

        @Override
        public void setId(final Long id){
            this.id = id;
        }
    }

}
