package com.thesimego.fximguruploader.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thesimego.framework.jfx.entity.GenericEN;
import com.thesimego.framework.sqlite.ORMLite;
import com.thesimego.fximguruploader.tools.Locations;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Simego
 */
@DatabaseTable(tableName = "images")
public class ImageEN extends GenericEN<ImageEN> {
    
    public static final Dao<ImageEN, Long> dao = DaoManager.lookupDao(ORMLite.getConnection(), ImageEN.class);
    public final static String ID = "id";
    public final static String IMAGE_ID = "image_id";
    public final static String LINK = "link";
    public final static String TITLE = "title";
    public final static String DATE = "date";
    public final static String FILENAME = "filename";

    @DatabaseField(columnName = ID, generatedId = true, allowGeneratedIdInsert = true)
    private Long id;

    @DatabaseField(columnName = IMAGE_ID)
    private String imageId;
    
    @DatabaseField(columnName = LINK)
    private String link;

    @DatabaseField(columnName = TITLE, width = 128)
    private String title;

    @DatabaseField(columnName = DATE, canBeNull = false)
    private Date date;

    @DatabaseField(columnName = FILENAME, canBeNull = false)
    private String filename;
    
    public ImageEN() {
        // ORMLite
    }

    private Label label;

    @Override
    public Label getTableRowHoverNode() {
        if (label == null) {
            label = new Label();
//            grid = new GridPane();
//            grid.setHgap(10);
//            grid.setVgap(5);
            BufferedImage image;
            try {
                image = ImageIO.read(new File(Locations.thumbnail(filename)));
            } catch (IOException ex) {
                image = null;
            }
            
            if(image == null) {
                label.setText("Image not found");
            } else {
                WritableImage wi = new WritableImage(image.getWidth(), image.getHeight());
                SwingFXUtils.toFXImage(image, wi);
                ImageView imView = new ImageView(wi);
                label.setGraphic(imView);
            }
////            ColumnConstraints col1 = new ColumnConstraints();
////            col1.setFillWidth(true);
////            col1.setHalignment(HPos.RIGHT);
////            grid.getColumnConstraints().addAll(col1);
////            grid.add(LabelBuilder.create("DeleteHash:").get(), 0, 0);
////            grid.add(new Label(getDeletehash()), 1, 0);
////            grid.add(LabelBuilder.create("ID:").get(), 0, 1);
////            grid.add(new Label(getId().toString()), 1, 1);
        }
        return label;
    }

    @Override
    public Dao<ImageEN, Long> getDao() {
        return dao;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}