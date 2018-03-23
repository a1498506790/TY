package com.ty.ty.ui.activity.my;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.ty.ty.R;
import com.ty.ty.adapter.ContactsAdapter;
import com.ty.ty.base.BaseActivity;
import com.ty.ty.bean.ContactComparator;
import com.ty.ty.bean.TelContactBean;
import com.ty.ty.utils.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc
 */

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.side_bar)
    WaveSideBar mSideBar;
    private ContactsAdapter mAdapter;
    private List<TelContactBean> mData = new ArrayList<>();

    private List<String> mCharacterList;
    private List<String> mSortList;
    private Map<String, String> mMapName;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            showContent();
            mAdapter.setNewData(mData);
            return true;
        }
    });


    @Override
    public int getLayoutRes() {
        return R.layout.activity_contacts;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar(UiUtils.getString(R.string.title_contacts));
        mSideBar.setIndexItems("A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#");
        showLoading();
        initAdapter();
        readContacts();
    }

    private void initAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ContactsAdapter(mData);
        mAdapter.setEmptyView(UiUtils.getEmptyView(mContext, mRecyclerView, "暂无联系人"));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                int position = getScrollPosition(index);
                layoutManager.scrollToPositionWithOffset(position, 0);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TelContactBean bean = mAdapter.getData().get(position);
                sendSms(bean.getTelNumber());
            }
        });
    }

    /**
     * 发送短信
     * @param telNumber
     */
    private void sendSms(String telNumber){
        try{
            Uri uri = Uri.parse("smsto:" + telNumber);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取手机通讯录联系人
     */
    private void readContacts(){
        new Thread(){
            @Override
            public void run() {
                Cursor cursor = null;
                try{
                    cursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,null,null,null);
                    mData.clear();
                    while(cursor.moveToNext()){
                        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        TelContactBean bean = new TelContactBean(displayName);
                        mData.add(bean);
                    }
                    sortList();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(cursor!=null){
                        cursor.close();
                    }
                }
            }
        }.start();
    }

    /**
     * 重新排序
     */
    private void sortList() {
        //存放排序好的字母全拼
        mSortList = new ArrayList<>();
        //存放首字母
        mCharacterList = new ArrayList<>();
        //存放名字
        mMapName = new HashMap<>();
        for (int i = 0; i < mData.size(); i++) {
            String name = mData.get(i).getName();
            String pingYin = UiUtils.getPingYin(name);
            mMapName.put(pingYin, name);
            mSortList.add(pingYin);
        }
        //重新排序 A - #
        Collections.sort(mSortList, new ContactComparator());
        mData.clear();
        //重新赋值
        for (int i = 0; i < mSortList.size(); i++) {
            String name = mSortList.get(i);
            //获得一个字母
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            //如果没有包含首字母
            if (!mCharacterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    mCharacterList.add(character);
                    mData.add(new TelContactBean(character, ContactsAdapter.ITEM_TYPE_CHARACTER));
                } else {
                    if (!mCharacterList.contains("#")) {
                        mCharacterList.add("#");
                        mData.add(new TelContactBean("#", ContactsAdapter.ITEM_TYPE_CHARACTER));
                    }
                }
            }
            String displayName  = mMapName.get(name);
            mData.add(new TelContactBean(displayName,
                    UiUtils.nameFindCall(mContext, displayName), ContactsAdapter.ITEM_TYPE_CONTACT));
        }
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 获得对应的位置
     * @param character
     * @return
     */
    public int getScrollPosition(String character) {
        if (mCharacterList.contains(character)) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getName().equals(character)) {
                    return i;
                }
            }
        }
        return -1; // -1不会滑动
    }
}
