package ua.george_nika.webtester.listeners;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ua.george_nika.webtester.dao.intface.RoleDao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by George on 20.06.2015.
 */
public class InitContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(InitContextListener.class);

    @Autowired
    RoleDao roleDao;

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext context = servletContextEvent.getServletContext();
        String contextPath = context.getContextPath();
        context.setAttribute("context", contextPath);

        LOGGER.info("Program has been started");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("Program has been destroyed");
    }
}
