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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A holder class of the transaction status together with providing hints to the
 * current transaction on whether or not it should be commit or rolled back.
 *
 * @version $Revision$
 */
public class TransactionOutcome {
    private static final transient Log log = LogFactory.getLog(TransactionOutcome.class);

    private static final ThreadLocal<TransactionOutcome> threadLocal = new ThreadLocal<TransactionOutcome>();

    private final TransactionStatus status;
    private TransactionTemplate transactionTemplate;
    private boolean shouldRollback = false;
    private boolean shouldCommit = false;
    private boolean delayCommit = true;

    /**
     * Marks that a transaction should be rolled back rather than commit
     */
    public static void shouldRollback() {
        TransactionOutcome outcome = getTransactionOutcome();
        if (outcome != null) {
            outcome.enableRollback();
        }
        else {
            log.error("No transaction in progress!");
        }
    }

    /**
     * Marks that a transaction should be committed. If this method is not
     * called then the transaction will be rolled back to avoid accidental
     * updates.
     */
    public static void shouldCommit() {
        TransactionOutcome outcome = getTransactionOutcome();
        if (outcome != null) {
            outcome.enableCommit();
        }
        else {
            log.error("No transaction in progress!");
        }
    }

    public static TransactionOutcome getTransactionOutcome() {
        return threadLocal.get();
    }

    public static void setTransactionOutcome(TransactionOutcome value) {
        threadLocal.set(value);
    }


    public TransactionOutcome(TransactionStatus status, TransactionTemplate transactionTemplate) {
        this.status = status;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * Marks that a transaction should be rolled back rather than commit
     */
    public void enableRollback() {
        Exception exception = new Exception();
        log.warn("Being told to rollback!", exception);
        exception.printStackTrace();

        shouldRollback = true;
    }

    /**
     * Marks that a transaction should be committed. If this method is not
     * called then the transaction wil be rolled back to avoid accidental
     * updates.
     */
    public void enableCommit() {
        if (delayCommit) {
            shouldCommit = true;
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Committing the transaction with rollback status: " + status.isRollbackOnly());
            }
            PlatformTransactionManager transactionManager = transactionTemplate.getTransactionManager();
            transactionManager.commit(status);
        }
    }

    /**
     * Returns true if this transaction should be rolled back to avoid any
     * accidental updates
     */
    public boolean isRollbackOnly() {
        return shouldRollback || !shouldCommit;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public boolean isDelayCommit() {
        return delayCommit;
    }

    /**
     * Should we delay the commit until the end of the filter or perform it immediately inside the action bean?
     */
    public void setDelayCommit(boolean delayCommit) {
        this.delayCommit = delayCommit;
    }
}
