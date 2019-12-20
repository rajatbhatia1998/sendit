package com.example.sendit.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sendit.Adapter.MessageAdapter;
import com.example.sendit.Adapter.RoomAdapter;
import com.example.sendit.Model.Chat;
import com.example.sendit.Model.Room;
import com.example.sendit.Model.UserData;
import com.example.sendit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomFragment extends Fragment {

    ImageButton send_btn;
    EditText text_send;
    RecyclerView recyclerView;
    FirebaseUser fuser;
    DatabaseReference reference;
    RoomAdapter roomAdapter;
    List<Room> mchat;
    String sender_name;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        send_btn = view.findViewById(R.id.btn_send);
        text_send = view.findViewById(R.id.text_send);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),msg);
                }
                else {
                    Toast.makeText(getContext(),"You can't send empty msg",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        readMessage("lol","default");

        return view;
    }
        private void readMessage(String sender,final String imageurl){
            mchat = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference("Global Room");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mchat.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Room chat = snapshot.getValue(Room.class);
                        mchat.add(chat);

                        roomAdapter = new RoomAdapter(getContext(),mchat,imageurl);
                        recyclerView.setAdapter(roomAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    private void sendMessage(String sender, String msg){

        DatabaseReference reference_name = FirebaseDatabase.getInstance().getReference("Users").child(sender);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData user = dataSnapshot.getValue(UserData.class);
                sender_name = user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender_name);
        hashMap.put("message",msg);
        hashMap.put("sender_id",sender);
        reference.child("Global Room").push().setValue(hashMap);


    }
}
