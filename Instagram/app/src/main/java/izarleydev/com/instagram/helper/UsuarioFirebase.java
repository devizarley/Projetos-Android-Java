package izarleydev.com.instagram.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import izarleydev.com.instagram.model.Usuario;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual (){
        FirebaseAuth usuario = ConfigFirebase.getAuth();
        return usuario.getCurrentUser();
    }

    public static String getIdUsuario (){
        return getUsuarioAtual().getUid();
    }

    public static Usuario getDadosUsarioLogado (){
        FirebaseUser userLogado = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(userLogado.getEmail());
        usuario.setName(userLogado.getDisplayName());
        usuario.setId(userLogado.getUid());

        if (userLogado.getPhotoUrl() == null) {
            usuario.setPhoto("");
        }else {
            usuario.setPhoto( userLogado.getPhotoUrl().toString() );
        }

        return usuario;
    }

    public static boolean atualizarNomeUsuario(String name){

        try {

            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
            return true;

        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarFotoUsuario(Uri url){

        try {

            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar a foto de perfil");
                    }
                }
            });
            return true;

        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
    }
}
