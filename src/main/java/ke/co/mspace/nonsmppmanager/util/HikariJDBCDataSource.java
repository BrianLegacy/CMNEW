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

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static Connection getConnection() {
        Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/dbSMS");
            config.setUsername("mysql");
            config.setPassword("mysql123");
            config.addDataSourceProperty("encrypt", "false");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setMaximumPoolSize(20);
            config.setConnectionTimeout(900000);
            config.setLeakDetectionThreshold(300000);
            config.setIdleTimeout(900000);
            config.setMaxLifetime(3600000);
            ds = new HikariDataSource(config);
            con=ds.getConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HikariJDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

}
