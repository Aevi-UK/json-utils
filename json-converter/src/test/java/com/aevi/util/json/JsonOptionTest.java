package com.aevi.util.json;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonOptionTest {

    class TestyPlop {
    }

    @Test
    public void testStringType() {
        JsonOption option = new JsonOption("Just a test");
        assertThat(option.getType()).isEqualTo("string");
    }

    @Test
    public void testIntegerType() {
        int integer = 123;
        JsonOption option = new JsonOption(integer);
        assertThat(option.getType()).isEqualTo("integer");
    }

    @Test
    public void testNonPrimitiveType() {
        TestyPlop obj = new TestyPlop();
        JsonOption option = new JsonOption(obj);
        assertThat(option.getType()).isEqualTo("testyplop");
    }

    @Test
    public void testArrayType() {
        JsonOption option = new JsonOption(new int[] { 1, 2, 3 });
        assertThat(option.getType()).isEqualTo("int[]");
    }

    @Test
    public void testArrayListType() {
        ArrayList<String> data = new ArrayList<>();
        data.add("one");
        data.add("two");
        JsonOption option = new JsonOption(data);
        assertThat(option.getType()).isEqualTo("arraylist");
    }
}
