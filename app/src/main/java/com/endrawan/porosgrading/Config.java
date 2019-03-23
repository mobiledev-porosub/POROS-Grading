package com.endrawan.porosgrading;

import com.endrawan.porosgrading.Models.Division;

public class Config {
    public static final String DB_ACTIVITIES = "activities";
    public static final String DB_USERS = "users";
    public static final String DB_ACTIVITY_TYPES = "activity_types";

    public static final int MIN_LENGTH_EMAIL = 6;
    public static final int MIN_LENGTH_PASSWORD = 6;
    public static final int MIN_LENGTH_NIM = 15;
    public static final int MIN_LENGTH_NAME = 6;

    public static final int MIN_LENGTH_ACTIVITY_NAME = 6;
    public static final int MIN_LENGTH_ACTIVITY_DESC = 6;

    public static final int MIN_POINTS = 1500;

    public static final Division[] DIVISIONS = {
            new Division("Mobile",
                    R.drawable.ic_smartphone_primary_24dp,
                    R.drawable.ic_smartphone_white_24dp,
                    0),
            new Division("Web",
                    R.drawable.ic_web_primary_24dp,
                    R.drawable.ic_web_white_24dp,
                    1),
            new Division("OS",
                    R.drawable.ic_os_primary_24dp,
                    R.drawable.ic_os_white_24dp,
                    2),
            new Division("Security",
                    R.drawable.ic_security_primary_24dp,
                    R.drawable.ic_security_white_24dp,
                    3)
    };
}
