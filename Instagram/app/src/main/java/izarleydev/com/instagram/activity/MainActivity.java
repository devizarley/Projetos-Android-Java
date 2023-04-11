package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = ConfigFirebase.getAuth();
        auth.getCurrentUser();
        auth.signOut();
    }

}