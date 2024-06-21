
package org.mspace.clientmanager.api;

import java.sql.SQLException;


/**
 *
 * @author olal
 */
public class ApiException extends Exception{

    public ApiException(String message) {
        super(message);
    }
    
}
