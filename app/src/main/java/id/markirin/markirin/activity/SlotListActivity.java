package id.markirin.markirin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.markirin.markirin.R;
import id.markirin.markirin.adapter.SlotAdapter;
import id.markirin.markirin.model.BookingHistory;
import id.markirin.markirin.model.ParkirSlot;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class SlotListActivity extends AppCompatActivity {


    ArrayList<ParkirSlot> dataModels;
    ListView listView;
    private static SlotAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String myBooking;
    private String uidParkiran;
    private Context mContext;
    private String licensePlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_list);

        mContext = this;
        licensePlate = "";
        uidParkiran = getIntent().getStringExtra("uidParkiran");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        database.getReference("users").child(auth.getCurrentUser().getUid()).child("booking").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myBooking = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView = (ListView) findViewById(R.id.list_view);
        dataModels = new ArrayList<>();

        mDatabase.child("slots").child(uidParkiran).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + s);
                ParkirSlot slot = dataSnapshot.getValue(ParkirSlot.class);
                dataModels.add(slot);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onChildAdded: " + slot.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + s);
                int num = 0;
                if(s != null) {
                    num = Integer.parseInt(s);
                }
                ParkirSlot slot = dataSnapshot.getValue(ParkirSlot.class);
                dataModels.set(num, slot);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onChildChanged: " + slot.getName());
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

        adapter = new SlotAdapter(dataModels, this);

        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(myBooking == null) {
                    if(dataModels.get(i).getReservedBy().equals("default")) {
                        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                                .title("Confirmation")
                                .content("Are you sure want to book?")
                                .positiveText("Yes")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    }
                                })
                                .negativeText("No")
                                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                                .input("License Plate Number", null, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        licensePlate += input;
                                        Log.d(TAG, "onClick: " + licensePlate);

                                        String key = mDatabase.child("booking-histories").push().getKey();
                                        BookingHistory bookingHistory = new BookingHistory(uidParkiran, "1", licensePlate, new Date(), null, null, user.getUid(), (double) 0, "ParkirPay");
                                        Map<String, Object> postValues = bookingHistory.toMap();

                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/booking-histories/" + key, postValues);
                                        childUpdates.put("/user-booking-histories/" + user.getUid() + "/" + key, postValues);

                                        mDatabase.updateChildren(childUpdates);

                                        mDatabase.child("slots").child(uidParkiran).child(String.valueOf(i+1)).child("reservedBy").setValue(auth.getCurrentUser().getUid());
                                        mDatabase.child("slots").child(uidParkiran).child(String.valueOf(i+1)).child("countdown").setValue(120);
                                        mDatabase.child("users").child(auth.getCurrentUser().getUid()).child("bookingSlotId").setValue(String.valueOf(i+1));
                                        mDatabase.child("users").child(auth.getCurrentUser().getUid()).child("bookingParkiranId").setValue(uidParkiran);
                                        mDatabase.child("users").child(auth.getCurrentUser().getUid()).child("bookingKey").setValue(key);

                                        Intent intent = new Intent(mContext, TicketActivity.class);
                                        intent.putExtra("bookingId", key);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();

//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                        builder.setMessage("Apakah anda yakin?")
//                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int a) {
//                                    }
//                                })
//                                .setNegativeButton("Tidak", null)
//                                .show();
                    } else {
                        Toast.makeText(mContext, "Tempat ini sudah dibooking!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Anda memiliki booking aktif!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
