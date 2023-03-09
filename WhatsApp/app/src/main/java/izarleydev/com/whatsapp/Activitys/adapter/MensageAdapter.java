package izarleydev.com.whatsapp.Activitys.adapter;

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

import java.util.List;

import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Mensagem;
import izarleydev.com.whatsapp.R;

public class MensageAdapter extends RecyclerView.Adapter<MensageAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensageAdapter(List<Mensagem> list, Context c) {
        this.mensagens = list;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;

        if (viewType == TIPO_REMETENTE){

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);

        } else if (viewType == TIPO_DESTINATARIO) {

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);

        }
        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensage();
        String image = mensagem.getImage();

        if (image != null){
            Uri url = Uri.parse(image);
            Glide.with(context).load(url).into(holder.imagem);
        }else {
            holder.textMensagem.setText(msg);
        }


    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagens.get(position);
        String idUser = UsuarioFirebase.getIndentificadorUsuario();

        if (idUser.equals(mensagem.getIdUser())){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textMensagem;
        ImageView imagem;

        public MyViewHolder(View view){
            super(view);

            textMensagem = view.findViewById(R.id.textMsg);
            imagem = view.findViewById(R.id.imageMsg);
        }
    }

}
