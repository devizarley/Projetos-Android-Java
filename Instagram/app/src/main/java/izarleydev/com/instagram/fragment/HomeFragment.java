package izarleydev.com.instagram.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.LoginActivity;
import izarleydev.com.instagram.activity.MainActivity;
import izarleydev.com.instagram.adapter.AdapterFeed;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Feed;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;
    private List<Feed> listFeed = new ArrayList<>();
    private ValueEventListener valueEventListener;
    private DatabaseReference feedRef;
    private String usuarioLogado;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        //Configurações firebase
        usuarioLogado = UsuarioFirebase.getIdUsuario();
        feedRef = ConfigFirebase.getFirebaseDatabase()
                .child("feed")
                .child(usuarioLogado);

        //Inicializar componentes
        recyclerFeed = view.findViewById(R.id.recyclerFeed);

        //Configura recyclerView
        adapterFeed = new AdapterFeed(listFeed, getActivity());
        recyclerFeed.setHasFixedSize(true);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFeed.setAdapter(adapterFeed);

        return view;
    }

    private void listarFeed() {
        valueEventListener = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    listFeed.add(ds.getValue(Feed.class));
                }
                Collections.reverse(listFeed);
                adapterFeed.notifyDataSetChanged();
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
        feedRef.removeEventListener(valueEventListener);
    }
}
