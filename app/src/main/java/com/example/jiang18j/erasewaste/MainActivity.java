package com.example.jiang18j.erasewaste;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    Button captureBtn;
    Button shareBtn;
    Bitmap imageBitmap;
    byte[] imgByteArray;
//    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CAPTURE BUTTON
        captureBtn = (Button) findViewById(R.id.captureBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        shareBtn.setEnabled(false);
        LocationManager locationManager =
                (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LAUNCHES CAMERA
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //INSERTS IMG INTO DATABASE
                String tagText = ((EditText) findViewById(R.id.tagText)).getText().toString();
                String descriptText = ((EditText) findViewById(R.id.descriptionText)).getText().toString();
                String userText = ((EditText) findViewById(R.id.userText)).getText().toString();
//                Location location = locationManager.getLastKnownLocation(gpsLocationProvider);;
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                //convert bitmap into bitarray
                imgByteArray = getBytes(imageBitmap);

                //insert into database
                db.addEntryPosts( descriptText, "Chapel Hill", currentDateTimeString, userText );
                db.addEntryPhotos( imgByteArray);
                db.addEntryTags( tagText );

                //lauches into FeedActivity
                Intent myIntent = new Intent(MainActivity.this, FeedActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        if(!hasCamera()){
            captureBtn.setEnabled(false);
        }
    }

    //CHECKS IF HAS CAMERA
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //GET THUMBNAIL (return image to imageView)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            shareBtn.setEnabled(true);
        }
    }

    //BITMAP-->BYTE ARRAY
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //BYTE ARRAY-->BITMAP
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
