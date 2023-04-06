package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, senhaLogin;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //configurações gerais
        emailLogin = findViewById(R.id.inputEmailLogin);
        senhaLogin = findViewById(R.id.inputSenhaLogin);

        //configurações firebase
        databaseReference = ConfigFirebase.getFirebaseDatabase();
        firebaseAuth = ConfigFirebase.getAuth();
    }

    public void login (Usuario usuario) {
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuario não está cadastrado.";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail e senha não correspondem à uma conta cadastrada.";
                    }catch (Exception e) {
                        excecao = "Erro ao logar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin (View view){

        String valueEmailLogin = emailLogin.getText().toString();
        String valueSenhaLogin = senhaLogin.getText().toString();

        if (!valueEmailLogin.isEmpty()){
            if (!valueSenhaLogin.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setEmail(valueEmailLogin);
                usuario.setSenha(valueSenhaLogin);

                login(usuario);

            }else {
                Toast.makeText(this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o email!", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void textCadastrar (View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
}