/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
public class MyLogger {
    
    
    Logger logger;

    public MyLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }
    public void info(String message){
        this.logger.info(message);
    }
    public void warning(String message){
        this.logger.warn(message);
    }
    public void error(String message){
        this.logger.error(message);
    }
    public void debug(String message){
        this.logger.debug(message);
    }
    
    
}
