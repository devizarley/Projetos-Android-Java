package izarleydev.com.whatsapp.Activitys.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;
import izarleydev.com.whatsapp.Activitys.model.Usuario;

public class UsuarioFirebase {

    //Retorna email do usuario criptografado
    public static String getIndentificadorUsuario () {
        FirebaseAuth usuario = ConfigFirebase.getAuth();
        String email = usuario.getCurrentUser().getEmail();
        String id = Base64.codBase64(email);

        return id;
    }

    //Objeto para retornar dados do usuario
    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfigFirebase.getAuth();
        return usuario.getCurrentUser();

    }


    //objeto para atualizar o nome
    public static boolean atualizarNomeUsuario(String nome) {
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //Atualiza a foto do usuario
    public static boolean atualizarFotoUsuario(Uri url) {
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil");
                    }
                }
            });
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //Objeto para atualizar dados do usuario logado
    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser user = getUsuarioAtual();

        Usuario dadosUser = new Usuario();
        dadosUser.setEmail(user.getEmail());
        dadosUser.setNome(user.getDisplayName());

        if ( user.getPhotoUrl() == null){
            dadosUser.setPhoto("");
        }else {
            dadosUser.setPhoto(user.getPhotoUrl().toString());
        }

        return dadosUser;
    }
}
