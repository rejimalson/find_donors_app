package com.rejimalson.finddonors.helper;

/**
 * Created by REJIMALson on 2/19/2017.
 */

public class UserDetails {
    String fullName, bloodGroup, birthDay, gender;
    String phone, email;
    String country, state, district;
    String password;

    public UserDetails(String fullName, String bloodGroup, String birthDay, String gender) {
        this.fullName = fullName;
        this.bloodGroup = bloodGroup;
        this.birthDay = birthDay;
        this.gender = gender;
    }

    public  UserDetails(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public UserDetails(String country, String state, String district) {
        this.country = country;
        this.state = state;
        this.district = district;
    }

    public UserDetails(String password) {
        //TODO: Encrypt password before assigning to object
        this.password = password;
    }
}
