package izarleydev.com.instagram.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

        //recuperar dados da imagem
        String urlImagem = getItem(position);

        Glide teste = Glide.get(getContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(urlImagem, viewHolder.imagem,
            new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(view.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(view.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.progressBar.setVisibility(view.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(view.GONE);
            }
        });


        return convertView;
    }
}
