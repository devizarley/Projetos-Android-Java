package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.helper.ConfigFirebase;

public class Usuario {

    private String name;
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

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference ref = firebase.child("usuarios").child(getId());

        Map<String, Object> valueUser = converterMap();

        ref.updateChildren(valueUser);

    }

    @Exclude
    public Map<String, Object> converterMap(){
        HashMap<String, Object> user = new HashMap<>();
        user.put("email", getEmail());
        user.put("name", getName());
        user.put("id", getId());
        user.put("photo", getPhoto());

        return  user;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
