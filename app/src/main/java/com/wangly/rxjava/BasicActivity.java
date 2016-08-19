package com.wangly.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_obServable;
    private Button bt_from;
    private Button bt_just;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        initView();
    }

    private void initView() {
        bt_obServable = (Button) findViewById(R.id.bt_obServable);
        bt_obServable.setOnClickListener(this);
        bt_from = (Button) findViewById(R.id.bt_from);
        bt_from.setOnClickListener(this);
        bt_just = (Button) findViewById(R.id.bt_just);
        bt_just.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_obServable:
                obServableMethod();
                break;
            case R.id.bt_from:
                fromMethod();
                break;
            case R.id.bt_just:
                justMethod();
                break;
            default:
                break;
        }
    }

    private void justMethod() {
        //传递 T t 类型
        Observable.just(555, 1111)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d("wangly", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("wangly", "得到的结果" + integer);
                    }
                });

    }


    /**
     * 从from 源码中可知 接收的是 T[] 类型
     * <p/>
     * Action1  根据 只不过是 Interface 等同于 onNext()
     * Action1    等同于 onError()
     * Action0  等同于 Complete()
     * 追踪源码 subscribe可知
     * 内部已经实现了 return Observable.subscribe(subscriber, this); 可知
     */
    private void fromMethod() {
        Integer[] array = {15, 200, 47};
        Observable.from(array)
                .subscribe(new Action1<Integer>() {  // onNext()
                    @Override
                    public void call(Integer s) {

                    }
                }, new Action1<Throwable>() {  // 等同于 onError()
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() { // Complete()

                    }
                });

        //--------当然你也可以使用 以下方式-------
       /* subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("wangly", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String o) {
                Log.d("wangly", "得到的结果" + o);
            }
        };

        Observable.from(array)
                .subscribe(subscriber);*/

    }


    private void obServableMethod() {
        //创建 Observable 被观察者

        // 在这里需要简单的了解下该方式运行在 mainThread的 需要指定 subscribeOn 将其定义在 子 Thread 当中 充分的实现了异步操作
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {   //相关操作计算

                subscriber.onNext("Hello Android");
                subscriber.onNext("Hi");  // onNext 可以调用多次
                subscriber.onCompleted();  //最终完成之后关闭所有操作的方法
            }
        });

        observable.subscribe(new Observer<String>() { // 指定观察者(订阅者)
            @Override
            public void onCompleted() {
                Log.d("wangly", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("wangly", "onError");
            }

            @Override
            public void onNext(String s) {
                Log.d("wangly", "得到的结果" + s);
            }
        });

    }
}
