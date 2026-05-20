package com.example.offlineblog.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlineblog.R;
import com.example.offlineblog.adapter.PostAdapter;
import com.example.offlineblog.db.PostsDao;
import com.example.offlineblog.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private PostsDao postsDao;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 🔹 Apply saved dark/light mode BEFORE loading UI
        SharedPreferences prefs =
                getSharedPreferences("settings", MODE_PRIVATE);

        boolean isDark = prefs.getBoolean("dark_mode", false);

        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views
        searchView = findViewById(R.id.searchPosts);
        recyclerView = findViewById(R.id.recyclerPosts);
        fabAdd = findViewById(R.id.fabAddPost);

        // FAB → Add new post
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PostListActivity.this,
                    EditPostActivity.class
            );
            startActivity(intent);
        });

        // RecyclerView
        postsDao = new PostsDao(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Search logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPosts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadPosts();
                } else {
                    searchPosts(newText);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
    }

    // Load all posts
    private void loadPosts() {
        List<Post> posts = postsDao.getAll();
        setupAdapter(posts);
    }

    // Search posts
    private void searchPosts(String keyword) {
        List<Post> results = postsDao.search(keyword);
        setupAdapter(results);
    }

    // Adapter setup
    private void setupAdapter(List<Post> posts) {
        if (adapter == null) {
            adapter = new PostAdapter(posts, new PostAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Post post) {
                    openViewPost(post.getId());
                }

                @Override
                public void onItemLongClick() {
                    // handled inside adapter
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setPosts(posts);
        }
    }

    // Open single post
    private void openViewPost(long postId) {
        Intent intent = new Intent(this, ViewPostActivity.class);
        intent.putExtra("post_id", postId);
        startActivity(intent);
    }

    // ===== TOOLBAR MENU =====

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences prefs =
                getSharedPreferences("settings", MODE_PRIVATE);

        // Dark mode toggle
        if (item.getItemId() == R.id.action_dark_mode) {

            boolean isDark = prefs.getBoolean("dark_mode", false);
            boolean newMode = !isDark;

            prefs.edit().putBoolean("dark_mode", newMode).apply();

            AppCompatDelegate.setDefaultNightMode(
                    newMode ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
            return true;
        }

        // 🗑 Delete selected posts
        if (item.getItemId() == R.id.action_delete) {

            if (adapter == null || adapter.getSelectedPosts().isEmpty()) {
                Toast.makeText(this, "No posts selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            deleteSelectedPosts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedPosts() {
        List<Post> selected = adapter.getSelectedPosts();

        for (Post post : selected) {
            postsDao.delete(post.getId());
        }

        adapter.clearSelection();
        loadPosts();
    }
}
