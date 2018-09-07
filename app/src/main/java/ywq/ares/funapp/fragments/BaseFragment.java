package ywq.ares.funapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {




    public static final String STATE_HIDDEN = "STATE_HIDDEN";
    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;
    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;


    /**
     * 是否加载完成
     * 当执行完onCreateView,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        mActivity = getActivity();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_HIDDEN,isHidden());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if(isSupportHidden){
                ft.hide(this);
            }else{
                ft.show(this);
            }
            ft.commitAllowingStateLoss();
        }

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(getLayoutId(), container, false);


        initData(getArguments(),mRootView);


        mIsPrepare = true;


        return mRootView;
    }



    /**
     *
     * @return 根布局资源id
     */
    protected abstract int getLayoutId();
    /**
     *  初始化数据
     * @param arguments 接收到的从其他地方传递过来的参数'
     * @param rootView 根view
     */
    protected abstract void initData(Bundle arguments,View rootView);


    @Override
    public void onDestroy() {
        super.onDestroy();
        whenDestroy();
    }

    public  abstract void whenDestroy();

    /**
     * 显示短toast
     * @param msg
     */
    protected void showShortToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {

        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        super.onDestroyView();
    }

    /**
     * 显示长toast
     * @param msg
     */
    protected void showLongToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

}
