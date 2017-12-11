package com.example.jiang18j.erasewaste;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getPost();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(35.909121, -79.048563);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Chapel Hill"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    //BYTE ARRAY-->BITMAP
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void getPost() {
        String id = getIntent().getStringExtra("ID");
        System.out.println(id);
        Cursor c = db.getSinglePost(id);
        c.moveToFirst();

        System.out.println(c.getCount());

        if (c.getCount() != 0) {
            System.out.println("good");
            for (int i = 0; i < c.getCount(); i++) {
                for (int j = 0; j < c.getColumnCount(); j++) {
                    switch (c.getColumnName(j)) {
                        case "image": //get image
                            byte[] a = c.getBlob(j);
                            ImageView image =  findViewById(R.id.image);
                            image.setImageBitmap(getImage(a));
//                            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(500, 500));
                            //Adds the view to the layout
                            image.setImageBitmap(BitmapFactory.decodeByteArray(a, 0, a.length));
                            break;
                        case "description":
                            TextView description = findViewById(R.id.description);
                            description.setText(c.getString(j));
                            break;
                        case "location":
                            TextView location = findViewById(R.id.location);
                            location.setText(c.getString(j));
                            break;
                        case "time":
                            TextView time = findViewById(R.id.time);
                            time.setText(c.getString(j));
                            break;
                        case "username":
                            TextView username = findViewById(R.id.user);
                            username.setText("by " + c.getString(j));
                            break;
                        case "tag":
                            TextView tag = findViewById(R.id.tags);
                            tag.setText("#" + c.getString(j));
                            break;
                    }
                }
                c.moveToNext();
            }
        }

    }
}
