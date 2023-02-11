package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import izarleydev.com.whatsapp.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnavegar (View view) {
        startActivity(new Intent(Login.this, Cadastro.class));
    }
}