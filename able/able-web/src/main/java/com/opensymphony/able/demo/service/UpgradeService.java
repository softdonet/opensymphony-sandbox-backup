package com.opensymphony.able.demo.service;

import com.opensymphony.able.demo.model.Build;
import com.opensymphony.able.service.Service;
import com.opensymphony.able.service.ServiceException;
import com.opensymphony.able.upgrade.BasicUpgradeTask;
import com.opensymphony.able.upgrade.UpgradeTask;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpgradeService extends Service implements BeanFactoryAware {
    private DataSource dataSource;
    private String basePackage;
    private BeanFactory beanFactory;
    private static long buildNumber;

    public static long getBuildNumber() {
        return buildNumber;
    }

    public UpgradeService() {
    }

    public void init() throws ServiceException {
        Build build;
        try {
            build = getCurrentBuild();
        } catch (SQLException e) {
            String msg = "Could not determine current build";
            log.severe(msg, e);
            throw new ServiceException(msg, e);
        }

        Class<? extends UpgradeTask> currentTaskClass = getUpgradeTaskClass(build);

        try {
            while (currentTaskClass != null) {
                UpgradeTask upgradeTask = currentTaskClass.newInstance();
                upgradeTask.setDataSource(dataSource);
                upgradeTask.setBeanFactory(beanFactory);
                long buildNumber = build.getBuild() + 1;
                upgradeTask.setBuildNumber(buildNumber);
                if (upgradeTask.execute()) {
                    log.info("Upgrade to build " + buildNumber + " SUCCESS");

                    // store the current upgrade info
                    incremementBuild(build);

                    // get the next upgrade task
                    currentTaskClass = getUpgradeTaskClass(getCurrentBuild());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            String msg = "Could not upgrade Able, failed on build " + build;
            log.severe(msg, e);
            throw new ServiceException(msg, e);
        }

        buildNumber = build.getBuild();
    }

    private void incremementBuild(Build build) throws SQLException {
        build.increment();
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE build SET build = " + build.getBuild());
        int rowsUpdated = ps.executeUpdate();
        ps.close();
        conn.close();

        if (1 != rowsUpdated) {
            log.severe("Only " + rowsUpdated + " rows updated when exactly one row was expected");
        }
    }

    private Build getCurrentBuild() throws SQLException {
        Build build;
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT build FROM build");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            build = new Build(rs.getLong(1));
        } else {
            build = new Build(0);
        }
        rs.close();
        ps.close();
        conn.close();

        return build;
    }

    private Class<? extends UpgradeTask> getUpgradeTaskClass(Build build) {
        try {
            String className = basePackage + ".UpgradeToBuild" + (build.getBuild() + 1);
            log.info("Looking up upgrade class " + className);
            return (Class<UpgradeTask>) getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return BasicUpgradeTask.class;
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setBeanFactory(BeanFactory factory) {
        this.beanFactory = factory;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
