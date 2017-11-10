package com.example.android.booklisting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String message = "";
    private String font = "fonts/Moodyrock.ttf";
    private ImageView headerpic;
    private LinearLayout headerPics;
    private EditText searching;
    private TextView text;
    private ListView view;
    private AlertDialog.Builder builder;

    private static final String TITLE = "title";
    private static final String CATEGORY = "categories";
    private static final String AUTHOR = "authors";
    private static final String PUBLISHER = "publisher";
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?";
    /*********************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headerpic = (ImageView) findViewById(R.id.headerPic);
        headerPics = (LinearLayout) findViewById(R.id.fiveHeaderPics);
        builder = new AlertDialog.Builder(MainActivity.this);
        text = (TextView) findViewById(R.id.example);
        searching = (EditText) findViewById(R.id.searchText);
        Button btnSearch = (Button) findViewById(R.id.searchButton);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new JSONTask().execute();
            }
        });//Onclick

        TextView header = (TextView) findViewById(R.id.header);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), font);
        header.setTypeface(typeface);
        text.setTypeface(typeface);

    }//On create
    /*********************************************************************************/
    /*
    Set visibility of certain views/layouts depending on orientation
     */
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        switch (orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                headerpic.setVisibility(View.GONE);
                headerPics.setVisibility(View.GONE);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                headerpic.setVisibility(View.VISIBLE);
                headerPics.setVisibility(View.VISIBLE);
                break;
        }
    }
    /*
    Internet connection
     */
    private boolean internetAvailable(){
        ConnectivityManager connected = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = connected.getActiveNetworkInfo();
        return active != null && active.isConnectedOrConnecting();
    }

    /*********************************************************************************/
    public class JSONTask extends AsyncTask<String, Void, ArrayList<BookDetails>> {

        @Override
        protected ArrayList<BookDetails> doInBackground(String... urls) {

            String query = searching.getText().toString().replace(" ", "").trim();
            HttpURLConnection connection = null;
            BufferedReader reader = null;


                try {
                    URL url = new URL(API_URL + "q=" + query + "&maxResults=20");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    System.out.println(buffer.toString()); //For me to check JSON results in the console
                    String finalJSON = buffer.toString();

                    ArrayList<BookDetails> book = new ArrayList<>();

                    // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                    // build up a list of book objects with the corresponding data.
                    String pic;
                    JSONObject baseJsonResponse = new JSONObject(finalJSON);
                    if (baseJsonResponse.isNull("items")) {
                        message = "No Books to Display";

                    } else {
                        JSONArray bookArray = baseJsonResponse.getJSONArray("items");
                        for (int i = 0; i < bookArray.length(); i++) {
                            JSONObject currentbook = bookArray.getJSONObject(i);
                            JSONObject properties = currentbook.getJSONObject("volumeInfo");
                            if (currentbook.getJSONObject("volumeInfo").has("imageLinks")) {
                                pic = properties.getJSONObject("imageLinks").optString("smallThumbnail", "none");
                            } else {
                                pic = "none";
                            }
                            String title = properties.optString(TITLE, "none");
                            String cat = properties.optString(CATEGORY, "none");
                            String author = properties.optString(AUTHOR, "none");
                            String pub = properties.optString(PUBLISHER, "none");

                            BookDetails books = new BookDetails(pic, title, cat, author, pub);
                            book.add(books);

                        }
                    }
                    return book;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    // If an error is thrown when executing any of the above statements in the "try" block,
                    // catch the exception here, so the app doesn't crash. Print a log message
                    // with the message from the exception.
                    Log.e("MainActivity", "Problem parsing the Books JSON results", e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            return null;
        }
        /*********************************************************************************/
        @Override
        protected void onPostExecute(ArrayList<BookDetails> result) {
            super.onPostExecute(result);
            view = (ListView) findViewById(R.id.list);

            if(internetAvailable() == true){
                if(message.equals("")){
                    text.setText(message);
                    view.setVisibility(View.VISIBLE);
                    ArrayList<BookDetails> books = result;
                    final BookAdapter adapter = new BookAdapter(MainActivity.this, books);
                    view.setAdapter(adapter);
                }else{
                    view.setVisibility(View.GONE);
                    text.setText(message);
                    message = "";
                }
            }else{
                builder.create();
                builder.setTitle("Uh Oh :( ").setMessage("No Internet Connection").setIcon(R.drawable.fail).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }


        }
    }

}//End class

