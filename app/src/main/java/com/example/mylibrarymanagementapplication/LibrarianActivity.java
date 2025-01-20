package com.example.mylibrarymanagementapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LibrarianActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;


    MyDatabaseHelper myDB;

    // Déclaration des listes pour stocker les informations des livres
    ArrayList<String> book_id, book_title, book_author, book_pages;
    CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        // Navigate to AddActivity when add_button is clicked
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibrarianActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //A MyDatabaseHelper instance (myDB) is created to interact with the database.
        myDB = new MyDatabaseHelper(LibrarianActivity.this);
        //Four ArrayList objects are initialized to hold data for each book's ID, title, author, and number of pages.
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();

        // Appel de la méthode qui récupère les données de la base de données et les stocke dans les listes
        storeDataInArrays();

        // Création et configuration de l'adaptateur personnalisé pour la RecyclerView
        customAdapter = new CustomAdapter(LibrarianActivity.this, this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(customAdapter);
        // Configuration du gestionnaire de mise en page en mode liste verticale
        recyclerView.setLayoutManager(new LinearLayoutManager(LibrarianActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    //storeDataInArrays() is responsible for fetching data from a database and storing it in array lists so that the data can later be displayed in a RecyclerView (or similar component).
    //cursor=set of rows from a database query result.
    //A query like SELECT * FROM books would return a Cursor that points to the first row.
    //When you call cursor.moveToNext(), it moves the cursor to the second row, and so on.
    //You can then access the data in the current row, like cursor.getString(1) to get the book_title.
    void storeDataInArrays(){
        // Récupération des données via la méthode readAllData() de MyDatabaseHelper qui renvoie un Cursor
        Cursor cursor = myDB.readAllData();

        // Si aucune donnée n'est trouvée, afficher un message Toast
        if(cursor.getCount() == 0){
            Toast.makeText(this,"No data", Toast.LENGTH_LONG).show();
        }else{
            // Boucle pour parcourir les résultats de la base de données
            while (cursor.moveToNext()){
                // Ajout des informations récupérées à partir du curseur dans les différentes listes
                book_id.add(cursor.getString(0)); // ID du livre
                book_title.add(cursor.getString(1)); // Titre du livre
                book_author.add(cursor.getString(2)); // Auteur du livre
                book_pages.add(cursor.getString(3)); // Nombre de pages du livre
            }
        }
    }

    //Lorsque l'activité est reprise, cette méthode vide les listes de données actuelles,
    // recharge les données de la base de données
    // et notifie l'adaptateur que les données ont changé pour rafraîchir l'affichage.
    @Override
    protected void onResume() {
        super.onResume();
        // Effacer les données actuelles des listes pour éviter les doublons
        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        // Recharger les données de la base de données
        storeDataInArrays();
        // Notifier l'adaptateur que les données ont été mises à jour
        customAdapter.notifyDataSetChanged();
    }

}