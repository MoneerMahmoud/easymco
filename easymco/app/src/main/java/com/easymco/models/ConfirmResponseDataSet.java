package com.easymco.models;

import java.util.ArrayList;

public class ConfirmResponseDataSet {
    private String mOrderId, mTotalsTitle, mTotalsValue;
    private ArrayList<ConfirmResponseDataSet> mTotalList = new ArrayList<>();

    public String getmOrderId() {
        return mOrderId;
    }

    public void setmOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public String getmTotalsTitle() {
        return mTotalsTitle;
    }

    public void setmTotalsTitle(String mTotalsTitle) {
        this.mTotalsTitle = mTotalsTitle;
    }

    public String getmTotalsValue() {
        return mTotalsValue;
    }

    public void setmTotalsValue(String mTotalsValue) {
        this.mTotalsValue = mTotalsValue;
    }

    public ArrayList<ConfirmResponseDataSet> getmTotalList() {
        return mTotalList;
    }

    public void setmTotalList(ArrayList<ConfirmResponseDataSet> mTotalList) {
        this.mTotalList = mTotalList;
    }
}
