package cn.yzl.wheelselector.mulpicker;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import cn.yzl.wheelselector.R;
import cn.yzl.wheelselector.wheel.adapters.AbstractWheelTextAdapter;


/**
 * Created by YZL on 2016/10/10.
 */

public class CityAdapter extends AbstractWheelTextAdapter {

    protected final String key;
    protected Context context;
    protected List<Map<String, Object>> data;

    public CityAdapter(Context context, List<Map<String, Object>> data, String key) {
        // itemview 的id.以及显示的textview的id
        super(context, R.layout.item_mul_selector_wheelselector_yzl_cn, R.id.item_tv_mul_selector, 0, 22, 16);
        this.context = context;
        this.data = data;
        this.key = key;
    }

    public CityAdapter(Context context, List<Map<String, Object>> data, String key, int itemLayout) {
        // itemview 的id.以及显示的textview的id
        super(context, itemLayout != 0 ? itemLayout : R.layout.item_mul_selector_wheelselector_yzl_cn, R.id.item_tv_mul_selector, 0, 22, 16);
        this.context = context;
        this.data = data;
        this.key = key;
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return data.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index == 0) {
            return data.get(index).get("name").toString();
        } else {
            return data.get(index).get(key).toString();
        }
    }

}
