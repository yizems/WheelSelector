package cn.yzl.wheelselector.singleselect;


/**
 * Created by 易点付 伊 on 2016/10/11.
 */
public interface ItemConfirmListener {
    /**
     * 确认按钮 回调
     *
     * @param position index
     * @param text     选择的项
     */
    void confirm(int position, String text);
}
