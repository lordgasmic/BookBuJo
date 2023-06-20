package com.lordgasmic.trapperkeeper;

import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGif();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadGif();
    }

    private void loadGif( ){
        setContentView(R.layout.activity_spinner);
        ImageView vw = findViewById(R.id.imgview);

        try {
            ImageDecoder.Source source = ImageDecoder.createSource(getResources(), R.drawable.spinner);
            Drawable drawable = ImageDecoder.decodeDrawable(source);
            vw.setImageDrawable(drawable);

            if (drawable instanceof AnimatedImageDrawable) {
                ((AnimatedImageDrawable) drawable).start();
            }
        }
        catch( IOException e) {
            e.printStackTrace();
        }
    }
}
