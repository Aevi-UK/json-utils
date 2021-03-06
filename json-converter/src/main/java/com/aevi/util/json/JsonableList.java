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

import java.util.ArrayList;
import java.util.Collection;

/**
 * An extension of {@link ArrayList} that allows serialisation/deserialisation of lists by the {@link JsonConverter}
 * @param <E> List object type
 */
public class JsonableList<E> extends ArrayList<E> implements Jsonable {

    public JsonableList() {
    }

    public JsonableList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static JsonableList fromJson(String json) {
        return JsonConverter.deserialize(json, JsonableList.class);
    }
}
