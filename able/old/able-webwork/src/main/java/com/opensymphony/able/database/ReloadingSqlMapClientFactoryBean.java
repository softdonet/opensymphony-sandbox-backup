package com.opensymphony.able.database;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

import org.springframework.core.io.Resource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Factory that returns a SqlMapClient that reloads its config file when
 * it is updated. This would typically be used in a development environment.
 * Use this class in place of the normal Spring SqlMapClientFactoryBean.
 * <p>
 * By default, all files in the same directory as the config file are checked.
 * If you only want to check the main config file, set the checkAllFiles
 * property to false.
 *
 * @author Steven Grimm (koreth@midwinter.com)
 */
public class ReloadingSqlMapClientFactoryBean
	extends SqlMapClientFactoryBean
	implements InvocationHandler
{
	private Logger _log = Logger.getLogger(getClass().getName());
	private SqlMapClient _rawClient;
	private SqlMapClient _proxyClient;
	private File _configFile;
	private long _lastCheckTime = 0;
	private boolean _checkForRefresh = true;
	private boolean _checkAllFiles = true;

	/**
	 * Returns true if one of the configuration files has been updated.
	 */
	private boolean needRefresh() {
		// Only check once a second to avoid zillions of file operations
		long now = System.currentTimeMillis();
		if (now - _lastCheckTime < 1000)
			return false;

		boolean needed = false;
		if (_configFile != null) {
			if (_checkAllFiles) {
				File[] files = _configFile.getParentFile().listFiles();
                for (File file : files) {
                    if (file.lastModified() >= _lastCheckTime) {
                        needed = true;
                        break;
                    }
                }
			}
			else if (_configFile.lastModified() >= _lastCheckTime) {
				needed = true;
			}
		}

		_lastCheckTime = now;
		return needed;
	}

	/**
	 * Handles calls to the client, intercepting them and checking to see if
	 * the underlying client object needs to be recreated due to a config
	 * file change.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable
	{
		if (_checkForRefresh && needRefresh()) {
			_log.info("Refreshing " + _configFile.toString());
			afterPropertiesSet();
		}
		try {
			return method.invoke(_rawClient, args);
		}
		catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	/**
	 * Returns the proxy object if we are checking for updates.
	 */
	public Object getObject() {
		if (_proxyClient == null || ! _checkForRefresh)
			return super.getObject();
		return _proxyClient;
	}

	/**
	 * Sets the configuration file location. We need to override this
	 * method because the superclass doesn't make the location available to
	 * subclasses.
	 */
	public void setConfigLocation(Resource configLocation) {
		super.setConfigLocation(configLocation);
		try {
			_configFile = configLocation.getFile();
		}
		catch (IOException e) {
			// Configuration file is in a jar or something, so stop checking it
			_checkForRefresh = false;
		}
	}

	/**
	 * Indicates that all the files in the main config file's directory
	 * should be checked for changes. If this is not set, only the config
	 * file itself is checked.
	 */
	public void setCheckAllFiles(boolean check) {
		_checkAllFiles = check;
	}

	/**
	 * Creates a proxy for the SqlMapClient that checks to see if a config file
	 * has been updated.
	 */
	private void createProxy() {
		_proxyClient = (SqlMapClient) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { SqlMapClient.class },
				this);
	}

	/**
	 * Sets up the client and wraps it in a proxy.
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		_rawClient = (SqlMapClient) super.getObject();
		createProxy();
		needRefresh();		// set up "last checked" timestamp
	}
}
