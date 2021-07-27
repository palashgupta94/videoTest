package com.tdh.videotest.youtubeDataModel;

import java.util.Map;

public class VideoSnippet {

    private String publishedAt;
    private String channelId;
    private String title;
    private String description;
    private Map<String , Thumbnail> thumbnails;
    private String channelTitle;
    private String playlistId;
    private int position;
    private Map<String , String>resourceId;
    private String videoOwnerChannelTitle;
    private String videoOwnerChannelId;
    private String standardThumbnailUrl;

    public VideoSnippet() {
    }

    public VideoSnippet(String publishedAt, String channelId, String title, String description, Map<String, Thumbnail> thumbnails,
                        String channelTitle, String playlistId, int position, Map<String, String> resourceId) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
        this.playlistId = playlistId;
        this.position = position;
        this.resourceId = resourceId;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<String, Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<String, String> getResourceId() {
        return resourceId;
    }

    public String getStandardThumbnailUrl() {
        return standardThumbnailUrl;
    }

    public void setStandardThumbnailUrl(String standardThumbnailUrl) {
        this.standardThumbnailUrl = standardThumbnailUrl;
    }

    @Override
    public String toString() {
        return "VideoSnippet{" +
                "publishedAt='" + publishedAt + '\'' +
                ", channelId='" + channelId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnails=" + thumbnails +
                ", channelTitle='" + channelTitle + '\'' +
                ", playlistId='" + playlistId + '\'' +
                ", position=" + position +
                ", resourceId=" + resourceId +
                ", videoOwnerChannelTitle='" + videoOwnerChannelTitle + '\'' +
                ", videoOwnerChannelId='" + videoOwnerChannelId + '\'' +
                ", standardThumbnailUrl='" + standardThumbnailUrl + '\'' +
                '}';
    }

    public void setResourceId(Map<String, String> resourceId) {
        this.resourceId = resourceId;
    }

    public String getVideoOwnerChannelTitle() {
        return videoOwnerChannelTitle;
    }

    public void setVideoOwnerChannelTitle(String videoOwnerChannelTitle) {
        this.videoOwnerChannelTitle = videoOwnerChannelTitle;
    }

    public String getVideoOwnerChannelId() {
        return videoOwnerChannelId;
    }

    public void setVideoOwnerChannelId(String videoOwnerChannelId) {
        this.videoOwnerChannelId = videoOwnerChannelId;
    }
}
