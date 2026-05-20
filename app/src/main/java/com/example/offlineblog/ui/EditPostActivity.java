package com.example.offlineblog.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.offlineblog.R;
import com.example.offlineblog.db.PostsDao;
import com.example.offlineblog.model.Post;

public class EditPostActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private ImageView imgPreview;
    private Button btnSave, btnPickImage;

    private PostsDao postsDao;

    private Uri selectedImageUri;
    private long postId = -1; // -1 = create, otherwise edit

    // ✅ OpenDocument picker
    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri == null) return;

                selectedImageUri = uri;

                // ✅ Try to persist permission (can throw on some devices)
                try {
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                } catch (SecurityException e) {
                    // Don't crash. Image may still work for this session.
                }

                // ✅ Safe preview
                try {
                    imgPreview.setVisibility(View.VISIBLE);
                    imgPreview.setImageURI(uri);
                } catch (Exception e) {
                    imgPreview.setVisibility(View.GONE);
                    Toast.makeText(this, "Image preview failed", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Views
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        imgPreview = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnPickImage = findViewById(R.id.btnPickImage);

        postsDao = new PostsDao(this);

        // Edit mode?
        postId = getIntent().getLongExtra("post_id", -1);

        if (postId != -1) {
            Post post = postsDao.getById(postId);
            if (post != null) {
                etTitle.setText(post.getTitle());
                etContent.setText(post.getContent());

                // ✅ Safe load existing image (old posts included)
                if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
                    try {
                        selectedImageUri = Uri.parse(post.getImageUri());
                        imgPreview.setVisibility(View.VISIBLE);
                        imgPreview.setImageURI(selectedImageUri);
                    } catch (Exception e) {
                        imgPreview.setVisibility(View.GONE);
                    }
                } else {
                    imgPreview.setVisibility(View.GONE);
                }
            }
        } else {
            imgPreview.setVisibility(View.GONE);
        }

        // Pick image
        btnPickImage.setOnClickListener(v -> imagePicker.launch(new String[]{"image/*"}));

        // Save
        btnSave.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content required", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUploadStatus(0);

        if (selectedImageUri != null) {
            post.setImageUri(selectedImageUri.toString());
        } else {
            post.setImageUri(null);
        }

        if (postId == -1) {
            postsDao.insert(post);
            Toast.makeText(this, "Post saved", Toast.LENGTH_SHORT).show();
        } else {
            post.setId(postId);
            postsDao.update(post);
            Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
