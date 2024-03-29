package com.example.whim;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;

/**
 * Utility class for handling Yelp API and handling request and responses to it
 */
public class YelpFusion extends Thread
{
    //Insert API Key here from txt file
    private final String apiKey = "";

    private Map<String, String> yelpFields;
    private Map<String, String> miscFields;

    private ArrayList<Business> businesses;

    YelpFusion(Map<String, String> yelpFields, Map<String, String> miscFields)
    {
        this.businesses = new ArrayList<>();

        this.yelpFields = yelpFields;

        this.miscFields = miscFields;
    }

    @Override
    public void run()
    {
        try
        {
            YelpFusionApi yelpFusionApi = new YelpFusionApiFactory().createAPI(apiKey);

            String transaction = "";

            if (Objects.requireNonNull(miscFields.get("order_delivery")).equals("true"))
            {
                transaction += "delivery";
            }
            if (Objects.requireNonNull(miscFields.get("order_takeout")).equals("true"))
            {
                transaction += "takeout";
            }

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(yelpFields);

            SearchResponse searchResponse = call.execute().body();

            this.businesses = searchResponse.getBusinesses();

            Collections.shuffle(businesses);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    ArrayList<Business> getBusinesses()
    {
        return businesses;
    }
}


