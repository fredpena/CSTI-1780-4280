package com.pucmm.csti.retrofit;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.*;
import com.pucmm.csti.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RetrofitActivity extends AppCompatActivity {

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, dateJsonDeserializer);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://137.184.110.89:7002")
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();

            Call<List<Userr>> userCall = retrofit.create(UserApiService.class).getAll();

            System.out.println("enqueue");
            userCall.enqueue(new Callback<List<Userr>>() {
                @Override
                public void onResponse(Call<List<Userr>> call, Response<List<Userr>> response) {
                    System.out.println("onResponse...");
                    response.body().forEach(obj -> {
                        System.out.println(obj.toString());
                    });
                }

                @Override
                public void onFailure(Call<List<Userr>> call, Throwable t) {
                    System.err.println("onFailure...");
                    t.printStackTrace();
                }
            });

        });

    }

    private final JsonDeserializer<Date> dateJsonDeserializer = (json, typeOfT, context) -> {
        try {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            final String strDate = dateFormat.format(new Date(json.getAsLong()));
            System.out.println(strDate);
            return new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    };

}