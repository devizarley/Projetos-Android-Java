package izarleydev.com.whatsapp.Activitys.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.model.Conversas;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public ContatosAdapter(List<Usuario> listContatos, Context c) {

        this.contatos = listContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Usuario usuario = contatos.get(position); // REVISAR

        holder.nome.setText(usuario.getNome());
        holder.email.setText(usuario.getEmail());

        if (usuario.getPhoto() != null) {
            Uri uri = Uri.parse(usuario.getPhoto());
            Glide.with(context).load(uri).into(holder.profile);
        }else {
            holder.profile.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, email;
        CircleImageView profile;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            profile = itemView.findViewById(R.id.imageViewContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
