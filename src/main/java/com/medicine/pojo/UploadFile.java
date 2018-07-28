package com.medicine.pojo;

import java.util.Date;

public class UploadFile {
    private Integer id;

    private Integer userId;

    private String userName;

    private String fileName;

    private String path;

    private Integer size;

    private Date createTime;

    private Date updateTime;

    public UploadFile(Integer id, Integer userId, String fileName, String path, Integer size, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UploadFile() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}