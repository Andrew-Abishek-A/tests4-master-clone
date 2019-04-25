package com.example.tests4;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChatActivity extends AppCompatActivity {

    static final int RC_PHOTO_PICKER = 1;

    private Button sendBtn;
    private EditText messageTxt;
    private RecyclerView messagesList;
    private ChatMessageAdapter adapter;
    private ImageButton imageBtn;
    private TextView usernameTxt;
    private View loginBtn;
    private View logoutBtn;
    Uri url;
    CollectionReference superintendants;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseApp app;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private FirebaseStorage instance = FirebaseStorage.getInstance();
    StorageReference store=instance.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        messageTxt = (EditText) findViewById(R.id.messageTxt);
        messagesList = (RecyclerView) findViewById(R.id.messagesList);
        imageBtn = (ImageButton) findViewById(R.id.imageBtn);
        usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        // TODO: add authentication
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent,RC_PHOTO_PICKER);
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String username = mAuth.getCurrentUser().getEmail();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesList.setHasFixedSize(false);
        messagesList.setLayoutManager(layoutManager);


        // Show an image picker when the user wants to upload an imasge
//        imageBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
//            }
//        });
//        // Show a popup when the user asks to sign in
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                LoginDialog.showLoginPrompt(MainActivity.this, app);
//            }
//        });
//        // Allow the user to sign out
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                auth.signOut();
//            }
//        });
        adapter = new ChatMessageAdapter(this);
        messagesList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messagesList.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChatMessage chat = new ChatMessage(username, messageTxt.getText().toString());
                // Push the chat message to the database

                db.collection("chats").add(chat)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("CHAT", username + " " + messageTxt.getText().toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("CHAT", e.toString());
                            }
                        });

                messageTxt.setText("");
            }
        });
    }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();

                // Get a reference to the location where we'll store our photos
                final StorageReference imageref=store.child("images");

                // Get a reference to store file at chat_photos/<FILENAME>
                // Upload file to Firebase Storage
                assert selectedImageUri!=null;
                imageref.putFile(selectedImageUri)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // When the image has successfully uploaded, we get its download URL
                                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        url=uri;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                                // Set the download URL to the message box, so that the user can send it to the database
                                messageTxt.setText(url.toString());
                            }
                        });
            }
        }

    }
