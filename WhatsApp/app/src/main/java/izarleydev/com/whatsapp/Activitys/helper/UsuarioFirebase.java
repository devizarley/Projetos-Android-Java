package izarleydev.com.whatsapp.Activitys.helper;

import com.google.firebase.auth.FirebaseAuth;

import izarleydev.com.whatsapp.Activitys.config.ConfigFirebase;

public class UsuarioFirebase {

    public static String getIndentificadorUsuario () {
        FirebaseAuth usuario = ConfigFirebase.getAuth();
        String email = usuario.getCurrentUser().getEmail();
        String id = Base64.codBase64(email);

        return id;
    }

}
