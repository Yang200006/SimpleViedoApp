package com.example.video;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoAdapter extends BaseAdapter {
    Context context;
    List<VideoBean.ItemListDTO> mList;

    public VideoAdapter(Context context, List<VideoBean.ItemListDTO> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //獲取指定位置的數據源
        VideoBean.ItemListDTO.DataDTO dataDTO = mList.get(position).getData();
        //設置發布者的信息
        VideoBean.ItemListDTO.DataDTO.AuthorDTO author = dataDTO.getAuthor();
        holder.item_main_tv_name.setText(author.getName());
        holder.item_main_tv_desc.setText(author.getDescription());
        String iconURL = author.getIcon();
        if (!TextUtils.isEmpty(iconURL)){
            Picasso.with(context).load(iconURL).into(holder.item_main_iv);
        }
        //獲取點讚數和評論數
        VideoBean.ItemListDTO.DataDTO.ConsumptionDTO consumption = dataDTO.getConsumption();
        holder.item_main_iv_heart.setText(consumption.getRealCollectionCount() + "");
        holder.item_main_iv_reply.setText(consumption.getReplyCount() + "");
        //設置視頻撥放器的信息
        holder.jzvdStd.setUp(dataDTO.getPlayUrl(),dataDTO.getTitle(),JzvdStd.SCREEN_NORMAL);
        String posterUrl = dataDTO.getCover().getFeed(); //縮略圖的網路地址
        if (!TextUtils.isEmpty(posterUrl)){
            Picasso.with(context).load(posterUrl).into(holder.jzvdStd.posterImageView);
        }
        holder.jzvdStd.positionInList = position;
        return convertView;
    }

    class ViewHolder{
        JzvdStd jzvdStd;
        ImageView item_main_iv;
        TextView item_main_tv_name;
        TextView item_main_iv_reply;
        TextView item_main_iv_heart;
        TextView item_main_tv_desc;

        public ViewHolder (View view){
            jzvdStd = view.findViewById(R.id.item_main);
            item_main_iv = view.findViewById(R.id.item_main_iv);
            item_main_tv_name = view.findViewById(R.id.item_main_tv_name);
            item_main_iv_reply = view.findViewById(R.id.item_main_iv_reply);
            item_main_iv_heart = view.findViewById(R.id.item_main_iv_heart);
            item_main_tv_desc = view.findViewById(R.id.item_main_tv_desc);
        }


    }
}
