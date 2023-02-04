package izarleydev.com.organizze.activitys.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.Base64Custom;
import izarleydev.com.organizze.activitys.Helper.DateCustom;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.activitys.model.Movimentacao;
import izarleydev.com.organizze.activitys.model.Usuario;

public class ReceitaActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double receitaTotal;
    private FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
    private DatabaseReference referenceDb = ConfigFirebase.getFirebaseDatabase();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoData = findViewById(R.id.editDataR);
        campoCategoria = findViewById(R.id.editCategoriaR);
        campoDescricao = findViewById(R.id.editDescricaoR);
        campoValor = findViewById(R.id.editValorR);

        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();
    }
    public void salvarReceitas (View view){

        if (validarCamposReceitas()){
            movimentacao = new Movimentacao();

            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            Double despesaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }
    public Boolean validarCamposReceitas(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();

        if ( !textoValor.isEmpty() ) {
            if ( !textoData.isEmpty() ) {
                if ( !textoCategoria.isEmpty() ) {
                    if ( !textoDescricao.isEmpty() ) {
                        return true;
                    }else {
                        Toast.makeText(ReceitaActivity.this, "Preencha o campo de Descrição.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(ReceitaActivity.this, "Preencha o campo de Categoria.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(ReceitaActivity.this, "Preencha o campo de Data.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(ReceitaActivity.this, "Preencha o campo de Valor.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarReceitaTotal(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = referenceDb.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getDespesaTotal();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void atualizarReceita(Double despesa){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = referenceDb.child("usuarios")
                .child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(despesa);
    }
}