package com.opensymphony.able.webwork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * When tagged on an action, the request attribute will automatically be set to "none", causing the page
 * to avoid being decoratored by SiteMesh.
 *
 * @see com.opensymphony.able.sitemesh.NoneDecoratorMapper
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Partial {
}
