package org.anhtran.funixyoutubechannel.video;

/**
 * Created by anhtran on 4/13/17.
 */

public class Video {
    public static final String PUBLISHER = "FUNiX";
    private String videoText;
    private String videoId;

    public Video(String videoId, String videoText) {
        this.videoText = videoText;
        this.videoId = videoId;
    }

    public String getVideoText() {
        return videoText;
    }

    public String getVideoId() {
        return videoId;
    }

}
