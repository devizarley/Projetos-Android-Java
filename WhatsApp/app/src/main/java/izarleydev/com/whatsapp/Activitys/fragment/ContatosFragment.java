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

import java.lang.reflect.Array;
import java.util.ArrayList;

import izarleydev.com.whatsapp.Activitys.activitys.ChatAtivity;
import izarleydev.com.whatsapp.Activitys.adapter.ContatosAdapter;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.RecyclerItemClickListener;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListContatos;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listContatos = new ArrayList<>();
    private DatabaseReference ref;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser currentUser = UsuarioFirebase.getUsuarioAtual();


    public ContatosFragment() {
        // Required empty public constructor
    }

    public static ContatosFragment newInstance(String param1, String param2) {
        ContatosFragment fragment = new ContatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configurações iniciais
        recyclerViewListContatos = view.findViewById(R.id.listContatos);
        ref = ConfigFirebase.getFirebaseDatabase().child("usuarios");

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

    @Override
    public void onStart() {
        super.onStart();
        listContatos.clear();
        recContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        ref.removeEventListener(valueEventListenerContatos);
    }

    public void recContatos (){
        valueEventListenerContatos = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for ( DataSnapshot dados: snapshot.getChildren() ){
                    //Lista todos os usuarios do banco de dados
                    Usuario usuario = dados.getValue(Usuario.class);

                    //recupera usuario logado
                    String emailCurrentUser = currentUser.getEmail();

                    //verificação se o email do usuario logado é igual à algum do banco de dados
                    if (!emailCurrentUser.equals(usuario.getEmail())){
                        listContatos.add(usuario);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}