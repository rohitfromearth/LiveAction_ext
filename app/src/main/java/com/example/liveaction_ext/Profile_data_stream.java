package com.example.liveaction_ext;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Profile_data_stream {
    String UserID = "";


    public String datasender(String urlString, JSONObject jsonBody) {
        try {
        String response = "";
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);



        DataOutputStream o = new DataOutputStream(urlConnection.getOutputStream());
        o.writeBytes(jsonBody.toString());
        o.flush();
        o.close();

        int responseCode = urlConnection.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((line = br.readLine()) != null) {
                response += line;
                String resid = response;
//                             final JSONObject obj = new JSONObject(response);
//                             final JSONArray geodata = obj.getJSONArray("geodata");
                final JSONObject user = new JSONObject(response);

////////////////////////////////////
                UserID = user.getString(("user_id"));

/////////////////////////////////////////////////


            }
        }




        urlConnection.disconnect();

    }
        catch (Exception e) {

        }
        return UserID;
}

}
