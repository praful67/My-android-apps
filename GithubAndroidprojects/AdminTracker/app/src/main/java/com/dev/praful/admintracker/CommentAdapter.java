package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.internal.zzbfq.NULL;

public class CommentAdapter extends BaseAdapter {
    List<Comment> comments;
    Activity activity;
    ArrayList<Comment> arrayList;

    public CommentAdapter(List<Comment> comments, Activity activity) {
        this.comments = comments;
        this.activity = activity;
        this.arrayList = new ArrayList<Comment>();
        this.arrayList.addAll(comments);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.timeoffitem, null);
            final TextView name = (TextView) view.findViewById(R.id.employeename);
            final ExpandableTextView commentText = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
            name.setText(comments.get(position).getName());
            String dateofsubmit = comments.get(position).getDateofsubmit();
            String comment = comments.get(position).getComment();
            String stars = NULL;
            if (comments.get(position).getStars() != null)
                stars = comments.get(position).getStars();
            else
                stars = "Not submited";

            commentText.setText("Date of submit : " + dateofsubmit + "\n\nRating : " + stars + " / 5 " + "\n\nComment : " + comment);

            Button details = (Button) view.findViewById(R.id.employeedetails);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                    intent.putExtra("employeeid", comments.get(position).getId());
                    activity.startActivity(intent);
                }
            });
            ImageView done = (ImageView) view.findViewById(R.id.done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                    else
                        builder = new android.support.v7.app.AlertDialog.Builder(activity);

                    builder.setMessage("Do you want remove this comment ? ")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employee'sComments1")
                                            .child(comments.get(position).getId())
                                            .removeValue();


                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            });
        }
        return view;
    }

    public void filter(String chartext) {
        chartext = chartext.toLowerCase(Locale.getDefault());
        comments.clear();
        if (chartext.length() == 0) {
            comments.addAll(arrayList);
        } else {
            for (Comment comment : arrayList) {
                if (comment.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    comments.add(comment);
                }
            }
        }
        notifyDataSetChanged();

    }
}
