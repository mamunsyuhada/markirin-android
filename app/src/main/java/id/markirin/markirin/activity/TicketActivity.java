package id.markirin.markirin.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import id.markirin.markirin.R;

public class TicketActivity extends AppCompatActivity {

    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        bookingId = getIntent().getStringExtra("bookingId");
        Bitmap myBitmap = QRCode.from(bookingId).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.image_qr);
        myImage.setImageBitmap(myBitmap);
        TextView textKode = (TextView) findViewById(R.id.text_kode);
        textKode.setText("Kode Booking Anda: " + bookingId);
    }
}
