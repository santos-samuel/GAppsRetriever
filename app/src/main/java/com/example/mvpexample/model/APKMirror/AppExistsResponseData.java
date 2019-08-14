package com.example.mvpexample.model.APKMirror;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppExistsResponseData
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SerializedName("pname")
    @Expose
    private String pname;

    @SerializedName("exists")
    @Expose
    private Boolean exists;

    @SerializedName("developer")
    @Expose
    private AppExistsResponseDeveloper developer;

    @SerializedName("app")
    @Expose
    private AppExistsResponseApp app;

    @SerializedName("release")
    @Expose
    private AppExistsResponseRelease release;

    @SerializedName("apks")
    @Expose
    private List<AppExistsResponseApk> apks = null;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getPname(
    ) {
        return pname;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setPname(
            String pname
    ) {
        this.pname = pname;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getExists(
    ) {
        return exists;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setExists(
            Boolean exists
    ) {
        this.exists = exists;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AppExistsResponseDeveloper getDeveloper(
    ) {
        return developer;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setDeveloper(
            AppExistsResponseDeveloper developer
    ) {
        this.developer = developer;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AppExistsResponseApp getApp(
    ) {
        return app;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setApp(
            AppExistsResponseApp app
    ) {
        this.app = app;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AppExistsResponseRelease getRelease(
    ) {
        return release;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setRelease(
            AppExistsResponseRelease release
    ) {
        this.release = release;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<AppExistsResponseApk> getApks(
    ) {
        return apks;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setApks(
            List<AppExistsResponseApk> apks
    ) {
        this.apks = apks;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}