package com.jayvaghela.chatbookmlx;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jay Vaghela on 8-11-2019
 */


public class Chat_Room extends AppCompatActivity {

    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name;
    private DatabaseReference root;
    private String temp_key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        final Button btn_send_msg = findViewById(R.id.btn_send);
        input_msg = findViewById(R.id.msg_input);
        chat_conversation = findViewById(R.id.textView);

        user_name = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("user_name")).toString();
        final String room_name = Objects.requireNonNull(getIntent().getExtras().get("room_name")).toString();
        setTitle(" Room - " + room_name);


        root = FirebaseDatabase.getInstance().getReference().child(room_name);


        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == null) {
                    input_msg.setText(room_name);
                }

                Map<String, Object> map = new HashMap<>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", user_name);
                map2.put("msg", input_msg.getText().toString());

                message_root.updateChildren(map2);

                input_msg.getText().clear();


            }
        });


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {

            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            String chat_user_name = (String) ((DataSnapshot) i.next()).getValue();

            chat_conversation.append(chat_user_name + " : " + chat_msg + " \n");
        }


    }
}