package com.vedas.spectrocare.PatientServerApiModel;

public class FamilyDetaislModel {
    String dieseaseName;
    String relationship;
    String age;
    String note;

    public String getDieseaseName() {
        return dieseaseName;
    }

    public void setDieseaseName(String dieseaseName) {
        this.dieseaseName = dieseaseName;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
