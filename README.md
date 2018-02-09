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

# Binaries

In your main gradle.build you'll need to include our public bintray in your main
repositories section.

```
repositories {
    maven {
        url "http://dl.bintray.com/aevi/aevi-uk"
    }
}
```

And then add to your dependencies section

```
implementation 'com.aevi.util:json-converter:<version>'
```

# Bugs and Feedback

For bugs, feature requests and discussion please use [GitHub Issues](https://github.com/Aevi-UK/android-pos-print-api/issues)

# LICENSE

Copyright 2018 AEVI International GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
