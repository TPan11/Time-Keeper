package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.btbsolutions.timekeeper.interfaces.LoginResponse;

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

public class LoginAsync extends AsyncTask<String, Void, String> {

    public LoginResponse delegate = null;

    private Context context;
    //private AlertDialog alertDialog;
    //private WeakReference<View> mProgressView;
    //private WeakReference<View> mLoginFormView;

    private View mProgressView;
    private View mLoginFormView;
    private String type;

    public LoginAsync(Context context, View progressView, View loginFormView) {
        this.context = context;
        this.mProgressView = progressView;
        this.mLoginFormView = loginFormView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //View pv = mProgressView.get();
        //View lv = mLoginFormView.get();
        //lv.setVisibility(View.GONE);
        //pv.setVisibility(View.VISIBLE);
        mLoginFormView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //View pv = mProgressView.get();
        //View lv = mLoginFormView.get();
        //lv.setVisibility(View.VISIBLE);
        //pv.setVisibility(View.GONE);
        mLoginFormView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        context = null;
        mProgressView = null;
        mLoginFormView = null;
        delegate.loginProcessFinish(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        mLoginFormView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... params) {
        String useremail = params[0];
        String password = params[1];
        StringBuilder result = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        try {

            //private String login_url = "http://10.0.2.2/DBConnections/login.php";
            String login_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/login.php";
            url = new URL(login_url);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(5000);
            //Toast.makeText(context, httpURLConnection.getResponseCode(), Toast.LENGTH_LONG).show();
            //if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, "iso-8859-1"));
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            //}
            //else{
            //    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            //}
        } catch (MalformedURLException e) {
      //      Log.d("check_login", "MalformedURLException");
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
   //         Log.d("check_login", "SocketTimeoutException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
      //      Log.d("check_login", "UnsupportedEncodingException");
            e.printStackTrace();
        } catch (ProtocolException e) {
      //      Log.d("check_login", "ProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
     //       Log.d("check_login", e.toString());
            e.printStackTrace();
        }

        return result.toString();
    }
}
