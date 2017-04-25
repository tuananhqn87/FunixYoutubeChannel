package org.anhtran.funixyoutubechannel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.anhtran.funixyoutubechannel.data.DbContract;
import org.anhtran.funixyoutubechannel.data.LoginDbHelper;
import org.anhtran.funixyoutubechannel.preferences.Constants;
import org.anhtran.funixyoutubechannel.utils.ActivityClasses;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private LoginDbHelper mDbHelper;

    private EditText mUserEditText;
    private EditText mPasswordEditText;
    private TextView mLoginButton;

    private String mUser;
    private String mPassword;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDbHelper = new LoginDbHelper(this);

        preferences = getSharedPreferences(Constants.PREFERENCE_WATCHED, Constants.PRIVATE_MODE);

        initializeWidgets();
        addBehaviors();
    }

    private void initializeWidgets() {
        mUserEditText = (EditText) findViewById(R.id.login_user);
        mPasswordEditText = (EditText) findViewById(R.id.login_password);
        mLoginButton = (TextView) findViewById(R.id.login_button);
    }

    private void addBehaviors() {
        setOnButtonClickListener(mLoginButton, ActivityClasses.CHANNEL_ACTIVITY_CLASS);
    }

    private void setOnButtonClickListener(View view, final Class activityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the inputted values before compare with database records
                getTextValues();

                if (isSuccessful(mUser, mPassword)) {
                    editor = preferences.edit();
                    editor.putBoolean(Constants.KEY_IS_LOGIN, Constants.LOGGED_IN);
                    editor.commit();
                    mDbHelper.close();
                    startAnActivity(LoginActivity.this, activityClass);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startAnActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        startActivity(intent);
        finish();
    }

    private void getTextValues() {

        mUser = mUserEditText.getText().toString().trim();

        mPassword = mPasswordEditText.getText().toString();

    }

    private boolean isSuccessful(String user, String password) {


        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DbContract.LoginEntry._ID,
                DbContract.LoginEntry.COLUMN_LOGIN_USER,
                DbContract.LoginEntry.COLUMN_LOGIN_PASSWORD
        };

        // Filter results WHERE "username" = mUser string
        String selection = DbContract.LoginEntry.COLUMN_LOGIN_USER + " = ?";
        String[] selectionArgs = {user};

        Cursor cursor = db.query(
                DbContract.LoginEntry.LOGIN_TABLE_NAME,   // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<String> users = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        int indexOfColumnUser =
                cursor.getColumnIndexOrThrow(DbContract.LoginEntry.COLUMN_LOGIN_USER);

        int indexOfColumnPassword =
                cursor.getColumnIndexOrThrow(DbContract.LoginEntry.COLUMN_LOGIN_PASSWORD);

        while (cursor.moveToNext()) {
            String existingUser = cursor.getString(indexOfColumnUser);
            String existingPassword = cursor.getString(indexOfColumnPassword);
            users.add(existingUser);
            passwords.add(existingPassword);
        }

        cursor.close();

        if (users.size() > 0 && passwords.size() > 0) {
            if (user.equals(users.get(0)) && password.equals(passwords.get(0))) {
                return true;
            }
        }

        return false;
    }
}
