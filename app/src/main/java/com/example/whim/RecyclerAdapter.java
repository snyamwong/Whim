package com.example.whim;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private Fragment fragment;
    
    public RecyclerAdapter(Fragment fragment)
    {
        this.fragment = fragment;
    }
    
    private String[] titles = {"Chapter One",
            "Chapter Two",
            "Chapter Three",
            "Chapter Four",
            "Chapter Five",
            "Chapter Six",
            "Chapter Seven",
            "Chapter Eight"};

    private String[] details = {"Item one details",
            "Item two details", "Item three details",
            "Item four details", "Item file details",
            "Item six details", "Item seven details",
            "Item eight details"};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_favorite_card_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v, fragment);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(R.drawable.stock_ramen);
    }

    @Override
    public int getItemCount()
    {
        return titles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDetail;

        ViewHolder(View itemView, Fragment fragment)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(v ->
            {
                // PlaceFragment code here
                Fragment placeFragment = new PlaceFragment();

                assert fragment.getFragmentManager() != null;
                FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();

                transaction.hide(fragment);
                transaction.add(R.id.main_content, placeFragment);
                transaction.addToBackStack("FavoriteFragment");
                transaction.commit();
            });
        }
    }
}
