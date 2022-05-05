package com.android.retrofitsampleapp.domain;

public class GitUserEntity {

    private int id;
    private String login;
    private String node_id;

    public GitUserEntity(int id, String name, String description) {
        this.id = id;
        this.login = name;
        this.node_id = description;
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

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }
}
