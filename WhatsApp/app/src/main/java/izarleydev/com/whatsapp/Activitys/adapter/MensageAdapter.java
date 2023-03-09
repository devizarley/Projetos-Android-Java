package izarleydev.com.whatsapp.Activitys.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Mensagem;

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

        if (viewType == TIPO_REMETENTE){

            View item = LayoutInflater.from(parent.getContext()).inflate();

        } else if (viewType == TIPO_DESTINATARIO) {

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
        public MyViewHolder(View view){
            super(view);
        }
    }

}
