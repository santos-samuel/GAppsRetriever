package com.example.mvpexample.model;

import java.util.List;

class DeviceSpecs {

    private final int osNumber;
    private final String[] supportedABIS;

    public DeviceSpecs(int osNumber, String[] supportedABIS) {
        this.osNumber = osNumber;
        this.supportedABIS = supportedABIS;
    }

    public int getOsNumber() {
        return osNumber;
    }

    public String[] getSupportedABIS() {
        return supportedABIS;
    }

    public boolean supports(List<String> arches, String minapi) {
        for (String apkArch : arches) {
            for (String supportedArch : supportedABIS) {
                if (apkArch.equals(supportedArch)) {
                    if (osNumber >= Integer.valueOf(minapi)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
