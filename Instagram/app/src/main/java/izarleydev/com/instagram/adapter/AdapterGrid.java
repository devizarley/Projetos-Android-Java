package izarleydev.com.instagram.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import izarleydev.com.instagram.R;
import izarleydev.com.instagram.helper.SquareImageView;

public class AdapterGrid extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> urlFotos;

    public AdapterGrid(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.urlFotos = objects;
    }

    public class ViewHolder {
        ImageView imagem;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //caso a View não esteja inflada
        ViewHolder viewHolder;

        if (convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
            viewHolder.progressBar = convertView.findViewById(R.id.progressGrid);
            viewHolder.imagem = convertView.findViewById(R.id.imageGridProfile);
            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        // Obtenha a URL da imagem do Firebase Realtime Database ou armazene-a em algum lugar
        String imageUrl = urlFotos.get(position);

        // Use o Glide para carregar e exibir a imagem
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.avatar))  // Imagem de placeholder enquanto a imagem é carregada
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Tratar falha no carregamento da imagem (se necessário)
                        viewHolder.progressBar.setVisibility(View.VISIBLE); // Ocultar a ProgressBar em caso de falha
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.progressBar.setVisibility(View.GONE); // Ocultar a ProgressBar quando a imagem for carregada com sucesso
                        return false;
                    }
                })
                .into(viewHolder.imagem);

        return convertView;
    }
}
