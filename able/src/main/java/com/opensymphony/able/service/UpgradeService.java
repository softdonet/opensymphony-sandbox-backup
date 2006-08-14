package com.opensymphony.able.service;

import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.able.model.Build;
import com.opensymphony.able.upgrade.UpgradeTask;
import org.apache.commons.dbcp.BasicDataSource;

public class UpgradeService extends DaoService<Build> {
    private BasicDataSource dataSource;
    private static long buildNumber;

    public static long getBuildNumber() {
        return buildNumber;
    }

    public UpgradeService() {
        super(Build.class);
    }

    public void init() throws ServiceException {
        super.init();

        Build build = getCurrentBuild();

        Class<UpgradeTask> currentTaskClass = getUpgradeTaskClass(build);
        try {
            while (currentTaskClass != null) {
                UpgradeTask upgradeTask = currentTaskClass.newInstance();
                upgradeTask.setDataSource(dataSource);
                long buildNumber = build.getBuild() + 1;
                upgradeTask.setBuildNumber(buildNumber);
                log.info("Upgrading to build " + buildNumber + "...");
                upgradeTask.execute();
                log.info("Upgrade to build " + buildNumber + " SUCCESS");

                // store the current upgrade info
                incremementBuild(build);

                // get the next upgrade task
                currentTaskClass = getUpgradeTaskClass(getCurrentBuild());
            }
        } catch (Exception e) {
            throw new ServiceException("Could not upgrade application, failed on build " + build, e);
        }

        buildNumber = build.getBuild();
    }

    private void incremementBuild(Build build) {
        build.increment();
        int rowsUpdated = dao.update(build);
        if (1 != rowsUpdated) {
            log.severe("Only " + rowsUpdated + " rows updated when exactly one row was expected");
        }
    }

    private Build getCurrentBuild() {
        Build build;
        try {
            build = dao.selectSingle("build");
        } catch (DaoException e) {
            // there is no build table yet, assume build is 0 then
            build = new Build(0);
        }

        return build;
    }

    private Class<UpgradeTask> getUpgradeTaskClass(Build build) {
        try {
            String className = "com.opensymphony.able.upgrade.UpgradeToBuild" + (build.getBuild() + 1);
            log.info("Looking up upgrade class " + className);
            return ClassLoaderUtil.loadClass(className, getClass());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
