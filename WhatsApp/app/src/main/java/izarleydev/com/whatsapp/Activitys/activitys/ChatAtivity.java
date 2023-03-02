package izarleydev.com.whatsapp.Activitys.activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import izarleydev.com.whatsapp.databinding.ActivityChatAtivityBinding;

public class ChatAtivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityChatAtivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatAtivityBinding.inflate(getLayoutInflater());
        binding.toolbarChat.setTitle("");
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarChat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}