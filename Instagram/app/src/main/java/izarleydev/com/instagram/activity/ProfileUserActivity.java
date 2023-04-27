package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Usuario;

public class ProfileUserActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;

    private Toolbar toolbar;
    private Button buttonProfileUser;
    private CircleImageView imageProfile;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioSelecionadoRef;
    private DatabaseReference seguidoresRef;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private ValueEventListener valueEventListenerProfileUser;
    private String idUserLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        //Iniciar os componentes
        componentes();

        usuarioRef = ConfigFirebase.getFirebaseDatabase()
                .child("usuarios");
        usuarioSelecionadoRef = ConfigFirebase.getFirebaseDatabase();
        seguidoresRef = ConfigFirebase.getFirebaseDatabase();

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

            String photoUser = usuarioSelecionado.getPhoto();
            if ( photoUser != null ) {

                Uri uri = Uri.parse(photoUser);
                Glide.with(ProfileUserActivity.this).load(uri).into(imageProfile);

            }
        }
        verificarSeguindoUsuario();
    }


    @Override
    protected void onStart() {
        recuperarDados();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioSelecionadoRef.removeEventListener(valueEventListenerProfileUser);
    }

    private void recuperarDados(){

        usuarioSelecionadoRef = usuarioRef.child( usuarioSelecionado.getId());

        valueEventListenerProfileUser = usuarioSelecionadoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Usuario usuario = snapshot.getValue(Usuario.class);

                        String publicacoes = String.valueOf(usuario.getPublicacoes());
                        String seguindo = String.valueOf(usuario.getSeguindo());
                        String seguidores = String.valueOf(usuario.getSeguidores());

                        textPublicacoes.setText(publicacoes);
                        textSeguindo.setText(seguindo);
                        textSeguidores.setText(seguidores);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
    private void verificarSeguindoUsuario(){

        DatabaseReference seguidorRef = seguidoresRef.child("seguidores")
                .child(idUserLogado)
                .child(usuarioSelecionado.getId());

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.d("dadosUsuario", ": Seguindo");
                            habilitarButton(true);
                        }else {
                            Log.d("dadosUsuario", ": seguir");
                            habilitarButton(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void habilitarButton (boolean segueUsuario){
        if (segueUsuario){
            buttonProfileUser.setText("Seguindo");
        }else {
            buttonProfileUser.setText("Seguir");
        }
    }

    private void componentes (){

        buttonProfileUser = findViewById(R.id.buttonProfile);
        buttonProfileUser.setText("Seguir");
        imageProfile = findViewById(R.id.circleImageProfile);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        idUserLogado = UsuarioFirebase.getIdUsuario();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}