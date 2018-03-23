package com.ty.ty.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ty.ty.R;
import com.ty.ty.bean.TelContactBean;

import java.util.List;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc
 */

public class ContactsAdapter extends BaseMultiItemQuickAdapter<TelContactBean, BaseViewHolder> {

    public static final int ITEM_TYPE_CHARACTER = 1;
    public static final int ITEM_TYPE_CONTACT = 2;

    public ContactsAdapter(List<TelContactBean> data) {
        super(data);
        addItemType(ITEM_TYPE_CHARACTER, R.layout.item_character);
        addItemType(ITEM_TYPE_CONTACT, R.layout.item_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, TelContactBean item) {
        switch (item.getItemType()){
            case ITEM_TYPE_CHARACTER:
                helper.setText(R.id.character, TextUtils.isEmpty(item.getName()) ? "" : item.getName());
                break;
            case ITEM_TYPE_CONTACT:
                helper.setText(R.id.contact_name, TextUtils.isEmpty(item.getName()) ? "" : item.getName());
                break;
        }
    }
}
