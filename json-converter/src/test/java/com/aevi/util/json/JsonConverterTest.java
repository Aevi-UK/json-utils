package com.aevi.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonConverterTest {


    class Amounts {
        String bleep = "bleep";
        String bloop = "bloop";

        @JsonConverter.ExposeMethod(value="totalAmount")
        public String getTotalAmount() {
            return "bleepbloop";
        }
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

        @JsonConverter.ExposeMethod(value = "primitiveExtra")
        public JsonOption getPrimitiveExtra() {
            return new JsonOption("blop");
        }

        @JsonConverter.ExposeMethod(value = "innerExtra")
        public JsonOption getInnerExtra() {
            return new JsonOption(new InnerBloop());
        }

        @Override
        public String toJson() {
            return JsonConverter.serialize(this);
        }

        public String toJsonWithMethods() {
            return JsonConverter.serializeWithExposedMethods(this);
        }
    }

    @Test
    public void checkExposedMethod() {
        String jsonWithout = new TestClass().toJson();
        String jsonWith = new TestClass().toJsonWithMethods();


        Gson gson = new GsonBuilder().create();
        JsonObject objWith = gson.fromJson(jsonWith, JsonObject.class);
        assertThat(objWith.has("field1")).isTrue();
        assertThat(objWith.has("field2")).isTrue();
        assertThat(objWith.has("field3")).isTrue();
        assertThat(objWith.has("amounts")).isTrue();
        assertThat(objWith.has("stink")).isTrue();
        assertThat(objWith.getAsJsonObject("amounts").has("bleep")).isTrue();
        assertThat(objWith.getAsJsonObject("amounts").has("bloop")).isTrue();
        assertThat(objWith.getAsJsonObject("amounts").has("totalAmount")).isTrue();
        assertThat(objWith.has("sausage")).isTrue();

        JsonObject objWithOut = gson.fromJson(jsonWithout, JsonObject.class);
        assertThat(objWithOut.has("field1")).isTrue();
        assertThat(objWithOut.has("field2")).isTrue();
        assertThat(objWithOut.has("field3")).isTrue();
        assertThat(objWithOut.has("stink")).isFalse();
        assertThat(objWithOut.has("amounts")).isFalse();
        assertThat(objWithOut.has("sausage")).isFalse();

    }

    @Test
    public void checkOptionSerailization() {
        String json = new TestClass().toJsonWithMethods();
        TestClass obj = JsonConverter.deserialize(json, TestClass.class);
        assertThat(obj.getPrimitiveExtra().getType()).isEqualTo("string");
        assertThat(obj.getPrimitiveExtra().getValue()).isEqualTo("blop");
        assertThat(obj.getInnerExtra().getValue()).isInstanceOf(InnerBloop.class);
    }
}
