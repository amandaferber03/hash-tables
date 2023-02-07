package hw7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("All")
public abstract class MapTest {

  protected Map<String, String> map;

  @BeforeEach
  public void setup() {
    map = createMap();
  }

  protected abstract Map<String, String> createMap();

  @Test
  public void newMapIsEmpty() {
    assertEquals(0, map.size());
  }

  @Test
  public void testHas() {
   assertFalse(map.has("key"));
  }

  @Test
  public void insertOneElement() {
    map.insert("key1", "value1");
    assertEquals(1, map.size());
    assertTrue(map.has("key1"));
    assertEquals("value1", map.get("key1"));
  }

  @Test
  public void insertMultipleElement() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    assertEquals(3, map.size());
    assertTrue(map.has("key1"));
    assertTrue(map.has("key2"));
    assertTrue(map.has("key3"));
    assertEquals("value1", map.get("key1"));
    assertEquals("value2", map.get("key2"));
    assertEquals("value3", map.get("key3"));
  }
  @Test
  public void testInsert2() {
    map.insert("Hello", "uno");
    map.insert("Hola", "dos");
    assertEquals(2, map.size());
    assertTrue(map.has("Hello"));
    assertTrue(map.has("Hola"));
    assertFalse(map.has("Ding"));
    assertFalse(map.has("Dong"));
    assertEquals(2, map.size());
    int counter = 0;
    for (int i = 0; i < 100; i++) {
      map.insert("key" + i, "val");
    }
    map.remove("key" + 65);
    assertFalse(map.has("key" + 65));
    int i = 0;
    for (String thing: map) {
      counter++;
      i++;
    }
    assertEquals(101, counter);
  }

@Test
  public void testOpen() {
    for (int i = 0; i < 1000; i++) {
      map.insert("key" + i, "val");
      System.out.println("key" + i);
    }
  }

  //iterator, has, get

  @Test
  public void testInsert() {
    for (int i = 0; i < 25718; i++) {
      map.insert("key" + i, "val");
      assertTrue(map.has("key" + i));
    }
    assertEquals(25718, map.size());
    for (int i = 0; i < 25718; i++) {
      assertTrue(map.has("key" + i));
    }
    assertFalse(map.has("key" + 25718));
    for (int i = 400; i < 410; i++) {
      System.out.print(i);
      System.out.println(map.has("key" + i));
      map.remove("key" + i);
    }
    for (int i = 400; i < 410; i++) {
      assertFalse(map.has("key" + i));
      map.insert("key" + i, "val");
    }
    for (int i = 0; i < 25718; i++) {
     assertTrue(map.has("key" + i));
    }
  }

  @Test
  public void testHasNext() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c");
    Iterator itr = map.iterator();
    assertTrue(itr.hasNext());
    for(int i = 0; i < 3; i++) {
      itr.next();
    }
    assertFalse(itr.hasNext());
  }

  @Test
  public void testForEachLoop() {
    for (int i = 0; i < 98; i++) {
      map.insert("key" + i, "val");
      assertTrue(map.has("key" + i));
    }
    int counter = 0;
    for (String element: map) {
      System.out.println(element);
      counter++;
    }
    assertEquals(98, counter);
  }

  @Test
  public void testNext() {
    for (int i = 0; i < 98; i++) {
      map.insert("key" + i, "val");
      assertTrue(map.has("key" + i));
    }
    Iterator itr = map.iterator();
    for(int i = 0; i < map.size(); i++) {
      System.out.println(itr.next());
    }
  }

  @Test
  public void insertDuplicatedKey() {
    try {
      map.insert("key1", "value1");
      map.insert("key1", "value2");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void insertNullKey() {
    try {
      map.insert(null, "value1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void insertDuplicatedValue() {
    map.insert("key1", "value1");
    map.insert("key2", "value1");
    assertEquals(2, map.size());
  }

  @Test
  public void insertNullValue() {
    map.insert("null", null);
    assertEquals(1, map.size());
  }

  @Test
  public void removeOneElement() {
    map.insert("key1", "value1");
    assertEquals("value1", map.remove("key1"));
    assertEquals(0, map.size());
  }

  @Test
  public void removeMultipleElements() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    assertEquals("value1", map.remove("key1"));
    assertEquals("value3", map.remove("key3"));
    assertEquals(1, map.size());
    assertFalse(map.has("key1"));
    assertTrue(map.has("key2"));
    assertFalse(map.has("key3"));
    assertEquals("value2", map.get("key2"));
  }

  @Test
  public void removeNull() {
    try {
      map.remove(null);
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void removeNoSuchElement() {
    try {
      map.remove("key1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void updateValue() {
    map.insert("key1", "value1");
    map.put("key1", "value2");
    assertEquals(1, map.size());
    assertEquals("value2", map.get("key1"));
  }

  @Test
  public void updateMultipleValues() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    map.put("key1", "updated1");
    map.put("key3", "updated3");
    assertEquals(3, map.size());
    assertEquals("updated1", map.get("key1"));
    assertEquals("value2", map.get("key2"));
    assertEquals("updated3", map.get("key3"));
  }

  @Test
  public void updateMultipleTimes() {
    map.insert("key1", "value1");
    map.put("key1", "value2");
    map.put("key1", "value3");
    map.put("key1", "value4");
    assertEquals(1, map.size());
    assertEquals("value4", map.get("key1"));
  }

  @Test
  public void updateNullValue() {
    map.insert("key1", "value1");
    map.put("key1", null);
    assertEquals(1, map.size());
    assertNull(map.get("key1"));
  }

  @Test
  public void updateNullKey() {
    try {
      map.put(null, "value");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void updateKeyNotMapped() {
    try {
      map.put("key", "value");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void getKeyNull() {
    try {
      map.get(null);
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void iteratorEmptyMap() {
    for (String key : map) {
      fail("Empty map!");
    }
  }

  @Test
  public void iteratorMultipleElements() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    int counter = 0;
    for (String key : map) {
      counter++;
      assertTrue(map.has(key));
    }
    assertEquals(3, counter);
  }

  // Ideally we should also check for "Keys must be immutable"
  // This is not trivial; check out
  // https://github.com/MutabilityDetector/MutabilityDetector
}