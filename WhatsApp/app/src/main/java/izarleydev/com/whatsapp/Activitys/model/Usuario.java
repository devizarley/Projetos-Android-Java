package izarleydev.com.whatsapp.Activitys.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String id;
    public Usuario() {
    }
    public void Salvar(){
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuario = databaseReference.child("usuarios").child(getId());

        usuario.setValue(this);
    }
    @Exclude
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
