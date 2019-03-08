package com.endrawan.porosgrading;

import com.endrawan.porosgrading.Models.Division;

public class Config {
    public static final int ADMIN_LEVEL = 1;
    public static final int USER_LEVEL = 2;

    public static final String DB_ACTIVITIES = "activities";
    public static final String DB_USERS = "users";

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
