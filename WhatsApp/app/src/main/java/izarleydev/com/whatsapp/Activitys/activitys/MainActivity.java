package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.R;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = ConfigFirebase.getAuth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                singOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void singOut (){
        try {
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}