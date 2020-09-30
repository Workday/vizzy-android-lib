# vizzy-android-lib
An Android library to simplify vizzy integration

Vizzy is a powerful Ruby on Rails web server that facilitates Visual Automation, a continuous integration testing strategy that aims to prevent visual regressions, also open sourced here: https://github.com/Workday/vizzy

## Installation
The library is available on jcenter, so just add it to your dependencies on the build.gradle of the project

```groovy
dependencies {
    [...]
    androidTestImplementation 'com.workday.vizzy:vizzy_android:1.0'
}
```

## Configuration

The library uses a custom Test Runner that requires some parameters, you can configure it all on the build.gradle:

```groovy
android {
    [...]
    defaultConfig {
        [...]
        testInstrumentationRunner "com.workday.vizzy.VizzyTestRunner"

        testInstrumentationRunnerArguments vizzyUserEmail: "<vizzy.user.mail#example.com>"
        testInstrumentationRunnerArguments vizzyUserToken: "<use.token>"
        testInstrumentationRunnerArguments vizzyBaseUrl: "<vizzy.url>"
        testInstrumentationRunnerArguments vizzyProjectId: "<project.id>"
        
        // Optional parameters, pullRequestNumber and commitSha
//        testInstrumentationRunnerArguments pullRequestNumber: System.getenv("PULL_REQUEST") as String
    }
```
If `pullRequestNumber` is passed, then a proper build is created:

* New images got added to base images

If `pullRequestNumber` is not passed, then a developer build is created:

* New images are not added to Vizzy
* Visual diffs are performed, but base images can not be replaced
* Build will be deleted from the server after 24 hours

While on a CICD pipeline, the `PULL_REQUEST` value should be passed by the system. When running on local dev machines, it can be ignored and a dev build will be generated.

### Notes

Replacing the instrumentation test runner will add the following:
* Before starting the Android tests, all images will be deleted
* After the Android tests are run, the images will be submmited to Vizzy using the configuration parameters

projectId is just a number you get after creating it on the Vizzy server.

`commitSha` is only used to link Vizzy with git, it is not mandatory and you can do PR builds without it.

## Usage

The library adds the  extension function `addVizzyTest()` to `ActivityTestRule`. This method creates a screenshot and stores it on a folder structure matching the package name, then the class name and then the method name, including sequential numbers as the name. The name of the screenshot can be overrited when passing name as a parameter.


```Kotlin
package com.workday.vizzytestapp

import [...]

@RunWith(AndroidJUnit4::class)
class MainActivityVizzyTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java) as ActivityTestRule<Activity>

    @Test fun sampleVisualTest() {
        activityRule.addVizzyTest()
    }

    @Test fun anotherVisualTest() {
        activityRule.addVizzyTest()
        activityRule.addVizzyTest("WithName")
    }
}
```

__To be reviewed:__ The previous example creates the following file tree on Vizzy

```
com
    workday
        vizzytestapp
            MainActivity_WithName.PNG            
            MainActivity_anotherVisualTest.PNG
            MainActivity_sampleVisualTest.PNG            
```
