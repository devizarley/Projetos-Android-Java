package izarleydev.com.whatsapp.Activitys.activitys;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
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
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);

    }

    public void cadastrarUsuario (View view) {

    }
    public void validarCadastro (View view) {

        String valueinputName = inputName.getText().toString();
        String valueinputEmail = inputEmail.getText().toString();
        String valueinputSenha= inputSenha.getText().toString();

        if (!valueinputName.isEmpty()){
            if (!valueinputEmail.isEmpty()){
                if (!valueinputSenha.isEmpty()){



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