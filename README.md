# Android Permissions Manager
Easily manage Android runtime permissions in API 23 `Marshmallow` and up. This library uses RXJava to skip all the painful parts of the `Activity`/`Fragment` lifecycle management.

# Features
- Reactive API
- Usage in non-Android modules
- Kotlin implementation
- Support for "Don't ask again"
- Catch missing `AndroidManifest.xml` permissions

# Install
APM is split into two modules. `permissions-manager` and `permissions-manager-android`. They can be used independently. 

`permissions-manager` provides the API to consume the library.

`permissions-manager-android` provides `PermissionsComponent` to init the Android specific implementation of `PermissionsManager`.

If your app is a monolith with a single `app` module, then you want to include both modules in your `app/build.gradle` like so:

```groovy
dependencies {
    implemetation 'net.ralphpina.permissionsmanager:permissions-manager:3.0.0'
    implemetation 'net.ralphpina.permissionsmanager:permissions-manager-android:3.0.0'
}
```

However, if your app is a multi-module project, you only need to include `permissions-manager` in the modules where you will consume the library. These modules could be Android or pure Kotlin/Java modules.

Let's say you have an app with the following `settings.gradle`:
```groovy
include ':app', 
        ':feature1', // pure Kotlin module, no com.android.library plugin 
        ':feature2'  // Android module with com.android.library plugin
```

Let's say that you use Dagger to build and inject dependencies in your `app` module. In `feature1/build.gradle` and `feature2/build.gradle` you would use the library by including the `permissions-manager` package:
```groovy
dependencies {
    implemetation 'net.ralphpina.permissionsmanager:permissions-manager-android:3.0.0'
}
```

Then, in your `app/build.gradle` module you would include both packages to inject `PermissionsManager` using `PermissionsComponent`:
```groovy
dependencies {
    implemetation 'net.ralphpina.permissionsmanager:permissions-manager:3.0.0'
    implemetation 'net.ralphpina.permissionsmanager:permissions-manager-android:3.0.0'
}
```

# Initing
Setting up the library the should be done once. If you are using Dagger you will want to provide it in one of your app scoped modules. Most likely in your `app` module. 

```kotlin
@Module
class AppModule {
    @AppScope
    @Provides
    fun providePermissionsManager(context: Context): PermissionsManager =
        PermissionsComponent.Initializer()
                            .context(context)
                            .prepare()
}
```

# Usage
1. Observe one of more permissions:
```kotlin
permissionsManager.observe(Permission.Location.Fine)
                .doOnNext {
                    println("Fine location granted: ${it[0].isGranted()}")
                }
                .subscribe()
```

For multiple permissions:
```kotlin
permissionsManager.observe(Permission.Location.Fine, Permission.Location.Coarse)
                .doOnNext {
                    println("${it[0].permission.value} granted: ${it[0].isGranted()}")
                    println("${it[1].permission.value} granted: ${it[1].isGranted()}")
                }
                .subscribe()
```

2. Request one of more permissions:
```kotlin
permissionsManager.request(Permission.Location.Fine)
                .doOnSuccess {
                    Toast.makeText(
                        dataBinding.root.context,
                        "Permission result for ${it[0].permission.value}, given: ${it[0].isGranted()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .subscribe()
```

For multiple permissions:
```kotlin
permissionsManager.request(Permission.Location.Fine, Permission.Location.Coarse)
                .doOnSuccess {
                    Toast.makeText(
                        context,
                        "Permission result for ${it[0].permission.value}, given: ${it[0].isGranted()} and ${it[1].permission.value}, given: ${it[1].isGranted()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .subscribe()
```

3. Navigate to settings:
```kotlin
permissionsManager.navigateToOsAppSettings()
```

That is the entirety of the API:
```kotlin
interface PermissionsManager {
    fun observe(vararg permissions: Permission): Observable<List<PermissionResult>>
    fun request(vararg permissions: Permission): Single<List<PermissionResult>>
    fun navigateToOsAppSettings()
}
```

`PermissionResult` provides the permission this result applies to, the result from the OS, and two flags, whether we've requested the permission before, and whether the user has selected "Don't ask again" for that or another permission in it's group.
```kotlin
data class PermissionResult(
    val permission: Permission,
    val result: Result,
    val hasAskedForPermissions: Boolean,
    val isMarkedAsDontAsk: Boolean = false
)

enum class Result {
    GRANTED, // PermissionChecker.PERMISSION_GRANTED
    DENIED, // PermissionChecker.PERMISSION_DENIED
    DENIED_APP_OP // PermissionChecker.PERMISSION_DENIED_APP_OP
}
```


# License
```
Copyright 2019 Ralph Pina.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
