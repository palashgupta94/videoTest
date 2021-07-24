package com.tdh.videotest.youtubeDataModel;

import java.util.Map;

public class VideoItem {

    private String kind;
    private String etag;
    private String id;
    private VideoSnippet snippet;
    private Map<String , String> contentDetails;

    public VideoItem() {
    }

    public VideoItem(String kind, String eTag, String id, VideoSnippet snippet, Map<String, String> contentDetails) {
        this.kind = kind;
        this.etag = eTag;
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

    public String getEtag() {
        return etag;
    }

    public void setEtag(String eTag) {
        this.etag = eTag;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", snippet=" + snippet +
                ", contentDetails=" + contentDetails +
                '}';
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
