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
 * 
 * TODO we could get clever and figure out what URIs are read only transactions etc
 * 
 * @version $Revision$
 */
public class TransactionServletFilter implements Filter {

    public static final String TRANSACTION_STATUS = TransactionServletFilter.class.getName() + ".status";
    
    private TransactionTemplate transactionTemplate;

    public void init(FilterConfig config) throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        transactionTemplate = (TransactionTemplate) ctx.getBean("transactionTemplate");
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        // TODO we could get clever and figure out what URIs are read only transactions etc
        transactionTemplate.setReadOnly(false);
        Exception e = (Exception) transactionTemplate.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                try {
                    request.setAttribute(TRANSACTION_STATUS, status);
                    filterChain.doFilter(request, response);
                    return null;
                }
                catch (Exception e) {
                    return e;
                }
            }
        });

        if (e instanceof IOException) {
            throw (IOException) e;
        }
        else if (e instanceof ServletException) {
            throw (ServletException) e;
        }
        else if (e != null) {
            throw new ServletException(e);
        }
    }

    public static TransactionStatus getTransactionStatus(final ServletRequest request) {
        return (TransactionStatus) request.getAttribute(TRANSACTION_STATUS);
    }
    
    public void destroy() {
    }
}
