package com.vedas.spectrocare.model;

import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;

public class RecordModel extends FamilyHistoryModel {
    String sNo;
    String condition;
    String relation;
    boolean txtBoolean;


    public RecordModel(String condition, String relation) {
        this.condition = condition;
        this.relation = relation;
    }

    public RecordModel(String sNo, String condition, String relation) {
        this.sNo = sNo;
        this.condition = condition;
        this.relation = relation;
    }

    public RecordModel() {
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String getRelation() {
        return relation;
    }

    @Override
    public void setRelation(String relation) {
        this.relation = relation;
    }

    public boolean isTxtBoolean() {
        return txtBoolean;
    }

    public void setTxtBoolean(boolean txtBoolean) {
        this.txtBoolean = txtBoolean;
    }
}
