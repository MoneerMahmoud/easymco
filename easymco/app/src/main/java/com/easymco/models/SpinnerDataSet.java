package com.easymco.models;

import java.util.ArrayList;

public class SpinnerDataSet {

    private String _name;
    private Integer _id;
    private ArrayList<SpinnerCountryList> stateList = new ArrayList<>();

    public String get_name() {
        return _name;
    }

    public Integer get_id() {
        return _id;
    }

    public ArrayList<SpinnerCountryList> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<SpinnerCountryList> stateList) {
        this.stateList = stateList;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }
}
