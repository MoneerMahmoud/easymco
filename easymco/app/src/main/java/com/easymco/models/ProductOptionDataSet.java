package com.easymco.models;

public class ProductOptionDataSet {

    private String product_option_id;
    private String product_option_name;
    private String product_option_type;
    private String product_option_required;
    private String product_option_value_id;
    private String product_name;
    private String product_option_quantity;
    private Boolean selected;

    public String getProduct_option_id() {
        return product_option_id;
    }

    public void setProduct_option_id(String product_option_id) {
        this.product_option_id = product_option_id;
    }

    public String getProduct_option_name() {
        return product_option_name;
    }

    public void setProduct_option_name(String product_option_name) {
        this.product_option_name = product_option_name;
    }

    public String getProduct_option_required() {
        return product_option_required;
    }

    public void setProduct_option_required(String product_option_required) {
        this.product_option_required = product_option_required;
    }

    public String getProduct_option_value_id() {
        return product_option_value_id;
    }

    public void setProduct_option_value_id(String product_option_value_id) {
        this.product_option_value_id = product_option_value_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_option_type() {
        return product_option_type;
    }

    public void setProduct_option_type(String product_option_type) {
        this.product_option_type = product_option_type;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getProduct_option_quantity() {
        return product_option_quantity;
    }

    public void setProduct_option_quantity(String product_option_quantity) {
        this.product_option_quantity = product_option_quantity;
    }
}
