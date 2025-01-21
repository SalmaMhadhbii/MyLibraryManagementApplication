package com.example.mylibrarymanagementapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    ReserveCustomAdapter reserveCustomAdapter; // Utiliser ReserveCustomAdapter ici

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.recyclerView);

        // Initialiser le helper de la base de données et les listes
        myDB = new MyDatabaseHelper(UserActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();

        // Récupérer les données de la base de données et remplir le RecyclerView
        storeDataInArrays();

        // Utiliser ReserveCustomAdapter ici
        reserveCustomAdapter = new ReserveCustomAdapter(UserActivity.this, this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(reserveCustomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));

        // Définir l'onClickListener pour l'icône du panier pour ouvrir ReservedBooksActivity
        ImageView cartIcon = findViewById(R.id.cart_icon);
        cartIcon.setOnClickListener(view -> {
            Intent intent = new Intent(UserActivity.this, ReservedBooksActivity.class);
            startActivity(intent);
        });
    }

    // Méthode pour récupérer les données de la base et les stocker dans les tableaux
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }
    }

    // Recharger les données à chaque fois que l'activité reprend
    @Override
    protected void onResume() {
        super.onResume();
        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        storeDataInArrays();
        reserveCustomAdapter.notifyDataSetChanged(); // Mise à jour de l'adaptateur
    }
}
