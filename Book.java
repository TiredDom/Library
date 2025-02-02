package lib;

class Book {
    private String title;
    private String author;
    private double price;
    private int stock;
    private boolean discontinued;

    public Book(String title, String author, double price, int stock) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.discontinued = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }
}