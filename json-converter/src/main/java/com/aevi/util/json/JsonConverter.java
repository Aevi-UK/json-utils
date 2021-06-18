/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aevi.util.json;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a number of useful serialiser/deserialiser implementations to handle the objects in this library.
 *
 * It also contains a serialiser/desrialiser to allow Android {@link Bitmap} objects to be stored in JSON.
 */
public final class JsonConverter {

    private static final Gson GSON;
    private static final Gson GSON_WITH_METHODS;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(JsonOption.class, new ExtrasDeserialiser())
                .registerTypeAdapter(Bitmap.class, new BitmapDeserialiser())
                .registerTypeAdapter(Bitmap.class, new BitmapSerialiser())
                .registerTypeAdapterFactory(new PostProcessingHandler())
                .create();
        GSON_WITH_METHODS = new GsonBuilder()
                .registerTypeAdapter(JsonOption.class, new ExtrasDeserialiser())
                .registerTypeAdapter(Bitmap.class, new BitmapDeserialiser())
                .registerTypeAdapter(Bitmap.class, new BitmapSerialiser())
                .registerTypeAdapterFactory(new ExposedMethodProcessingHandler())
                .create();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD) //can use in method only.
    public @interface ExposeMethod {
        /**
         * @return The name of the field to store the serialized result of the method
         */
        String value();
    }

    public static String serialize(Object object) {
        return GSON.toJson(object);
    }

    public static String serializeWithExposedMethods(Object object) {
        return GSON_WITH_METHODS.toJson(object);
    }

    public static JsonElement serializeToTree(Object object) {
        return GSON.toJsonTree(object);
    }

    public static JsonElement serializeToTreeWithExposedMethods(Object object) {
        return GSON_WITH_METHODS.toJsonTree(object);
    }

    public static <T> T deserialize(String json, Class<T> type) throws JsonParseException {
        return type.cast(GSON.fromJson(json, type));
    }

    public static <T> T deserialize(String json, Type type) throws JsonParseException {
        return GSON.fromJson(json, type);
    }

    private static class ExposedMethodProcessingHandler implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T src) throws IOException {
                    JsonElement element = delegate.toJsonTree(src);

                    if (element.isJsonObject()) {
                        JsonObject object = (JsonObject) element;
                        for (Method m : getAnnotatedMembers(src.getClass())) {
                            try {
                                m.setAccessible(true);
                                Object value = m.invoke(src);
                                object.add(m.getAnnotation(com.aevi.util.json.JsonConverter.ExposeMethod.class).value(), GSON_WITH_METHODS.toJsonTree(value));
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                // fall thru...
                            }
                        }
                        GSON_WITH_METHODS.toJson(object, out);
                    } else {
                        delegate.write(out, src);
                    }
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    return delegate.read(in);
                }

                private Method[] getAnnotatedMembers(Class<?> jsonable) {
                    Method[] methods = jsonable.getMethods();
                    List<Method> annotated = new ArrayList<>();
                    for (Method m : methods) {
                        if (m.getAnnotation(com.aevi.util.json.JsonConverter.ExposeMethod.class) != null) {
                            annotated.add(m);
                        }
                    }
                    return annotated.toArray(new Method[annotated.size()]);
                }
            };
        }
    }

    private static class PostProcessingHandler implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T src) throws IOException {
                    delegate.write(out, src);
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    T object = delegate.read(in);
                    if (object instanceof JsonPostProcessing) {
                        ((JsonPostProcessing) object).onJsonDeserialisationCompleted();
                    }
                    return object;
                }
            };
        }
    }

    private static class BitmapSerialiser implements JsonSerializer<Bitmap> {

        @Override
        public JsonElement serialize(Bitmap src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(writeBitmap(src));
        }
    }

    private static class BitmapDeserialiser implements JsonDeserializer<Bitmap> {

        @Override
        public Bitmap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return readBitmap(json.getAsString());
        }
    }

    private static class ExtrasDeserialiser implements JsonDeserializer<JsonOption> {

        public JsonOption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();
            JsonElement entry = obj.get("value");
            return new JsonOption(entry);
        }
    }

    private static String writeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 70, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    private static Bitmap readBitmap(String in) {
        byte[] pngBytes = Base64.decode(in.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(pngBytes, 0, pngBytes.length);
    }
}
