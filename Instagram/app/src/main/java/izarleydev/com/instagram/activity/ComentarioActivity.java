package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.adapter.AdapterComentario;
import izarleydev.com.instagram.adapter.AdapterFeed;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Comentario;
import izarleydev.com.instagram.model.Usuario;

public class ComentarioActivity extends AppCompatActivity {

    private EditText inputComentario;
    private FloatingActionButton fabSubmit;
    private Toolbar toolbar;
    private String idPostagem;
    private Usuario usuarioLogado;
    private RecyclerView recyclerComentario;
    private AdapterComentario adapterComentario;
    private ValueEventListener valueEventListener;
    private List<Comentario> listComentario = new ArrayList<>();
    private DatabaseReference comentarioRef;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        inputComentario = findViewById(R.id.inputComentario);
        fabSubmit = findViewById(R.id.buttonSubmit);
        recyclerComentario = findViewById(R.id.recyclerComentario);

        usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();
        firebaseRef = ConfigFirebase.getFirebaseDatabase();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Comentarios");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);


        //Configurar recyclerView
        adapterComentario = new AdapterComentario(listComentario, getApplicationContext());
        recyclerComentario.setHasFixedSize(true);
        recyclerComentario.setLayoutManager(new LinearLayoutManager(this));
        recyclerComentario.setAdapter(adapterComentario);

        //Recuperar id da postagem
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idPostagem = bundle.getString("idPostagem");
        }

    }

    private void listarComentario() {
        comentarioRef = firebaseRef.child("comentarios")
                .child(idPostagem);
        valueEventListener = comentarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComentario.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    listComentario.add(ds.getValue(Comentario.class));
                }
                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarComentario();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentarioRef.removeEventListener(valueEventListener);
    }

    public void salvarComentario(View view){

        String textoComentario = inputComentario.getText().toString();
        if (textoComentario != null && !textoComentario.equals("")){

            Comentario comentario = new Comentario();
            comentario.setIdPostagem(idPostagem);
            comentario.setIdUsuario(usuarioLogado.getId());
            comentario.setNomeUsuario(usuarioLogado.getName());
            comentario.setCaminhoFoto(usuarioLogado.getPhoto());
            comentario.setComentario(textoComentario);

            if (comentario.salvar()){
                Toast.makeText(this,
                        "Comentario Salvo com sucesso",
                        Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,
                    "Insira um comentário antes de salvar!",
                    Toast.LENGTH_SHORT).show();
        }

        //limpar comentario escrito
        inputComentario.setText("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}