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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by anhtran on 4/17/17.
 */

public class WatchedFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    private static final String LOG_TAG = WatchedFragment.class.getSimpleName();
    private static final String DEFAULT_VIDEO_ID = "Hk8sgIFMe-E";
    private YouTubePlayer mPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment;

    private String mCurrentVideoId;
    private ListView watchedListView;
    private ArrayList<Video> mVideoList;

    private SharedPreferences preferences;

    public WatchedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if (getUserVisibleHint()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

            youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        // Get shared preferences
        preferences = getContext().getSharedPreferences(
                Constants.PREFERENCE_WATCHED,
                Constants.PRIVATE_MODE);

        mVideoList = new ArrayList<>();

        // Create the list view of videos
        watchedListView = (ListView) rootView.findViewById(R.id.video_list);
        // Create list view adapter
        VideoListAdapter adapter = new VideoListAdapter(getContext(), mVideoList);
        // Set adapter to list view
        watchedListView.setAdapter(adapter);
        setVideoListOnItemClickListener();

        return rootView;
    }

    /**
     * This method is to set listener when on video item click
     */
    public void setVideoListOnItemClickListener() {
        watchedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentVideoId = mVideoList.get(position).getVideoId();
                playVideo(getVideoId());
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
        // then add fragment transaction and create watched video list from shared preferences
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

            // Create video list from shared preferences
            Set<String> videoSet = preferences.getStringSet(Constants.KEY_VIDEO_ID_SET, null);
            Log.e(LOG_TAG, "videoSet = " + videoSet);
            if (videoSet != null) {
                List<String> watchedVideos = new ArrayList<>(videoSet);
                mVideoList.clear();

                // Add Video objects to video list with video id from the Set
                // and video title that was saved to preference with corresponding video ID.
                for (int i = 0; i < watchedVideos.size(); i++) {
                    String title = preferences.getString(watchedVideos.get(i), null);
                    mVideoList.add(new Video(watchedVideos.get(i), title));
                }
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            mPlayer = youTubePlayer;
            mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
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

    public String getVideoId() {
        return mCurrentVideoId;
    }

    public void setVideoId(String id) {
        mCurrentVideoId = id;
    }

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
        }
    }
}
