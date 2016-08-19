package com.wangly.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * map 转换符，
 * 在实际开发当中我们可能需要将 Stream 流对象转换为 String，或者是将 数据库 cursor
 * 对象转换为JavaBean ,File 转换为 JavaBean 等等
 * 在这里例举一个常用类型转换
 * Integer -->  String
 * 反之亦然
 */
public class OperatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_long;
    private Button bt_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        initView();

    }

    private void initView() {
        bt_long = (Button) findViewById(R.id.bt_long);

        bt_long.setOnClickListener(this);
        bt_string = (Button) findViewById(R.id.bt_string);

        bt_string.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_long:
                fromStringToLong();
                break;
            case R.id.bt_string:
                fromIntToString();
                break;
            default:
                break;
        }
    }

    /**
     * //Map 转换将一个对象 转换为 另一个对象
     * new Func1<Integer, String>()
     * 在这里 Integer 代表的是转换之前的类型，第二个参数代表转换之后的类型
     * <p/>
     * 从 Int 转换为 String  反之亦然
     */
    private void fromIntToString() {
        int number = 102;
        //Map 转换将一个对象 转换为 另一个对象
        Observable.just(number)
                // 在这里 Integer 代表的是转换之前的类型，第二个参数代表转换之后的类型
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer + "";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("wangly", "转换结果" + s);
                    }
                });
    }

    /**
     * 从 String 转换 为 Long  反之亦然
     */
    private void fromStringToLong() {
        String number = "101";
        Observable.just(number)
                .map(new Func1<String, Long>() {
                    @Override
                    public Long call(String s) {
                        return Long.parseLong(s);
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long s) {
                        Log.d("wangly", "转换结果" + s);
                    }
                });
    }
}
