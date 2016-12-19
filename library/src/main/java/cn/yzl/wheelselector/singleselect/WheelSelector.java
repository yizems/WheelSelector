package cn.yzl.wheelselector.singleselect;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yzl.wheelselector.R;
import cn.yzl.wheelselector.wheel.views.OnWheelChangedListener;
import cn.yzl.wheelselector.wheel.views.OnWheelScrollListener;
import cn.yzl.wheelselector.wheel.views.WheelView;


/**
 * Created by 易点付 伊 on 2016/10/11.
 */
public class WheelSelector extends PopupWindow {
    private Window window;
    private int layout;

    //一些相关的配置

    private float textSize = 24;

    /**
     * 主数据,主要存储 省市区数据
     */
    private View view;

    private Context context;

    private WheelView wheelView;
    private ItemAdapter adapter;

    private String[] data;

    private ItemConfirmListener mConfirmListener;
    private String title;
    private TextView titleTV;
    private TextView cancelBut;
    private TextView confirmBut;
    private View titleV;

    /**
     * @param context
     * @param data    数据源
     */
    public WheelSelector(Context context, String[] data, String title, Window window) {
        super(context);
        this.context = context;
        this.data = data;
        this.title = title;
        this.layout = 0;
        this.window = window;
        initData();
        initView();
    }


    /**
     * @param context
     * @param data    数据源
     * @param layout  自定义视图
     */
    public WheelSelector(Context context, String[] data, String title, Window window, @LayoutRes int layout) {
        super(context);
        this.context = context;
        this.data = data;
        this.title = title;
        this.layout = 0;
        this.window = window;
        this.layout = layout;
        initData();
        initView();
    }

    /**
     * 初始化数据并且初始化 适配器
     */
    private void initData() {
        adapter = new ItemAdapter(context, data);
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(layout != 0 ? layout : R.layout.view_pop_single_seletor, null);

        //初始化 WheelView
        wheelView = (WheelView) view.findViewById(R.id.item_wheel);

        //设置确认按钮 和取消按钮的监听
        confirmBut = (TextView) view.findViewById(R.id.tv_confirm);
        cancelBut = (TextView) view.findViewById(R.id.tv_cancel);
        titleTV = (TextView) view.findViewById(R.id.tv_title);
        titleTV.setText(title);
        titleV = view.findViewById(R.id.title_v);
        //取消
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelSelector.this.dismiss();
            }
        });
        //确认 按钮
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    mConfirmListener.confirm(wheelView.getCurrentItem(), data[wheelView.getCurrentItem()]);
                    WheelSelector.this.dismiss();
                } else {
                    throw new NullPointerException("AddressConfirmListener is null");
                }
            }
        });

        wheelView.setViewAdapter(adapter);

        //数据变化
        wheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setTextviewSize(adapter.getItemText(wheel.getCurrentItem()).toString(), adapter);
            }
        });
        wheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                setTextviewSize(adapter.getItemText(wheel.getCurrentItem()).toString(), adapter);
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
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        setTextviewSize(adapter.getItemText(wheelView.getCurrentItem()).toString(), adapter);
    }


    /**
     * 设置 滚动中 字体的大小变化
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, ItemAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(textSize);
//                textvew.setTextColor(Color.parseColor("#3B3B3B"));
            } else {
                textvew.setTextSize(textSize - 6);
//                textvew.setTextColor(Color.parseColor("#BFBFBF"));
            }
        }
        wheelView.invalidate();
    }

    public void setConfirmListener(ItemConfirmListener confirmListener) {
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
    }

    /**
     * 获取标题栏
     *
     * @return
     */
    public TextView getTitleTV() {
        return titleTV;
    }

    /**
     * 获取取消按钮
     *
     * @return
     */
    public TextView getCancelBut() {
        return cancelBut;
    }

    /**
     * 获取确认按钮
     *
     * @return
     */
    public TextView getConfirmBut() {
        return confirmBut;
    }

    public View getTitleV() {
        return titleV;
    }
}
