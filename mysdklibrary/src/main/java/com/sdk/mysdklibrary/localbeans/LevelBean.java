package com.sdk.mysdklibrary.localbeans;

/**
 * Created by daven.liu on 2017/9/21 0021.
 */

public class LevelBean {
    private int appId;
    private int level;
    private String packageName;
    private String clientDate;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPackageName() {
        return packageName==null?"":packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClientDate() {
        return clientDate==null?"":clientDate;
    }

    public void setClientDate(String clientDate) {
        this.clientDate = clientDate;
    }
}
