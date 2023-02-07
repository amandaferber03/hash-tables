
package hw7.hashing;

import hw7.Map;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {

  int[] primes = {2, 5, 11, 23, 47, 97, 197, 397, 797,
                  1597, 3203, 6421, 12853, 25717, 51437,102877,
                  205759, 411527, 823117, 1646237,3292489, 6584983, 13169977};
  double alpha = 0.8;
  int capacity;
  int numElements;
  int capacityCount;
  private List<Entry<K, V>>[] arr;

  /** Constructor for ChainingHashMap.
   *
   */
  public ChainingHashMap() {
    numElements = 0;
    capacityCount = 0;
    capacity = primes[capacityCount];
    arr = (List<Entry<K,V>>[]) Array.newInstance(List.class, capacity);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);//employ hashing function to get index for key
    Entry<K, V> element = new Entry<>(k, v);
    //if there is no ArrayList at the index, create one and insert it at that index
    if (arr[index] == null) {
      List<Entry<K, V>> entry = new ArrayList<>();
      arr[index] = entry;
    } else if (listContainsKey(arr[index], k)) {
      //if an existing ArrayList contains the key, throw exception
      throw new IllegalArgumentException();
    }
    arr[index].add(element);//add the key-value pair to the arraylist at the index
    numElements++;
    //after each insertion, check the load factor to prevent greater collisions
    checkLoadFactor();
  }

  private boolean listContainsKey(List<Entry<K, V>> list, K k) {
    for (Entry<K, V> entry : list) {
      //if a key-value pair in an ArrayList matches the key and is not a tombstone, the key
      //already exists
      if (entry.key.equals(k) && !entry.isDeleted) {
        return true;
      }
    }
    return false;
  }

  private int getIndex(K k) {
    return Math.abs(k.hashCode()) % capacity;//take the absolute value of the hashcode to prevent
    //a negative index
  }

  private void checkLoadFactor() {
    double lf = ((double) numElements) / ((double) capacity);
    if (lf > alpha) {
      rehash();
    }
  }

  private void rehash() {
    //generate the new capacity for the hash map from the list of prime numbers
    capacityCount++;
    capacity = primes[capacityCount];
    List<Entry<K, V>>[] temp = (List<Entry<K,V>>[]) Array.newInstance(List.class, capacity);
    for (List<Entry<K, V>> listEntry : arr) {
      if (listEntry != null) {
        // if an ArrayList already exists at the current position in the current hashmap,
        //iterate through the ArrayList to reinsert all mappings into the new hashmap
        for (Entry<K, V> kvEntry : listEntry) {
          //if the entry is not null and not been deleted, calculate its new index with the new
          //capacity and insert it in the corresponding ArrayList
          if (kvEntry != null && !kvEntry.isDeleted) {
            int index = getIndex(kvEntry.key);
            if (temp[index] == null) { //create new ArrayList if it doesn't already exist in the new hashmap
              List<Entry<K, V>> entry = new ArrayList<>();
              temp[index] = entry;
            }
            temp[index].add(kvEntry);
          }
        }
      }
    }
    arr = temp;
  }


  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    //if the ArrayList at the current index is null, there is nothing to
    //remove and an exception is thrown
    if (arr[index] == null) {
      throw new IllegalArgumentException();
    } else {
      for (Entry<K, V> entry : arr[index]) {
        //if the entry is not a tombstone and has the desired key,
        //remove the element and designate it as a tombstone
        if (entry.key.equals(k) && !entry.isDeleted) {
          entry.isDeleted = true;
          numElements--;
          return entry.value;
        }
      }
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    //if the ArrayList at the current index is null, there is nothing to
    //update and an exception is thrown
    if (arr[index] == null) {
      throw new IllegalArgumentException();
    } else {
      for (Entry<K, V> entry : arr[index]) {
        //if the entry is not a tombstone and has the desired key,
        //update the value
        if (entry.key.equals(k) && !entry.isDeleted) {
          entry.value = v;
          return;
        }
      }
      throw new IllegalArgumentException();
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    //if the ArrayList at the current index is null, there is nothing to
    //access and an exception is thrown
    if (arr[index] == null) {
      throw new IllegalArgumentException();
    } else {
      for (Entry<K, V> entry : arr[index]) {
        //if the entry is not a tombstone and has the desired key,
        //return the value
        if (entry.key.equals(k) && !entry.isDeleted) {
          return entry.value;
        }
      }
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    int index = getIndex(k);
    //if the ArrayList at the current index is null, there is nothing to
    //access and the key is not in the hashmap
    if (arr[index] == null) {
      return false;
    } else {
      for (Entry<K, V> entry : arr[index]) {
        //if the element at the current index is null, there is nothing to
        //access and the key is not in the hashmap
        if (entry == null) {
          return false;
        }
        //if the element is not a tombstone and has the desired key,
        //the key is in the hashmap
        if (entry.key.equals(k) && !entry.isDeleted) {
          return true;
        }
      }
      return false;
    }
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new ChainingHashMapIterator();
  }


  private class ChainingHashMapIterator implements Iterator<K> {
    private int hashCursor;
    private int auxCursor;
    private int count;

    ChainingHashMapIterator() {
      hashCursor = 0;
      auxCursor = 0;
      count = 0;
    }

    @Override
    public boolean hasNext() {
      return count < numElements;
    }

    public void checkHasNext() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
    }

    @Override
    public K next() {
      checkHasNext();
      K key = null;
      while (hashCursor < arr.length) { //while we have not reached the end of the hashmap

        //if we have reached the end of the ArrayList, increment the hashCursor to access the next ArrayList and
        //set the auxCursor back to zero to iterate through the new ArrayList
        if (arr[hashCursor] == null || auxCursor >= arr[hashCursor].size()) {
          auxCursor = 0;
          hashCursor++;
        } else { //skip the deleted entries in each ArrayList and return the key of the next
          //available entry in the ArrayList
          if (arr[hashCursor].get(auxCursor).isDeleted) {
            auxCursor++;
          } else {
            key = arr[hashCursor].get(auxCursor).key;
            auxCursor++;
            break;
          }
        }
      }
      count++;
      return key;
    }
  }

  private static class Entry<K, V> {
    K key;
    V value;
    boolean isDeleted;

    // Constructor to make node creation easier to read.
    Entry(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      isDeleted = false;
    }
  }

}
