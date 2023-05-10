package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import izarleydev.com.instagram.helper.ConfigFirebase;

public class PostagemCurtidas {
    public String idPostagem;
    public int qtdCurtidas = 0;
    public Usuario usuario;

    public PostagemCurtidas() {
    }

    public void salvar (){

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

        //Objeto usuario
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put("foto", usuario.getPhoto());
        dadosUsuario.put("nome", usuario.getName());

        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(getIdPostagem())
                .child(usuario.getId());
        pCurtidasRef.setValue(dadosUsuario);

        //atualizar quantidade de curtidas
        atualizarQtdCurtidas(1);
    }

    public void atualizarQtdCurtidas (int valor){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(getIdPostagem())
                .child("qtdCurtidas");
        setQtdCurtidas(getQtdCurtidas() + valor);
        pCurtidasRef.setValue(getQtdCurtidas());

    }

    public void removerQtdCurtidas () {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(getIdPostagem())
                .child(usuario.getId());
        pCurtidasRef.removeValue();

        //atualizar quantidade de curtidas
        atualizarQtdCurtidas(-1);

    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
