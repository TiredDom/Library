package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BookstoreInventory extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private LinkedList books;
    private static final String FILE_NAME = "books.json";
    private static final Gson gson = new Gson();

    private boolean isTitleAscending = true;
    private boolean isAuthorAscending = true;
    private boolean isPriceAscending = true;
    private boolean isStockAscending = true;
    
    

    // Constructor to initialize the bookstore inventory
    public BookstoreInventory() {
    	ImageIcon appIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/Group_2_Logo.ico"));
        setIconImage(appIcon.getImage());  // Set the application icon
        books = new LinkedList();
        loadBooksFromFile(); // Load saved books from file
        initComponents();
    }

    // Save the list of books to a JSON file
    private void saveBooksToFile() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(books.toList(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load books from the JSON file
    private void loadBooksFromFile() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            List<Book> loadedBooks = gson.fromJson(reader, new TypeToken<List<Book>>() {}.getType());
            if (loadedBooks != null) {
                books.clear();
                for (Book book : loadedBooks) {
                    books.add(book);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize UI components
    private void initComponents() {
        setTitle("Bookstore Inventory");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        getContentPane().add(panel);

        // Table setup
        String[] columnNames = {"Title", "Author", "Price", "Stock"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        addTableHeaderMouseListener();

        JPanel bottomPanel = new JPanel();
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add buttons for functionality
        JButton addButton = new JButton("Add Book");
        bottomPanel.add(addButton);
        addButton.addActionListener(e -> {
            addBook();
            saveBooksToFile();
        });

        JButton searchButton = new JButton("Search");
        bottomPanel.add(searchButton);
        searchButton.addActionListener(e -> searchBook());

        JButton updateStockButton = new JButton("Update Stock");
        bottomPanel.add(updateStockButton);
        updateStockButton.addActionListener(e -> {
            updateStock();
            saveBooksToFile();
        });

        JButton markDiscontinuedButton = new JButton("Mark as Discontinued");
        bottomPanel.add(markDiscontinuedButton);
        markDiscontinuedButton.addActionListener(e -> {
            markAsDiscontinued();
            saveBooksToFile();
        });
        
        JButton resetButton = new JButton("Reset Inventory");
        bottomPanel.add(resetButton);
        resetButton.addActionListener(e -> resetInventory());


        refreshTable();
    }

    // Method to add mouse listener to the table header for sorting
    private void addTableHeaderMouseListener() {
        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                String columnName = table.getColumnName(col);

                switch (columnName) {
                    case "Title":
                        sortBooks("title");
                        break;
                    case "Author":
                        sortBooks("author");
                        break;
                    case "Price":
                        sortBooks("price");
                        break;
                    case "Stock":
                        sortBooks("stock");
                        break;
                }
            }
        });
    }
    
    private void sortBooks(String criteria) {
        List<Book> bookList = books.toList().stream()
                .filter(book -> !book.isDiscontinued())
                .collect(Collectors.toList());

        switch (criteria) {
            case "title":
                bookList.sort(isTitleAscending ? Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER)
                        : Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
                isTitleAscending = !isTitleAscending;
                break;
            case "author":
                bookList.sort(isAuthorAscending ? Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER)
                        : Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER).reversed());
                isAuthorAscending = !isAuthorAscending;
                break;
            case "price":
                bookList.sort(isPriceAscending ? Comparator.comparingDouble(Book::getPrice)
                        : Comparator.comparingDouble(Book::getPrice).reversed());
                isPriceAscending = !isPriceAscending;
                break;
            case "stock":
                bookList.sort(isStockAscending ? Comparator.comparingInt(Book::getStock)
                        : Comparator.comparingInt(Book::getStock).reversed());
                isStockAscending = !isStockAscending;
                break;
        }

        // After sorting, update the table
        updateTable(bookList);  // Call this to refresh the table with the sorted list
        updateTableHeaderIcons();  // Optional: Update header icons to show ascending/descending arrows
    }

 // Method to update table header icons based on sorting order
    private void updateTableHeaderIcons() {
        JTableHeader header = table.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String columnName = columnModel.getColumn(i).getHeaderValue().toString().split(" ")[0];
            String icon = "";

            switch (columnName) {
                case "Title":
                    icon = isTitleAscending ? " \u25B2" : " \u25BC";
                    break;
                case "Author":
                    icon = isAuthorAscending ? " \u25B2" : " \u25BC";
                    break;
                case "Price":
                    icon = isPriceAscending ? " \u25B2" : " \u25BC";
                    break;
                case "Stock":
                    icon = isStockAscending ? " \u25B2" : " \u25BC";
                    break;
            }
            columnModel.getColumn(i).setHeaderValue(columnName + icon);
        }

        header.repaint(); // Repaint the header to show updated icons
    }
    // Method to add a new book to the inventory
    private void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        if (title == null || title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.");
            return;
        }

        String author = JOptionPane.showInputDialog(this, "Enter book author:");
        if (author == null || author.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Author cannot be empty.");
            return;
        }

        // Check if the book already exists
        if (books.toList().stream().anyMatch(b -> b.getTitle().equalsIgnoreCase(title) && b.getAuthor().equalsIgnoreCase(author))) {
            JOptionPane.showMessageDialog(this, "This book with the same title and author already exists or discontinued");
            return;
        }

        double price = -1;
        while (price < 0) {
            try {
                String priceInput = JOptionPane.showInputDialog(this, "Enter book price (numbers only):");
                if (priceInput == null) return;
                price = Double.parseDouble(priceInput);
                if (price < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a non-negative number for price.");
            }
        }

        int stock = -1;
        while (stock < 0) {
            try {
                String stockInput = JOptionPane.showInputDialog(this, "Enter stock quantity (non-negative integer only):");
                if (stockInput == null) return;
                stock = Integer.parseInt(stockInput);
                if (stock < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a non-negative integer for stock.");
            }
        }

        books.add(new Book(title, author, price, stock)); // Add the new book to the list
        refreshTable(); // Refresh the table to show the updated list
    }

    // Method to search for a book by title or author
    private void searchBook() {
        String query = JOptionPane.showInputDialog(this, "Enter title or author to search:");
        if (query == null || query.trim().isEmpty()) {
            refreshTable();
            return;
        }

        List<Book> filteredBooks = books.toList().stream()
                .filter(book -> (book.getTitle().equalsIgnoreCase(query) || book.getAuthor().equalsIgnoreCase(query)) && !book.isDiscontinued())
                .collect(Collectors.toList());

        if (filteredBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book not found.");
        } else {
            updateTable(filteredBooks); // Update the table to show the search results
        }
    }

    // Method to update the stock of a book
    private void updateStock() {
        String title = JOptionPane.showInputDialog(this, "Enter book title to update stock:");
        Book book = books.find(title);

        if (book != null && !book.isDiscontinued()) {
            try {
                int newStock = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new stock quantity (non-negative numbers only):"));
                if (newStock >= 0) {
                    book.setStock(newStock);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid input. Stock must be a non-negative number.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for stock.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Book not found!");
        }
    }

    // Method to mark a book as discontinued
    private void markAsDiscontinued() {
        String title = JOptionPane.showInputDialog(this, "Enter book title to mark as discontinued:");
        Book book = books.find(title);

        if (book != null) {
            book.setDiscontinued(true); // Mark the book as discontinued
            refreshTable(); // Refresh the table to show the updated status
        } else {
            JOptionPane.showMessageDialog(this, "Book not found!");
        }
    }

    // Method to refresh the table with active books
    private void refreshTable() {
        List<Book> activeBooks = books.toList().stream()
                .filter(book -> !book.isDiscontinued())
                .collect(Collectors.toList());
        updateTable(activeBooks); // Update the table with active books
    }

    // Method to update the table with a list of books
    private void updateTable(List<Book> books) {
        model.setRowCount(0); // Clear the table
        for (Book book : books) {
            model.addRow(new Object[]{book.getTitle(), book.getAuthor(), String.format("%.2f", book.getPrice()), book.getStock()});
        }
    }
    private void resetInventory() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the inventory?", 
                                                     "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Clear the books list
            books.clear();

            // Delete or reset the JSON file
            File file = new File(FILE_NAME);
            if (file.exists()) {
                file.delete();  // Delete the file or reset its content
            }

            // Save an empty list to the file
            saveBooksToFile();

            // Refresh the table
            refreshTable();
        }
    }


    public static void main(String[] args) {
        // Show the loading screen first
        LoadingScreen loadingScreen = new LoadingScreen();  // Show the loading screen

        // Create a thread to simulate loading and ensure the loading screen stays visible
        new Thread(() -> {
            try {
                // Simulate a loading delay (adjust as necessary)
                Thread.sleep(3000);  // Show the loading screen for 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After loading, dispose of the loading screen and open the main window
            loadingScreen.dispose();  // Close the loading screen
            SwingUtilities.invokeLater(() -> new BookstoreInventory().setVisible(true));  // Open the main window
        }).start();
    }


}