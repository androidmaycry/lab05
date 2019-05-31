package com.mad.customer.UI;

import static com.mad.mylibrary.SharedClass.CUSTOMER_PATH;
import static com.mad.mylibrary.SharedClass.RESTAURATEUR_INFO;
import static com.mad.mylibrary.SharedClass.STATUS_DISCARDED;
import static com.mad.mylibrary.SharedClass.orderToTrack;
import static com.mad.mylibrary.SharedClass.orderToTrack;
import static com.mad.mylibrary.SharedClass.user;
import static com.mad.mylibrary.SharedClass.ROOT_UID;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mad.customer.R;
import com.mad.customer.UI.Order;
import com.mad.customer.UI.Profile;
import com.mad.customer.UI.Restaurant;
import com.mad.mylibrary.OrderItem;
import com.mad.mylibrary.User;

import java.util.HashMap;



public class NavApp extends AppCompatActivity implements
        Restaurant.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener,
        Order.OnFragmentInteractionListener{

    public static final String PREFERENCE_NAME = "ORDER_LIST";

    private SharedPreferences order_to_listen;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item ->  {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                //onRefuseOrder();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Restaurant()).commit();
                return true;
            case R.id.navigation_profile:
                //onRefuseOrder();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                return true;
            case R.id.navigation_reservation:
                //onRefuseOrder();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Order()).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nav_app);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Restaurant()).commit();
        }
        //Get the hashMap from sharedPreferences
        order_to_listen = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String storedHashMapString = order_to_listen.getString("HashMap",null);
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        orderToTrack = gson.fromJson(storedHashMapString, type);


        getUserInfo();

        Log.d("ROOT_UID", ROOT_UID);
    }

    public void getUserInfo() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(CUSTOMER_PATH).child(ROOT_UID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("customer_info").getValue(User.class);
                Log.d("user", ""+user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MAIN", "Failed to read value.", error.toException());
            }
        });

    }

    private void onRefuseOrder (){

            Query query = FirebaseDatabase.getInstance().getReference(CUSTOMER_PATH).child(ROOT_UID).child("orders");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Toast.makeText(getApplicationContext(), "ciao", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    AlertDialog alertDialog = new AlertDialog.Builder(NavApp.this).create();
                    alertDialog.setTitle("Alert");
                    OrderItem oi= dataSnapshot.getValue(OrderItem.class);
                    alertDialog.setMessage(oi.getKey()+"ciao");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        //}

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefuseOrder();
    }

    @Override
    protected void onStop() {
        Gson gson = new Gson();
        order_to_listen = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String mapString = gson.toJson(orderToTrack);
        order_to_listen.edit().putString("HashMap", mapString).apply();
        super.onStop();

    }


}

