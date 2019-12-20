package com.example.sendit.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.sendit.Model.UserData;
import com.example.sendit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
//import com.google.android.gms.common.images.ImageRequest;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<UserData> mUsers;
    private boolean ischat;
    String ThelastMessage;
    public UserAdapter(Context mContext,List<UserData> mUsers,boolean ischat){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final UserData user;
        user = mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if("default".equals(user.getImageURL())){
            viewHolder.profile_image.setImageResource(R.drawable.ic_default_profile_picture);
        }
        else
        {
            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);
        }
        if(ischat){
            viewHolder.lastMessage(user.getId(),viewHolder.last_msg);
        }
        else{
            viewHolder.last_msg.setVisibility(View.GONE);
        }
        if(ischat){
            if(user.getStatus().equals("online")){
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            }
            else{
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userID",user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
        private void lastMessage(final String userId, final TextView last_msg){
            ThelastMessage = "default";
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getReciever().equals(firebaseUser.getUid())&&chat.getSender().equals(userId) || chat.getReciever().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
                            ThelastMessage = chat.getMessage();
                        }

                    }
                    switch (ThelastMessage){
                        case "default":
                            last_msg.setText("No Message");
                            break;
                        default:
                            last_msg.setText(ThelastMessage);
                    }

                    ThelastMessage = "default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
