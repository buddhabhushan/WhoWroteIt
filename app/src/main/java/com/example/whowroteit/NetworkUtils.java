package com.example.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";
    private static final String DOWNLOADABLE = "download";

    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String bookJSONString = null;

        try {
            Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .appendQueryParameter(DOWNLOADABLE, "epub")
                    .build();

            URL requestURL = new URL(builtUri.toString());

            // Open the urlconnection and then connect
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from inputStream
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Use the String builder to hold the incoming response
            StringBuilder builder = new StringBuilder();

            // Get the string
            String line;
            while((line = bufferedReader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }

            // Check if InputStream was empty
            if(builder.length() == 0){
                return null;
            }

            bookJSONString = builder.toString();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, bookJSONString);

        return bookJSONString;
    }

}
