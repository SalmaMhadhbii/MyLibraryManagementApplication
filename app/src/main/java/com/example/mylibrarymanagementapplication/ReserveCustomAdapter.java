package com.example.mylibrarymanagementapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Cet adaptateur gère la liste des livres pour la réservation
public class ReserveCustomAdapter extends RecyclerView.Adapter<ReserveCustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> book_id, book_title, book_author, book_pages;

    // Constructeur pour l'adaptateur
    public ReserveCustomAdapter(Activity activity, Context context, ArrayList<String> book_id,
                                ArrayList<String> book_title, ArrayList<String> book_author, ArrayList<String> book_pages) {
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
    }

    // Créer la vue pour chaque élément de la liste
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    // Remplir chaque ligne avec les données
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.book_id_txt.setText(book_id.get(position));
        holder.book_title_txt.setText(book_title.get(position));
        holder.book_author_txt.setText(book_author.get(position));
        holder.book_pages_txt.setText(book_pages.get(position));

        // Définir l'action lors du clic sur une ligne
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Créer un Intent pour démarrer l'activité ReserveActivity
                    Intent intent = new Intent(context, ReserveActivity.class);
                    intent.putExtra("id", book_id.get(adapterPosition));
                    intent.putExtra("title", book_title.get(adapterPosition));
                    intent.putExtra("author", book_author.get(adapterPosition));
                    intent.putExtra("pages", book_pages.get(adapterPosition));
                    activity.startActivity(intent);  // Lancer l'activité de réservation
                }
            }
        });
    }

    // Retourner le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return book_id.size();
    }

    // Classe interne pour contenir les vues de chaque ligne
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
