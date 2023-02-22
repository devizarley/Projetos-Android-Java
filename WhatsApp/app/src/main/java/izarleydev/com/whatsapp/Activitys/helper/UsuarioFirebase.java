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

public class UsuarioFirebase {

    public static String getIndentificadorUsuario () {
        FirebaseAuth usuario = ConfigFirebase.getAuth();
        String email = usuario.getCurrentUser().getEmail();
        String id = Base64.codBase64(email);

        return id;
    }

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfigFirebase.getAuth();
        return usuario.getCurrentUser();

    }

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

}
