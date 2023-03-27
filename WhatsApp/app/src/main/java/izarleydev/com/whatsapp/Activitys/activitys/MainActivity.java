package izarleydev.com.whatsapp.Activitys.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.fragment.ContatosFragment;
import izarleydev.com.whatsapp.Activitys.fragment.ConversasFragment;
import izarleydev.com.whatsapp.R;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth = ConfigFirebase.getAuth();
    androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contato", ContatosFragment.class)
                        .create()
        );
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        //Configuração do search view

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menuPesquisa).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                singOut();
                finish();
                break;
            case R.id.menuConfiguracoes:
                openConfig();
                break;
            case R.id.menuPesquisa:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void openConfig (){
        startActivity(new Intent(this, Configuracoes.class));
    }
    public void singOut (){
        try {
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}