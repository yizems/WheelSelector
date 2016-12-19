package cn.yzl.wheelselector.addresspicker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yzl.wheelselector.R;
import cn.yzl.wheelselector.wheel.views.OnWheelChangedListener;
import cn.yzl.wheelselector.wheel.views.OnWheelScrollListener;
import cn.yzl.wheelselector.wheel.views.WheelView;


/**
 * Created by 易点付 伊 on 2016/10/11.
 */
public class AddressPickerView extends PopupWindow {
    private Window window;
    private int layout;

    //一些相关的配置

    private float textSize = 24;

    /**
     * 主数据,主要存储 省市区数据
     */
    private List<Map<String, Object>> mData;
    private View view;

    private Context context;

    private WheelView proviceWV;
    private WheelView cityWV;
    private WheelView districtWV;
    private CityAdapter proviceAdapter;
    private CityAdapter cityAdapter;
    private CityAdapter disAdapter;

    private Map<String, Object> selProvice;
    private Map<String, Object> selCity;
    private Map<String, Object> selDis;

    private List<Map<String, Object>> disData;
    private List<Map<String, Object>> cityData;
    private List<Map<String, Object>> proviceData;

    private Map<String, Object> noSelData;

    private AddressConfirmListener mConfirmListener;

    /**
     * 是否显示区,默认显示
     */
    private boolean showDis;

    //省市区 在map中对应的id的名字的key

    /**
     * 是否 自定义了各个省市区的 key
     */
    private boolean isCustomKey;

    private String pIdKey;
    private String pNameKey;
    private String cIdKey;
    private String cNameKey;
    private String dIdKey;
    private String dNameKey;

    private String cityKey;

    private String disKey;
    private TextView confirmBut;
    private TextView cancelBut;
    private TextView title_tv;
    private View titleV;

    /**
     * @param context
     * @param data    数据源
     * @param showDis 是否显示区
     */
    public AddressPickerView(Context context,
                             List<Map<String, Object>> data, Window window,
                             boolean showDis) {
        super(context);
        this.context = context;
        this.mData = data;
        this.showDis = showDis;
        this.window = window;
        layout = 0;
        initData();
        initView();
    }


    /**
     * @param context
     * @param data    数据源
     * @param showDis 是否显示区
     * @param layout  自定义视图
     */
    public AddressPickerView(Context context,
                             List<Map<String, Object>> data,
                             Window window,
                             boolean showDis, @LayoutRes int layout) {
        super(context);
        this.context = context;
        this.mData = data;
        this.showDis = showDis;
        this.window = window;
        this.layout = layout;
        initData();
        initView();
    }

    /**
     * 自定义各个字段 的key值
     *
     * @param context
     * @param data     数据源
     * @param pIdKey   省份id key
     * @param pNameKey 省份 name key
     * @param cIdKey
     * @param cNameKey
     * @param dIdKey
     * @param dNameKey
     * @param cityKey  包含 的城市所在字段
     * @param disKey   包含的 区县 所在字段
     * @param showDis  是否显示区
     */
    public AddressPickerView(Context context, List<Map<String, Object>> data,
                             String pIdKey, String pNameKey, String cIdKey,
                             String cNameKey, String dIdKey,
                             String dNameKey, String cityKey,
                             String disKey, boolean showDis, Window window) {
        super(context);
        this.isCustomKey = true;
        this.context = context;
        this.mData = data;
        this.cityKey = cityKey;
        this.disKey = disKey;
        this.pIdKey = pIdKey;
        this.pNameKey = pNameKey;
        this.cIdKey = cIdKey;
        this.cNameKey = cNameKey;
        this.showDis = showDis;
        this.window = window;
        if (showDis) {
            this.dIdKey = dIdKey;
            this.dNameKey = dNameKey;
        }
        layout = 0;
        initData();
        initView();
    }

