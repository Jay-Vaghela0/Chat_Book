package com.jayvaghela.chatbookmlx;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jay Vaghela on 8-11-2019
 */

public class MainActivity extends AppCompatActivity {

    private EditText room_name;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button add_room = findViewById(R.id.btn_add_room);
        room_name = findViewById(R.id.room_name_edittext);
        ListView listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView.setAdapter(arrayAdapter);

        //Ask user to enter username first

        request_user_name();
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<>();
                map.put(room_name.getText().toString(), "");
                root.updateChildren(map);

            }
        });


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    set.add((snapshot).getKey());
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intentChatroom = new Intent(getApplicationContext(), Chat_Room.class);
                intentChatroom.putExtra("room_name", ((TextView) view).getText().toString());
                intentChatroom.putExtra("user_name", name);
                startActivity(intentChatroom);
            }
        });

    }


    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter name:");

        final EditText input_field = new EditText(this);
        builder.setView(input_field);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (name != null) //check for null user 
                    name = input_field.getText().toString();
                request_user_name();
            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")// :)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input_field.setText("Anonymous");
                name = input_field.getText().toString();
            }
        });

        builder.show();
    }


}