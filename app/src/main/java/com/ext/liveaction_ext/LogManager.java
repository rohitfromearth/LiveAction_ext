package com.ext.liveaction_ext;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogManager {

    private static LogManager instance;
    private static final String LOG_FILE_NAME = "activity_log.txt";
    private Context context;

    private LogManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static LogManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LogManager.class) {
                if (instance == null) {
                    instance = new LogManager(context);
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String logMessage = timestamp + " - " + message + "\n";

        try (FileOutputStream fos = context.openFileOutput(LOG_FILE_NAME, Context.MODE_APPEND)) {
            fos.write(logMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLogs() {
        StringBuilder logContent = new StringBuilder();

        try (FileInputStream fis = context.openFileInput(LOG_FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                logContent.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logContent.toString();
    }
}
