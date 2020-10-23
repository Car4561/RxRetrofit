package com.carlos.rxretrofit.adapter;

import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.rxretrofit.R;
import com.carlos.rxretrofit.model.GitHubRepo;

import java.util.List;

public class RepositoryAdapter  extends  RecyclerView.Adapter<RepositoryAdapter.ViewHolder>{

    private List<GitHubRepo> repos;

    public RepositoryAdapter(List<GitHubRepo> repos){
        this.repos = repos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GitHubRepo repo = repos.get(position);
        holder.tvLenguaje.setText(repo.getLanguage());
        holder.tvRepositorio.setText(repo.getName());
        holder.tvStar.setText(String.valueOf(repo.getStargazers_count()));

    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void setData(List<GitHubRepo> repos){
        this.repos = repos;
        notifyDataSetChanged();
    }
    public void setData(){
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRepositorio;
        TextView tvLenguaje;
        TextView tvStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRepositorio = itemView.findViewById(R.id.tvRepositorio);
            tvLenguaje = itemView.findViewById(R.id.tvLenguaje);
            tvStar = itemView.findViewById(R.id.tvStars);
        }
    }
}
