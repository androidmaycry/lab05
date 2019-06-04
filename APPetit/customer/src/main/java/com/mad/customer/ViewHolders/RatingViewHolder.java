package com.mad.customer.ViewHolders;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.customer.R;
import com.mad.mylibrary.ReviewItem;

public class RatingViewHolder extends RecyclerView.ViewHolder {
    private TextView name, comment;
    private RatingBar ratingBar;
    private ImageView img;
    private View view;


    public RatingViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        name = itemView.findViewById(R.id.rating_item_name);
        comment  = itemView.findViewById(R.id.rating_item_comment);
        ratingBar = itemView.findViewById(R.id.ratingbaritem);
        img = itemView.findViewById(R.id.rating_item_img);
    }
    public View getView() {
        return view;
    }

    public void setData (ReviewItem ri){
        name.setText(ri.getName());
        if (ri.getComment()!=null){
            comment.setText(ri.getComment());
        }
        else{
            comment.setVisibility(View.GONE);
        }
        if(!ri.getImg().isEmpty() && ri.getImg()!=null && !ri.getImg().equals("null")){
            Glide.with(view.getContext()).load(ri.getImg()).into(img);
        }
    }
}
