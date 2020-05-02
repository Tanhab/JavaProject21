package com.example.javaproject21;

public class Student {
    String name,PhoneNo,BloodGroup,Email,imageUri,nickName,address,bio,district,hobbies,dateOfBirth;

    public Student() {
    }

    public Student(String name, String phoneNo, String bloodGroup, String email, String imageUri, String nickName, String address, String bio, String district, String hobbies, String dateOfBirth) {
        this.name = name;
        PhoneNo = phoneNo;
        BloodGroup = bloodGroup;
        Email = email;
        this.imageUri = imageUri;
        this.nickName = nickName;
        this.address = address;
        this.bio = bio;
        this.district = district;
        this.hobbies = hobbies;
        this.dateOfBirth=dateOfBirth;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", BloodGroup='" + BloodGroup + '\'' +
                ", Email='" + Email + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", nickName='" + nickName + '\'' +
                ", address='" + address + '\'' +
                ", bio='" + bio + '\'' +
                ", district='" + district + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
