package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

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

    private int size;
    private final float loadFactor;
    private int threshold;
    private final Class<?> componentType;
    private Entry<V>[] entries;

    /**
     * Varargs are used for generate array of generics.
     * It isn't required to pass them
     */
    @SafeVarargs
    public LongMapImpl(V... args) {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, args);
    }

    @SafeVarargs
    public LongMapImpl(Integer initialCapacity, V... args) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, args);
    }


    @SuppressWarnings("unchecked")
    public LongMapImpl(Integer initialCapacity, Float loadFactor, V... args) {
        if (initialCapacity == null || initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive!");
        }
        if (loadFactor == null || loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("loadFactor must be positive!");
        }
        initialCapacity = Math.min(initialCapacity, MAXIMUM_CAPACITY);
        this.entries = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (this.loadFactor * initialCapacity);
        this.componentType = args.getClass().getComponentType();
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(LongMap<V> anotherMap, V... args) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.componentType = args.getClass().getComponentType();
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
        int index = calculateIndex(key);
        Entry<V> existingEntry = entries[index];
        if (existingEntry == null) {
            entries[index] = new Entry<>(key, value);
        } else {
            if (existingEntry.getNext() == null) {
                if (existingEntry.getKey() == key) {
                    existingEntry.setValue(value);
                    return value;
                }
                existingEntry.setNext(new Entry<>(key, value));
                size++;
                resizeIfNeeded();
                return value;
            }
            while (existingEntry.getNext() != null) {
                if (existingEntry.getKey() == key) {
                    existingEntry.setValue(value);
                    return value;
                }
                existingEntry = existingEntry.getNext();
            }
            existingEntry.setNext(new Entry<>(key, value));
        }
        size++;
        resizeIfNeeded();
        return value;
    }

    public V get(long key) {
        Entry<V> entry = entries[calculateIndex(key)];
        if (entry == null) {
            return null;
        }
        if (entry.getKey() == key) {
            return entry.getValue();
        }
        entry = entry.getNext();
        while (entry != null) {
            if (entry.getKey() == key) {
                return entry.getValue();
            }
            entry = entry.getNext();
        }
        return null;
    }

    /**
     * I don't use Long.hashcode 'cause the result
     * is almost the same value as a plain long (excluding negative values)
    * */
    private int calculateIndex(long key) {
        return Math.abs((int) key % entries.length);
    }

    public V remove(long key) {
        int index = calculateIndex(key);
        Entry<V> entry = entries[index];
        if(entry == null) return null;
        if(entry.getNext() == null) {
            V tempValue = entry.getValue();
            entries[index] = null;
            size--;
            return tempValue;
        }
        Entry<V> previousEntry = null;
        while (entry != null) {
            if (entry.getKey() == key) {
                V returnedValue = entry.getValue();
                if (previousEntry != null) {
                    previousEntry.setNext(entry.getNext());
                } else {
                    entries[index] = entry.getNext();
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
        Entry<V> entry = entries[calculateIndex(key)];
        if (entry == null) {
            return false;
        }
        if (entry.getKey() == key) {
            return true;
        }
        entry = entry.getNext();
        while (entry != null) {
            if (entry.getKey() == key) {
                return true;
            }
            entry = entry.getNext();
        }
        return false;
    }

    public boolean containsValue(V value) {
        if (size == 0) {
            return false;
        }
        return Arrays.stream(entries).anyMatch(entry -> isContainsValue(entry, value));
    }

    private boolean isContainsValue(Entry<V> entry, V value) {
        if (entry == null) {
            return false;
        }
        if (entry.getNext() == null) {
            return Objects.equals(entry.getValue(), value);
        } else {
            while (entry != null) {
                if (Objects.equals(entry.getValue(), value)) {
                    return true;
                }
                entry = entry.getNext();
            }
        }
        return false;
    }

    public long[] keys() {
        long[] keys = new long[size];
        if (size > 0) {
            int index = 0;
            for (Entry<V> entry : entries) {
                if (entry != null) {
                    if (entry.getNext() == null) {
                        keys[index++] = entry.getKey();
                    } else {
                        while (entry != null) {
                            keys[index++] = entry.getKey();
                            entry = entry.getNext();
                        }
                    }
                }
            }
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    public V[] values() {
        V[] values = (V[]) Array.newInstance(componentType, size);
        if (size > 0) {
            int index = 0;
            for (Entry<V> entry : entries) {
                if (index == size) {
                    return values;
                }
                if (entry != null) {
                    if (entry.getNext() == null) {
                        values[index++] = entry.getValue();
                    } else {
                        while (entry != null) {
                            values[index++] = entry.getValue();
                            entry = entry.getNext();
                        }
                    }
                }
            }
        }
        return values;
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
        if (size > threshold && size < MAXIMUM_CAPACITY) {
            int nextSize = entries.length * 2;
            entries = Arrays.copyOf(entries, Math.min(nextSize, MAXIMUM_CAPACITY));
            threshold = (int) (entries.length * loadFactor);
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder().append("[");
        for (Entry<V> entry : entries) {
            while (entry != null) {
                stringBuilder.append(entry.toString()).append(",");
                entry = entry.getNext();
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.append("]").toString();
    }

    static class Entry<V> {
        private final long key;
        private V value;
        private Entry<V> next;

        public long getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Entry<V> getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Entry<V> next) {
            this.next = next;
        }

        public Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
