package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.btbsolutions.timekeeper.interfaces.GetAllDailyTaskDataResponse;

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

public class GetAllDailyTaskDataAsync extends AsyncTask<String, Void, String> {

    public GetAllDailyTaskDataResponse delegate = null;
    Context mContext;
    private View mProgressView;
    private View mLoginFormView;


    public GetAllDailyTaskDataAsync(Context context, View progressView, View loginFormView){
        this.mContext = context;
        this.mProgressView = progressView;
        this.mLoginFormView = loginFormView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoginFormView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mLoginFormView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        delegate.getAllDailyTaskDataProcessFinish(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        //String data_url = "http://10.0.2.2/DBConnections/getAllDailyTaskEntriesByUser.php";
        String data_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/getAllDailyTaskEntriesByUser.php";
        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        String useremail = strings[0];
        try {
            URL url = new URL(data_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8");
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
