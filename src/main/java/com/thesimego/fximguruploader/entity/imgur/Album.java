package com.thesimego.fximguruploader.entity.imgur;

import flexjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Album {

    private String id;

    private String title;

    private Object description;

    private Integer datetime;

    private String cover;

    @JSON(name = "account_url")
    private String accountUrl;

    private String privacy;

    private String layout;

    private Integer views;

    private String link;

    private String deletehash;

    @JSON(name = "images_count")
    private Integer imagesCount;

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

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public Integer getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(Integer imagesCount) {
        this.imagesCount = imagesCount;
    }

    public String getFullLink() {
        return "https://imgur.com/a/" + getId();
    }
    
//    @Override
//    public String toString() {
//        StringBuilder stb = new StringBuilder();
//        if (getTitle() != null) {
//            stb.append("Title: ").append(getTitle()).append(", ");
//        }
//        stb.append("Link: ").append((getFullLink())).append(".");
//        return stb.toString();
//    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
