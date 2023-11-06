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
public class IndexBean {
    
    private Long id;
    private String username;
    private String password;
    private String status;
    private String render;
    private static String page="/manager/newUser.jsp";
    

    /**
     * Creates a new instance of IndexBean
     */
    public IndexBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }
    

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
    
    
}
