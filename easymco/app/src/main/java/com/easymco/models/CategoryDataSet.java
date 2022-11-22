package com.easymco.models;

public class CategoryDataSet {

    private int sort_order, category_id, top, parent_id;
    private String name, image, icon_image, menu_icon;
    private boolean marked, isOpened;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public String getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(String menu_icon) {
        this.menu_icon = menu_icon;
    }

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }
}
