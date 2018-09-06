package ywq.ares.funapp.adapter;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ares.downloader.jarvis.Jarvis;
import com.ares.downloader.jarvis.core.DownloadListener;
import com.ares.downloader.jarvis.core.DownloadState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawableResource;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ywq.ares.funapp.R;

public class PhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public PhotoAdapter() {
        super(R.layout.item_photo);

    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {


        ((TextView) helper.getView(R.id.tv)).setText(item);
        final ProgressBar progressBar = (ProgressBar) helper.getView(R.id.pb);
        final Button startBtn = helper.getView(R.id.startBtn);

        final Jarvis.Downloader downloader = Jarvis.with(helper.itemView.getContext()).withUrl(item).threadCount(4).filePath(Environment.getExternalStorageDirectory() + File.separator + "testss").setDownloadListener(new DownloadListener() {
            @Override
            public void onSuccess(File file) {

                if (item.endsWith(".gif")) {


//                    GlideApp.with(helper.itemView.getContext()).asGif().load(file).thumbnail(0.1f).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) helper.getView(R.id.iv));
                } else {
//                    GlideApp.with(helper.itemView.getContext()).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) helper.getView(R.id.iv));

                }

                startBtn.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(long l, float v) {

                System.out.println("pb = " + v);
                progressBar.setProgress((int) (v * 100));
            }

            @Override
            public void onStart() {

                startBtn.setText("正在下载");
            }

            @Override
            public void onPause() {

                startBtn.setText("继续下载");
            }

            @Override
            public void onFail() {
                startBtn.setText("下载失败，点击重试");

            }

            @Override
            public void onDelete(boolean b) {

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloader.getDownloadState() == DownloadState.PAUSE) {

                    downloader.download();
                }else{
                    downloader.pause();
                }
            }
        });


    }
}
