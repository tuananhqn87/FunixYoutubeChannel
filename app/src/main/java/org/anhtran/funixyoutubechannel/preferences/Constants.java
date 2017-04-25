package org.anhtran.funixyoutubechannel.preferences;

import android.content.Context;

/**
 * Created by anhtran on 4/21/17.
 */

public class Constants {
    // Name of Watched Videos Shared Preferences
    public static final String PREFERENCE_WATCHED = "Watched_Videos";
    // Mode private of shared preferences
    public static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    // Key of video ID set
    public static final String KEY_VIDEO_ID_SET = "video_ids";
    // Login check
    public static final String KEY_IS_LOGIN = "is_login";
    // Logged in value
    public static final boolean LOGGED_IN = true;
    // Logged out value
    public static final boolean LOGGED_OUT = false;

    private Constants() {
    }
}
