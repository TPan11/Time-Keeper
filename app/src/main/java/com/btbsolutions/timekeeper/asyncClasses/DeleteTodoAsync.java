package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DeleteTodoAsync extends AsyncTask<String, Void, String> {

    Context mContext;
    //public DeleteTodoResponse delegate = null;

    public DeleteTodoAsync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //delegate.getAllToDoDataDeleteFinish(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        String delete_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/deleteTodoEntries.php";
        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        String useremail = strings[0];
        String id = strings[1];
        try {
            URL url = new URL(delete_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8") + "&"
                    + URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
