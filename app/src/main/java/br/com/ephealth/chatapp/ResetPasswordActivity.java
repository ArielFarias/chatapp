package br.com.ephealth.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;
    @BindView(R.id.buttonReset)
    Button buttonReset;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.editTextSendEmail)
    EditText editTextSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
    }


    @OnClick(R.id.buttonReset)
    public void onViewClicked() {
        String email = editTextSendEmail.getText().toString();

        if (email.isEmpty())
            Toasty.warning(ResetPasswordActivity.this, "Por favor preencha o campo corretamente", Toast.LENGTH_SHORT).show();
        else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toasty.success(ResetPasswordActivity.this, "Email enviado com sucesso", Toasty.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    } else {
                        String error = task.getException().getMessage();
                        Toasty.error(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
