package izarleydev.com.whatsapp.Activitys.model;

import com.google.firebase.database.DatabaseReference;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;

public class Conversas {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;

    public Conversas() {
    }

    //cria toda construção no firebase
    public void salvar(){
        DatabaseReference database = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference conversaRef = database.child("Conversas");

        conversaRef.child(this.getIdRemetente()).child(this.getIdDestinatario()).setValue( this );

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

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
}
