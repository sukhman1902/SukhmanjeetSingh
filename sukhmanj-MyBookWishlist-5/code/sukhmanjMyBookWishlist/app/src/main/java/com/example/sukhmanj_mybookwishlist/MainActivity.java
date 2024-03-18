package com.example.sukhmanj_mybookwishlist;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sukhmanj_mybookwishlist.Book;
import com.example.sukhmanj_mybookwishlist.BookAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Book> bookList;// arraylist to store the list of books
    private BookAdapter bookAdapter;// adapter for managing the listview of books
    private TextView tvItemCount;// textview to display the count of books read
    private TextView tvItemCountAbove;// textview to display the total count of books

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize the arraylist and bookadapter
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);
        // initialize the listview and set its adapter
        ListView listView = findViewById(R.id.listView);
        // set item click listener for listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selection when an item is clicked
                if (position == bookAdapter.getSelectedPosition()) {
                    bookAdapter.clearSelectedPosition();
                } else {
                    bookAdapter.setSelectedPosition(position);
                }
            }
        });

        listView.setAdapter(bookAdapter);
        tvItemCount = findViewById(R.id.tvItemCount);
        //set touch listener for the root layout
        View rootLayout = findViewById(R.id.root_layer_id);
        tvItemCount.setText("Books Read: 0");
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //clear selection if touched outside the list view
                float x = event.getX();
                float y = event.getY();
                int position = listView.pointToPosition((int) x, (int) y);

                if (position != AdapterView.INVALID_POSITION) {
                    bookAdapter.clearSelectedPosition();
                } else {
                    listView.setItemChecked(bookAdapter.getSelectedPosition(), false);
                    bookAdapter.clearSelectedPosition();
                }

                return false;
            }
        });

        Button addButton = findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBookDialog();
            }
        });

        Button editButton = findViewById(R.id.btnEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = bookAdapter.getSelectedPosition();

                if (selectedPosition != -1) {

                    showEditBookDialog(selectedPosition);
                } else {
                    showToast("Select a book to edit");
                }
            }
        });

        Button deleteButton = findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedBook();
            }
        });

        tvItemCountAbove = findViewById(R.id.tvItemCountAbove);
    }
    // method to show a bialog for adding a new book
    private void showAddBookDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);

        final EditText titleEditText = dialogView.findViewById(R.id.editTextTitle);
        final EditText authorEditText = dialogView.findViewById(R.id.editTextAuthor);
        final EditText genreEditText = dialogView.findViewById(R.id.editTextGenre);
        final EditText yearEditText = dialogView.findViewById(R.id.editTextYear);
        final EditText statusEditText = dialogView.findViewById(R.id.editTextStatus);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add a New Book")
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String title = titleEditText.getText().toString();
                    String author = authorEditText.getText().toString();
                    String genre = genreEditText.getText().toString();
                    int year = 0;
                    try {
                        String yearString = yearEditText.getText().toString();
                        if (yearString.length() == 4) {
                            year = Integer.parseInt(yearString);
                        } else {
                            Toast.makeText(MainActivity.this, "Enter a valid 4-digit year", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException ignored) {
                        Toast.makeText(MainActivity.this, "Enter a valid integer for the year", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String status = statusEditText.getText().toString();
                    if (title.isEmpty() || title.length() > 50) {
                        showToast("Enter a valid title (up to 50 characters)");
                        return;
                    }

                    if (author.isEmpty() || author.length() > 30) {
                        showToast("Enter a valid author name (up to 30 characters)");
                        return;
                    }

                    if (genre.isEmpty()) {
                        showToast("Enter a valid genre");
                        return;
                    }

                    if (!status.equalsIgnoreCase("Read") && !status.equalsIgnoreCase("Unread")) {
                        showToast("Enter a valid status (Read/Unread)");
                        return;
                    }
                    status = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();

                    Book newBook = new Book(title, author, genre, year, status);
                    bookList.add(newBook);
                    bookAdapter.notifyDataSetChanged();

                    if (status.equalsIgnoreCase("Read")) {
                        incrementCountTextView();
                    }
                    updateTotalCount();
                })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }
    // method to show a dialog for editing an existing book
    private void showEditBookDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);
        final EditText titleEditText = dialogView.findViewById(R.id.editTextTitle);
        final EditText authorEditText = dialogView.findViewById(R.id.editTextAuthor);
        final EditText genreEditText = dialogView.findViewById(R.id.editTextGenre);
        final EditText yearEditText = dialogView.findViewById(R.id.editTextYear);
        final EditText statusEditText = dialogView.findViewById(R.id.editTextStatus);

        Book selectedBook = bookList.get(position);
        titleEditText.setText(selectedBook.getTitle());
        authorEditText.setText(selectedBook.getAuthor());
        genreEditText.setText(selectedBook.getGenre());
        yearEditText.setText(String.valueOf(selectedBook.getPublicationYear()));
        statusEditText.setText(selectedBook.getStatus());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Edit Book Details")
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newTitle = titleEditText.getText().toString();
                    String newAuthor = authorEditText.getText().toString();
                    String newGenre = genreEditText.getText().toString();
                    int newYear = 0;
                    try {
                        String yearString = yearEditText.getText().toString();
                        if (yearString.length() == 4) {
                            newYear = Integer.parseInt(yearString);
                        } else {
                            showToast("Enter a valid 4-digit year");
                            return;
                        }
                    } catch (NumberFormatException ignored) {
                        showToast("Enter a valid integer for the year");
                        return;
                    }
                    String newStatus = statusEditText.getText().toString();

                    if (newTitle.isEmpty() || newTitle.length() > 50) {
                        showToast("Enter a valid title (up to 50 characters)");
                        return;
                    }

                    if (newAuthor.isEmpty() || newAuthor.length() > 30) {
                        showToast("Enter a valid author name (up to 30 characters)");
                        return;
                    }

                    if (newGenre.isEmpty()) {
                        showToast("Enter a valid genre");
                        return;
                    }

                    if (!newStatus.equalsIgnoreCase("Read") && !newStatus.equalsIgnoreCase("Unread")) {
                        showToast("Enter a valid status (Read/Unread)");
                        return;
                    }
                    newStatus = newStatus.substring(0, 1).toUpperCase() + newStatus.substring(1).toLowerCase();
                    Book updatedBook = new Book(newTitle, newAuthor, newGenre, newYear, newStatus);
                    if (!selectedBook.getStatus().equalsIgnoreCase(updatedBook.getStatus())) {
                        if (updatedBook.getStatus().equalsIgnoreCase("Read")) {
                            incrementCountTextView();
                        } else {
                            decrementCountTextView();
                        }
                    }

                    bookAdapter.updateBook(position, updatedBook);

                    bookAdapter.updateBook(position, updatedBook);

                })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }
    // method to show a toast message
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // method to update the total count of book s
    private void updateTotalCount() {
        int totalCount = bookList.size();
        tvItemCountAbove.setText("Total Books: " + totalCount);
    }
    // methid to increment the count of books read
    private void incrementCountTextView() {
        int currentCount = Integer.parseInt(tvItemCount.getText().toString().replaceAll("[^0-9]", ""));
        int newCount = currentCount + 1;
        tvItemCount.setText("Books Read: " + newCount);
        updateTotalCount();
    }
    // method to decrement  the count of books read
    private void decrementCountTextView() {
        int currentCount = Integer.parseInt(tvItemCount.getText().toString().replaceAll("[^0-9]", ""));
        int newCount = Math.max(0, currentCount - 1);
        tvItemCount.setText("Books Read: " + newCount);
        updateTotalCount();
    }

    // method to delete a selected book

    private void deleteSelectedBook() {
        int selectedPosition = bookAdapter.getSelectedPosition();
        if (selectedPosition != -1) {
            Book selectedBook = bookList.get(selectedPosition);
            bookList.remove(selectedPosition);
            bookAdapter.clearSelectedPosition();
            bookAdapter.notifyDataSetChanged();
            updateTotalCount();

            if (selectedBook.getStatus().equalsIgnoreCase("Read")) {
                decrementCountTextView();
            }
        }
    }
}