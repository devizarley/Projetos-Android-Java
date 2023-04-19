package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

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
        DatabaseReference usuario = databaseReference.child("usuario").child(getId());

        usuario.setValue(this);
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
