package com.wangly.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button bt_operator;
    private Subscriber<String> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        start = (Button) findViewById(R.id.bt_basic);
        start.setOnClickListener(this);
        bt_operator = (Button) findViewById(R.id.bt_operator);
        bt_operator.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {


            case R.id.bt_basic:
                intent = new Intent(MainActivity.this, BasicActivity.class);
                startActivity(intent);


                threadState();

                break;
            case R.id.bt_operator:
                intent = new Intent(MainActivity.this, OperatorActivity.class);
                startActivity(intent);

//                threadState();

                break;

            default:
                break;
        }
    }







    //---------------------------------操作符------------------------


    /**
     * map 转换符，
     * 在实际开发当中我们可能需要将 Stream 流对象转换为 String，或者是将 数据库 cursor
     * 对象转换为JavaBean ,File 转换为 JavaBean 等等
     * 在这里例举一个常用类型转换
     * Integer -->  String
     * 反之亦然
     */

    private void typeSwitch() {
        int array = 1;
        //Map 转换将一个对象 转换为 另一个对象
        Observable.just(array)
                // 在这里 Integer 代表的是转换之前的类型，第二个参数代表转换之后的类型
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer + "";
                    }
                })
                // 将 String 转换为 Long 类型
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


//    ------------- 线程 状态--------------------


    public void threadState() {
        final Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {   //相关操作计算

                System.out.println("call 运行在" + Thread.currentThread().getName());
                int result = 10 / 2;
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())  // 处理在 子 Thread
                .observeOn(AndroidSchedulers.mainThread());  // 结果运行在mainThread


        observable.subscribe(new Observer<Integer>() { // 指定观察者(订阅者)
            @Override
            public void onCompleted() {
                System.out.println("onCompleted 运行在" + Thread.currentThread().getName());
                Log.d("wangly", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("wangly", "onError");
            }

            @Override
            public void onNext(Integer s) {

                System.out.println("onNext 运行在" + Thread.currentThread().getName());
                Log.d("wangly", "得到的结果" + s);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        // 取消订阅
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }


}
