package com.easymco.models;

import java.io.Serializable;

public class OrderHistoryData implements Serializable {

    private String mOrderId;
    private String mOrderStatusId;
    private String mOrderStatus;
    private String mDateAdded;
    private String mFirstName;
    private String mLastName;
    private String mTotal;

    private String mInvoiceNo;
    private String mInvoicePrefix;
    private String mStoreId;
    private String mStoreName;
    private String mStoreUrl;
    private String mCustomerId;
    private String mTelephone;
    private String mFax;
    private String mEmail;

    private String mPayFirstName;
    private String mPayLastName;
    private String mPayCompany;
    private String mPayAdds1;
    private String mPayAdds2;
    private String mPayPostCode;
    private String mPayCity;
    private String mPayZoneId;
    private String mPayZone;
    private String mPayZoneCode;
    private String mPayCountryId;
    private String mPayCountry;
    private String mPayIsoCode2;
    private String mPayIsoCode3;
    private String mPayAddsFormat;
    private String mPayMethod;

    private String mShipFirstName;
    private String mShipLastName;
    private String mShipCompany;
    private String mShipAdds1;
    private String mShipAdds2;
    private String mShipPostcode;
    private String mShipCity;
    private String mShipZoneId;
    private String mShipZone;
    private String mShipZoneCode;
    private String mShipCountryId;
    private String mShipCountry;
    private String mShipIsoCode2;
    private String mShipIsoCode3;
    private String mShipAddressFormat;
    private String mShipMethod;

    private String mComment;
    private String mLanguageId;
    private String mCurrencyId;
    private String mCurrencyCode;
    private String mCurrencyValue;
    private String mDateModified;
    private String mIp;

    private String mOrderProductId;


    private String mProductsOrderId;
    private String mProductId;
    private String mName;
    private String mModel;
    private String mQuantity;
    private String mPrice;
    private String mProductsTotal;
    private String mTax;
    private String mReward;
    private String mImageUrl;

    private String mOrderTotalId;
    private String mOrderTotalsOrderID;
    private String mCode;
    private String mTitle;
    private String mValue;
    private String mSortOrder;
    private String mText;



    private String mOrderOptionId, mOptionsOrderId, mOptionsOrderProductId, mProductOptionsId,
            mProductOptionsValueId, mOptionsName, mOptionsValue, mOptionsType;

    public OrderHistoryData(String orderoptionsid, String OptionsOrderId, String OptionsOrderProductId,
                            String ProductOptionsId, String ProductOptionsValueId, String OptionsName, String OptionsValue, String OptionsType) {

        this.mOrderOptionId = orderoptionsid;
        this.mOptionsOrderId = OptionsOrderId;
        this.mOptionsOrderProductId = OptionsOrderProductId;
        this.mProductOptionsId = ProductOptionsId;
        this.mProductOptionsValueId = ProductOptionsValueId;
        this.mOptionsName = OptionsName;
        this.mOptionsValue = OptionsValue;
        this.mOptionsType = OptionsType;
    }

    public OrderHistoryData(String orderid, String invoiceno, String invoiceprefix, String storeid, String storename, String storeurl,
                            String customerid, String fname, String lname, String teleph, String fax, String email, String payfname,
                            String paylname, String paycompany, String payadds1, String payadds2, String paypcode, String paycity,
                            String payzoneid, String payzone, String payzonecode, String paycountryid, String paycountry, String payisocode2,
                            String payisocode3, String payaddsformat, String paymethod, String shipfname,
                            String shiplname, String shipcompany, String shipadds1, String shipadds2, String shippcode, String shipcity,
                            String shipzoneid, String shipzone, String shipzonecode, String shipcountryid, String shipcountry, String shipisocode2,
                            String shipisocode3, String shipaddsformat, String shipmethod, String comment, String total, String orderstatusid,
                            String orderstatus, String laguageid, String currencyid, String currencycode, String currencyvalue, String datemodified,
                            String dateadded, String ip) {

        this.mOrderId = orderid;
        this.mInvoiceNo = invoiceno;
        this.mInvoicePrefix = invoiceprefix;
        this.mStoreId = storeid;
        this.mStoreName = storename;
        this.mStoreUrl = storeurl;
        this.mCustomerId = customerid;
        this.mFirstName = fname;
        this.mLastName = lname;
        this.mTelephone = teleph;
        this.mFax = fax;
        this.mEmail = email;
        this.mPayFirstName = payfname;
        this.mPayLastName = paylname;
        this.mPayCompany = paycompany;
        this.mPayAdds1 = payadds1;
        this.mPayAdds2 = payadds2;
        this.mPayPostCode = paypcode;
        this.mPayCity = paycity;
        this.mPayZoneId = payzoneid;
        this.mPayZone = payzone;
        this.mPayZoneCode = payzonecode;
        this.mPayCountryId = paycountryid;
        this.mPayCountry = paycountry;
        this.mPayIsoCode2 = payisocode2;
        this.mPayIsoCode3 = payisocode3;
        this.mPayAddsFormat = payaddsformat;
        this.mPayMethod = paymethod;
        this.mShipFirstName = shipfname;
        this.mShipLastName = shiplname;
        this.mShipCompany = shipcompany;
        this.mShipAdds1 = shipadds1;
        this.mShipAdds2 = shipadds2;
        this.mShipPostcode = shippcode;
        this.mShipCity = shipcity;
        this.mShipZoneId = shipzoneid;
        this.mShipZone = shipzone;
        this.mShipZoneCode = shipzonecode;
        this.mShipCountryId = shipcountryid;
        this.mShipCountry = shipcountry;
        this.mShipIsoCode2 = shipisocode2;
        this.mShipIsoCode3 = shipisocode3;
        this.mShipAddressFormat = shipaddsformat;
        this.mShipMethod = shipmethod;
        this.mComment = comment;
        this.mTotal = total;
        this.mOrderStatusId = orderstatusid;
        this.mOrderStatus = orderstatus;
        this.mLanguageId = laguageid;
        this.mCurrencyId = currencyid;
        this.mCurrencyCode = currencycode;
        this.mCurrencyValue = currencyvalue;
        this.mDateModified = datemodified;
        this.mDateAdded = dateadded;
        this.mIp = ip;
    }

