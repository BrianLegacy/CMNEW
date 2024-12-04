/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amos
 */
public class HikariJDBCDataSource {

    static boolean DATA_CENTER_NEW = false;       //dc
    static boolean DEBUG = false;              //local
    static boolean DEV_SERVER = false;   //dev
    static boolean GATEWAY = false;
    static boolean WEBSITE = false;

    private static final Logger logger = Logger.getLogger(HikariJDBCDataSource.class.getName());

    private static HikariDataSource smsDataSource;
    private static HikariDataSource emailDataSource;
    private static HikariDataSource taskDataSource;
    private static HikariDataSource ussdDataSource;
    private static HikariDataSource paymentsDataSource;

    public static HikariDataSource initializeDataSource(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setMaximumPoolSize(10);
            config.setIdleTimeout(30000); // 30 seconds
            config.setMaxLifetime(1800000); // 30 minutes
            config.setConnectionTimeout(30000); // 30 seconds
            config.setLeakDetectionThreshold(20000); // 20 seconds

            return new HikariDataSource(config);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to innitialize datasoucre " + e);
            return null;
        }
    }

    private static HikariDataSource getSmsDataSource() {
        if (smsDataSource == null) {
            if (DEV_SERVER) {
                smsDataSource = initializeDataSource("jdbc:mysql://192.168.10.199/dbSMS", "clientmanager", "ClientManager@2024#");
            } else if (DEBUG) {
                smsDataSource = initializeDataSource("jdbc:mysql://localhost/dbSMS", "mysql", "mysql123");
            } else if (DATA_CENTER_NEW) {
                smsDataSource = initializeDataSource("jdbc:mysql://192.168.10.49/dbSMS", "clientmanager", "ClientManager@2024#");
            } else if (GATEWAY) {
                smsDataSource = initializeDataSource("jdbc:mysql://10.164.0.5/dbSMS", "newsms", "NewSms123#");
            }
        }
        return smsDataSource;
    }

    // initialization for dbTASK
    private static HikariDataSource getTaskDataSource() {
        if (taskDataSource == null) {
            if (DEV_SERVER) {
                taskDataSource = initializeDataSource("jdbc:mysql://192.168.10.199/dbTASK", "clientmanager", "ClientManager@2024#");
            } else if (DEBUG) {
                taskDataSource = initializeDataSource("jdbc:mysql://localhost/dbTASK", "mysql", "mysql123");
            } else if (DATA_CENTER_NEW) {
                taskDataSource = initializeDataSource("jdbc:mysql://192.168.10.49/dbTASK", "clientmanager", "ClientManager@2024#");
            } else if (GATEWAY) {
                taskDataSource = initializeDataSource("jdbc:mysql://10.164.0.5/dbTASK", "newsms", "NewSms123#");
            }
        }
        return taskDataSource;
    }

    // initialization for dbEMAIL
    private static HikariDataSource getEmailDataSource() {
        if (emailDataSource == null) {
            if (DEV_SERVER) {
                emailDataSource = initializeDataSource("jdbc:mysql://192.168.10.199/dbEMAIL", "clientmanager", "ClientManager@2024#");
            } else if (DEBUG) {
                emailDataSource = initializeDataSource("jdbc:mysql://localhost/dbEMAIL", "mysql", "mysql123");
            } else if (DATA_CENTER_NEW) {
                emailDataSource = initializeDataSource("jdbc:mysql://192.168.10.49/dbEMAIL", "clientmanager", "ClientManager@2024#");
            } else if (GATEWAY) {
                emailDataSource = initializeDataSource("jdbc:mysql://10.164.0.5/dbEMAIL", "newsms", "NewSms123#");
            }
        }
        return emailDataSource;
    }

    //  initialization for dbUSSD
    private static HikariDataSource getUSSDDataSource() {
        if (ussdDataSource == null) {
            if (DEV_SERVER) {
                ussdDataSource = initializeDataSource("jdbc:mysql://192.168.10.199/dbUSSD", "clientmanager", "Clientmanager@2024#");
            } else if (DEBUG) {
                ussdDataSource = initializeDataSource("jdbc:mysql://localhost/dbUSSD", "mysql", "mysql123");
            } else if (DATA_CENTER_NEW) {
                ussdDataSource = initializeDataSource("jdbc:mysql://192.168.10.49/dbUSSD", "clientmanager", "Clientmanager@2024#");
            } else if (GATEWAY) {
                ussdDataSource = initializeDataSource("jdbc:mysql://10.164.0.5/dbUSSD", "newsms", "NewSms123#");
            }
        }
        return ussdDataSource;
    }

    private static HikariDataSource getPaymentsDataSource() {
        if (paymentsDataSource == null) {
            if (DEV_SERVER) {
                paymentsDataSource = initializeDataSource("jdbc:mysql://192.168.10.199/dbPAYMENTS", "clientmanager", "ClientManager@2024#");
            } else if (DEBUG) {
                paymentsDataSource = initializeDataSource("jdbc:mysql://localhost/dbPAYMENTS", "mysql", "mysql123");
            } else if (DATA_CENTER_NEW) {
                paymentsDataSource = initializeDataSource("jdbc:mysql://192.168.10.49/dbPAYMENTS", "clientmanager", "ClientManager@2024#");
            } else if (GATEWAY) {
                paymentsDataSource = initializeDataSource("jdbc:mysql://10.164.0.5/dbPAYMENTS", "newsms", "NewSms123#");
            }
        }
        return paymentsDataSource;
    }

//        GET CONNECTIONS
    public static Connection getConnectionTodbSMS() {
        try {
            return getSmsDataSource().getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting connection to dbSMS", e);
            return null;
        }
    }

    public static Connection getConnectionTodbTask() {
        try {
            return getTaskDataSource().getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting connection to dbTASK", e);
            return null;
        }
    }

    public static Connection getConnectionTodbEMAIL() {
        try {
            return getEmailDataSource().getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting connection to dbEMAIL", e);
            return null;
        }
    }

    public static Connection getConnectionTodbUSSD() {
        try {
            return getUSSDDataSource().getConnection();
        } catch (SQLException e) {
            System.out.println("Error getting connection to dbUSSD");
            return null;
        }
    }

    public static Connection getConnectionTodbPAYMENT() {
        try {
            return getPaymentsDataSource().getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting connection to dbPayments", e);
            return null;
        }
    }

}
