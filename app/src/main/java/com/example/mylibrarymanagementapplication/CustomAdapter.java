package com.example.mylibrarymanagementapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//making a translator between your data (books) and the RecyclerView (a scrollable list).
//Purpose of CustomAdapter: It's a helper that takes your data (like a list of books) and makes sure it shows up correctly on the screen inside the RecyclerView.
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

    //preparing the rows in your list. It decides how each row in the list will look (based on your layout file, my_row.xml).
    //The onCreateViewHolder method runs only when a new row layout needs to be created.
    //MyViewHolder: container for the row layout’s views (e.g., TextView widgets for book details) so you can efficiently set data in those views without repeatedly finding them.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater: a class that converts (or "inflates") an XML layout file into an actual View object in memory.
        //context here is usually the activity or application that knows how to access resources, such as layouts.
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate the XML layout file my_row.xml, which defines what each row in your RecyclerView looks like.
        //The false means "don’t attach this view to the RecyclerView just yet." This is because the RecyclerView will handle attaching it later.
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    //Once a row is ready, this step fills it with the right data. For example, it puts the book title, author, and other details in the right places for each row.
    //For example, if the RecyclerView is displaying 10 rows, this method will be called 10 times.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the text of the book_id_txt TextView in the current row to the corresponding value from the book_id dataset.
        //The book_id.get(position) retrieves the position-th item from the book_id list.
        holder.book_id_txt.setText(book_id.get(position));
        holder.book_title_txt.setText(book_title.get(position));
        holder.book_author_txt.setText(book_author.get(position));
        holder.book_pages_txt.setText(book_pages.get(position));
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
        }
    }
}
