package izarleydev.com.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.helper.ConfigFirebase;

public class Usuario implements Serializable {

    private String name;
    private String email;
    private String senha;
    private String id;
    private String photo;
    private int seguidores = 0;
    private int seguindo = 0;
    private int publicacoes = 0;

    public Usuario() {
    }

    public void Salvar(){
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuario = databaseReference.child("usuarios").child(getId());

        usuario.setValue(this);
    }

    public void atualizar(){

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();

        Map objeto = new HashMap();
        objeto.put("/usuarios/" + getId() + "/nome", getName());
        objeto.put("/usuarios/" + getId() + "/photo", getPhoto());

        firebase.updateChildren(objeto);

    }

    @Exclude
    public Map<String, Object> converterMap(){

        HashMap<String, Object> user = new HashMap<>();

        user.put("email", getEmail());
        user.put("name", getName());
        user.put("id", getId());
        user.put("photo", getPhoto());
        user.put("seguidores", getSeguidores());
        user.put("seguindo", getSeguindo());
        user.put("publicacoes", getPublicacoes());

        return  user;
    }


    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(int publicacoes) {
        this.publicacoes = publicacoes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name.toUpperCase();
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
