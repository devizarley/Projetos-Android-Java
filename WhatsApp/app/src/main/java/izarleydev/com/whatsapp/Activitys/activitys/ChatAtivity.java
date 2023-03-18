package izarleydev.com.whatsapp.Activitys.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.adapter.MensageAdapter;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.Base64;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Conversas;
import izarleydev.com.whatsapp.Activitys.model.Mensagem;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;
import izarleydev.com.whatsapp.databinding.ActivityChatAtivityBinding;

public class ChatAtivity extends AppCompatActivity {

    private ActivityChatAtivityBinding binding;
    private TextView textNameChat;
    private CircleImageView circleImageView;
    private Usuario usuarioDestinatario;
    private EditText inputContentMsg;
    private ChildEventListener childEventListenerMensagens;
    private ImageView camInputChat;
    private static final int SELECAO_CAMERA = 1;

    //identificador usuarios remetende e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    //RecyclerView
    private RecyclerView recyclerMensage;
    private MensageAdapter mensageAdapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    //firebase

    private StorageReference storageReference = ConfigFirebase.getFirebaseStorage();
    private DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();;
    private DatabaseReference mensagensRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //configurações toolbar
        binding = ActivityChatAtivityBinding.inflate(getLayoutInflater());
        binding.toolbarChat.setTitle("");
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarChat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textNameChat = findViewById(R.id.textNameChat);
        circleImageView = findViewById(R.id.circleImageChat);
        inputContentMsg = findViewById(R.id.inputContentMsg);
        camInputChat = findViewById(R.id.iconInputCamera);

        //Configurações RecyclerView
        recyclerMensage = findViewById(R.id.recyclerMensage);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensage.setLayoutManager(layoutManager);
        recyclerMensage.setHasFixedSize(true);

        ///Configuração Adapter
        mensageAdapter = new MensageAdapter(mensagens, getApplicationContext());
        recyclerMensage.setAdapter(mensageAdapter);

        //dados usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIndentificadorUsuario();

        //Recupera dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textNameChat.setText(usuarioDestinatario.getNome());

            //para setar a imagem do destinatario, é necessario recupera-la

            String photo = usuarioDestinatario.getPhoto();
            if (photo != null) {
                Uri url = Uri.parse(usuarioDestinatario.getPhoto());
                Glide.with(this)
                        .load(url)
                        .into(circleImageView);
            }else {
                circleImageView.setImageResource(R.drawable.padrao);
            }
            //recuperar dados usuario destinatario
            idUsuarioDestinatario = Base64.codBase64(usuarioDestinatario.getEmail());
        }
        //configs listar mensagens
        mensagensRef = databaseReference.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //Camera input
        camInputChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

    }

    //resultado camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {

            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if (imagem != null){

                    //Criar nome da imagem baseado na data/hora/minuto/segundo
                    String nomeImagem = UUID.randomUUID().toString();

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no firebase
                    StorageReference imagens = storageReference
                            .child("imagens")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);

                    final StorageReference imagemRef = imagens.child(nomeImagem);

                    UploadTask uploadTask = imagens.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatAtivity.this, "Erro ao enviar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagens.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();

                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUser(idUsuarioRemetente);
                                    mensagem.setMensage("imagem.jpeg");
                                    mensagem.setImage(url);

                                    //Salvar imagem Remetente
                                    sendMsg(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                                    //Salvar imagem Destinatario
                                    sendMsg(idUsuarioDestinatario, idUsuarioRemetente, mensagem);
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

    //objeto para salvar a mensagem no firebase
    public void submitMsg(View view) {
        //recupera conteudo dentro do input
        String textMsg = inputContentMsg.getText().toString();

        if (!textMsg.isEmpty()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUsuarioRemetente);
            mensagem.setMensage(textMsg);

            //Salvar mensagem para o remetente
            sendMsg(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

            //Salvar mensagem para o destinatario
            sendMsg(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

            //limpar texto
            inputContentMsg.setText("");

            //salvar conversa
            saveChat(mensagem);

        }else {
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChat(Mensagem mensagem) {

        Conversas conversaRemetente = new Conversas();

        //seta valores nos contrutures
        conversaRemetente.setIdRemetente(idUsuarioRemetente);
        conversaRemetente.setIdDestinatario(idUsuarioDestinatario);
        conversaRemetente.setUltimaMensagem(mensagem.getMensage());
        conversaRemetente.setUsuarioExibicao( usuarioDestinatario );

        conversaRemetente.salvar();
    }

    private void sendMsg(String idRemetente, String idDestinatario, Mensagem mensagem){

        DatabaseReference database = ConfigFirebase.getFirebaseDatabase();
        mensagensRef = database.child("mensagens");
        mensagensRef.child(idRemetente).child(idDestinatario).push().setValue(mensagem);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens(){

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagens.add( mensagem );
                mensageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}