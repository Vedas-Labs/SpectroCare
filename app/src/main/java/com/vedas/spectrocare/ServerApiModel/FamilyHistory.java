package com.vedas.spectrocare.ServerApiModel;

public class FamilyHistory {

    String condition;
    String relationship;
    String age;
    String moreInfo;

    public FamilyHistory(String condition, String relationship, String age, String moreInfo) {
        this.condition = condition;
        this.relationship = relationship;
        this.age = age;
        this.moreInfo = moreInfo;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
