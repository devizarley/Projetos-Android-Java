package izarleydev.com.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.model.Usuario;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> {

    private List<Usuario> listUsuario;
    private Context context;

    public AdapterSearch(List<Usuario> listUsuario, Context context) {
        this.listUsuario = listUsuario;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa_usuario, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = listUsuario.get(position);

        holder.nome.setText(usuario.getName());

        if (usuario.getPhoto() != null) {
            Uri uri = Uri.parse( usuario.getPhoto() );
            Glide.with(context).load(uri).into(holder.foto);
        }else {
            holder.foto.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return listUsuario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageSearch);
            nome = itemView.findViewById(R.id.textUserSearch);
        }
    }
}
