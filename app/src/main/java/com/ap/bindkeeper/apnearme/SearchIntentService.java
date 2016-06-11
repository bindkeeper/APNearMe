package com.ap.bindkeeper.apnearme;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SearchIntentService extends IntentService {
    public SearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // do some long background work
        ArrayList<Place> places = new ArrayList<>();
        String searchCriteria = intent.getStringExtra("search");
        double lat = intent.getDoubleExtra("lat", -1);
        double lon = intent.getDoubleExtra("lon", -1);
        String strUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat+","+lon+"&radius="+500+"&keyword=" +
                searchCriteria +
                "&key="+ getResources().getString(R.string.google_places_key);


        try {
            URL url = new URL(strUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "", line;
                while ((line = reader.readLine()) != null) {
                    result += (line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject jsonPlace = resultArray.getJSONObject(i);
                        String name = jsonPlace.optString("name");
                        String address = jsonPlace.optString("vicinity");
                        places.add(new Place(name, address));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // intent with action = action is like the frequency of the broadcast
        Intent intent2 = new Intent("com.ap.bindkeeper.apnearme.SEARCH_RESULT");
        // put the number inside the intent
        intent2.putExtra("places", places);
        // send broadcast to the application
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
    }
}
