package izarleydev.com.organizze.activitys.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import izarleydev.com.organizze.activitys.config.ConfigFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private Double receitaTotal = 0.00;
    private Double despesaTotal = 0.00;
    private String categoria;
    private String descricao;

    public Usuario() {
    }
    public void salvar(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase();
        reference.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);
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

    public Double getReceitaTotal() {
        return receitaTotal;
    }
    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }
    public Double getDespesaTotal() {
        return despesaTotal;
    }
    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
