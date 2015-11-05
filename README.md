# Android Permissions Manager
Easily manage Android Marshmellow and up runtime permissions.

This library is backwards compatible. In pre-Marshmallow devices permissions are returned as given. This is done using the Android Support library ```ActivityCompat``` and support ```Fragment``` methods for permissions.

# Including Library
Right now the entire ```library``` module needs to be imported into your project. However, I plan to have it up in Maven before 1.0.

# Usage
Right now the library supports the following permissions. See [Request Other Permissions](#request-other-permissions) to see how to use this library for permissions not listed below. Namely SENSORS and SMS.

# Never Ask Again
If the user has selected "Never ask again" you can intent into the app's settings using the following:

```java
PermissionsManager.get()
                  .intentToAppSettings(activity);
```

**If the user selected "Never ask again", then they give you permissions in the app settings page, and then remove them in the same page. This method will return true. Even though at that point you can ask for permissions. I have not been able to figure out a way around this.**

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
