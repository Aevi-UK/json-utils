# json-utils

Basic collection of JSON utilities.

## JSON Converter

JSON Converter is a simple library for serialising and deserialising arbitrary Java POJOs or collections.

It was created to facilitate an easy way of sending data between activities and services, or between different applications
using our [Android RxMessenger](https://github.com/Aevi-UK/android-rxmessenger) library.

To use these features, have your models implement `Jsonable` or `Sendable`.

`Sendable` is a simple extension of `Jsonable` that also provides an identifier for the model that can be useful to track the JSON that is
 being sent around.

 A typical example POJO looks like this;

 ```
 public class Example implements Jsonable {

     @Override
     public String toJson() {
         return JsonConverter.serialize(this);
     }

     public static Example fromJson(String json) {
         return JsonConverter.deserialize(json, Example.class);
     }
 }
 ```

For collections, `JsonableList` or `JsonableMap` may be used.