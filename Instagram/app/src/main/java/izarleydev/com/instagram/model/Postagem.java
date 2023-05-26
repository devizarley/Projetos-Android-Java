package izarleydev.com.instagram.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;

public class Postagem implements Serializable {

    private String nomeUsuarioAutor;
    private String fotoPerfilAutor;
    private String idUsuarioAutor;
    private String fotoPostagem;
    private String idPostagem;
    private String descricao;

    public Postagem() {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference postagem = firebaseRef.child("postagens");

        String idPostagem = postagem.push().getKey();
        setIdPostagem(idPostagem);

    }

    public boolean salvar (){

        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

        setFotoPerfilAutor(usuarioLogado.getPhoto());
        setIdUsuarioAutor(usuarioLogado.getId());
        setNomeUsuarioAutor(usuarioLogado.getName());

        //Referencia para postagem
        String combinacaoId = "/" + getIdUsuarioAutor() + "/" + getIdPostagem();
        objeto.put("/postagens" + combinacaoId, this);

        firebaseRef.updateChildren(objeto);
        return true;

    }

    public String getNomeUsuarioAutor() {
        return nomeUsuarioAutor;
    }

    public void setNomeUsuarioAutor(String nomeUsuarioAutor) {
        this.nomeUsuarioAutor = nomeUsuarioAutor;
    }

    public String getFotoPerfilAutor() {
        return fotoPerfilAutor;
    }

    public void setFotoPerfilAutor(String fotoPerfilAutor) {
        this.fotoPerfilAutor = fotoPerfilAutor;
    }

    public String getIdUsuarioAutor() {
        return idUsuarioAutor;
    }

    public void setIdUsuarioAutor(String idUsuarioAutor) {
        this.idUsuarioAutor = idUsuarioAutor;
    }

    public String getFotoPostagem() {
        return fotoPostagem;
    }

    public void setFotoPostagem(String fotoPostagem) {
        this.fotoPostagem = fotoPostagem;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
