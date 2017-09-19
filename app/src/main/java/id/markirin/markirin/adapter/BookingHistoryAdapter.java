package id.markirin.markirin.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.markirin.markirin.R;
import id.markirin.markirin.activity.MainActivity;
import id.markirin.markirin.model.BookingHistory;
import id.markirin.markirin.model.KantongParkir;
import id.markirin.markirin.model.User;

/**
 * Created by faldyikhwanfadila on 7/13/17.
 */

public class BookingHistoryAdapter extends ArrayAdapter<BookingHistory> {

    private ArrayList<BookingHistory> dataSet;
    Context mContext;
    LatLng latLng;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth auth;
    private FirebaseUser user;

    // View lookup cache
    private static class ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textRange;
        TextView textAvailability;
    }

    public BookingHistoryAdapter(ArrayList<BookingHistory> data, Context context, LatLng latLng) {
        super(context, R.layout.list_row_booking_history, data);
        this.dataSet = data;
        this.mContext = context;
        this.latLng = latLng;
        this.mDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BookingHistory bookingHistory = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row_kantong_parkir, parent, false);
            viewHolder.textName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.textDescription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.textRange = (TextView) convertView.findViewById(R.id.range);
            viewHolder.textAvailability = (TextView) convertView.findViewById(R.id.text_availability);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(bookingHistory.getKantongParkir() != null) {
            DatabaseReference databaseUser = mDatabase.getReference("kantongs").child(bookingHistory.getKantongParkir());
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    KantongParkir kantongParkir = dataSnapshot.getValue(KantongParkir.class);

                    viewHolder.textName.setText(kantongParkir.getNama());
                    viewHolder.textDescription.setText(kantongParkir.getDeskripsi());
                    viewHolder.textRange.setText(distanceBetween(latLng, new LatLng(kantongParkir.getLatitude(), kantongParkir.getLongitude()))/1000 + "km");
                    viewHolder.textAvailability.setText(kantongParkir.getSisa() + " Slot");
                    if(bookingHistory.getWaktuSelesai() == null && bookingHistory.getWaktuMasuk() == null) {
                        viewHolder.textAvailability.setText("Active");
                        viewHolder.textAvailability.setBackgroundResource(android.R.color.holo_green_light);
                    } else if(bookingHistory.getWaktuSelesai() == null && bookingHistory.getWaktuMasuk() != null) {
                        viewHolder.textAvailability.setText("Active");
                        viewHolder.textAvailability.setBackgroundResource(android.R.color.holo_green_light);
                    } else if(bookingHistory.getWaktuSelesai() != null && bookingHistory.getWaktuMasuk() != null) {
                        viewHolder.textAvailability.setText("Done");
                        viewHolder.textAvailability.setBackgroundResource(android.R.color.darker_gray);
                    } else {
                        viewHolder.textAvailability.setText("Done");
                        viewHolder.textAvailability.setBackgroundResource(android.R.color.darker_gray);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private Float distanceBetween(LatLng latLng1, LatLng latLng2) {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);


        return loc1.distanceTo(loc2);
    }
}
