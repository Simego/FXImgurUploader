package com.thesimego.fximguruploader.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thesimego.framework.jfx.entity.GenericEN;
import com.thesimego.framework.sqlite.ORMLite;
import flexjson.JSON;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author drafaelli
 */
@DatabaseTable(tableName = "accesstoken")
public class AccessTokenEN extends GenericEN<AccessTokenEN> {

// {"access_token":"554a335099f1b45dd9d8821351219e1fcd8fe549","expires_in":3600,"token_type":"bearer","scope":null,"refresh_token":"66f2fd8ffbf8a487121bbf0f8b1d2e67c8066322","account_id":9042594,"account_username":"Simego"}
    public static final Dao<AccessTokenEN, Long> dao = DaoManager.lookupDao(ORMLite.getConnection(), AccessTokenEN.class);
    public final static String ID = "id";
    public final static String ACCESS_TOKEN = "accessToken";
    public final static String EXPIRES_IN = "expiresIn";
    public final static String TOKEN_TYPE = "tokenType";
    public final static String REFRESH_TOKEN = "refreshToken";
    public final static String ACCOUNT_ID = "accountId";
    public final static String ACCOUNT_USERNAME = "accountUsername";
    public final static String LAST_UPDATE = "lastUpdate";

    @DatabaseField(columnName = ID, generatedId = true, allowGeneratedIdInsert = true)
    private Long id;
    
    @JSON(name = "access_token")
    @DatabaseField(columnName = ACCESS_TOKEN)
    private String accessToken;
    
    @JSON(name = "expires_in")
    @DatabaseField(columnName = EXPIRES_IN)
    private Long expiresIn;
    
    @JSON(name = "token_type")
    @DatabaseField(columnName = TOKEN_TYPE)
    private String tokenType;
    
//    private Object bearer;
//    private Object scope;
    
    @JSON(name = "refresh_token")
    @DatabaseField(columnName = REFRESH_TOKEN)
    private String refreshToken;
    
    @JSON(name = "account_id")
    @DatabaseField(columnName = ACCOUNT_ID)
    private Long accountId;
    
    @JSON(name = "account_username")
    @DatabaseField(columnName = ACCOUNT_USERNAME)
    private String accountUsername;
    
    @DatabaseField(columnName = LAST_UPDATE)
    private Date lastUpdate;

    public AccessTokenEN() {
        // ORMLite
    }

    @Override
    public Dao<AccessTokenEN, Long> getDao() {
        return dao;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public static AccessTokenEN getByAccountId(Long accountId) {
        try {
            return AccessTokenEN.dao.queryForFirst(AccessTokenEN.dao.queryBuilder().where().eq(ACCOUNT_ID, accountId).prepare());
        } catch (SQLException ex) {
            Logger.getLogger(PreferencesEN.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getAccountLink() {
        return "https://imgur.com/user/"+getAccountUsername();
    }
    
}
