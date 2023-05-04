package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import izarleydev.com.instagram.helper.ConfigFirebase;

public class Postagem implements Serializable {

    private String idUser;
    private String id;
    private String descricao;
    private String foto;

    public Postagem() {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference postagem = firebaseRef.child("postagens");
        String idPostagem = postagem.push().getKey();
        setId(idPostagem);

    }

    public boolean salvar (){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference postagensRef = firebaseRef.child("postagens")
                .child(getIdUser())
                .child(getId());
        postagensRef.setValue(this);
        return true;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
