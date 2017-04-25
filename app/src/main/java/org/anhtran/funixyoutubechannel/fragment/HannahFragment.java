package org.anhtran.funixyoutubechannel.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.anhtran.funixyoutubechannel.developer.DeveloperKey;
import org.anhtran.funixyoutubechannel.R;
import org.anhtran.funixyoutubechannel.adapter.VideoListAdapter;
import org.anhtran.funixyoutubechannel.preferences.Constants;
import org.anhtran.funixyoutubechannel.video.Video;
import org.anhtran.funixyoutubechannel.video.VideoList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HannahFragment extends Fragment
        implements YouTubePlayer.OnInitializedListener {
    private static final String LOG_TAG = HannahFragment.class.getSimpleName();
    private static final String DEFAULT_VIDEO_ID = "iYKmxxIzEiQ";
    private String mCurrentVideoId;
    private String mCurrentTitle;
    private YouTubePlayer mPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ListView mHannahDiaryList;
    private ArrayList<Video> mVideoList;

    private ArrayList<String> mWatchedList;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the root view of fragment
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        // Initialize new instance of Youtube player support fragment
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        // If user visible hint is true,
        if (getUserVisibleHint()) {
            // Create a new transaction for Youtube player fragment to make it on top of container view
            // Youtube Player Fragment is a child fragment of root fragment
            // so we need to call getChildFragmentManager method for Fragment transaction
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

            // Initialize Youtube Player Fragment with Developer Key
            youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        // Create the list view of videos
        mHannahDiaryList = (ListView) rootView.findViewById(R.id.video_list);

        // Create the video list
        mVideoList = VideoList.getHannahDiaryList();
        // Create list view adapter
        VideoListAdapter adapter = new VideoListAdapter(getContext(), mVideoList);
        // Set adapter to list view
        mHannahDiaryList.setAdapter(adapter);

        // Get shared mPreferences
        preferences = getContext().getSharedPreferences(
                Constants.PREFERENCE_WATCHED,
                Constants.PRIVATE_MODE);


        setVideoListOnItemClickListener();

        return rootView;
    }

    /**
     * This method is to set listener when on video item click
     */
    public void setVideoListOnItemClickListener() {
        mHannahDiaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentVideoId = mVideoList.get(position).getVideoId();
                mCurrentTitle = mVideoList.get(position).getVideoText();
                playVideo(getVideoId());
                Log.e(LOG_TAG, mCurrentVideoId + " " + mCurrentTitle);

                // If watched list is not contains current video ID,
                // then add video ID to watched list
                // and put video title to preference with key is video ID
                if (!mWatchedList.contains(mCurrentVideoId)) {
                    mWatchedList.add(mCurrentVideoId);
                    saveToPreference(mWatchedList, mCurrentVideoId, mCurrentTitle);
                }
            }
        });
    }

    /**
     * The method is to check if Youtube player fragment view is visible to user
     * This method also help the player is not auto pause after 1 second
     * because a view is overlay on it
     *
     * @param isVisibleToUser This variant is true if the view visible to user, otherwise is false
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // Set the user visible hint
        super.setUserVisibleHint(isVisibleToUser);
        // If the Youtube player fragment is not directly visible and player is not null
        // then remove fragment transaction of Youtube player fragment
        if (!isVisibleToUser && mPlayer != null) {
            mPlayer.release();

            try {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(youTubePlayerFragment).commit();
            } catch (IllegalStateException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        // If the fragment is on top of view and the player fragment is not null
        // then add fragment transaction
        if (isVisibleToUser && youTubePlayerFragment != null) {

            // If the fragment is added then return,
            // this prevents app to be crashed in landscape mode
            if (youTubePlayerFragment.isAdded()) {
                return;
            }

            // Create a new transaction for Youtube player fragment to make it on top of container view
            try {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
                youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
            } catch (IllegalStateException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
            getWatchedList();
        }
    }

    /**
     * This method defines the action when Youtube Player is successful initialized
     *
     * @param provider      The Youtube player provider
     * @param youTubePlayer The Youtube player
     * @param wasSuccessful This parameter is true if player was successful initialized
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean wasSuccessful) {
        // If the player was successful initialized
        // then assign Youtube player parameter to global player and play video
        if (!wasSuccessful) {
            mPlayer = youTubePlayer;
            playVideo(getVideoId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        // YouTube error
        String errorMessage = error.toString();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG, errorMessage);

    }


    /**
     * This method is to get current playing video ID
     *
     * @return Return the current video ID
     */
    public String getVideoId() {
        return mCurrentVideoId;
    }

    /**
     * This method is to set video ID to be the current video
     *
     * @param id This parameter is the new video ID
     */
    public void setVideoId(String id) {
        mCurrentVideoId = id;
    }

    /**
     * This method is to play the current video
     */
    private void playVideo(String id) {
        if (id == null) {
            setVideoId(DEFAULT_VIDEO_ID);
        } else {
            setVideoId(id);
        }
        try {
            mPlayer.cueVideo(getVideoId());
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    /**
     * This method is to save watched video IDs list to shared mPreferences
     */
    private void saveToPreference(List<String> watchedList, String currentVideoId, String currentTitle) {
        Log.e(LOG_TAG, "watched list = " + watchedList);

        SharedPreferences.Editor pref_editor = preferences.edit();

        pref_editor.putString(currentVideoId, currentTitle);

        Set<String> videos = new HashSet<String>();
        videos.addAll(watchedList);

        if (preferences.contains(Constants.KEY_VIDEO_ID_SET)) {
            pref_editor.remove(Constants.KEY_VIDEO_ID_SET);
        }

        pref_editor.putStringSet(Constants.KEY_VIDEO_ID_SET, videos);
        pref_editor.commit();
    }

    private void getWatchedList() {
        // Get saved video ID list from shared preferences
        Set<String> videoSet = preferences.getStringSet(Constants.KEY_VIDEO_ID_SET, null);
        Log.e(LOG_TAG, "videoSet = " + videoSet);
        if (videoSet != null) {
            mWatchedList = new ArrayList<>(videoSet);
        } else {
            mWatchedList = new ArrayList<>();
        }
    }
}
