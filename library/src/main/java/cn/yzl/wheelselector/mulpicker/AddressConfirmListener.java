package cn.yzl.wheelselector.mulpicker;

import java.util.Map;

/**
 * Created by 易点付 伊 on 2016/10/11.
 */
public interface AddressConfirmListener {
    /**
     * 确认按钮 回调
     * @param proviceData 选择的省份
     * @param cityData 选择的城市
     * @param disData 选择的地区
     */
    void confirm(Map<String, Object> proviceData, Map<String, Object> cityData, Map<String, Object> disData);
}
