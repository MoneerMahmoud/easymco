package com.easymco.models;

import java.util.ArrayList;

public class Navigation_DataSet {

    private CategoryDataSet mParentList;
    private ArrayList<CategoryDataSet> mChildAdder;

    public ArrayList<CategoryDataSet> getmChildAdder() {
        return mChildAdder;
    }

    public void setmChildAdder(ArrayList<CategoryDataSet> mChildAdder) {
        this.mChildAdder = mChildAdder;
    }

    public CategoryDataSet getmParentList() {
        return mParentList;
    }

    public void setmParentList(CategoryDataSet mParentList) {
        this.mParentList = mParentList;
    }
}
