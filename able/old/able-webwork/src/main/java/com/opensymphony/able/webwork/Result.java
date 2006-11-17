package com.opensymphony.able.webwork;

import com.opensymphony.xwork.Action;
import com.opensymphony.webwork.dispatcher.ServletDispatcherResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Result {
    String name() default Action.SUCCESS;
    Class type() default ServletDispatcherResult.class;
    String location();
}
