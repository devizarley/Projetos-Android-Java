package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.model.Usuario;

public class ProfileUserActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;

    private Toolbar toolbar;
    private Button buttonProfileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        //Iniciar os componentes
        buttonProfileUser = findViewById(R.id.buttonProfile);
        buttonProfileUser.setText("Seguir");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        //Recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //Configurar nome do usuário na toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getName());

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}