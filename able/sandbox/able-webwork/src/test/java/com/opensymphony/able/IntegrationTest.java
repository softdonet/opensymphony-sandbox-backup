package com.opensymphony.able;

import com.opensymphony.able.model.User;
import com.opensymphony.able.service.Application;
import com.opensymphony.able.service.UserService;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.AbstractSpringContextTests;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public abstract class IntegrationTest {
    protected String[] getConfigLocations() {
        return new String[]{"applicationContext.xml"};
    }

    protected BasicDataSource dataSource;
    protected Application application;
    protected UserService userService;

    public int getCount(String sql) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            ps.close();

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Assert.fail("Could not close connection");
                }
            }
        }
    }

    public List<Object> getValues(String sql) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();

            int count = rs.getMetaData().getColumnCount();
            ArrayList<Object> values = new ArrayList<Object>(count);
            for (int i = 0; i < count; i++) {
                values.add(rs.getObject(i + 1));
            }

            rs.close();
            ps.close();

            return values;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Assert.fail("Could not close connection");
                }
            }
        }
    }

    /**
     * Map of context keys returned by subclasses of this class, to
     * Spring Contexts. This needs to be static, as JUnit tests are
     * destroyed and recreated between running individual test methods.
     */
    private static Map<String, ConfigurableApplicationContext> contextKeyToContextMap = new HashMap<String, ConfigurableApplicationContext>();

    /**
     * Logger available to subclasses.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Set custom locations dirty. This will cause them to be reloaded
     * from the cache before the next test case is executed.
     * <p>Call this method only if you change the state of a singleton
     * bean, potentially affecting future tests.
     */
    protected void setDirty(String[] locations) {
        String keyString = contextKeyString(locations);
        ConfigurableApplicationContext ctx =
                contextKeyToContextMap.remove(keyString);
        if (ctx != null) {
            ctx.close();
        }
    }

    protected boolean hasCachedContext(Object contextKey) {
        //noinspection SuspiciousMethodCalls
        return contextKeyToContextMap.containsKey(contextKey);
    }

    /**
     * Subclasses can override this to return a String representation of
     * their contextKey for use in logging
     */
    protected String contextKeyString(Object contextKey) {
        if (contextKey instanceof String[]) {
            return StringUtils.arrayToCommaDelimitedString((String[]) contextKey);
        } else {
            return contextKey.toString();
        }
    }

    protected ConfigurableApplicationContext getContext(Object key) {
        ++this.loadCount;
        String keyString = contextKeyString(key);
        ConfigurableApplicationContext ctx =
                contextKeyToContextMap.get(keyString);
        if (ctx == null) {
            if (key instanceof String[]) {
                ctx = loadContextLocations((String[]) key);
            } else {
                ctx = loadContext(key);
            }
            contextKeyToContextMap.put(keyString, ctx);
        }
        return ctx;
    }


    /**
     * Subclasses can invoke this to get a context key for the given location.
     * This doesn't affect the applicationContext instance variable in this class.
     * Dependency Injection cannot be applied from such contexts.
     */
    protected ConfigurableApplicationContext loadContextLocations(String[] locations) {
        if (logger.isInfoEnabled()) {
            logger.info("Loading config for: " + StringUtils.arrayToCommaDelimitedString(locations));
        }
        return new ClassPathXmlApplicationContext(locations);
    }

    protected ConfigurableApplicationContext loadContext(Object key) {
        throw new UnsupportedOperationException("Subclasses may override this");
    }

    /**
     * Constant that indicates no autowiring at all.
     *
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_NO = 0;

    /**
     * Constant that indicates autowiring bean properties by name.
     *
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    /**
     * Constant that indicates autowiring bean properties by type.
     *
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;


    private boolean populateProtectedVariables = false;

    private int autowireMode = AUTOWIRE_BY_TYPE;

    private boolean dependencyCheck = true;

    /**
     * Application context this test will run against
     */
    protected ConfigurableApplicationContext applicationContext;

    protected String[] managedVariableNames;

    private int loadCount = 0;

    /**
     * Set whether to populate protected variables of this test case.
     * Default is "false".
     */
    public void setPopulateProtectedVariables(boolean populateFields) {
        this.populateProtectedVariables = populateFields;
    }

    /**
     * Return whether to populate protected variables of this test case.
     */
    public boolean isPopulateProtectedVariables() {
        return populateProtectedVariables;
    }

    /**
     * Set the autowire mode for test properties set by Dependency Injection.
     * <p>The default is "AUTOWIRE_BY_TYPE". Can be set to "AUTOWIRE_BY_NAME"
     * or "AUTOWIRE_NO" instead.
     *
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_NO
     */
    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    /**
     * Return the autowire mode for test properties set by Dependency Injection.
     */
    public int getAutowireMode() {
        return autowireMode;
    }

    /**
     * Set whether or not dependency checking should be performed
     * for test properties set by Dependency Injection.
     * <p>The default is "true", meaning that tests cannot be run
     * unless all properties are populated.
     */
    public void setDependencyCheck(boolean dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    /**
     * Return whether or not dependency checking should be performed
     * for test properties set by Dependency Injection.
     */
    public boolean isDependencyCheck() {
        return dependencyCheck;
    }

    /**
     * Return the current number of context load attempts.
     */
    public final int getLoadCount() {
        return loadCount;
    }


    /**
     * Called to say that the "applicationContext" instance variable is dirty and
     * should be reloaded. We need to do this if a test has modified the context
     * (for example, by replacing a bean definition).
     */
    public void setDirty() {
        setDirty(getConfigLocations());
    }


    @Configuration(beforeTestMethod = true)
    protected final void setUp() throws Exception {
        this.applicationContext = getContext(contextKey());

        if (isPopulateProtectedVariables()) {
            if (this.managedVariableNames == null) {
                initManagedVariableNames();
            }
            populateProtectedVariables();
        } else if (getAutowireMode() != AUTOWIRE_NO) {
            this.applicationContext.getBeanFactory().autowireBeanProperties(
                    this, getAutowireMode(), isDependencyCheck());
        }
    }

    /**
     * Return a key for this context. Usually based on config locations, but
     * a subclass overriding buildContext() might want to return its class.
     */
    protected Object contextKey() {
        return getConfigLocations();
    }

    protected void initManagedVariableNames() throws IllegalAccessException {
        LinkedList<String> managedVarNames = new LinkedList<String>();
        Class clazz = getClass();

        do {
            Field[] fields = clazz.getDeclaredFields();
            if (logger.isDebugEnabled()) {
                logger.debug("Found " + fields.length + " fields on " + clazz);
            }

            for (Field field : fields) {
                field.setAccessible(true);
                if (logger.isDebugEnabled()) {
                    logger.debug("Candidate field: " + field);
                }
                if (!Modifier.isStatic(field.getModifiers()) && Modifier.isProtected(field.getModifiers())) {
                    Object oldValue = field.get(this);
                    if (oldValue == null) {
                        managedVarNames.add(field.getName());
                        if (logger.isDebugEnabled()) {
                            logger.debug("Added managed variable '" + field.getName() + "'");
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Rejected managed variable '" + field.getName() + "'");
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        while (!clazz.equals(AbstractDependencyInjectionSpringContextTests.class));

        this.managedVariableNames = managedVarNames.toArray(new String[managedVarNames.size()]);
    }

    protected void populateProtectedVariables() throws IllegalAccessException {
        for (String varName : this.managedVariableNames) {
            Object bean;
            try {
                Field field = findField(getClass(), varName);
                bean = this.applicationContext.getBean(varName, field.getType());
                field.setAccessible(true);
                field.set(this, bean);
                if (logger.isDebugEnabled()) {
                    logger.debug("Populated field: " + field);
                }
            }
            catch (NoSuchFieldException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("No field with name '" + varName + "'");
                }
            }
            catch (NoSuchBeanDefinitionException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("No bean with name '" + varName + "'");
                }
            }
        }
    }

    private Field findField(Class clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        }
        catch (NoSuchFieldException ex) {
            Class superclass = clazz.getSuperclass();
            if (superclass != AbstractSpringContextTests.class) {
                return findField(superclass, name);
            } else {
                throw ex;
            }
        }
    }


    /**
     * Reload the context if it's marked as dirty.
     */
    @Configuration(afterTestMethod = true)
    protected final void tearDown() {
        try {
            application.dispose();
            setDirty();
        }
        catch (Exception ex) {
            logger.error("onTearDown error", ex);
        }
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected User createUser(String username, String email, String name, String password) {
        User user = new User(username, email, name, null);
        userService.create(user, password);
        return user;
    }

    protected User createUser(String username, String email, String name, String password, Date createDate, Date updateDate) {
        User user = new User(username, email, name, null);
        user.setCreationDate(createDate);
        user.setUpdateDate(updateDate);
        userService.create(user, password);
        return user;
    }

    protected User createDefaultUser() {
        return createUser("niko2416", "nick.hill@gmail.com", "Nick Hill", "password");
    }

    protected User createDefaultSecondUser() {
        return createUser("travhoang", "travhoang@yahoo.com", "Travis Hoang", "admin");
    }

    protected User createDefaultSpecialUser() {
        User user = new User("jhouse", "jhouse@revolition.net", "James House", null);
        userService.create(user, "password");
        return user;
    }
}
