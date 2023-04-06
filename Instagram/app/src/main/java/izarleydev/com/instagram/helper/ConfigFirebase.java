package izarleydev.com.instagram.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference storageReference;

    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return  databaseReference;
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getAuth (){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    //retorna a instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

}
