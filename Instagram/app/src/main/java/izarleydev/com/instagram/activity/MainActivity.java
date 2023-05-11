package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.databinding.ActivityMainBinding;
import izarleydev.com.instagram.fragment.AccountFragment;
import izarleydev.com.instagram.fragment.AddFragment;
import izarleydev.com.instagram.fragment.HomeFragment;
import izarleydev.com.instagram.fragment.SearchFragment;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.Permissao;
import izarleydev.com.instagram.helper.UsuarioFirebase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth ;
    ActivityMainBinding binding;
    private FloatingActionButton ic_add;
    private Toolbar toolbar2;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("");
        setSupportActionBar(toolbar2);
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        ic_add = findViewById(R.id.ic_add);
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AddFragment());
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener( item -> {

            switch (item.getItemId()) {
                case R.id.ic_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.ic_pesquisa:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.ic_account:
                    replaceFragment(new AccountFragment());
                    break;
                case R.id.ic_cam:
                        replaceFragment(new AddFragment());
                    break;
            }

            return true;

        });
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}