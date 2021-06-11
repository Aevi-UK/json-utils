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

/**
 * This class allows you to store a java Object of any type in JSON and restore it again afterwards to the correct Class type.
 * <p>
 * If serialised to JSON the JSON produced contains the objects type (class) in a parameter called `type` and the objects data JSON serialised into
 * the `value` parameter.
 */
public class JsonOption implements Jsonable {

    private final Object value;
    private final String type;

    public JsonOption(Object value) {
        this.value = value;
        this.type = value.getClass().getSimpleName().toLowerCase();
    }

    public JsonOption(Object value, String forcedType) {
        this.value = value;
        this.type = forcedType;
    }

    /**
     * @return The source object
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return The source objects type/class
     */
    public String getType() {
        return type;
    }

    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonOption that = (JsonOption) o;

        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
