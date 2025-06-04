package com.example.uap_pam_watchlist.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uap_pam_watchlist.R;
import com.example.uap_pam_watchlist.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEditMovieActivity extends AppCompatActivity {
    private EditText etTitle;
    private Spinner spinnerStatus;
    private Button btnSave, btnDelete;
    private DatabaseReference mDatabase;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        mDatabase = FirebaseDatabase.getInstance().getReference("movies")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        etTitle = findViewById(R.id.et_movie_title);
        spinnerStatus = findViewById(R.id.spinner_status);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        movieId = getIntent().getStringExtra("MOVIE_ID");
        if (movieId != null) {
            etTitle.setText(getIntent().getStringExtra("MOVIE_TITLE"));
            spinnerStatus.setSelection(adapter.getPosition(getIntent().getStringExtra("MOVIE_STATUS")));
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> saveMovie());
        btnDelete.setOnClickListener(v -> deleteMovie());
    }

    private void saveMovie() {
        String title = etTitle.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Judul film harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Movie movie = new Movie(title, status);
        if (movieId == null) {
            movieId = mDatabase.push().getKey();
        }
        mDatabase.child(movieId).setValue(movie)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(AddEditMovieActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteMovie() {
        mDatabase.child(movieId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(AddEditMovieActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}