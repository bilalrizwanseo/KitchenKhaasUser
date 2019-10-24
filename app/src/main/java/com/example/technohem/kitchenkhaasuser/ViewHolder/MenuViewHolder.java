package com.example.technohem.kitchenkhaasuser.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.technohem.kitchenkhaasuser.Interface.ItemClickListener;
import com.example.technohem.kitchenkhaasuser.R;

// Recycler View code

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView, imageView1;
    public ItemClickListener listener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        imageView1 = (ImageView) itemView.findViewById(R.id.gallery_image);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v ,getAdapterPosition(), false);
    }

}
