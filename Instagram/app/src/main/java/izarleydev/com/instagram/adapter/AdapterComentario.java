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
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Comentario;
import izarleydev.com.instagram.model.Usuario;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.MyViewHolder> {

    private List<Comentario> listComentario;
    private Context context;

    public AdapterComentario(List<Comentario> listComentario, Context context) {

        this.listComentario = listComentario;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comentario, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comentario comentario = listComentario.get(position);

        Glide.with(context).load(comentario.getCaminhoFoto()).into(holder.fotoPerfil);

        holder.nome.setText(comentario.getNomeUsuario());
        holder.comentario.setText(comentario.getComentario());

    }

    @Override
    public int getItemCount() {
        return listComentario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView fotoPerfil;
        TextView nome, comentario;

        public MyViewHolder(View view){
            super(view);

            fotoPerfil = view.findViewById(R.id.imageUserComentario);
            nome = view.findViewById(R.id.textNameUser);
            comentario = view.findViewById(R.id.textComentario);

        }

    }

}
