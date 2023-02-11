package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import izarleydev.com.whatsapp.R;

public class MainActivity extends AppCompatActivity {

    /*Toda configuração geral*/
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    public void btnavegar (View view) {
        startActivity(new Intent(MainActivity.this, aaaa.class));
    }

}