package com.sports.oscaracademy.chatService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.chatMessageAdapter;
import com.sports.oscaracademy.data.Message;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.ActivityChatActivtyBinding;
import com.sports.oscaracademy.service.studentsList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class chat_activty extends AppCompatActivity {
    ActivityChatActivtyBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    chatMessageAdapter adapter;
    ArrayList<Message> messages = new ArrayList<>();
    String receiverUid;
    String senderUid = FirebaseAuth.getInstance().getUid();
    String senderRoom;
    String receiverRoom;
    String receiverToken;
    SharedPreferences pref;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_activty);
        String receiverName = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        setSupportActionBar(binding.toolbar.getRoot());
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        receiverRoom = receiverUid + senderUid;
        senderRoom = senderUid + receiverUid;
        getuserName();
        FirebaseFirestore.getInstance().collection("token").document(receiverUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        receiverToken = task.getResult().get("token", String.class);
                        Log.e("TAG", "onComplete: token" + receiverToken);
                    }
                });

        Sprite doubleBounce = new WanderingCubes();
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);
        binding.toolbar.topTitleName.setText(receiverName);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new chatMessageAdapter(this, messages, senderRoom, receiverRoom);
        binding.recyclerView.setAdapter(adapter);


        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (status != null) {
                        if (!status.isEmpty()) {
                            if (status.equals("offline")) {
                                binding.toolbar.status.setVisibility(View.GONE);
                            } else {
                                binding.toolbar.status.setVisibility(View.VISIBLE);
                                binding.toolbar.status.setText(status);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                        try {
                            binding.recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //                        binding.recyclerView.smoothScrollToPosition(-1);
                        binding.progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        final Handler handler = new Handler();
        binding.messageBox.addTextChangedListener(new TextWatcher() {
            final Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("presence").child(senderUid).setValue("Online");
                }
            };

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.sendBtn.setEnabled(count != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                database.getReference().child("presence").child(senderUid).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 1000);
            }
        });


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.messageBox.getText().toString();

                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());
                binding.messageBox.setText("");

                String randomKey = database.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendNotification(pref.getString("userName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName()), message.getMessage(), receiverToken);
                            }
                        });
                    }
                });

            }
        });
    }

    private void getuserName() {
        studentsList list = new studentsList(this);
        SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
        if (pref.getString("isStudent", "false").equals("true")) {
            list.getStudents().observeForever(new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    for (Studentdata data : studentdata)
                        if (FirebaseAuth.getInstance().getUid().equals(data.getUserId())) {
                            editor.putString("userName", data.getName());
                            editor.apply();
                        }
                }
            });
        } else {
            list.getUsers().observeForever(new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    for (Studentdata data : studentdata)
                        if (FirebaseAuth.getInstance().getUid().equals(data.getUserId())) {
                            editor.putString("userName", data.getName());
                            editor.apply();
                        }
                }
            });
        }
    }

    void sendNotification(String name, String message, String token) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title", name);
            data.put("body", message);
            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", data);
            notificationData.put("to", token);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, notificationData
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(chat_activty.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("presence").child(FirebaseAuth.getInstance().getUid()).setValue("Online");
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("presence").child(FirebaseAuth.getInstance().getUid()).setValue("offline");
    }
}