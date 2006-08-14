package com.opensymphony.able.upgrade;

public class BasicUpgradeTask extends DatabaseUpgradeTask {
    public void execute() throws UpgradeException {
        runScript("upgrade.sql");
    }
}
