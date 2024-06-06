package com.ext.liveaction_ext.service_clas;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.ext.liveaction_ext.Screens.FailureScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

public class Conn_service {
    private static final String TAG = "Conn_service";
    private String UserID = "";
    private SharedPreferences sh;
    private ArrayList<String> appl = new ArrayList<>();
    private String endpot = "https://sondersort.online";
    private Activity activity;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Conn_service(Activity activity) {
        this.activity = activity;
    }

    public void App_list(Context context, AppListCallback callback) {
        executorService.execute(() -> {
            sh = context.getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.putString("endpt", endpot);
            myEdit.apply();
            try {
                String response = "";
                URL url = new URL(endpot + "/apps/list");
                Log.e(TAG, String.valueOf(url));
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setDoInput(true);
                int responseCode = urlConn.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String applist;
                    while ((applist = br.readLine()) != null) {
                        response += applist;
                    }
                    final JSONObject app = new JSONObject(response);
                    JSONArray appsList = app.getJSONArray("data");
                    for (int i = 0; i < appsList.length(); i++) {
                        appl.add(appsList.getString(i));
                    }
                }else{
                    showFailureScreen();
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getMessage());
                showFailureScreen();
            }
            // Pass result to callback
            callback.onResult(appl);
        });
    }

    public void datasender(JSONObject jsonBody, String end, String firebaseToken, View.OnClickListener onClickListener, ResponseCallback responseCallback) {
        executorService.execute(() -> {
            try {
                String response = "";
                String urlString = endpot + end;
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("authorization", "Bearer " + firebaseToken);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                DataOutputStream o = new DataOutputStream(urlConnection.getOutputStream());
                o.writeBytes(jsonBody.toString());
                o.flush();
                o.close();
                int responseCode = urlConnection.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    UserID = response;
                }else {
                    showFailureScreen();
                }
                urlConnection.disconnect();
//                onClickListener.onClick(null); // You can pass a View object if needed
                activity.runOnUiThread(() -> onClickListener.onClick(null));

                // Pass the response to the ResponseCallback interface
                if (activity != null) {
                    activity.runOnUiThread(() -> responseCallback.onResponse(UserID));}
                // Pass the response to the ResponseCallback interface
//                responseCallback.onResponse(response);
            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
                showFailureScreen();
            }
            // Pass result to callback


        });
    }

    public void formdatasend(JSONObject jsonBody, String end, FormDataSendCallback callback) {
        executorService.execute(() -> {
            try {
                String response = "";
                String urlString = endpot + end;
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
                Log.e(TAG, String.valueOf(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    UserID = response;
                }
                else{
                    showFailureScreen();
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
                showFailureScreen();
            }
            // Pass result to callback
            callback.onResult(UserID);
        });
    }
    public void PackRulecomment(String endr, String firebaseToken, PackRulecommentCallback callback) {
        executorService.execute(() -> {
            String response = "";
            try {
                URL url = new URL(endpot + endr);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("authorization", "Bearer " + firebaseToken);
                urlConn.setDoInput(true);
                int responseCode = urlConn.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode)+url);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String applist;
                    while ((applist = br.readLine()) != null) {
                        response += applist;
                    }
                    Log.e(TAG, response);
                }
                else{
                    showFailureScreen();
                }
                String finalResponse = response;
                if (activity != null) {
                    activity.runOnUiThread(() -> callback.onResultcomment(finalResponse));}
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                showFailureScreen();
            }
            // Pass result to callback

        });
    }

    public void pack_rule(String endr, String firebaseToken, PackRuleCallback callback) {
        executorService.execute(() -> {
            String response = "";
            try {
                URL url = new URL(endpot + endr);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("authorization", "Bearer " + firebaseToken);
                urlConn.setDoInput(true);
                int responseCode = urlConn.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode)+url);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String applist;
                    while ((applist = br.readLine()) != null) {
                        response += applist;
                    }
                    Log.e(TAG, response);
                }else{
                    showFailureScreen();
                }
                String finalResponse = response;
                if (activity != null) {
                    activity.runOnUiThread(() ->  callback.onResult(finalResponse));}
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                showFailureScreen();
            }
        });
    }
    public void pack_ruleclick( String endr, String firebaseToken, View.OnClickListener onClickListener, PackRuleClickCallback resCallback) {
        executorService.execute(() -> {
            String response = "";
            try {
                URL url = new URL(endpot + endr);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("authorization", "Bearer " + firebaseToken);
                urlConn.setDoInput(true);
                int responseCode = urlConn.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode)+url);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String applist;
                    while ((applist = br.readLine()) != null) {
                        response += applist;
                    }
                    Log.e(TAG, response);
                }
                else{
                    showFailureScreen();
                }
                activity.runOnUiThread(() -> onClickListener.onClick(null));

                // Pass the response to the ResponseCallback interface
                String finalResponse = response;
                if (activity != null) {
                    activity.runOnUiThread(() -> resCallback.onResposeClick(finalResponse));}

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                showFailureScreen();
            }
            // Pass result to callback

        });
    }

    public void crashLog(String str1, CrashLogCallback callback) {
        executorService.execute(() -> {
            try {
                String response = "";
                JSONObject obj = new JSONObject();
                obj.put("exception_msg", str1);
                String urlString = endpot+"/appException";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                DataOutputStream o = new DataOutputStream(urlConnection.getOutputStream());
                o.writeBytes(obj.toString());
                o.flush();
                o.close();
                int responseCode = urlConnection.getResponseCode();
                Log.e(TAG, String.valueOf(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    UserID = response;
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
            }
            // Pass result to callback
            callback.onCrash(str1);
        });
    }
    private void showFailureScreen() {
        activity.runOnUiThread(() -> {
            Intent intent = new Intent(activity, FailureScreenActivity.class);
            activity.startActivity(intent);
        });
    }

    public interface AppListCallback {
        void onResult(ArrayList<String> appl);
    }



    public interface ResponseCallback {
        void onResponse(String response);
    }
    public interface FormDataSendCallback {
        void onResult(String result);
    }

    public interface PackRuleCallback {
        void onResult(String result);
    }
    public interface PackRulecommentCallback {
        void onResultcomment(String result);
    }
    public interface PackRuleClickCallback {
        void onResposeClick(String response);
    }


    public interface CrashLogCallback {
        void onCrash(String result);
    }
}