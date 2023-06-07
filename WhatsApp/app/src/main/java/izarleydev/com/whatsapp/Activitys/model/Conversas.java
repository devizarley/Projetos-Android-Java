package izarleydev.com.whatsapp.Activitys.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;

public class Conversas {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;
    private Usuario usuarioExibicaoLogado;
    private Mensagem mensagem;
    private String idConversa;

    public Conversas() {
    }

    //cria toda construção no firebase
    public void salvar(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("idDestinatario", getIdDestinatario());
        user.put("idRemetente", getIdRemetente());

        db.collection("conversas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // A conversa já existe, adicionar mensagem à subcoleção "mensagens"
                                DocumentSnapshot conversaDoc = querySnapshot.getDocuments().get(0);
                                String conversaId = conversaDoc.getId();
                                adicionarMensagem(conversaId, mensagem.getMensage());
                            } else {
                                // A conversa não existe, criar novo documento de conversa e adicionar mensagem
                                Map<String, Object> conversa = new HashMap<>();
                                conversa.put("idDestinatario", getIdDestinatario());
                                conversa.put("idRemetente", getIdRemetente());
                                String idConversa = UUID.randomUUID().toString(); // Gerar um ID único para a conversa
                                conversa.put("idConversa", idConversa);

                                db.collection("conversas")
                                        .document(idConversa) // Definir o ID da conversa
                                        .set(conversa)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Teste", "Conversa adicionada com sucesso");
                                                adicionarMensagem(idConversa, mensagem.getMensage());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Teste", "Erro ao adicionar conversa", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w("Teste", "Erro ao buscar conversa", task.getException());
                        }
                    }
                });
    }
    private void adicionarMensagem(String conversaId, String mensagem) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String mensagemId = UUID.randomUUID().toString();


        Map<String, Object> objetoMensagem = new HashMap<>();
        objetoMensagem.put("mensagem", mensagem);
        if (getMensagem().getImage() != null){
            objetoMensagem.put("imgMensagem", getMensagem().getImage());
        }
        objetoMensagem.put("idRemetente", getIdRemetente());
        objetoMensagem.put("timestamp", FieldValue.serverTimestamp());

        db.collection("conversas")
                .document(conversaId)
                .collection("mensagens")
                .document(mensagemId)
                .set(objetoMensagem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Teste", "Mensagem adicionada com sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Teste", "Erro ao adicionar mensagem", e);
                    }
                });
    }

    public String getIdConversa() {
        return idConversa;
    }

    public void setIdConversa(String idConversa) {
        this.idConversa = idConversa;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuarioExibicaoLogado() {
        return usuarioExibicaoLogado;
    }

    public void setUsuarioExibicaoLogado(Usuario usuarioExibicaoLogado) {
        this.usuarioExibicaoLogado = usuarioExibicaoLogado;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
}
