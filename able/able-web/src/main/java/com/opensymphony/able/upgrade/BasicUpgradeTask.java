package com.opensymphony.able.upgrade;

public class BasicUpgradeTask extends DatabaseUpgradeTask {
    public boolean execute() throws UpgradeException {
        return runScript("upgrade");
    }
}
