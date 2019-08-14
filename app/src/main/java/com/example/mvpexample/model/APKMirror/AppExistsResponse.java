package com.example.mvpexample.model.APKMirror;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppExistsResponse
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SerializedName("data")
    @Expose
    private List<AppExistsResponseData> data = null;

    @SerializedName("headers")
    @Expose
    private AppExistsResponseHeaders headers;

    @SerializedName("status")
    @Expose
    private Integer status;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<AppExistsResponseData> getData(
    ) {
        return data;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setData(
            List<AppExistsResponseData> data
    ) {
        this.data = data;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AppExistsResponseHeaders getHeaders(
    ) {
        return headers;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setHeaders(
            AppExistsResponseHeaders headers
    ) {
        this.headers = headers;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getStatus(
    ) {
        return status;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setStatus(
            Integer status
    ) {
        this.status = status;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
