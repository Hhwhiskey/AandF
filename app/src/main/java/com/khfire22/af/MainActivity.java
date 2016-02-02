package com.khfire22.af;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // URL to get promos
    private static String jsonURL;

    // JSON node TAGS to be used
    private static String TAG_PROMOTIONS = "promotions";
    private static String TAG_PROMO_TITLE = "title";
    private static String TAG_PROMO_IMAGE = "image";
    private static String TAG_VIEW_PROMO_IMAGE = "image";
    private static String TAG_VIEW_PROMO_TITLE = "title";
    private static String TAG_VIEW_DESCRIPTION = "description";
    private static String TAG_VIEW_FOOTER = "footer";
    private static String TAG_VIEW_BUTTON_TEXT = "title";
    private static String TAG_VIEW_BUTTON_TARGET = "target";
    private static final String TAG = "MainActivity";
    private static ImageView imageView1;
    private static ImageView imageView2;
    private static TextView title1;
    private static TextView title2;
    private TableLayout layout1;
    private TableLayout layout2;

//    ProgressDialog dialog;
    private ArrayList<Promo> list;
    private String clickedTitle;
    private String clickedDescription;
    private String clickedImageUrl;
    private String clickedFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = (ImageView) findViewById(R.id.title_image_1);
        imageView2 = (ImageView) findViewById(R.id.title_image_2);
        title1 = (TextView) findViewById(R.id.title_1);
        title2 = (TextView) findViewById(R.id.title_2);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPromoIntent(0);
            }
        });

        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPromoIntent(0);
            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPromoIntent(1);
            }
        });


        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPromoIntent(1);

            }
        });

        GetJsonPromo getJsonPromo = new GetJsonPromo();
        getJsonPromo.execute();

    }



    public void viewPromoIntent(int i) {

        String clickedTitle = list.get(i).title;
        String clickedImageUrl = list.get(i).image;
        String clickedDescription = list.get(i).description;
        String clickedFooter = list.get(i).footer;
        String position = String.valueOf(i);

        final Intent promoIntent = new Intent(MainActivity.this, PromoView.class);
        promoIntent.putExtra("promoTitle", clickedTitle);
        promoIntent.putExtra("promoDescription", clickedDescription);
        promoIntent.putExtra("promoImageUrl", clickedImageUrl);
        promoIntent.putExtra("promoFooter", clickedFooter);
        promoIntent.putExtra("position", position);

        startActivity(promoIntent);
    }

    public class GetJsonPromo extends AsyncTask<Void, Void, String> {

        private String title;
        private String imageUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show progress dialog
//            dialog = new ProgressDialog(MainActivity.this);
//            dialog.setMessage("Please wait...");
//            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // Point to the promo URL
            jsonURL =  "http://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json";


            // Connect to the A&F Promo URL
            try {
                URL jsonUrl = new URL("http://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json");
                HttpURLConnection conn = (HttpURLConnection) jsonUrl.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                // Initialize the connection to the created stream
                InputStream stream = conn.getInputStream();

                // Method to get the string from the input stream
                String response = getStringFromInputStream(stream);
                stream.close();

                //parseResponse(response);
                list = parseResponse(response);

                Log.d("JSON", "response = " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(title);
//            dialog.dismiss();

        }
    }



    private ArrayList<Promo> parseResponse(String response) {

        list = new ArrayList<>();

        // Convert String to json object
        try {
            JSONObject mainObject = new JSONObject(response);

            JSONArray jsonArray = mainObject.getJSONArray("promotions");


            for (int i = 0; i < jsonArray.length(); i++) {
                final Promo promo = new Promo();
                promo.description = jsonArray.getJSONObject(i).optString("description");
                promo.title = jsonArray.getJSONObject(i).optString("title");
                promo.image = jsonArray.getJSONObject(i).optString("image");
                promo.footer = jsonArray.getJSONObject(i).optString("footer");



                final int finalI = i;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (finalI == 0) {
                            title1.setText(promo.title);
                            new ImageLoadTask(promo.image, imageView1).execute();


                        } else {
                            title2.setText(promo.title);
                            new ImageLoadTask(promo.image, imageView2).execute();

                        }
                    }
                });
//                  TODO buttons
//                promo1.buttonTarget = jsonArray.getJSONObject(i).optJSONObject("button").optString("target");
//                promo1.buttonTitle = jsonArray.getJSONObject(i).optJSONObject("button").optString("title");



                list.add(promo);
                Log.d("JSON", "list = " + list.size());
            }
        } catch (JSONException e) {
            Log.d("JSON", "Error =  "+ e.toString());
            e.printStackTrace();
        }

        return list;
    }

    public void getPromoDataOnClick() {

    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        //can use JSON as follows: (also in Parse Response method)
        /*try {
            JSONObject content = new JSONObject(br.readLine());
			//define keys that you need values for
            String promotion = content.getString("promotion");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
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
            imageView.setImageBitmap(result);
        }

    }
}
