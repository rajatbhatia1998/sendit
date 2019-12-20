package com.example.sendit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendit.Fragments.UsersFragment;
import com.example.sendit.MessageActivity;
import com.example.sendit.Model.Chat;
import com.example.sendit.Model.Room;
import com.example.sendit.Model.UserData;
import com.example.sendit.R;
import com.google.android.gms.common.images.ImageManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private Context mContext;
    private List<Room> mChat;
    private String imageUrl;

    FirebaseUser fuser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT =1 ;



    public RoomAdapter(Context mContext, List<Room> mChat, String imageUrl){
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            return new RoomAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_chat_item_left,viewGroup,false);
            return new RoomAdapter.ViewHolder(view);
        }


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Room chat = mChat.get(i);
        Random random = new Random();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        viewHolder.show_message.setText(chat.getMessage());



        if(fuser.getUid().equals(chat.getSender_id())){
            viewHolder.sender_name.setText("You");
            viewHolder.sender_name.setTextColor(Color.GRAY);
        }
        else {
            viewHolder.sender_name.setText(chat.getSender());
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            viewHolder.sender_name.setTextColor(Color.rgb(r,g,b));
        }
        if(imageUrl.equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Picasso.with(mContext).load(imageUrl).into(viewHolder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView sender_name;

        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            sender_name = itemView.findViewById(R.id.sender_name);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser.getUid().equals(mChat.get(position).getSender())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
