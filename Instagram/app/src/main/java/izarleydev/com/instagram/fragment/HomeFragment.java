package izarleydev.com.instagram.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;

public class HomeFragment extends Fragment {
    androidx.appcompat.widget.Toolbar toolbar;

    private Button deslogar;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        deslogar = view.findViewById(R.id.deslogar);

        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = ConfigFirebase.getAuth();
                auth.getCurrentUser();
                auth.signOut();
            }
        });

        return view;
    }
}