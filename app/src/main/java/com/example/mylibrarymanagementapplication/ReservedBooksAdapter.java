package com.example.mylibrarymanagementapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReservedBooksAdapter extends RecyclerView.Adapter<ReservedBooksAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> book_id, book_title, book_author, book_pages, book_reserved_date;

    // Constructeur mis à jour pour accepter les six listes
    public ReservedBooksAdapter(Context context, ArrayList<String> book_id, ArrayList<String> book_title, ArrayList<String> book_author, ArrayList<String> book_pages, ArrayList<String> book_reserved_date) {
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
        this.book_reserved_date = book_reserved_date;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflater le layout pour chaque élément de la liste
        View view = LayoutInflater.from(context).inflate(R.layout.my_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Assigner les valeurs aux TextView correspondants
        holder.bookTitleTextView.setText(book_title.get(position));
        holder.bookAuthorTextView.setText(book_author.get(position));
        holder.bookPagesTextView.setText(book_pages.get(position) + " pages");
        holder.bookReservedDateTextView.setText(book_reserved_date.get(position));  // Affichage de la date de réservation
    }

    @Override
    public int getItemCount() {
        return book_title.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitleTextView, bookAuthorTextView, bookPagesTextView, bookReservedDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialiser les TextView de chaque ligne
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            bookPagesTextView = itemView.findViewById(R.id.book_pages);
            bookReservedDateTextView = itemView.findViewById(R.id.book_reserved_date); // Ajouter la TextView pour la date de réservation
        }
    }
}
