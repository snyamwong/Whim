package com.example.whim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.SearchResponse;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // API Key
        final String apiKey = "OiUsQEd9t6xdnrCN1DNUfwy4uLK_HNZ2n6e_hqKJUsV8qlY8TSRxkM_L5yfMAA--4uJqfCvxNc0RlM65jAnfvSzCETfK9woIYW9fxLq9xM5ZBAQA_CcgIouyvPpIXHYx";

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // Yelp API
                try
                {
                    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
                    YelpFusionApi yelpFusionApi = apiFactory.createAPI(apiKey);

                    Map<String, String> params = new HashMap<>();
                    params.put("term", "indian food");

                    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
                    Response<SearchResponse> response = call.execute();

                    System.out.println(response.body());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
