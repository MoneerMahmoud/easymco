package com.easymco.models;

public class FilterDataSet {
    private String mName;
    private String mFilterId;
    private boolean select,checked;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmFilterId() {
        return mFilterId;
    }

    public void setmFilterId(String mFilterId) {
        this.mFilterId = mFilterId;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
