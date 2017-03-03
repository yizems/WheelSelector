package cn.yzl.wheelselector.singleselect;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.yzl.wheelselector.R;
import cn.yzl.wheelselector.wheel.adapters.AbstractWheelTextAdapter;


/**
 * Created by YZL on 2016/10/10.
 */

public class ItemAdapter extends AbstractWheelTextAdapter {

    protected Context context;
    protected String[] data;

    public ItemAdapter(Context context, String[] data) {
        // itemview 的id.以及显示的textview的id
        super(context, R.layout.item_pop_single_selector__wheelselector_yzl_cn, R.id.item_tv_single_selector, 0, 22, 16);
        this.context = context;
        this.data = data;
    }

    public ItemAdapter(Context context, String[] data, int itemLayout) {
        // itemview 的id.以及显示的textview的id
        super(context, itemLayout != 0 ? itemLayout : R.layout.item_pop_single_selector__wheelselector_yzl_cn, R.id.item_tv_single_selector, 0, 22, 16);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return data.length;
    }

    @Override
    public CharSequence getItemText(int index) {
        return data[index];
    }

}
