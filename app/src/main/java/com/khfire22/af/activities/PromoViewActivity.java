package com.khfire22.af.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khfire22.af.R;
import com.squareup.picasso.Picasso;

public class PromoViewActivity extends AppCompatActivity {

    private static final String TAG = "PromoViewActivity";
    private String promoImageUrl;
    private String promoTitleString;
    private String promoDescriptionString;
    private String promoFooterString;

    private ImageView promoImage;
    private TextView promoTitle;
    private TextView promoDescription;
    private TextView promoFooter;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_view);

        // Instantiate all the views
        promoImage = (ImageView) findViewById(R.id.promo_image);
        promoTitle = (TextView) findViewById(R.id.promo_title);
        promoDescription = (TextView) findViewById(R.id.promo_description);
        promoFooter = (TextView) findViewById(R.id.promo_footer);

        // Get intent data from sending activity
        Intent intent = getIntent();
        promoImageUrl = intent.getStringExtra("promoImageUrl");
        promoTitleString = intent.getStringExtra("promoTitle");
        promoDescriptionString = intent.getStringExtra("promoDescription");
        promoFooterString = intent.getStringExtra("promoFooter");
        position = intent.getStringExtra("position");

        // Log to determine what is being passed in intent extra
        Log.d(TAG, "Extra = " + promoImageUrl + promoTitleString
                + promoDescription + promoFooterString + position);

        // Get and display the picture
        Picasso.with(PromoViewActivity.this)
                .load(promoImageUrl)
                .centerCrop()
                .resize(300, 300)
                .placeholder(R.drawable.fillerpicture)
                .into(promoImage);

        promoTitle.setText(promoTitleString);
        promoDescription.setText(promoDescriptionString);
        if (promoFooterString != null) {
            promoFooter.setText(Html.fromHtml(promoFooterString));
        }

        promoFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoViewActivity.this, WebViewActivity.class);
                intent.putExtra("position", "promo");
                startActivity(intent);
            }
        });

        // Instantiate the button and set the OCL to open the webView
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab_web_view);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoViewActivity.this, WebViewActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    public ImageView getPromoImage() {
        return promoImage;
    }
}
