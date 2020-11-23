package mobile.project.animalwallpaper.firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mobile.project.animalwallpaper.ImagePreview;
import mobile.project.animalwallpaper.R;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    List<FirebaseModel> list = new ArrayList<FirebaseModel>();
    Context context;

    public AdapterList(List<FirebaseModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_list, parent, false);
        AdapterList.ViewHolder myHoder = new AdapterList.ViewHolder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.ViewHolder holder, int position) {

        FirebaseModel mylist = list.get(position);

        Picasso.with(context).load(mylist.getImage())
                .fit()
                .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePreview.class);
                intent.putExtra("image",mylist.getImage());
                context.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemview) {
            super(itemview);
            imageView = (ImageView) itemview.findViewById(R.id.idImage);
        }


    }

    @Override
    public int getItemCount() {
        int arr = 0;

        try {
            if (list.size() == 0) {
                arr = 0;
            } else {

                arr = list.size();
            }
        } catch (Exception e) {
        }
        return arr;
    }

    private AdapterList.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(AdapterList.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
