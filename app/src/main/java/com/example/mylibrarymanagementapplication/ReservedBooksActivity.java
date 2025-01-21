package com.example.mylibrarymanagementapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReservedBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservedBooksAdapter reservedBooksAdapter;
    private ArrayList<String> book_id, book_title, book_author, book_pages, book_reserved_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_books);  // Assurez-vous que ce fichier XML contient un RecyclerView avec ID recyclerView

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser les listes
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_reserved_date = new ArrayList<>();

        // Remplir les listes avec des données fictives
        book_id.add("1");
        book_title.add("Title of Book 1");
        book_author.add("Author 1");
        book_pages.add("250");
        book_reserved_date.add("12/01/2025");

        book_id.add("2");
        book_title.add("Title of Book 2");
        book_author.add("Author 2");
        book_pages.add("300");
        book_reserved_date.add("15/01/2025");

        // Créer un nouvel adaptateur et le lier au RecyclerView
        reservedBooksAdapter = new ReservedBooksAdapter(
                ReservedBooksActivity.this,
                book_id,
                book_title,
                book_author,
                book_pages,
                book_reserved_date
        );
        recyclerView.setAdapter(reservedBooksAdapter);
    }
}
