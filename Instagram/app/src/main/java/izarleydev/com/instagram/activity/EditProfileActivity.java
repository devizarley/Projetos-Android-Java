package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Usuario;


public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView alterarImagem;
    private TextInputEditText inputEditEmail, inputEditUser;
    private Button buttonSalvar;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //configurações gerais
        user = UsuarioFirebase.getDadosUsarioLogado();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        inicializarComponentes();

        //recuperar dados do usuario e setar nos inputs
        FirebaseUser userProfile = UsuarioFirebase.getUsuarioAtual();
        inputEditUser.setText(userProfile.getDisplayName());
        inputEditEmail.setText(userProfile.getEmail());

    }

    public void inicializarComponentes() {

        profile_image = findViewById(R.id.profile_image);
        alterarImagem = findViewById(R.id.alterarImagem);
        inputEditEmail = findViewById(R.id.inputEditEmail);
        inputEditUser = findViewById(R.id.inputEditUser);
        buttonSalvar = findViewById(R.id.buttonSalvar);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringName = inputEditUser.getText().toString();

                UsuarioFirebase.atualizarNomeUsuario(stringName);

                user.setName(stringName);
                user.atualizar();
                finish();
                Toast.makeText(EditProfileActivity.this, "Nome atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        inputEditEmail.setFocusable(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}