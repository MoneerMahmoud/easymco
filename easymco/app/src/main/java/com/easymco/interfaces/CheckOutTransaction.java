package com.easymco.interfaces;

public interface CheckOutTransaction {
    void checkout_address_edit(String data);
    void checkout_delivery_type(String data);
    void checkout_payment_type(String data);
    void checkout_cart_changer();
    void checkout_confirmation(String data);
    void checkout_place_order();
}
