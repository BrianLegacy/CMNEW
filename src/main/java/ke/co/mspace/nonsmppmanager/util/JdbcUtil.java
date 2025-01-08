/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Norrey Osako
 */

public class JdbcUtil implements Serializable{

    boolean DATA_CENTER_NEW = false;     //dc
    boolean DATA_CENTER_LIVE = false;     //gateway .5
    boolean DEBUG = true;              //local
    boolean DEV_SERVER = false;   //dev
    boolean GATEWAY = false;
    boolean WEBSITE = false;
    
    public String databaseType = "mysql";
    public String dbSMS = "dbSMS";
    public String alert = "alerts";
    public String dbTASK = "dbTASK";
    public String dbPAYMENTS = "dbPAYMENTS";
    public String dbUSSD = "dbUSSD";
    public String dbEMAIL = "dbEMAIL";

    public String dataCenterUser = "newsms";
    public String dataCenterTestUser = "datacenter";//second datacenter test server
//    public String dataCenterTestUser = "dashboard";
    public String localMySQLUser = "mysql";
//    public String clientManagerMySQLUser = "clientmanager
    public String clientManagerMySQLUser = "clientmanager";
    public String websiteUser = "dlr";
    
//    data center new
    public String dataCenterUserNew = "clientmanager";
    public String dataCenterPasswordNew="ClientManager@2024#";
    private final String dataCenterHostNew="192.168.10.49";
    
//    dev server
    public String devUser = "clientmanager";
    public String devPassword ="ClientManager@2024#";
    private final String devHost= "192.168.10.199";
    
    public String testMySQLUser = "test";
    public String testMySQLUser64 = "mysql";

    public String dataCenterPassword = "NewSms123#";
    
    public String dataCenterTestPassword = "DataCenter2023#";//second datacenter test server
//     public String dataCenterTestPassword = "Dashboard@2023#";
    public String localMySQLPassword = "mysql123";
    public String testMySQLPassword = "Mspace54#";
    public String testMySQLPassword64 = "mysql123";
//    public String clientManagerMySQLPassword = "DatabaseMspace54#";
    public String clientManagerMySQLPassword = "ClientManager123#";
    public String websitePassword = "dlr123";

    private final String dataCenterHost = "10.164.0.5";
    
    
    private final String dataCenterTestHost = "34.90.1.89";//second datacenter test server
//          private final String dataCenterTestHost = "192.168.10.44";
    private final String localMySQLHost = "127.0.0.1";
    private final String testMySQLHost = "10.164.0.5";
    private final String testMySQLHost64 = "192.168.1.51";
//    private final String clientManagerMySQLHost = "34.90.100.104";
    private final String clientManagerMySQLHost = "10.164.0.5";
    private final String websiteHost = "10.164.0.5";

    private final String mysqlOptions = "?zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&useUnicode=false";

    private final int portNumber = 3306;
    //             192.168.1.51
    //sms
    //sms1234

