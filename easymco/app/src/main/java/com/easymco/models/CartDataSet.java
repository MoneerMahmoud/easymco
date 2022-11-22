package com.easymco.models;

public class CartDataSet {
    private String product_id;
    private String title;
    private String image;
    private String price;
    private String rating;
    private String special_price;
    private String product_string;
    private String quantity;
    private String no_of_review;
    private String description;
    private String manufacturer;
    private int minimum;
    private int list_count;
    private String cart_total;
    private String total;
    private boolean stock_status,load;
    private int index;
    private int subtract;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_string() {
        return product_string;
    }

    public void setProduct_string(String product_string) {
        this.product_string = product_string;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(String special_price) {
        this.special_price = special_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNo_of_review() {
        return no_of_review;
    }

    public void setNo_of_review(String no_of_review) {
        this.no_of_review = no_of_review;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStock_status() {
        return stock_status;
    }

    public void setStock_status(boolean stock_status) {
        this.stock_status = stock_status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }


    public int getList_count() {
        return list_count;
    }

    public void setList_count(int list_count) {
        this.list_count = list_count;
    }

    public String getCart_total() {
        return cart_total;
    }

    public void setCart_total(String cart_total) {
        this.cart_total = cart_total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSubtract() {
        return subtract;
    }

    public void setSubtract(int subtract) {
        this.subtract = subtract;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }
}
