package com.mad.customer.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.customer.UI.OrderingFragment;
import com.mad.customer.R;
import com.mad.customer.UI.TabApp;
import com.mad.mylibrary.Restaurateur;

import java.util.HashMap;
import java.util.UUID;

import static com.mad.mylibrary.SharedClass.CUSTOMER_PATH;
import static com.mad.mylibrary.SharedClass.ROOT_UID;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView name;
    private TextView addr;
    private TextView cuisine;
    private TextView opening;
    private ImageView img;
    private Restaurateur current;
    private String key;
    private Context context;
    private boolean favorite_bool;

    public RestaurantViewHolder(View itemView, Context context){
        super(itemView);
        name = itemView.findViewById(R.id.restaurant_name);
        addr = itemView.findViewById(R.id.listview_address);
        cuisine = itemView.findViewById(R.id.listview_cuisine);
        img = itemView.findViewById(R.id.restaurant_image);
        opening = itemView.findViewById(R.id.listview_opening);
        this.context = context;
        favorite_bool = false;
        itemView.setOnClickListener(this);
    }

    public void setData (Restaurateur current, int position, String key){
        this.name.setText(current.getName());
        this.addr.setText(current.getAddr());
        this.cuisine.setText(current.getCuisine());
        this.opening.setText(current.getOpeningTime());
        if(!current.getPhotoUri().equals("null")) {
            Glide.with(itemView).load(current.getPhotoUri()).into(this.img);
        }
        this.current = current;
        this.key = key;
        Drawable d;
        ImageView favorite = itemView.findViewById(R.id.star_favorite);
        favorite.setOnClickListener(e ->{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(CUSTOMER_PATH).child(ROOT_UID).child("favorite");
            if (favorite_bool) {
                ref.child(key).removeValue();
                ImageView start = itemView.findViewById(R.id.star_favorite);
                start.setImageResource(R.drawable.heart);
                favorite_bool = false;

                Toast.makeText(context,"Remove from favorite",
                        Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> favorite_restaurant = new HashMap<String, Object>();
                favorite_restaurant.put(key, current);

                ref.updateChildren(favorite_restaurant);
                ImageView start = itemView.findViewById(R.id.star_favorite);
                start.setImageResource(R.drawable.heart_fill);
                favorite_bool = true;

                Toast.makeText(context,"Added to favorite",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), TabApp.class);
        intent.putExtra("res_item", current);
        intent.putExtra("key", this.key);
        view.getContext().startActivity(intent);
    }
}
