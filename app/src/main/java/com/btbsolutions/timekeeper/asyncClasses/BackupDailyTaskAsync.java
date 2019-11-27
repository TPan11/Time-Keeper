package com.btbsolutions.timekeeper.asyncClasses;

import android.os.AsyncTask;

import com.btbsolutions.timekeeper.interfaces.BackupDailyTaskResponse;

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

public class BackupDailyTaskAsync extends AsyncTask<String, Void, String> {

    //String backup_url = "http://10.0.2.2/DBConnections/insertdailytask.php";
    String backup_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/insertdailytask.php";

    public BackupDailyTaskResponse delegate = null;
    public long taskId = 0;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.backupDailyTaskProcessFinish(s, taskId);
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();

        String id = strings[0];
        String task = strings[1];
        String date = strings[2];
        String startTime = strings[3];
        String endTime = strings[4];
        String metricQ = strings[5];
        String duration = strings[6];
        String complete = strings[7];
        String useremail = strings[8];

        URL url = null;
        try {
            url = new URL(backup_url);

     //       Log.d("check_backup", "opening connection");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);

      //      Log.d("check_backup", "connect success");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                    + URLEncoder.encode("task", "UTF-8") + "=" + URLEncoder.encode(task, "UTF-8") + "&"
                    + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                    + URLEncoder.encode("startTime", "UTF-8") + "=" + URLEncoder.encode(startTime, "UTF-8") + "&"
                    + URLEncoder.encode("endTime", "UTF-8") + "=" + URLEncoder.encode(endTime, "UTF-8") + "&"
                    + URLEncoder.encode("metricQ", "UTF-8") + "=" + URLEncoder.encode(metricQ, "UTF-8") + "&"
                    + URLEncoder.encode("duration", "UTF-8") + "=" + URLEncoder.encode(duration, "UTF-8") + "&"
                    + URLEncoder.encode("complete", "UTF-8") + "=" + URLEncoder.encode(complete, "UTF-8") + "&"
                    + URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8");
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
    //        Log.d("check_backup", "SocketTimeoutException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
     //       Log.d("check_backup", "UnsupportedEncodingException");
            e.printStackTrace();
        } catch (ProtocolException e) {
     //       Log.d("check_backup", "ProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
     //       Log.d("check_backup", "IOException");
            e.printStackTrace();
        }

        return result.toString();
    }
}
