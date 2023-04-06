package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import izarleydev.com.instagram.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText inputEmailCadastro, inputSenhaCadastro, inputCSenhaCadastro;
    private Button buttonCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Configurações gerais
        inputEmailCadastro = findViewById(R.id.inputEmailCadastro);
        inputSenhaCadastro = findViewById(R.id.inputSenhaCadastro);
        inputCSenhaCadastro = findViewById(R.id.inputCSenhaCadastro);


    }

    public void validarCadastro (){




    }

    public void textEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}