package de.comparus.opensource.longmap;

import lombok.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Ihor Biedin
 */

public class LongMapImpl<V> implements LongMap<V> {

    /**
     * The default value used for initialization size of array
     * if none specified in the constructor
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * The default value used in the case of the argument
     * from a constructor is greater than 1 << 30
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Default load factor used for deciding when the size
     * of the array should changes if none specified in the constructor
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.85f;

    private long size;
    private final float loadFactor;
    private Entry<V>[] entries;

    public LongMapImpl() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public LongMapImpl(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive!");
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("loadFactor must be positive!");
        }
        this.entries = new Entry[Math.min(initialCapacity, MAXIMUM_CAPACITY)];
        this.loadFactor = loadFactor;
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(LongMap<V> anotherMap) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        int initialCapacity = anotherMap != null
                ? Math.max((int) anotherMap.size(), DEFAULT_INITIAL_CAPACITY)
                : DEFAULT_INITIAL_CAPACITY;
        this.entries = new Entry[initialCapacity];
        if (anotherMap != null && anotherMap.size() > 0) {
            for (long key : anotherMap.keys()) {
                this.put(key, anotherMap.get(key));
            }
        }
    }

    public V put(long key, V value) {
        Entry<V> entry = new Entry<>(key, value, null);
        int index = Math.abs((int) key % this.entries.length);
        Entry<V> existingEntry = entries[index];
        if (existingEntry == null) {
            entries[index] = entry;
            size++;
            resizeIfNeeded();
        } else {
            while (existingEntry.getNext() != null) {
                if (existingEntry.getKey() == key) {
                    existingEntry.setValue(value);
                    return value;
                }
                existingEntry = existingEntry.getNext();
            }
            if (existingEntry.getKey() == key) {
                existingEntry.setValue(value);
            } else {
                existingEntry.setNext(entry);
                size++;
                resizeIfNeeded();
            }
        }
        return value;
    }

    public V get(long key) {
        Entry<V> entry = entries[Math.abs((int) key % entries.length)];
        while (entry != null) {
            if (entry.getKey() == key) {
                return entry.getValue();
            }
            entry = entry.getNext();
        }
        return null;
    }

    public V remove(long key) {
        Entry<V> entry = entries[Math.abs((int) key % entries.length)];
        Entry<V> previousEntry = null;
        while (entry != null) {
            if (entry.getKey() == key) {
                V returnedValue = entry.getValue();
                if (previousEntry != null) {
                    previousEntry.setNext(entry.getNext());
                } else {
                    entries[Math.abs((int) key % entries.length)] = entry.getNext();
                }
                entry.setNext(null);
                size--;
                return returnedValue;
            }
            previousEntry = entry;
            entry = entry.getNext();
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        Entry<V> entry = entries[Math.abs((int) key % entries.length)];
        if (entry == null) {
            return false;
        }
        while (entry != null) {
            if (entry.getKey() == key) {
                return true;
            }
            entry = entry.getNext();
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Entry<V> entry : entries) {
            while (entry != null) {
                if ((value == null && entry.getValue() == null) || entry.getValue().equals(value)) {
                    return true;
                }
                entry = entry.getNext();
            }
        }
        return false;
    }

    public long[] keys() {
        long[] arrayOfKeys = new long[(int) size];
        int index = 0;
        if (size > 0) {
            for (Entry<V> entry : entries) {
                while (entry != null) {
                    arrayOfKeys[index] = entry.getKey();
                    entry = entry.getNext();
                    index++;
                }
            }
        }
        return arrayOfKeys;
    }

    @SuppressWarnings("unchecked")
    public V[] values() {
        List<V> valuesList = new ArrayList<>();
        if (size > 0) {
            for (Entry<V> entry : entries) {
                while (entry != null) {
                    valuesList.add(entry.getValue());
                    entry = entry.getNext();
                }
            }
            for (V object : valuesList) {
                if (object != null) {
                    V[] genericArray = (V[]) Array.newInstance(object.getClass(), (int) size);
                    return valuesList.toArray(genericArray);
                }
            }
        }
        return null;
    }

    public long size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        entries = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    private void resizeIfNeeded() {
        if (size > entries.length * loadFactor) {
            entries = Arrays.copyOf(entries, entries.length * 2);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder stringBuilder = new StringBuilder().append("[");
        for (Entry<V> entry : entries) {
            if (entry != null) {
                while (entry != null) {
                    stringBuilder.append(entry.getKey())
                            .append("=").append(entry.getValue()).append(",");
                    entry = entry.getNext();
                }
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.append("]").toString();
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Entry<V> {
        private long key;
        private V value;
        private Entry<V> next;

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
