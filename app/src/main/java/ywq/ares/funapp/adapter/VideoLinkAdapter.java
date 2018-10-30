package ywq.ares.funapp.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import ywq.ares.funapp.R;
import ywq.ares.funapp.bean.Actress;
import ywq.ares.funapp.bean.VideoSearchItem;
import ywq.ares.funapp.common.GlideApp;
import ywq.ares.funapp.http.DataSource;

public class VideoLinkAdapter extends BaseQuickAdapter<VideoSearchItem, BaseViewHolder> {


    public VideoLinkAdapter() {

        super(R.layout.item_video);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final VideoSearchItem item) {

        helper.setText(R.id.tvTitle, item.getName());
        helper.setText(R.id.tvSize, item.getSize());
        helper.setText(R.id.tvDate, item.getDate());
        helper.setText(R.id.tvLink, item.getMagnet());

//
        helper.itemView.setOnClickListener(v -> {

            if (onItemClick != null) {

                onItemClick.click(helper.getAdapterPosition(), item);
            }
        });

    }


    private OnItemClick onItemClick;


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {


        void click(int pos, VideoSearchItem item);
    }

}
