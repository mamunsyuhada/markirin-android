package id.markirin.markirin.adapter;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;

import id.markirin.markirin.R;
import id.markirin.markirin.model.KantongParkir;

/**
 * Created by faldyikhwanfadila on 7/13/17.
 */

public class KantongParkirAdapter extends ArrayAdapter<KantongParkir> {

    private ArrayList<KantongParkir> dataSet;
    Context mContext;
    LatLng latLng;

    // View lookup cache
    private static class ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textRange;
        TextView textAvailability;
    }

    public KantongParkirAdapter(ArrayList<KantongParkir> data, Context context, LatLng latLng) {
        super(context, R.layout.list_row_kantong_parkir, data);
        this.dataSet = data;
        this.mContext = context;
        this.latLng = latLng;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        KantongParkir kantongParkir = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

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

        viewHolder.textName.setText(kantongParkir.getNama());
        viewHolder.textDescription.setText(kantongParkir.getDeskripsi());
        viewHolder.textRange.setText(distanceBetween(latLng, new LatLng(kantongParkir.getLatitude(), kantongParkir.getLongitude()))/1000 + "km");
        viewHolder.textAvailability.setText(kantongParkir.getSisa() + " Slot");
        if(kantongParkir.getKapasitas() == 0) {
            viewHolder.textAvailability.setText("Coming Soon");
            viewHolder.textAvailability.setBackgroundResource(android.R.color.darker_gray);
        } else if(kantongParkir.getSisa() > 3) {
            viewHolder.textAvailability.setBackgroundResource(android.R.color.holo_green_light);
        } else if(kantongParkir.getSisa() > 0) {
            viewHolder.textAvailability.setBackgroundResource(android.R.color.holo_orange_light);
        } else {
            viewHolder.textAvailability.setText("Penuh");
            viewHolder.textAvailability.setBackgroundResource(android.R.color.darker_gray);
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