    /**
     * 自定义各个字段 的key值
     *
     * @param context
     * @param data     数据源
     * @param pIdKey   省份id key
     * @param pNameKey 省份 name key
     * @param cIdKey
     * @param cNameKey
     * @param dIdKey
     * @param dNameKey
     * @param cityKey  包含 的城市所在字段
     * @param disKey   包含的 区县 所在字段
     * @param showDis  是否显示区
     */
    public AddressPickerView(Context context, List<Map<String, Object>> data,
                             String pIdKey, String pNameKey, String cIdKey,
                             String cNameKey, String dIdKey,
                             String dNameKey, String cityKey,
                             String disKey, boolean showDis, Window window, @LayoutRes int layout) {
        super(context);
        this.isCustomKey = true;
        this.context = context;
        this.mData = data;
        this.cityKey = cityKey;
        this.disKey = disKey;
        this.pIdKey = pIdKey;
        this.pNameKey = pNameKey;
        this.cIdKey = cIdKey;
        this.cNameKey = cNameKey;
        this.showDis = showDis;
        this.window = window;
        if (showDis) {
            this.dIdKey = dIdKey;
            this.dNameKey = dNameKey;
        }
        this.layout = layout;
        initData();
        initView();
    }

    /**
     * 初始化数据并且初始化 适配器
     */
    private void initData() {
        noSelData = new HashMap<>();
        if (isCustomKey) {
            noSelData.put("id", "0");
            noSelData.put("name", "不限");
            noSelData.put(pIdKey, "0");
            noSelData.put(pNameKey, "不限");
            noSelData.put(cIdKey, "0");
            noSelData.put(cNameKey, "不限");
            if (showDis) {
                noSelData.put(dIdKey, "0");
                noSelData.put(dNameKey, "不限");
            }
        } else {
            noSelData.put("id", "0");
            noSelData.put("name", "不限");
            noSelData.put("province_id", "0");
            noSelData.put("province_name", "不限");
            noSelData.put("city_id", "0");
            noSelData.put("city_name", "不限");
            if (showDis) {
                noSelData.put("district_id", "0");
                noSelData.put("district_name", "不限");
            }
        }

        proviceData = new ArrayList<>();
        proviceData.add(noSelData);

        for (int i = 0; i < mData.size(); i++) {
            proviceData.add(mData.get(i));
        }

        if (isCustomKey) {
            proviceAdapter = new CityAdapter(context, proviceData, pNameKey);
        } else {
            proviceAdapter = new CityAdapter(context, proviceData, "province_name");
        }

        cityData = new ArrayList<>();
        cityData.add(noSelData);

        if (isCustomKey) {
            cityAdapter = new CityAdapter(context, cityData, cNameKey);
        } else {
            cityAdapter = new CityAdapter(context, cityData, "city_name");
        }

        if (showDis) {
            disData = new ArrayList<>();
            disData.add(noSelData);

            if (isCustomKey) {
                disAdapter = new CityAdapter(context, disData, dNameKey);
            } else {
                disAdapter = new CityAdapter(context, disData, "district_name");
            }
        }
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(layout != 0 ? layout : R.layout.view_pop_city_seletor, null);

        //初始化 WheelView
        proviceWV = (WheelView) view.findViewById(R.id.id_province);
        cityWV = (WheelView) view.findViewById(R.id.id_city);
        districtWV = (WheelView) view.findViewById(R.id.id_district);
        if (showDis) {
            districtWV.setVisibility(View.VISIBLE);
        } else {
            districtWV.setVisibility(View.GONE);
        }

        //设置确认按钮 和取消按钮的监听
        confirmBut = (TextView) view.findViewById(R.id.tv_confirm);
        cancelBut = (TextView) view.findViewById(R.id.tv_cancel);
        title_tv = (TextView) view.findViewById(R.id.tv_title);
        titleV = view.findViewById(R.id.title_v);

        //取消
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressPickerView.this.dismiss();
            }
        });
        //确认 按钮
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    if (showDis) {
                        mConfirmListener.confirm(selProvice, selCity, selDis);
                    } else {
                        mConfirmListener.confirm(selProvice, selCity, null);
                    }
                    AddressPickerView.this.dismiss();
                } else {
                    throw new NullPointerException("AddressConfirmListener is null");
                }
            }
        });

        proviceWV.setViewAdapter(proviceAdapter);
        cityWV.setViewAdapter(cityAdapter);
        if (showDis) {
            districtWV.setViewAdapter(disAdapter);
        }

        //省份 数据变化
        proviceWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setTextviewSize(proviceAdapter.getItemText(wheel.getCurrentItem()).toString(), proviceAdapter);
                if (wheel.getCurrentItem() == 0) {
                    selProvice = null;
                    selCity = null;
                    selDis = null;
                } else {
                    selProvice = proviceData.get(wheel.getCurrentItem());
                    selCity = null;
                    selDis = null;
                }
                updateCityData();
            }
        });
        proviceWV.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                setTextviewSize(proviceAdapter.getItemText(wheel.getCurrentItem()).toString(), proviceAdapter);
            }
        });

        //城市 数据变化
        cityWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setTextviewSize(cityAdapter.getItemText(wheel.getCurrentItem()).toString(), cityAdapter);
                if (wheel.getCurrentItem() == 0) {
                    selCity = null;
                    selDis = null;
                } else {
                    selCity = cityData.get(wheel.getCurrentItem());
                    selDis = null;
                }
                if (showDis) {
                    updateDisData();
                }
            }
        });

        cityWV.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                setTextviewSize(cityAdapter.getItemText(wheel.getCurrentItem()).toString(), cityAdapter);
            }
        });
        if (showDis) {
            //区 数据变化
            districtWV.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    setTextviewSize(disAdapter.getItemText(wheel.getCurrentItem()).toString(), disAdapter);
                    if (wheel.getCurrentItem() == 0) {
                        selDis = null;
                    } else {
                        selDis = disData.get(wheel.getCurrentItem());
                    }
                }
            });

            districtWV.addScrollingListener(new OnWheelScrollListener() {
                @Override
                public void onScrollingStarted(WheelView wheel) {

                }

                @Override
                public void onScrollingFinished(WheelView wheel) {
                    setTextviewSize(disAdapter.getItemText(wheel.getCurrentItem()).toString(), disAdapter);
                }
            });
        }
        setContentView(view);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.no_bg));
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        setTextviewSize(proviceAdapter.getItemText(proviceWV.getCurrentItem()).toString(), proviceAdapter);
        setTextviewSize(cityAdapter.getItemText(cityWV.getCurrentItem()).toString(), cityAdapter);
        setTextviewSize(disAdapter.getItemText(districtWV.getCurrentItem()).toString(), disAdapter);
    }


    /**
     * 设置 滚动中 字体的大小变化
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CityAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(textSize);
            } else {
                textvew.setTextSize(textSize - 6);
            }
        }
    }


    //选择省份后 更新 城市数据
    private void updateCityData() {
        if (selProvice == null) {
            cityData.clear();
            cityData.add(noSelData);
        } else {
            cityData.clear();
            cityData.add(noSelData);
            List<Map<String, Object>> temp;
            if (isCustomKey) {
                temp = (List<Map<String, Object>>) selProvice.get(cityKey);
            } else {
                temp = (List<Map<String, Object>>) selProvice.get("city");
            }

            for (int i = 0; i < temp.size(); i++) {
                cityData.add(temp.get(i));
            }
        }
        if (isCustomKey) {
            cityAdapter = new CityAdapter(context, cityData, cNameKey);
        } else {
            cityAdapter = new CityAdapter(context, cityData, "city_name");
        }
        cityWV.setViewAdapter(cityAdapter);
        cityWV.setCurrentItem(0);
        if (showDis) {
            updateDisData();
        }
    }

    //选择城市后 更新 区数据
    private void updateDisData() {
        if (selCity == null) {
            disData.clear();
            disData.add(noSelData);
        } else {
            disData.clear();
            disData.add(noSelData);
            List<Map<String, Object>> temp;
            if (isCustomKey) {
                temp = (List<Map<String, Object>>) selCity.get(disKey);
            } else {
                temp = (List<Map<String, Object>>) selCity.get("district");
            }
            for (int i = 0; i < temp.size(); i++) {
                disData.add(temp.get(i));
            }
        }
        if (isCustomKey) {
            disAdapter = new CityAdapter(context, disData, dNameKey);
        } else {
            disAdapter = new CityAdapter(context, disData, "district_name");
        }
        districtWV.setViewAdapter(disAdapter);
        districtWV.setCurrentItem(0);
    }

    public void setConfirmListener(AddressConfirmListener confirmListener) {
        mConfirmListener = confirmListener;
    }

    /**
     * 设置 字体大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void show(View parent, int gravity) {
        super.showAtLocation(parent, gravity, 0, 0);
    }

    public TextView getConfirmBut() {
        return confirmBut;
    }

    public TextView getCancelBut() {
        return cancelBut;
    }

    public TextView getTitle_tv() {
        return title_tv;
    }

    public View getTitleV() {
        return titleV;
    }
}
