package izarleydev.com.whatsapp.Activitys.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.helper.UsuarioFirebase;

public class Usuario implements Serializable {
    private String nome;
    private String email;
    private String senha;
    private String id;
    private String photo;

    public Usuario() {
    }

    public void Salvar(){

        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuario = databaseReference.child("usuarios").child(getId());

        usuario.setValue(this);
    }

    public void atualizar(){

        String idUser = UsuarioFirebase.getIndentificadorUsuario();
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference ref = firebase.child("usuarios")
                .child(idUser);


        Map<String, Object> valueUser = converterMap();

        ref.updateChildren(valueUser);

    }

    @Exclude
    public Map<String, Object> converterMap(){
        HashMap<String, Object> user = new HashMap<>();
        user.put("email", getEmail());
        user.put("nome", getNome());
        user.put("photo", getPhoto());

        return  user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
