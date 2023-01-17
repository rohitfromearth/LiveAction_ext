package com.example.liveaction_ext;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Pack_api {
    ArrayList<String> appl = new ArrayList<String>();
    public ArrayList pack_rule(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String[] appslist = new String []{};
        try {
            String response = "";
            URL url = new URL("https://3685-182-70-71-190.in.ngrok.io/apps/list");
            HttpURLConnection urlConn = null;

            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConn.setRequestProperty("Accept", "application/json");
            Log.e("datastream","nice3");
            urlConn.setDoInput(true);
            int responseCode = urlConn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String applist;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                while ((applist= br.readLine()) != null) {
                    response += applist;
                    final JSONObject app = new JSONObject(response);
//                     appslist


                    JSONArray appsList = app.getJSONArray("data");
                    for(int i=0;i< appsList.length();i++) {
//                         String fg = app.get("data").getString(i);
                        Log.e("datastream",appsList.getString(i));
//                         appslist[i]= appsList.getString(i);
                        appl.add(appsList.getString(i));

                        Log.e("datastream", String.valueOf(appl));
                    }

//                    appslist= appl.toArray(appslist);
//                    appslist

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appl;
    }
}
