package com.example.mvpexample.model;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class RequestManager {

    private final PackageManager packageManager;
    private final ContentResolver contentResolver;
    private PersistentMemory memory;

    public RequestManager(PersistentMemory persistentMemory, PackageManager packageManager, ContentResolver contentResolver) {
        this.memory = persistentMemory;
        this.packageManager = packageManager;
        this.contentResolver = contentResolver;
    }

    public int askForNextColor(int colorID) {
        return memory.getNextColor(colorID);
    }

    public String askForNextString(String oldString) {
        return memory.getNextString(oldString);
    }

    public int retrieveLastColor() {
        return memory.getLastGivenColor();
    }

    public String retrieveLastString() {
        return memory.getLastGivenString();
    }

    public void doesThisAppRequireGooglePlayServices(Uri apkURI) {
        String realPath = getRealPathFromURI(apkURI);

        /*PackageInfo info = packageManager.getPackageArchiveInfo(selectedAPK.getAbsolutePath(), PackageManager.GET_META_DATA);

        if (info == null) {
            throw new IllegalStateException("Extension APK could not be parsed");
        }*/
        /*ApplicationInfo info = packageManager.getApplicationInfo(PACKAGENAME, PackageManager.GET_META_DATA);

        Bundle metaData = info.metaData;
        String string = metaData.getString("com.google.android.gms.version");
        int anInt = metaData.getInt("com.google.android.gms.version");
        Log.d("Test", "a:" + string);
        Log.d("Test", "int:" + anInt);*/
    }

    public String getRealPathFromURI(Uri contentUri) // NEEDS FIX Images, Media?
    {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(contentUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA }; //?

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);
        } catch (SecurityException e) {
            //Permission Denial
            e.printStackTrace();
        }
        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
