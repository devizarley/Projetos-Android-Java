package izarleydev.com.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.EditProfileActivity;
import izarleydev.com.instagram.activity.ProfileUserActivity;
import izarleydev.com.instagram.adapter.AdapterGrid;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class AccountFragment extends Fragment {

    private Button buttonProfile;
    private DatabaseReference usuariosRef, seguidoresRef, firebaseRef, usuarioLogadoRef, postagensUsuarioRef;
    private ValueEventListener valueEventListenerProfile;
    private Usuario usuarioSelecionado, usuarioLogado, usuarioLogadoDados;
    private TextView textPublicacoes, textSeguidores, textSeguindo, textName;
    private GridView gridViewProfile;
    private AdapterGrid adapterGrid;
    private ProgressBar progressGrid;
    private ImageView imageGrid;
    private CircleImageView imageProfile;
    private String idUserLogado;


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


        inicializarImageLoader();
        carregarFotosPostagem();

        return view;
    }

    public void inicializarImageLoader (){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getActivity())
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
                        textName.setText(usuarioLogado.getName());
                        textPublicacoes.setText(publicacoes);

                        //RECUPERAR FOTO

                        String photoUser = usuarioLogado.getPhoto();
                        if ( photoUser != null ) {

                            Uri uri = Uri.parse(photoUser);
                            Glide.with(AccountFragment.this).load(uri).into(imageProfile);

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
                    urlFotos.add(postagem.getFoto());
                }

                //Configurar adapter
                adapterGrid = new AdapterGrid(getContext(), R.layout.grid_postagem, urlFotos);
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
        usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();
        usuarioLogadoRef = usuariosRef.child(idUserLogado);

        postagensUsuarioRef = ConfigFirebase.getFirebaseDatabase()
                .child("postagens")
                .child(idUserLogado);

        usuarioLogadoDados = new Usuario();
    }

    private void componentes (View view){

        buttonProfile = view.findViewById(R.id.buttonProfile);
        imageProfile = view.findViewById(R.id.circleImageProfile);
        textPublicacoes = view.findViewById(R.id.textPublicacoes);
        textSeguidores = view.findViewById(R.id.textSeguidores);
        textSeguindo = view.findViewById(R.id.textSeguindo);
        gridViewProfile = view.findViewById(R.id.gridViewProfile);
        progressGrid = view.findViewById(R.id.progressGrid);
        imageGrid = view.findViewById(R.id.imageGridProfile);
        textName = view.findViewById(R.id.textName);

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