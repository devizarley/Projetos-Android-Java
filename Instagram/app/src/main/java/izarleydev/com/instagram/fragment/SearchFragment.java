package izarleydev.com.instagram.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.ProfileUserActivity;
import izarleydev.com.instagram.adapter.AdapterSearch;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.RecyclerItemClickListener;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Usuario;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Usuario> listUser;
    private DatabaseReference usuarioRef;
    private AdapterSearch adapterSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        //configurações gerais
        searchView = view.findViewById(R.id.searchView);
        listUser = new ArrayList<>();

        usuarioRef = ConfigFirebase.getFirebaseDatabase()
                        .child("usuarios");

        //configuração recyclerView
        recyclerView = view.findViewById(R.id.recyclerSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSearch = new AdapterSearch(listUser, getActivity());
        recyclerView.setAdapter(adapterSearch);

        //Configurar evento de clique
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Usuario usuarioSelecionado = listUser.get(position);
                        Intent i = new Intent(getActivity(), ProfileUserActivity.class);
                        i.putExtra("usuarioSelecionado", usuarioSelecionado);
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        searchView.setQueryHint("Buscar Usuários");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);
                return true;
            }
        });

        return view;
    }
    private void pesquisarUsuarios (String texto) {

        //limpar lista
        listUser.clear();

        //pesquisar usuarios caso tenha texto na pesquisa
        if (texto.length() >= 2){
            Query query = usuarioRef.orderByChild("name")
                    .startAfter(texto)
                    .endAt(texto + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //limpar lista
                    listUser.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //recupera o usuario no firebase
                        listUser.add( ds.getValue(Usuario.class) );
                    }
                    adapterSearch.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}