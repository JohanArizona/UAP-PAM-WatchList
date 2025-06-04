package com.example.uap_pam_watchlist.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uap_pam_watchlist.R;
import com.example.uap_pam_watchlist.adapter.MovieAdapter;
import com.example.uap_pam_watchlist.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button btnAddMovie, btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference("movies").child(mAuth.getCurrentUser().getUid());

        recyclerView = findViewById(R.id.rv_movies);
        btnAddMovie = findViewById(R.id.btn_add_movie);
        btnLogout = findViewById(R.id.btn_logout);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        btnAddMovie.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddEditMovieActivity.class)));
        btnLogout.setOnClickListener(v -> {
            // Hapus state login
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        loadMovies();
    }

    private void loadMovies() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    movie.setId(snapshot.getKey());
                    movieList.add(movie);
                }
                movieAdapter.setMovies(movieList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}