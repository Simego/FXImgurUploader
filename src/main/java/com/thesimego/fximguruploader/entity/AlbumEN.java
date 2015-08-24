package com.thesimego.fximguruploader.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thesimego.framework.jfx.entity.GenericEN;
import com.thesimego.framework.sqlite.ORMLite;

/**
 *
 * @author Simego
 */
@DatabaseTable(tableName = "albums")
public class AlbumEN extends GenericEN<AlbumEN> {

    public static final Dao<AlbumEN, Long> dao = DaoManager.lookupDao(ORMLite.getConnection(), AlbumEN.class);
    public final static String ID = "id";
    public final static String ALBUM_ID = "albumId";
    public final static String LINK = "link";
    public final static String TITLE = "title";
    
    @DatabaseField(columnName = ID, generatedId = true, allowGeneratedIdInsert = true)
    private Long id;

    @DatabaseField(columnName = ALBUM_ID)
    private String albumId;
    
    @DatabaseField(columnName = LINK)
    private String link;

    @DatabaseField(columnName = TITLE, width = 55)
    private String title;

    public AlbumEN() {
        // ORMLite
    }
    
    @Override
    public Dao<AlbumEN, Long> getDao() {
        return dao;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}