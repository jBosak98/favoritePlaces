package com.jbosak.favoriteplaces;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jbosak.favoriteplaces.activities.BaseActivity;
import com.jbosak.favoriteplaces.activities.NoteActivity;
import com.jbosak.favoriteplaces.views.NavDrawer;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private OnCreateFavoriteListener mCallback;
    private LatLng placeLatLng;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        try {
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (activity.getIntent().hasExtra(NoteActivity.PLACE_LATITUDE)) {


                placeLatLng = new LatLng(
                        activity.getIntent().getDoubleExtra(NoteActivity.PLACE_LATITUDE, -34),
                        activity.getIntent().getDoubleExtra(NoteActivity.PLACE_LONGITUDE, 151));
            }
            mCallback = (OnCreateFavoriteListener) activity;


        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCreateFavoriteListener");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (placeLatLng != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 7f));
            mMap.addMarker(
                    new MarkerOptions()
                            .position(placeLatLng)
                            .title(getActivity()
                                    .getIntent()
                                    .getStringExtra(NoteActivity.PLACE_NAME)));
        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                mMap.addMarker(new MarkerOptions().position(latLng).title("new Place"));

                mCallback.onCreateFavorite(
                        new NavDrawer.ActivityNavDrawerItem("new Place", latLng, null, NoteActivity.class));
            }

        });
    }


    public interface OnCreateFavoriteListener {
        public void onCreateFavorite(NavDrawer.ActivityNavDrawerItem item);
    }
}
