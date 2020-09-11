package com.example.plasma_bank_cmh;

public class user {
    String name;
    String address;
    String contact;
    String mail;
    String password;
    String dob;
    String role;
    String description;
    String request_status;
    public user()
    {

    }

    public user(String name, String address, String contact, String mail, String password, String dob, String role, String description, String request_status) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.mail = mail;
        this.password = password;
        this.dob = dob;
        this.role = role;
        this.description = description;
        this.request_status = request_status;
    }



    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }
}
