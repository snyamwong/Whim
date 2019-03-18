package com.example.whim;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import retrofit2.Call;

/**
 * Utility class for handling Yelp API and handling request and responses to it
 */
public class YelpFusion extends Thread
{
    // TODO relocate the API Key somewhere else
    private String apiKey = "OiUsQEd9t6xdnrCN1DNUfwy4uLK_HNZ2n6e_hqKJUsV8qlY8TSRxkM_L5yfMAA--4uJqfCvxNc0RlM65jAnfvSzCETfK9woIYW9fxLq9xM5ZBAQA_CcgIouyvPpIXHYx";

    private YelpFusionApi yelpFusionApi;

    @Override
    public void run()
    {
        // Yelp API
        try
        {
            this.yelpFusionApi = new YelpFusionApiFactory().createAPI(apiKey);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Params is obtained via SearchFragment and FilterFragment
     *
     * @param params
     * @return businesses
     */
    public ArrayList<Business> getBusinesses(Map<String, String> params)
    {
        try
        {
            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);

            SearchResponse searchResponse = call.execute().body();

            ArrayList<Business> businesses = searchResponse.getBusinesses();

            Collections.shuffle(businesses);

            return businesses;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}


