package izarleydev.com.organizze.activitys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.databinding.ActivityPrincipalBinding;

public class PrincipalActivity extends AppCompatActivity {
    private FloatingActionButton fabDespesa;
    private FloatingActionButton fabReceita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        fabDespesa = findViewById(R.id.fab);
        fabReceita = findViewById(R.id.fab2);

        FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();

        fabDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, DespesasActivity.class));
            }
        });
        fabReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));
            }
        });
    }
}