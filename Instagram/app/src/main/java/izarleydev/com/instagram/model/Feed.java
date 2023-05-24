package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;

public class Feed {

    private String id;
    private String fotoPostagem;
    private String descricao;
    private String nomeUsuario;
    private String fotoUsuario;
    private String iduser;

    public Feed() {
    }

    public void atualizar(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();

        Map objeto = new HashMap();
        objeto.put("/fotoUsuario", getFotoUsuario());

        firebase.updateChildren(objeto);
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFotoPostagem() {
        return fotoPostagem;
    }

    public void setFotoPostagem(String fotoPostagem) {
        this.fotoPostagem = fotoPostagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }
}
