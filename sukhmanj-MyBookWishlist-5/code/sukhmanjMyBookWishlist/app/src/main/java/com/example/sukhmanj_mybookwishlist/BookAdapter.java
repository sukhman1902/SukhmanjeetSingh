package com.example.sukhmanj_mybookwishlist;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.sukhmanj_mybookwishlist.Book;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    private Context context;// Context of the application
    private List<Book> books;// Lost of book objects
    private int selectedPosition = -1;// position of selected item
    private EditClickListener editClickListener;// listener for edit button clicks

    //interface for handling edit button clicks in the adapter
    public interface EditClickListener {
        void onEditClick(int position);
    }
    //sets the listener for edit button clicks
    public void setEditClickListener(EditClickListener listener) {
        this.editClickListener = listener;
    }
    //constructor for the book adapter
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }
    // updates the book at specified position in the list
    public void updateBook(int position, Book updatedBook) {
        if (position >= 0 && position < books.size()) {
            books.set(position, updatedBook);
            notifyDataSetChanged();
        }
    }
    // gets the view for each item in the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false);
        }
        // gets the current book object at the specifies position
        Book currentBook = getItem(position);
        // finding textviews in the layout
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView authorTextView = convertView.findViewById(R.id.authorTextView);
        TextView genreTextView = convertView.findViewById(R.id.genreTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        TextView yearTextView = convertView.findViewById(R.id.yearTextView);
        // populate textviews with information from the current book
        if (currentBook != null) {
            String formattedTitle = "Title: " + currentBook.getTitle();
            titleTextView.setText(formattedTitle);

            authorTextView.setText("Author: " + currentBook.getAuthor());
            genreTextView.setText("Genre: " + currentBook.getGenre());
            yearTextView.setText("Year: " + currentBook.getPublicationYear());
            statusTextView.setText("Status: " + currentBook.getStatus());

        }
        // highlight the selected item with a different background color
        if (position == selectedPosition) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selectedColor));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }
    // sets the position of the selected item and notifies the adapter
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
    // clears the selected position and notifies the adapter
    public void clearSelectedPosition() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }
    // gets the position of the selected item
    public int getSelectedPosition() {
        return selectedPosition;
    }
}