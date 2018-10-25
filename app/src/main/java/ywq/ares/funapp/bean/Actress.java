package ywq.ares.funapp.bean;

import java.io.Serializable;

public class Actress implements Serializable {


    private String name;
    private String avatar;
    private String artworkListUrl;

    public Actress() {
    }

    public Actress(String name, String avatar, String artworkListUrl) {
        this.name = name;
        this.avatar = avatar;
        this.artworkListUrl = artworkListUrl;
    }

    public String getArtworkListUrl() {
        return artworkListUrl;
    }

    public void setArtworkListUrl(String artworkListUrl) {
        this.artworkListUrl = artworkListUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Actress{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", artworkListUrl='" + artworkListUrl + '\'' +
                '}';
    }
}
