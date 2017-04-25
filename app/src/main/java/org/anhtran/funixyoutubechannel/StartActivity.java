package org.anhtran.funixyoutubechannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.anhtran.funixyoutubechannel.utils.ActivityClasses;

public class StartActivity extends AppCompatActivity {
    private static final String LOG_TAG = StartActivity.class.getSimpleName();
    private TextView loginButton;
    private TextView signUpButton;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeWidgets();
        addBehaviors();

    }

    private void initializeWidgets() {
        loginButton = (TextView) findViewById(R.id.start_login_button);
        signUpButton = (TextView) findViewById(R.id.start_signup_button);
        logo = (ImageView) findViewById(R.id.start_logo);

        // Use Picasso to load and resize the logo image, DO NOT set image directly to image view,
        // it's could cause app crashing
        Picasso.with(this)
                .load(R.drawable.xlogo)
                .resizeDimen(R.dimen.start_logo_width, R.dimen.start_logo_height)
                .into(logo);
    }

    private void addBehaviors() {
        setOnButtonClickListener(loginButton, ActivityClasses.LOGIN_ACTIVITY_CLASS);
        setOnButtonClickListener(signUpButton, ActivityClasses.SIGN_UP_ACTIVITY_CLASS);

    }

    private void setOnButtonClickListener(View view, final Class activityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnActivity(activityClass);
            }
        });
    }

    private void startAnActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
