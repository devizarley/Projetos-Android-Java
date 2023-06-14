package izarleydev.com.whatsapp.Activitys.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import izarleydev.com.whatsapp.Activitys.activitys.ChatAtivity;
import izarleydev.com.whatsapp.Activitys.adapter.ContatosAdapter;
import izarleydev.com.whatsapp.Activitys.adapter.ConversasAdapter;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.RecyclerItemClickListener;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Conversas;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class ConversasFragment extends Fragment {
    private List<Conversas> listConversas = new ArrayList<>();
    private RecyclerView recyclerViewConversas;
    private DatabaseReference database;
    private DatabaseReference converasref;
    private ChildEventListener childEventListenerConversas;
    private Conversas conversa;
    private ConversasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //configurações iniciais
        recyclerViewConversas = view.findViewById(R.id.recyclerConversas);

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);

        //configura evento de click
        recyclerViewConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //recupera o usuario selecionado
                                Conversas conversaSelecionada = listConversas.get(position);
                                Intent i = new Intent(getActivity(), ChatAtivity.class);
                                //repassa informações para a intent startada
                                i.putExtra("chatContato", conversaSelecionada.getUsuarioExibicao());
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                ));

        return view;
    }


/*
    public void searchConversas(String texto){

        ///Log.d("pesquisa", texto);
        List<Conversas> listSearchConversas = new ArrayList<>();
        for (Conversas conversas : listConversas) {

            String nome = conversas.getUsuarioExibicao().getNome().toLowerCase();
            String ultimaMensagem = conversas.getUltimaMensagem().toLowerCase();

            if ( nome.contains(texto) || ultimaMensagem.contains(texto) ) {
                listSearchConversas.add(conversas);
            }
        }


        adapter = new ConversasAdapter(listSearchConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }*/

    public void recuperarConversas() {
        String idLogado = UsuarioFirebase.getIndentificadorUsuario();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Consulta para obter as conversas em que o idRemetente seja igual ao idLogado
        Query queryRemetente = db.collection("conversas")
                .whereEqualTo("idRemetente", idLogado);

        // Consulta para obter as conversas em que o idDestinatario seja igual ao idLogado
        Query queryDestinatario = db.collection("conversas")
                .whereEqualTo("idDestinatario", idLogado);

        // Executar a consulta do idRemetente
        queryRemetente.get().addOnSuccessListener(queryRemetenteSnapshot -> {

            // Lista de conversas encontradas para o idRemetente
            List<DocumentSnapshot> remetenteDocs = queryRemetenteSnapshot.getDocuments();

            // Executar a consulta do idDestinatario
            queryDestinatario.get().addOnSuccessListener(queryDestinatarioSnapshot -> {

                // Lista de conversas encontradas para o idDestinatario
                List<DocumentSnapshot> destinatarioDocs = queryDestinatarioSnapshot.getDocuments();

                // Combinar as duas listas de documentos
                List<DocumentSnapshot> allDocs = new ArrayList<>();
                allDocs.addAll(remetenteDocs);
                allDocs.addAll(destinatarioDocs);

                List<Conversas> conversas = new ArrayList<>();

                // Converter os documentos em objetos Conversa
                for (DocumentSnapshot document : allDocs) {
                    Conversas conversa = document.toObject(Conversas.class);
                    conversas.add(conversa);
                }


                // Atualizar a lista de conversas do adaptador
                listConversas = conversas;

                adapter = new ConversasAdapter(listConversas, getActivity(), new ConversasAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Conversas conversa) {
                        // Abrir a conversa correspondente passando as informações necessárias
                        Intent intent = new Intent(getActivity(), ChatAtivity.class);
                        intent.putExtra("conversa", conversa.getClass());
                        startActivity(intent);
                    }
                });

                recyclerViewConversas.setAdapter(adapter);

                // Notificar o adaptador de que os dados foram alterados
                adapter.notifyDataSetChanged();

            }).addOnFailureListener(e -> {
                // Lidar com erros na consulta do idDestinatario
            });
        }).addOnFailureListener(e -> {
            // Lidar com erros na consulta do idRemetente
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}