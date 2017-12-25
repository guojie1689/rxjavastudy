package com.joyveb.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;

public class CombiningActivity extends AppCompatActivity {

    private ObservableSource<String> createObserval(final int idx) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subscribe) throws Exception {
                for (int i = 0; i < 6; i++) {
                    String info = idx + ":" + i;
                    subscribe.onNext(info);
                }

                subscribe.onComplete();
            }
        });
    }

    @OnClick(R.id.btn_combineLatest)
    protected void combineLatest() {

        Observable.combineLatest(createObserval(0), createObserval(1), new BiFunction<String, String, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                Log.d(MainActivity.TAG, "s:" + s);
                Log.d(MainActivity.TAG, "s2:" + s2);

                return s + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(MainActivity.TAG, "accept:" + s);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Filter操作符");

        setContentView(R.layout.activity_combing);

        ButterKnife.bind(this);
    }
}
