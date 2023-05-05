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
import com.like.LikeButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.model.Feed;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Feed> listFeed;
    private Context context;

    public AdapterFeed(List<Feed> listFeed, Context context) {

        this.listFeed = listFeed;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Feed feed = listFeed.get(position);

        //Carrega dados do feed
        Uri uriFotoUsuario = Uri.parse(feed.getFotoUsuario());
        Uri uriFotoPostagem = Uri.parse(feed.getFotoPostagem());

        Glide.with(context).load(uriFotoUsuario).into(holder.fotoPerfil);
        Glide.with(context).load(uriFotoPostagem).into(holder.fotoPostagem);

        holder.descricao.setText(feed.getDescricao());
        holder.nome.setText(feed.getNomeUsuario());

    }

    @Override
    public int getItemCount() {
        return listFeed.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView fotoPerfil;
        TextView nome, descricao, qtdCurtidas;
        ImageView fotoPostagem, visualizarComentario;
        LikeButton likeButton;

        public MyViewHolder(View view){
            super(view);

            fotoPerfil = view.findViewById(R.id.imagemPerfil);
            nome = view.findViewById(R.id.textNameUsuario);
            fotoPostagem = view.findViewById(R.id.imagemPostagem);
            descricao = view.findViewById(R.id.textDescricaoPostagem);
            qtdCurtidas = view.findViewById(R.id.textCurtidas);
            visualizarComentario = view.findViewById(R.id.imageComentario);
            likeButton = view.findViewById(R.id.likeButtonFeed);

        }

    }


}
