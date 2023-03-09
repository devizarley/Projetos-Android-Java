package izarleydev.com.whatsapp.Activitys.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.adapter.MensageAdapter;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.Base64;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Mensagem;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;
import izarleydev.com.whatsapp.databinding.ActivityChatAtivityBinding;

public class ChatAtivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityChatAtivityBinding binding;
    private TextView textNameChat;
    private CircleImageView circleImageView;
    private Usuario usuarioDestinatario;
    private FloatingActionButton fabSubmit;
    private EditText inputContentMsg;
    private DatabaseReference databaseReference;
    private  DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //identificador usuarios remetende e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    //RecyclerView
    private RecyclerView recyclerMensage;
    private MensageAdapter mensageAdapter;
    private List<Mensagem> mensagens = new ArrayList<>();

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
        fabSubmit = findViewById(R.id.buttonSubmitMsg);
        inputContentMsg = findViewById(R.id.inputContentMsg);

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
        databaseReference = ConfigFirebase.getFirebaseDatabase();
        mensagensRef = databaseReference.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

    }
    //objeto para salvar a mensagem no firebase
    public void submitMsg(View view) {
        //recupera conteudo dentro do input
        String textMsg = inputContentMsg.getText().toString();

        if (!textMsg.isEmpty()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUsuarioRemetente);
            mensagem.setMensage(textMsg);

            //Salvar mensagem
            sendMsg(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

            //limpar texto
            inputContentMsg.setText("");

        }else {
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
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