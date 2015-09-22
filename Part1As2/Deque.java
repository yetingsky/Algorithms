import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
        
        private Node(Item item) {
            this.item = item;
            next = null;
            prev = null;
        }
    }
    
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    private void checkAdd(Item item) {
        if (item == null)
            throw new NullPointerException("add null");
    }
    
    private void checkRemove(Node node) {
        if (node == null)
            throw new NoSuchElementException("remove from null");
    }
    
    public void addFirst(Item item) {
        checkAdd(item);
        Node node = new Node(item);
        if (size == 0) {
            head = node;
            tail = node;
        } else {
            head.prev = node;
            node.next = head;
            head = node;
        } 
        size++;
    }
    
    public void addLast(Item item) {
        checkAdd(item);
        Node node = new Node(item);
        if (size == 0) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }
    
    public Item removeFirst() {
        checkRemove(head);
        Node node = head;
        Item item = node.item;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        node = null;
        size--;
        return item;
    }
//    
    public Item removeLast() {
        checkRemove(tail);
        Node node = tail;
        Item item = node.item;
        if (size == 1) {
            head = null; 
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        node = null;
        size--;
        return item;
    }
    
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current;
        
        public DequeIterator() {
            current = head;
        }
        
        public boolean hasNext() {
            return current != null;
        }
        
        public Item next() {
            if (current == null)
                throw new NoSuchElementException("not item");
            Item item = current.item;
            current = current.next;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("not support remove()");
        }
    }
    
    private void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.item);
            System.out.print("-");
            current = current.next;
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        System.out.println(deque.size());
        
//         test for addFirst
//        deque.addFirst(3);
//        deque.addFirst(2);
//        deque.addFirst(1);
//        deque.printList();  // which should print 1-2-3-
//        System.out.println(deque.size()); 
//        deque.addFirst(null);
        
        // test for addLast
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.printList();
        System.out.println(deque.size());
        
        // test for removeFirst
//        deque.removeFirst();
//        deque.printList();  // which should print 2-3-
//        deque.removeFirst();  
//        deque.printList();  // which should print 3-
//        deque.removeFirst();
//        deque.printList();
//        deque.removeFirst();  // which should throw a error
        
        // test for removeLast
//        deque.removeLast();
//        deque.printList();  // which should print 1-2
//        deque.removeLast();  
//        deque.printList();  // which should print 1-
//        deque.removeLast(); 
//        deque.printList();
//        deque.removeLast();  // which should throw a error
    }

}
