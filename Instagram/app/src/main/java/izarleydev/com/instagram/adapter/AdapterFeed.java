package izarleydev.com.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.ComentarioActivity;
import izarleydev.com.instagram.activity.PostagemActivity;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;
import izarleydev.com.instagram.model.Feed;
import izarleydev.com.instagram.model.PostagemCurtidas;
import izarleydev.com.instagram.model.Usuario;

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
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();

        //Carrega dados do feed
        Uri uriFotoUsuario = Uri.parse(feed.getFotoUsuario());
        Uri uriFotoPostagem = Uri.parse(feed.getFotoPostagem());

        Glide.with(context).load(uriFotoUsuario).into(holder.fotoPerfil);
        Glide.with(context).load(uriFotoPostagem).into(holder.fotoPostagem);

        holder.descricao.setText(feed.getDescricao());
        holder.nome.setText(feed.getNomeUsuario());

        /*
        * postagens-curtidas
        *   +id_postagem
        *       +qtdCurtidas
        *       +id_usuario
        *           nome_usuario
        *           caminho_foto
        * */

        //Recuperar dados da postagem curtida
        DatabaseReference curtidas = ConfigFirebase.getFirebaseDatabase()
                .child("postagens-curtidas")
                .child(feed.getId());

        curtidas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int qtdCurtidas = 0;
                if (snapshot.hasChild("qtdCurtidas")){

                    PostagemCurtidas postagemCurtida = snapshot.getValue(PostagemCurtidas.class);
                    qtdCurtidas = postagemCurtida.getQtdCurtidas();

                }

                //Verificar se já foi clicado em curtir pelo id do usuario
                if (snapshot.hasChild(usuarioLogado.getId())) {
                    holder.likeButton.setLiked(true);
                }else {
                    holder.likeButton.setLiked(false);
                }

                //Objeto postagem curtida
                PostagemCurtidas curtida = new PostagemCurtidas();
                curtida.setIdPostagem(feed.getId());
                curtida.setUsuario(usuarioLogado);
                curtida.setQtdCurtidas(qtdCurtidas);

                holder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        curtida.salvar();
                        holder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        curtida.removerQtdCurtidas();
                        holder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");
                    }
                });
                holder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");

                holder.visualizarComentario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ComentarioActivity.class);
                        i.putExtra("idPostagem", feed.getId());
                        context.startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
