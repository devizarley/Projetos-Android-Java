package izarleydev.com.whatsapp.Activitys.activitys;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.Base64;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class Cadastro extends AppCompatActivity {

    TextInputEditText inputEmail, inputName, inputSenha;
    private DatabaseReference ref = ConfigFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfigFirebase.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputLoginEmail);
        inputSenha = findViewById(R.id.inputLoginSenha);

    }

    public void cadastrarUsuario (Usuario usuario) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    try {
                        String idUsuario = Base64.codBase64(usuario.getEmail());
                        usuario.setId(idUsuario);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e ){
                        excecao = "Esta conta já foi cadastrada";
                    }catch (Exception e ){
                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(Cadastro.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    
    public void validarCadastro (View view) {

        String valueinputName = inputName.getText().toString();
        String valueinputEmail = inputEmail.getText().toString();
        String valueinputSenha= inputSenha.getText().toString();

        if (!valueinputName.isEmpty()){
            if (!valueinputEmail.isEmpty()){
                if (!valueinputSenha.isEmpty()){

                    Usuario usuario = new Usuario();
                    usuario.setNome(valueinputName);
                    usuario.setEmail(valueinputEmail);
                    usuario.setSenha(valueinputSenha);

                    cadastrarUsuario(usuario);
                }else {
                    Toast.makeText(this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Preencha o email!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
        }

    }
}