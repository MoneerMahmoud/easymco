package com.easymco.json_mechanism;

import android.content.Context;
import android.util.Log;

import com.easymco.Application_Context;
import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.models.AccountDataSet;
import com.easymco.models.CartDataSet;
import com.easymco.models.CategoryDataSet;
import com.easymco.models.ConfirmResponseDataSet;
import com.easymco.models.FilterDataSet;
import com.easymco.models.Navigation_DataSet;
import com.easymco.models.ProductDataSet;
import com.easymco.models.ProductImageDataSet;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.models.ProductReviewListDataSet;
import com.easymco.models.ProductSpecificationDataSet;
import com.easymco.models.Response;
import com.easymco.models.RewardPointsData;
import com.easymco.models.ShippingAndPayment_DataSet;
import com.easymco.models.SpinnerCountryList;
import com.easymco.models.SpinnerDataSet;
import com.easymco.models.WholeFilterDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetJSONData {

    /*Special list Featured products,Special products,Latest products starts*/

    /*Home*/
    public static ArrayList<ProductDataSet> getHomeData(String data) {

        ArrayList<ProductDataSet> list_data = new ArrayList<>();
        try {
            if (data != null) {
                JSONObject mObject = new JSONObject(data);
                if (mObject.length() != 0) {
                    JSONArray mData = mObject.getJSONArray(JSON_Names.KEY_PRODUCT);
                    if (mData != null) {
                        for (int i = 0; i < mData.length(); i++) {
                            JSONObject arrayObject = mData.getJSONObject(i);
                            if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID) && !arrayObject.isNull(JSON_Names.KEY_QUANTITY)) {
                                ProductDataSet result = new ProductDataSet();
                                result.setProduct_string(arrayObject.toString());
                                if (arrayObject.getString(JSON_Names.KEY_IMAGE) != null) {
                                    result.setImage(arrayObject.getString(JSON_Names.KEY_IMAGE));
                                } else {
                                    result.setImage(null);
                                }
                                if (arrayObject.getString(JSON_Names.KEY_PRICE) != null) {
                                    result.setPrice(arrayObject.getString(JSON_Names.KEY_PRICE));
                                } else {
                                    result.setPrice(null);
                                }
                                if (arrayObject.getString(JSON_Names.KEY_NAME) != null) {
                                    result.setTitle(arrayObject.getString(JSON_Names.KEY_NAME));
                                } else {
                                    result.setTitle(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_SPECIAL)) {
                                    result.setSpecial_price(arrayObject.getString(JSON_Names.KEY_SPECIAL));
                                } else {
                                    result.setSpecial_price(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                                    result.setProduct_id(arrayObject.getString(JSON_Names.KEY_PRODUCT_ID));
                                } else {
                                    result.setProduct_id(null);
                                }
                                list_data.add(result);
                            }
                        }
                        return list_data;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /*Category*/
    public static ArrayList<ProductDataSet> getProductDetailsThree(String data) {

        ArrayList<ProductDataSet> list_data = new ArrayList<>();
        try {
            if (data != null) {
                JSONObject mObject = new JSONObject(data);
                if (mObject.length() != 0) {

                    DataStorage.mRemoveSharedPreferenceString(Application_Context.getAppContext(), JSON_Names.KEY_CURRENT_COUNT);
                    if (!mObject.isNull(JSON_Names.KEY_COUNT)) {
                        DataStorage.mStoreSharedPreferenceString(Application_Context.getAppContext(), JSON_Names.KEY_CURRENT_COUNT,
                                mObject.getString(JSON_Names.KEY_COUNT));
                    } else {
                        DataStorage.mStoreSharedPreferenceString(Application_Context.getAppContext(), JSON_Names.KEY_CURRENT_COUNT, "0");
                    }

                    JSONArray mData = mObject.getJSONArray(JSON_Names.KEY_PRODUCT);

                    for (int i = 0; i < mData.length(); i++) {
                        JSONObject arrayObject = mData.getJSONObject(i);
                        ProductDataSet result = new ProductDataSet();
                        if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID) && !arrayObject.isNull(JSON_Names.KEY_QUANTITY)) {
                           // if (!arrayObject.getString(JSON_Names.KEY_QUANTITY).equals("0")) {
                                result.setProduct_string(data);
                                if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                                    result.setProduct_id(arrayObject.getString(JSON_Names.KEY_PRODUCT_ID));
                                } else {
                                    result.setProduct_id(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_IMAGE)) {
                                    result.setImage(arrayObject.getString(JSON_Names.KEY_IMAGE));
                                } else {
                                    result.setImage(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_PRICE)) {
                                    result.setPrice(arrayObject.getString(JSON_Names.KEY_PRICE));
                                } else {
                                    result.setPrice(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_NAME)) {
                                    result.setTitle(arrayObject.getString(JSON_Names.KEY_NAME));
                                } else {
                                    result.setTitle(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_SPECIAL)) {
                                    result.setSpecial_price(arrayObject.getString(JSON_Names.KEY_SPECIAL));
                                } else {
                                    result.setSpecial_price(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_QUANTITY)) {
                                    result.setQuantity(arrayObject.getString(JSON_Names.KEY_QUANTITY));
                                } else {
                                    result.setQuantity(null);
                                }
                                if (!arrayObject.isNull(JSON_Names.KEY_MINIMUM)) {
                                    result.setMinimum(arrayObject.getString(JSON_Names.KEY_MINIMUM));
                                } else {
                                    result.setMinimum(null);
                                }
                                int count = 0;

                                JSONArray mOptionJsonArrayObject = arrayObject.getJSONArray(JSON_Names.KEY_PRODUCT_OPTIONS);
                                if (mOptionJsonArrayObject.length() != 0) {
                                    for (int j = 0; j < mOptionJsonArrayObject.length(); j++) {

                                        JSONObject mOptionJsonObject = mOptionJsonArrayObject.getJSONObject(j);
                                        String temp = mOptionJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_TYPE);
                                        if ((temp.equals("radio")) || (temp.equals("checkbox")) || (temp.equals("select")) || (temp.equals("image"))) {
                                            count = count + 1;
                                        }
                                    }

                                }
                                list_data.add(result);
                           // }
                        }

                    }
                }
            } else {
                return null;
            }
            return list_data;
        } catch (Exception e) {
            return null;
        }
    }

    /*Special list Featured products,Special products,Latest products Ends*/

    public static ArrayList<Navigation_DataSet> getNavigationData(String data,Context context) {

        if (data != null) {

            ArrayList<Navigation_DataSet> navigation_dataSets = new ArrayList<>();
            Navigation_DataSet mNavigation_Object;
            CategoryDataSet PC;
            ArrayList<CategoryDataSet> mChildList;
            try {
                JSONObject jsono = new JSONObject(data);

                if (jsono.length() > 0) {

                    //Parsing the parent item
                    JSONArray jsonArrayparent = jsono.getJSONArray("categories");
                    for (int i = 0; i < jsonArrayparent.length(); i++) {
                        mNavigation_Object = new Navigation_DataSet();
                        JSONObject jsonObjectparent = jsonArrayparent.getJSONObject(i);
                        if (jsonObjectparent.optString(JSON_Names.KEY_NAME) != null && jsonObjectparent.optString(JSON_Names.KEY_RESPONSE_SORT_ORDER) != null) {
                            if (!jsonObjectparent.isNull("top")) {
                                PC = new CategoryDataSet();
                                PC.setName(jsonObjectparent.optString(JSON_Names.KEY_NAME));
                                PC.setSort_order(jsonObjectparent.getInt(JSON_Names.KEY_RESPONSE_SORT_ORDER));
                                PC.setTop(jsonObjectparent.getInt("top"));
                                PC.setCategory_id(jsonObjectparent.getInt("category_id"));
                                PC.setImage(jsonObjectparent.getString(JSON_Names.KEY_IMAGE));
                                if (!jsonObjectparent.isNull("mobile_app_icon")) {
                                    PC.setMenu_icon(jsonObjectparent.getString("mobile_app_icon"));
                                } else {
                                    PC.setMenu_icon(null);
                                }
                                PC.setMarked(true);
                                PC.setOpened(false);
                                mNavigation_Object.setmParentList(PC);
                            }
                        }
                        if (!jsonObjectparent.isNull("top")) {
                            //Parsing the Child item
                            mChildList = new ArrayList<>();
                            JSONArray jsonarraychild = jsonObjectparent.getJSONArray("child");
                            for (int j = 0; j < jsonarraychild.length(); j++) {
                                JSONObject jsonObjectchild = jsonarraychild.getJSONObject(j);
                                if (jsonObjectchild.optString(JSON_Names.KEY_NAME) != null && jsonObjectchild.optString(JSON_Names.KEY_RESPONSE_SORT_ORDER) != null) {
                                    CategoryDataSet CC = new CategoryDataSet();
                                    CC.setSort_order(jsonObjectchild.getInt(JSON_Names.KEY_RESPONSE_SORT_ORDER));
                                    CC.setTop(jsonObjectchild.getInt("top"));
                                    CC.setCategory_id(jsonObjectchild.getInt("category_id"));
                                    CC.setImage(jsonObjectchild.getString(JSON_Names.KEY_IMAGE));
                                    if (CC.getCategory_id() == jsonObjectparent.getInt("category_id")) {
                                        CC.setParent_id(jsonObjectparent.getInt("category_id"));
                                        CC.setName(context.getResources().getString(R.string.all_products));
                                    } else {
                                        CC.setParent_id(jsonObjectchild.getInt("parent_id"));
                                        CC.setName(jsonObjectchild.optString(JSON_Names.KEY_NAME));
                                    }
                                    if (!jsonObjectparent.isNull("mobile_app_icon")) {
                                        CC.setMenu_icon(jsonObjectparent.getString("mobile_app_icon"));
                                    } else {
                                        CC.setMenu_icon(null);
                                    }
                                    CC.setMarked(true);
                                    CC.setOpened(false);
                                    mChildList.add(CC);
                                }
                            }
                            mChildList = Sort_Main_List.getChildSortedLists(mChildList);
                            mNavigation_Object.setmChildAdder(mChildList);
                        }
                        navigation_dataSets.add(mNavigation_Object);
                    }
                }
                return Sort_Main_List.getHomeListSortedNew(navigation_dataSets);

            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


    public static ArrayList<ProductDataSet> getProductDetails(String data) {

        ArrayList<ProductDataSet> list_data = new ArrayList<>();
        try {
            if (data != null) {
                JSONObject mObject = new JSONObject(data);
                if (mObject.length() != 0) {
                    DataStorage.mRemoveSharedPreferenceString(Application_Context.getAppContext(), JSON_Names.KEY_CURRENT_COUNT);
                    DataStorage.mStoreSharedPreferenceString(Application_Context.getAppContext(), JSON_Names.KEY_CURRENT_COUNT, mObject.getString(JSON_Names.KEY_COUNT));
                    JSONArray mData = mObject.getJSONArray(JSON_Names.KEY_PRODUCTS);

                    for (int i = 0; i < mData.length(); i++) {
                        JSONObject arrayObject = mData.getJSONObject(i);
                        ProductDataSet result = new ProductDataSet();
                        result.setProduct_string(arrayObject.toString());
                        if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                            if (!arrayObject.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                                result.setProduct_id(arrayObject.getString(JSON_Names.KEY_PRODUCT_ID));
                            } else {
                                result.setProduct_id(null);
                            }
                            if (!arrayObject.isNull(JSON_Names.KEY_IMAGE)) {
                                result.setImage(arrayObject.getString(JSON_Names.KEY_IMAGE));
                            } else {
                                result.setImage(null);
                            }
                            if (!arrayObject.isNull(JSON_Names.KEY_PRICE)) {
                                result.setPrice(arrayObject.getString(JSON_Names.KEY_PRICE));
                            } else {
                                result.setPrice(null);
                            }
                            if (!arrayObject.isNull(JSON_Names.KEY_NAME)) {
                                result.setTitle(arrayObject.getString(JSON_Names.KEY_NAME));
                            } else {
                                result.setTitle(null);
                            }
                            if (!arrayObject.isNull(JSON_Names.KEY_SPECIAL)) {
                                result.setSpecial_price(arrayObject.getString(JSON_Names.KEY_SPECIAL));
                            } else {
                                result.setSpecial_price(null);
                            }
                            if (!arrayObject.isNull(JSON_Names.KEY_QUANTITY)) {
                                result.setQuantity(arrayObject.getString(JSON_Names.KEY_QUANTITY));
                            } else {
                                result.setQuantity(null);
                            }
                            list_data.add(result);
                        }

                    }
                }
            } else {
                return null;
            }
            return list_data;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<CategoryDataSet> getHomeCategoryData(String data) {

        if (data != null) {
            ArrayList<CategoryDataSet> mParentList = new ArrayList<>();
            try {
                JSONObject jsono = new JSONObject(data);

                if (jsono.length() > 0) {

                    //Parsing the parent item
                    JSONArray jsonArrayParent = jsono.getJSONArray("categories");
                    for (int i = 0; i < jsonArrayParent.length(); i++) {

                        JSONObject jsonObjectParent = jsonArrayParent.getJSONObject(i);
                        if (jsonObjectParent.optString(JSON_Names.KEY_NAME) != null && jsonObjectParent.optString(JSON_Names.KEY_RESPONSE_SORT_ORDER) != null) {
                            if (!jsonObjectParent.isNull("top")) {
                                if (!jsonObjectParent.isNull("mobile_icon")) {
                                    if (jsonObjectParent.getString("mobile_icon").length() > 0) {
                                        if (!jsonObjectParent.getString("mobile_icon").contains("no_image")) {
                                            CategoryDataSet PC = new CategoryDataSet();
                                            PC.setName(jsonObjectParent.optString(JSON_Names.KEY_NAME));
                                            PC.setSort_order(jsonObjectParent.getInt(JSON_Names.KEY_RESPONSE_SORT_ORDER));
                                            PC.setTop(jsonObjectParent.getInt("top"));
                                            PC.setCategory_id(jsonObjectParent.getInt("category_id"));
                                            PC.setImage(jsonObjectParent.getString(JSON_Names.KEY_IMAGE));
                                            PC.setMarked(true);
                                            if (!jsonObjectParent.isNull("mobile_icon")) {
                                                if (jsonObjectParent.getString("mobile_icon").length() > 0) {
                                                    PC.setIcon_image(jsonObjectParent.getString("mobile_icon"));
                                                }
                                            } else {
                                                PC.setIcon_image(null);
                                            }
                                            mParentList.add(PC);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mParentList = Sort_Main_List.getParentSortedLists(mParentList);
                }
                return mParentList;

            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static ArrayList<WholeFilterDataSet> getFilterData(String data) {
        ArrayList<WholeFilterDataSet> mReturnValue = new ArrayList<>();
        ArrayList<FilterDataSet> mTitleList = new ArrayList<>();
        ArrayList<FilterDataSet> mFilterChildList;
        ArrayList<ArrayList<FilterDataSet>> mChildList = new ArrayList<>();
        WholeFilterDataSet mWholeList;

        if (data != null) {
            try {
                JSONObject mFilterData = new JSONObject(data);
                if (mFilterData.length() != 0) {
                    JSONArray mArrayList = mFilterData.getJSONArray(JSON_Names.KEY_FILTERS);
                    for (int i = 0; i < mArrayList.length(); i++) {
                        JSONObject mRealData = mArrayList.getJSONObject(i);
                        FilterDataSet mFilterDataSet = new FilterDataSet();
                        mFilterDataSet.setmName(mRealData.getString(JSON_Names.KEY_FILTER_NAME));
                        if (i == 0) {
                            mFilterDataSet.setSelect(true);
                        } else {
                            mFilterDataSet.setSelect(false);
                        }
                        JSONArray mChildRealDataJSONArray = mRealData.getJSONArray(JSON_Names.KEY_FILTER);
                        mTitleList.add(mFilterDataSet);
                        mFilterChildList = new ArrayList<>();
                        for (int j = 0; j < mChildRealDataJSONArray.length(); j++) {
                            JSONObject mChildData = mChildRealDataJSONArray.getJSONObject(j);
                            FilterDataSet mChildFilterDataSet = new FilterDataSet();
                            mChildFilterDataSet.setmName(mChildData.getString(JSON_Names.KEY_FILTER_NAME));
                            mChildFilterDataSet.setmFilterId(mChildData.getString(JSON_Names.KEY_FILTER_ID));
                            mChildFilterDataSet.setSelect(false);
                            mFilterChildList.add(mChildFilterDataSet);
                        }
                        mChildList.add(mFilterChildList);
                        mWholeList = new WholeFilterDataSet(mTitleList, mChildList);
                        mReturnValue.add(mWholeList);
                    }
                    return mReturnValue;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static ArrayList<CategoryDataSet> getSubCategoryList(String data) {
        ArrayList<CategoryDataSet> mList = new ArrayList<>();
        try {
            if (data != null) {
                JSONObject mSubCategoryData = new JSONObject(data);
                JSONArray mArrayList = mSubCategoryData.getJSONArray("categories");
                for (int i = 0; i < mArrayList.length(); i++) {
                    JSONObject jsonObjectparent = mArrayList.getJSONObject(i);
                    JSONArray jsonarraychild = jsonObjectparent.getJSONArray("child");
                    for (int j = 0; j < jsonarraychild.length(); j++) {
                        JSONObject jsonObjectchild = jsonarraychild.getJSONObject(j);
                        CategoryDataSet mCategoryDataSet = new CategoryDataSet();
                        if (jsonObjectchild.optString(JSON_Names.KEY_NAME) != null && !jsonObjectchild.isNull("category_id")) {
                            mCategoryDataSet.setName(jsonObjectchild.optString(JSON_Names.KEY_NAME));
                            mCategoryDataSet.setCategory_id(jsonObjectchild.optInt("category_id"));
                            mList.add(mCategoryDataSet);
                        }
                    }
                }
                return mList;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, Object> getSeparateProductDetail(String data) {
        HashMap<String, Object> mList = new HashMap<>();
        ArrayList<ProductImageDataSet> mImageList = new ArrayList<>();
        ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
        ArrayList<ProductOptionDataSet> mOptionListChild;
        HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChildHolder = new HashMap<>();
        ArrayList<ProductReviewListDataSet> mReviewList = new ArrayList<>();
        ProductDataSet mProductDataSet = new ProductDataSet();
        ProductImageDataSet mProductImageDataSet;
        ProductOptionDataSet mProductOptionDataSet;
        ProductOptionDataSet mProductOptionDataSetChild;
        ProductReviewListDataSet mProductReviewListDataSet;
        try {
            JSONObject mJsonParentObjectProduct = new JSONObject(data);
            JSONObject mJsonParentObject = mJsonParentObjectProduct.getJSONObject("product");
            mProductDataSet.setProduct_string(data);

            if (!mJsonParentObject.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                mProductDataSet.setProduct_id(mJsonParentObject.getString(JSON_Names.KEY_PRODUCT_ID));
            } else {
                mProductDataSet.setProduct_id(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_MINIMUM)) {
                mProductDataSet.setMinimum(mJsonParentObject.getString(JSON_Names.KEY_MINIMUM));
            } else {
                mProductDataSet.setMinimum(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_NAME)) {
                mProductDataSet.setTitle(mJsonParentObject.getString(JSON_Names.KEY_NAME));
            } else {
                mProductDataSet.setTitle(null);
            }
            if (!mJsonParentObject.isNull("subtract")) {
                mProductDataSet.setSubtract(mJsonParentObject.getInt("subtract"));
            } else {
                mProductDataSet.setSubtract(0);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_PRICE)) {
                mProductDataSet.setPrice(mJsonParentObject.getString(JSON_Names.KEY_PRICE));
            } else {
                mProductDataSet.setPrice(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_SPECIAL)) {
                mProductDataSet.setSpecial_price(mJsonParentObject.getString(JSON_Names.KEY_SPECIAL));
            } else {
                mProductDataSet.setSpecial_price(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_IMAGE)) {
                mProductDataSet.setImage(mJsonParentObject.getString(JSON_Names.KEY_IMAGE));
            } else {
                mProductDataSet.setImage(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_QUANTITY)) {
                mProductDataSet.setQuantity(mJsonParentObject.getString(JSON_Names.KEY_QUANTITY));
            } else {
                mProductDataSet.setQuantity(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_REVIEWS)) {
                mProductDataSet.setNo_of_review(mJsonParentObject.getString(JSON_Names.KEY_REVIEWS));
            } else {
                mProductDataSet.setNo_of_review(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_RATING)) {
                mProductDataSet.setRating(mJsonParentObject.getString(JSON_Names.KEY_RATING));
            } else {
                mProductDataSet.setRating(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_DESCRIPTION)) {
                mProductDataSet.setDescription(mJsonParentObject.getString(JSON_Names.KEY_DESCRIPTION));
            } else {
                mProductDataSet.setDescription(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_STOCK_STATUS)) {
                mProductDataSet.setStock_status(mJsonParentObject.getString(JSON_Names.KEY_STOCK_STATUS));
            } else {
                mProductDataSet.setStock_status(null);
            }
            if (!mJsonParentObject.isNull(JSON_Names.KEY_MANUFACTURER)) {
                mProductDataSet.setManufacturer(mJsonParentObject.getString(JSON_Names.KEY_MANUFACTURER));
            } else {
                mProductDataSet.setManufacturer(null);
            }
            if (!mJsonParentObject.isNull("big_image")) {
                mProductDataSet.setBig_image(mJsonParentObject.getString("big_image"));
            } else {
                mProductDataSet.setBig_image(null);
            }


            JSONArray mOptionJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_PRODUCT_OPTIONS);
            if (mOptionJsonArrayObject.length() != 0) {
                for (int i = 0; i < mOptionJsonArrayObject.length(); i++) {
                    mProductOptionDataSet = new ProductOptionDataSet();
                    JSONObject mOptionJsonObject = mOptionJsonArrayObject.getJSONObject(i);
                    String temp = mOptionJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_TYPE);
                    if ((temp.equals("radio")) || (temp.equals("checkbox")) || (temp.equals("select")) || (temp.equals("image"))) {
                        if (!mOptionJsonObject.isNull(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_ID)) {
                            mProductOptionDataSet.setProduct_option_id(mOptionJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_ID));
                        } else {
                            mProductOptionDataSet.setProduct_option_id(null);
                        }
                        if (!mOptionJsonObject.isNull(JSON_Names.KEY_NAME)) {
                            mProductOptionDataSet.setProduct_option_name(mOptionJsonObject.getString(JSON_Names.KEY_NAME));
                        } else {
                            mProductOptionDataSet.setProduct_option_name(null);
                        }
                        if (!mOptionJsonObject.isNull(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_TYPE)) {
                            mProductOptionDataSet.setProduct_option_type(mOptionJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_TYPE));
                        } else {
                            mProductOptionDataSet.setProduct_option_type(null);
                        }
                        if (!mOptionJsonObject.isNull(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_REQUIRED)) {
                            mProductOptionDataSet.setProduct_option_required(mOptionJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_REQUIRED));
                        } else {
                            mProductOptionDataSet.setProduct_option_required(null);
                        }
                        JSONArray mOptionJsonInnerArrayObject = mOptionJsonObject.getJSONArray(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_VALUE);
                        if (mOptionJsonInnerArrayObject != null) {
                            mOptionListChild = new ArrayList<>();

                            ProductOptionDataSet dummyProductOptionDataSet = new ProductOptionDataSet();
                            dummyProductOptionDataSet.setProduct_name(Application_Context.getAppContext().getResources().getString(R.string.please_select));
                            dummyProductOptionDataSet.setProduct_option_value_id("-1");
                            dummyProductOptionDataSet.setSelected(false);

                            mOptionListChild.add(dummyProductOptionDataSet);
                            for (int j = 0; j < mOptionJsonInnerArrayObject.length(); j++) {
                                JSONObject mOptionInnerJsonObject = mOptionJsonInnerArrayObject.getJSONObject(j);
                                mProductOptionDataSetChild = new ProductOptionDataSet();
                                if (!mOptionInnerJsonObject.isNull(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_VALUE_ID)) {
                                    mProductOptionDataSetChild.setProduct_option_value_id(mOptionInnerJsonObject.getString(JSON_Names.KEY_PRODUCT_OPTION_PRODUCT_OPTION_VALUE_ID));
                                } else {
                                    mProductOptionDataSetChild.setProduct_option_value_id(null);
                                }
                                mProductOptionDataSetChild.setSelected(false);
                                if (!mOptionInnerJsonObject.isNull(JSON_Names.KEY_NAME)) {
                                    mProductOptionDataSetChild.setProduct_name(mOptionInnerJsonObject.getString(JSON_Names.KEY_NAME));
                                } else {
                                    mProductOptionDataSetChild.setProduct_name(null);
                                }
                                if (!mOptionInnerJsonObject.isNull(JSON_Names.KEY_QUANTITY)) {
                                    mProductOptionDataSetChild.setProduct_option_quantity(mOptionInnerJsonObject.getString(JSON_Names.KEY_QUANTITY));
                                } else {
                                    mProductOptionDataSetChild.setProduct_option_quantity(null);
                                }
                                mOptionListChild.add(mProductOptionDataSetChild);
                            }
                            mOptionListChildHolder.put(mOptionListChildHolder.size(), mOptionListChild);
                            mOptionList.add(mProductOptionDataSet);
                        }
                    }
                }
            }

            JSONArray mImageJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_IMAGES);
            if (mImageJsonArrayObject.length() != 0) {
                for (int i = 0; i < mImageJsonArrayObject.length(); i++) {
                    JSONObject mImageJsonObject = mImageJsonArrayObject.getJSONObject(i);
                    mProductImageDataSet = new ProductImageDataSet();
                    if (!mImageJsonObject.isNull(JSON_Names.KEY_IMAGE)) {
                        mProductImageDataSet.setChildImage(mImageJsonObject.getString(JSON_Names.KEY_IMAGE));
                    } else {
                        mProductImageDataSet.setChildImage(null);
                    }
                    mImageList.add(mProductImageDataSet);
                }
            }

            JSONArray mReviewListJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_PRODUCT_REVIEW_LIST);
            if (mReviewListJsonArrayObject.length() != 0) {
                for (int i = 0; i < mReviewListJsonArrayObject.length(); i++) {
                    JSONObject mReviewListJsonObject = mReviewListJsonArrayObject.getJSONObject(i);
                    mProductReviewListDataSet = new ProductReviewListDataSet();
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_RATING)) {
                        mProductReviewListDataSet.setProduct_rating(mReviewListJsonObject.getString(JSON_Names.KEY_RATING));
                    } else {
                        mProductReviewListDataSet.setProduct_rating(null);
                    }
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_PRODUCT_TEXT)) {
                        mProductReviewListDataSet.setProduct_text(mReviewListJsonObject.getString(JSON_Names.KEY_PRODUCT_TEXT));
                    } else {
                        mProductReviewListDataSet.setProduct_text(null);
                    }
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_PRODUCT_AUTHOR)) {
                        mProductReviewListDataSet.setProduct_author(mReviewListJsonObject.getString(JSON_Names.KEY_PRODUCT_AUTHOR));
                    } else {
                        mProductReviewListDataSet.setProduct_author(null);
                    }
                    mReviewList.add(mProductReviewListDataSet);
                }
            }
            mProductDataSet.setProductImageDataSetsList(mImageList);
            mProductDataSet.setmReviewList(mReviewList);
            mList.put("Product", mProductDataSet);
            mList.put("Option", mOptionList);
            mList.put("Option Child", mOptionListChildHolder);
        } catch (Exception e) {
            return null;
        }

        return mList;
    }

    public static ArrayList<ProductImageDataSet> getImageList(String data) {
        try {
            ProductImageDataSet mProductImageDataSet;
            ProductImageDataSet mProductImageDataSetStatic = new ProductImageDataSet();
            ArrayList<ProductImageDataSet> mImageList = new ArrayList<>();
            JSONObject mJsonParentObjectProduct = new JSONObject(data);
            JSONObject mJsonParentObject = mJsonParentObjectProduct.getJSONObject("product");
            if (!mJsonParentObject.isNull("big_image")) {
                mProductImageDataSetStatic.setChildImage(mJsonParentObject.getString("big_image"));
            } else {
                mProductImageDataSetStatic.setChildImage(null);
            }
            mProductImageDataSetStatic.setSelected(true);

            mImageList.add(mProductImageDataSetStatic);

            JSONArray mImageJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_IMAGES);
            if (mImageJsonArrayObject.length() != 0) {
                for (int i = 0; i < mImageJsonArrayObject.length(); i++) {
                    JSONObject mImageJsonObject = mImageJsonArrayObject.getJSONObject(i);
                    mProductImageDataSet = new ProductImageDataSet();
                    if (mImageJsonObject.isNull(JSON_Names.KEY_PRODUCT_OPTION_WEIGHT_PREFIX)) {
                        mProductImageDataSet.setChildImage(mImageJsonObject.getString(JSON_Names.KEY_IMAGE));
                    } else {
                        mProductImageDataSet.setChildImage(null);
                    }
                    mProductImageDataSet.setSelected(false);
                    mImageList.add(mProductImageDataSet);
                }
            }

            return mImageList;
        } catch (Exception e) {
            return null;
        }
    }

    /*Separate review list*/
    public static ArrayList<ProductReviewListDataSet> getProductReviewList(String data) {

        ArrayList<ProductReviewListDataSet> mReviewList = new ArrayList<>();
        ProductReviewListDataSet mProductReviewListDataSet;
        try {
            JSONObject mJsonParentObjectProduct = new JSONObject(data);
            JSONObject mJsonParentObject = mJsonParentObjectProduct.getJSONObject("product");

            JSONArray mReviewListJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_PRODUCT_REVIEW_LIST);
            if (mReviewListJsonArrayObject.length() != 0) {
                for (int i = 0; i < mReviewListJsonArrayObject.length(); i++) {
                    JSONObject mReviewListJsonObject = mReviewListJsonArrayObject.getJSONObject(i);
                    mProductReviewListDataSet = new ProductReviewListDataSet();
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_RATING)) {
                        mProductReviewListDataSet.setProduct_rating(mReviewListJsonObject.getString(JSON_Names.KEY_RATING));
                    } else {
                        mProductReviewListDataSet.setProduct_rating(null);
                    }
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_PRODUCT_TEXT)) {
                        mProductReviewListDataSet.setProduct_text(mReviewListJsonObject.getString(JSON_Names.KEY_PRODUCT_TEXT));
                    } else {
                        mProductReviewListDataSet.setProduct_text(null);
                    }
                    if (!mReviewListJsonObject.isNull(JSON_Names.KEY_PRODUCT_AUTHOR)) {
                        mProductReviewListDataSet.setProduct_author(mReviewListJsonObject.getString(JSON_Names.KEY_PRODUCT_AUTHOR));
                    } else {
                        mProductReviewListDataSet.setProduct_author(null);
                    }
                    if (!mReviewListJsonObject.isNull("date_added")) {
                        mProductReviewListDataSet.setProduct_date(mReviewListJsonObject.getString("date_added"));
                    } else {
                        mProductReviewListDataSet.setProduct_date(null);
                    }
                    mReviewList.add(mProductReviewListDataSet);
                }
            }
            return mReviewList;
        } catch (Exception e) {
            return null;
        }
    }

    private static ProductDataSet getSeparateProductProductDataSet(String data) {
        ProductDataSet mProductDataSet = new ProductDataSet();
        try {
            if (data != null) {
                JSONObject mJsonParentObjectProduct = new JSONObject(data);
                JSONObject object = mJsonParentObjectProduct.getJSONObject("product");
                if (!object.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                    mProductDataSet.setProduct_id(object.getString(JSON_Names.KEY_PRODUCT_ID));
                } else {
                    mProductDataSet.setProduct_id(null);
                }
                if (!object.isNull(JSON_Names.KEY_NAME)) {
                    mProductDataSet.setTitle(object.getString(JSON_Names.KEY_NAME));
                } else {
                    mProductDataSet.setTitle(null);
                }
                if (!object.isNull(JSON_Names.KEY_MINIMUM)) {
                    mProductDataSet.setMinimum(object.getString(JSON_Names.KEY_MINIMUM));
                } else {
                    mProductDataSet.setMinimum(null);
                }
                if (!object.isNull(JSON_Names.KEY_PRICE)) {
                    mProductDataSet.setPrice(object.getString(JSON_Names.KEY_PRICE));
                } else {
                    mProductDataSet.setPrice(null);
                }
                if (!object.isNull(JSON_Names.KEY_SPECIAL)) {
                    mProductDataSet.setSpecial_price(object.getString(JSON_Names.KEY_SPECIAL));
                } else {
                    mProductDataSet.setSpecial_price(null);
                }
                if (!object.isNull(JSON_Names.KEY_IMAGE)) {
                    mProductDataSet.setImage(object.getString(JSON_Names.KEY_IMAGE));
                } else {
                    mProductDataSet.setImage(null);
                }
                if (!object.isNull(JSON_Names.KEY_QUANTITY)) {
                    mProductDataSet.setQuantity(object.getString(JSON_Names.KEY_QUANTITY));
                } else {
                    mProductDataSet.setQuantity(null);
                }
                if (!object.isNull(JSON_Names.KEY_REVIEWS)) {
                    mProductDataSet.setNo_of_review(object.getString(JSON_Names.KEY_REVIEWS));
                } else {
                    mProductDataSet.setNo_of_review(null);
                }
                if (!object.isNull(JSON_Names.KEY_RATING)) {
                    mProductDataSet.setRating(object.getString(JSON_Names.KEY_RATING));
                } else {
                    mProductDataSet.setRating("1");
                }
                if (!object.isNull(JSON_Names.KEY_DESCRIPTION)) {
                    mProductDataSet.setDescription(object.getString(JSON_Names.KEY_DESCRIPTION));
                } else {
                    mProductDataSet.setDescription(null);
                }
                if (!object.isNull(JSON_Names.KEY_STOCK_STATUS)) {
                    mProductDataSet.setStock_status(object.getString(JSON_Names.KEY_STOCK_STATUS));
                } else {
                    mProductDataSet.setStock_status(null);
                }
                if (!object.isNull(JSON_Names.KEY_MANUFACTURER)) {
                    mProductDataSet.setManufacturer(object.getString(JSON_Names.KEY_MANUFACTURER));
                } else {
                    mProductDataSet.setManufacturer(null);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return mProductDataSet;

    }

    public static ArrayList<Object> getProductSpecificationForSpecification(String data) {
        ArrayList<Object> result = new ArrayList<>();
        ProductSpecificationDataSet mProductSpecificationDataSet;
        ProductDataSet productDataSet;
        try {
            if (data != null) {
                productDataSet = getSeparateProductProductDataSet(data);
                result.add(productDataSet);
                JSONObject mJsonParentObjectProduct = new JSONObject(data);
                JSONObject mJsonParentObject = mJsonParentObjectProduct.getJSONObject("product");
                if (!mJsonParentObject.isNull(JSON_Names.KEY_SPECIFICATION)) {
                    JSONArray mSpecificationJsonArrayObject = mJsonParentObject.getJSONArray(JSON_Names.KEY_SPECIFICATION);
                    for (int i = 0; i < mSpecificationJsonArrayObject.length(); i++) {
                        JSONObject mSpecificationListJsonObject = mSpecificationJsonArrayObject.getJSONObject(i);
                        result.add(mSpecificationListJsonObject.getString(JSON_Names.KEY_NAME));
                        JSONArray mSpecificationJsonArrayObjectChild = mSpecificationListJsonObject.getJSONArray(JSON_Names.KEY_SPECIFICATION_ATTRIBUTE);
                        if (mSpecificationJsonArrayObjectChild != null) {
                            for (int j = 0; j < mSpecificationJsonArrayObjectChild.length(); j++) {
                                mProductSpecificationDataSet = new ProductSpecificationDataSet();
                                JSONObject mSpecificationListJsonObjectChild = mSpecificationJsonArrayObjectChild.getJSONObject(j);
                                mProductSpecificationDataSet.setProduct_name(mSpecificationListJsonObjectChild.getString(JSON_Names.KEY_NAME));
                                mProductSpecificationDataSet.setProduct_text(mSpecificationListJsonObjectChild.getString(JSON_Names.KEY_PRODUCT_TEXT));
                                result.add(mProductSpecificationDataSet);
                            }
                        }
                    }
                    return result;
                }
                return result;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static AccountDataSet getLoginData(String data) {
        AccountDataSet dataSet = new AccountDataSet();
        try {
            if (data != null) {
                JSONObject object = new JSONObject(data);
                if (!object.isNull(JSON_Names.KEY_STATUS)) {
                    dataSet.setmStatus(object.getString(JSON_Names.KEY_STATUS));
                } else {
                    dataSet.setmStatus(null);
                }
                JSONObject childJsonObject = object.getJSONObject(JSON_Names.KEY_CUSTOMER);
                dataSet.setmAccountString(data);
                if (!childJsonObject.isNull(JSON_Names.KEY_CUSTOMER_ID)) {
                    dataSet.setmCustomerId(childJsonObject.getString(JSON_Names.KEY_CUSTOMER_ID));
                } else {
                    dataSet.setmCustomerId(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_FIRST_NAME)) {
                    dataSet.setmFirstName(childJsonObject.getString(JSON_Names.KEY_FIRST_NAME));
                } else {
                    dataSet.setmFirstName(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_LAST_NAME)) {
                    dataSet.setmLastName(childJsonObject.getString(JSON_Names.KEY_LAST_NAME));
                } else {
                    dataSet.setmLastName(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_EMAIL)) {
                    dataSet.setmEmailId(childJsonObject.getString(JSON_Names.KEY_EMAIL));
                } else {
                    dataSet.setmEmailId(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_TELEPHONE)) {
                    dataSet.setmTelePhone(childJsonObject.getString(JSON_Names.KEY_TELEPHONE));
                } else {
                    dataSet.setmTelePhone(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_FAX)) {
                    dataSet.setmFax(childJsonObject.getString(JSON_Names.KEY_FAX));
                } else {
                    dataSet.setmFax(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_COMPANY)) {
                    dataSet.setmCompany(childJsonObject.getString(JSON_Names.KEY_COMPANY));
                } else {
                    dataSet.setmCompany(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_1)) {
                    dataSet.setmAddress_1(childJsonObject.getString(JSON_Names.KEY_ADDRESS_1));
                } else {
                    dataSet.setmAddress_1(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_2)) {
                    dataSet.setmAddress_2(childJsonObject.getString(JSON_Names.KEY_ADDRESS_2));
                } else {
                    dataSet.setmAddress_2(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_ID)) {
                    dataSet.setmAddress_Id(childJsonObject.getString(JSON_Names.KEY_ADDRESS_ID));
                } else {
                    dataSet.setmAddress_Id(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_CITY)) {
                    dataSet.setmCity(childJsonObject.getString(JSON_Names.KEY_CITY));
                } else {
                    dataSet.setmCity(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_POSTCODE)) {
                    dataSet.setmPostcode(childJsonObject.getString(JSON_Names.KEY_POSTCODE));
                } else {
                    dataSet.setmPostcode(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_ZONE_ID)) {
                    dataSet.setmZone_id(childJsonObject.getString(JSON_Names.KEY_ZONE_ID));
                } else {
                    dataSet.setmZone_id(null);
                }
                if (!childJsonObject.isNull(JSON_Names.KEY_COUNTRY)) {
                    dataSet.setmCountry(childJsonObject.getString(JSON_Names.KEY_COUNTRY));
                } else {
                    dataSet.setmCountry(null);
                }
                return dataSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }


    }

    public static ArrayList<AccountDataSet> getCustomerAddress(String data) {
        ArrayList<AccountDataSet> list = new ArrayList<>();
        AccountDataSet dataSet;
        try {
            if (data != null) {
                JSONObject object = new JSONObject(data);
                JSONArray childJsonArray = object.getJSONArray(JSON_Names.KEY_CUSTOMER_ADDRESS);
                for (int i = 0; i < childJsonArray.length(); i++) {
                    dataSet = new AccountDataSet();
                    JSONObject childJsonObject = childJsonArray.getJSONObject(i);

                    if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_ID)) {
                        dataSet.setmAddress_Id(childJsonObject.getString(JSON_Names.KEY_ADDRESS_ID));
                    } else {
                        dataSet.setmAddress_Id(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_CUSTOMER_ID)) {
                        dataSet.setmCustomerId(childJsonObject.getString(JSON_Names.KEY_CUSTOMER_ID));
                    } else {
                        dataSet.setmCustomerId(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_FIRST_NAME)) {
                        dataSet.setmFirstName(childJsonObject.getString(JSON_Names.KEY_FIRST_NAME));
                    } else {
                        dataSet.setmFirstName(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_LAST_NAME)) {
                        dataSet.setmLastName(childJsonObject.getString(JSON_Names.KEY_LAST_NAME));
                    } else {
                        dataSet.setmLastName(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_COMPANY)) {
                        dataSet.setmCompany(childJsonObject.getString(JSON_Names.KEY_COMPANY));
                    } else {
                        dataSet.setmCompany(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_1)) {
                        dataSet.setmAddress_1(childJsonObject.getString(JSON_Names.KEY_ADDRESS_1));
                    } else {
                        dataSet.setmAddress_1(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_ADDRESS_2)) {
                        dataSet.setmAddress_2(childJsonObject.getString(JSON_Names.KEY_ADDRESS_2));
                    } else {
                        dataSet.setmAddress_2(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_CITY)) {
                        dataSet.setmCity(childJsonObject.getString(JSON_Names.KEY_CITY));
                    } else {
                        dataSet.setmCity(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_POSTCODE)) {
                        dataSet.setmPostcode(childJsonObject.getString(JSON_Names.KEY_POSTCODE));
                    } else {
                        dataSet.setmPostcode(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_COUNTRY_ID)) {
                        dataSet.setmCountry_id(childJsonObject.getString(JSON_Names.KEY_COUNTRY_ID));
                    } else {
                        dataSet.setmCountry_id(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_ZONE_ID)) {
                        dataSet.setmZone_id(childJsonObject.getString(JSON_Names.KEY_ZONE_ID));
                    } else {
                        dataSet.setmZone_id(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_COUNTRY)) {
                        dataSet.setmCountry(childJsonObject.getString(JSON_Names.KEY_COUNTRY));
                    } else {
                        dataSet.setmCountry(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_STATE)) {
                        dataSet.setmState(childJsonObject.getString(JSON_Names.KEY_STATE));
                    } else {
                        dataSet.setmState(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_EMAIL)) {
                        dataSet.setmEmailId(childJsonObject.getString(JSON_Names.KEY_EMAIL));
                    } else {
                        dataSet.setmEmailId(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_TELEPHONE)) {
                        dataSet.setmTelePhone(childJsonObject.getString(JSON_Names.KEY_TELEPHONE));
                    } else {
                        dataSet.setmTelePhone(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_DEFAULT_ADDRESS_ID)) {
                        dataSet.setmDefaultAddressId(childJsonObject.getString(JSON_Names.KEY_DEFAULT_ADDRESS_ID));
                    } else {
                        dataSet.setmDefaultAddressId(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_DEFAULT_FIRST_NAME)) {
                        dataSet.setmDefaultFirstName(childJsonObject.getString(JSON_Names.KEY_DEFAULT_FIRST_NAME));
                    } else {
                        dataSet.setmDefaultFirstName(null);
                    }
                    if (!childJsonObject.isNull(JSON_Names.KEY_DEFAULT_LAST_NAME)) {
                        dataSet.setmDefaultLastName(childJsonObject.getString(JSON_Names.KEY_DEFAULT_LAST_NAME));
                    } else {
                        dataSet.setmDefaultLastName(null);
                    }

                    list.add(dataSet);
                }
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public static ArrayList<SpinnerDataSet> getCountryList(String data, Context context) {
        ArrayList<SpinnerDataSet> countryArrayList = new ArrayList<>();
        SpinnerDataSet spinnerDataSet;
        SpinnerCountryList spinnerCountryList;
        try {
            SpinnerDataSet spinnerDataSetEmpty = new SpinnerDataSet();
            spinnerDataSetEmpty.set_id(-1);
            spinnerDataSetEmpty.set_name(context.getResources().getString(R.string.sign_up_please_select));
            spinnerDataSetEmpty.setStateList(null);

            countryArrayList.add(spinnerDataSetEmpty);

            JSONArray jArray = new JSONArray(data);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject_country = jArray.getJSONObject(i);

                if (!jObject_country.isNull(JSON_Names.KEY_COUNTRY_ID)) {

                    spinnerDataSet = new SpinnerDataSet();

                    if (!jObject_country.isNull(JSON_Names.KEY_NAME)) {
                        spinnerDataSet.set_name(jObject_country.getString(JSON_Names.KEY_NAME));
                    } else {
                        spinnerDataSet.set_name(null);
                    }

                    if (!jObject_country.isNull(JSON_Names.KEY_COUNTRY_ID)) {
                        spinnerDataSet.set_id(jObject_country.getInt(JSON_Names.KEY_COUNTRY_ID));
                    } else {
                        spinnerDataSet.set_id(-1);
                    }

                    JSONArray jsonArray = jObject_country.getJSONArray(JSON_Names.KEY_STATE);
                    if (jsonArray.length() != 0) {
                        ArrayList<SpinnerCountryList> mStateArray = new ArrayList<>();
                        SpinnerCountryList spinnerCountryListEmpty = new SpinnerCountryList();
                        spinnerCountryListEmpty.set_name(context.getResources().getString(R.string.sign_up_please_select));
                        spinnerCountryListEmpty.set_id(-1);
                        mStateArray.add(spinnerCountryListEmpty);

                        for (int j = 0; j < jsonArray.length(); j++) {
                            spinnerCountryList = new SpinnerCountryList();
                            JSONObject jObject_state = jsonArray.getJSONObject(j);

                            if (!jObject_state.isNull(JSON_Names.KEY_NAME)) {
                                spinnerCountryList.set_name(jObject_state.getString(JSON_Names.KEY_NAME));
                            } else {
                                spinnerCountryList.set_name(null);
                            }

                            if (!jObject_state.isNull(JSON_Names.KEY_STATE_ID)) {
                                spinnerCountryList.set_id(jObject_state.getInt(JSON_Names.KEY_STATE_ID));
                            } else {
                                spinnerCountryList.set_id(-1);
                            }
                            mStateArray.add(spinnerCountryList);
                        }
                        spinnerDataSet.setStateList(mStateArray);
                    }
                    countryArrayList.add(spinnerDataSet);
                }
            }
            return countryArrayList;
        } catch (Exception e) {
            return null;
        }

    }

    public static Response getResponse(String data) {

        try {
            Response response = new Response();
            JSONObject object = new JSONObject(data);
            if (!object.isNull(JSON_Names.KEY_STATUS)) {
                response.setmStatus(object.getInt(JSON_Names.KEY_STATUS));
            } else {
                response.setmStatus(0);
            }
            if (!object.isNull(JSON_Names.KEY_MESSAGE)) {
                response.setmMessage(object.getString(JSON_Names.KEY_MESSAGE));
            } else {
                response.setmMessage(null);
            }
            return response;

        } catch (Exception e) {
            return null;
        }
    }

    public static String getResponseStatus(String data) {
        String result;
        try {
            JSONObject object = new JSONObject(data);
            if (!object.isNull(JSON_Names.KEY_STATUS)) {
                result = object.getString(JSON_Names.KEY_STATUS);
            } else {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    public static ArrayList<ShippingAndPayment_DataSet> getShippingMethod(String data) {
        ArrayList<ShippingAndPayment_DataSet> shippingAndPayment_dataSetArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_Names.KEY_RESPONSE_SHIPPING_METHOD);
            for (int i = 0; i < jsonArray.length(); i++) {
                ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
                JSONObject childJSONObject = jsonArray.getJSONObject(i);
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_CODE)) {
                    dataSet.setmCode(childJSONObject.getString(JSON_Names.KEY_RESPONSE_CODE));
                } else {
                    dataSet.setmCode(null);
                }
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_TITLE)) {
                    dataSet.setmTitle(childJSONObject.getString(JSON_Names.KEY_RESPONSE_TITLE));
                } else {
                    dataSet.setmTitle(null);
                }
                JSONArray childJSONArray = childJSONObject.getJSONArray(JSON_Names.KEY_RESPONSE_QUOTE);
                for (int j = 0; j < childJSONArray.length(); j++) {
                    JSONObject quoteJSONObject = childJSONArray.getJSONObject(j);
                    if (!quoteJSONObject.isNull(JSON_Names.KEY_RESPONSE_COST)) {
                        dataSet.setmCost(quoteJSONObject.getString(JSON_Names.KEY_RESPONSE_COST));
                    } else {
                        dataSet.setmCost(null);
                    }
                    if (!quoteJSONObject.isNull(JSON_Names.KEY_RESPONSE_TAX_CLASS_ID)) {
                        dataSet.setmTaxClassId(quoteJSONObject.getString(JSON_Names.KEY_RESPONSE_TAX_CLASS_ID));
                    } else {
                        dataSet.setmTaxClassId(null);
                    }
                }
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_SORT_ORDER)) {
                    dataSet.setmSortOrder(childJSONObject.getString(JSON_Names.KEY_RESPONSE_SORT_ORDER));
                } else {
                    dataSet.setmSortOrder(null);
                }
                shippingAndPayment_dataSetArrayList.add(dataSet);
            }
            return shippingAndPayment_dataSetArrayList;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<ShippingAndPayment_DataSet> getPaymentMethod(String data) {
        ArrayList<ShippingAndPayment_DataSet> shippingAndPayment_dataSetArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_Names.KEY_RESPONSE_PAYMENT_METHOD);

            for (int i = 0; i < jsonArray.length(); i++) {
                ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
                JSONObject childJSONObject = jsonArray.getJSONObject(i);
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_CODE)) {
                    dataSet.setmCode(childJSONObject.getString(JSON_Names.KEY_RESPONSE_CODE));
                } else {
                    dataSet.setmCode(null);
                }
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_TITLE)) {
                    dataSet.setmTitle(childJSONObject.getString(JSON_Names.KEY_RESPONSE_TITLE));
                } else {
                    dataSet.setmTitle(null);
                }
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_SORT_ORDER)) {
                    dataSet.setmSortOrder(childJSONObject.getString(JSON_Names.KEY_RESPONSE_SORT_ORDER));
                } else {
                    dataSet.setmSortOrder(null);
                }
                if (!childJSONObject.isNull(JSON_Names.KEY_RESPONSE_TERMS)) {
                    dataSet.setmTerms(childJSONObject.getString(JSON_Names.KEY_RESPONSE_TERMS));
                } else {
                    dataSet.setmTerms(null);
                }
                shippingAndPayment_dataSetArrayList.add(dataSet);
            }
            return shippingAndPayment_dataSetArrayList;
        } catch (Exception e) {
            return null;
        }
    }

    public static ConfirmResponseDataSet get_confirmation_detail(String data) {
        ArrayList<ConfirmResponseDataSet> totals_list = new ArrayList<>();
        ConfirmResponseDataSet confirmResponseDataSet = new ConfirmResponseDataSet();
        ConfirmResponseDataSet confirmTotalsResponseDataSet;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject order_info = jsonObject.getJSONObject(JSON_Names.KEY_CONFIRMATION_ORDER_INFO);
            if (!order_info.isNull(JSON_Names.KEY_CONFIRMATION_ORDER_ID)) {
                confirmResponseDataSet.setmOrderId(order_info.getString(JSON_Names.KEY_CONFIRMATION_ORDER_ID));
            } else {
                confirmResponseDataSet.setmOrderId(null);
            }
            JSONArray jsonArrayTotals = order_info.getJSONArray(JSON_Names.KEY_CONFIRMATION_ORDER_TOTALS);
            if (jsonArrayTotals.length() > 0) {
                for (int i = 0; i < jsonArrayTotals.length(); i++) {
                    confirmTotalsResponseDataSet = new ConfirmResponseDataSet();
                    JSONObject jsonObjectTotals = jsonArrayTotals.getJSONObject(i);
                    if (!jsonObjectTotals.isNull(JSON_Names.KEY_RESPONSE_TITLE)) {
                        confirmTotalsResponseDataSet.setmTotalsTitle(jsonObjectTotals.getString(JSON_Names.KEY_RESPONSE_TITLE));
                    } else {
                        confirmTotalsResponseDataSet.setmTotalsTitle(null);
                    }
                    if (!jsonObjectTotals.isNull(JSON_Names.KEY_CONFIRMATION_ORDER_VALUE)) {
                        confirmTotalsResponseDataSet.setmTotalsValue(jsonObjectTotals.getString(JSON_Names.KEY_CONFIRMATION_ORDER_VALUE));
                    } else {
                        confirmTotalsResponseDataSet.setmTotalsValue(null);
                    }
                    totals_list.add(confirmTotalsResponseDataSet);
                }
                confirmResponseDataSet.setmTotalList(totals_list);
            } else {
                confirmResponseDataSet.setmTotalList(null);
            }

            return confirmResponseDataSet;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<CartDataSet> get_cart_detail(String data) {
        ArrayList<CartDataSet> dataSets = new ArrayList<>();
        int option_size;
        CartDataSet productDataSet;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_Names.KEY_CART);
            for (int i = 0; i < jsonArray.length(); i++) {
                productDataSet = new CartDataSet();
                JSONObject product_object = jsonArray.getJSONObject(i);
                if (!product_object.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                    productDataSet.setProduct_id(product_object.getString(JSON_Names.KEY_PRODUCT_ID));
                } else {
                    productDataSet.setProduct_id(null);
                }
                if (!product_object.isNull(JSON_Names.KEY_MINIMUM)) {
                    productDataSet.setMinimum(product_object.getInt(JSON_Names.KEY_MINIMUM));
                } else {
                    productDataSet.setMinimum(0);
                }
                if (!product_object.isNull(JSON_Names.KEY_NAME)) {
                    productDataSet.setTitle(product_object.getString(JSON_Names.KEY_NAME));
                } else {
                    productDataSet.setTitle(null);
                }
                if (!product_object.isNull(JSON_Names.KEY_IMAGE)) {
                    productDataSet.setImage(product_object.getString(JSON_Names.KEY_IMAGE));
                } else {
                    productDataSet.setImage(null);
                }
                if (!product_object.isNull(JSON_Names.KEY_QUANTITY)) {
                    productDataSet.setQuantity(product_object.getString(JSON_Names.KEY_QUANTITY));
                } else {
                    productDataSet.setQuantity(null);
                }
                if (!product_object.isNull(JSON_Names.KEY_PRICE)) {
                    productDataSet.setPrice(product_object.getString(JSON_Names.KEY_PRICE));
                } else {
                    productDataSet.setPrice(null);
                }
                if (!product_object.isNull(JSON_Names.KEY_TOTAL)) {
                    productDataSet.setTotal(product_object.getString(JSON_Names.KEY_TOTAL));
                } else {
                    productDataSet.setTotal(null);
                }
                if (!product_object.isNull("stock")) {
                    productDataSet.setStock_status(product_object.getBoolean("stock"));
                } else {
                    productDataSet.setStock_status(false);
                }
                productDataSet.setLoad(false);

                if (!product_object.isNull("option")) {
                    JSONArray option_array = product_object.getJSONArray("option");
                    ArrayList<Integer[]> list = new ArrayList<>();
                    for (int j = 0; j < option_array.length(); j++) {
                        JSONObject option_object = option_array.getJSONObject(j);
                        Integer[] value = new Integer[2];
                        if (!option_object.isNull("product_option_id")) {
                            value[0] = option_object.getInt("product_option_id");
                        } else {
                            value[0] = 0;
                        }
                        if (!option_object.isNull("product_option_value_id")) {
                            value[1] = option_object.getInt("product_option_value_id");
                        } else {
                            value[1] = 0;
                        }

                        list.add(value);
                    }
                    productDataSet.setIndex(get_index(productDataSet.getProduct_id(), list));
                }

                if (!product_object.isNull("quantity")) {
                    option_size = product_object.getInt("quantity");
                } else {
                    option_size = -1;
                }
                productDataSet.setSubtract(option_size);

                dataSets.add(productDataSet);
            }
            return dataSets;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<FilterDataSet> get_cart_total_new(String data) {
        try {
            ArrayList<FilterDataSet> total_list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(data);
            FilterDataSet cartTotalSet;
            JSONObject jsonWholeObject = jsonObject.getJSONObject("cart_info");
            if (jsonWholeObject.getJSONArray("totals") != null) {
                JSONArray jsonArrayShippingList = jsonWholeObject.getJSONArray("totals");
                for (int i = 0; i < jsonArrayShippingList.length(); i++) {
                    JSONObject childJSONList = jsonArrayShippingList.getJSONObject(i);
                    cartTotalSet = new FilterDataSet();
                    if (!childJSONList.isNull("title")) {
                        cartTotalSet.setmName(childJSONList.getString("title"));
                    } else {
                        cartTotalSet.setmName(null);
                    }
                    if (!childJSONList.isNull("value")) {
                        cartTotalSet.setmFilterId(childJSONList.getString("value"));
                    } else {
                        cartTotalSet.setmFilterId(null);
                    }
                    total_list.add(cartTotalSet);
                }
            }
            return total_list;
        } catch (Exception e) {
            return null;
        }
    }

    private static int get_index(String product_id, ArrayList<Integer[]> list) {

        ArrayList<Integer> index_list = DataBaseHandlerCart.getInstance(Application_Context.getAppContext()).get_index(product_id);
        if (index_list != null) {
            if (index_list.size() == 1) {
                return index_list.get(0);
            } else {
                for (int i = 0; i < index_list.size(); i++) {
                    if (DataBaseHandlerCartOptions.getInstance(Application_Context.getAppContext()).check_option_index(index_list.get(i))) {
                        ArrayList<Integer[]> db_data = DataBaseHandlerCartOptions.getInstance(Application_Context.getAppContext()).option_checking(index_list.get(i));
                        if (db_data != null && list != null) {
                            if (db_data.size() == list.size()) {
                                int size = 0;
                                for (int j = 0; j < db_data.size(); j++) {
                                    Integer[] array_list = list.get(j);
                                    for (int k = 0; k < db_data.size(); k++) {
                                        Integer[] array_db_list = db_data.get(k);
                                        if (array_list[0].equals(array_db_list[0]) && array_list[1].equals(array_db_list[1])) {
                                            size = size + 1;
                                        }
                                    }
                                }
                                if (size == db_data.size()) {
                                    return index_list.get(i);
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static int get_cart_max_reward_point(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);

            if (!jsonObject.isNull("maximum_reward_points")) {
                return jsonObject.getInt("maximum_reward_points");
            } else {
                return 0;
            }

        } catch (Exception e) {
            return 0;
        }
    }

    public static int get_cart_customer_reward_point(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);

            if (!jsonObject.isNull("customer_reward_points")) {
                return jsonObject.getInt("customer_reward_points");
            } else {
                return 0;
            }

        } catch (Exception e) {
            return 0;
        }
    }

    public static ArrayList<RewardPointsData> getRewardPointData(String data) {
        try {
            ArrayList<RewardPointsData> mRewardPointsList = new ArrayList<>();

            JSONObject mParentJsonObject = new JSONObject(data);
            JSONArray mRewardList = mParentJsonObject.getJSONArray("orders");
            if (mRewardList != null) {
                if (mRewardList.length() > 0) {
                    for (int i = 0; i < mRewardList.length(); i++) {
                        JSONObject jsonChildObject = mRewardList.getJSONObject(i);
                        RewardPointsData rewardPointsData = new RewardPointsData();

                        if (!jsonChildObject.isNull("points")) {
                            rewardPointsData.setmPoints(jsonChildObject.getString("points"));
                        } else {
                            rewardPointsData.setmPoints(null);
                        }
                        if (!jsonChildObject.isNull("description")) {
                            rewardPointsData.setmDescription(jsonChildObject.getString("description"));
                        } else {
                            rewardPointsData.setmDescription(null);
                        }
                        if (!jsonChildObject.isNull("date_added")) {
                            rewardPointsData.setmDateAdded(jsonChildObject.getString("date_added"));
                        } else {
                            rewardPointsData.setmDateAdded(null);
                        }

                        mRewardPointsList.add(rewardPointsData);
                    }

                    return mRewardPointsList;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRewardPointTotal(String data) {
        try {
            JSONObject mParentJsonObject = new JSONObject(data);
            if (!mParentJsonObject.isNull("total")) {
                return mParentJsonObject.getString("total");
            } else {
                return "0";
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] get_slide(String data) {
        try {
            JSONObject parent_object = new JSONObject(data);
            JSONArray child = parent_object.getJSONArray("slider");
            if (child != null) {
                String images[] = new String[child.length()];
                for (int i = 0; i < child.length(); i++) {
                    JSONObject child_ = child.getJSONObject(i);
                    if (!child_.isNull(JSON_Names.KEY_IMAGE)) {
                        images[i] = child_.getString(JSON_Names.KEY_IMAGE);
                    } else {
                        images[i] = null;
                    }
                }
                return images;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, ArrayList<String[]>> get_cart_options(String data) {
        HashMap<String, ArrayList<String[]>> list = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_Names.KEY_CART);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product_object = jsonArray.getJSONObject(i);
                if (!product_object.isNull(JSON_Names.KEY_PRODUCT_ID)) {
                    String id = product_object.getString(JSON_Names.KEY_PRODUCT_ID);
                    JSONArray option_array = product_object.getJSONArray("option");
                    ArrayList<String[]> option_list = new ArrayList<>();
                    for (int j = 0; j < option_array.length(); j++) {
                        JSONObject option_object = option_array.getJSONObject(j);
                        String[] value = new String[2];
                        if (!option_object.isNull("name")) {
                            value[0] = option_object.getString("name");
                        } else {
                            value[0] = "0";
                        }
                        if (!option_object.isNull("value")) {
                            value[1] = option_object.getString("value");
                        } else {
                            value[1] = "0";
                        }
                        option_list.add(value);
                    }
                    list.put(id + "" + i, option_list);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCustomerStatus(String data) {
        String mCustomerStatus;
        try {
            JSONObject parent_object = new JSONObject(data);
            if(parent_object.length() > 0){
                if (!parent_object.isNull(JSON_Names.KEY_CUSTOMER_STATUS)) {
                    mCustomerStatus = parent_object.getString(JSON_Names.KEY_CUSTOMER_STATUS);
                } else {
                    mCustomerStatus = "";
                }
               // Log.e("mCustomerStatus" ," "+mCustomerStatus);
                return mCustomerStatus;
            }else {
               // Log.e("mCustomerStatus" ,"api empty");
                return null;

            }

        } catch (Exception e) {
          //  Log.e("mCustomerStatus Exc" ,e.toString());
            return null;
        }
    }

}
