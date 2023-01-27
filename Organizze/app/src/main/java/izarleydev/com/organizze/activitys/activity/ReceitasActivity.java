package izarleydev.com.organizze.activitys.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.DateCustom;
import izarleydev.com.organizze.activitys.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private FloatingActionButton buttonSubmitReceita;
    private Usuario usuario;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData = findViewById(R.id.editDataR);
        campoCategoria = findViewById(R.id.editCategoriaR);
        campoDescricao = findViewById(R.id.editDescricaoR);
        campoValor = findViewById(R.id.editValorR);
        buttonSubmitReceita = findViewById(R.id.fabSalvarR);

        campoData.setText(DateCustom.dataAtual());

        /*buttonSubmitReceita.setOnClickListener(new View.OnClickListener() {

            String textValor = campoValor.getText().toString();
            String textCategoria = campoCategoria.getText().toString();
            String textData = campoData.getText().toString();
            String textDescricao = campoDescricao.getText().toString();

            @Override
            public void onClick(View view) {
                if ( !textValor.isEmpty() ) {
                    if ( !textData.isEmpty() ) {
                        if ( !textCategoria.isEmpty() ) {
                            if ( !textDescricao.isEmpty() ) {

                                usuario = new Usuario();
                                usuario.setCategoria(textCategoria);
                                usuario.setDescricao(textDescricao);

                            }else {
                                Toast.makeText(ReceitasActivity.this, "Preencha o campo de Descrição.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ReceitasActivity.this, "Preencha o campo de Categoria.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ReceitasActivity.this, "Preencha o campo de Data.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ReceitasActivity.this, "Preencha o campo de Valor.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/



    }
}