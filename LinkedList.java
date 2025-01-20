package lib;
import java.util.List;
import java.util.ArrayList;
class LinkedList {
    private Node head;
    private int size;

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(Book book) {
        Node newNode = new Node(book);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void remove(String title) {
        if (head == null) return;

        if (head.data.getTitle().equalsIgnoreCase(title)) {
            head = head.next;
            size--;
            return;
        }

        Node current = head;
        while (current.next != null && !current.next.data.getTitle().equalsIgnoreCase(title)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
        }
    }

    public Book find(String title) {
        Node current = head;
        while (current != null) {
            if (current.data.getTitle().equalsIgnoreCase(title)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public List<Book> toList() {
        List<Book> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public int size() {
        return size;
    }
}
