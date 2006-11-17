package com.opensymphony.able.util;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public abstract class EmptyMap implements Map {
    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Object put(Object key, Object value) {
        return null;
    }

    public Object remove(Object key) {
        return null;
    }

    public void putAll(Map t) {
    }

    public void clear() {
    }

    public Set keySet() {
        return null;
    }

    public Collection values() {
        return null;
    }

    public Set entrySet() {
        return null;
    }
}
