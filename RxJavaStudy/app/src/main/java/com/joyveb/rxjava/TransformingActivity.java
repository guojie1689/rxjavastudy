package com.joyveb.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class TransformingActivity extends AppCompatActivity {

    private static String data[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    @OnClick(R.id.btn_buffer)
    protected void buffer() {
        Observable.just("1", "2", "3", "4", "5", "6", "7").buffer(2, 4).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < strings.size(); i++) {
                    sb.append(strings.get(i));
                    sb.append(",");
                }

                Log.d(MainActivity.TAG, sb.toString());
            }
        });
    }

    @OnClick(R.id.btn_flatmap)
    protected void flatmap() {
        Observable.just("1", "2", "3").flatMap(new Function<String, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull String s) throws Exception {
                return Observable.just(Integer.parseInt(s));
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "flatmap:" + integer);
            }
        });
    }

    @OnClick(R.id.btn_groupby)
    protected void groupby() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8).groupBy(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                return integer % 2;
            }
        }).subscribe(new Consumer<GroupedObservable<Integer, Integer>>() {
            @Override
            public void accept(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) throws Exception {
                integerIntegerGroupedObservable.count().subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(MainActivity.TAG, integerIntegerGroupedObservable.getKey() + " contains:" + aLong);
                    }
                });
            }
        });
    }

    @OnClick(R.id.btn_map)
    protected void mapAndCast() {
        Observable.just(1, 2, 3).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                return integer * 10;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(MainActivity.TAG, "mapAndCast:" + integer);
            }
        });
    }

    @OnClick(R.id.btn_scan)
    protected void scan() {
        Observable.fromArray(1, 2, 3, 4).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return integer * integer2;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(MainActivity.TAG, "scan result:" + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(MainActivity.TAG, "onComplete --");
            }
        });
    }

    @OnClick(R.id.btn_window)
    protected void window() {
        Observable.fromArray(data).window(2).subscribe(new Consumer<Observable<String>>() {
            @Override
            public void accept(Observable<String> stringObservable) throws Exception {
                stringObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(MainActivity.TAG, "window:" + s);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Transforming Observables基本操作符");

        setContentView(R.layout.activity_transform);

        ButterKnife.bind(this);
    }
}
