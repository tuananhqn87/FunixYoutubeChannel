package org.anhtran.funixyoutubechannel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.anhtran.funixyoutubechannel.R;
import org.anhtran.funixyoutubechannel.video.Video;

import java.util.ArrayList;


/**
 * Created by anhtran on 4/17/17.
 */

public class VideoListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Video> mVideos;
    private LayoutInflater inflater;


    public VideoListAdapter(Context context, ArrayList<Video> videos) {
        mContext = context;
        mVideos = videos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public Object getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        // Get the current video in item's position
        Video currentVideo = (Video) getItem(position);

        View videoListItemView = convertView;
        if (videoListItemView == null) {
            videoListItemView = inflater.inflate(R.layout.video_list_item, parent, false);

            holder = new Holder();
            holder.videoTextView = (TextView) videoListItemView.findViewById(R.id.video_text_view);
            holder.authorTextView = (TextView) videoListItemView.findViewById(R.id.author_text_view);
            holder.thumbnail = (ImageView) videoListItemView.findViewById(R.id.thumbnail);

            videoListItemView.setTag(holder);

        } else {
            holder = (Holder) videoListItemView.getTag();
        }


        String imgUrl = "https://img.youtube.com/vi/" + currentVideo.getVideoId() + "/hqdefault.jpg";

        Picasso.with(mContext)
                .load(imgUrl)
                .into(holder.thumbnail);

        // Set the video text
        holder.videoTextView.setText(currentVideo.getVideoText());

        // Set the video author name
        holder.authorTextView.setText(Video.PUBLISHER);


        return videoListItemView;
    }

    private static class Holder {
        TextView videoTextView, authorTextView;
        ImageView thumbnail;
    }

}
