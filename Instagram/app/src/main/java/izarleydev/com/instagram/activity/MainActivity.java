package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth ;
    androidx.appcompat.widget.Toolbar toolbar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        auth = ConfigFirebase.getAuth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sair:
                signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut (){
        try {
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}