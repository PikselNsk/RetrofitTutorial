package info.javaway.retrofittutorial.javaway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.javaway.retrofittutorial.R;
import info.javaway.retrofittutorial.javaway.api.UmoriliApi;
import info.javaway.retrofittutorial.javaway.controller.Controller;
import info.javaway.retrofittutorial.javaway.model.AnekdotModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private static UmoriliApi umoriliApi;
    RecyclerView recyclerView;
    List<AnekdotModel> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        umoriliApi = Controller.getApi();

        posts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PostsAdapter adapter = new PostsAdapter(posts);
        recyclerView.setAdapter(adapter);

        /* Пример вызова синхронного запроса. В главном потоке ТАБУ!
        try {
            Response response = umoriliApi.getData("bash", 50).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        umoriliApi.getData("new anekdot", 50).enqueue(new Callback<List<AnekdotModel>>() {
            @Override
            public void onResponse(Call<List<AnekdotModel>> call, Response<List<AnekdotModel>> response) {
                posts.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<AnekdotModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
