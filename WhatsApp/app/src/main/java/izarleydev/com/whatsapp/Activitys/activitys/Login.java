package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class Login extends AppCompatActivity {

    private TextInputEditText fieldEmail, fieldSenha;
    private FirebaseAuth auth = ConfigFirebase.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fieldEmail = findViewById(R.id.inputLoginEmail);
        fieldSenha = findViewById(R.id.inputLoginSenha);
    }

    public void loginUser (Usuario usuario){
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(Login.this, MainActivity.class));

                }else {
                    Toast.makeText(Login.this, "Erro ao autenticar usuario!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validateFildUser (View view){
        String valueEmail = fieldEmail.getText().toString();
        String valueSenha = fieldSenha.getText().toString();

        if (!valueEmail.isEmpty()){
            if (!valueSenha.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setEmail(valueEmail);
                usuario.setSenha(valueSenha);

                loginUser(usuario);

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