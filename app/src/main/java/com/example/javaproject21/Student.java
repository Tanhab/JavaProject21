package com.example.javaproject21;

public class Student {
       String name,PhoneNo,BloodGroup,Email,imageUri;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", BloodGroup='" + BloodGroup + '\'' +
                ", Email='" + Email + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Student() {
    }

    public Student(String name, String phoneNo, String bloodGroup, String email, String imageUri) {
        this.name = name;
        PhoneNo = phoneNo;
        BloodGroup = bloodGroup;
        Email = email;
        this.imageUri = imageUri;
    }
}
