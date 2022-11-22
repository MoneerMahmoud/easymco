package com.easymco.interfaces;

public interface CheckOutAPIRequest {
    void checkout_delivery_detail_request();
    void checkout_payment_type_request();
    void checkout_confirmation();
    void checkout_place_order(String data);
    void checkout_edit_address();
    void checkout_edit_address_post(String data);
}
