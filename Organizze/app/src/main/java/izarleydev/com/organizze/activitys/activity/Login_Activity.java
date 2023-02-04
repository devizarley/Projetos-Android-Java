package izarleydev.com.organizze.activitys.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.activitys.model.Usuario;

public class Login_Activity extends AppCompatActivity {
    private EditText inputEmail, inputSenha;
    private Button buttonSubmit;
    private Usuario usuario;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputLoginEmail);
        inputSenha = findViewById(R.id.inputLoginSenha);
        buttonSubmit = findViewById(R.id.buttonLoginSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textemail = inputEmail.getText().toString();
                String textsenha = inputSenha.getText().toString();

                if ( !textemail.isEmpty() ) {
                    if ( !textsenha.isEmpty() ) {

                        usuario = new Usuario();
                        usuario.setEmail(textemail);
                        usuario.setSenha(textsenha);
                        validarLogin();
                    }else
                        Toast.makeText(Login_Activity.this, "Preencha o campo de Senha.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Login_Activity.this, "Preencha o campo de Email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void validarLogin() {
        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    openActivityPrincipal();
                }else {

                    String exception = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exception = "Usuário não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Email e senha não correspondem a um usuário cadastrado.";

                    }catch (Exception e){
                        exception = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(Login_Activity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openActivityPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}