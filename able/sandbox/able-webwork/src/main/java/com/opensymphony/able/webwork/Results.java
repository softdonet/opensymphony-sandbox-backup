package com.opensymphony.able.webwork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Results {
    Result[] results();
}
