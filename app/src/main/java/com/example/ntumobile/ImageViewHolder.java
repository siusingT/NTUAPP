package com.example.ntumobile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.image_single_view);
        textView = itemView.findViewById(R.id.textView_single_view);
    }
}
