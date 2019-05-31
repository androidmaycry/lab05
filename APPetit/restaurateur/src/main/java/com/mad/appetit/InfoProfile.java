package com.mad.appetit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mad.mylibrary.Restaurateur;

import java.util.Objects;

import static com.mad.mylibrary.SharedClass.RESTAURATEUR_INFO;
import static com.mad.mylibrary.SharedClass.ROOT_UID;


public class InfoProfile extends Fragment {

    private String name, addr, descr, mail, phone, photoUri, time;
    private OnFragmentInteractionListener mListener;

    public InfoProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_profile_fragment, container, false);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(RESTAURATEUR_INFO + "/" + ROOT_UID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Restaurateur restaurateur = new Restaurateur();

                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        if(d.getKey().equals("info")){
                            restaurateur = d.getValue(Restaurateur.class);
                            break;
                        }
                    }

                    name = restaurateur.getName();
                    addr = restaurateur.getAddr();
                    descr = restaurateur.getCuisine();
                    mail = restaurateur.getMail();
                    phone = restaurateur.getPhone();
                    photoUri = restaurateur.getPhotoUri();
                    time = restaurateur.getOpeningTime();

                    //((TextView)view.findViewById(R.id.name)).setText(name);
                    ((TextView)view.findViewById(R.id.address)).setText(addr);
                    ((TextView)view.findViewById(R.id.description)).setText(descr);
                    ((TextView)view.findViewById(R.id.mail)).setText(mail);
                    ((TextView)view.findViewById(R.id.phone2)).setText(phone);
                    ((TextView)view.findViewById(R.id.time_text)).setText(time);

                    /*if(photoUri != null) {
                        Glide.with(Objects.requireNonNull(view.getContext()))
                                .load(photoUri)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .into((ImageView) view.findViewById(R.id.profile_image));
                    }
                    else {
                        Glide.with(Objects.requireNonNull(view.getContext()))
                                .load(R.drawable.restaurant_home)
                                .into((ImageView) view.findViewById(R.id.profile_image));
                    }*/
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("DAILY OFFER", "Failed to read value.", error.toException());
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
