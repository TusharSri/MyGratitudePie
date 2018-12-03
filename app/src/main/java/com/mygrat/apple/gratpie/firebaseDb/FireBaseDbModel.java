package com.mygrat.apple.gratpie.firebaseDb;

import java.io.Serializable;

public class FireBaseDbModel implements Serializable {
    private String moment;
    private String url;

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}