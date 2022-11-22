package com.easymco.activity.category;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.adapter.Filter_Row_Adapter;
import com.easymco.adapter.Filter_Title_Adapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.FilterTitleSelection;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.FilterDataSet;
import com.easymco.models.WholeFilterDataSet;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;

public class Filter_Activity extends AppCompatActivity implements View.OnClickListener, API_Result, FilterTitleSelection {

    public static int mCategory_Id;
    ArrayList<WholeFilterDataSet> mList = new ArrayList<>();
    RecyclerView mFilterTitle, mFilterValue;
    Button mFilterButton, mFilterCancelButton;
    API_Result api_result;
    String title, image_url;
    CardView no_filter_data;
    FilterTitleSelection filterTitleSelection;
    ImageButton close_filter;
    ArrayList<Integer> selected_list = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_filter_new);

        api_result = this;
        filterTitleSelection = this;

        mFilterButton = (Button) findViewById(R.id.filter_recycler_view_btn);
        mFilterCancelButton = (Button) findViewById(R.id.filter_recycler_view_btn_cancel);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        no_filter_data = (CardView) findViewById(R.id.no_filter);
        close_filter = (ImageButton) findViewById(R.id.close_filter);

        mFilterTitle = (RecyclerView) findViewById(R.id.filter_recycler_view_title);
        mFilterTitle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mFilterValue = (RecyclerView) findViewById(R.id.filter_recycler_view_value);
        mFilterValue.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mFilterCancelButton.setOnClickListener(this);
        mFilterButton.setOnClickListener(this);
        close_filter.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE) != 0) {
                mCategory_Id = bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE) != null) {
                title = bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE) != null) {
                image_url = bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE);
            }
        }
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String url[] = {URL_Class.mURL + URL_Class.mURL_GetFilter_For_Category + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "Filter");
        } else {
            Intent intent = new Intent(Filter_Activity.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(
                    "ar".equals(AppLanguageSupport.getLanguage(Filter_Activity.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void setting(String data) {
        if (data != null) {
            mList = GetJSONData.getFilterData(data);
            if (mList != null) {
                if (mList.size() > 0) {
                    mFilterTitle.setAdapter(new Filter_Title_Adapter(getApplicationContext(), mList.get(0).mTitleList, filterTitleSelection));
                    mFilterValue.setAdapter(new Filter_Row_Adapter(getApplicationContext(), mList.get(0).mChildList.get(0), filterTitleSelection));
                    no_filter_data.setVisibility(View.GONE);
                } else {
                    mFilterTitle.setVisibility(View.GONE);
                    mFilterValue.setVisibility(View.GONE);
                    no_filter_data.setVisibility(View.VISIBLE);
                }
            } else {
                mFilterTitle.setVisibility(View.GONE);
                mFilterValue.setVisibility(View.GONE);
                no_filter_data.setVisibility(View.VISIBLE);
            }
        } else {
            mFilterTitle.setVisibility(View.GONE);
            mFilterValue.setVisibility(View.GONE);
            no_filter_data.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.filter_recycler_view_btn) {
            if (mList == null) {
                toast_message();
                onBackPressed();
                finish();
            } else {
                if (selected_list != null) {
                    if (selected_list.size() > 0) {
                        intentTransfer();
                    } else {
                        toast_message();
                        onBackPressed();
                        finish();
                    }
                } else {
                    toast_message();
                    onBackPressed();
                    finish();
                }
            }
        } else if (id == R.id.filter_recycler_view_btn_cancel) {
            reset();
        } else if (id == R.id.close_filter) {
            onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return JSON_Names.KEY_TRUE_BOOLEAN;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void intentTransfer() {
        Intent intent = new Intent(Filter_Activity.this, Category_Details.class);
        intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mCategory_Id);
        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.filter);
        intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
        intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
        if (selected_list != null) {
            if (selected_list.size() > 0) {
                String filter_data = "";
                for (int i = 0; i < selected_list.size(); i++) {
                    if (i == 0) {
                        filter_data = selected_list.get(i) + "";
                    } else {
                        filter_data = filter_data + "," + selected_list.get(i);
                    }
                }
                intent.putExtra("Filter", filter_data);
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("Filter")) {
            if (data != null) {
                setting(data[0]);
            }
        }
    }

    @Override
    public void title_selection(String title) {
        if (getPosition(title) != -1) {
            ArrayList<FilterDataSet> mReturnValue = mList.get(getPosition(title)).mChildList.get(getPosition(title));
            if (selected_list != null) {
                if (selected_list.size() > 0) {
                    for (int i = 0; i < mReturnValue.size(); i++) {
                        for (int j = 0; j < selected_list.size(); j++) {
                            if (Integer.valueOf(mReturnValue.get(i).getmFilterId()).equals(selected_list.get(j))) {
                                mReturnValue.get(i).setChecked(true);
                            }
                        }
                    }
                }
            }

            mFilterValue.setAdapter(new Filter_Row_Adapter(getApplicationContext(), mReturnValue, filterTitleSelection));
        } else {
            ArrayList<FilterDataSet> mReturnValue = mList.get(0).mChildList.get(0);
            if (selected_list != null) {
                if (selected_list.size() > 0) {
                    for (int i = 0; i < mReturnValue.size(); i++) {
                        for (int j = 0; j < selected_list.size(); j++) {
                            if (Integer.valueOf(mReturnValue.get(i).getmFilterId()).equals(selected_list.get(j))) {
                                mReturnValue.get(i).setChecked(true);
                            }
                        }
                    }
                }
            }

            mFilterValue.setAdapter(new Filter_Row_Adapter(getApplicationContext(), mList.get(0).mChildList.get(0), filterTitleSelection));
        }
    }

    @Override
    public void selected_list(int number) {
        if (selected_list.size() > 0) {
            if (!isExist(number)) {
                selected_list.add(number);
            }
        } else {
            selected_list.add(number);
        }
    }

    private boolean isExist(int number) {
        boolean value = false;
        for (int i = 0; i < selected_list.size(); i++) {
            if (selected_list.get(i).equals(number)) {
                value = true;
            }
        }
        return value;
    }

    @Override
    public void remove_from_list(int number) {
        if (selected_list != null) {
            if (selected_list.size() > 0) {
                for (int i = 0; i < selected_list.size(); i++) {
                    if (number == selected_list.get(i)) {
                        selected_list.remove(i);
                    }
                }
            }
        }
    }

    private int getPosition(String title) {
        for (int i = 0; i < mList.get(0).mTitleList.size(); i++) {
            if (title.equals(mList.get(0).mTitleList.get(i).getmName())) {
                return i;
            }
        }
        return -1;
    }

    private void reset() {
        if (mList != null) {
            if (mList.size() > 0) {
                resetFullList();
                mFilterTitle.setAdapter(new Filter_Title_Adapter(getApplicationContext(), resetList(mList.get(0).mTitleList), filterTitleSelection));
                mFilterValue.setAdapter(new Filter_Row_Adapter(getApplicationContext(), resetList(mList.get(0).mChildList.get(0)), filterTitleSelection));
            } else {
                toast_message();
            }
        } else {
            toast_message();
        }
    }

    private ArrayList<FilterDataSet> resetList(ArrayList<FilterDataSet> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.get(i).setSelect(true);
                list.get(i).setChecked(false);
            } else {
                list.get(i).setSelect(false);
                list.get(i).setChecked(false);
            }
        }

        return list;
    }

    private void resetFullList() {
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mList.get(0).mChildList.size(); j++) {
                for (int k = 0; k < mList.get(i).mChildList.get(j).size(); k++) {
                    mList.get(i).mChildList.get(j).get(k).setSelect(false);
                    mList.get(i).mChildList.get(j).get(k).setChecked(false);
                }
            }
        }

        selected_list.clear();
    }

    private void toast_message() {
        Methods.toast(getResources().getString(R.string.empty_filter));
    }
}
