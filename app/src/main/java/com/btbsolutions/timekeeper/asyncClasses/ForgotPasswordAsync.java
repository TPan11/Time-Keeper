package com.btbsolutions.timekeeper.asyncClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.btbsolutions.timekeeper.interfaces.ForgotPasswordRespose;

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

public class ForgotPasswordAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    public ForgotPasswordRespose delegate = null;

    public ForgotPasswordAsync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.forgotPasswordProcessFinish(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        String useremail = strings[0];
        String bdate = strings[1];
        StringBuilder result = new StringBuilder();
        try {

            //private String login_url = "http://10.0.2.2/DBConnections/login.php";
            String login_url = "http://tpandedeveloper.000webhostapp.com/dbconnection/forgotpassword.php";
            URL url = new URL(login_url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(5000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(useremail, "UTF-8") + "&"
                    + URLEncoder.encode("bdate", "UTF-8") + "=" + URLEncoder.encode(bdate, "UTF-8");
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
