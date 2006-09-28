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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * @version $Revision$
 */
public class TransactionOutcomeTest {

    @Test
    public void testDefaultIsRollback() {
        TransactionOutcome outcome = new TransactionOutcome(null, null);
        Assert.assertEquals(true, outcome.isRollbackOnly(), "Should be rollback by default");
    }

    @Test
    public void testNotRollbackIfShouldCommit() {
        TransactionOutcome outcome = new TransactionOutcome(null, null);
        outcome.shouldCommit();
        Assert.assertEquals(false, outcome.isRollbackOnly());
    }

    @Test
    public void testRollbackIfCommitThenLaterOnDecideToRollback() {
        TransactionOutcome outcome = new TransactionOutcome(null, null);
        outcome.shouldCommit();
        outcome.shouldRollback();
        outcome.shouldCommit();
        Assert.assertEquals(true, outcome.isRollbackOnly());
    }
}
