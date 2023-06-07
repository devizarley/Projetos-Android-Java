package izarleydev.com.whatsapp.Activitys.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;
import izarleydev.com.whatsapp.Activitys.model.Conversas;
import izarleydev.com.whatsapp.Activitys.model.Usuario;
import izarleydev.com.whatsapp.R;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversas> conversas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ConversasAdapter(List<Conversas> lista, Context c, OnItemClickListener listener) {
        this.conversas = lista;
        this.context = c;
        this.onItemClickListener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    public interface OnItemClickListener {
        void onItemClick(Conversas conversa);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversas conversa = conversas.get(position);
        String idConversa = conversa.getIdConversa();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference conversasRef = db.collection("conversas");
        DocumentReference conversaRef = conversasRef.document(idConversa);

        conversaRef.collection("mensagens")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot mensagemSnapshot = querySnapshot.getDocuments().get(0);
                        Map<String, Object> mensagemData = mensagemSnapshot.getData();

                        if (mensagemData != null) {
                            String mensagem = (String) mensagemData.get("mensagem");
                            // Exiba a última mensagem na ViewHolder
                            holder.ultimaMensagem.setText(mensagem);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("ERROR", "onBindViewHolder: " + e);
                });

        conversaRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> conversaData = documentSnapshot.getData();

                if (conversaData != null) {
                    String idDestinatario = (String) conversaData.get("idDestinatario");
                    String idRemetente = (String) conversaData.get("idRemetente");

                    // Exemplo:
                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                    CollectionReference usuariosRef = db2.collection("usuario");

                    // Recupera os dados do usuário remetente
                    usuariosRef.document(idRemetente).get().addOnSuccessListener(remetenteSnapshot -> {
                        if (remetenteSnapshot.exists()) {
                            Usuario remetente = remetenteSnapshot.toObject(Usuario.class);
                            if (remetente != null) {
                                holder.nome.setText(remetente.getNome());
                            }
                        }
                    });

                    usuariosRef.document(idDestinatario).get().addOnSuccessListener(destinatarioSnapshot -> {
                        if (destinatarioSnapshot.exists()) {
                            Usuario destinatario = destinatarioSnapshot.toObject(Usuario.class);
                            if (destinatario != null) {
                                holder.nome.setText(destinatario.getNome());
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> {
            Log.d("ERROR", "onBindViewHolder: " + e);
        });
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        TextView nome, ultimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.imagePhoto);
            nome = itemView.findViewById(R.id.textTitulo);
            ultimaMensagem = itemView.findViewById(R.id.textSubTitulo);
        }
    }

}
