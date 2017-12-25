package com.joyveb.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.KeyInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";
    private static String[] testData = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    protected void create() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subcriber) throws Exception {
                for (int i = 0; i < testData.length; i++) {
                    subcriber.onNext("i:" + testData[i]);
                }

                subcriber.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "s:" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete --");
            }
        });
    }


    @OnClick(R.id.btn_range)
    protected void range() {
        Observable.just(testData).range(3, 2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("GJ", "value:" + integer.intValue());
            }
        });
    }

    @OnClick(R.id.btn_defer)
    protected void deferAndJust() {
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("123");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "deffer:" + s);
            }
        });
    }

    @OnClick(R.id.btn_from)
    protected void from() {
        Observable.fromArray(testData).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "from:" + s);
            }
        });

        Observable.just(testData).subscribe(new Consumer<String[]>() {
            @Override
            public void accept(String[] strings) throws Exception {
                Log.d(TAG, "just -- ");
            }
        });
    }

    private Disposable disposable;

    @OnClick(R.id.btn_interval)
    protected void interval() {

        Consumer<Long> consumer = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "interval:" + aLong);
                if (aLong >= 10) {
                    disposable.dispose();
                }
            }
        };

        disposable = Observable.interval(1, TimeUnit.SECONDS).subscribe(consumer);
    }

    @OnClick(R.id.btn_repeat)
    protected void repeat() {
        Observable.just("1").repeat(3).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "repeat:" + s);
            }
        });

        Observable.just("1").timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "timer:" + aLong);
            }
        });
    }

    @OnClick(R.id.btn_transform)
    protected void transform() {
        Intent intent = new Intent(this, TransformingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_filter)
    protected void filter() {
        Intent intent = new Intent(this, FilteringActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_combining_activity)
    protected void combiningActivity() {
        Intent intent = new Intent(this, CombiningActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_error_handing)
    protected void errorHandingActivity() {
        Intent intent = new Intent(this, ErrorHandlingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("RXJava基本操作符");

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Button btn = (Button) findViewById(R.id.btn_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

    }
}
