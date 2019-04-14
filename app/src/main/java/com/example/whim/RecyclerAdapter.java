package com.example.whim;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.fusion.client.models.Business;

import java.io.InputStream;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private WhimDatabaseHelper whimDatabaseHelper;

    private List<Business> businesses;

    private Fragment fragment;

    public RecyclerAdapter(Fragment fragment)
    {
        this.fragment = fragment;

        whimDatabaseHelper = new WhimDatabaseHelper(this.fragment.getContext());

        businesses = whimDatabaseHelper.getAllData();

        whimDatabaseHelper.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_favorite_card_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        ImageView itemImage = viewHolder.itemImage;

        viewHolder.itemTitle.setText(businesses.get(i).getName());
        viewHolder.itemDetail.setText(businesses.get(i).getLocation().getAddress1());
        viewHolder.id = businesses.get(i).getId();
        new DownloadImageFromInternet(itemImage).execute(businesses.get(i).getImageUrl());
        itemImage.getLayoutParams().width = 600;
        itemImage.getLayoutParams().height = 600;
    }

    @Override
    public int getItemCount()
    {
        return businesses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDetail;
        String id;

        ViewHolder(View itemView)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(v ->
            {
                Cursor data = whimDatabaseHelper.getRestaurant(id);
                try
                {
                    if (data.moveToFirst())
                    {
                        String businessID = data.getString(data.getColumnIndex("ID"));

                        // deletes from the internal database
                        whimDatabaseHelper.deleteRestaurant(businessID);

                        // deletes from the current memory
                        for (Business b : businesses)
                        {
                            if (b.getId().equals(businessID))
                            {
                                businesses.remove(b);

                                break;
                            }
                        }
                    }
                }
                finally
                {
                    if (!data.isClosed())
                    {
                        data.close();
                    }
                    whimDatabaseHelper.close();
                }

                // refreshes the RecyclerAdapter
                notifyDataSetChanged();
            });
        }
    }

    class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>
    {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView)
        {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls)
        {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try
            {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            }
            catch (Exception e)
            {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result)
        {
            imageView.setImageBitmap(result);
        }
    }
}
