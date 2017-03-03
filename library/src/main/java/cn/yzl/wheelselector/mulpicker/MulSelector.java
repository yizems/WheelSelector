package cn.yzl.wheelselector.mulpicker;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
public class MulSelector extends PopupWindow {
    private int itemLayout;
    protected Window window;
    protected int layout;

    //一些相关的配置

    protected float textSize = 24;

    /**
     * 主数据,主要存储 省市区数据
     */
    protected List<Map<String, Object>> mData;
    protected View view;

    protected Context context;

    protected WheelView proviceWV;
    protected WheelView cityWV;
    protected WheelView districtWV;
    protected CityAdapter proviceAdapter;
    protected CityAdapter cityAdapter;
    protected CityAdapter disAdapter;

    protected Map<String, Object> selProvice;
    protected Map<String, Object> selCity;
    protected Map<String, Object> selDis;

    protected List<Map<String, Object>> disData;
    protected List<Map<String, Object>> cityData;
    protected List<Map<String, Object>> proviceData;

    protected Map<String, Object> noSelData;

    protected AddressConfirmListener mConfirmListener;

    /**
     * 是否显示区,默认显示
     */
    protected boolean showDis;

    //省市区 在map中对应的id的名字的key

    /**
     * 是否 自定义了各个省市区的 key
     */
    protected boolean isCustomKey;

    protected String pIdKey;
    protected String pNameKey;
    protected String cIdKey;
    protected String cNameKey;
    protected String dIdKey;
    protected String dNameKey;

    protected String cityKey;

    protected String disKey;
    protected TextView confirmBut;
    protected TextView cancelBut;
    protected TextView title_tv;
    protected View titleV;

    /**
     * @param context
     * @param data    数据源
     * @param showDis 是否显示区
     */
    public MulSelector(Context context,
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
    public MulSelector(Context context,
                       List<Map<String, Object>> data,
                       Window window,
                       boolean showDis, @LayoutRes int layout, @LayoutRes int itemLayout) {
        super(context);
        this.context = context;
        this.mData = data;
        this.showDis = showDis;
        this.window = window;
        this.layout = layout;
        this.itemLayout = itemLayout;
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
    public MulSelector(Context context, List<Map<String, Object>> data,
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
    public MulSelector(Context context, List<Map<String, Object>> data,
                       String pIdKey, String pNameKey, String cIdKey,
                       String cNameKey, String dIdKey,
                       String dNameKey, String cityKey,
                       String disKey, boolean showDis,
                       Window window,
                       @LayoutRes int layout,
                       @LayoutRes int itemLayout) {
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
        this.itemLayout = itemLayout;
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
    protected void initData() {
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
            proviceAdapter = new CityAdapter(context, proviceData, pNameKey, itemLayout);
        } else {
            proviceAdapter = new CityAdapter(context, proviceData, "province_name", itemLayout);
        }

        cityData = new ArrayList<>();
        cityData.add(noSelData);

        if (isCustomKey) {
            cityAdapter = new CityAdapter(context, cityData, cNameKey, itemLayout);
        } else {
            cityAdapter = new CityAdapter(context, cityData, "city_name", itemLayout);
        }

        if (showDis) {
            disData = new ArrayList<>();
            disData.add(noSelData);

            if (isCustomKey) {
                disAdapter = new CityAdapter(context, disData, dNameKey, itemLayout);
            } else {
                disAdapter = new CityAdapter(context, disData, "district_name", itemLayout);
            }
        }
    }

    protected void initView() {
        view = LayoutInflater.from(context).inflate(layout != 0 ? layout : R.layout.view_pop_mul_seletor_wheelselector_yzl_cn, null);

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
                MulSelector.this.dismiss();
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
                    MulSelector.this.dismiss();
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

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlph(1);
            }
        });

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
    protected void updateCityData() {
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
            if (temp != null) {
                for (int i = 0; i < temp.size(); i++) {
                    cityData.add(temp.get(i));
                }
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
    protected void updateDisData() {
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
            if (temp != null) {
                for (int i = 0; i < temp.size(); i++) {
                    disData.add(temp.get(i));
                }
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

    public void show(int gravity) {
        super.showAtLocation(window.getDecorView().getRootView(), gravity, 0, 0);
        setWindowAlph(0.7f);
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

    protected void setWindowAlph(@FloatRange(from = 0, to = 1) float alphaNumb) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alphaNumb;
        window.setAttributes(params);
    }
}
