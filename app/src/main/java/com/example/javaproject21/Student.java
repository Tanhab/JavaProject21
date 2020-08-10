package com.example.javaproject21;

/**
 * The class for Student.
 */
public class Student {
    /**
     * The String for Name.
     */
    String name;
    /**
     * The String for Phone no.
     */
    String PhoneNo;
    /**
     * The String for Blood group.
     */
    String BloodGroup;
    /**
     * The String for Email.
     */
    String Email;
    /**
     * The String for Image uri.
     */
    String imageUri;
    /**
     * The String for Nick name.
     */
    String nickName;
    /**
     * The String for Address.
     */
    String address;
    /**
     * The String for Bio.
     */
    String bio;
    /**
     * The String for District.
     */
    String district;
    /**
     * The String for Hobbies.
     */
    String hobbies;
    /**
     * The String for Date of birth.
     */
    String dateOfBirth;

    /**
     * Instantiates a new Student.
     */
    String Uid,currentClass;
    public Student() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    /**
     * Instantiates a new Student.
     *
     * @param name        the name
     * @param phoneNo     the phone no
     * @param bloodGroup  the blood group
     * @param email       the email
     * @param imageUri    the image uri
     * @param nickName    the nick name
     * @param address     the address
     * @param bio         the bio
     * @param district    the district
     * @param hobbies     the hobbies
     * @param dateOfBirth the date of birth
     */
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

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets phone no.
     *
     * @return the phone no
     */
    public String getPhoneNo() {
        return PhoneNo;
    }

    /**
     * Sets phone no.
     *
     * @param phoneNo the phone no
     */
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    /**
     * Gets blood group.
     *
     * @return the blood group
     */
    public String getBloodGroup() {
        return BloodGroup;
    }

    /**
     * Sets blood group.
     *
     * @param bloodGroup the blood group
     */
    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * Gets image uri.
     *
     * @return the image uri
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * Sets image uri.
     *
     * @param imageUri the image uri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Gets nick name.
     *
     * @return the nick name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets nick name.
     *
     * @param nickName the nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets bio.
     *
     * @return the bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets bio.
     *
     * @param bio the bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets district.
     *
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets district.
     *
     * @param district the district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Gets hobbies.
     *
     * @return the hobbies
     */
    public String getHobbies() {
        return hobbies;
    }

    /**
     * Sets hobbies.
     *
     * @param hobbies the hobbies
     */
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    /**
     * Gets date of birth.
     *
     * @return the date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets date of birth.
     *
     * @param dateOfBirth the date of birth
     */
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
