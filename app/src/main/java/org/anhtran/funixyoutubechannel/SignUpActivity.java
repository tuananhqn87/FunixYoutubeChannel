package org.anhtran.funixyoutubechannel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.anhtran.funixyoutubechannel.data.DbContract;
import org.anhtran.funixyoutubechannel.data.LoginDbHelper;
import org.anhtran.funixyoutubechannel.utils.ActivityClasses;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private LoginDbHelper mDbHelper;

    private EditText mUserEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView mSignUpButton;

    private String mUser;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDbHelper = new LoginDbHelper(this);

        initializeWidgets();
        addBehaviors();
    }

    private void initializeWidgets() {
        mUserEditText = (EditText) findViewById(R.id.sign_up_user);
        mPasswordEditText = (EditText) findViewById(R.id.sign_up_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.sign_up_confirm_password);
        mSignUpButton = (TextView) findViewById(R.id.sign_up_button);
    }

    private void addBehaviors() {
        setOnButtonClickListener(mSignUpButton, ActivityClasses.LOGIN_ACTIVITY_CLASS);
    }

    private void setOnButtonClickListener(View view, final Class activityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTextValues();
                long newRowId = insertLoginRecord();
                if (newRowId != -1) {
                    startAnActivity(activityClass);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Create user failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startAnActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    private void getTextValues() {

        mUser = mUserEditText.getText().toString().trim();

        mPassword = mPasswordEditText.getText().toString();
        Log.e("mPassword is ", mPassword);

        mConfirmPassword = mConfirmPasswordEditText.getText().toString();
        Log.e("mConfirmPassword is ", mConfirmPassword);
    }

    private boolean areValuesValid() {
        if (mUser.isEmpty() || mUser == null
                || mPassword.isEmpty() || mPassword == null
                || mConfirmPassword.isEmpty() || mConfirmPassword == null) {
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_LONG).show();
            return false;
        }

        if (isUserExisting(mUser)) {
            Toast.makeText(this, "Username is not available", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!mPassword.equals(mConfirmPassword)) {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private long insertLoginRecord() {

        if (areValuesValid()) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.LoginEntry.COLUMN_LOGIN_USER, mUser);
            values.put(DbContract.LoginEntry.COLUMN_LOGIN_PASSWORD, mPassword);
            return db.insert(DbContract.LoginEntry.LOGIN_TABLE_NAME, null, values);
        }
        return -1;
    }

    private boolean isUserExisting(String user) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DbContract.LoginEntry._ID,
                DbContract.LoginEntry.COLUMN_LOGIN_USER,
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

        int indexOfColumnUser =
                cursor.getColumnIndexOrThrow(DbContract.LoginEntry.COLUMN_LOGIN_USER);
        while (cursor.moveToNext()) {
            String existingUser = cursor.getString(indexOfColumnUser);
            users.add(existingUser);
        }
        cursor.close();

        return users.contains(user);
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
