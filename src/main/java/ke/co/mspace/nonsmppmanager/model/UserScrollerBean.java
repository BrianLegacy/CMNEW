/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import org.mspace.clientmanager.user.UserController;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.service.UserScroller;
import ke.co.mspace.nonsmppmanager.service.UserServiceApi;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author Norrey Osako
 */
public class UserScrollerBean {

    private UserController currentUser = new UserController();
    private Alpha alpha = new Alpha();
    JdbcUtil util = new JdbcUtil();
    Connection conn = null;
    private static final Logger LOG = Logger.getLogger(UserScrollerBean.class.getName());

    /**
     * Creates a new instance of UserScrollerBean
     */
    public UserScrollerBean() {
    }

    public UserController getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserController currentUser) {
        this.currentUser = currentUser;
    }

    public Alpha getAlpha() {
        return alpha;
    }

    public void setAlpha(Alpha alpha) {
        this.alpha = alpha;
    }

    public List<UserController> getUsers() {
        UserScroller sc = new UserScroller();
        List<UserController> users = null;
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("getUsers");
            UserServiceApi userService = new UserServiceImpl();
            users = userService.getAllUsers(conn, sc.userS);

            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return users;
    }

    public void saveOrUpdateAlpha() {

        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("saveOrUpdateAlpha");
            AlphaServiceImpl service = new AlphaServiceImpl();
            String username = currentUser.getUsername();
            String alphanumeric = currentUser.getAlphanumeric();
            Long id = currentUser.getAlphaId();

            System.out.println("Username : " + username);
            System.out.println("Alpha : " + alphanumeric);
            System.out.println("Id : " + id);
            String alphaType = service.getAlphanumericType(alphanumeric, conn);
            if (id == 0) {

                service.persistAlpha(username, alphanumeric, alphaType, conn);
                currentUser.setMessage("User alpha saved successfully.");
            } else {

                service.updateAlpha(username, alphanumeric,alphaType, conn);
                currentUser.setMessage("User alpha updated successfully.");

            }
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

    }

}
