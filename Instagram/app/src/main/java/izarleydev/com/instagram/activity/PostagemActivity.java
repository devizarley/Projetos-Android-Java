package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.PostagemCurtidas;
import izarleydev.com.instagram.model.Usuario;

public class PostagemActivity extends AppCompatActivity implements Serializable {
    private TextView textNameUsuario, textQntLikePostagem, textDescricaoPostagem;
    private ImageView imagemPostagem, imageComentario;
    private CircleImageView imagemPerfilUsuario;
    private Toolbar toolbar;
    private List<Postagem> listPostagem;

    ImageView visualizarComentario;
    LikeButton likeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        inicializarComponentes();

        //recuperar dados da activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Postagem postagem = (Postagem) bundle.getSerializable("postagem");
            Usuario usuarioSelecionado = (Usuario) bundle.getSerializable("usuario");

            //Exibe dados de usuario
            if (usuarioSelecionado.getPhoto() != null){
                Uri uri = Uri.parse(usuarioSelecionado.getPhoto());
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imagemPerfilUsuario);
            }else {
                imagemPerfilUsuario.setImageResource(R.drawable.avatar);
            }
            textNameUsuario.setText(usuarioSelecionado.getName());

            //Exibe dados da postagem
            Uri uriPostagem = Uri.parse(postagem.getFotoPostagem());
            Glide.with(PostagemActivity.this)
                    .load(uriPostagem)
                    .into(imagemPostagem);
            textDescricaoPostagem.setText(postagem.getDescricao());

            //Recuperar dados da postagem curtida
            DatabaseReference curtidas = ConfigFirebase.getFirebaseDatabase()
                    .child("postagens-curtidas")
                    .child(postagem.getIdPostagem());

            //Objeto postagem curtida

            curtidas.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int qtdCurtidas = 0;
                    if (snapshot.hasChild("qtdCurtidas")){

                        PostagemCurtidas postagemCurtida = snapshot.getValue(PostagemCurtidas.class);
                        qtdCurtidas = postagemCurtida.getQtdCurtidas();

                    }

                    Usuario usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();
                    PostagemCurtidas curtida = new PostagemCurtidas();
                    curtida.setIdPostagem(postagem.getIdPostagem());
                    curtida.setUsuario(usuarioLogado);
                    curtida.setQtdCurtidas(qtdCurtidas);

                    //Verificar se já foi clicado em curtir pelo id do usuario
                    if (snapshot.hasChild(usuarioLogado.getId())) {
                        likeButton.setLiked(true);
                    }else {
                        likeButton.setLiked(false);
                    }

                    likeButton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            curtida.salvar();
                            textQntLikePostagem.setText(curtida.getQtdCurtidas() + " curtidas");
                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {
                            curtida.removerQtdCurtidas();
                            textQntLikePostagem.setText(curtida.getQtdCurtidas() + " curtidas");
                        }
                    });

                    textQntLikePostagem.setText(curtida.getQtdCurtidas() + " curtidas");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            visualizarComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ComentarioActivity.class);
                    i.putExtra("idPostagem", postagem.getIdPostagem());
                    startActivity(i);
                }
            });
        }else {
            Log.d("testeUsuario", "bundle está nulo");
        }
    }

    private void inicializarComponentes() {

        textNameUsuario = findViewById(R.id.textNameUsuario);
        textQntLikePostagem = findViewById(R.id.textCurtidas);
        textDescricaoPostagem = findViewById(R.id.textDescricaoPostagem);
        imagemPostagem = findViewById(R.id.imagemPostagem);
        imagemPerfilUsuario = findViewById(R.id.imagemPerfil);
        imageComentario = findViewById(R.id.imageComentario);
        likeButton = findViewById(R.id.likeButtonFeed);
        visualizarComentario = findViewById(R.id.imageComentario);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }
}