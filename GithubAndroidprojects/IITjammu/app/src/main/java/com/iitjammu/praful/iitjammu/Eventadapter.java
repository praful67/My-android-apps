package com.iitjammu.praful.iitjammu;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Eventadapter extends RecyclerView.Adapter<Eventadapter.RecyclerViewHolder> {

    List<Eventitem> eventitems;
    Activity activity;

    public Eventadapter(List<Eventitem> eventitems, Activity activity) {
        this.eventitems = eventitems;
        this.activity = activity;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.eventitem, null);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {

        holder.description.setText(eventitems.get(position).getDescription());
        Picasso.with(activity).load(eventitems.get(position).getImageurl()).into(holder.imageView);
        holder.zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageZoomActivity.class);
                intent.putExtra("imageurl", eventitems.get(position).getImageurl());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventitems.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, zoom;
        TextView description;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            zoom = (ImageView) itemView.findViewById(R.id.zoom);
            imageView.setOnTouchListener(new ImageMatrixTouchHandler(itemView.getContext()));
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

}
