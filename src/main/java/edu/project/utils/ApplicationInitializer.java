package edu.project.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
            ConnectionManager.initPool();
            ScriptReader.initScripts();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.closeConnectionPool();
    }
}
