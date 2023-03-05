package izarleydev.com.whatsapp.Activitys.activitys;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;
import izarleydev.com.whatsapp.databinding.ActivityChatAtivityBinding;

public class ChatAtivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityChatAtivityBinding binding;
    private TextView textNameChat;
    private CircleImageView circleImageView;
    private Usuario usuarioDestinatario;

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

        }
    }
}