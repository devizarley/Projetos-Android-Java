package izarleydev.com.whatsapp.Activitys.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

import izarleydev.com.whatsapp.Activitys.activitys.ChatAtivity;
import izarleydev.com.whatsapp.Activitys.adapter.ContatosAdapter;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.RecyclerItemClickListener;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listContatos = new ArrayList<>();
    private DatabaseReference ref;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser currentUser = UsuarioFirebase.getUsuarioAtual();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usuariosRef = db.collection("usuario");;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configurações iniciais
        recyclerViewListContatos = view.findViewById(R.id.listContatos);

        //Configurar adapter
        adapter = new ContatosAdapter( listContatos, getActivity());

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListContatos.setLayoutManager(layoutManager);
        recyclerViewListContatos.setHasFixedSize(true);
        recyclerViewListContatos.setAdapter(adapter);

        //configura evento de clique no recyclerview
        recyclerViewListContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //recupera o usuario selecionado
                                Usuario usuarioSelecionado = listContatos.get( position );
                                Intent i = new Intent(getActivity(), ChatAtivity.class);
                                //repassa informações para a intent startada
                                i.putExtra("chatContato", usuarioSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        return view;
    }
    public void recContatos (){
        usuariosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listContatos.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    // Recupera os dados do documento como um objeto Usuario
                    Usuario usuario = document.toObject(Usuario.class);

                    // Recupera o email do usuário logado
                    String emailCurrentUser = currentUser.getEmail();

                    // Verifica se o email do usuário logado é diferente do email do documento atual
                    if (!emailCurrentUser.equals(usuario.getEmail())) {
                        listContatos.add(usuario);
                    }
                }

                adapter.notifyDataSetChanged();
            } else {
                // Tratar falha na recuperação dos dados
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        listContatos.clear();
        recContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}