package ywq.ares.funapp.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ywq.ares.funapp.R;
import ywq.ares.funapp.bean.Actress;
import ywq.ares.funapp.bean.ActressSearchItem;

public class ActressesAdapter extends BaseQuickAdapter<Actress,BaseViewHolder> {



    public ActressesAdapter() {

        super(R.layout.item_actress);
    }



    @Override
    protected void convert(BaseViewHolder helper, Actress item) {

        helper.setText(R.id.tvName,item.getName());
        ImageView cover =  helper.getView(R.id.ivCover);

//        GlideApp.with(cover).load(item.getAvatar()).placeholder(R.drawable.ic_launcher_background).into(cover);
        Glide.with(cover).load(item.getAvatar()).into(cover);
    }


}
