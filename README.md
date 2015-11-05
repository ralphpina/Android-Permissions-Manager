# Android Permissions Manager
Easily manage Android Marshmellow and up runtime permissions.

This library is backwards compatible. In pre-Marshmallow devices permissions are returned as given. This is done using the Android Support library ```ActivityCompat``` and support ```Fragment``` methods for permissions. I've tried to make sure this library is well tested.

# Including Library
Right now the entire ```library``` module needs to be imported into your project. However, I plan to have it up in Maven before 1.0.

# Usage
This library provides an interface to request ```PROTECTION_DANGEROUS``` Android permissions. Permissions fall into groups that are granted by the user. See which permissions fall into each group in the [Android docs](http://developer.android.com/guide/topics/security/permissions.html#perm-groups).Right now the library supports the following permission groups:

- CALENDAR
- CAMERA
- CONTACTS
- LOCATION
- MICROPHONE
- PHONE
- STORAGE

See [Request Other Permissions](#request-other-permissions) to see how to use this library for permissions not listed above, namely:

- SENSORS
- SMS.

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
        android:name=".PMApplication"
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

### Checking ```onRequestPermissionsResult``` methods
Below are the main methods, however, you can also check out the Javadocs. Normally, after asking for a permission all you need to do is check in ```onResume``` whether the user has given you the permission. Otherwise, you can check using the following request codes in the ```Activity.onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)``` or ```Fragment.onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)``` methods of your view controller.

```java
PermissionsManager.REQUEST_CAMERA_PERMISSION;
PermissionsManager.REQUEST_LOCATION_PERMISSION;
PermissionsManager.REQUEST_AUDIO_RECORDING_PERMISSION;
PermissionsManager.REQUEST_CALENDAR_PERMISSION;
PermissionsManager.REQUEST_CONTACTS_PERMISSION;
PermissionsManager.REQUEST_STORAGE_PERMISSION;
PermissionsManager.REQUEST_CALL_PHONE_PERMISSION;
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestCalendarPermission(this);
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestCameraPermission(this);
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestContactsPermission(this);
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestLocationPermission(this);
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestCallingPermission(this);
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

Request the permission. ```this``` can be an Activity of support Fragment.
```java
PermissionsManager.get()
                  .requestStoragePermission(this);
```

# Never Ask Again
If the user has selected "Never ask again" you can intent into the app's settings using the following:

```java
PermissionsManager.get()
                  .intentToAppSettings(activity);
```

**If the user selected "Never ask again", then they give you permissions in the app settings page, and then remove them, this method will return true. Even though at that point you can ask for permissions. I have not been able to figure out a way around this.**

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
