package izarleydev.com.organizze.activitys.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.DateCustom;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText  campoValor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricaoR);
        campoValor = findViewById(R.id.editValorR);

        campoData.setText(DateCustom.dataAtual());
    }
}