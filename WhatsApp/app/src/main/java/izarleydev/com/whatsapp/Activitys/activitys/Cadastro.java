package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.R;

public class Cadastro extends AppCompatActivity {

    /*Toda a configuração de cadastro de usuarios*/
    TextInputEditText inputEmail;
    TextInputEditText inputSenha;
    TextInputEditText inputSenhaC;
    private DatabaseReference ref = ConfigFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfigFirebase.getAuth();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);
        inputSenhaC = findViewById(R.id.inputSenhaC);


    }
    public void buttonCadastro (View view) {

        String valueInputEmail = inputEmail.getText().toString();
        String valueInputSenha = inputSenha.getText().toString();
        String valueInputSenhaC= inputSenhaC.getText().toString();

        if (valueInputEmail.isEmpty()){

        }else {
            
        }


        auth.createUserWithEmailAndPassword(valueInputEmail, valueInputSenha)
                        .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                            }
                        });

    }
}