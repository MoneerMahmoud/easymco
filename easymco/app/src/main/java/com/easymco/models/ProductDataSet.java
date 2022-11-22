package com.easymco.models;

import java.util.ArrayList;

public class ProductDataSet {

    private String product_id, title, image, price, rating, special_price, product_string, quantity, no_of_review,
            description, stock_status, manufacturer, minimum, total, heading,big_image;
    private int index, subtract;
    private ArrayList<ProductImageDataSet> productImageDataSetsList=new ArrayList<>();
    private ArrayList<ProductOptionListDataSet> productOptionListDataSetArrayList=new ArrayList<>();
    private ArrayList<ProductReviewListDataSet> mReviewList = new ArrayList<>();

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

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
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

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getSubtract() {
        return subtract;
    }

    public void setSubtract(int subtract) {
        this.subtract = subtract;
    }

    public ArrayList<ProductImageDataSet> getProductImageDataSetsList() {
        return productImageDataSetsList;
    }

    public void setProductImageDataSetsList(ArrayList<ProductImageDataSet> productImageDataSetsList) {
        this.productImageDataSetsList = productImageDataSetsList;
    }

    public ArrayList<ProductOptionListDataSet> getProductOptionListDataSetArrayList() {
        return productOptionListDataSetArrayList;
    }

    public void setProductOptionListDataSetArrayList(ArrayList<ProductOptionListDataSet> productOptionListDataSetArrayList) {
        this.productOptionListDataSetArrayList = productOptionListDataSetArrayList;
    }

    public ArrayList<ProductReviewListDataSet> getmReviewList() {
        return mReviewList;
    }

    public void setmReviewList(ArrayList<ProductReviewListDataSet> mReviewList) {
        this.mReviewList = mReviewList;
    }

    public String getBig_image() {
        return big_image;
    }

    public void setBig_image(String big_image) {
        this.big_image = big_image;
    }
}
