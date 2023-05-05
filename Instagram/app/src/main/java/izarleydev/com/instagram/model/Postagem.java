package izarleydev.com.instagram.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.helper.ConfigFirebase;
import izarleydev.com.instagram.helper.UsuarioFirebase;

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

    public boolean salvar (DataSnapshot snapshot){
        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsarioLogado();

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

        //Referencia para postagem
        String combinacaoId = "/" + getIdUser() + "/" + getId();
        objeto.put("/postagens" + combinacaoId, this);

        //Referencia pra postagem

        for (DataSnapshot seguidores: snapshot.getChildren()){

            /*
            * +feed
            *   +id_seguidor
            *       +id_postagem
            *           postagem
            * */

            String idSeguidor = seguidores.getKey();

            /*Objeto para salvar*/

            HashMap<String, Object> dadosSeguidor = new HashMap<>();
            //dados da postagem do usuario
            dadosSeguidor.put("fotoPostagem", getFoto());
            dadosSeguidor.put("descricao", getDescricao());
            dadosSeguidor.put("id", getId());
            //dados do usuario
            dadosSeguidor.put("nomeUsuario", usuarioLogado.getName());
            dadosSeguidor.put("fotoUsuario", usuarioLogado.getPhoto());

            String idsAtualizacao = "/" + idSeguidor + "/" + getId();
            objeto.put("/feed" + idsAtualizacao, dadosSeguidor);

        }

        firebaseRef.updateChildren(objeto);
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
