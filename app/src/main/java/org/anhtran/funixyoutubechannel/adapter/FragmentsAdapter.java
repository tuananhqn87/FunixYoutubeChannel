package org.anhtran.funixyoutubechannel.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.anhtran.funixyoutubechannel.R;
import org.anhtran.funixyoutubechannel.fragment.BeginnersFragment;
import org.anhtran.funixyoutubechannel.fragment.HannahFragment;
import org.anhtran.funixyoutubechannel.fragment.WatchedFragment;


public class FragmentsAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public FragmentsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BeginnersFragment();
        } else if (position == 1) {
            return new HannahFragment();
        } else {
            return new WatchedFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.beginners);
        } else if (position == 1) {
            return mContext.getString(R.string.hannah_diary);
        } else {
            return mContext.getString(R.string.watched);
        }
    }
}
