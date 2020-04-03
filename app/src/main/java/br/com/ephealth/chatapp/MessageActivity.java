package br.com.ephealth.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.circleImageViewProfile)
    CircleImageView circleImageViewProfile;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.buttonSend)
    ImageButton buttonSend;
    @BindView(R.id.textInputEditTextMessage)
    TextInputEditText textInputEditTextMessage;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        textInputLayout.setHint("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        intent = getIntent();
        userId = intent.getStringExtra(IntentParameters.userID);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(IFirebase.USERS).child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewUserName.setText(user.getUsername());
                if (user.getImageURL().equals(IFirebase.DEFAULT)) {
                    circleImageViewProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(circleImageViewProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(IFirebase.SENDER, sender);
        hashMap.put(IFirebase.RECEIVER, receiver);
        hashMap.put(IFirebase.MESSAGE, message);
        reference.child(IFirebase.CHATS).push().setValue(hashMap);

    }

    @OnClick(R.id.buttonSend)
    public void onViewClicked() {
        String message = textInputEditTextMessage.getText().toString();
        if (!message.isEmpty()) {
            sendMessage(firebaseUser.getUid(), userId, message);
        } else {
            Toasty.warning(this, R.string.cannotSendEmptyMessage, Toast.LENGTH_SHORT).show();
        }
        textInputEditTextMessage.setText("");
    }
}
