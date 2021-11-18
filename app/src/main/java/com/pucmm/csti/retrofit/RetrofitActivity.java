package com.pucmm.csti.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pucmm.csti.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://curly-chipmunk-76.loca.lt")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Call<List<Userr>> userCall = retrofit.create(UserApiService.class).getAll();

        System.out.println("enqueue");
        userCall.enqueue(new Callback<List<Userr>>() {
            @Override
            public void onResponse(Call<List<Userr>> call, Response<List<Userr>> response) {
                response.body().forEach(obj -> {
                    System.out.println(obj.getFirstName());
                });
            }

            @Override
            public void onFailure(Call<List<Userr>> call, Throwable t) {
                System.err.println(t.getLocalizedMessage());
            }
        });


//        Post post = new Post();
//        post.setUserId(1);
//        post.setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
//        post.setText("quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto");
//
//        Call<Post> postCall = retrofit.create(APIService.class).create(post);
//
//        postCall.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                System.err.println("code: " + response.code());
//                System.err.println(response.body().getText());
//
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                System.err.println(t.getLocalizedMessage());
//            }
//        });

//        Call<List<Comment>> listCall = retrofit.create(APIService.class).getCommentsByPost(2);
//        listCall.enqueue(new Callback<List<Comment>>() {
//            @Override
//            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//
//                response.body().forEach(obj -> {
//                    System.out.println(obj.getName());
//                });
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Comment>> call, Throwable t) {
//                System.err.println(t.getLocalizedMessage());
//
//            }
//        });

    }
}