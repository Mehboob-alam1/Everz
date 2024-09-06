package com.example.everz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder>{


    private Context context;
    private ArrayList<Blog> list;

    public Adapter(Context context, ArrayList<Blog> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<Blog> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_news,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Blog blog = list.get(position);

        holder.txtMainHeading.setText(blog.getTitle());
        holder.txtContent.setText(blog.getDescription());
        Glide.with(context)
                .load(blog.getImageUrl())
                .placeholder(R.drawable.simuico)
                .into(holder.imgNews);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class Holder extends RecyclerView.ViewHolder{

   private TextView txtMainHeading,txtContent;
   private ImageView imgNews;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imgNews=itemView.findViewById(R.id.imgNews);
            txtMainHeading=itemView.findViewById(R.id.txtMainHeading);
            txtContent=itemView.findViewById(R.id.txtContentItem);
        }
    }
}
