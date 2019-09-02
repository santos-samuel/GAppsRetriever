package com.example.mvpexample.updater;

import java.util.List;

public class DeviceSpecs {

    private final int deviceApi;
    private final String release;
    private final String[] supportedABIS;

    public DeviceSpecs(int deviceApi, String release, String[] supportedABIS, String installedGPSVersionName, int installedGPSVersionCode) {
        this.deviceApi = deviceApi;
        this.release = release;
        this.supportedABIS = supportedABIS;
    }

    public String getRelease() {
        return release;
    }

    public int getDeviceApi() {
        return deviceApi;
    }

    public String[] getSupportedABIS() {
        return supportedABIS;
    }

    public boolean supports(List<String> arches, int minapi) {
        if (arches == null || minapi == -1)
            return false;

        if (arches.get(0).equals("noarch") || arches.get(0).equals("universal"))
            return deviceApi >= minapi;

        else {
            for (String apkArch : arches) {
                for (String supportedArch : supportedABIS) {
                    if (apkArch.equals(supportedArch)) {
                        return deviceApi >= minapi;
                    }
                }
            }
        }

        return false;
    }

    public boolean supports(List<String> arches, String releaseVersion) {
        if (arches == null || releaseVersion == null)
            return false;

        if (arches.get(0).equals("noarch") || arches.get(0).equals("universal"))
            return isDeviceReleaseHigherOrEqualToApkMinRelease(releaseVersion);

        else {
            for (String apkArch : arches) {
                for (String supportedArch : supportedABIS) {
                    if (apkArch.equals(supportedArch)) {
                        return isDeviceReleaseHigherOrEqualToApkMinRelease(releaseVersion);
                    }
                }
            }
        }

        return false;
    }

    private boolean isDeviceReleaseHigherOrEqualToApkMinRelease(String releaseVersion) {
        // we need thisRelease >= apkMinRelease
        String[] thisRelease = this.release.split("\\.");
        String[] apkMinRelease = releaseVersion.split("\\.");

        int len = Math.min(thisRelease.length, apkMinRelease.length);

        for (int i = 0; i < len; i++) {
            int r1 = Integer.parseInt(thisRelease[i]);
            int r2 = Integer.parseInt(apkMinRelease[i]);

            if (r1 > r2)
                return true;
            if (r1 == r2)
                continue;
            if (r1 < r2)
                return false;
        }
        return true; // always continued (equal version)
    }
}