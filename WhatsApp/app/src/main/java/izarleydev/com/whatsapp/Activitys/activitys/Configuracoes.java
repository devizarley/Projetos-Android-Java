package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.Permissao;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class Configuracoes extends AppCompatActivity {

    private String[] permissions;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 1;
    private static final int SELECAO_GALERIA = 2;
    private CircleImageView imageConfig;
    private EditText editName;
    private StorageReference storageReference = ConfigFirebase.getFirebaseStorage();
    private String idUser = UsuarioFirebase.getIndentificadorUsuario();
    private ImageButton attName;
    private Usuario userLogado;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //teste
        if (Build.VERSION.SDK_INT > 32) {
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
            };
        }else{
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }

        //Validar permissões
        Permissao.validarPermissoes(permissions, this, 1);

        //Configurações iniciais
        userLogado = UsuarioFirebase.getDadosUsuarioLogado();

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGallery);
        imageConfig = findViewById(R.id.circleImageView);
        editName = findViewById(R.id.editPerfilName);
        attName = findViewById(R.id.atualizarNome);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do usuario
        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        Uri url = user.getPhotoUrl();

        if (url != null) {

            Glide.with(Configuracoes.this)
                    .load(url)
                    .into(imageConfig);

        }else{
             imageConfig.setImageResource(R.drawable.padrao);
        }

        //Seta o nome configurado no banco de dados.
        editName.setText(user.getDisplayName());

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        attName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = editName.getText().toString();
                boolean r = UsuarioFirebase.atualizarNomeUsuario(nome);
                if (r){

                    userLogado.setNome(nome);
                    userLogado.atualizar();

                    Toast.makeText(Configuracoes.this, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Atualizar foto na activity "Configurações"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {
            Bitmap imagem = null;
            try {

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImage = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImage);
                        break;
                }
                if (imagem != null){

                    imageConfig.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();


                    //Salvar imagem no firebase
                     StorageReference imagens = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(idUser)
                            .child("perfil.jpeg");

                    final StorageReference imagemRef = imagens.child("perfil.jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Configuracoes.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);
                                }

                            });

                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Objeto criado para atualizar foto do Usuario.
    public void atualizaFotoUsuario (Uri url){
        UsuarioFirebase.atualizarFotoUsuario(url);

        userLogado.setPhoto(url.toString());
        userLogado.atualizar();

    }

    //Objeto para recuperar resultado da validação
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            };
        }

    }
    //Alerta ao negar permissão Camera/Galeria
    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}