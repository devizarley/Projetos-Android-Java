package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class PostagemActivity extends AppCompatActivity {
    private TextView textNameUsuario, textQntLikePostagem, textDescricaoPostagem, textVisualizarComentarios;
    private ImageView imagemPostagem;
    private CircleImageView imagemPerfilUsuario;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        
        inicializarComponentes();

        //recuperar dados da activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Postagem postagem = (Postagem) bundle.getSerializable("postagem");
            Usuario usuarioSelecionado = (Usuario) bundle.getSerializable("usuario");

            //Exibe dados de usuario
            Uri uri = Uri.parse(usuarioSelecionado.getPhoto());
            Glide.with(PostagemActivity.this)
                    .load(uri)
                    .into(imagemPerfilUsuario);
            textNameUsuario.setText(usuarioSelecionado.getName());

            //Exibe dados da postagem
            Uri uriPostagem = Uri.parse(postagem.getFoto());
            Glide.with(PostagemActivity.this)
                    .load(uriPostagem)
                    .into(imagemPostagem);
            textDescricaoPostagem.setText(postagem.getDescricao());
        }
    }

    private void inicializarComponentes() {

        textNameUsuario = findViewById(R.id.textNameUsuario);
        textQntLikePostagem = findViewById(R.id.textCurtidas);
        textDescricaoPostagem = findViewById(R.id.textDescricaoPostagem);
        textVisualizarComentarios = findViewById(R.id.visualizarComentarios);
        imagemPostagem = findViewById(R.id.imagemPostagem);
        imagemPerfilUsuario = findViewById(R.id.imagemPerfil);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}