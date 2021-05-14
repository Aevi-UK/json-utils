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

# Exposed methods

This library makes use of GSON for the serialisation to/from JSON. Therefore, only field values will be exported in your serialised objects.

This library contains an annotation that can be used to expose method data in your JSON as well.

```
    class Amounts {
        String currency = "GBP";
        long baseAmount = 10;
        long otherAmount = 5;

        @JsonConverter.ExposeMethod(value="totalAmount")
        public long getTotalAmount() {
            return baseAmount + otherAmount;
        }
    }
```

The example above will export the following JSON which includes the totalAmount parameter

```
{
  currency: "GBP",
  baseAmount: 10,
  otherAmount: 5,
  totalAmount: 15
}
```

getting data from exposed methods requires extra processing on behalf of the serialisation mechanism, therefore in order
to serialise with exposed methods you must call `JsonConverter.serializeWithExposedMethods(object)`

When deserialising your the JSON data above the `totalAmount` field will be ignored.


# Binaries

As of json-utils 1.1.1, artifacts are published to Github Packages instead of `jcenter`. This is due to JFrog [shutting down jcenter](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/).

Add the below entry to your `repositories` closure in the root `build.gradle` file. Note that unfortunately Github requires authentication for programmatic access of public packages (see [this thread](https://github.community/t/download-from-github-package-registry-without-authentication/14407/85) for details on this issue). This means that you must authenticate with a Github username and [generate a PAT (personal access token)](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token) in order to access the dependencies as per below. If you or your organisation don't already have a Github account, you can sign up for one [here](https://github.com/join).

```
maven {
        name = "AEVI-UK"
        url = uri("https://maven.pkg.github.com/aevi-uk/*")
        credentials {
            username = <your Github username>
            password = <your Github personal access token with `read:packages` enabled>
        }
    }
```

Artifacts version 1.1.0 and earlier will still be available via `jcenter`.

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
