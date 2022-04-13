# GymApp
An android app to track gym stats. I.E. Weights, Reps, Sets and Run Tracking

## Usage
Requires a Google Maps Api key that can be created for free here: https://developers.google.com/maps/documentation/embed/get-api-key

Place the Api key in local.properties:
```
MAPS_API_KEY={Your Api Key} 
```

Or replace the interpolated value in the AndroidManifest:
```
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="{Your Api Key}"/>
```

## Dependancies 
- Kotlin 1.6.20
- Gradle 7.1.3
- Android SDK version 26 to 31
