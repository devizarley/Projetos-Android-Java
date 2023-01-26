package izarleydev.com.organizze.activitys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import izarleydev.com.organizze.R;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

    Button button;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_4)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_cadastro)
                .build());
    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, Cadastro_Activity.class));
    }
    public void btEntrar(View view) {
        startActivity(new Intent(this, Login_Activity.class));
    }

    public void verificarUsuarioLogado () {
        auth = ConfigFirebase.getFirebaseAuth();
        if (auth.getCurrentUser() != null ){
            openActivityPrincipal();
        }
    }


    //utilizado para ao cadastrar, ir direto para o conteudo principal.
    public void onStart(){
        super.onStart();
        verificarUsuarioLogado();
    }

    public void openActivityPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

}

















