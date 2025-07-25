/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceApi;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author Norrey Osako
 */
public class Alpha {

    private Long id;
    private String name;
    private String username;
    private Alpha alpha;
    private Long userid;
    private List<Alpha> listAlphas;
    private String message;
    final JdbcUtil util = new JdbcUtil();

    Connection conn = null;
    private static final Logger LOG = Logger.getLogger(Alpha.class.getName());

    /**
     * Creates a new instance of Alpha
     */
    public Alpha() {
    }

    public Alpha(Long id, String name, String username, Long userid, String message) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.userid = userid;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Alpha getAlpha() {

        return alpha;
    }

    public void setAlpha(Alpha alpha) {
        this.alpha = alpha;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public List<Alpha> getListAlphas() {

        return listAlphas;
    }

    public void setListAlphas(List<Alpha> listAlphas) {
        this.listAlphas = listAlphas;
        //System.out.println("===nesh===" + listAlphas);
    }

    public void retrieveAlphas() {
        setListAlphas(listAlphas());
    }

    @Override
    public String toString() {
        return "Alpha{" + "id=" + id + ", name=" + name + ", username=" + username + ", alpha=" + alpha + ", userid=" + userid + '}';
    }
    
   
    public List<Alpha> listAlphas() {
        List<Alpha> alphas = null;
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("listAlphas");
            AlphaServiceApi alphaService = new AlphaServiceImpl();
            alphas = alphaService.getAllAlphanumerics(conn);
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

        return alphas;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void persistAlpha() {
        LOG.info("PERSIST ALPHA");
        try {
            conn = util.getConnectionTodbSMS();
            AlphaServiceImpl service = new AlphaServiceImpl();
            LOG.info("persistAlphas");
            String alphaType = service.getAlphanumericType(name, conn);
            service.persistAlpha(username, name, alphaType, conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

    }

    public Alpha loadAlphaByUsername(String username) {

        Alpha anAlpha = null;
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("loadAlphaByUsername");
            AlphaServiceImpl service = new AlphaServiceImpl();
            anAlpha = service.loadAlphanumericByUsername(username, conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return anAlpha;
    }

    public void loadAlphaByUsername() {

        setAlpha(loadAlphaByUsername(username));
    }

    public void saveOrUpdateAlpha() {
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("listAlphas");
            AlphaServiceImpl service = new AlphaServiceImpl();

            System.out.println("Username : " + username);
            System.out.println("Alpha : " + name);
            System.out.println("Id : " + id);
            String alphaype = service.getAlphanumericType(name, conn);
            if (id == 0) {

                service.persistAlpha(username, name, alphaype, conn);
                this.setMessage("User alpha saved successfully.");
            } else {

                service.updateAlpha(username, name, alphaype, conn);
                this.setMessage("User alpha updated successfully.");

            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

    }

    public void generateXSL() {
        AlphaServiceApi service = new AlphaServiceImpl();
        service.generateXSL();

    }

}
