package izarleydev.com.whatsapp.Activitys.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import izarleydev.com.whatsapp.Activitys.adapter.ContatosAdapter;
import izarleydev.com.whatsapp.Activitys.adapter.ConversasAdapter;
import izarleydev.com.whatsapp.Activitys.model.Conversas;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversasFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Conversas> listConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private RecyclerView recyclerViewConversas;

    public ConversasFragment() {
    }

    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
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
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //configurações iniciais
        recyclerViewConversas = view.findViewById(R.id.recyclerConversas);

        //Configurar adapter
        adapter = new ConversasAdapter( listConversas, getActivity());

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        return view;
    }
}