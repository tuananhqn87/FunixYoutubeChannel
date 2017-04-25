package org.anhtran.funixyoutubechannel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.anhtran.funixyoutubechannel.adapter.FragmentsAdapter;
import org.anhtran.funixyoutubechannel.preferences.Constants;
import org.anhtran.funixyoutubechannel.utils.ActivityClasses;

public class ChannelActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChannelActivity.class.getSimpleName();
    SharedPreferences preferences;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get shared preferences
        preferences = getSharedPreferences(
                Constants.PREFERENCE_WATCHED, Constants.PRIVATE_MODE);

        // Check log in status
        boolean wasLoggedIn = checkLogIn();

        // If status is logged out then start StartActivity,
        // if status is logged in then initialize views
        if (wasLoggedIn == Constants.LOGGED_OUT) {
            Log.e(LOG_TAG, "Logged out - Start StartActivity");
            startAnActivity(ActivityClasses.START_ACTIVITY_CLASS);
        } else {
            setContentView(R.layout.activity_channel_main);
            // Initialize views
            initializeWidgets();

            // Add views' behavior, this line has to be put inside this else statement
            // if it's put outside it could cause app crashing
            addBehaviors();
        }


    }

    private void initializeWidgets() {
        //Create toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Set toolbar logo
        mToolbar.setNavigationIcon(R.drawable.xlogo_blue_48x48);

        //Initiate ViewPager object to create main activity with fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initiate TabLayout for activity, the activity is to have 2 tab for 2 fragment
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    private void addBehaviors() {

        //Initiate FragmentsAdapter object to get fragment view to main activity
        FragmentsAdapter adapter = new FragmentsAdapter(this, getSupportFragmentManager());

        //Set adapter to ViewPager
        viewPager.setAdapter(adapter);

        //Set up tab layout with ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the option menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // If "Log Out" is selected, set status to logged out and start StartActivity
            case R.id.menu_log_out:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Constants.KEY_IS_LOGIN, Constants.LOGGED_OUT);
                editor.commit();
                startAnActivity(ActivityClasses.START_ACTIVITY_CLASS);
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }

    /**
     * This method is to start an activity
     *
     * @param activityClass Class object of activity class
     */
    private void startAnActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    /**
     * This method is to check log in status
     *
     * @return
     */
    private boolean checkLogIn() {

        boolean isLoggedIn = false;

        if (preferences.contains(Constants.KEY_IS_LOGIN)) {
            isLoggedIn = preferences.getBoolean(Constants.KEY_IS_LOGIN, false);
        }

        return isLoggedIn;
    }

    /**
     * On back pressed from this activity then close app
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
