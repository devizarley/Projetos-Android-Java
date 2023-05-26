package izarleydev.com.instagram.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.activity.EditProfileActivity;
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

        Map objeto = new HashMap();
        if (getPhoto() == null & getPhoto() == ""){
            objeto.put("photo", R.drawable.avatar);
        }else {
            objeto.put("photo", getPhoto());
        }
        objeto.put("name", getName());
        objeto.put("email", getEmail());
        objeto.put("id", getId());
        objeto.put("seguidores", getSeguidores());
        objeto.put("seguindo", getSeguindo());
        objeto.put("publicacoes", getPublicacoes());


        usuario.setValue(objeto);
    }

    public void atualizar(){

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();

        Map objeto = new HashMap();
        objeto.put("/usuarios/" + getId() + "/name", getName());
        objeto.put("/usuarios/" + getId() + "/photo", getPhoto());

        firebase.updateChildren(objeto);

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
