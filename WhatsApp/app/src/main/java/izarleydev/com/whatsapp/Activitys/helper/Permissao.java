package izarleydev.com.whatsapp.Activitys.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23){
            List<String> listPermissions = new ArrayList<>();

            /*
            * Percorre as permissões passadas,
            * verificando uma a uma se já tem
            * a permissao liberada*/

            for (String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao) listPermissions.add(permissao);
            }

            //Caso a lista esteja vazia, não é necessário solicitar permissão
            if ( listPermissions.isEmpty()) return true;

            String[] novasPermissçoes = new String[listPermissions.size()];
            listPermissions.toArray(novasPermissçoes);

            //solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissçoes, requestCode );

        }

        return true;
    };

}
