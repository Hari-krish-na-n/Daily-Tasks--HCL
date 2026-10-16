package com.auction.lab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Day 10 Memory Lab Experiments Test Suite")
class MemoryLabTest {

    @Test
    @DisplayName("Experiment 1: StackOverflowError is thrown upon deep recursion")
    void testStackOverflowErrorThrown() {
        assertThrows(StackOverflowError.class, () -> StackOverflowExperiment.recursiveMethod(1L));
        assertTrue(StackOverflowExperiment.getRecursionDepth() > 500,
                "Recursion depth should exceed at least 500 frames before exhausting thread stack");
    }

    @Test
    @DisplayName("Experiment 3: String and StringBuilder produce identical concatenated content")
    void testStringVsStringBuilderEquivalence() {
        int count = 100;
        String s = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            s += "a";
            sb.append("a");
        }
        assertEquals(s.length(), sb.length());
        assertEquals(s, sb.toString());
    }

    @Test
    @DisplayName("Experiment 5: Boxed Integer Cache guarantees == inside [-128, 127] and equals outside")
    void testIntegerCacheSemantics() {
        Integer inside1 = 100;
        Integer inside2 = 100;
        assertSame(inside1, inside2, "Values within [-128, 127] must be cached singletons (== is true)");

        Integer outside1 = 1000;
        Integer outside2 = 1000;
        assertNotSame(outside1, outside2, "Values outside default cache range should be distinct heap objects");
        assertEquals(outside1, outside2, ".equals() must compare value equality and return true");
    }
}
