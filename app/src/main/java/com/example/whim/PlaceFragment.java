package com.example.whim;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.fusion.client.models.Business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaceFragment extends Fragment
{

    private Map<String, String> yelpFields, miscFields;
    private WhimDatabaseHelper whimDatabaseHelper;
    private int restCounter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        String prevFragment = getCallerFragment();

        if (prevFragment.equals("FilterFragment"))
        {
            restCounter = 0;
            yelpFields = new HashMap<>();
            miscFields = new HashMap<>();

            Bundle bundle = this.getArguments();

            if (bundle != null)
            {
                for (String key : bundle.keySet())
                {
                    if (!key.equals("star") && !key.equals("order_delivery") && !key.equals("order_takeout"))
                    {
                        yelpFields.put(key, Objects.requireNonNull(bundle.getString(key)));

                        Log.v("PlaceFragment", String.format("%s : %s", key, bundle.getString(key)));
                    }
                    else
                    {
                        miscFields.put(key, Objects.requireNonNull(bundle.getString(key)));
                    }
                }
            }

            YelpFusion yelpFusion = new YelpFusion(yelpFields, miscFields);

            try
            {
                yelpFusion.start();

                yelpFusion.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            ImageButton favorite = (ImageButton) view.findViewById(R.id.favorite_restaurant);
            ImageButton unfavorite = (ImageButton) view.findViewById(R.id.unfavorite_restaurant);
            ImageButton reroll = (ImageButton) view.findViewById(R.id.new_restaurant);
            TextView restNameText = (TextView) view.findViewById(R.id.restaurant_name);
            TextView restAddressText = (TextView) view.findViewById(R.id.restaurant_address);
            Button returnHome = (Button) view.findViewById(R.id.return_home);

            whimDatabaseHelper = new WhimDatabaseHelper(PlaceFragment.this.getActivity());
            ArrayList<Business> businesses = yelpFusion.getBusinesses();
            if (businesses.size() > 0)
            {
                String imageUrl = businesses.get(0).getImageUrl();
                String restName = businesses.get(0).getName().replaceAll("'", "");
                String restAddress = businesses.get(0).getLocation().getAddress1().replaceAll("'", "");

                restNameText.setText(restName);
                restAddressText.setText(restAddress);

                new DownloadImageFromInternet((ImageView) view.findViewById(R.id.restaurant_photo)).execute(imageUrl);
                ImageView restPhoto = (ImageView) view.findViewById(R.id.restaurant_photo);
                restPhoto.getLayoutParams().width = 700;
                restPhoto.getLayoutParams().height = 700;

                final Cursor data = whimDatabaseHelper.getRestaurant(businesses.get(0).getId());
                String itemId;
                try
                {
                    itemId = "";
                    while (data.moveToNext())
                    {
                        itemId = data.getString(data.getColumnIndex("ID"));
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

                if (itemId.equals(""))
                {
                    unfavorite.setVisibility(View.INVISIBLE);
                    favorite.setVisibility(View.VISIBLE);
                }
                else
                {
                    favorite.setVisibility(View.INVISIBLE);
                    unfavorite.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                restNameText.setText("No Restaurants Found");
                favorite.setVisibility(View.INVISIBLE);
                unfavorite.setVisibility(View.INVISIBLE);
                reroll.setVisibility(View.INVISIBLE);
            }

            favorite.setOnClickListener(v ->
            {
                try
                {
                    whimDatabaseHelper.insertData(businesses.get(restCounter));
                }
                finally
                {
                    whimDatabaseHelper.close();
                }
                favorite.setVisibility(View.INVISIBLE);
                unfavorite.setVisibility(View.VISIBLE);
            });

            whimDatabaseHelper = new WhimDatabaseHelper(PlaceFragment.this.getActivity());
            unfavorite.setOnClickListener(v ->
            {
                String restName = businesses.get(restCounter).getName().replaceAll("'", "");
                Cursor data = whimDatabaseHelper.getRestaurant(businesses.get(restCounter).getId());
                try
                {
                    if (data.moveToFirst())
                    {
                        whimDatabaseHelper.deleteRestaurant(data.getString(data.getColumnIndex("ID")));
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
                favorite.setVisibility(View.VISIBLE);
                unfavorite.setVisibility(View.INVISIBLE);
            });

            whimDatabaseHelper = new WhimDatabaseHelper(PlaceFragment.this.getActivity());
            reroll.setOnClickListener(v ->
            {
                restCounter++;
                if (restCounter >= businesses.size())
                {
                    restCounter = 0;
                }
                String imageUrl = businesses.get(restCounter).getImageUrl();
                String restName = businesses.get(restCounter).getName().replaceAll("'", "");
                String restAddress = businesses.get(restCounter).getLocation().getAddress1().replaceAll("'", "");
                restNameText.setText(restName);
                restAddressText.setText(restAddress);
                new DownloadImageFromInternet((ImageView) view.findViewById(R.id.restaurant_photo)).execute(imageUrl);

                final Cursor data = whimDatabaseHelper.getRestaurant(businesses.get(restCounter).getId());
                String itemId;
                try
                {
                    itemId = "";
                    while (data.moveToNext())
                    {
                        itemId = data.getString(data.getColumnIndex("ID"));
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

                if (itemId.equals(""))
                {
                    unfavorite.setVisibility(View.INVISIBLE);
                    favorite.setVisibility(View.VISIBLE);
                }
                else
                {
                    favorite.setVisibility(View.INVISIBLE);
                    unfavorite.setVisibility(View.VISIBLE);
                }
            });

            returnHome.setOnClickListener(v ->
            {
                MainActivity mainActivity = (MainActivity) PlaceFragment.this.getActivity();
                mainActivity.showSupportActionBar();
                mainActivity.showTabLayout();

                Fragment fragment = new SearchFragment();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.hide(PlaceFragment.this);
                transaction.add(R.id.main_content, fragment);
                transaction.commit();
            });
        }

        return view;
    }

    private String getCallerFragment()
    {
        assert getFragmentManager() != null;

        FragmentManager fm = getFragmentManager();

        int count = fm.getBackStackEntryCount();

        return fm.getBackStackEntryAt(count - 1).getName();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>
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