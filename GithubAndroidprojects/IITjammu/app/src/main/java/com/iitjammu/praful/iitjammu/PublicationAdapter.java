package com.iitjammu.praful.iitjammu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.RecyclerViewHolder> {

    List<Publicationitem> publicationitems;
    Activity activity;
    Bitmap bitmap;

    public PublicationAdapter(List<Publicationitem> publicationitems, Activity activity) {
        this.publicationitems = publicationitems;
        this.activity = activity;

    }

    @Override
    public PublicationAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.publicationitem, null);

        return new PublicationAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.description.setText(publicationitems.get(position).getDescription());

      /*  AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = Picasso.with(activity).load(publicationitems.get(position).getImageurl()).get();

                } catch (IOException e) {e.printStackTrace();}
            }
        });*/
        Picasso.with(activity).load(publicationitems.get(position).getImageurl()).into(holder.imageView);
        holder.zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageZoomActivity.class);
                intent.putExtra("imageurl", publicationitems.get(position).getImageurl());
                activity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return publicationitems.size();
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
