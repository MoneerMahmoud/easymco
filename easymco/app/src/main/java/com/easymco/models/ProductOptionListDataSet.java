package com.easymco.models;


import java.util.ArrayList;

public class ProductOptionListDataSet {
    private ProductOptionDataSet productOptionDataSet=new ProductOptionDataSet();
    private ArrayList<ProductOptionDataSet> productOptionDataSetsList=new ArrayList<>();

    public ProductOptionDataSet getProductOptionDataSet() {
        return productOptionDataSet;
    }

    public void setProductOptionDataSet(ProductOptionDataSet productOptionDataSet) {
        this.productOptionDataSet = productOptionDataSet;
    }

    public ArrayList<ProductOptionDataSet> getProductOptionDataSetsList() {
        return productOptionDataSetsList;
    }

    public void setProductOptionDataSetsList(ArrayList<ProductOptionDataSet> productOptionDataSetsList) {
        this.productOptionDataSetsList = productOptionDataSetsList;
    }
}
