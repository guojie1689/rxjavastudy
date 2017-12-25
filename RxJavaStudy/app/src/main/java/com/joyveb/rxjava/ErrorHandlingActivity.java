package com.joyveb.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ErrorHandlingActivity extends AppCompatActivity {

    private Observable<String> createCatchObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subscribe) throws Exception {
                for (int i = 0; i < 6; i++) {
                    if (i < 3) {
                        subscribe.onNext(i + "");
                    } else {
                        subscribe.onError(new Throwable("on Errror"));
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_catch_error_return)
    protected void catchTest() {

        Observable<String> observable = createCatchObservable();

        observable.onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(@NonNull Throwable throwable) throws Exception {
                return "onErrorReturn";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(MainActivity.TAG, "onSubscribe --- ");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(MainActivity.TAG, "onNext : " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(MainActivity.TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(MainActivity.TAG, "onComplete");
            }
        });
    }

    @OnClick(R.id.btn_catch_error_resume_next)
    protected void catchErrorResumeNext() {
        Observable<String> observable = createCatchObservable();
        Observable<String> observable1 = Observable.fromArray("7", "8", "9");

        observable.onErrorResumeNext(observable1).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(MainActivity.TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(MainActivity.TAG, "onNext : " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(MainActivity.TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(MainActivity.TAG, "onComplete");
            }
        });
    }

    private Observable<String> createExceptionObservable(final boolean isCreateException) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subcribe) throws Exception {
                for (int i = 1; i < 6; i++) {
                    if (i < 3) {
                        subcribe.onNext(i + "");
                    } else if (isCreateException) {
                        subcribe.onError(new Exception("Exception"));
                    } else {
                        subcribe.onError(new Throwable("throw error"));
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_on_error_resume_next)
    protected void onErrorResumeNext() {
        createExceptionObservable(true).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(MainActivity.TAG, "onSubscribe --");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(MainActivity.TAG, "onNext -- " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(MainActivity.TAG, "onError -- " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(MainActivity.TAG, "onComplete -- ");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("ErrorHandling");

        setContentView(R.layout.activity_error_handing);

        ButterKnife.bind(this);
    }
}
