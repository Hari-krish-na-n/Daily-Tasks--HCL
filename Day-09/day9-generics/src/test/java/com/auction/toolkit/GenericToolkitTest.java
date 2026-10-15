package com.auction.toolkit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying Day 9 Generic Toolkit:
 * - Pair with different types
 * - Box with numeric types
 * - Generic max() with comparable values
 * - Upper- and lower-bounded wildcard examples (PECS)
 */
public class GenericToolkitTest {

    @Test
    @DisplayName("Pair holds two values of different types and supports equality")
    void testPairWithDifferentTypes() {
        GenericToolkit.Pair<String, Integer> pair1 = new GenericToolkit.Pair<>("Score", 100);
        GenericToolkit.Pair<String, Integer> pair2 = new GenericToolkit.Pair<>("Score", 100);
        GenericToolkit.Pair<String, Integer> pair3 = new GenericToolkit.Pair<>("Score", 99);

        assertEquals("Score", pair1.getFirst());
        assertEquals(100, pair1.getSecond());
        assertEquals(pair1, pair2);
        assertNotEquals(pair1, pair3);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    @DisplayName("Box accepts numeric types and computes comparisons correctly")
    void testBoxWithNumericTypes() {
        GenericToolkit.Box<Integer> b1 = new GenericToolkit.Box<>(50);
        GenericToolkit.Box<Double> b2 = new GenericToolkit.Box<>(45.5);

        assertEquals(50, b1.getValue());
        assertEquals(50.0, b1.doubleValue());
        assertTrue(b1.isGreaterThan(b2));
        assertFalse(b2.isGreaterThan(b1));
    }

    @Test
    @DisplayName("Generic max() returns maximum of comparable values")
    void testGenericMax() {
        assertEquals(99, GenericToolkit.max(45, 99));
        assertEquals("Orange", GenericToolkit.max("Apple", "Orange"));
        assertEquals(100.5, GenericToolkit.max(List.of(10.2, 55.4, 100.5, 3.1)));
        assertNull(GenericToolkit.max(Collections.<Integer>emptyList()));
    }

    @Test
    @DisplayName("Upper bounded wildcard (? extends Number) reads and sums values")
    void testUpperBoundedWildcard() {
        List<Integer> intList = List.of(10, 20, 30);
        List<Double> doubleList = List.of(1.5, 2.5);

        assertEquals(60.0, GenericToolkit.sumOfNumbers(intList), 0.001);
        assertEquals(4.0, GenericToolkit.sumOfNumbers(doubleList), 0.001);
    }

    @Test
    @DisplayName("Lower bounded wildcard (? super Integer) appends integers to consumer")
    void testLowerBoundedWildcard() {
        List<Number> numList = new ArrayList<>();
        GenericToolkit.appendIntegers(numList, 1, 3);

        assertEquals(3, numList.size());
        assertEquals(1, numList.get(0));
        assertEquals(2, numList.get(1));
        assertEquals(3, numList.get(2));

        List<Object> objList = new ArrayList<>();
        GenericToolkit.appendIntegers(objList, 10, 11);
        assertEquals(2, objList.size());
    }

    @Test
    @DisplayName("PECS copy method correctly copies from producer to consumer")
    void testPecsCopy() {
        List<Integer> producer = List.of(10, 20, 30);
        List<Number> consumer = new ArrayList<>();

        GenericToolkit.copy(producer, consumer);
        assertEquals(List.of(10, 20, 30), consumer);
    }
}
