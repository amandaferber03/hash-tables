package hw7.hashing;

import hw7.Map;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {

  int[] primes = {2, 5, 11, 23, 47, 97, 197, 397, 797,
                  1597, 3203, 6421, 12853, 25717, 51437,102877,
                  205759, 411527, 823117, 1646237,3292489, 6584983, 13169977};
  double alpha = 0.5;
  int numElements;
  int numTombstones;
  int capacity;
  int capacityCount;
  private Entry<K,V>[] arr;

  /** Constructor for OpenAddressingHashMap.
   *
   */
  public OpenAddressingHashMap() {
    numElements = 0;
    numTombstones = 0;
    capacityCount = 0;
    capacity = primes[capacityCount];
    arr = (Entry<K,V>[]) Array.newInstance(Entry.class, capacity);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);//employ hashing function to get index for key
    for (int i = 1; i < capacity + 1; i++) { //for all elements of the hashmap array
      //check if the desired index has a value already or if the new key can
      //be inserted in that index
      if (arr[index] == null) {
        arr[index] = new Entry<>(k, v);
        numElements++;
        break;
      } else if ((arr[index].key.equals(k)) && !arr[index].isDeleted) {
        throw new IllegalArgumentException();
      }
      //if the key cannot be inserted at the current index, employ linear probing
      index = (getIndex(k) + i) % capacity;
    }
    //after each insertion, check the load factor to prevent greater collisions
    checkLoadFactor();
  }

  private int getIndex(K k) {
    return Math.abs(k.hashCode()) % capacity; //take the absolute value of the hashcode to prevent
    //a negative index
  }

  private void checkLoadFactor() {
    double lf = ((double) numElements + numTombstones) / ((double) capacity);
    if (lf > alpha) {
      rehash();
    }
  }

  private void rehash() {
    //generate the new capacity for the hash map from the list of prime number
    capacityCount++;
    capacity = primes[capacityCount];
    int i = 1;
    int index;
    //create a new array with the new capacity
    Entry<K, V>[] temp = (Entry<K,V>[]) Array.newInstance(Entry.class, capacity);
    for (Entry<K, V> kvEntry : arr) {
      //if the element of the hashmap has a mapping that is not a tombstone,
      //add it to the next available index in the new hashmap array
      if (kvEntry != null && !kvEntry.isDeleted) {
        index = getIndex(kvEntry.key);
        while (temp[index] != null && i < capacity + 1) {
          index = (getIndex(kvEntry.key) + i) % capacity;
          i++;
        }
        temp[index] = kvEntry;
      }
      i = 0;
    }
    //no tombstones are in the newly hashed array
    numTombstones = 0;
    arr = temp;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    for (int i = 1; i < capacity + 1; i++) {
      //if the element at the current index is null, there is nothing to
      //remove and an exception is thrown
      if (arr[index] == null) {
        break;
      }
      //if the element is not a tombstone and has the desired key,
      //remove the element and designate it as a tombstone
      if ((arr[index].key.equals(k)) && !arr[index].isDeleted) {
        arr[index].isDeleted = true;
        numElements--;
        numTombstones++;
        return arr[index].value;
      }
      //employ linear probing if the current element of the hashmap is
      //not the key to remove
      index = (getIndex(k) + i) % capacity;
    }
    throw new IllegalArgumentException();
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    for (int i = 1; i < capacity + 1; i++) {
      //if the element at the current index is null, there is nothing to
      //update and an exception is thrown
      if (arr[index] == null) {
        break;
      }
      //if the element is not a tombstone and has the desired key,
      //update the value
      if ((arr[index].key.equals(k)) && !arr[index].isDeleted) {
        arr[index].value = v;
        return;
      }
      //employ linear probing if the current element of the hashmap is
      //not the mapping that should be updated
      index = (getIndex(k) + i) % capacity;
    }
    throw new IllegalArgumentException();
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    for (int i = 1; i < capacity + 1; i++) {
      //if the element at the current index is null, there is nothing to
      //access and an exception is thrown
      if (arr[index] == null) {
        break;
      }
      //if the element is not a tombstone and has the desired key,
      //return the value
      if ((arr[index].key.equals(k)) && !arr[index].isDeleted) {
        return arr[index].value;
      }
      //employ linear probing if the current element of the hashmap is
      //not the mapping that should be accessed
      index = (getIndex(k) + i) % capacity;
    }
    throw new IllegalArgumentException();
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    int index = getIndex(k);
    for (int i = 1; i < capacity + 1; i++) {
      //if the element at the current index is null, there is nothing to
      //access and the key is not in the hashmap
      if (arr[index] == null) {
        return false;
      }
      //if the element is not a tombstone and has the desired key,
      //the key is in the hashmap
      if (arr[index].key.equals(k) && !arr[index].isDeleted) {
        return true;
      }
      //employ linear probing if the current element of the hashmap is
      //not the desired map
      index = (getIndex(k) + i) % capacity;
    }
    //the appropriate key was never located in the hashmap
    return false;
  }


  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new OpenAddressingHashMapIterator();
  }

  private class OpenAddressingHashMapIterator implements Iterator<K> {
    private int cursor;
    private int count;

    OpenAddressingHashMapIterator() {
      cursor = 0;
      count = 0;
    }

    @Override
    public boolean hasNext() {
      return cursor < capacity && count < numElements;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      for (int i = cursor; i < capacity; i++) {
        //if the cursor points to a null element or tombstone, the cursor should be incremented
        if (arr[cursor] == null || arr[cursor].isDeleted) {
          cursor++;
        } else { //if a valid mapping in the hashmap has been located, return its key and update the count
          count++;
          break;
        }
      }
      K key = arr[cursor].key;
      cursor++;
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