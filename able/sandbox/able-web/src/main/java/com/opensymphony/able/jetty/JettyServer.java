package com.opensymphony.able.jetty;

import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;

import com.opensymphony.able.util.Log;

/**
 * To start a Jetty server used by the QuickStart application.
 */
public class JettyServer {
    static {
        System.setProperty("org.apache.commons.logging.Log", org.apache.commons.logging.impl.Jdk14Logger.class.getName());
        //Logger.getLogger("java.sql").setLevel(Level.FINE);
    }

    private static final Log LOG = new Log(JettyServer.class);

    public static void main(String[] args) {
        LOG.info("Starting Able...");

        try {
            Server server = new Server();
            SocketListener socketListener = new SocketListener();
            socketListener.setPort(8080);
            server.addListener(socketListener);
            WebApplicationContext ctx = new WebApplicationContext("src/main/webapp");

            ctx.setClassLoader(Thread.currentThread().getContextClassLoader());
            ctx.setContextPath("/");
            server.addContext(null, ctx);

            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
