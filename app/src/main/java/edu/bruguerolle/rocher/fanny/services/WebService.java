package edu.bruguerolle.rocher.fanny.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.bruguerolle.rocher.fanny.model.Match;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class WebService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POST = "edu.bruguerolle.rocher.fanny.services.action.POST";
    private static final String EXTRA_MATCH = "edu.bruguerolle.rocher.fanny.services.extra.MATCH";

    private static final String ACTION_PUT = "edu.bruguerolle.rocher.fanny.services.action.PUT";
    private static final String EXTRA_PUT = "edu.bruguerolle.rocher.fanny.services.extra.PUT";

    private static final String ACTION_GET = "edu.bruguerolle.rocher.fanny.services.action.GET";
    private static final String EXTRA_DEVICE_ID = "edu.bruguerolle.rocher.fanny.services.extra.DEVICE_ID";
    private static final String EXTRA_CALLBACK = "edu.bruguerolle.rocher.fanny.services.extra.CALLBACK";
    public static final int CALLBACK_CODE = 1;
    public static final String EXTRA_CODE = "edu.bruguerolle.rocher.fanny.services.extra.RESULT";

    /**
     * Node.JS RESTAPI
     * To check the code go to https://fannyapi-fpnzdflooq.now.sh/_src
     */
    private static final String ENDPOINT = "https://fannyapi-fpnzdflooq.now.sh/match/";

    public WebService() {
        super("WebService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionPost(Context context, Match match) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(ACTION_POST);
        JSONObject json = match.toJson();
        intent.putExtra(EXTRA_MATCH, json.toString());
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGet(Context context, String deviceId, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(ACTION_GET);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(EXTRA_CALLBACK, resultReceiver);
        context.startService(intent);
    }

    public static void startActionPut(Context context, String deviceId, long matchSQLId, String imgPath) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(ACTION_PUT);
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", deviceId);
            json.put("id", matchSQLId);
            json.put("imgPath", imgPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    intent.putExtra(EXTRA_PUT, json.toString());
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST.equals(action)) {
                final String json = intent.getStringExtra(EXTRA_MATCH);
                handleActionPost(json);
            }
            else if (ACTION_GET.equals(action)) {
                Bundle bundle = new Bundle();
                ResultReceiver callback = intent.getParcelableExtra(EXTRA_CALLBACK);
                final String deviceId = intent.getStringExtra(EXTRA_DEVICE_ID);
                bundle.putString(EXTRA_CODE, handleActionGet(deviceId));
                callback.send(CALLBACK_CODE, bundle);
            }
            else if(ACTION_PUT.equals(action)) {
                final String json = intent.getStringExtra(EXTRA_PUT);
                handleActionPut(json);
            }
        }
    }

    /**
     * Handle action GET in the provided background thread with the provided
     * parameters.
     */
    private String handleActionGet(String deviceId) {
        HttpsURLConnection http = null;
        try {
            http =  (HttpsURLConnection) new URL(ENDPOINT+deviceId).openConnection();;

            int responseCode = http.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                return readStream(http.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(http != null) {
                http.disconnect();
            }
        }

        return null;
    }

    /**
     * Handle action POST in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPost(String matchJson) {
        HttpsURLConnection http = null;
        try {
            http = (HttpsURLConnection) new URL(ENDPOINT).openConnection();;
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            OutputStream os = http.getOutputStream();
            os.write(matchJson.getBytes("UTF-8"));
            os.close();

            http.connect();


            int status = http.getResponseCode();
            System.out.println("HTTP status code : " + status);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(http != null) {
                http.disconnect();
            }
        }
    }

    private void handleActionPut(String matchJson) {
        HttpsURLConnection http = null;
        try {
            http = (HttpsURLConnection) new URL(ENDPOINT+"/img").openConnection();;
            http.setRequestMethod("PUT");
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            OutputStream os = http.getOutputStream();
            os.write(matchJson.getBytes("UTF-8"));
            os.close();

            http.connect();


            int status = http.getResponseCode();
            System.out.println("HTTP status code : " + status);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(http != null) {
                http.disconnect();
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
