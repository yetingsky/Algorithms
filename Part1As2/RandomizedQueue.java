import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int size;
    
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    private void resize(int capacity) {
        assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("enqueue null");
        if (size == a.length) 
            resize(a.length*2);
        a[size++] = item;
    }
    
    private void check() {
        if (size == 0)
            throw new NoSuchElementException("queue is empty");
    }
    
    public Item dequeue() {
        check();
        int rand = StdRandom.uniform(size);
        Item temp = a[rand];
        a[rand] = a[--size];
        a[size] = null;
        if (size > 0 && size == a.length/4)
            resize(a.length/2);
        return temp;
    }
    
    public Item sample() {
        check();
        int rand = StdRandom.uniform(size);
        return a[rand];
    }

    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        private int current;
        private int[] indexArray;
        
        public RandQueueIterator() {
            current = 0;
            indexArray = new int[size];
            for (int i = 0; i < size; i++) 
                indexArray[i] = i;
            StdRandom.shuffle(indexArray);
        }
        
        public boolean hasNext() {
            return current < size;
        }
        
        public Item next() {
            if (current >= size)
                throw new NoSuchElementException("queue is empty");
            return a[indexArray[current++]];
        }
        
        public void remove() {
            throw new UnsupportedOperationException("not support remove()");
        }
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        

    }

}
