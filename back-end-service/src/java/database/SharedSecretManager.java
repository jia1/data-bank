/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JeremyBachtiar
 */
public class SharedSecretManager {

    private static final String PROPS_FILENAME = "/database/sharedSecret.properties";
    private static String user;
    private static String admin;

    static {

        try {
            // Retrieve properties from connection.properties via the CLASSPATH
            // WEB-INF/classes is on the CLASSPATH
            InputStream is = SharedSecretManager.class.getResourceAsStream(PROPS_FILENAME);
            Properties props = new Properties();
            props.load(is);

            // load database connection details
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String dbName = props.getProperty("db.name");
            user = props.getProperty("ss.user");
            admin = props.getProperty("ss.admin");
            is.close();

        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to load '" + PROPS_FILENAME + "'.";

            System.out.println(message);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, message, ex);
            throw new RuntimeException(message, ex);
        }

    }


    /**
     * returns the admin of the shared secrety key
     * @return admin
     */

    public static String getSharedSecretKeyAdministrator() {
        return admin;
    }


    /**
     * returns the user of the shared secret key
     * @return user
     */

    public static String getSharedSecretKeyUser() {
        return user;
    }
}
