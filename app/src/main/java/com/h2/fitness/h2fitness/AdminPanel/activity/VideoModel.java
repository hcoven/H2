package com.h2.fitness.h2fitness.AdminPanel.activity;

/**
 * Created by HP on 10/1/2017.
 */

public class VideoModel {


    private String mFileName;
    private String mFilePath;

    public VideoModel() {
    }

    public VideoModel(String mFileName, String mFilePaht) {

        this.mFileName = mFileName;
        this.mFilePath = mFilePaht;
    }

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePaht) {
        this.mFilePath = mFilePaht;
    }
}
