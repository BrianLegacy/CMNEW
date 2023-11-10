/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.util;

/**
 *
 * @author mspace
 */
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hibernate.SessionFactory;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;

public class factoryclose implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SessionFactory factory = null;
        if (factory != null) {
            if (factory instanceof SessionFactoryImpl) {
                SessionFactoryImpl sf = (SessionFactoryImpl) factory;
                ConnectionProvider conn = (ConnectionProvider) sf.getCurrentSession();
                if (conn instanceof C3P0ConnectionProvider) {
                    ((C3P0ConnectionProvider) conn).stop();
                }
            }

            factory.close();

        }
        
    }
}
