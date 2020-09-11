package com.example.plasma_bank_cmh;

public class donor {
    String name;
    String address;
    String contact;
    String mail;
    String dob;
    String blood_group;
    String pos_date;
    String neg_date;
    String med_history;

    public donor(String name, String address, String contact, String mail, String dob, String blood_group, String pos_date, String neg_date, String med_history) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.mail = mail;
        this.dob = dob;
        this.blood_group = blood_group;
        this.pos_date = pos_date;
        this.neg_date = neg_date;
        this.med_history = med_history;
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

    public String getDob() {
        return dob;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public String getPos_date() {
        return pos_date;
    }

    public String getNeg_date() {
        return neg_date;
    }

    public String getMed_history() {
        return med_history;
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

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public void setPos_date(String pos_date) {
        this.pos_date = pos_date;
    }

    public void setNeg_date(String neg_date) {
        this.neg_date = neg_date;
    }

    public void setMed_history(String med_history) {
        this.med_history = med_history;
    }
}
