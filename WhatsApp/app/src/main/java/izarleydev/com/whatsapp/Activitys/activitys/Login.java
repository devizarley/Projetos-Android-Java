package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class Login extends AppCompatActivity {

    private TextInputEditText fieldEmail, fieldSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fieldEmail = findViewById(R.id.inputLoginEmail);
        fieldSenha = findViewById(R.id.inputLoginSenha);
    }

    public void loginUser (View view){
        String valueEmail = fieldEmail.getText().toString();
        String valueSenha = fieldSenha.getText().toString();

        if (!valueEmail.isEmpty()){
            if (!valueSenha.isEmpty()){



            }else {
                Toast.makeText(this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o email!", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnavegar (View view) {
        startActivity(new Intent(Login.this, Cadastro.class));
    }
}