package com.opensymphony.able.upgrade;

import org.apache.commons.dbcp.BasicDataSource;

public interface UpgradeTask {
    void execute() throws UpgradeException;

    void setDataSource(BasicDataSource dataSource);

    void setBuildNumber(long build);
}
