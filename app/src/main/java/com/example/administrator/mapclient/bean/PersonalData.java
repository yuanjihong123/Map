package com.example.administrator.mapclient.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15.
 */

public class PersonalData implements Serializable {
    private String headPortraitImage;//个人头像
    private String personal_nickname;//昵称
    private String personal_sex;
    private String personal_age;//年龄
    private String personal_birthday;//生日
    private String personal_bloodType;//血型
    private String personal_Profession;//职业
    private String email;//邮箱

    @Override
    public String toString() {
        return "PersonalData{" +
                "headPortraitImage='" + headPortraitImage + '\'' +
                ", personal_nickname='" + personal_nickname + '\'' +
                ", personal_sex='" + personal_sex + '\'' +
                ", personal_age='" + personal_age + '\'' +
                ", personal_birthday='" + personal_birthday + '\'' +
                ", personal_bloodType='" + personal_bloodType + '\'' +
                ", personal_Profession='" + personal_Profession + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getHeadPortraitImage() {
        return headPortraitImage;
    }

    public void setHeadPortraitImage(String headPortraitImage) {
        this.headPortraitImage = headPortraitImage;
    }

    public String getPersonal_nickname() {
        return personal_nickname;
    }

    public void setPersonal_nickname(String personal_nickname) {
        this.personal_nickname = personal_nickname;
    }

    public String getPersonal_sex() {
        return personal_sex;
    }

    public void setPersonal_sex(String personal_sex) {
        this.personal_sex = personal_sex;
    }

    public String getPersonal_age() {
        return personal_age;
    }

    public void setPersonal_age(String personal_age) {
        this.personal_age = personal_age;
    }

    public String getPersonal_birthday() {
        return personal_birthday;
    }

    public void setPersonal_birthday(String personal_birthday) {
        this.personal_birthday = personal_birthday;
    }

    public String getPersonal_bloodType() {
        return personal_bloodType;
    }

    public void setPersonal_bloodType(String personal_bloodType) {
        this.personal_bloodType = personal_bloodType;
    }

    public String getPersonal_Profession() {
        return personal_Profession;
    }

    public void setPersonal_Profession(String personal_Profession) {
        this.personal_Profession = personal_Profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}