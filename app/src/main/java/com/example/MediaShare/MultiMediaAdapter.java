package com.example.MediaShare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MultiMediaAdapter extends RecyclerView.Adapter {
    private ArrayList<MultiMedia> dataSet;
    private Context context;
    private View view;


    public MultiMediaAdapter(ArrayList<MultiMedia> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MultiMedia.IMAGE_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_items,parent,false);
            return new ImageTypeViewHolder(view);
        }
        else  if (viewType == MultiMedia.VIDEO_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_items,parent,false);
            return new VideoTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiMedia object = dataSet.get(position);

        if(object!=null){

            switch (object.type){

                //Set the media object in the list
                case MultiMedia.IMAGE_TYPE:
                    Glide.with(context)
                            .load(dataSet.get(position).data.getImageUrl())
                                 .into(  ((ImageTypeViewHolder)holder).imageView);

                    //Sets the text of who uploaded the media file.
                    ((ImageTypeViewHolder) holder).textView_image.setText(dataSet.get(position).data.getUsername());


                    break;
                case MultiMedia.VIDEO_TYPE:

                    ((VideoTypeViewHolder) holder).textView_video.setText(dataSet.get(position).data.getUsername());

                    VideoTypeViewHolder video_holder = ((VideoTypeViewHolder)holder);
                    video_holder.videoView.setVideoPath(object.data.getImageUrl());
                    video_holder.videoView.start();
                    break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //Inflates the current image.
    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView_image;
        ImageTypeViewHolder(View itemView){
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textView_image = itemView.findViewById(R.id.textView_image);

        }
    }

    //Inflates the current video.
    public static class VideoTypeViewHolder extends RecyclerView.ViewHolder{
        VideoView videoView;
        TextView textView_video;
        VideoTypeViewHolder(View itemView ){
            super(itemView);
            this.videoView = itemView.findViewById(R.id.videoView);
            this.textView_video = itemView.findViewById(R.id.textView_video);

        }
    }

    @Override
    public int getItemViewType(int position) {
        switch(dataSet.get(position).type){
            case 0:
                return MultiMedia.IMAGE_TYPE;
            case 1:
                return MultiMedia.VIDEO_TYPE;
            default:
                return -1;
        }
    }

    //If video is on the screen, starts paying
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.getItemViewType()==1){
            VideoTypeViewHolder video_holder = ((VideoTypeViewHolder)holder);
            video_holder.videoView.start();
        }
    }

    //If video is not on the screen, stops paying
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.getItemViewType()==1){
            VideoTypeViewHolder video_holder = ((VideoTypeViewHolder)holder);
            video_holder.videoView.stopPlayback();
        }
    }

    public ArrayList<MultiMedia> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<MultiMedia> dataSet) {
        this.dataSet = dataSet;
    }


}
