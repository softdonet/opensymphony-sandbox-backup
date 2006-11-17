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
package com.opensymphony.able.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 * @version $Revision$
 */
public class MockEntityManager implements EntityManager {
    private FlushModeType flushMode;

    public void persist(Object object) {
    }

    public <T> T merge(T t) {
        return t;
    }

    public void remove(Object object) {
    }

    public <T> T find(Class<T> aClass, Object object) {
        return null;
    }

    public <T> T getReference(Class<T> aClass, Object object) {
        return null;
    }

    public void flush() {

    }

    public void setFlushMode(FlushModeType flushModeType) {

    }

    public FlushModeType getFlushMode() {
        return flushMode;
    }

    public void lock(Object object, LockModeType lockModeType) {

    }

    public void refresh(Object object) {
    }

    public void clear() {
    }

    public boolean contains(Object object) {
        return false;
    }

    public Query createQuery(String string) {
        return null;
    }

    public Query createNamedQuery(String string) {
        return null;
    }

    public Query createNativeQuery(String string) {
        return null;
    }

    public Query createNativeQuery(String string, Class aClass) {
        return null;
    }

    public Query createNativeQuery(String string, String string1) {
        return null;
    }

    public void joinTransaction() {

    }

    public Object getDelegate() {
        return null;
    }

    public void close() {

    }

    public boolean isOpen() {
        return false;
    }

    public EntityTransaction getTransaction() {
        return null;
    }
}
