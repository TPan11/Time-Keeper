package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

public class LogoutAsync extends AsyncTask<String, Void, String> {
    private Context context;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;

    //String backup_url = "http://10.0.2.2/DBConnections/inserttask.php";
    private String backup_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/inserttask.php";

    public LogoutAsync(Context context) {
        this.context = context;
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();

        URL url = null;
        try {
            url = new URL(backup_url);

    //        Log.d("check_backup", "opening connection");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);

     //       Log.d("check_register", "connect success");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&"
                    + URLEncoder.encode("task", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&"
                    + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&"
                    + URLEncoder.encode("priority", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&"
                    + URLEncoder.encode("detailedNotes", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&"
                    + URLEncoder.encode("duration", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") + "&"
                    + URLEncoder.encode("complete", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8") + "&"
                    + URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
     //       Log.d("check_backup", "MalformedURLException");
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
     //       Log.d("check_backup", "SocketTimeoutException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
    //        Log.d("check_backup", "UnsupportedEncodingException");
            e.printStackTrace();
        } catch (ProtocolException e) {
     //       Log.d("check_backup", "ProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
    //        Log.d("check_backup", "IOException");
            e.printStackTrace();
        }

        return result.toString();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //editor = mPreferences.edit();
        //editor.clear();
        //editor.apply();
    }
}
