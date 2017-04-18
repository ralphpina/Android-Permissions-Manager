# Android Permissions Manager
Easily manage Android Marshmallow and up runtime permissions. This library uses RXJava to skip all the painful parts of the `Activity`/`Fragment` lifecycle management.

The RxJava architecture was first taken from work originally done by [Fai Al Qadi](https://github.com/FaiFai21).

### Checking Permissions

```java
PermissionsManager.get()
      .requestCameraPermission()
      .subscribe(new Action1<PermissionsResult>() {
          @Override
          public void call(PermissionsResult permissionsResult) {
            if (permissionsResult.isGranted()) { // always true pre-M
              // do whatever
            }
            if (permissionsResult.hasAskedForPermissions()) { // false if pre-M
              // do whatever
            }
          }
      });
```

This library is backwards compatible. In pre-Marshmallow devices permissions are returned as given. This is done using the Android Support library ```ActivityCompat``` and support ```Fragment``` methods for permissions. I've tried to make sure this library is well tested.

Javadocs can be found in the [docs](/docs) folder.

# Including Library
JCenter is a pain to maintan, so I use [Jitpack.io](https://jitpack.io).

You can include it in your gradle file like so:

```groovy
repositories {
  maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.ralphpina:Android-Permissions-Manager:v2.0.1'
}
```

# Usage
This library provides an interface to request ```PROTECTION_DANGEROUS``` Android permissions. Permissions fall into groups that are granted by the user. See which permissions fall into each group in the [Android docs](http://developer.android.com/guide/topics/security/permissions.html#perm-groups).Right now the library supports the following permission groups:

- CALENDAR
- CAMERA
- CONTACTS
- LOCATION
- MICROPHONE
- PHONE
- STORAGE
- BODY SENSORS
- SMS

See [Request Other Permissions](#request-other-permissions) to see how to use this library to request various permissions in different groups.

The API allows you to check 3 things:
  1. Whether the permission has been granted.
  2. Whether you've previously asked the user before.
  3. Whether the user has selected the "Do not ask" option.

Check out the sample app included in the ```app``` folder for an example of how it works.

### Initialization
Initialize this app on startup in your ```Application``` class or some other singleton tied to the ```Application Context```.

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PermissionsManager.init(this);
    }
}
```

Make sure to include this ```Application``` subclass in your ```AndroidManifest.xml```.

```xml
<manifest package="net.ralphpina.permissionsmanager.sample"
          xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:name=".MyApplication"
        ...
    </application>
    
</manifest>
```

Make sure to include whatever permissions you will need in the ```AndroidManifest.xml```. <b>You still need to include the appropriate permissions in your manifest before asking for them at runtime.<b>

```xml
<manifest package="net.ralphpina.permissionsmanager.sample"
          xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- ==== android.permission-group.CAMERA ==== -->
    <uses-permission android:name="android.permission.CAMERA" />

    ...
    
</manifest>
```                

### Calendar

```java
PermissionsManager.get()
                  .isCalendarGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForCalendarPermission()
```
```java
PermissionsManager.get()
                  .neverAskForCalendar(mActivity);
```

### Camera

```java
PermissionsManager.get()
                  .isCameraGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForCameraPermission()
```
```java
PermissionsManager.get()
                  .neverAskForCamera(mActivity);
```

### Contacts

```java
PermissionsManager.get()
                  .isContactsGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForContactsPermission()
```
```java
PermissionsManager.get()
                  .neverAskForContacts(mActivity);
```

### Location

```java
PermissionsManager.get()
                  .isLocationGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForLocationPermission()
```
```java
PermissionsManager.get()
                  .neverAskForLocation(mActivity);
```

### Microphone

```java
PermissionsManager.get()
                  .isAudioRecordingGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForAudioRecordingPermission()
```
```java
PermissionsManager.get()
                  .neverAskForAudio(mActivity);
```

### Phone

```java
PermissionsManager.get()
                  .isCallingGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForCallingPermission()
```
```java
PermissionsManager.get()
                  .neverAskForCalling(mActivity);
```

### Storage

```java
PermissionsManager.get()
                  .isStorageGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForStoragePermission()
```
```java
PermissionsManager.get()
                  .neverAskForStorage(mActivity);
```

### Body Sensors

```java
PermissionsManager.get()
                  .isBodySensorGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForBodySensorPermission()
```
```java
PermissionsManager.get()
                  .neverAskForBodySensor(mActivity);
```

### SMS

```java
PermissionsManager.get()
                  .isSmsGranted()
```
```java
PermissionsManager.get()
                  .hasAskedForSmsPermission()
```
```java
PermissionsManager.get()
                  .neverAskForSms(mActivity);
```

# Never Ask Again
If the user has selected "Never ask again" you can intent into the app's settings using the following:

```java
PermissionsManager.get()
                  .intentToAppSettings(activity);
```

**If the user selected "Never ask again", then they give you permissions in the app settings page, and then remove them, this method will return true. Even though at that point you can ask for permissions. I have not been able to figure out a way around this.**

# Requesting Multiple Permissions
While there are methods to request some of the more common permissions, if you want to request multiple permissions at once:

```java
PermissionsManager.get()
      .requestPermissions(REQUEST_CAMERA_PERMISSION, REQUEST_LOCATION_PERMISSION)
      .subscribe(new Action1<PermissionsResult>() {
          @Override
          public void call(PermissionsResult permissionsResult) {
            if (permissionsResult.isGranted()) { // always true pre-M
              // do whatever
            }
            if (permissionsResult.hasAskedForPermissions()) { // false if pre-M
              // do whatever
            }
          }
      });
```

# Contributing
If you plan on contributing, please make sure to update the README and Javadocs if there are API changes and add tests!

**To generate Javadocs:**
-
./gradlew generateReleaseJavadoc

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