    public OrderHistoryData(String orderproductid, String porderid, String productid, String name, String model, String quantity, String price,
                            String productstotal, String tax, String reward, String imageUrl) {

        this.mOrderProductId = orderproductid;
        this.mProductsOrderId = porderid;
        this.mProductId = productid;
        this.mName = name;
        this.mModel = model;
        this.mQuantity = quantity;
        this.mPrice = price;
        this.mProductsTotal = productstotal;
        this.mTax = tax;
        this.mReward = reward;
        this.mImageUrl = imageUrl;
    }

    public OrderHistoryData(String ordertotalid, String oorderid, String code, String title, String value, String sortorder, String text) {
        this.mOrderTotalId = ordertotalid;
        this.mOrderTotalsOrderID = oorderid;
        this.mCode = code;
        this.mTitle = title;
        this.mValue = value;
        this.mSortOrder = sortorder;
        this.mText = text;
    }


    public String getmText() {
        return mText;
    }

    public String getmOptionsName() {
        return mOptionsName;
    }

    public String getmOptionsValue() {
        return mOptionsValue;
    }

    public String getmDateAdded() {
        return mDateAdded;
    }

    public String getmCode() {
        return mCode;
    }

    public String getmComment() {
        return mComment;
    }

    public String getmModel() {
        return mModel;
    }

    public String getmName() {
        return mName;
    }

    public String getmOrderId() {
        return mOrderId;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public String getmPayAdds1() {
        return mPayAdds1;
    }

    public String getmPayAdds2() {
        return mPayAdds2;
    }

    public String getmPayCity() {
        return mPayCity;
    }

    public String getmPayCompany() {
        return mPayCompany;
    }

    public String getmPayCountry() {
        return mPayCountry;
    }

    public String getmPayFirstName() {
        return mPayFirstName;
    }

    public String getmPayLastName() {
        return mPayLastName;
    }

    public String getmPayMethod() {
        return mPayMethod;
    }

    public String getmPayPostCode() {
        return mPayPostCode;
    }

    public String getmPayZone() {
        return mPayZone;
    }

    public String getmPrice() {
        return mPrice;
    }


    public String getmProductsTotal() {
        return mProductsTotal;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmShipAdds1() {
        return mShipAdds1;
    }

    public String getmShipAdds2() {
        return mShipAdds2;
    }

    public String getmShipCity() {
        return mShipCity;
    }

    public String getmShipCompany() {
        return mShipCompany;
    }

    public String getmShipCountry() {
        return mShipCountry;
    }

    public String getmShipFirstName() {
        return mShipFirstName;
    }

    public String getmShipLastName() {
        return mShipLastName;
    }

    public String getmShipMethod() {
        return mShipMethod;
    }

    public String getmShipPostcode() {
        return mShipPostcode;
    }

    public String getmShipZone() {
        return mShipZone;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmTotal() {
        return mTotal;
    }

    public String getmValue() {
        return mValue;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmOrderProductId() {
        return mOrderProductId;
    }

    public String getmOptionsOrderProductId() {
        return mOptionsOrderProductId;
    }
}
