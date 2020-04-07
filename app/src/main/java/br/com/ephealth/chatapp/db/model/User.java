package br.com.ephealth.chatapp.db.model;

import br.com.ephealth.chatapp.db.IUser;

public class User implements IUser {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String normalizedName;


    public User() {
    }

    public User(String id, String username, String imageURL, String status, String normalizedName) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.normalizedName = normalizedName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
}
