package izarleydev.com.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import izarleydev.com.instagram.R;

public class FiltroActivity extends AppCompatActivity {

    private ImageView imagemFotoEscolhida;
    private Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        imagemFotoEscolhida = findViewById(R.id.imagemFotoEscolhida);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imagemFotoEscolhida.setImageBitmap(imagem);
        }

    }
}