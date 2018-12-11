package com.aevi.util.json;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonConverterTest {


    class Amounts {
        String bleep = "bleep";
        String bloop = "bloop";
    }

    class InnerBloop {
        String cheese;

        @JsonConverter.ExposeMethod(value="stink")
        public String getCheeseStinkValue() {
            return "smelly";
        }
    }

    class TestClass extends InnerBloop implements Jsonable {
        private String field1 = "one";
        private String field2 = "two";
        private String field3 = "three";

        @JsonConverter.ExposeMethod(value = "sausage")
        public String getSausage() {
            return "sausage";
        }

        @JsonConverter.ExposeMethod(value = "amounts")
        public Amounts getAmounts() {
            return new Amounts();
        }

        @Override
        public String toJson() {
            return JsonConverter.serialize(this);
        }
    }

    @Test
    public void checkExposedMethod() {
        String json = new TestClass().toJson();
        System.err.println(json);

        assertThat(json).isEqualTo("{\"field1\":\"one\",\"field2\":\"two\",\"field3\":\"three\",\"sausage\":\"sausage\",\"amounts\":{\"bleep" +
                                           "\":\"bleep\",\"bloop\":\"bloop\"},\"stink\":\"smelly\"}");
    }
}
