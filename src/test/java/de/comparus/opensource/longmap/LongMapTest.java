package de.comparus.opensource.longmap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class LongMapTest {

    static LongMap<String> longMap = new LongMapImpl<>();

    @Before
    public void setUp() {
        longMap.clear();
    }

    @Test
    public void shouldReturnThePuttedValue() {
        String expected = "Expected";
        String actual = longMap.put(45L, expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnZeroAfterClear() {
        longMap.put(45L, "Value");
        longMap.clear();
        Assert.assertEquals(0, longMap.size());
    }

    @Test
    public void shouldReturnValueByKey() {
        String testCaseOne = "testCaseOne";
        String testCaseTwo = "testCaseTwo";
        String testCaseThree = "testCaseThree";
        longMap.put(0L, testCaseOne);
        longMap.put(-90L, testCaseTwo);
        longMap.put(999999999L, testCaseThree);
        Assert.assertEquals(testCaseOne, longMap.get(0L));
        Assert.assertEquals(testCaseTwo, longMap.get(-90L));
        Assert.assertEquals(testCaseThree, longMap.get(999999999L));
    }

    @Test
    public void shouldReturnRemovedValue() {
        String expected = "Expected";
        longMap.put(1L, expected);
        String actual = longMap.remove(1L);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullAfterRemoving() {
        longMap.put(1L, "TestCaseOne");
        longMap.remove(1L);
        Assert.assertNull(longMap.get(1L));
    }

    @Test
    public void shouldReturnTrueIfEmpty() {
        Assert.assertTrue(longMap.isEmpty());
    }

    @Test
    public void shouldReturnFalseIfNotEmpty() {
        longMap.put(1L, "notEmpty");
        Assert.assertFalse(longMap.isEmpty());
    }

    @Test
    public void shouldReturnFalseIfNotContainsKey() {
        longMap.put(1L, "Value");
        Assert.assertFalse(longMap.containsKey(200L));
    }

    @Test
    public void shouldReturnTrueIfContainsKey() {
        longMap.put(1L, "Value");
        Assert.assertTrue(longMap.containsKey(1L));
    }

    @Test
    public void shouldReturnFalseIfNotContainsValue() {
        longMap.put(1L, "Value");
        Assert.assertFalse(longMap.containsValue("Key"));
    }

    @Test
    public void shouldReturnTrueIfContainsValue() {
        longMap.put(1L, "Value");
        Assert.assertTrue(longMap.containsValue("Value"));
    }

    @Test
    public void shouldReturnArrayOfKeys() {
        long[] expectedArray = new long[]{-12L, 0L, 33L, 66L};
        for (long key : expectedArray) {
            longMap.put(key, "someValue");
        }
        long[] actualArray = longMap.keys();
        Arrays.sort(actualArray);
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void shouldReturnArrayOfValues() {
        String[] expectedArray = new String[]{"A", "B", "C", "D"};
        for (int i = 0; i < expectedArray.length; i++) {
            longMap.put(i, expectedArray[i]);
        }
        String[] actualArray = longMap.values();
        Arrays.sort(actualArray);
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void shouldReturnSizeOfLongMap() {
        long expectedSize = 2;
        longMap.put(1L, "TestCaseOne");
        longMap.put(2L, "TestCaseTwo");
        long actualSize = longMap.size();
        Assert.assertEquals(2, actualSize);
    }

    @Test
    public void shouldReturnNullWhenGettingValues() {
        longMap.put(22, null);
        Assert.assertNull(longMap.values());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseOfCapacity() {
        new LongMapImpl<>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseOfLoadFactor() {
        new LongMapImpl<>(300, -2);
    }
}