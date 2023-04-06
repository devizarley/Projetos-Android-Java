package izarleydev.com.instagram.activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.Base64;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText inputEmailCadastro, inputNomeCadastro, inputCSenhaCadastro;
    private Button buttonCadastro;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Configurações gerais
        inputEmailCadastro = findViewById(R.id.inputEmailCadastro);
        inputNomeCadastro = findViewById(R.id.inputNomeCadastro);
        inputCSenhaCadastro = findViewById(R.id.inputSenhaCadastro);

        //Configurações Firebase
        firebaseAuth = ConfigFirebase.getAuth();


    }

    public void cadastrarUsuario(Usuario usuario){
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String idUser = Base64.codBase64(usuario.getEmail());
                    usuario.setId(idUser);
                    usuario.Salvar();

                    finish();
                }else {

                    //Tratamento para email e senha

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
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarCadastro (View view){

        String valueEmailInput = inputEmailCadastro.getText().toString();
        String valueNomeInput = inputNomeCadastro.getText().toString();
        String valueSenhaInput = inputCSenhaCadastro.getText().toString();

        if (!valueEmailInput.isEmpty()){
            if (!valueNomeInput.isEmpty()){
                if (!valueSenhaInput.isEmpty()){


                    Usuario usuario = new Usuario();
                    usuario.setEmail(valueEmailInput);
                    usuario.setName(valueNomeInput);
                    usuario.setSenha(valueSenhaInput);

                    cadastrarUsuario(usuario);

                }else {
                    Toast.makeText(this, "Confirme sua senha", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Digite uma senha", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Digite um email", Toast.LENGTH_SHORT).show();
        }

    }

    public void textEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}