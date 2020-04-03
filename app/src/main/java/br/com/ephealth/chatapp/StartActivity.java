package br.com.ephealth.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;

    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Checa se o usuario é nulo
        if (firebaseUser != null) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bem vindo!");


    }

    @OnClick({R.id.buttonLogin, R.id.buttonRegister})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.buttonLogin:
                intent.setClass(StartActivity.this, LoginActivity.class);
                break;
            case R.id.buttonRegister:
                intent.setClass(StartActivity.this, RegisterActivity.class);
                break;
        }

        startActivity(intent);

    }
}
