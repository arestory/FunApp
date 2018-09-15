package ywq.ares.funapp.util.rxjava2;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 用于处理 rxjava 接受数据的事件传递
 * Created by ares on 2017/5/4.
 */

public abstract class DataConsumer<T> implements Consumer<T> {

    @Override
    public void accept(@NonNull T t) throws Exception {

        acceptData(t);
    }


    public abstract  void acceptData(@NonNull T t);

}
