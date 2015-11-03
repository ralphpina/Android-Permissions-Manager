# Android Permissions Manager
Easily manage Android Marshmellow and up runtime permissions.

This library is backwards compatible. In pre-Marshmallow devices permissions are returned as given. This is done using the Support Library methods for permissions.

# Never Ask Again
If the user has selected "Never ask again" you can intent into the app's settings using the following:

```java
PermissionsManager.get()
                  .intentToAppSettings(activity);
```

# Request Other Permissions
While there are methods to request some of the more common permissions, if the one you need is not there you can still use this library to request it. The example below tries to request body sensors:

```java
PermissionsManager.get()
                  .requestPermission(activity, REQUEST_CODE, BODY_SENSORS);
```

To call into the should show request permission rationale methods you can also use this library:
```java
PermissionsManager.get()
                  .shouldShowRequestPermissionRationale(activity, BODY_SENSORS);
```

# Contributing
If you plan on contributing, please make sure to update the README and Javadocs if there are API changes and add tests!


# License
    Copyright 2015 Ralph Pina.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
