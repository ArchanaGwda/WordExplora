package com.dsa.word.explora.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchResponse implements Serializable {

    private String title;

    private int pageId;

    private String thumbnail;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "title='" + title + '\'' +
                ", pageId=" + pageId +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