    public Connection getConnectionTodbSMS() {
        {
            Connection conn = null;
            //         DataCenter
            Properties localDataCenterProps = new Properties();
            localDataCenterProps.put("user", this.dataCenterUser);
            localDataCenterProps.put("password", this.dataCenterPassword);
            
//            Dev_Server
            Properties DevProperties = new Properties();
            DevProperties.put("user", this.devUser);
            DevProperties.put("password", this.devPassword);  
            
//            Data Center New
            Properties DataCenterPropsNew = new Properties();
            DataCenterPropsNew.put("user", this.dataCenterUserNew);
            DataCenterPropsNew.put("password", this.dataCenterPasswordNew);

//         local
            Properties localConnectionProps = new Properties();
            localConnectionProps.put("user", this.localMySQLUser);
            localConnectionProps.put("password", this.localMySQLPassword);

//             Test 
            Properties testConnectionProps = new Properties();
            testConnectionProps.put("user", this.testMySQLUser);
            testConnectionProps.put("password", this.testMySQLPassword);

            // server 64
            Properties testConnectionProps64 = new Properties();
            testConnectionProps64.put("user", this.testMySQLUser64);
            testConnectionProps64.put("password", this.testMySQLPassword64);

//            lIVE
            Properties liveConnectionProps = new Properties();
            liveConnectionProps.put("user", this.clientManagerMySQLUser);
            liveConnectionProps.put("password", this.clientManagerMySQLPassword);

//               WEB
            Properties websiteConnectionProps = new Properties();
            websiteConnectionProps.put("user", this.websiteUser);
            websiteConnectionProps.put("password", this.websitePassword);
//            

            if (this.databaseType.equals("mysql")) {
                try {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                    if (conn == null) {
//                     DataCenter
                        if (DATA_CENTER_LIVE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    localDataCenterProps);
                        }
                        
//                        DevServer
                        if (DEV_SERVER) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + devHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DevProperties);
                        }
                        
//                        Data center new
                        if(DATA_CENTER_NEW){
                              conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHostNew
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DataCenterPropsNew);
                        }
//                  
//                    Local
                        if (DEBUG) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + localMySQLHost
                                    + ":" + portNumber + "/" + dbSMS + mysqlOptions,
                                    localConnectionProps);
                        }
//Test

//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost
//                          + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
//                      testConnectionProps);
//                      test 64
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost64
//                          + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
//                      testConnectionProps64);
//Live
                        if (GATEWAY) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + clientManagerMySQLHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    liveConnectionProps);
                        }
//website
                        if (WEBSITE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + websiteHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    websiteConnectionProps);
                        }

                    }
                    if (conn != null) {
                        conn.setCatalog(this.dbSMS);
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (this.databaseType.equals("derby")) {

                try {
                    conn = DriverManager.getConnection("jdbc:" + databaseType + ":" + dbSMS, localConnectionProps);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return conn;
        }
    }

    public Connection getConnectionTodbEMAIL() {
        {

            Connection conn = null;
            //         DataCenter
            Properties localDataCenterProps = new Properties();
            localDataCenterProps.put("user", this.dataCenterUser);
            localDataCenterProps.put("password", this.dataCenterPassword);
            
//          Dev_Server
            Properties DevProperties = new Properties();
            DevProperties.put("user", this.devUser);
            DevProperties.put("password", this.devPassword); 
            
            //            Data Center New
            Properties DataCenterPropsNew = new Properties();
            DataCenterPropsNew.put("user", this.dataCenterUserNew);
            DataCenterPropsNew.put("password", this.dataCenterPasswordNew);

//         local
            Properties localConnectionProps = new Properties();
            localConnectionProps.put("user", this.localMySQLUser);
            localConnectionProps.put("password", this.localMySQLPassword);

//             Test 
            Properties testConnectionProps = new Properties();
            testConnectionProps.put("user", this.testMySQLUser);
            testConnectionProps.put("password", this.testMySQLPassword);

            //             server 64
            Properties testConnectionProps64 = new Properties();
            testConnectionProps64.put("user", this.testMySQLUser64);
            testConnectionProps64.put("password", this.testMySQLPassword64);

//            lIVE
            Properties liveConnectionProps = new Properties();
            liveConnectionProps.put("user", this.clientManagerMySQLUser);
            liveConnectionProps.put("password", this.clientManagerMySQLPassword);

//               WEB
            Properties websiteConnectionProps = new Properties();
            websiteConnectionProps.put("user", this.websiteUser);
            websiteConnectionProps.put("password", this.websitePassword);
//            

            if (this.databaseType.equals("mysql")) {
                try {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                    if (conn == null) {
//                     DataCenter
                        if (DATA_CENTER_LIVE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    localDataCenterProps);
                        }
                        
//                    DevServer
                        if (DEV_SERVER) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + devHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DevProperties);
                        }
                        
                          if(DATA_CENTER_NEW){
                              conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHostNew
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DataCenterPropsNew);
                        }
////                    

//                    Local
                        if (DEBUG) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + localMySQLHost
                                    + ":" + portNumber + "/" + dbEMAIL + mysqlOptions,
                                    localConnectionProps);
                        }
