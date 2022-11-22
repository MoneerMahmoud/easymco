package com.easymco.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Search;
import com.easymco.activity.category.Category_Details;
import com.easymco.constant_class.JSON_Names;
import com.easymco.interfaces.BannerHandler;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.CategoryDataSet;
import com.easymco.models.ProductDataSet;

import java.util.ArrayList;
import java.util.Calendar;


public class Home_Adapter extends RecyclerView.Adapter<ViewHolder> implements View.OnTouchListener {
    private final int KEY_SLIDE = 2, KEY_BANNER = 4, KEY_CATEGORY = 1, KEY_STATIC = 5;
    Context mContext;
    private ArrayList<Object> mWholeList = new ArrayList<>();
    private String banner_url[], mCategoryData;
    private int slide_btn_fix_size = 0;
    private WishListAPIRequest wishListAPIRequest;
    private BannerHandler bannerHandler;

    public Home_Adapter(Context context, ArrayList<Object> mHomeViewOrder,
                        String CategoryData, WishListAPIRequest wishListAPIRequest, BannerHandler bannerHandler) {
        this.mContext = context;
        this.mWholeList = mHomeViewOrder;
        this.mCategoryData = CategoryData;
        this.wishListAPIRequest = wishListAPIRequest;
        this.bannerHandler = bannerHandler;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.test_search_view:
                Intent intent = new Intent(v.getContext(), Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWholeList.get(position) instanceof String[]) {
            return KEY_SLIDE;
        } else if (mWholeList.get(position) instanceof String) {
            return KEY_BANNER;
        } else if (mWholeList.get(position) instanceof Boolean) {
            return KEY_CATEGORY;
        } else if (mWholeList.get(position) instanceof Integer) {
            return KEY_STATIC;
        } else {
            return 3;
        }

    }

