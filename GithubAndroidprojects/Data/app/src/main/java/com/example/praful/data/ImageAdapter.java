package com.example.praful.data;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Upload> {


    private Activity context;
    private List<Upload> uploads;
    private  int resource;
    public ImageAdapter( @NonNull Activity context , int resource,@NonNull List<Upload> uploads){
        super(context ,resource ,uploads);
        context = context;
        resource = resource;
        uploads = uploads;
    }
    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource , null);
        TextView tip1 = v.findViewById(R.id.tipUpload);
        TextView title1 = v.findViewById(R.id.titleUpload);
        TextView des1 = v.findViewById(R.id.desUpload);
        ImageView img = v.findViewById(R.id.imageViewUpload);

        tip1.setText(uploads.get(position).getTip());
        title1.setText(uploads.get(position).getTitle());
        des1.setText(uploads.get(position).getDes());
        Glide.with(context).load(uploads.get(position).getUrl()).into(img);
        return v;


    }
}

