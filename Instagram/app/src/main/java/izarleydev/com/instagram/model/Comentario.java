package izarleydev.com.instagram.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import izarleydev.com.instagram.helper.ConfigFirebase;

public class Comentario {

    private String idComentario;
    private String idPostagem;
    private String idUsuario;
    private String caminhoFoto;
    private String nomeUsuario;
    private String comentario;

    public Comentario() {
    }

    public boolean salvar (){

        /*
        * comentarios
            +id_postagem
                +id_comentario
                    comentario
        * */

        DatabaseReference comentariosRef = ConfigFirebase.getFirebaseDatabase()
                .child("comentarios")
                .child(getIdPostagem());

        String chave = comentariosRef.push().getKey();
        setIdComentario(chave);
        comentariosRef.child(getIdComentario()).setValue(this);

        return true;
    }

    public void atualizarFoto(String idUsuario, String urlFotoUsuario) {
        DatabaseReference comentariosRef = ConfigFirebase.getFirebaseDatabase()
                .child("comentarios");

        comentariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Dentro desse for ele está retornando todo nó de comentarios
                //Log.d("TESTEEE", "onDataChange: " + snapshot);

                for (DataSnapshot ds : snapshot.getChildren()) {

                    //Dentro desse for ele esta retornando a lista de todo s os comentarios da postagem
                    //Log.d("TESTEEE", "onDataChange: " + ds);

                    for (DataSnapshot comentarioSnapshot : ds.getChildren()) {
                        //Dentro desse for ele esta retornando a lista de todo s os comentarios da postagem de forma separada
                        //Log.d("TESTEEE", "onDataChange: " + comentarioSnapshot);

                        Comentario comentario = comentarioSnapshot.getValue(Comentario.class);
                        //se nos comentarios percorridos haver um IdUsuario igual ao IdUsuario logado ele faz a atualização da imagem
                        if (comentario != null && comentario.getIdUsuario().equals(idUsuario)) {
                            comentario.setCaminhoFoto(urlFotoUsuario);
                            comentarioSnapshot.getRef().setValue(comentario);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "onCancelled: " + error);
            }
        });
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
