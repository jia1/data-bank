/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Theresa Lee
 */
public class ConnectionManager {

    private static final String PROPS_FILENAME = "/database/connection.properties";
    private static String dbUser;
    private static String dbPassword;
    private static String dbURL;

    static {
        readLocalDatabaseProperties();
        initDBDriver();
    }
    /**
    * connects database to the app
    *
    **/

    private static void readLocalDatabaseProperties() {
        
        try {
            // Retrieve properties from connection.properties via the CLASSPATH
            // WEB-INF/classes is on the CLASSPATH
            InputStream is = ConnectionManager.class.getResourceAsStream(PROPS_FILENAME);
            Properties props = new Properties();
            props.load(is);

            // load database connection details
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String dbName = props.getProperty("db.name");
            dbUser = props.getProperty("db.user");
            
            String username = System.getProperty("os.name");
            if (username.equals("Linux")) {
                // in production environment, use aws.db.password
                dbPassword = props.getProperty("aws.db.password");
            } else {
                // in local environment, use db.password
                dbPassword = props.getProperty("db.password");
            }

            dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
            is.close();
        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to load '" + PROPS_FILENAME + "'.";

            System.out.println(message);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, message, ex);
        }
         
    }
    /**
    * loads properties file
    *
    **/
    private static void initDBDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to find JDBC driver for MySQL.";

            System.out.println(message);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, message, ex);
        }
    }

    /**
     * establishes connection to database
     * @return Connection object
     * @throws SQLException
     **/
    public static Connection getConnection() throws SQLException {
        String message = "dbURL: " + dbURL
                + "  , dbUser: " + dbUser
                + "  , dbPassword: " + dbPassword;
        Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, message);

        return DriverManager.getConnection(dbURL, dbUser, dbPassword);

    }

    /**
     * closes connection to database
     * @param conn - connection
     * @param stmt - statement
     * @param rs - resultset
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close ResultSet", ex);
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close Statement", ex);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close Connection", ex);
        }
    }

    /**
     * closes connection to database
     * @param conn - connection
     * @param stmt - statement
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    /**
     * closes connection to database
     * @param conn - connection
     */
    public static void close(Connection conn) {
        close(conn, null, null);
    }

}
