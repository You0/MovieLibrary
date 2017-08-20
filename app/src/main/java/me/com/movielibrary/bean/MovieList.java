package me.com.movielibrary.bean;

import java.util.Date;

/**
 * Created by Me on 2017/8/6.
 */
public class MovieList {
    private String cover;
    private String title;
    private String actor;
    private String fh;
    private Date c_date;
    private int height;
    private int width;
    private float rating;

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public String getFh() {
        return fh;
    }
    public void setFh(String fh) {
        this.fh = fh;
    }
    public Date getC_date() {
        return c_date;
    }
    public void setC_date(Date c_date) {
        this.c_date = c_date;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "MovieListBean [cover=" + cover + ", tile=" + title + ", actor=" + actor + ", fh=" + fh + ", c_date="
                + c_date + "]";
    }

}