package com.khfire22.af;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PromoView extends AppCompatActivity {

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

        promoImage = (ImageView) findViewById(R.id.promo_image);
        promoTitle = (TextView) findViewById(R.id.promo_title);
        promoDescription = (TextView) findViewById(R.id.promo_description);
        promoFooter = (TextView) findViewById(R.id.promo_footer);

        Intent intent = getIntent();
        promoImageUrl = intent.getStringExtra("promoImageUrl");
        promoTitleString = intent.getStringExtra("promoTitle");
        promoDescriptionString = intent.getStringExtra("promoDescription");
        promoFooterString = intent.getStringExtra("promoFooter");
        position = intent.getStringExtra("position");

        new ImageLoadTask(promoImageUrl, promoImage).execute();
        promoTitle.setText(promoTitleString);
        promoDescription.setText(promoDescriptionString);
        if (promoFooterString != null) {
            promoFooter.setText(promoFooterString);
        }


//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.view_user_collapsing_tool_bar);
//        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
//
//        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab_promo);
//        myFab.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//
//            }
//        });
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) myFab.getLayoutParams();
//            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
//            myFab.setLayoutParams(p);
//        }

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab_web_view);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoView.this, WebViewActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });


    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            promoImage.setImageBitmap(result);
        }

    }
}
