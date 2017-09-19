package id.markirin.markirin.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import id.markirin.markirin.R;
import id.markirin.markirin.activity.SlotListActivity;
import id.markirin.markirin.model.KantongParkir;

/**
 * A simple {@link Fragment} subclass.
 */
public class KantongParkirMapsFragment extends Fragment {

    private static final int RC_PERMISSION_GPS_REQUEST = 32132;
    private static final String TAG = KantongParkirMapsFragment.class.getName();
    MapView mMapView;
    private GoogleMap googleMap;
    private Context mContext;
    private ArrayList<KantongParkir> dataModels;
    private ArrayList<Marker> markers;
    private FirebaseDatabase mDatabase;

    public KantongParkirMapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_kantong_parkir_maps, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        dataModels = new ArrayList<>();
        markers = new ArrayList<>();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                requestPermission();

                // For dropping a marker at a point on the Map
                LatLng bdv = new LatLng(-6.873199, 107.586869);
//                googleMap.addMarker(new MarkerOptions().position(bdv).title("Bandung Digital Valley").snippet("Kantong parkir BDV"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(bdv).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Initialize database
                mDatabase.getReference("kantongs").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded: " + s);
                        KantongParkir kantong = dataSnapshot.getValue(KantongParkir.class);
                        // Add kantong to array
                        dataModels.add(kantong);
                        // New markeroptions
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(kantong.getLatitude(), kantong.getLongitude()))
                                .title(kantong.getNama())
                                .snippet(kantong.getDeskripsi());
                        Marker marker = googleMap.addMarker(markerOptions);
                        // Add marker to array
                        markers.add(marker);
                        Log.d(TAG, "onChildAdded: " + kantong.getNama());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildChanged: " + s);
                        int num = 0;
                        if(s != null) {
                            num = Integer.parseInt(s);
                        }
                        KantongParkir kantong = dataSnapshot.getValue(KantongParkir.class);
                        dataModels.set(num, kantong);
                        Marker marker = markers.get(num);
                        marker.setPosition(new LatLng(kantong.getLatitude(), kantong.getLongitude()));
                        marker.setTitle(kantong.getNama());
                        marker.setSnippet(kantong.getDeskripsi());
                        Log.d(TAG, "onChildChanged: " + kantong.getNama());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        int i = markers.indexOf(marker);
                        KantongParkir kantongParkir = dataModels.get(i);
                        Intent intent = new Intent(mContext, SlotListActivity.class);
                        intent.putExtra("uidParkiran", kantongParkir.getUid());
                        startActivity(intent);
                    }
                });
            }
        });

        return rootView;
    }

    public static KantongParkirMapsFragment newInstance() {
        return new KantongParkirMapsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void requestPermission() {
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION_GPS_REQUEST);
            return;
        } else {
            // Call if location enabled
            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RC_PERMISSION_GPS_REQUEST) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            }
        }
    }
}
