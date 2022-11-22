package com.easymco.constant_class;

public class URL_Class {

    /*Symbols*/

    public static String mEqual_Symbol = "=";

    public static String mAnd_Symbol = "&";

    public static String mConvertType = "UTF-8";

    public static String mMail_Pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static String mLogin_UserName = "username";

    public static String mLogin_Password = "password";

    /*ID's*/

    public static String mUser_Id = "&user_id=";

    public static String mAddress_Id = "&address_id=";

    public static String mProduct_id = "&product_id=";

    public static String mCategory_id = "&category_id=";

    public static String mLanguage_ENGLISH = "language_id=1";

    public static String mLanguage_ARABIC = "language_id=2";

    public static String mURL_GetFilter_For_Filter_Id = "&filters=";

    public static String mURL_Search = "&filter_name=";

    public static String mCustomer_id = "customer_id=";

     /*Sorting*/

    public static String mURL_GetCategory_SortList_Default = "&sort=p.sort_order&order=ASC";

    public static String mURL_GetCategory_SortList_ByName_ASC = "&sort=pd.name&order=ASC";

    public static String mURL_GetCategory_SortList_ByName_DESC = "&sort=pd.name&order=DESC";

    public static String mURL_GetCategory_SortList_ByPrice_ASC = "&sort=p.price&order=ASC";

    public static String mURL_GetCategory_SortList_ByPrice_DESC = "&sort=p.price&order=DESC";

    public static String mURL_GetCategory_SortList_ByRating_DESC = "&sort=rating&order=DESC";

    public static String mURL_GetCategory_SortList_ByRating_ASC = "&sort=rating&order=ASC";

    public static String mURL_GetCategory_SortList_ByModel_ASC = "&sort=p.model&order=ASC";

    public static String mURL_GetCategory_SortList_ByModel_DESC = "&sort=p.model&order=DESC";

    /*Main*/

    public static String mURL = "http://easymco.com/api/api.php?rquest=";

    public static String mShare = "http://easymco.com/index.php?route=product/product&product_id=";

    public static String mDescription = "http://easymco.com/index.php?route=product/productdesc&product_id=";

    public static String mAbout_us = "http://easymco.com/index.php?route=information/information&information_id=4";

    /*Filter*/

    public static String mURL_GetFilter_For_Category = "getfilters&";

    /*Extra*/

    public static String mURL_SpecialProduct = "getspecialsproduct&";

    public static String mURL_FeaturedProduct = "getfeatureproduct&";

    public static String mURL_LatestProduct = "getlatestproduct&";

    /*Page limit*/

    public static String mURL_Limit = "&limit=6";

    public static String mURL_Limit_Special = "&limit=4";

    public static String mURL_Page = "&page=";

    /*Category*/

    public static String mURL_MainCategory = "getcategories&";

    public static String mURL_GetCategory_List = "getproducts&";

    /*User*/

    public static String mURL_ForgetPassword = "forgotpassword";

    public static String mURL_Login = "login";

    public static String mURL_Country = "country";

    public static String mURL_Registration = "registration";

  /*Wish list*/

    public static String mURL_Add_To_WishList = "update_wishlist";

    public static String mURL_Get_WishList = "get_wishlist&";//User id required

    public static String mURL_Remove_WishList = "remove_wishlist";

    /*Checkout*/

    public static String mURL_Get_Cart_Product = "get_cart_products";

    public static String mURL_Get_Shipping_Method = "getshipping_method";

    public static String mURL_Get_Payment_Method = "getpayment_method";

    public static String mURL_Confirm_Order = "confirmorder";

    public static String mURL_Place_order = "place_order";

    public static String mURL_Gift_Voucher = mURL + "validatevoucher";

    public static String mURL_Coupon = mURL + "validatecoupon";

    /*Search*/

    public static String mURL_Search_Product = "getproducts&";//User id required

    /*Review*/

    public static String mURL_Review_Post = "post_review";

    /*Account*/

    public static String mURL_Get_Customer_Profile = "get_customer_address&";

    public static String mURL_Edit_Address = "update_customer_address";

    public static String mURL_New_Address = "insert_customer_address";

    public static String mURL_Delete_Address = "delete_customer_address&";

    public static String mURL_Change_Password = "change_password";

    public static String mGet_Customer_Order = "getcustomer_order&";

    public static String mUpdateAccountDetail = "update_account_detail";

    public static String mGetCustomerRewardPoints = "getcustomer_rewards";

    /*Product*/

    public static String mProductDetail = "getproduct&";

    /*Banner*/

    public static String mBanner = "get_slideshow&";

    public static String mContact_Us = "contactus";

}
