package izarleydev.com.whatsapp.Activitys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import izarleydev.com.whatsapp.Activitys.model.Mensagem;

public class MensageAdapter extends RecyclerView.Adapter<MensageAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;

    public MensageAdapter(List<Mensagem> list, Context c) {
        this.mensagens = list;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view){
            super(view);
        }
    }

}
