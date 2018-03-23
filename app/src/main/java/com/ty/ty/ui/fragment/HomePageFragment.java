package com.ty.ty.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ty.ty.R;
import com.ty.ty.adapter.HomePageAdapter;
import com.ty.ty.base.BaseBean;
import com.ty.ty.base.BaseFragment;
import com.ty.ty.bean.HomeBean;
import com.ty.ty.bean.ListBean;
import com.ty.ty.constants.AppConstants;
import com.ty.ty.http.HttpClient;
import com.ty.ty.http.HttpParams;
import com.ty.ty.http.MyCallback;
import com.ty.ty.http.api.UserService;
import com.ty.ty.ui.activity.BrowserActivity;
import com.ty.ty.utils.UiUtils;
import com.ty.ty.utils.UserUtils;
import com.ty.ty.widget.recycler.OnSimpleClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc 首页
 */

public class HomePageFragment extends BaseFragment implements OnLoadmoreListener, OnRefreshListener{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private HomePageAdapter mAdapter;
    private List<HomeBean> mData = new ArrayList<>();
    private int mPage = 1;

    @Override
    public View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, null);
    }

    @Override
    public void onCreateFragment(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = initToolbar(UiUtils.getString(R.string.title_maintain_consult));
        toolbar.setNavigationIcon(null);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        initAdapter();
        requestData();
        showLoading();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomePageAdapter(R.layout.item_rlv_home_page, mData);
        mAdapter.setEmptyView(UiUtils.getEmptyView(mContext, mRecyclerView));
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnSimpleClickListener(){
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                HomeBean bean = mAdapter.getData().get(i);
                Intent intent = new Intent(mContext, BrowserActivity.class);
                intent.putExtra(AppConstants.EXTRA_URL, bean.getA_from());
                intent.putExtra(AppConstants.EXTRA_TITLE, bean.getA_title());
                startActivity(intent);
            }
        });
    }

    private void requestData() {
        HttpClient.getIns().createService(UserService.class)
                .home(HttpParams.getIns().putPage(UserUtils.getUid(), mPage))
                .equals(new MyCallback<BaseBean<ListBean<HomeBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<ListBean<HomeBean>>> response) {
                        showContent();
                        ListBean<HomeBean> data = response.body().getData();
                        if(mPage <= 1){
                            mAdapter.setNewData(data.lists);
                            mRefreshLayout.finishRefresh();
                        }else{
                            mAdapter.addData(data.lists);
                            mRefreshLayout.finishLoadmore();
                        }
                        mRefreshLayout.setEnableLoadmore(mPage < data.pageAll);
                    }

                    @Override
                    public void onFail(String message) {
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                        showFail(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showLoading();
                                requestData();
                            }
                        });
                    }
                });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage ++;
        requestData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        requestData();
    }
}
