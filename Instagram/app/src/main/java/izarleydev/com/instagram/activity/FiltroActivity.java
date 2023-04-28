package izarleydev.com.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.adapter.AdapterMiniaturas;
import izarleydev.com.instagram.helper.RecyclerItemClickListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        //configurações iniciais
        listaFiltros = new ArrayList<>();

        //componentes
        imagemFotoEscolhida = findViewById(R.id.imagemFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imagemFotoEscolhida.setImageBitmap(imagem);



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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}