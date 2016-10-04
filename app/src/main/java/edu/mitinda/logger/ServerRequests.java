package edu.mitinda.logger;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vignesh on 03-10-2016.
 */

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String SERVER_ADDRESS = "http://vmobilebase.hol.es/tracker/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("Please wait");

    }

    public  void fetchDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new FetchDataAsyncTask(user, callback).execute();
    }

    public class FetchDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback callback;

        public FetchDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        private String getEncodedData(Map<String, String> data) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }

                stringBuilder.append(key + "=" + value);
            }
            return stringBuilder.toString();
        }

        @Override
        protected User doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uname", user.username);
            dataToSend.put("key", user.password);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;
            User returnedUser = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "authenticate.php" + "?uname=" + user.username + "&key=" + user.password);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"/*"POST"*/);
                /*connection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(encodedStr);
                writer.flush();*/

                StringBuilder stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                line = stringBuilder.toString();
                Log.v("JSON----------", line);

                JSONObject jsonObject = new JSONObject(line);

                if(jsonObject.length() == 0) {
                    returnedUser = null;
                }
                else {
                    String response = null;
                    if (jsonObject.has("response")) {
                        response = jsonObject.getString("response");
                        Log.v("hghgh----------", response);
                    }

                    returnedUser = new User(user.username, user.password);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            callback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
