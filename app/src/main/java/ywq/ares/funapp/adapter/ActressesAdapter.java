package ywq.ares.funapp.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ywq.ares.funapp.R;
import ywq.ares.funapp.bean.Actress;
import ywq.ares.funapp.bean.ActressSearchItem;
import ywq.ares.funapp.http.DataSource;

public class ActressesAdapter extends BaseQuickAdapter<Actress, BaseViewHolder> {


    public ActressesAdapter() {

        super(R.layout.item_actress);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final Actress item) {

        helper.setText(R.id.tvName, item.getName());
        ImageView cover = helper.getView(R.id.ivCover);

//        GlideApp.with(cover).load(item.getAvatar()).placeholder(R.drawable.ic_launcher_background).into(cover);
        if(DataSource.INSTANCE.isOpenPhoto()){
            Glide.with(cover).load(item.getAvatar()).into(cover);

        }else{
            Glide.with(cover).asDrawable().load(R.drawable.avatar).into(cover);

        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClick != null) {

                    onItemClick.click(helper.getAdapterPosition(), item);
                }
            }
        });
    }



    private OnItemClick onItemClick;


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    interface OnItemClick {


        void click(int pos, Actress item);
    }

}
