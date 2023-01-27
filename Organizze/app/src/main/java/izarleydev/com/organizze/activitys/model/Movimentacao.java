package izarleydev.com.organizze.activitys.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import izarleydev.com.organizze.activitys.Helper.Base64Custom;
import izarleydev.com.organizze.activitys.Helper.DateCustom;
import izarleydev.com.organizze.activitys.config.ConfigFirebase;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    public Movimentacao() {
    }
    public void salvar(String dataEscolhida){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase();
        FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
        String idUsuario = Base64Custom.codificarBase64( auth.getCurrentUser().getEmail() );
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);

        reference.child("Movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
