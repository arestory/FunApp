package ywq.ares.funapp.util.rxjava2;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 用于处理 rxjava 完成或取消事件时的传递
 * Created by ares on 2017/5/4.
 */

public  abstract class DisposableConsumer implements Consumer<Disposable> {
    @Override
    public final void accept(@NonNull Disposable disposable) throws Exception {

        onDispose(disposable);
    }

    public abstract  void onDispose(Disposable disposable);



}
