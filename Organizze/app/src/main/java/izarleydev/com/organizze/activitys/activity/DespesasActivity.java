package izarleydev.com.organizze.activitys.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.DateCustom;
import izarleydev.com.organizze.activitys.model.Movimentacao;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText  campoValor;
    private Movimentacao movimentacao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = (TextInputEditText)findViewById(R.id.editDataD);
        campoCategoria = (TextInputEditText)findViewById(R.id.editCategoriaD);
        campoDescricao = (TextInputEditText)findViewById(R.id.editDescricaoD);
        campoValor = (EditText)findViewById(R.id.editValorD);

        campoData.setText(DateCustom.dataAtual());

    }

    public void salvarDespesa (View view){

        movimentacao = new Movimentacao();

        String data = campoData.getText().toString();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(data);
        movimentacao.setTipo("d");
        movimentacao.salvar(data);

    }
}