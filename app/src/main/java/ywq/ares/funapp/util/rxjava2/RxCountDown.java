package ywq.ares.funapp.util.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于实现倒计时
 * Created by ares on 2017/5/4.
 */

public class RxCountDown {


    public static Observable<Integer> countDown(int time) {

        if (time < 0) time = 0;


        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long increaseTime) throws Exception {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }


    /**
     *
     * @param time 倒计时秒数
     * @param dataConsumer 秒数递减
     * @param completeAction 完成倒计时
     * @param throwableConsumer
     * @param disposableConsumer
     * @return
     */
    public static Disposable countDown(int time,DataConsumer<Integer> dataConsumer,CompleteAction completeAction,ThrowableConsumer throwableConsumer,DisposableConsumer disposableConsumer){

        if (time < 0) time = 0;


        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long increaseTime) throws Exception {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1).subscribe(dataConsumer,throwableConsumer,completeAction,disposableConsumer);


    }



}