package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.UsuarioFirebase;


public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView alterarImagem;
    private TextInputEditText inputEditEmail, inputEditUser;
    private Button buttonSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        inicializarComponentes();

        //recuperar dados do usuario
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

        inputEditEmail.setFocusable(false);


    }
}