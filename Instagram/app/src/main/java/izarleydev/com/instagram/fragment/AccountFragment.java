package izarleydev.com.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.EditProfileActivity;
import izarleydev.com.instagram.activity.MainActivity;
import izarleydev.com.instagram.activity.PostagemActivity;
import izarleydev.com.instagram.adapter.AdapterGrid;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class AccountFragment extends Fragment {

    private Button buttonProfile, mensagemButton;
    private DatabaseReference usuariosRef, seguidoresRef, firebaseRef, usuarioLogadoRef, postagensUsuarioRef;
    private ValueEventListener valueEventListenerProfile;
    private Usuario usuarioSelecionado, usuarioLogadoDados;
    private FirebaseUser usuarioLogado;
    private TextView textPublicacoes, textSeguidores, textSeguindo, textName;
    private GridView gridViewProfile;
    private AdapterGrid adapterGrid;
    private ProgressBar progressGrid;
    private ImageView imageGrid;
    private CircleImageView imageProfile;
    private String idUserLogado;
    private List<Postagem> listPostagens = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        firebase();
        componentes(view);

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
        mensagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Esta função está em construção!",Toast.LENGTH_SHORT).show();
            }
        });


        inicializarImageLoader();
        carregarFotosPostagem();



        return view;
    }

    public void inicializarImageLoader (){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getContext())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(config);

    }

    private void recuperarDadosUsuarioLogado(){

        valueEventListenerProfile = usuarioLogadoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Usuario usuario = snapshot.getValue(Usuario.class);

                        String publicacoes = String.valueOf(usuario.getPublicacoes());
                        String seguindo = String.valueOf(usuario.getSeguindo());
                        String seguidores = String.valueOf(usuario.getSeguidores());

                        //textPublicacoes.setText(publicacoes);
                        textSeguindo.setText(seguindo);
                        textSeguidores.setText(seguidores);
                        textName.setText(usuarioLogado.getDisplayName());
                        textPublicacoes.setText(publicacoes);

                        FirebaseUser usuarioLogado = UsuarioFirebase.getUsuarioAtual();

                        if ( usuarioLogado.getPhotoUrl() != null ) {

                            Uri uri = Uri.parse(usuarioLogado.getPhotoUrl().toString());
                            Glide.with(getActivity()).load(uri).into(imageProfile);

                        }else {
                            imageProfile.setImageResource(R.drawable.avatar);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void carregarFotosPostagem(){

        //Recupera as fotos postadas pelo usuario
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Configurar o tamanho do grid
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                gridViewProfile.setColumnWidth(tamanhoGrid);

                List<String> urlFotos = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Postagem postagem = ds.getValue(Postagem.class);
                    listPostagens.add(postagem);
                    urlFotos.add(postagem.getFoto());
                }

                // Inverter a ordem da lista de postagens
                Collections.reverse(listPostagens);
                Collections.reverse(urlFotos);

                //abrir foto clicada
                gridViewProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Postagem postagem = listPostagens.get(position);
                        Intent i = new Intent(getContext(), PostagemActivity.class);
                        i.putExtra("postagem", postagem);
                        i.putExtra("usuario", usuarioLogado);

                        startActivity(i);

                    }
                });

                //Configurar adapter
                adapterGrid = new AdapterGrid(getActivity(), R.layout.grid_postagem, urlFotos);
                gridViewProfile.setAdapter(adapterGrid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void firebase (){
        firebaseRef = ConfigFirebase.getFirebaseDatabase();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");
        idUserLogado = UsuarioFirebase.getIdUsuario();
        usuarioLogado = UsuarioFirebase.getUsuarioAtual();
        usuarioLogadoRef = usuariosRef.child(idUserLogado);

        postagensUsuarioRef = ConfigFirebase.getFirebaseDatabase()
                .child("postagens")
                .child(idUserLogado);

        usuarioLogadoDados = new Usuario();
    }

    private void componentes (View view){

        buttonProfile = view.findViewById(R.id.buttonProfile);
        imageProfile = view.findViewById(R.id.imagemPerfilUsuario);
        textPublicacoes = view.findViewById(R.id.textPublicacoes);
        textSeguidores = view.findViewById(R.id.textSeguidores);
        textSeguindo = view.findViewById(R.id.textSeguindo);
        gridViewProfile = view.findViewById(R.id.gridViewProfile);
        progressGrid = view.findViewById(R.id.progressGrid);
        imageGrid = view.findViewById(R.id.imageGridProfile);
        textName = view.findViewById(R.id.textName);
        mensagemButton = view.findViewById(R.id.mensagemButton);

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuarioLogado();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListenerProfile);
    }
}