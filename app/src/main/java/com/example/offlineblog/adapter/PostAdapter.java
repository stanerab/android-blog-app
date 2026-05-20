package com.example.offlineblog.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlineblog.R;
import com.example.offlineblog.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Post post);
        void onItemLongClick();
    }

    private List<Post> posts;
    private final OnItemClickListener listener;
    private final List<Post> selectedPosts = new ArrayList<>();
    private boolean selectionMode = false;

    public PostAdapter(List<Post> posts, OnItemClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> newPosts) {
        this.posts = newPosts;
        selectedPosts.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    public List<Post> getSelectedPosts() {
        return selectedPosts;
    }

    public boolean isSelectionMode() {
        return selectionMode;
    }

    public void clearSelection() {
        selectedPosts.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPreview;

        PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPreview = itemView.findViewById(R.id.tvPreview);
        }

        void bind(Post post) {
            tvTitle.setText(post.getTitle());
            tvPreview.setText(post.getContent());

            itemView.setBackgroundColor(
                    selectedPosts.contains(post) ? Color.LTGRAY : Color.TRANSPARENT
            );

            itemView.setOnClickListener(v -> {
                if (selectionMode) {
                    toggleSelection(post);
                } else {
                    listener.onItemClick(post);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (!selectionMode) {
                    selectionMode = true;
                    listener.onItemLongClick();
                }
                toggleSelection(post);
                return true;
            });
        }

        void toggleSelection(Post post) {
            if (selectedPosts.contains(post)) {
                selectedPosts.remove(post);
            } else {
                selectedPosts.add(post);
            }
            notifyItemChanged(getAdapterPosition());
        }
    }
}
