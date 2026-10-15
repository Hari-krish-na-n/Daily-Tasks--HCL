package com.auction.toolkit;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Generic Toolkit demonstrating:
 * 1. Pair<K, V> - stores two values of potentially different types
 * 2. Box<T extends Number> - bounded type parameter accepting only numeric types
 * 3. max() with bound <T extends Comparable<T>>
 * 4. Method with ? extends Number (PECS - Producer Extends, reading numbers)
 * 5. Method with ? super Integer (PECS - Consumer Super, writing integers)
 * 6. Explanation and demonstration of PECS and Type Erasure
 */
public class GenericToolkit {

    // 1. Generic Pair<K, V>
    public static class Pair<K, V> {
        private final K first;
        private final V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        public K getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return String.format("Pair[%s, %s]", first, second);
        }
    }

    // 2. Bounded Box<T extends Number>
    public static class Box<T extends Number> {
        private final T value;

        public Box(T value) {
            this.value = Objects.requireNonNull(value, "Box value cannot be null");
        }

        public T getValue() {
            return value;
        }

        public double doubleValue() {
            return value.doubleValue();
        }

        public boolean isGreaterThan(Box<? extends Number> other) {
            if (other == null) return true;
            return this.doubleValue() > other.doubleValue();
        }

        @Override
        public String toString() {
            return "Box(" + value + ")";
        }
    }

    // 3. Generic max method with bound <T extends Comparable<T>>
    public static <T extends Comparable<T>> T max(T a, T b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static <T extends Comparable<T>> T max(List<T> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        T maxItem = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current != null && (maxItem == null || current.compareTo(maxItem) > 0)) {
                maxItem = current;
            }
        }
        return maxItem;
    }

    // 4. Upper bounded wildcard (? extends Number) - Producer Extends (read-only from producer)
    public static double sumOfNumbers(List<? extends Number> numbers) {
        if (numbers == null) return 0.0;
        double sum = 0.0;
        for (Number n : numbers) {
            if (n != null) {
                sum += n.doubleValue();
            }
        }
        return sum;
    }

    // 5. Lower bounded wildcard (? super Integer) - Consumer Super (write-into consumer)
    public static void appendIntegers(Collection<? super Integer> consumer, int from, int to) {
        if (consumer == null) return;
        for (int i = from; i <= to; i++) {
            consumer.add(i); // Safe because consumer accepts Integer or any supertype (Number, Object)
        }
    }

    // 6. PECS demonstration helper
    public static <T> void copy(List<? extends T> src, List<? super T> dest) {
        // src is Producer (extends) -> only read from it
        // dest is Consumer (super) -> write into it
        for (T item : src) {
            dest.add(item);
        }
    }
}
