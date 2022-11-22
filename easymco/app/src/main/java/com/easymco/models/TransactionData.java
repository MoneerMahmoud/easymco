package com.easymco.models;

public class TransactionData {

    private String mDateAdded;
    private String mDiscription;
    private String mAmtUSD;

    public TransactionData(String dateAdded,String discription,String amtUSD){
        this.mDateAdded=dateAdded;
        this.mDiscription=discription;
        this.mAmtUSD = amtUSD;
    }

    public String getmDateAdded() {
        return mDateAdded;
    }

    public String getmDiscription() {
        return mDiscription;
    }

    public String getmAmtUSD() {
        return mAmtUSD;
    }
}
