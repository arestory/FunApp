package ywq.ares.funapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ares.datacontentlayout.DataContentLayout;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ywq.ares.funapp.R;
import ywq.ares.funapp.common.GlideApp;
import ywq.ares.funapp.http.AppConstants;

public class PictureFragment extends Fragment {

    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public static PictureFragment newInstance(String imageUrl) {
        PictureFragment fragment = new PictureFragment();
        fragment.setImageUrl(imageUrl);
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        fragment.setArguments(args);
        Log.d("ImageFragment", "ImageFragment create");
        return fragment;
    }

    public String getImageName() {

        String attr[] = getImageUrl().split("/");

        return attr[attr.length - 1];
    }

    private Bitmap saveBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_show_photo, container, false);
        System.out.println("onCreateView");

        final DataContentLayout loadingLayout = rootView.findViewById(R.id.dataLayout);
        final PhotoView photoView = rootView.findViewById(R.id.ivTarget);
        photoView.enable();
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);


        loadImage(rootView.getContext(), getImageUrl(), photoView, loadingLayout);

//        GlideApp.with(photoView.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cover)
//                .into(photoView);

        loadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();

                }
            }
        });
        photoView.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();

            }
        });

        photoView.setOnLongClickListener(v -> {


                    if (saveBitmap == null) {


                        GlideApp.with(this).asFile().load(getImageUrl()).addListener(new RequestListener<File>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {

                                Toast.makeText(getActivity(), "正在保存，请稍等", Toast.LENGTH_LONG).show();

                                new File(AppConstants.LOCAL.IMAGE_CACHE + File.separator).mkdirs();
                                System.out.println("缓存路径" + resource.getPath());

                                copyFile(resource, AppConstants.LOCAL.IMAGE_CACHE + File.separator + getImageName(), new OnSaveListener() {
                                    @Override
                                    public void onSaveSuccess(File file) {
                                        Toast.makeText(getActivity(), "保存成功，路径：" + file.getPath(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onFail(Throwable throwable) {
                                        Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();

                                        throwable.printStackTrace();
                                    }
                                });


                                return false;
                            }
                        }).submit();
                    } else {
                        new File(AppConstants.LOCAL.IMAGE_CACHE + File.separator).mkdirs();

                        saveFile(saveBitmap, AppConstants.LOCAL.IMAGE_CACHE + File.separator + getImageName(), new OnSaveListener() {
                            @Override
                            public void onSaveSuccess(File file) {
                                Toast.makeText(getActivity(), "保存成功，路径：" + file.getPath(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFail(Throwable throwable) {
                                Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();

                                throwable.printStackTrace();
                            }
                        });
                    }
                    return false;
                }

        );

        return rootView;
    }

    public interface OnSaveListener {

        void onSaveSuccess(File file);

        void onFail(Throwable throwable);
    }

    @SuppressLint("CheckResult")
    private void copyFile(File targetFile, String newPath, OnSaveListener listener) {


        Observable.just(newPath).subscribeOn(Schedulers.io()).map((Function<String, File>) s -> {
            try {
                return copyFile(targetFile.getPath(), newPath);
            } catch (Exception e) {

                e.printStackTrace();
                return null;

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(listener::onSaveSuccess, listener::onFail);


    }

    @SuppressLint("CheckResult")
    private void saveFile(Bitmap bitmap, String newPath, OnSaveListener listener){
        Observable.just(newPath).subscribeOn(Schedulers.io()).map((Function<String, File>) s -> {

            File myCaptureFile = new File(newPath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();

            return myCaptureFile;

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(listener::onSaveSuccess, listener::onFail);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (saveBitmap != null) {
            saveBitmap =null;
        }
    }

    /**
     * 将文件拷贝到指定目录
     *
     * @param oldAddress 文件所在目录(文件的全路径)
     * @param newAddress 指定目录(包含复制文件的全名称)
     * @throws Exception
     */
    private File copyFile(String oldAddress, String newAddress) throws Exception {
        FileInputStream input = new FileInputStream(oldAddress);
        FileOutputStream output = new FileOutputStream(newAddress);//注意：newAddress必须包含文件名字，比如说将D:/AAA文件夹下的文件"a.xml"复制到D:\test目录下，则newAddress必须为D:\test\a.xml
        int in = input.read();
        while (in != -1) {
            output.write(in);
            in = input.read();
        }
        input.close();
        output.close();

        return new File(newAddress);
    }


    public void loadImage(final Context context, final String imageUrl, final PhotoView photoView, final DataContentLayout loadingLayout) {

        loadingLayout.showLoading();

        GlideApp.with(context).asBitmap().load(imageUrl)

                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                        loadingLayout.showError(new DataContentLayout.ErrorListener() {
                            @Override
                            public void showError(@NotNull View view) {

                                loadImage(context, imageUrl, photoView, loadingLayout);
                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        System.out.println("onResourceReady....");
                        photoView.setImageBitmap(resource);
                        loadingLayout.showContent();
                        saveBitmap = resource;
                        return false;
                    }
                }).submit();

    }
}

