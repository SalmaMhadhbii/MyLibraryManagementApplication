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

//But principal :
//Adapter les données (livres) pour les afficher dans un RecyclerView.
//Créer et remplir les lignes (chaque ligne représentant un livre) à partir des données fournies.
//Gérer les interactions (par exemple, un clic sur une ligne pour ouvrir une autre activité).
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> book_id, book_title, book_author, book_pages;



    // Constructor
    public CustomAdapter(Activity activity, Context context, ArrayList<String> book_id,
                         ArrayList<String> book_title, ArrayList<String> book_author, ArrayList<String> book_pages) {
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
    }

    //1- creation de ligne
    //preparing the rows in your list. It decides how each row in the list will look (based on your layout file, my_row.xml).
    //The onCreateViewHolder method runs only when a new row layout needs to be created.
    //MyViewHolder: classe interne statique dans l'adaptateur CustomAdapter : container for the row layout’s views (e.g., TextView widgets for book details) so you can efficiently set data in those views without repeatedly finding them.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // LayoutInflater : une classe qui convertit (ou "inflates") un fichier de mise en page XML en un objet View réel en mémoire.
        // Le contexte ici est généralement l'activité ou l'application qui sait comment accéder aux ressources, comme les mises en page.

        View view = inflater.inflate(R.layout.my_row, parent, false);
        // convertit le fichier de mise en page XML my_row.xml, qui définit l'apparence de chaque ligne dans votre RecyclerView.
        //parent :ViewGroup parent dans lequel cette vue sera attachée. Il est nécessaire, même si dans ce cas, nous ne l'utilisons pas pour attacher la vue immédiatement, car cela sera géré par le RecyclerView lui-même.
        // Le "false" signifie "ne pas encore attacher cette vue au RecyclerView". Cela est dû au fait que le RecyclerView gérera l'attachement plus tard.

        return new MyViewHolder(view);
    }


    // 2- remplir la ligne
    // Une fois qu'une ligne est prête, cette étape la remplit avec les bonnes données. Par exemple, elle place le titre du livre, l'auteur et d'autres détails aux bons endroits pour chaque ligne.
    // Par exemple, si le RecyclerView affiche 10 lignes, cette méthode sera appelée 10 fois.
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        // Définir le texte du TextView book_id_txt dans la ligne actuelle avec la valeur correspondante du liste book_id.
        // book_id.get(position) récupère l'élément à la position donnée dans la liste book_id.
        holder.book_id_txt.setText(book_id.get(position));
        holder.book_title_txt.setText(book_title.get(position));
        holder.book_author_txt.setText(book_author.get(position));
        holder.book_pages_txt.setText(book_pages.get(position));

        // OnClickListener pour le RecyclerView
        // Utilisez getAdapterPosition() dans le OnClickListener pour obtenir la position correcte
        // Le mainLayout (qui est un LinearLayout enveloppant toute la ligne) est configuré pour déclencher un Intent vers UpdateActivity lorsqu'il est cliqué.
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Utilisez getAdapterPosition() au lieu de position directement. Cela est crucial pour éviter les problèmes pouvant survenir à cause du recyclage des vues dans le RecyclerView.
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) { // Vérifier si la position est valide
                    // Créer un Intent pour démarrer l'activité UpdateActivity
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", book_id.get(adapterPosition));
                    intent.putExtra("title", book_title.get(adapterPosition));
                    intent.putExtra("author", book_author.get(adapterPosition));
                    intent.putExtra("pages", book_pages.get(adapterPosition));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }

    //This tells the RecyclerView how many items you have in total (e.g., the number of books).
    @Override
    public int getItemCount() {
        return book_id.size(); // Return the size of the dataset
    }

    //The MyViewHolder class is a custom ViewHolder that provides references to the views for each data item in a row of the RecyclerView.
    //onCreateViewHolder:
    //
    //Creates a new instance of MyViewHolder for each row layout.
    //Passes the row layout view (itemView) to the MyViewHolder constructor.
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;

        //Contient les références aux widgets de chaque ligne (TextView pour l'ID, le titre, l'auteur, etc.).
        //Permet d’associer facilement les données aux widgets sans avoir à les rechercher plusieurs fois.

                LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);

            //mainLayout is used to refer to the entire row layout of each item in the RecyclerVie
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
