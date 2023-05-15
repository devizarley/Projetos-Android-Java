package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.databinding.ActivityMainBinding;
import izarleydev.com.instagram.fragment.AccountFragment;
import izarleydev.com.instagram.fragment.AddFragment;
import izarleydev.com.instagram.fragment.HomeFragment;
import izarleydev.com.instagram.fragment.SearchFragment;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.Permissao;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth ;
    ActivityMainBinding binding;
    private FloatingActionButton ic_add;
    private Toolbar toolbar2;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = ConfigFirebase.getAuth();

        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("");
        setSupportActionBar(toolbar2);

        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        /*FloatingActionButton fab = findViewById(R.id.ic_add);
        fab.setImageResource(R.drawable.ic_add_white);
        fab.setColorFilter(ContextCompat.getColor(this, R.color.fabStyle), PorterDuff.Mode.SRC_IN);*/

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        ic_add = findViewById(R.id.ic_add);
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener( item -> {

            switch (item.getItemId()) {
                case R.id.ic_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.ic_pesquisa:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.ic_account:
                    replaceFragment(new AccountFragment());
                    break;
                case R.id.ic_cam:
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (i.resolveActivity(getApplicationContext().getPackageManager()) != null){
                        startActivityForResult(i,SELECAO_CAMERA);
                    }
                    break;
            }

            return true;

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.sair);
        Drawable icon = menuItem.getIcon();
        icon.setColorFilter(ContextCompat.getColor(this, R.color.icon_color), PorterDuff.Mode.SRC_IN);
        menuItem.setIcon(icon);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sair:
                signOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instagram");
        builder.setMessage("Deseja realmente deslogar?");

        builder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.getCurrentUser();
                        auth.signOut();
                        finish();
                    }
                });

        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout storiesLayout = dialog.findViewById(R.id.layoutStories);
        LinearLayout feedLayout = dialog.findViewById(R.id.layoutFeed);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        storiesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Esta função está em construção!",Toast.LENGTH_SHORT).show();

            }
        });

        feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getApplicationContext().getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }

                dialog.dismiss();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Esta função está em construção!",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == this.RESULT_OK ){

            Bitmap imagem = null;

            try {
                //validar tipo de selcao
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), localImagem);
                        break;
                }

                //validar imagem selecionada

                if (imagem != null){
                    //converte imagem em byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //envia imagem escolhida para aplicação de filtro
                    Intent i = new Intent(getApplicationContext(), FiltroActivity.class);
                    i.putExtra("fotoEscolhida", dadosImagem);
                    startActivity(i);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
