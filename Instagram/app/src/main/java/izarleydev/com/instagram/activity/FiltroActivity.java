package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.adapter.AdapterMiniaturas;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.RecyclerItemClickListener;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Postagem;
import izarleydev.com.instagram.model.Usuario;

public class FiltroActivity extends AppCompatActivity {
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }
    private ImageView imagemFotoEscolhida;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private Toolbar toolbar;
    private List<ThumbnailItem> listaFiltros;
    private RecyclerView recyclerFiltros;
    private AdapterMiniaturas adapterMiniaturas;
    private DatabaseReference usuariosRef, firebaseRef, usuarioLogadoRef;
    private Usuario publicacoesRef, usuarioLogado;
    private StorageReference storageReference;
    private TextInputEditText inputDescricao;
    private String idUserLogado;
    private AlertDialog dialog;
    private DataSnapshot seguidoresSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        //configurações iniciais
        configuracoesIniciais();

        //componentes
        androidComponentes();
        firebaseComponentes();

        //Recuperar dados para uma nova postagem
        recuperarDadosPostagem();

        //toolbar
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imagemFotoEscolhida.setImageBitmap(imagem);
            imagemFiltro = imagem.copy(imagem.getConfig(), true);

            //configuração recyclerView

            adapterMiniaturas = new AdapterMiniaturas(listaFiltros, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerFiltros.setLayoutManager(layoutManager);
            recyclerFiltros.setAdapter(adapterMiniaturas);

            //Adiciona evento de click no recyclerView
            recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(
                    getApplicationContext(),
                    recyclerFiltros,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            ThumbnailItem item = listaFiltros.get(position);

                            imagemFiltro = imagem.copy(imagem.getConfig(), true);
                            Filter filter = item.filter;
                            imagemFotoEscolhida.setImageBitmap(filter.processFilter(imagemFiltro));
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }
            ));

            recuperarFiltros();

        }
    }

    private void abrirDialogCarregamento (String titulo){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setCancelable(false);
        alert.setView(R.layout.carregamento);

        dialog = alert.create();
        dialog.show();

    }

    private void recuperarDadosPostagem(){
        abrirDialogCarregamento("Carregando dados, aguarde!");
        usuarioLogadoRef = usuariosRef.child(idUserLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Recupera dados de usuario logado
                        usuarioLogado = snapshot.getValue(Usuario.class);

                        //Recuperar seguidores
                        DatabaseReference seguidoresRef = firebaseRef.child("seguidores")
                                .child(idUserLogado);
                        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                seguidoresSnapshot = snapshot;
                                dialog.cancel();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    private void recuperarFiltros(){

        listaFiltros.clear();
        ThumbnailsManager.clearThumbs();

        //Configurar filtro normal
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";

        ThumbnailsManager.addThumb(item);

        //Lista todos filtros
        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

        for (Filter filter: filters){

            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filter;
            itemFiltro.filterName = filter.getName();

            ThumbnailsManager.addThumb(itemFiltro);

        }

        listaFiltros.addAll( ThumbnailsManager.processThumbs(getApplicationContext()) );
        adapterMiniaturas.notifyDataSetChanged();

    }

    private void publicarPostagem(){

        abrirDialogCarregamento ("Salvando postagem!");
        String textDescricao = inputDescricao.getText().toString();

        //instanciar o objeto postagem
        Postagem postagem = new Postagem();
        postagem.setIdUser(idUserLogado);
        postagem.setDescricao(textDescricao);

        //Recuperar dados da imagem para o firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        StorageReference imagemRef = storageReference
                .child("imagens")
                .child("postagens")
                .child(postagem.getId()+ ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FiltroActivity.this,
                        "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        postagem.setFoto(url.toString());

                        int qntPublicacoes = publicacoesRef.getPublicacoes() + 1;

                        HashMap<String, Object> dadosPublicacoes = new HashMap<>();
                        dadosPublicacoes.put("publicacoes", qntPublicacoes);

                        DatabaseReference usuarioAtual = usuariosRef
                                .child(idUserLogado);
                        usuarioAtual.updateChildren(dadosPublicacoes);

                        //salvar postagem
                        if (postagem.salvar(seguidoresSnapshot)){

                            Toast.makeText(FiltroActivity.this, "Postagem publicada com sucesso!", Toast.LENGTH_SHORT).show();

                            dialog.cancel();
                            finish();
                        }
                    }
                });
            }
        });

    }

    private void configuracoesIniciais(){
        listaFiltros = new ArrayList<>();
    }

    private void androidComponentes(){
        toolbar = findViewById(R.id.toolbar);
        imagemFotoEscolhida = findViewById(R.id.imagemFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        inputDescricao = findViewById(R.id.textDesricao);
    }

    private void firebaseComponentes (){
        firebaseRef = ConfigFirebase.getFirebaseDatabase();
        usuariosRef = firebaseRef.child("usuarios");
        idUserLogado = UsuarioFirebase.getIdUsuario();
        storageReference = ConfigFirebase.getFirebaseStorage();
        publicacoesRef = new Usuario();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.publicar:
                publicarPostagem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }

}