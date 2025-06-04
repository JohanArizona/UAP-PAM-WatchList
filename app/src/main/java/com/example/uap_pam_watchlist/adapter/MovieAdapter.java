package com.example.uap_pam_watchlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uap_pam_watchlist.R;
import com.example.uap_pam_watchlist.model.Movie;
import com.example.uap_pam_watchlist.ui.AddEditMovieActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> movieList;
    private Context context;
    private DatabaseReference mDatabase;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.mDatabase = FirebaseDatabase.getInstance().getReference("movies")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvStatus.setText(movie.getStatus());
        holder.ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditMovieActivity.class);
            intent.putExtra("MOVIE_ID", movie.getId());
            intent.putExtra("MOVIE_TITLE", movie.getTitle());
            intent.putExtra("MOVIE_STATUS", movie.getStatus());
            context.startActivity(intent);
        });
        holder.ivDelete.setOnClickListener(v -> {
            mDatabase.child(movie.getId()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Film dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus film", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() { return movieList.size(); }

    public void setMovies(ArrayList<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvStatus;
        ImageView ivEdit, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvStatus = itemView.findViewById(R.id.tv_movie_status);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}