//Test

//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost
//                          + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
//                      testConnectionProps);
//                      test 64
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost64
//                          + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
//                      testConnectionProps64);
//Live
                        if (GATEWAY) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + clientManagerMySQLHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    liveConnectionProps);
                        }
//website
                        if (WEBSITE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + websiteHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    websiteConnectionProps);
                        }

                    }
                    if (conn != null) {
                        conn.setCatalog(this.dbEMAIL);
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (this.databaseType.equals("derby")) {

                try {
                    conn = DriverManager.getConnection("jdbc:" + databaseType + ":" + dbEMAIL, localConnectionProps);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return conn;
        }
    }

    public Connection getConnectionTodbTask() {
        {
            Connection conn = null;
            //DataCenterlocalMySQLUser

            Properties localDataCenterProps = new Properties();
            localDataCenterProps.put("user", this.dataCenterUser);
            localDataCenterProps.put("password", this.dataCenterPassword);
            
            //            Dev_Server
            Properties DevProperties = new Properties();
            DevProperties.put("user", this.devUser);
            DevProperties.put("password", this.devPassword);  
            
            //            Data Center New
            Properties DataCenterPropsNew = new Properties();
            DataCenterPropsNew.put("user", this.dataCenterUserNew);
            DataCenterPropsNew.put("password", this.dataCenterPasswordNew);

            //Local
            Properties localConnectionProps = new Properties();
            localConnectionProps.put("user", this.localMySQLUser);
            localConnectionProps.put("password", this.localMySQLPassword);
            //Test

            Properties testConnectionProps = new Properties();
            testConnectionProps.put("user", this.testMySQLUser);
            testConnectionProps.put("password", this.testMySQLPassword);

            //Test64
            Properties testConnectionProps64 = new Properties();
            testConnectionProps64.put("user", this.testMySQLUser64);
            testConnectionProps64.put("password", this.testMySQLPassword64);

            //Live
            Properties liveConnectionProps = new Properties();
            liveConnectionProps.put("user", this.clientManagerMySQLUser);
            liveConnectionProps.put("password", this.clientManagerMySQLPassword);

//               WEB
            Properties websiteConnectionProps = new Properties();
            websiteConnectionProps.put("user", this.websiteUser);
            websiteConnectionProps.put("password", this.websitePassword);

            if (this.databaseType.equals("mysql")) {
                try {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                    if (conn == null) {

                        //DataCenter
                        if (DATA_CENTER_LIVE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHost
                                    + ":" + portNumber + "/" + dbTASK + "?zeroDateTimeBehavior=convertToNull",
                                    localDataCenterProps);
                        }
                        
                        //  DevServer
                        if (DEV_SERVER) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + devHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DevProperties);
                        }
                        
                         if(DATA_CENTER_NEW){
                              conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHostNew
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DataCenterPropsNew);
                        }
//
//Local
                        if (DEBUG) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + localMySQLHost
                                    + ":" + portNumber + "/" + dbTASK + mysqlOptions,
                                    localConnectionProps);
                        }
//Test
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost
//                           + ":" + portNumber + "/" + dbTASK + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps);

//                             Test64
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost64 
//                           + ":" + portNumber + "/" + dbTASK + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps64);
//Live
                        if (GATEWAY) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + clientManagerMySQLHost
                                    + ":" + portNumber + "/" + dbTASK + "?zeroDateTimeBehavior=convertToNull",
                                    liveConnectionProps);
                        }
