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
package com.opensymphony.able.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * A more complex transactional filter which avoids using the TransactionTemplate so that the transaction can be committed & resumed within the filter
 *
 * @version $Revision$
 */
public class TransactionServletFilter implements Filter {

    private static final Log log = LogFactory.getLog(TransactionServletFilter.class);

    protected static final String TRANSACTION_OUTCOME = TransactionServletFilter.class.getName() + ".outcome";

    private WebApplicationContext context;


    public void init(FilterConfig config) throws ServletException {
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        // TODO we could get clever and figure out what URIs are read only transactions etc
        TransactionTemplate transactionTemplate = (TransactionTemplate) context.getBean("transactionTemplate");
        transactionTemplate.setReadOnly(false);

        if (log.isDebugEnabled()) {
            log.debug("Starting a transaction");
        }

        PlatformTransactionManager transactionManager = transactionTemplate.getTransactionManager();
        TransactionStatus status = transactionManager.getTransaction(transactionTemplate);
        TransactionOutcome outcome = new TransactionOutcome(status, transactionTemplate);
        request.setAttribute(TRANSACTION_OUTCOME, outcome);


        Exception exception = null;
        try {
            filterChain.doFilter(request, response);
            status = transactionManager.getTransaction(transactionTemplate);
        }
        catch (Exception e) {
            exception = e;
            log.warn("Caught: " + e, e);
        }

        if (outcome.isRollbackOnly() || exception != null) {
            status.setRollbackOnly();
        }

        try {
            if (status.isRollbackOnly()) {
                log.debug("Rolling back transaction");
                transactionManager.rollback(status);
            }
            else {
                log.debug("Committing transaction");
                transactionManager.commit(status);
            }
        }
        catch (TransactionException e) {
            if (exception == null) {
                exception = e;
            }
            else {
                log.debug("Failed to rollback transaction: " + e, e);
            }
        }


        if (exception instanceof IOException) {
            throw(IOException) exception;
        }
        else if (exception instanceof ServletException) {
            throw(ServletException) exception;
        }
        else if (exception != null) {
            throw new ServletException(exception);
        }
    }


    public static TransactionOutcome getTransactionOutcome(ServletRequest request) {
        return (TransactionOutcome) request.getAttribute(TRANSACTION_OUTCOME);
    }

    public void destroy() {
    }
}
