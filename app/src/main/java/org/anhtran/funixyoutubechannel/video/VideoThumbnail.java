package org.anhtran.funixyoutubechannel.video;

/**
 * Created by anhtran on 4/17/17.
 */

public class VideoThumbnail {

    private VideoThumbnail() {
    }

    public static String get(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/default.jpg";
    }

}
