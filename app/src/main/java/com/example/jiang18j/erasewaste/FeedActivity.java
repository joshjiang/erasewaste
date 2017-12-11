package com.example.jiang18j.erasewaste;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FeedActivity extends AppCompatActivity {
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent intent = getIntent();

        //add image button to another activity
        ImageButton btnCamera = (ImageButton)findViewById(R.id.capture);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FeedActivity.this, MainActivity.class);
                 FeedActivity.this.startActivity(myIntent);
            }
        });

        LinearLayout sv = (LinearLayout) findViewById(R.id.postsLayout);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FeedActivity.this, MapsActivity.class);
                FeedActivity.this.startActivity(myIntent);
            }
        });
        getPostsFeed(10);
    }

    //BYTE ARRAY-->BITMAP
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void getPostsFeed(Integer limit){
        LinearLayout layout = (LinearLayout) findViewById(R.id.postsLayout);
        layout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);

        Cursor c = db.getPostsPhotosTags();
        c.moveToFirst();

        if(c.getCount() != 0) {
            for (int i = 0; i < c.getCount(); i++) {
                LinearLayout rowLO = new LinearLayout(this);
                rowLO.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout ws = new LinearLayout(this);
                ws.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams paramsWS = layout.getLayoutParams();
                paramsWS.height = 100;
                ws.setLayoutParams(paramsWS);

                LinearLayout imageLO = new LinearLayout(this);
                imageLO.setOrientation(LinearLayout.VERTICAL);

                LinearLayout contentLO = new LinearLayout(this);
                contentLO.setOrientation(LinearLayout.VERTICAL);

                for (int j = 0; j < c.getColumnCount(); j++) {
                    LinearLayout row = new LinearLayout(this);
                    //row.setOrientation(LinearLayout.VERTICAL);
                    switch(c.getColumnName(j))
                    {
                        case "image": //get image
                            System.out.println(" the column is : " + c.getColumnName(j) + " j is :" + j);
                            byte[] a = c.getBlob(j);
                            ImageView image = new ImageView(this);
                            image.setImageBitmap(getImage(a));
                            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(300,300));
                            //Adds the view to the layout
                            imageLO.addView(image);
                            image.setImageBitmap(BitmapFactory.decodeByteArray(a, 0, a.length));
                            break;
                        case "description" :
                            TextView description = new TextView(this);
                            description.setTypeface(null, Typeface.BOLD);
                            description.setText("   " + c.getString(j).toUpperCase());
                            row.addView(description);
                            break;
                        case "location" :
                            TextView location = new TextView(this);
                            location.setText("   " + c.getString(j));
                            row.addView(location);
                            break;
                        case "time" :
                            TextView time = new TextView(this);
                            time.setTypeface(null, Typeface.ITALIC);
                            time.setText("   " + c.getString(j));
                            row.addView(time);
                            break;
                        case "username" :
                            TextView username = new TextView(this);
                            username.setText("   by "+c.getString(j));
                            row.addView(username);
                            break;
                        case "tag" :
                            TextView tag = new TextView(this);
                            tag.setText("   #"+c.getString(j));
                            row.addView(tag);
                            break;
                        case "id" :
                            final Integer id = c.getInt(j);
                            System.out.println("successfully got id of :"+ id);
                            rowLO.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    System.out.println("nice click");
                                    Intent singleView = new Intent(FeedActivity.this, MapsActivity.class);
                                    singleView.putExtra("ID", id.toString());
                                    FeedActivity.this.startActivity(singleView);
                                }
                            });
                            break;
                    }
                    contentLO.addView(row);
                }
                rowLO.addView(imageLO);
                rowLO.addView(contentLO);
                layout.addView(rowLO);
                layout.addView(ws);
                c.moveToNext();
            }
        }
    }

}