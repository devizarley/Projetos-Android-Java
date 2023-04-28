package izarleydev.com.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.model.Usuario;

public class AdapterMiniaturas extends RecyclerView.Adapter<AdapterMiniaturas.MyViewHolder> {

    private List<ThumbnailItem> listaFiltros;
    private Context context;

    public AdapterMiniaturas(List<ThumbnailItem> listaFiltros, Context context) {
        this.listaFiltros = listaFiltros;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros, parent, false);
        return new AdapterMiniaturas.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem item = listaFiltros.get(position);
        holder.foto.setImageBitmap(item.image);
        holder.nome.setText(item.filterName);
    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageFiltro);
            nome = itemView.findViewById(R.id.textFiltro);

        }
    }
}
