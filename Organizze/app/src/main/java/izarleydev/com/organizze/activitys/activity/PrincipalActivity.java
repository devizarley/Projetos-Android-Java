package izarleydev.com.organizze.activitys.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.Helper.Base64Custom;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;
import izarleydev.com.organizze.activitys.model.Usuario;
import izarleydev.com.organizze.databinding.ActivityPrincipalBinding;

public class PrincipalActivity extends AppCompatActivity {
    private FloatingActionButton fabDespesa;
    private FloatingActionButton fabReceita;
    private MaterialCalendarView calendarView;
    private ActivityPrincipalBinding binding;
    private FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
    private TextView textIntro, textSaldo;
    private DatabaseReference referenceDb = ConfigFirebase.getFirebaseDatabase();
    private Double saldoTotal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        binding.toolbar.setTitle("");
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        fabDespesa = findViewById(R.id.fab);
        fabReceita = findViewById(R.id.fab2);
        calendarView = findViewById(R.id.calendarView);
        textIntro = findViewById(R.id.textIntro);
        textSaldo = findViewById(R.id.textSaldo);

        configuraCalendarView();
        setarInfos();

        fabDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, DespesasActivity.class));
            }
        });
        fabReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                auth = ConfigFirebase.getFirebaseAuth();
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configuraCalendarView() {
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }

    public void setarInfos(){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = referenceDb.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })

        textIntro.setText("Olá, "+ usuario.getNome());
    }
}