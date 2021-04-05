package com.itheamc.licensefinder.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class License {
    private int _id;
    private String _name;
    private String _image;
    private String _license_no;
    private boolean _approved;
    private String _issuer;


    // Constructors
    public License() {
    }

    public License(int _id, String _name, String _image, String _license_no, boolean _approved, String _issuer) {
        this._id = _id;
        this._name = _name;
        this._image = _image;
        this._license_no = _license_no;
        this._approved = _approved;
        this._issuer = _issuer;
    }

    // Getters

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_image() {
        return _image;
    }

    public String get_license_no() {
        return _license_no;
    }

    public boolean is_approved() {
        return _approved;
    }

    public String get_issuer() {
        return _issuer;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "License{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _image='" + _image + '\'' +
                ", _license_no='" + _license_no + '\'' +
                ", _approved=" + _approved +
                ", _issuer='" + _issuer + '\'' +
                '}';
    }


    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        License license = (License) o;
        return get_id() == license.get_id() &&
                is_approved() == license.is_approved() &&
                get_name().equals(license.get_name()) &&
                Objects.equals(get_image(), license.get_image()) &&
                get_license_no().equals(license.get_license_no()) &&
                get_issuer().equals(license.get_issuer());
    }


    // DiffUtil.ItemCallback object of the Licence class
    public static DiffUtil.ItemCallback<License> licenseItemCallback = new DiffUtil.ItemCallback<License>() {
        @Override
        public boolean areItemsTheSame(@NonNull License oldItem, @NonNull License newItem) {
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull License oldItem, @NonNull License newItem) {
            return newItem.get_id() == oldItem.get_id() &&
                    newItem.get_name().equals(oldItem.get_name()) &&
                    newItem.get_image().equals(oldItem.get_image()) &&
                    newItem.get_license_no().equals(oldItem.get_license_no()) &&
                    newItem.get_issuer().equals(oldItem.get_issuer()) &&
                    newItem.is_approved() == oldItem.is_approved();
        }
    };
}
