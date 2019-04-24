package com.bmusic.bmusicworld.model;


public class User {

    private String id;
    private String userName, mobileNo,email,dob,user_type,aadhar,pan,add,created_date;


    public User() {
    }

    public User(String id, String userName, String mobileNo, String email, String user_type, String dob,String created_date) {
        this.id = id;
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.dob = dob;
        this.user_type = user_type;
        this.created_date = created_date;
    }
public User( String userName, String mobileNo, String email, String user_type, String dob,String created_date) {
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.dob = dob;
        this.user_type = user_type;
        this.created_date = created_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String profileImage) {
        this.user_type = profileImage;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", profileImage='" + user_type + '\'' +
                '}';
    }


}
