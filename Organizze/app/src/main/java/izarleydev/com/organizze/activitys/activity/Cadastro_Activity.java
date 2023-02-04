package izarleydev.com.organizze.activitys.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.Base64Custom;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.activitys.model.Usuario;


public class Cadastro_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText cInputName, cInputEmail, cInputSenha;
    private Button cButtonSubmit;
    private Usuario usuario;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cInputName = findViewById(R.id.cInputName);
        cInputEmail = findViewById(R.id.cInputEmail);
        cInputSenha = findViewById(R.id.cInputSenha);
        cButtonSubmit = findViewById(R.id.cButtonSubmit);

        cButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textname = cInputName.getText().toString();
                String textemail = cInputEmail.getText().toString();
                String textsenha = cInputSenha.getText().toString();

                if ( !textname.isEmpty() ) {
                    if ( !textemail.isEmpty() ) {
                        if ( !textsenha.isEmpty() ) {

                            usuario = new Usuario();
                            usuario.setNome(textname);
                            usuario.setEmail(textemail);
                            usuario.setSenha(textsenha);

                            userRegistration();
                        }else {
                            Toast.makeText(Cadastro_Activity.this, "Preencha o campo de Senha.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Cadastro_Activity.this, "Preencha o campo de Email.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Cadastro_Activity.this, "Preencha o campo de Nome.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void userRegistration (){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();

                    finish();
                }else {

                    String exception = "";

                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        exception = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e ){
                        exception = "Esta conta já foi cadastada";
                    }catch (Exception e){
                        exception = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(Cadastro_Activity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}