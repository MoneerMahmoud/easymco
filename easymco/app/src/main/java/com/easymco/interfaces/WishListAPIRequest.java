package com.easymco.interfaces;

import android.widget.ImageView;

public interface WishListAPIRequest {
    void wish_list_api_request(String data, String url[],Boolean isAdd);
    void transaction(String product_id, ImageView transaction_view, String image_url);
}
