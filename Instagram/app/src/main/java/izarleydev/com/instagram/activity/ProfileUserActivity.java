package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.adapter.AdapterGrid;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class ProfileUserActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado, usuarioLogado;
    private Toolbar toolbar;
    private Button buttonProfileUser, mensagemButton;
    private CircleImageView imageProfile;
    private TextView textPublicacoes, textSeguidores, textSeguindo, textName;
    private ValueEventListener valueEventListenerProfileUser;
    private String idUserLogado;
    private GridView gridViewProfile;
    private AdapterGrid adapterGrid;
    private ProgressBar progressGrid;
    private ImageView imageGrid;
    private DatabaseReference usuariosRef, usuarioSelecionadoRef, seguidoresRef, firebaseRef, usuarioLogadoRef, postagensUsuarioRef;
    private List<Postagem> postagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);


        firebaseRef = ConfigFirebase.getFirebaseDatabase();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");
        usuarioSelecionadoRef = firebaseRef;
        idUserLogado = UsuarioFirebase.getIdUsuario();

        //Iniciar os componentes
        componentes();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mensagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Esta função está em construção!",Toast.LENGTH_SHORT).show();
            }
        });

        //Recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //referencia postagens usuario selecionado
            postagensUsuarioRef = ConfigFirebase.getFirebaseDatabase()
                    .child("postagens")
                            .child(usuarioSelecionado.getId());

            //Configurar nome do usuário na toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getName());

            String photoUser = usuarioSelecionado.getPhoto();
            if ( photoUser != null ) {

                Uri uri = Uri.parse(photoUser);
                Glide.with(ProfileUserActivity.this).load(uri).into(imageProfile);

            }
        }

        //abrir foto clicada
        gridViewProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Postagem postagem = postagens.get(position);
                Intent i = new Intent(getApplicationContext(), PostagemActivity.class);
                i.putExtra("postagem", postagem);
                i.putExtra("usuario", usuarioSelecionado);

                startActivity(i);

            }
        });



        carregarFotosPostagem();


    }

    public void carregarFotosPostagem(){

        //Recupera as fotos postadas pelo usuario
        postagens = new ArrayList<>();
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Configurar o tamanho do grid
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                gridViewProfile.setColumnWidth(tamanhoGrid);

                List<String> urlFotos = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Postagem postagem = ds.getValue(Postagem.class);
                    postagens.add(postagem);
                    urlFotos.add(postagem.getFotoPostagem());
                }
                //Configurar adapter
                adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, urlFotos);
                gridViewProfile.setAdapter(adapterGrid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void recDadosUsuarioLogado(){

        usuarioLogadoRef = usuariosRef.child(idUserLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usuarioLogado = snapshot.getValue(Usuario.class);

                        //Após recuperar os dados do usuario, inicia o metodo de verificar se está seguindo:
                        verificarSeguindoUsuario();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void verificarSeguindoUsuario(){

        DatabaseReference seguidorRef = seguidoresRef.child("seguidores")
                .child(usuarioSelecionado.getId())
                .child(idUserLogado);

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.d("dadosUsuario", ": Seguindo");
                            habilitarButton(true);
                        }else {
                            Log.d("dadosUsuario", ": seguir");
                            habilitarButton(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void recuperarDadosUsuarioSelecionado(){

        usuarioSelecionadoRef = usuariosRef.child( usuarioSelecionado.getId());

        valueEventListenerProfileUser = usuarioSelecionadoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Usuario usuario = snapshot.getValue(Usuario.class);

                        String publicacoes = String.valueOf(usuario.getPublicacoes());
                        String seguindo = String.valueOf(usuario.getSeguindo());
                        String seguidores = String.valueOf(usuario.getSeguidores());

                        textPublicacoes.setText(publicacoes);
                        textSeguindo.setText(seguindo);
                        textSeguidores.setText(seguidores);
                        textName.setText(usuarioSelecionado.getName());


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }


    private void habilitarButton (boolean segueUsuario){
        if (segueUsuario){
            buttonProfileUser.setText("Seguindo");

        }else {
            buttonProfileUser.setText("Seguir");

            buttonProfileUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //salvar seguidor
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });
        }
    }

    private void salvarSeguidor(Usuario uLogado, Usuario uSelecionado){

        HashMap<String, Object> dadosUserLogado = new HashMap<>();
        dadosUserLogado.put("idUser", uSelecionado.getId());
        DatabaseReference seguindoRef = seguidoresRef
                .child(uLogado.getId())
                .child(uSelecionado.getId());
        seguindoRef.setValue(dadosUserLogado);


        //ALTERAÇÃO NO BOTÃO
        buttonProfileUser.setText("Seguindo");
        buttonProfileUser.setOnClickListener(null);

        //incrementar seguindo do usuario logado

        int seguindo = uLogado.getSeguindo() + 1;
        HashMap<String, Object> dadosSeguindo = new HashMap<>();
        dadosSeguindo.put("seguindo", seguindo);
        DatabaseReference usuarioSeguindo = usuariosRef
                .child(uLogado.getId());
        usuarioSeguindo.updateChildren(dadosSeguindo);

        //incrementar seguidores do amigo

        int seguidores = uSelecionado.getSeguidores() + 1;
        HashMap<String, Object> dadosSeguidores = new HashMap<>();
        dadosSeguidores.put("seguidores", seguidores);
        DatabaseReference usuarioSeguidores = usuariosRef
                .child(uSelecionado.getId());
        usuarioSeguidores.updateChildren(dadosSeguidores);

    }

    private void componentes (){

        buttonProfileUser = findViewById(R.id.buttonProfile);
        buttonProfileUser.setText("Carregando");
        imageProfile = findViewById(R.id.imagemPerfilUsuario);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        gridViewProfile = findViewById(R.id.gridViewProfile);
        progressGrid = findViewById(R.id.progressGrid);
        imageGrid = findViewById(R.id.imageGridProfile);
        mensagemButton = findViewById(R.id.mensagemButton);
        textName = findViewById(R.id.textName);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //recuperar dados do usuario selecionado
        recuperarDadosUsuarioSelecionado();
        //recuperar dados usuario logado
        recDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioSelecionadoRef.removeEventListener(valueEventListenerProfileUser);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}