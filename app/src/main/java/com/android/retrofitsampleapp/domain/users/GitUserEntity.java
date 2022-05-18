package com.android.retrofitsampleapp.domain.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GitUserEntity implements Serializable {

    private int id;
    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("node_id")
    private String nodeId;

    public GitUserEntity() {

    }

    public GitUserEntity(int id, String name, String description, String avatarUrl) {
        this.id = id;
        this.login = name;
        this.nodeId = description;
        this.avatarUrl = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