    @Override
    public int getItemCount() {
        return mWholeList.size();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case KEY_SLIDE:
                final ViewHolder_SlideView holderSlide = (ViewHolder_SlideView) holder;
                banner_url = (String[]) mWholeList.get(position);
                if (banner_url != null) {
                    if (banner_url.length > 0) {
                        if (slide_btn_fix_size == 0) {
                            bannerHandler.bannerTransfer(holderSlide.slide_view, holderSlide.button_holder);
                            slide_btn_fix_size++;
                        }
                    }
                }
                break;
            case KEY_BANNER:
                ViewHolder_Image_view holderImage = (ViewHolder_Image_view) holder;
                String url = (String) mWholeList.get(position);
                Methods.glide_image_loader(url, holderImage.testImage);
                break;
            case KEY_CATEGORY:
                ArrayList<CategoryDataSet> mHomeCategoryList;
                mHomeCategoryList = GetJSONData.getHomeCategoryData(mCategoryData);
                ViewHolder_Category_Recycler_view viewHolder_category_recycler_view = (ViewHolder_Category_Recycler_view) holder;
                viewHolder_category_recycler_view.child_Category_Recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
                if (mHomeCategoryList != null) {
                    if (mHomeCategoryList.size() != 0) {
                        viewHolder_category_recycler_view.child_Category_Recycler_view.setAdapter(
                                new HomeCategoryAdapter(mContext, mHomeCategoryList));
                    }
                }
                break;
            case KEY_STATIC:
                ViewHolderStaticContent viewHolderStaticContent = (ViewHolderStaticContent) holder;
                String copy_right = mContext.getResources().getString(R.string.copy_rights) + " " + Calendar.getInstance().get(Calendar.YEAR);
                viewHolderStaticContent.copy_right_custom_text_view.setText(copy_right);
                break;
            default:
                ViewHolder_Recycler_view holderRecycler_view = (ViewHolder_Recycler_view) holder;
                final ArrayList<ProductDataSet> list = (ArrayList<ProductDataSet>) mWholeList.get(position);
                if (list != null) {
                    holderRecycler_view.title.setActivated(true);
                    holderRecycler_view.title.setText(list.get(0).getHeading());
                    holderRecycler_view.view_all.getParent().requestDisallowInterceptTouchEvent(true);
                    holderRecycler_view.childRecycler_view.setNestedScrollingEnabled(false);
                    holderRecycler_view.view_all.setPadding(5, 5, 5, 5);
                    if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.special))) {
                        holderRecycler_view.view_all.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_with_corner_style));
                        holderRecycler_view.childRecycler_view.setLayoutManager(new LinearLayoutManager(mContext,
                                LinearLayoutManager.VERTICAL, false));
                        holderRecycler_view.childRecycler_view.setAdapter(new HomeListImageDifferentAdapter(mContext, list));
                    } else {
                        holderRecycler_view.view_all.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_with_corner_style_new));
                        holderRecycler_view.title.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                        holderRecycler_view.home_product_list_back_ground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                        holderRecycler_view.childRecycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
                        holderRecycler_view.childRecycler_view.setAdapter(new Adapter_Row(list, mContext, wishListAPIRequest));
                    }
                    holderRecycler_view.view_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, Category_Details.class);
                            intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, list.get(0).getHeading());
                            if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.featured))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 11);
                            } else if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.latest))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 22);
                            } else if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.special))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 33);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                }
                break;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case KEY_SLIDE:
                View v2 = inflater.inflate(R.layout.home_sliderview, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_SlideView(v2);
                break;
            case KEY_BANNER:
                View v3 = inflater.inflate(R.layout.home_image_view, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Image_view(v3);
                break;
            case KEY_CATEGORY:
                View v1 = inflater.inflate(R.layout.home_category_holder, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Category_Recycler_view(v1);
                break;
            case KEY_STATIC:
                View v5 = inflater.inflate(R.layout.home_static_content, viewGroup, false);
                viewHolder = new ViewHolderStaticContent(v5);
                break;
            default:
                View v4 = inflater.inflate(R.layout.home_inner_recycler_view, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Recycler_view(v4);
                break;
        }
        return viewHolder;
    }

    private class ViewHolder_SlideView extends ViewHolder {
        ViewPager slide_view;
        LinearLayout button_holder;

        ViewHolder_SlideView(View view) {
            super(view);
            slide_view = view.findViewById(R.id.home_slider_view_view_pager);
            button_holder = view.findViewById(R.id.home_slider_view_view_pager_button_holder);
        }
    }

    private class ViewHolder_Recycler_view extends ViewHolder {
        RecyclerView childRecycler_view;
        TextView title, view_all;
        LinearLayout home_product_list_back_ground;

        ViewHolder_Recycler_view(View itemView) {
            super(itemView);
            childRecycler_view = itemView.findViewById(R.id.test_home_recycler_view);
            title = itemView.findViewById(R.id.test_cat_title);
            view_all = itemView.findViewById(R.id.home_view_all);
            home_product_list_back_ground = itemView.findViewById(R.id.home_product_list_back_ground);
        }
    }

    private class ViewHolder_Image_view extends ViewHolder {

        ImageView testImage;

        ViewHolder_Image_view(View itemView) {
            super(itemView);
            testImage = itemView.findViewById(R.id.test_imageView_recycler_view);

        }
    }

    private class ViewHolder_Category_Recycler_view extends ViewHolder {
        RecyclerView child_Category_Recycler_view;

        ViewHolder_Category_Recycler_view(View itemView) {
            super(itemView);
            child_Category_Recycler_view = itemView.findViewById(R.id.home_category_recycler_view);
        }
    }

    private class ViewHolderStaticContent extends ViewHolder {
        TextView copy_right_custom_text_view;

        ViewHolderStaticContent(View itemView) {
            super(itemView);
            copy_right_custom_text_view = itemView.findViewById(R.id.copy_right_custom_text_view);
        }
    }

}