//WEB
                        if (WEBSITE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + websiteHost
                                    + ":" + portNumber + "/" + dbTASK + "?zeroDateTimeBehavior=convertToNull",
                                    websiteConnectionProps);
                        }
                    }
                    if (conn != null) {;
                        conn.setCatalog(this.dbTASK);
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (this.databaseType.equals("derby")) {
                try {
                    conn = DriverManager.getConnection("jdbc:" + databaseType + ":" + dbTASK, localConnectionProps);
                } catch (SQLException e) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, e);

                }
            }

            return conn;
        }
    }

    public Connection getConnectionTodbPAYMENT() {
        {
            Connection conn = null;

            //DataCenter
            Properties localDataCenterProps = new Properties();
            localDataCenterProps.put("user", this.dataCenterUser);
            localDataCenterProps.put("password", this.dataCenterPassword);
            
            //Dev_Server
            Properties DevProperties = new Properties();
            DevProperties.put("user", this.devUser);
            DevProperties.put("password", this.devPassword);  
            
             //Data Center New
            Properties DataCenterPropsNew = new Properties();
            DataCenterPropsNew.put("user", this.dataCenterUserNew);
            DataCenterPropsNew.put("password", this.dataCenterPasswordNew);

//            local
            Properties localConnectionProps = new Properties();
            localConnectionProps.put("user", this.localMySQLUser);
            localConnectionProps.put("password", this.localMySQLPassword);

            //Test
            Properties testConnectionProps = new Properties();
            testConnectionProps.put("user", this.testMySQLUser);
            testConnectionProps.put("password", this.testMySQLPassword);

            //Test64
            Properties testConnectionProps64 = new Properties();
            testConnectionProps64.put("user", this.testMySQLUser64);
            testConnectionProps64.put("password", this.testMySQLPassword64);

            //Live
            Properties liveConnectionProps = new Properties();
            liveConnectionProps.put("user", this.clientManagerMySQLUser);
            liveConnectionProps.put("password", this.clientManagerMySQLPassword);

            //               WEB
            Properties websiteConnectionProps = new Properties();
            websiteConnectionProps.put("user", this.websiteUser);
            websiteConnectionProps.put("password", this.websitePassword);

            if (this.databaseType.equals("mysql")) {
                try {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                    if (conn == null) {

//                        //Local
                        if (DATA_CENTER_LIVE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHost
                                    + ":" + portNumber + "/" + dbPAYMENTS + "?zeroDateTimeBehavior=convertToNull",
                                    localDataCenterProps);
                        }
                        
                        //DevServer
                        if (DEV_SERVER) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + devHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DevProperties);
                        }
                        
                         if(DATA_CENTER_NEW){
                              conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHostNew
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DataCenterPropsNew);
                        }

//                    //Local
                        if (DEBUG) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + localMySQLHost
                                    + ":" + portNumber + "/" + dbPAYMENTS + mysqlOptions,
                                    localConnectionProps);
                        }

//                 Test
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost 
//                           + ":" + portNumber + "/" + dbPAYMENTS + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps);  
//                             Test
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost64 
//                           + ":" + portNumber + "/" + dbPAYMENTS + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps64);
//Live
                        if (GATEWAY) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + clientManagerMySQLHost
                                    + ":" + portNumber + "/" + dbPAYMENTS + "?zeroDateTimeBehavior=convertToNull",
                                    liveConnectionProps);
                        }
