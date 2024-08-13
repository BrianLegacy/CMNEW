/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Alpnumeric;
import ke.co.mspace.nonsmppmanager.model.EmailUser;
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.model.UserProfile;
import ke.co.mspace.nonsmppmanager.model.creditRecord;

/**
 *
 * @author Norrey Osako
 */
public interface UserServiceApi {

    public void deleteReseller(UserController selected, Connection conn);

    public void deleteUser(UserController selected, Connection conn);

    public int UpdateUserMaxContacts(UserController currentItem, int MaxContacts, Date StartDate, Date EndDate, Connection conn);

    public List<UserController> getLastCreated(Connection conn, String name) throws SQLException;

    List<Alpha> getAgentAlphas(Connection conn, String user);

    List<UserController> getAllUsers(Connection conn, String name) throws SQLException;

    List<Alpnumeric> getUserAlphas(Connection conn, String name);

    List<creditRecord> getAllUsersCred(Connection conn, String name) throws SQLException;

    List<UserController> getAllUserPerAgent(Connection conn, String name) throws SQLException;

    void persistUser(UserController user, Connection conn) throws SQLException;

    void persistEmailUser(EmailUser user, Connection conn) throws SQLException;

    void persistUserAgent(UserController user, Connection conn) throws SQLException;

    void persistEmailUserAgent(EmailUser user, Connection conn) throws SQLException;

    public Date setEndDate();

    public int getUserId();

    public boolean authenticateUser(String username, String password);

    public boolean authenticateEmailUser(String username, String password);
//  public  ArrayList<EmailPricingTable> getPricingTable() throws SQLException;

    public void updateUser(UserController aThis, Connection conn) throws SQLException;

    public void updateUserWithoutGroup(UserController aThis, Connection conn) throws SQLException;

    public void updateEmailUser(EmailUser aThis, Connection conn) throws SQLException;

    public UserController loadCustomerByUsername(String selectedUsername, Connection conn) throws SQLException;

    public void generateXSL(Connection conn) throws SQLException;

    public void updatePassword(Connection conn, String password) throws SQLException;

    public Map<String, Object> simpleStatistics();

    public void updateCredits(String username, int smsCredits, Connection conn) throws SQLException;

    public void updateEmailCredits(String username, int smsCredits, Connection conn) throws SQLException;

    public boolean addEmailAgentCredits(String agent, int newBalace, Connection conn);

    public void updateAgentCredits(String agent, int current, int toDeduct, int newBalace, Connection conn);

    public void alterAgentCredits(String agent, int currentagentcredits, int currrentusercreds, int alter, Connection conn);

    public UserProfile refreshProfile(String user);

    public String loadScript(String username, String password);

    public BigInteger smsSumary(String agent);

    public void persistUssdCode(UserController aThis, Connection conn);

    public List<UserController> getAllEmailUsers(Connection conn, String userS);

}
