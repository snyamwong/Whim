package com.example.whim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

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

        ArrayList<Business> businesses = yelpFusion.getBusinesses();
        String imageUrl = businesses.get(0).getImageUrl();
        String restName = businesses.get(0).getName();

        TextView restNameText = (TextView) view.findViewById(R.id.restaurant_name);
        restNameText.setText(restName);

        new DownloadImageFromInternet((ImageView) view.findViewById(R.id.restaurant_photo)).execute(imageUrl);
        return view;
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
