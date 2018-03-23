package com.ty.ty.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ty.ty.R;
import com.ty.ty.bean.HomeBean;
import com.ty.ty.utils.ImageLoader;

import java.util.List;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc
 */

public class HomePageAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder>{

    public HomePageAdapter(int layoutResId, @Nullable List<HomeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean item) {
        ImageLoader.getIns(mContext).load(item.getPicurl(), (ImageView) helper.getView(R.id.img_cover));
    }
}
