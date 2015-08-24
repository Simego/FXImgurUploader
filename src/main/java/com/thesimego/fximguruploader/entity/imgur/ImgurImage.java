package com.thesimego.fximguruploader.entity.imgur;

import flexjson.JSON;
import flexjson.factories.DateObjectFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ImgurImage {

    private String id;
    private String title;
    private String description;
    
    @JSON(objectFactory = DateObjectFactory.class)
    private Date datetime;
    
    private String type;
    private Boolean animated;
    private Integer width;
    private Integer height;
    private Integer size;
    private Integer views;
    private Integer bandwidth;
    private String deletehash;
    private String name;
    private String section;
    private String link;
    private String gifv;
    private String mp4;
    private String webm;
    private Boolean looping;
    private Boolean favorite;
    private Boolean nsfw;
    
    private String vote;
    
//    @JSON(name = "account_url")
//    private String accountUrl;
    
//    @JSON(name = "account_id")
//    private String accountId;
    
//    @Override
//    public String toString() {
//        StringBuilder stb = new StringBuilder();
//        if (getTitle() != null) {
//            stb.append("Title: ").append(getTitle()).append(", ");
//        }
//        if (getDescription() != null) {
//            stb.append("Description: ").append(getDescription()).append(", ");
//        }
//        stb.append("Size: ").append((getSize() / 1024)).append("kb, ");
//        stb.append("Width: ").append(getWidth()).append(", ");
//        stb.append("Height: ").append(getHeight()).append(", ");
//
//        stb.append("Uploaded at: ").append(getDateString()).append(".");
//        return stb.toString();
//    }
    
    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        long epochTime = ((long) getDatetime()) * 1000;
//        date.setTime(epochTime);
//        return sdf.format(date);
        return sdf.format(datetime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isAnimated() {
        return animated;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGifv() {
        return gifv;
    }

    public void setGifv(String gifv) {
        this.gifv = gifv;
    }

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String getWebm() {
        return webm;
    }

    public void setWebm(String webm) {
        this.webm = webm;
    }

    public Boolean isLooping() {
        return looping;
    }

    public void setLooping(Boolean looping) {
        this.looping = looping;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

//    public String getAccountUrl() {
//        return accountUrl;
//    }
//
//    public void setAccountUrl(String accountUrl) {
//        this.accountUrl = accountUrl;
//    }

//    public String getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}