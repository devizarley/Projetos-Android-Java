package izarleydev.com.instagram.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.adapter.AdapterFeed;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;
    private List<Postagem> listFeed = new ArrayList<>();
    private ValueEventListener valueEventListener;
    private DatabaseReference seguidoresRef;
    private String usuarioLogado;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Configurações firebase
        usuarioLogado = UsuarioFirebase.getIdUsuario();
        seguidoresRef = ConfigFirebase.getFirebaseDatabase()
                .child("seguidores")
                .child(usuarioLogado);


        //Inicializar componentes
        recyclerFeed = view.findViewById(R.id.recyclerFeed);

        //Configura recyclerView
        adapterFeed = new AdapterFeed(listFeed, getActivity());
        recyclerFeed.setHasFixedSize(true);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFeed.setAdapter(adapterFeed);

        FirebaseUser usuariOO = UsuarioFirebase.getUsuarioAtual();

        Log.d("Teste", "onCreateView: " + usuariOO);

        return view;
    }

    private void listarFeed() {
        listFeed.clear();
        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String idUsuarioSeguido = snapshot.getKey();

                        DatabaseReference publicacoesRef = ConfigFirebase.getFirebaseDatabase().child("postagens").child(idUsuarioSeguido);
                        Query query = publicacoesRef.orderByChild("idUsuarioAutor").equalTo(idUsuarioSeguido);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    String idUsuarioAutor = snapshot.child("idUsuarioAutor").getValue(String.class);
                                    String fotoPerfilAutor = snapshot.child("fotoPerfilAutor").getValue(String.class);
                                    String nomeUsuarioAutor = snapshot.child("nomeUsuarioAutor").getValue(String.class);
                                    String fotoPostagem = snapshot.child("fotoPostagem").getValue(String.class);
                                    String idPostagem = snapshot.child("idPostagem").getValue(String.class);
                                    String descricao = snapshot.child("descricao").getValue(String.class);

                                    Postagem postagem = new Postagem();
                                    postagem.setIdUsuarioAutor(idUsuarioAutor);
                                    postagem.setFotoPerfilAutor(fotoPerfilAutor);
                                    postagem.setNomeUsuarioAutor(nomeUsuarioAutor);
                                    postagem.setFotoPostagem(fotoPostagem);
                                    postagem.setIdPostagem(idPostagem);
                                    postagem.setDescricao(descricao);

                                    listFeed.add(postagem);

                                }

                                // Aqui você pode atualizar o feed após processar todas as postagens
                                adapterFeed.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("testee", "onDataChange: " + error);

                            }
                        });
                    }
                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        listarFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
