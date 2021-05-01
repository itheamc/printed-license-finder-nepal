package com.itheamc.licensefinder.models;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class License {
    private int _id;
    private String _name;
    private String _image;
    private String _license_no;
    private boolean _approved;
    private String _issuer;
    private String _type;
    private String _sent_date;

    // Extra
    private String _citizenship_no;
    private String _dob_ad;
    private String _blood_group;
    private String _ward_number;
    private String _tole;
    private String _zone;
    private String _district;
    private String _municipality;
    private String _issue_date;
    private String _expiry_date;
    private String _category;


    // Constructors
    public License() {
    }

    public License(int _id, String _name, String _image, String _license_no, boolean _approved, String _issuer, String _type, String _sent_date) {
        this._id = _id;
        this._name = _name;
        this._image = _image;
        this._license_no = _license_no;
        this._approved = _approved;
        this._issuer = _issuer;
        this._type = _type;
        this._sent_date = _sent_date;
    }

    // Getters and Setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_license_no() {
        return _license_no;
    }

    public void set_license_no(String _license_no) {
        this._license_no = _license_no;
    }

    public boolean is_approved() {
        return _approved;
    }

    public void set_approved(boolean _approved) {
        this._approved = _approved;
    }

    public String get_issuer() {
        return _issuer;
    }

    public void set_issuer(String _issuer) {
        this._issuer = _issuer;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_sent_date() {
        return _sent_date;
    }

    public void set_sent_date(String _sent_date) {
        this._sent_date = _sent_date;
    }

    public String get_citizenship_no() {
        return _citizenship_no;
    }

    public void set_citizenship_no(String _citizenship_no) {
        this._citizenship_no = _citizenship_no;
    }

    public String get_dob_ad() {
        return _dob_ad;
    }

    public void set_dob_ad(String _dob_ad) {
        this._dob_ad = _dob_ad;
    }

    public String get_blood_group() {
        return _blood_group;
    }

    public void set_blood_group(String _blood_group) {
        this._blood_group = _blood_group;
    }

    public String get_ward_number() {
        return _ward_number;
    }

    public void set_ward_number(String _ward_number) {
        this._ward_number = _ward_number;
    }

    public String get_tole() {
        return _tole;
    }

    public void set_tole(String _tole) {
        this._tole = _tole;
    }

    public String get_zone() {
        return _zone;
    }

    public void set_zone(String _zone) {
        this._zone = _zone;
    }

    public String get_district() {
        return _district;
    }

    public void set_district(String _district) {
        this._district = _district;
    }

    public String get_municipality() {
        return _municipality;
    }

    public void set_municipality(String _municipality) {
        this._municipality = _municipality;
    }

    public String get_issue_date() {
        return _issue_date;
    }

    public void set_issue_date(String _issue_date) {
        this._issue_date = _issue_date;
    }

    public String get_expiry_date() {
        return _expiry_date;
    }

    public void set_expiry_date(String _expiry_date) {
        this._expiry_date = _expiry_date;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    // Overriding toString() method for debugging purpose
    @Override
    public String toString() {
        return "License{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _image='" + _image + '\'' +
                ", _license_no='" + _license_no + '\'' +
                ", _approved=" + _approved +
                ", _issuer='" + _issuer + '\'' +
                ", _type='" + _type + '\'' +
                ", _sent_date='" + _sent_date + '\'' +
                ", _citizenship_no='" + _citizenship_no + '\'' +
                ", _dob_ad='" + _dob_ad + '\'' +
                ", _blood_group='" + _blood_group + '\'' +
                ", _ward_number='" + _ward_number + '\'' +
                ", _tole='" + _tole + '\'' +
                ", _zone='" + _zone + '\'' +
                ", _district='" + _district + '\'' +
                ", _municipality='" + _municipality + '\'' +
                ", _issue_date='" + _issue_date + '\'' +
                ", _expiry_date='" + _expiry_date + '\'' +
                ", _category='" + _category + '\'' +
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
                Objects.equals(get_name(), license.get_name()) &&
                Objects.equals(get_image(), license.get_image()) &&
                Objects.equals(get_license_no(), license.get_license_no()) &&
                Objects.equals(get_issuer(), license.get_issuer()) &&
                Objects.equals(get_type(), license.get_type()) &&
                Objects.equals(get_sent_date(), license.get_sent_date()) &&
                Objects.equals(get_citizenship_no(), license.get_citizenship_no()) &&
                Objects.equals(get_dob_ad(), license.get_dob_ad()) &&
                Objects.equals(get_blood_group(), license.get_blood_group()) &&
                Objects.equals(get_ward_number(), license.get_ward_number()) &&
                Objects.equals(get_tole(), license.get_tole()) &&
                Objects.equals(get_zone(), license.get_zone()) &&
                Objects.equals(get_district(), license.get_district()) &&
                Objects.equals(get_municipality(), license.get_municipality()) &&
                Objects.equals(get_issue_date(), license.get_issue_date()) &&
                Objects.equals(get_expiry_date(), license.get_expiry_date()) &&
                Objects.equals(get_category(), license.get_category());
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
                    newItem.is_approved() == oldItem.is_approved() &&
                    newItem.get_type().equals(oldItem.get_type()) &&
                    newItem.get_sent_date().equals(oldItem.get_sent_date());
        }
    };


    @BindingAdapter("android:imageUrl")
    public static void setImage(ImageView imageView, String imageUrl) {
        Picasso
                .get()
                .load(imageUrl)
                .into(imageView);
    }
}
