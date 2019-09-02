package com.example.mvpexample.model;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final int INSTALL_REQUEST_CODE = 51;
    public static String GOOGLE_PLAY_SERVICES_PACKAGE_NAME = "com.google.android.gms";
    public static String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";
    public static String GOOGLE_ACCOUNT_MANAGER_PACKAGE_NAME = "com.google.android.gsf.login";
    public static String GOOGLE_SERVICES_FRAMEWORK_PACKAGE_NAME = "com.google.android.gsf";
    public static String GOOGLE_CALENDAR_SYNC_PACKAGE_NAME = "com.google.android.syncadapters.calendar";
    public static String GOOGLE_CONTACT_SYNC_PACKAGE_NAME = "com.google.android.syncadapters.contacts";

    public static final String GPS_FILE_NAME = "gps.apk";
    public static final String GSF_FILE_NAME = "gfs.apk";
    public static final String GAM_FILE_NAME = "gam.apk";
    public static final String GPStore_FILE_NAME = "gpstore.apk";
    public static final String GContactSync_FILE_NAME = "gcontactsync.apk";
    public static final String GCalendarSync_FILE_NAME = "gcalendarsync.apk";

    public static final List<String> appsToInstallQuery = new ArrayList<String>() {{
        add("/?q=google-services-framework");
        add("/?q=google-account-manager");
        add("/?q=google-play-services");
        add("/?q=google-calendar-sync");
        add("/?q=google-contacts-sync");
        add("/?q=google-play-store");
    }};

    public static final List<String> appsToInstallFileName = new ArrayList<String>() {{
        add(GSF_FILE_NAME);
        add(GAM_FILE_NAME);
        add(GPS_FILE_NAME);
        add(GCalendarSync_FILE_NAME);
        add(GContactSync_FILE_NAME);
        add(GPStore_FILE_NAME);
    }};

    public static final List<String> appsPackageNames = new ArrayList<String>() {{
        add(GOOGLE_SERVICES_FRAMEWORK_PACKAGE_NAME);
        add(GOOGLE_ACCOUNT_MANAGER_PACKAGE_NAME);
        add(GOOGLE_PLAY_SERVICES_PACKAGE_NAME);
        add(GOOGLE_CALENDAR_SYNC_PACKAGE_NAME);
        add(GOOGLE_CONTACT_SYNC_PACKAGE_NAME);
        add(GOOGLE_PLAY_STORE_PACKAGE_NAME);
    }};
}
