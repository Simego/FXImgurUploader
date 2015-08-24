package com.thesimego.fximguruploader.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thesimego.framework.jfx.entity.GenericEN;
import com.thesimego.framework.sqlite.ORMLite;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author drafaelli
 */
@DatabaseTable(tableName = "preferences")
public class PreferencesEN extends GenericEN {

    public static final Dao<PreferencesEN, Long> dao = DaoManager.lookupDao(ORMLite.getConnection(), PreferencesEN.class);

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Long id;
    
    @DatabaseField
    private Long loggedAccountId;
    
    @Override
    public Dao getDao() {
        return dao;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoggedAccountId() {
        return loggedAccountId;
    }

    public void setLoggedAccountId(Long loggedAccountId) {
        this.loggedAccountId = loggedAccountId;
    }

    public static PreferencesEN get() {
        try {
            return PreferencesEN.dao.queryForFirst(PreferencesEN.dao.queryBuilder().prepare());
        } catch (SQLException ex) {
            Logger.getLogger(PreferencesEN.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
