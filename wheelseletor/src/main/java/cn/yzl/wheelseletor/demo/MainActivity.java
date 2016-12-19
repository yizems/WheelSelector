package cn.yzl.wheelseletor.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.yzl.wheelselector.mulpicker.AddressConfirmListener;
import cn.yzl.wheelselector.mulpicker.MulSelector;
import cn.yzl.wheelselector.singleselect.ItemConfirmListener;
import cn.yzl.wheelselector.singleselect.SingleSelector;

public class MainActivity extends AppCompatActivity {

    Button button1, button2;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.but1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleSelector singleSelector = new SingleSelector(MainActivity.this
                        , new String[]{"1", "二", "叁"}, "标题", getWindow());

                singleSelector.setConfirmListener(new ItemConfirmListener() {
                    @Override
                    public void confirm(int position, String text) {
                        Toast.makeText(MainActivity.this, position + ":::" + text, Toast.LENGTH_SHORT).show();
                    }
                });
                singleSelector.show(Gravity.BOTTOM);
            }
        });

        String s = "[{\"province_id\":\"79\",\"province_name\":\"北京\",\"city\":[{\"city_id\":\"31\",\"city_name\":\"东城\"},{\"city_id\":\"32\",\"city_name\":\"西城\"},{\"city_id\":\"35\",\"city_name\":\"朝阳\"},{\"city_id\":\"36\",\"city_name\":\"丰台\"},{\"city_id\":\"37\",\"city_name\":\"石景山\"},{\"city_id\":\"38\",\"city_name\":\"海淀\"},{\"city_id\":\"39\",\"city_name\":\"门头沟\"},{\"city_id\":\"40\",\"city_name\":\"房山\"},{\"city_id\":\"41\",\"city_name\":\"通州\"},{\"city_id\":\"42\",\"city_name\":\"顺义\"},{\"city_id\":\"43\",\"city_name\":\"昌平\"},{\"city_id\":\"44\",\"city_name\":\"大兴\"},{\"city_id\":\"45\",\"city_name\":\"怀柔\"},{\"city_id\":\"46\",\"city_name\":\"平谷\"},{\"city_id\":\"47\",\"city_name\":\"密云\"},{\"city_id\":\"48\",\"city_name\":\"延庆\"}]},{\"province_id\":\"90\",\"province_name\":\"上海\",\"city\":[{\"city_id\":\"49\",\"city_name\":\"黄浦\"},{\"city_id\":\"51\",\"city_name\":\"徐汇\"},{\"city_id\":\"52\",\"city_name\":\"长宁\"},{\"city_id\":\"53\",\"city_name\":\"静安\"},{\"city_id\":\"54\",\"city_name\":\"普陀\"},{\"city_id\":\"55\",\"city_name\":\"闸北\"},{\"city_id\":\"56\",\"city_name\":\"虹口\"},{\"city_id\":\"57\",\"city_name\":\"杨浦\"},{\"city_id\":\"58\",\"city_name\":\"闵行\"},{\"city_id\":\"59\",\"city_name\":\"宝山\"},{\"city_id\":\"60\",\"city_name\":\"嘉定\"},{\"city_id\":\"61\",\"city_name\":\"浦东\"},{\"city_id\":\"62\",\"city_name\":\"金山\"},{\"city_id\":\"63\",\"city_name\":\"松江\"},{\"city_id\":\"64\",\"city_name\":\"青浦\"},{\"city_id\":\"66\",\"city_name\":\"奉贤\"}]}]";
        data = new Gson().fromJson(s, new TypeToken<List<Map<String, Object>>>() {
        }.getType());


        button2 = (Button) findViewById(R.id.but2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MulSelector mulSelector = new MulSelector(MainActivity.this, data, getWindow(), true);

                mulSelector.setConfirmListener(new AddressConfirmListener() {
                    @Override
                    public void confirm(Map<String, Object> proviceData, Map<String, Object> cityData, Map<String, Object> disData) {

                    }
                });

                mulSelector.show(Gravity.BOTTOM);
            }
        });
    }


}
