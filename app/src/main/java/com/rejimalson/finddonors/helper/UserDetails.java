package com.rejimalson.finddonors.helper;

/**
 * Created by REJIMALson on 2/19/2017.
 */

public class UserDetails {
    private String Full_Name, Blood_Group, Birthday, Gender;
    private String Phone_Number, Email_Id;
    private String Country, State, District;
    private String Password;

    public UserDetails(String name, String bloodgroup, String birthday, String gender) {
        this.Full_Name = name;
        this.Blood_Group = bloodgroup;
        this.Birthday = birthday;
        this.Gender = gender;
    }

    public  UserDetails(String phone, String email) {
        this.Phone_Number = phone;
        this.Email_Id = email;
    }

    public UserDetails(String country, String state, String district) {
        this.Country = country;
        this.State = state;
        this.District = district;
    }

    public UserDetails(String password) {
        //TODO: Encrypt the password before storing it to Database
        this.Password = password;
    }
}
