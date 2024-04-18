import java.util.Iterator;
import java.util.ListIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import javax.activity.InvalidActivityException;

public class SimpleCDLL<T> implements SimpleList<T> {

  // +--------+--------------------------------------------------------
  // | Fields |
  // +--------+

  Node2<T> dummy;
  int numChanges;
  int size;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  public SimpleCDLL() {
    this.dummy = new Node2<T>(this.dummy, null, this.dummy);
    this.numChanges = 0;
    this.size = 0;
  }

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  }

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      int pos = 0;
      int numChanges = SimpleCDLL.this.numChanges;
      Node2<T> prev = SimpleCDLL.this.dummy;
      Node2<T> next = SimpleCDLL.this.dummy.next;

      Node2<T> update = null;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      private void failFast() {
        if(this.numChanges != SimpleCDLL.this.numChanges) {
          throw new ConcurrentModificationException();
        }
        return;
      }

      public void add(T val) throws UnsupportedOperationException {
        failFast();
        if(this.numChanges != SimpleCDLL.this.numChanges) {
          throw new UnsupportedOperationException();
        }
        ++this.numChanges;
        ++SimpleCDLL.this.numChanges;
        this.prev = this.prev.insertAfter(val);
        this.update = null;
        ++SimpleCDLL.this.size;
        ++this.pos;
      }
      
      public boolean hasNext() {
        failFast();
        return (this.pos < SimpleCDLL.this.size);
      } // hasNext()

      public boolean hasPrevious() {
        failFast();
        return (this.pos > 0);
      } // hasPrevious()

      public T next() {
        failFast();
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        this.update = this.next;
        this.prev = this.next;
        this.next = this.next.next;
        ++this.pos;
        return this.update.value;
      } // next 

      public int nextIndex() {
        failFast();
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        failFast();
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        failFast();
        if (!this.hasPrevious())
          throw new NoSuchElementException();
        this.update = this.prev;
        this.next = this.prev;
        this.prev = this.prev.prev;
        --this.pos;
        return this.update.value;
      } // previous()

      public void remove() {
        failFast();
        if(this.update == null) {
          throw new IllegalStateException();
        }

        if(this.next == this.update) {
          this.next = this.update.next;
        }

        if(this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        }

        ++this.numChanges;
        ++SimpleCDLL.this.numChanges;
        this.update.remove();
        --SimpleCDLL.this.size;
        this.update = null;
      }

      public void set(T val) {
        failFast();
        if(this.update == null) {
          throw new IllegalStateException();
        }
        this.update.value = val;
        this.update = null;
      }
    };
  }

}