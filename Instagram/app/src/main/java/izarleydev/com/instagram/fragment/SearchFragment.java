package izarleydev.com.instagram.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Usuario;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Usuario> listUser;
    private DatabaseReference usuarioRef;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        //configurações gerais
        recyclerView = view.findViewById(R.id.recyclerSearch);
        searchView = view.findViewById(R.id.searchView);
        listUser = new ArrayList<>();
        usuarioRef = ConfigFirebase.getFirebaseDatabase()
                        .child("usuarios");

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
        if (texto.length() > 0){
            Query query = usuarioRef.orderByChild("name")
                    .startAfter(texto)
                    .endAt(texto + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //recupera o usuario no firebase
                        listUser.add( ds.getValue(Usuario.class) );
                    }
                    
                    int total = listUser.size();
                    Log.i("totalUsuarios", "onDataChange: " + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}