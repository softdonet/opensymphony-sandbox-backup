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

/**
 * A holder class of the transaction status together with providing hints to the
 * current transaction on whether or not it should be commit or rolled back.
 * 
 * @version $Revision$
 */
public class TransactionOutcome {

    private final TransactionStatus status;
    private boolean shouldRollback = false;
    private boolean shouldCommit = false;

    public TransactionOutcome(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Marks that a transaction should be rolled back rather than commit
     */
    public void shouldRollback() {
        shouldRollback = true;
    }

    /**
     * Marks that a transaction should be committed. If this method is not
     * called then the transaction wil be rolled back to avoid accidental
     * updates.
     */
    public void shouldCommit() {
        shouldCommit = true;
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

}
