
package com.example.gigpasser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigpasser.Model.User;
import com.example.gigpasser.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    private Context  context;
    private List<User> userList;

    public UserAdapter() {
    }

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_displayed_layout,parent,false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user=userList.get(position);

        holder.type.setText(user.getType());

        if(user.getType().equals("donor")){
            holder.emailNow.setVisibility(View.GONE);
        }
        holder.userEmail.setText(user.getEmail());
        holder.phoneNumber.setText(user.getPhoneNumber());
        holder.userName.setText(user.getName());
        holder.lefteye.setText(user.getLefteye());
        holder.righteye.setText(user.getRighteye());


        //   Glide.with(context).load(user.getProfilepictureurl()).into(holder.userProfileImage);



    }

    @Override
    public int getItemCount()
    {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {

        // public CircleImageView userProfileImage;
        public TextView type,userName,userEmail,phoneNumber,lefteye,righteye;
        public Button emailNow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //    userProfileImage=itemView.findViewById(R.id.userProfileImage);
            type=itemView.findViewById(R.id.type);
            userName=itemView.findViewById(R.id.userName);
            userEmail=itemView.findViewById(R.id.userEmail);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
            lefteye=itemView.findViewById(R.id.lefteye);
            righteye=itemView.findViewById(R.id.righteye);
            emailNow=itemView.findViewById(R.id.emailNow);



        }
    }


}