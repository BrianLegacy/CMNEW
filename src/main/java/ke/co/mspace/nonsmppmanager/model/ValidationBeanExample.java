/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author Norrey Osako
 */

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Ilya Shaikovsky
 *
 */
public class ValidationBeanExample {

    private String progressString="Fill the form please";
    
    @NotEmpty
    //@Pattern(regex=".*[^\\s].*", message="This string contain only spaces")
    @Length(min=3,max=12)
    private String name;
    @Email
    @NotEmpty
    private String email;
    
    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;
    
    public ValidationBeanExample() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public void success() {
        setProgressString(getProgressString() + "(Strored successfully)");
    }

    public String getProgressString() {
        return progressString;
    }

    public void setProgressString(String progressString) {
        this.progressString = progressString;
    }
}