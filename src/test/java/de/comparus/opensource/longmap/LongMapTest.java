package de.comparus.opensource.longmap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class LongMapTest {

    static LongMap<String> longMap = new LongMapImpl<>(String.class);

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
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldReturnContainsKeyTrueIfValueIsNull() {
        longMap.put(1, null);
        Assert.assertTrue(longMap.containsKey(1));
    }

    @Test
    public void shouldRemoveElementInOneBucket() {
        LongMap<Integer> map = new LongMapImpl<>(1, 10, Integer.class);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.remove(3);
        long[] expectKeyArray = {1, 2, 4};
        Integer[] expectValueArray = {1, 2, 4};
        long[] actualKeyArray = map.keys();
        Integer[] actualValueArray = map.values();
        Arrays.sort(actualKeyArray);
        Arrays.sort(actualValueArray);
        Assert.assertArrayEquals(expectKeyArray, actualKeyArray);
        Assert.assertArrayEquals(expectValueArray, actualValueArray);
    }

    @Test
    public void shouldReturnContainsValueTrueIfValueIsNull() {
        longMap.put(1, null);
        Assert.assertTrue(longMap.containsValue(null));
    }

    @Test
    public void shouldReturnNotEmptyArrayWhenGettingValues() {
        longMap.put(22, null);
        String[] expectedArray = new String[1];
        Assert.assertArrayEquals(expectedArray, longMap.values());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseOfCapacity() {
        new LongMapImpl<>(0, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseOfLoadFactor() {
        new LongMapImpl<>(300, -2, String.class);
    }

}