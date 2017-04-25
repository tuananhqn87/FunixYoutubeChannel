package org.anhtran.funixyoutubechannel.data;

import android.provider.BaseColumns;

/**
 * Created by anhtran on 4/23/17.
 */

public class DbContract {

    private DbContract() {
    }

    public static class LoginEntry implements BaseColumns {

        public static final String LOGIN_TABLE_NAME = "login";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_LOGIN_USER = "username";

        public static final String COLUMN_LOGIN_PASSWORD = "password";

        public static final String COLUMN_LOGIN_EMAIL = "email";
    }

}
