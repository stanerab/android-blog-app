package com.example.offlineblog.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlineblog.R;
import com.example.offlineblog.db.PostsDao;
import com.example.offlineblog.model.Post;

import java.io.InputStream;

public class ViewPostActivity extends AppCompatActivity {

    private long postId;
    private PostsDao postsDao;

    private TextView tvTitle, tvContent;
    private ImageView imgViewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        // Views
        tvTitle = findViewById(R.id.tvViewTitle);
        tvContent = findViewById(R.id.tvViewContent);
        imgViewPost = findViewById(R.id.imgViewPost);

        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnShare = findViewById(R.id.btnShare);

        postsDao = new PostsDao(this);

        // Get post ID
        postId = getIntent().getLongExtra("post_id", -1);
        if (postId == -1) {
            finish();
            return;
        }

        loadPostSafely();

        // Edit
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPostActivity.class);
            intent.putExtra("post_id", postId);
            startActivity(intent);
        });

        // Delete
        btnDelete.setOnClickListener(v -> deletePost());

        // Share via email
        btnShare.setOnClickListener(v -> shareViaEmail());
    }

    /**
     * Safely loads post + image (prevents crashes)
     */
    private void loadPostSafely() {
        Post post = postsDao.getById(postId);

        if (post == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(post.getTitle());
        tvContent.setText(post.getContent());

        // Reset image view first
        imgViewPost.setImageDrawable(null);
        imgViewPost.setVisibility(View.GONE);

        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(post.getImageUri());

                // Validate URI before showing
                InputStream stream = getContentResolver().openInputStream(imageUri);
                if (stream != null) {
                    stream.close();
                    imgViewPost.setVisibility(View.VISIBLE);
                    imgViewPost.setImageURI(imageUri);
                }

            } catch (Exception e) {
                imgViewPost.setVisibility(View.GONE);
                Toast.makeText(this, "Image could not be loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deletePost() {
        postsDao.delete(postId);
        Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Share post via EMAIL (text + optional image)
     */
    private void shareViaEmail() {
        Post post = postsDao.getById(postId);
        if (post == null) return;

        String title = post.getTitle();
        String body = post.getTitle() + "\n\n" + post.getContent();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
            Uri imageUri = Uri.parse(post.getImageUri());
            emailIntent.setType("image/*");
            emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            emailIntent.setType("text/plain");
        }

        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }
}
