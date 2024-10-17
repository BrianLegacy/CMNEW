/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.util.regex.Pattern;
import javax.annotation.PostConstruct;

/**
 *
 * @author Samson
 */
public class TestingBean {

    /**
     * Creates a new instance of TestingBean
     */
    @javax.validation.constraints.Pattern(regexp ="^[0-9]*$" ,message = "bad deeds")
     private int x;
     private String message;
     
   
    
    public TestingBean() {
        
   
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TestingBean(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "TestingBean{" + "x=" + x + '}';
    }
    
     
}
