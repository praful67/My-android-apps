package com.example.praful.blogretrive;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageListAdapter extends ArrayAdapter<ImageUpload> {

    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;

    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        listImage = objects;
        java.util.Collections.reverse(listImage);
        this.context = context;
        this.resource = resource;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource , null);
        TextView tvName = v.findViewById(R.id.tvImageName);
        TextView tip = v.findViewById(R.id.tip);
        TextView des = v.findViewById(R.id.des);
        ImageView img = v.findViewById(R.id.imgView);

        tvName.setText(listImage.get(position).getName());
        tip.setText(listImage.get(position).getTip());
        des.setText(listImage.get(position).getDes());
        Glide.with(context).load(listImage.get(position).getUrl()).placeholder(R.mipmap.ic_launcher).into(img);
        return v;


    }
}
