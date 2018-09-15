package ywq.ares.funapp.fragments;

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

import com.ares.datacontentlayout.DataContentLayout;
import com.bm.library.PhotoView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import ywq.ares.funapp.R;
import ywq.ares.funapp.common.GlideApp;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_show_photo, container,false);
        System.out.println("onCreateView");

        final DataContentLayout loadingLayout = rootView.findViewById(R.id.dataLayout);
        final PhotoView photoView =rootView.findViewById(R.id.ivTarget);
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
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();

                }
            }
        });

        return rootView;
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
                        return false;
                    }
                }).submit();

    }
}

