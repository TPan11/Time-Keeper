package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.btbsolutions.timekeeper.interfaces.RegisterResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterAsync extends AsyncTask<String, Void, String> {

    public RegisterResponse delegate = null;

    //private String register_url = "http://10.0.2.2/DBConnections/registration.php";
    private String register_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/registration.php";

    private WeakReference<View> mProgressView;
    private WeakReference<View> mLoginFormView;
    private String type;

    public RegisterAsync(Context context, View progressView, View loginFormView) {
        //this.context = context;
        this.mProgressView = new WeakReference<View>(progressView);
        this.mLoginFormView = new WeakReference<View>(loginFormView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        View pv = mProgressView.get();
        View lv = mLoginFormView.get();
        lv.setVisibility(View.GONE);
        pv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        View pv = mProgressView.get();
        View lv = mLoginFormView.get();
        lv.setVisibility(View.VISIBLE);
        pv.setVisibility(View.GONE);
        delegate.registerProcessFinish(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {
        String fname = params[0];
        String lname = params[1];
        String age = params[2];
        String useremail = params[3];
        String password = params[4];

        URL url = null;

        StringBuilder result = new StringBuilder();
        try {
            url = new URL(register_url);

      //      Log.d("check_register", "opening connection");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);

    //        Log.d("check_register", "connect success");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8") + "&"
                    + URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8") + "&"
                    + URLEncoder.encode("birthday", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8") + "&"
                    + URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
    //        Log.d("check_register", "MalformedURLException");
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
      //      Log.d("check_register", "SocketTimeoutException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
    //        Log.d("check_register", "UnsupportedEncodingException");
            e.printStackTrace();
        } catch (ProtocolException e) {
    //        Log.d("check_register", "ProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
     //       Log.d("check_register", "IOException");
            e.printStackTrace();
        }

        return result.toString();
    }
}