//WEB
                        if (WEBSITE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + websiteHost
                                    + ":" + portNumber + "/" + dbPAYMENTS + "?zeroDateTimeBehavior=convertToNull",
                                    websiteConnectionProps);
                        }
                    }
                    if (conn != null) {;
                        conn.setCatalog(this.dbPAYMENTS);
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (this.databaseType.equals("derby")) {

                try {
                    conn = DriverManager.getConnection("jdbc:" + databaseType + ":" + dbPAYMENTS, localConnectionProps);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return conn;
        }
    }

    public Connection getConnectionTodbUSSD() {
        {
            Connection conn = null;
            //Data Center

            Properties localDataCenterProps = new Properties();
            localDataCenterProps.put("user", this.dataCenterUser);
            localDataCenterProps.put("password", this.dataCenterPassword);
            
            // Dev_Server
            Properties DevProperties = new Properties();
            DevProperties.put("user", this.devUser);
            DevProperties.put("password", this.devPassword);  
            
            //Data Center New
            Properties DataCenterPropsNew = new Properties();
            DataCenterPropsNew.put("user", this.dataCenterUserNew);
            DataCenterPropsNew.put("password", this.dataCenterPasswordNew);

            //Local props
            Properties localConnectionProps = new Properties();
            localConnectionProps.put("user", this.localMySQLUser);
            localConnectionProps.put("password", this.localMySQLPassword);

//            //Test
            Properties testConnectionProps = new Properties();
            testConnectionProps.put("user", this.testMySQLUser);
            testConnectionProps.put("password", this.testMySQLPassword);

            //Test64
            Properties testConnectionProps64 = new Properties();
            testConnectionProps64.put("user", this.testMySQLUser64);
            testConnectionProps64.put("password", this.testMySQLPassword64);
//
            //Live
            Properties liveConnectionProps = new Properties();
            liveConnectionProps.put("user", this.clientManagerMySQLUser);
            liveConnectionProps.put("password", this.clientManagerMySQLPassword);

            //               WEB
            Properties websiteConnectionProps = new Properties();
            websiteConnectionProps.put("user", this.websiteUser);
            websiteConnectionProps.put("password", this.websitePassword);

            if (this.databaseType.equals("mysql")) {
                try {
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                    if (conn == null) {

                        //  Data Center
                        if (DATA_CENTER_LIVE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHost
                                    + ":" + portNumber + "/" + dbUSSD + "?zeroDateTimeBehavior=convertToNull",
                                    localDataCenterProps);
                        }
                        
                        // DevServer
                        if (DEV_SERVER) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + devHost
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DevProperties);
                        }
                        
                          if(DATA_CENTER_NEW){
                              conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + dataCenterHostNew
                                    + ":" + portNumber + "/" + dbSMS + "?zeroDateTimeBehavior=convertToNull",
                                    DataCenterPropsNew);
                        }

                        //  Local
                        if (DEBUG) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + localMySQLHost
                                    + ":" + portNumber + "/" + dbUSSD + mysqlOptions,
                                    localConnectionProps);
                        }
////
// Test
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost
//                           + ":" + portNumber + "/" + dbUSSD + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps);

//                            Test64
//                    conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + testMySQLHost64 
//                           + ":" + portNumber + "/" + dbUSSD + "?zeroDateTimeBehavior=convertToNull",
//                          testConnectionProps64);
////
// Live
                        if (GATEWAY) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + clientManagerMySQLHost
                                    + ":" + portNumber + "/" + dbUSSD + "?zeroDateTimeBehavior=convertToNull",
                                    liveConnectionProps);
                        }
// WEB
                        if (WEBSITE) {
                            conn = DriverManager.getConnection("jdbc:" + databaseType + "://" + websiteHost
                                    + ":" + portNumber + "/" + dbUSSD + "?zeroDateTimeBehavior=convertToNull",
                                    websiteConnectionProps);
                        }
                    }
                    if (conn != null) {
                        conn.setCatalog(this.dbUSSD);
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (this.databaseType.equals("derby")) {

                try {
                    conn = DriverManager.getConnection("jdbc:" + databaseType + ":" + dbUSSD, localConnectionProps);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JdbcUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return conn;
        }
    }

    public static void closeConnection(Connection connArg) {
        try {
            if (connArg != null) {
                connArg.close();
                connArg = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public static void printSQLException(SQLException ex) {
        Logger logger = LoggerFactory.getLogger(JdbcUtil.class);
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(((SQLException) e).getSQLState()) == false) {
                    e.printStackTrace(System.err);
                    logger.error("SQLState: " + ((SQLException) e).getSQLState());
                    logger.error("Error Code: " + ((SQLException) e).getErrorCode());
                    logger.error("Message: " + e.getMessage());
                    Throwable t = ex.getCause();
                    while (t != null) {
                        t = t.getCause();
                    }
                }
            }
        }
    }

    public static boolean ignoreSQLException(String sqlState) {
        if (sqlState == null) {
            return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32")) {
            return true;
        }
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55")) {
            return true;
        }
        return false;
    }

    public static java.sql.Date convertUtilToSql(java.util.Date uDate) {

        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

}
