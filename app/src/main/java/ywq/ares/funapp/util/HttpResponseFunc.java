package ywq.ares.funapp.util;


import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 用于转换 rxJava 中的 自定义异常,向目标方向传递，已适配RxJava2
 * Created by ares on 2017/4/27.
 */

public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {

//
//    @Override
//    public Observable<T> call(Throwable throwable) {
//        return Observable.error(ExceptionHandle.handleException(throwable));
//    }

    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
