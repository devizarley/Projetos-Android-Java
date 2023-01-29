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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.Base64Custom;
import izarleydev.com.organizze.activitys.Helper.DateCustom;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.activitys.model.Movimentacao;
import izarleydev.com.organizze.activitys.model.Usuario;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText  campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference referenceDb = ConfigFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
    private Double despesaTotal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editDataD);
        campoCategoria = findViewById(R.id.editCategoriaD);
        campoDescricao = findViewById(R.id.editDescricaoD);
        campoValor = findViewById(R.id.editValorD);

        campoData.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();
    }

    public void salvarDespesa (View view){

        if (validarCamposDespesa()){
            movimentacao = new Movimentacao();

            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            Double despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(data);
            //retornar a home ao fazer o registro
            finish();
        }
    }

    public Boolean validarCamposDespesa(){

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
                         Toast.makeText(DespesasActivity.this, "Preencha o campo de Descrição.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(DespesasActivity.this, "Preencha o campo de Categoria.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(DespesasActivity.this, "Preencha o campo de Data.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(DespesasActivity.this, "Preencha o campo de Valor.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarDespesaTotal(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = referenceDb.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void atualizarDespesa(Double despesa){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = referenceDb.child("usuarios")
                .child(idUsuario);
        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}