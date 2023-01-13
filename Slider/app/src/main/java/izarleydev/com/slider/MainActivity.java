package izarleydev.com.slider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove os botões do slide
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        /*


        addSlide(new SimpleSlide.Builder()
                .title("Titulo")
                .description("Descricao")
                .image(R.drawable.um)
                .background(R.color.black)
                .backgroundDark(R.color.white)
                .build()
        );
        addSlide(new SimpleSlide.Builder()
                .title("Titulo2")
                .description("Descricao2")
                .image(R.drawable.dois)
                .background(R.color.black)
                .backgroundDark(R.color.white)
                .build()
        );addSlide(new SimpleSlide.Builder()
                .title("Titulo3")
                .description("Descricao3")
                .image(R.drawable.tres)
                .background(R.color.black)
                .backgroundDark(R.color.white)
                .build()
        );
         */
    }
}