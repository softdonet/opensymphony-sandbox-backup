package com.opensymphony.able.upgrade;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;

public interface UpgradeTask {
	/**
	 * Upgrades to the current build number.
	 *
	 * @return true if this upgrade was successful.
	 */
    boolean execute() throws UpgradeException;

    void setDataSource(DataSource dataSource);

    /**
     * Sets the Spring bean factory. This may be used by upgrade classes
     * to obtain references to DAOs, etc.
     */
    void setBeanFactory(BeanFactory factory);

    void setBuildNumber(long build);
}
