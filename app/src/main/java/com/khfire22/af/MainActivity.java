package com.khfire22.af;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khfire22.af.activities.PromoViewActivity;
import com.khfire22.af.model.PromoItem;
import com.khfire22.af.utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

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

    private static final String TAG = "MainActivity";

    // JSON node TAGS to be used
    private static String TAG_PROMOTIONS = "promotions";
    private static String TAG_PROMO_TITLE = "title";
    private static String TAG_PROMO_IMAGE = "image";
    private static String TAG_PROMO_FOOTER = "footer";
    private static String TAG_PROMO_DESCRIPTION = "description";

    private static ImageView imageView1;
    private static ImageView imageView2;
    private static TextView title1;
    private static TextView title2;
    private ConnectionDetector detector;



    //    ProgressDialog dialog;
    private ArrayList<PromoItem> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detector = new ConnectionDetector(this);

        // Instantiate all views
        // Show fallback pic if no network is detected
        imageView1 = (ImageView) findViewById(R.id.title_image_1);
        Picasso.with(MainActivity.this)
                .load(R.drawable.fillerpicture)
                .centerCrop()
                .resize(300, 300)
                .into(imageView1);

        imageView2 = (ImageView) findViewById(R.id.title_image_2);
        Picasso.with(MainActivity.this)
                .load(R.drawable.fillerpicture)
                .centerCrop()
                .resize(300, 300)
                .into(imageView2);

        title1 = (TextView) findViewById(R.id.title_1);
        title2 = (TextView) findViewById(R.id.title_2);


        /*
         * OCL for all views
         * If no connection is detected, prevent clicks on any views and give toast to user
         */

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detector.isConnectedToInternet()) {
                    Toast.makeText(MainActivity.this, R.string.please_check_network_settings, Toast.LENGTH_LONG).show();
                } else {
                    viewPromoIntent(0);
                }
            }
        });

        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detector.isConnectedToInternet()) {
                    Toast.makeText(MainActivity.this, R.string.please_check_network_settings, Toast.LENGTH_LONG).show();
                } else {
                    viewPromoIntent(0);
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detector.isConnectedToInternet()) {
                    Toast.makeText(MainActivity.this, R.string.please_check_network_settings, Toast.LENGTH_LONG).show();
                } else {
                    viewPromoIntent(1);
                }
            }
        });

        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detector.isConnectedToInternet()) {
                    Toast.makeText(MainActivity.this, R.string.please_check_network_settings, Toast.LENGTH_LONG).show();
                } else {
                    viewPromoIntent(1);
                }
            }
        });


        // Execute task that will point to JSON URL
        GetJsonPromo getJsonPromo = new GetJsonPromo();
        getJsonPromo.execute();

    }

    // Intent extra for all relevant data
    public void viewPromoIntent(int i) {

        String clickedTitle = list.get(i).title;
        String clickedImageUrl = list.get(i).image;
        String clickedDescription = list.get(i).description;
        String clickedFooter = list.get(i).footer;
        String position = String.valueOf(i);

        final Intent promoIntent = new Intent(MainActivity.this, PromoViewActivity.class);
        promoIntent.putExtra("promoTitle", clickedTitle);
        promoIntent.putExtra("promoDescription", clickedDescription);
        promoIntent.putExtra("promoImageUrl", clickedImageUrl);
        promoIntent.putExtra("promoFooter", clickedFooter);
        promoIntent.putExtra("position", position);

        startActivity(promoIntent);
    }

    // Async to point to the promo URL
    public class GetJsonPromo extends AsyncTask<Void, Void, String> {

        // Point to the URL in the background
        @Override
        protected String doInBackground(Void... params) {

            // Connect to the A&F promo URL
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
            super.onPostExecute(result);
        }
    }

    // Parse the String from the input steam
    private ArrayList<PromoItem> parseResponse(String response) {

        list = new ArrayList<>();

        // Convert String to json object
        try {
            JSONObject mainObject = new JSONObject(response);

            JSONArray jsonArray = mainObject.getJSONArray(TAG_PROMOTIONS);

            // Iterate through the jsonArray and create promo objects with the data
            for (int i = 0; i < jsonArray.length(); i++) {
                final PromoItem promoItem = new PromoItem();
                promoItem.description = jsonArray.getJSONObject(i).optString(TAG_PROMO_DESCRIPTION);
                promoItem.title = jsonArray.getJSONObject(i).optString(TAG_PROMO_TITLE);
                promoItem.image = jsonArray.getJSONObject(i).optString(TAG_PROMO_IMAGE);
                promoItem.footer = jsonArray.getJSONObject(i).optString(TAG_PROMO_FOOTER);

                // Update the imageViews and textViews on the UI thread
                final int finalI = i;

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (finalI == 0) {

                            title1.setText(promoItem.title);

                            Picasso.with(MainActivity.this)
                                    .load(promoItem.image)
                                    .centerCrop()
                                    .resize(300, 300)
                                    .placeholder(R.drawable.fillerpicture)
                                    .into(imageView1);
                        } else {

                            title2.setText(promoItem.title);

                            Picasso.with(MainActivity.this)
                                    .load(promoItem.image)
                                    .centerCrop()
                                    .resize(300, 300)
                                    .placeholder(R.drawable.fillerpicture)
                                    .into(imageView2);
                        }
                    }
                });

                list.add(promoItem);
                Log.d("JSON", "list = " + list.size());
            }
        } catch (JSONException e) {
            Log.d("JSON", "Error =  " + e.toString());
            e.printStackTrace();
        }

        return list;
    }

    // Convert the the InputStream to a String
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

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

    @Override
    protected void onResume() {
        super.onResume();
        // Execute task that will point to JSON URL
        GetJsonPromo getJsonPromo = new GetJsonPromo();
        getJsonPromo.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
