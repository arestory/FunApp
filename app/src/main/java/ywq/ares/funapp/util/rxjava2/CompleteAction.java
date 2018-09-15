package ywq.ares.funapp.util.rxjava2;

import io.reactivex.functions.Action;

/**
 * 用于定义 RxJava2中的完成操作
 * Created by ares on 2017/5/3.
 */

public abstract class CompleteAction implements Action {


    @Override
    public final void run() throws Exception {

        complete();
    }

    public abstract void complete() throws Exception;
}
