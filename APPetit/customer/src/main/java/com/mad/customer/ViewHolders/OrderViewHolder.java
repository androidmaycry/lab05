package com.mad.customer.ViewHolders;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mad.customer.R;
import com.mad.mylibrary.OrderItem;

import java.util.Calendar;
import java.util.Date;

import static com.mad.mylibrary.SharedClass.RESTAURATEUR_INFO;
import static com.mad.mylibrary.SharedClass.STATUS_DELIVERED;
import static com.mad.mylibrary.SharedClass.STATUS_DELIVERING;
import static com.mad.mylibrary.SharedClass.STATUS_DISCARDED;
import static com.mad.mylibrary.SharedClass.STATUS_UNKNOWN;

public class OrderViewHolder extends RecyclerView.ViewHolder{
    private TextView name, date, delivery, total;
    private ImageView img;
    View view;

    public OrderViewHolder(View itemView){
        super(itemView);
        view = itemView;
        name = itemView.findViewById(R.id.order_res_name);
        date = itemView.findViewById(R.id.order_date);
        delivery = itemView.findViewById(R.id.order_status);
        total = itemView.findViewById(R.id.order_tot);
        img = itemView.findViewById(R.id.order_image);
    }

    public void setData(OrderItem current, int position){
        //Set time in correct format
        Date d = new Date(current.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        String date = day+"/"+month+"/"+year;
        this.date.setText(date);
        //Set delivery
        switch (current.getStatus()){
            case STATUS_UNKNOWN:
                delivery.setText("Order sent");
                break;
            case STATUS_DELIVERED:
                delivery.setText("Order delivered");
                delivery.setTextColor(Color.parseColor("#59cc33"));
                break;
            case STATUS_DISCARDED:
                delivery.setText("Order refused");
                delivery.setTextColor(Color.parseColor("#cc3333"));
                break;
            case STATUS_DELIVERING:
                delivery.setText("Delivering...");
                delivery.setTextColor(Color.parseColor("#ffb847"));
                break;
        }

        total.setText(current.getTotPrice()+" €");
        Query query = FirebaseDatabase.getInstance().getReference(RESTAURATEUR_INFO).child(current.getKey()).child("info");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText((String)dataSnapshot.child("name").getValue());
                if(dataSnapshot.child("img").exists()){
                    Glide.with(itemView).load(dataSnapshot.child("img").getValue()).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public View getView (){
        return view;
    }
}

