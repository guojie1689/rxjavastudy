package com.joyveb.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class FilteringActivity extends AppCompatActivity {

    private static String data[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    @OnClick(R.id.btn_debounce)
    protected void debounce() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subcribe) throws Exception {
                Integer threadSleep;

                for (int i = 0; i < data.length; i++) {
                    if ("3".equals(data[i])) {
                        threadSleep = 300;
                    } else {
                        threadSleep = 100;
                    }

                    Thread.sleep(threadSleep);

                    subcribe.onNext(data[i]);
                }

            }
        }).throttleWithTimeout(200, TimeUnit.MILLISECONDS).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, "throttle:" + s);
            }
        });

        Observable.fromArray(data).debounce(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull final String s) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        if ("2".equals(s)) {
                            e.onNext(s);
                            e.onComplete();
                        }
                    }
                });
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, "debounce:" + s);
            }
        });
    }

    @OnClick(R.id.btn_distinct)
    protected void distinct() {
        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1).distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "distinct:" + integer);
            }
        });

        Observable.just(1, 2, 3, 3, 3, 1, 2, 3, 3).distinctUntilChanged().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "distinctUntilChanged:" + integer);
            }
        });
    }

    @OnClick(R.id.btn_filter)
    protected void filterAndElementAt() {
        Observable.just(1, 2, 3, 4, 5, 6, 7).elementAt(10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "elementAt --- " + integer);
            }
        });

        Observable.just(1, 2, 3, 4, 5, 6, 7).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return integer > 3;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "filter --- " + integer);
            }
        });
    }

    @OnClick(R.id.btn_take_and_skip)
    protected void firstAndLast() {
        Observable.fromArray(data).skip(3).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, "firstElement:" + s);
            }
        });

        Observable.fromArray(data).take(3).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, "firstElement:" + s);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Filter操作符");

        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);
    }
}
