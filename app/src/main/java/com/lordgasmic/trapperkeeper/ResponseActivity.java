package com.lordgasmic.trapperkeeper;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;

public class ResponseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String title = extras.getString("title");
            if (title != null) {
                TextView textView = findViewById(R.id.textTitle);
                textView.setText(title);
            }

            String imageUrl = extras.getString("imageUrl");
            if (imageUrl != null) {
                imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/') + 1);
                imageUrl = imageUrl + 600;
                NetworkImageView imageView = findViewById(R.id.cover);
                imageView.setImageUrl(imageUrl, MyVolley.getInstance(this).getImageLoader());
            }

            String issue = extras.getString("issue");
            System.out.println("issue");
            System.out.println(issue);
            if (issue != null) {
                TextView issueView = findViewById(R.id.txtIssue);
                issueView.setText(issue);
            }

            String volume = extras.getString("volume");
            if (volume != null) {
                TextView volumeView = findViewById(R.id.txtVolume);
                volumeView.setText(volume);
            }

            boolean variant = extras.getBoolean("variant");
            CheckBox variantCb = findViewById(R.id.cbVariant);
            variantCb.setChecked(variant);
        }
    }
}
