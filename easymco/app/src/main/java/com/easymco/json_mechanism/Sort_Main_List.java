package com.easymco.json_mechanism;

import com.easymco.models.CategoryDataSet;
import com.easymco.models.Navigation_DataSet;

import java.util.ArrayList;
import java.util.Arrays;

class Sort_Main_List {

    static ArrayList<Navigation_DataSet> getHomeListSortedNew(ArrayList<Navigation_DataSet> parentList) {

        ArrayList<Navigation_DataSet> result = new ArrayList<>();


        int values[] = new int[parentList.size()];
        for (int i = 0; i < parentList.size(); i++) {
            values[i] = parentList.get(i).getmParentList().getSort_order();
        }

        Arrays.sort(values);

        for (int value : values) {
            for (int j = 0; j < values.length; j++) {
                if (value == parentList.get(j).getmParentList().getSort_order() && parentList.get(j).getmParentList().getMarked()) {
                    result.add(parentList.get(j));
                    parentList.get(j).getmParentList().setMarked(false);
                }
            }
        }
        return result;
    }

    static ArrayList<CategoryDataSet> getParentSortedLists(ArrayList<CategoryDataSet> mPParentList) {
        ArrayList<CategoryDataSet> mParentList = new ArrayList<>();

        int values[] = new int[mPParentList.size()];
        for (int i = 0; i < mPParentList.size(); i++) {
            values[i] = mPParentList.get(i).getSort_order();
        }

        Arrays.sort(values);

        for (int value : values) {
            for (int j = 0; j < values.length; j++) {
                if (value == mPParentList.get(j).getSort_order() && mPParentList.get(j).getMarked()) {
                    CategoryDataSet parentCategory = new CategoryDataSet();
                    parentCategory.setName(mPParentList.get(j).getName());
                    parentCategory.setCategory_id(mPParentList.get(j).getCategory_id());
                    parentCategory.setTop(mPParentList.get(j).getTop());
                    parentCategory.setSort_order(mPParentList.get(j).getSort_order());
                    parentCategory.setImage(mPParentList.get(j).getImage());
                    parentCategory.setIcon_image(mPParentList.get(j).getIcon_image());
                    parentCategory.setMenu_icon(mPParentList.get(j).getMenu_icon());
                    mPParentList.get(j).setMarked(false);
                    parentCategory.setMarked(true);
                    mParentList.add(parentCategory);
                }
            }
        }
        return mParentList;
    }


    static ArrayList<CategoryDataSet> getChildSortedLists(ArrayList<CategoryDataSet> mPChildList) {
        ArrayList<CategoryDataSet> mChildList = new ArrayList<>();
        ArrayList<CategoryDataSet> mChildListFinal = new ArrayList<>();
        int k = 0;

        int values[] = new int[mPChildList.size()];
        for (int i = 0; i < mPChildList.size(); i++) {
            values[i] = mPChildList.get(i).getSort_order();
        }

        Arrays.sort(values);

        for (int value : values) {
            for (int j = 0; j < values.length; j++) {
                if (value == mPChildList.get(j).getSort_order() && mPChildList.get(j).getMarked()) {
                        if (mChildList.size() != 0) {
                            if (!mChildList.get(k).getName().equals(mPChildList.get(j).getName()) && mPChildList.get(j).getMarked()) {
                                CategoryDataSet categoryDataSet = new CategoryDataSet();
                                categoryDataSet.setCategory_id(mPChildList.get(j).getCategory_id());
                                categoryDataSet.setSort_order(mPChildList.get(j).getSort_order());
                                categoryDataSet.setTop(mPChildList.get(j).getTop());
                                categoryDataSet.setName(mPChildList.get(j).getName());
                                categoryDataSet.setImage(mPChildList.get(j).getImage());
                                categoryDataSet.setParent_id(mPChildList.get(j).getParent_id());
                                categoryDataSet.setMenu_icon(mPChildList.get(j).getMenu_icon());
                                mPChildList.get(j).setMarked(false);
                                categoryDataSet.setMarked(true);
                                mChildList.add(categoryDataSet);
                                k++;
                            }
                        } else if (mChildList.size() == 0 && mPChildList.get(j).getMarked()) {
                            CategoryDataSet categoryDataSet = new CategoryDataSet();
                            categoryDataSet.setCategory_id(mPChildList.get(j).getCategory_id());
                            categoryDataSet.setSort_order(mPChildList.get(j).getSort_order());
                            categoryDataSet.setTop(mPChildList.get(j).getTop());
                            categoryDataSet.setName(mPChildList.get(j).getName());
                            categoryDataSet.setParent_id(mPChildList.get(j).getParent_id());
                            categoryDataSet.setImage(mPChildList.get(j).getImage());
                            categoryDataSet.setMenu_icon(mPChildList.get(j).getMenu_icon());
                            mPChildList.get(j).setMarked(false);
                            categoryDataSet.setMarked(true);
                            mChildList.add(categoryDataSet);
                        }

                }
            }
        }

        //For positioning all product at bottom of list :-
        CategoryDataSet allProductCategoryDs = new CategoryDataSet();

        for(int all = 0 ; all <mChildList.size();all++ ){

            if(mChildList.get(all).getCategory_id() == mChildList.get(all).getParent_id() ){

                // all product :-

                allProductCategoryDs.setCategory_id(mChildList.get(all).getCategory_id());
                allProductCategoryDs.setIcon_image(mChildList.get(all).getIcon_image());
                allProductCategoryDs.setImage(mChildList.get(all).getImage());
                allProductCategoryDs.setOpened(mChildList.get(all).getOpened());
                allProductCategoryDs.setMarked(mChildList.get(all).getMarked());
                allProductCategoryDs.setMenu_icon(mChildList.get(all).getMenu_icon());
                allProductCategoryDs.setName(mChildList.get(all).getName());
                allProductCategoryDs.setParent_id(mChildList.get(all).getParent_id());
                allProductCategoryDs.setSort_order(mChildList.get(all).getSort_order());
                allProductCategoryDs.setTop(mChildList.get(all).getTop());


            }else {

                // other category :-

                CategoryDataSet categoryDs = new CategoryDataSet();
                categoryDs.setCategory_id(mChildList.get(all).getCategory_id());
                categoryDs.setIcon_image(mChildList.get(all).getIcon_image());
                categoryDs.setImage(mChildList.get(all).getImage());
                categoryDs.setOpened(mChildList.get(all).getOpened());
                categoryDs.setMarked(mChildList.get(all).getMarked());
                categoryDs.setMenu_icon(mChildList.get(all).getMenu_icon());
                categoryDs.setName(mChildList.get(all).getName());
                categoryDs.setParent_id(mChildList.get(all).getParent_id());
                categoryDs.setSort_order(mChildList.get(all).getSort_order());
                categoryDs.setTop(mChildList.get(all).getTop());
                mChildListFinal.add(categoryDs);

            }

        }

        //adding all product to list :-
        mChildListFinal.add(allProductCategoryDs);
        return mChildListFinal;
    }
}

