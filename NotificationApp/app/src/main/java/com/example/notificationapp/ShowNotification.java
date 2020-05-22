package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.Uri.*;
import static com.example.notificationapp.R.layout.activity_show_notification;

public class ShowNotification extends AppCompatActivity {
    TextView notification;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        notification = (TextView) findViewById(R.id.notification);
        imageView = this.<ImageView>findViewById(R.id.imageView);
        showNotification(getIntent().getStringExtra("ID"));
    }

    private void showNotification(String id) {
        String url = "http://10.0.2.2:3000/announcements/"+getIntent().getStringExtra("ID");

        RequestQueue requestQueue = Volley.newRequestQueue(ShowNotification.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.has("imageURL")) {
                        notification.setText("Date :" + object.getString("date") + "\n\n" + "Title :  " + object.getString("title") + "\n\n" + "Description :  " + object.getString("description") + "\n\nDetails :  " + object.getString("details") + "\n\nLink :  " + object.getString("link")+"\n\nImage:");
                        String url = object.getString("imageURL").replace("localhost","10.0.2.2");
                        Picasso.get().load(url).into(imageView);
                    }else{
                        notification.setText("Date :" + object.getString("date") + "\n\n" + "Title :  " + object.getString("title") + "\n\n" + "Description :  " + object.getString("description") + "\n\nDetails :  " + object.getString("details") + "\n\nLink :  " + object.getString("link"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type","application/json; charset=utf-8");
                params.put("Authorization","Bearer "+getIntent().getStringExtra("TOKEN"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
