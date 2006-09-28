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
package com.opensymphony.able.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
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
 * A simple transactional filter so that all web operations are in a transaction
 * for JPA reads and writes.
 * <p/>
 * TODO we could get clever and figure out what URIs are read only transactions etc
 *
 * @version $Revision$
 */
public class SimpleTransactionServletFilter implements Filter {

    private static final Log log = LogFactory.getLog(SimpleTransactionServletFilter.class);

    protected static final String TRANSACTION_OUTCOME = SimpleTransactionServletFilter.class.getName() + ".outcome";

    private WebApplicationContext context;


    public void init(FilterConfig config) throws ServletException {
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        // TODO we could get clever and figure out what URIs are read only transactions etc
        final TransactionTemplate transactionTemplate = (TransactionTemplate) context.getBean("transactionTemplate");
        transactionTemplate.setReadOnly(false);

        if (log.isDebugEnabled()) {
            log.debug("Starting a transaction");
        }

        try {
            Exception e = (Exception) transactionTemplate.execute(new TransactionCallback() {

                public Object doInTransaction(TransactionStatus status) {
                    try {
                        TransactionOutcome outcome = new TransactionOutcome(status, transactionTemplate);
                        TransactionOutcome.setTransactionOutcome(outcome);
                        filterChain.doFilter(request, response);

                        if (outcome.isRollbackOnly()) {
                            log.debug("Outcome is rollback");
                            status.setRollbackOnly();
                        }

                        if (log.isDebugEnabled()) {
                            log.debug("Completing a transaction with rollback: " + status.isRollbackOnly());
                        }
                        return null;
                    }
                    catch (Exception e) {
                        return e;
                    }
                }
            });

            if (log.isDebugEnabled()) {
                log.debug("End transaction with exception: " + e);
            }

            if (e instanceof IOException) {
                throw(IOException) e;
            }
            else if (e instanceof ServletException) {
                throw(ServletException) e;
            }
            else if (e != null) {
                throw new ServletException(e);
            }
        }
        finally {
            TransactionOutcome.setTransactionOutcome(null);
        }
    }

    public void destroy() {
    }
}
