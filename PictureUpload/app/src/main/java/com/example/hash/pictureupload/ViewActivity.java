package com.example.hash.pictureupload;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.ImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Hash on 14/8/2016.
 */
public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //  Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        byte[] bytes = getIntent().getByteArrayExtra("BMP");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView imageview = (ImageView) findViewById(R.id.photo_view);
        imageview.setImageBitmap(bitmap);


    }


}

