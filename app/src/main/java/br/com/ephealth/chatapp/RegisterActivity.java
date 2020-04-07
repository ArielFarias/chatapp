package br.com.ephealth.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.IUser;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
    }

    private void register(final String username, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference(IFirebase.USERS).child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(IUser.ID, userId);
                        hashMap.put(IUser.USERNAME, username);
                        hashMap.put(IUser.IMAGE_URL, IFirebase.DEFAULT);
                        hashMap.put(IUser.STATUS, IUser.OFFLINE);
                        hashMap.put(IUser.NORMALIZED_NAME, GeneralUtils.normalizedName(username));

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed registering with this email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.buttonRegister)
    public void onViewClicked() {
        if (validateField(username) || validateField(password) || validateField(email)) {
            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

        } else if (password.getText().toString().length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password must have at last 6 characters", Toast.LENGTH_SHORT).show();

        } else {
            register(username.getText().toString(), email.getText().toString(), password.getText().toString());
        }
    }

    private boolean validateField(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }
}
