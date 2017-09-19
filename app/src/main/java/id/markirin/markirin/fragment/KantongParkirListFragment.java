package id.markirin.markirin.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import id.markirin.markirin.R;
import id.markirin.markirin.activity.SlotListActivity;
import id.markirin.markirin.adapter.KantongParkirAdapter;
import id.markirin.markirin.model.KantongParkir;
import id.markirin.markirin.model.ParkirSlot;

/**
 * A simple {@link Fragment} subclass.
 */
public class KantongParkirListFragment extends Fragment {

    private static final String TAG = KantongParkirListFragment.class.getName();
    private static final int RC_PERMISSION_GPS_REQUEST = 89342;
    private ArrayList<KantongParkir> dataModels;
    private ListView listView;
    private KantongParkirAdapter adapter;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth auth;
    private Context mContext;
    private LatLng latLngGps;

    public KantongParkirListFragment() {
        // Required empty public constructor
    }

    public static KantongParkirListFragment newInstance() {
        return new KantongParkirListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_kantong_parkir_list, container, false);

        mContext = getActivity();
        latLngGps = new LatLng(-6.873199, 107.586869); // BDV latlng
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference("kantongs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + s);
                KantongParkir kantong = dataSnapshot.getValue(KantongParkir.class);
                dataModels.add(kantong);
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
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

        listView = (ListView) rootView.findViewById(R.id.list_view);
        dataModels = new ArrayList<>();
        adapter = new KantongParkirAdapter(dataModels, getActivity(), latLngGps);

        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.emptyElement));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String uidParkiran = dataModels.get(i).getUid();
                Intent intent = new Intent(mContext, SlotListActivity.class);
                intent.putExtra("uidParkiran", uidParkiran);
                startActivity(intent);
            }
        });
        checkLocation();

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void checkLocation() {
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PERMISSION_GPS_REQUEST);
            return;
        } else {
            // Call if location enabled
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                latLngGps = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RC_PERMISSION_GPS_REQUEST) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocation();
            }
        }
    }
}
