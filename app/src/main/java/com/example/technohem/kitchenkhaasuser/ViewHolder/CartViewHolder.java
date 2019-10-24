package com.example.technohem.kitchenkhaasuser.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.technohem.kitchenkhaasuser.Interface.ItemClickListener;
import com.example.technohem.kitchenkhaasuser.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName, txtMenuPrice , txtMenuGuests;
    public ImageView menuImage;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.cart_menu_name);
        txtMenuPrice = itemView.findViewById(R.id.cart_menu_Price);
        txtMenuGuests = itemView.findViewById(R.id.cart_menu_guests);
        menuImage = (ImageView) itemView.findViewById(R.id.cart_menu_image);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false );
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
