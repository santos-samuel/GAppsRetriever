package com.example.mvpexample.updater;

import java.util.List;

public class DeviceSpecs {

    private final int osNumber;
    private final String release;
    private final String[] supportedABIS;
    private final String installedGPSVersionName;
    private final int installedGPSVersionCode;

    public DeviceSpecs(int osNumber, String release, String[] supportedABIS, String installedGPSVersionName, int installedGPSVersionCode) {
        this.osNumber = osNumber;
        this.release = release;
        this.supportedABIS = supportedABIS;
        this.installedGPSVersionName = installedGPSVersionName;
        this.installedGPSVersionCode = installedGPSVersionCode;
    }

    public String getRelease() {
        return release;
    }

    public String getInstalledGPSVersionName() {
        return installedGPSVersionName;
    }

    public int getOsNumber() {
        return osNumber;
    }

    public String[] getSupportedABIS() {
        return supportedABIS;
    }

    public int getInstalledGPSVersionCode() {
        return installedGPSVersionCode;
    }

    public boolean supports(List<String> arches, int minapi) {
        for (String apkArch : arches) {
            for (String supportedArch : supportedABIS) {
                if (apkArch.equals(supportedArch)) {
                    if (osNumber >= minapi) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean supports(List<String> arches, String releaseVersion) {
        for (String apkArch : arches) {
            for (String supportedArch : supportedABIS) {
                if (apkArch.equals(supportedArch)) {
                    if (isDeviceReleaseHigherOrEqualToApkMinRelease(releaseVersion))
                        return true;
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
        return true; // always continued
    }
}