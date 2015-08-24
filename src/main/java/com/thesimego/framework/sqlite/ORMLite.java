package com.thesimego.framework.sqlite;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.thesimego.fximguruploader.entity.AccessTokenEN;
import com.thesimego.fximguruploader.entity.AlbumEN;
import com.thesimego.fximguruploader.entity.ImageEN;
import com.thesimego.fximguruploader.entity.PreferencesEN;
import com.thesimego.fximguruploader.tools.Locations;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simego
 */
public final class ORMLite {

    private static ConnectionSource connection;

    public static ConnectionSource getConnection() {
        return connection;
    }
    
    public static void openConnection() throws SQLException {
        connection = new JdbcConnectionSource("jdbc:sqlite:"+Locations.HOME+"\\imguruploader.db");
        
        TableUtils.createTableIfNotExists(connection, ImageEN.class);
        TableUtils.createTableIfNotExists(connection, AlbumEN.class);
        TableUtils.createTableIfNotExists(connection, AccessTokenEN.class);
        TableUtils.createTableIfNotExists(connection, PreferencesEN.class);
        
        DaoManager.registerDao(connection, DaoManager.createDao(connection, ImageEN.class));
        DaoManager.registerDao(connection, DaoManager.createDao(connection, AlbumEN.class));
        DaoManager.registerDao(connection, DaoManager.createDao(connection, AccessTokenEN.class));
        DaoManager.registerDao(connection, DaoManager.createDao(connection, PreferencesEN.class));
    }

    public static void closeConnection() throws SQLException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(ORMLite.class.getName()).log(Level.SEVERE, "Failed disconnecting from the Database..", e);
        }
    }

}