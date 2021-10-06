package com.example.addfriend;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    TextView userName, fullName, school;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.friends_username);
        fullName = itemView.findViewById(R.id.friends_fullname);
        school = itemView.findViewById(R.id.friends_school);

    }
}
