package com.itheamc.licensefinder.viewmodel;

import androidx.lifecycle.ViewModel;

import com.itheamc.licensefinder.models.License;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<License> licenseList;
    private List<License> tempList;
    private License license;

    // Getter and Setter for license List
    public List<License> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<License> licenseList) {
        if (this.licenseList == null) {
            this.licenseList = new ArrayList<>();
        }

        this.licenseList = licenseList;
    }


    // Getter and setter for filtered
    public List<License> getTempList() {
        return tempList;
    }

    public void setTempList(List<License> tempList) {
        if (this.tempList == null) {
            this.tempList = new ArrayList<>();
        }

        this.tempList = tempList;
    }

    // Getter and Setter for License
    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
