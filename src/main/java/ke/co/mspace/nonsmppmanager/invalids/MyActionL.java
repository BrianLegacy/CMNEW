/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

/**
 *
 * @author developer
 */

public class MyActionL implements AjaxBehaviorListener {

    @Override
    public void processAjaxBehavior(AjaxBehaviorEvent abe) throws AbortProcessingException {
        System.out.println("test");    }

  
    
}
