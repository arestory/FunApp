package ywq.ares.funapp.util.rxjava2;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ywq.ares.funapp.util.ExceptionHandle;

/**
 * 用于处理异常的消费行为
 * Created by ares on 2017/5/3.
 */

public abstract class ThrowableConsumer implements Consumer<Throwable> {


    @Override
    public void accept(@NonNull Throwable e) throws Exception {

        if(e instanceof ExceptionHandle.ResponseThrowable){
            handle((ExceptionHandle.ResponseThrowable)e);
        } else {
            handle(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    public abstract void handle(ExceptionHandle.ResponseThrowable e);
}
