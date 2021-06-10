package com.aevi.util.json;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonOptionTest {

    class TestyPlop {
    }

    @Test
    public void testStringType() {
        JsonOption option = new JsonOption("Just a test");
        assertThat(option.getType()).isEqualTo("String");
    }

    @Test
    public void testIntegerType() {
        int integer = 123;
        JsonOption option = new JsonOption(integer);
        assertThat(option.getType()).isEqualTo("Integer");
    }

    @Test
    public void testNonPrimitiveType() {
        TestyPlop obj = new TestyPlop();
        JsonOption option = new JsonOption(obj);
        assertThat(option.getType()).isEqualTo("TestyPlop");
    }
}
