package com.example.MediaShare;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MultiAdapter extends RecyclerView.Adapter {
    private ArrayList<MultiModel> dataSet;
    private Context context;
    private int total_types;

    public MultiAdapter(ArrayList<MultiModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
        this.total_types = dataSet.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MultiModel.IMAGE_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_items,parent,false);
            return new ImageTypeViewHolder(view);
        }
        else  if (viewType == MultiModel.VIDEO_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_items,parent,false);
            return new VideoTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiModel object = dataSet.get(position);
        if(object!=null){
            switch (object.type){
                case MultiModel.IMAGE_TYPE:
                    Glide.with(context)
                            .load(dataSet.get(position).data.getImageUrl())
                                 .into(  ((ImageTypeViewHolder)holder).imageView);
                    break;
                case MultiModel.VIDEO_TYPE:
                    ((VideoTypeViewHolder)holder).videoView.setVideoURI(Uri.parse(object.data.getImageUrl()));
                    MediaController mediaController = new MediaController(context);
                    mediaController.setAnchorView(((VideoTypeViewHolder)holder).videoView);
                    ((VideoTypeViewHolder)holder).videoView.setMediaController(mediaController);
                    ((VideoTypeViewHolder)holder).videoView.seekTo(100);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageTypeViewHolder(View itemView){
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
        }
    }
    public static class VideoTypeViewHolder extends RecyclerView.ViewHolder{
        VideoView videoView;
        VideoTypeViewHolder(View itemView){
            super(itemView);
            this.videoView = itemView.findViewById(R.id.videoView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch(dataSet.get(position).type){
            case 0:
                return MultiModel.IMAGE_TYPE;

            case 1:
                return MultiModel.VIDEO_TYPE;
            default:
                return -1;

        }
    }
}
