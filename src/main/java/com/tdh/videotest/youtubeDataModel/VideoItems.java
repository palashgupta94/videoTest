package com.tdh.videotest.youtubeDataModel;

import java.util.Map;

public class VideoItems {

    private String kind;
    private String eTag;
    private String id;
    private VideoSnippet snippet;
    private Map<String , String> contentDetails;

    public VideoItems() {
    }

    public VideoItems(String kind, String eTag, String id, VideoSnippet snippet, Map<String, String> contentDetails) {
        this.kind = kind;
        this.eTag = eTag;
        this.id = id;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoSnippet snippet) {
        this.snippet = snippet;
    }

    public Map<String, String> getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(Map<String, String> contentDetails) {
        this.contentDetails = contentDetails;
    }
}
