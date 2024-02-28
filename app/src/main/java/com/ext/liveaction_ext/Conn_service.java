package com.ext.liveaction_ext;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Conn_service {
    String UserID = "";
    String endpot = "https://lifeactions.online";

    public String datasender(JSONObject jsonBody, String end, String firbaseTokenn) {
        try {
            String response = "";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            String urlString = endpot + end;
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("authorization", "Bearer " + firbaseTokenn);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream o = new DataOutputStream(urlConnection.getOutputStream());
            o.writeBytes(jsonBody.toString());
            o.flush();
            o.close();

            int responseCode = urlConnection.getResponseCode();

            String reso = urlConnection.getResponseMessage();

            Log.e("reso","reso"+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                    UserID = response;
                 //   Log.e("responseee", response);
                }
            }
            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e("Exect", String.valueOf(e));
        }
        return UserID;
    }

    public String formdatasend(JSONObject jsonBody, String end) {
        try {
            String response = "";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String urlString = endpot + end;
            Log.e("checkjson", String.valueOf(jsonBody));
            Log.e("checkRes21",urlString);
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
            Log.e("checkRescode", String.valueOf(responseCode));
            String reso = urlConnection.getResponseMessage();
            Log.e("reponsemsage", reso);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                    UserID = response;
                    Log.e("checkRes2", response);

                }
            }

            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e("checkRes3", String.valueOf(e));
        }
        return UserID;
    }

    public String pack_rule(String endr, String firbaseTokenn) {
        String response = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String[] appslist = new String[]{};

        try {


            URL url = new URL(endpot + endr);
          //  Log.e("endpoint", endr);

            HttpURLConnection urlConn = null;

            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestProperty("authorization", "Bearer " + firbaseTokenn);
            urlConn.setDoInput(true);
            int responseCode = urlConn.getResponseCode();
         //  Log.e("responseee1", String.valueOf(responseCode));
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String applist;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                while ((applist = br.readLine()) != null) {
                    response += applist;
                    Log.e("responseee", response);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }
    ///////////////////////////////////////


    public String crashLog(String str1, String str2) {

        try {
            String response = "";

            JSONObject obj = new JSONObject();
            obj.put("exception_msg", str1);

            //make single thread
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            String urlString = endpot + "/appException";
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            //urlConnection.setRequestProperty("authorization",  "Bearer " + firbaseTokenn);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream o = new DataOutputStream(urlConnection.getOutputStream());
            o.writeBytes(obj.toString());
            o.flush();
            o.close();

            int responseCode = urlConnection.getResponseCode();
            Log.e("Resposes", String.valueOf(responseCode));
            String reso = urlConnection.getResponseMessage();
            Log.e("reponsemsage", reso);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                    UserID = response;
                  //  Log.e("responseee", response);

                }
            }

            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e("Exect", String.valueOf(e));
        }

        return str1;
    }

}
