package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Usuario;


public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView alterarImagem;
    private TextInputEditText inputEditEmail, inputEditUser;
    private Button buttonSalvar;
    private Usuario user;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //configurações gerais
        user = UsuarioFirebase.getDadosUsarioLogado();
        storageReference = ConfigFirebase.getFirebaseStorage();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        inicializarComponentes();

        //recuperar dados do usuario e setar nos inputs
        FirebaseUser userProfile = UsuarioFirebase.getUsuarioAtual();
        inputEditUser.setText(userProfile.getDisplayName());
        inputEditEmail.setText(userProfile.getEmail());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                        break;
                }
                if (imagem != null){

                    profile_image.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no firebase
                    StorageReference imagemRef = storageReference.child("imagens").child("perfil").child("<id-usuario.jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    public void inicializarComponentes() {

        profile_image = findViewById(R.id.profileEditImage);
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
                Toast.makeText(EditProfileActivity.this, "Nome atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        alterarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
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