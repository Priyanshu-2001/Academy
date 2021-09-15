package com.sports.oscaracademy.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.sports.oscaracademy.data.feedsData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class feedsService {
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void UpdateFeeds(feedsData data, Context context) {
        String randomKey = db.getReference().push().getKey();
        db.getReference()
                .child("newsfeeds")
                .child(randomKey)
                .setValue(data).addOnSuccessListener(unused -> {
            sendNotification(context);
        });
    }

    private void sendNotification(Context context) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title", "Your News Feed has been Updated !!");
            data.put("body", "Check Now ");
            data.put("tag", "feed");
            data.put("click_action", "feed");

            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", data);
            notificationData.put("to", "/topics/feeds");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, notificationData
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("TAG", "onResponse: " + response + " " + " sent sucessfully");
                    // Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(chat_activty.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    String key = "Key=AAAAbf9g9hw:APA91bGO2qax6-20o-PAlCUDiG0DUOFL3aOz6u9__m5tVj-smkX3nEwwYA7wfyfz5kBLmbTKHPqTXdgtuMXVGPFGklLvw_jfqIwKYehw7PlTI7QZqdVXV5ppvMbl8VCnMKV5WTE7Wq5c";
                    map.put("Content-Type", "application/json");
                    map.put("Authorization", key);
                    return map;
                }
            };

            queue.add(request);


        } catch (Exception ex) {

        }


    }

    public void delete_feeds(feedsData data) {
        db.getReference().child("newsfeeds")
                .child(data.getFeedID())
                .removeValue();
    }

    public void update_feeds(feedsData data, feedsData newData) {
        db.getReference().child("newsfeeds")
                .child(data.getOnlyDate())
                .child(data.getFeedID())
                .setValue(newData);
    }
}