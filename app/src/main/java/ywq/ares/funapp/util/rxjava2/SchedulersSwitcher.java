package ywq.ares.funapp.util.rxjava2;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ywq.ares.funapp.util.HttpResponseFunc;

/**
 * 用于切换线程
 * Created by ares on 2017/6/22.
 */

public class SchedulersSwitcher {

    /**
     * IO 线程到 UI 线程
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io2UiThread() {


        return (upstream)->{
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        };

    }

    /**
     *  IO 线程到 UI 线程，并统一处理错误，主要用于网络请求
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io2UiThreadAndHandleError() {


        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onErrorResumeNext(new HttpResponseFunc<T>()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * io到io，未处理异常
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io2Io() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }


    /**
     * IO到IO，已处理异常
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io2IoAndHandleError() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onErrorResumeNext(new HttpResponseFunc<T>()).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }
}
