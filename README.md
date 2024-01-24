# Firebase Android App Demo

This is a simple Android app demo showcasing integration with Firebase for Google login, Crashlytics for crash reporting, and push notifications.

## Features

- **Google Login:** Allow users to sign in using their Google account.
- **Crash Reporting:** Utilize Crashlytics to monitor and report app crashes in real-time.
- **Push Notifications:** Send and receive push notifications using Firebase Cloud Messaging (FCM).

## Prerequisites

- Android Studio installed on your development machine.
- A Google Developer Console project set up for Firebase.
- Firebase project configuration file (`google-services.json`) added to the app module.

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/Nikul-Nakrani/FirebaseLoginAuthentication.git


1. Open the project in Android Studio.
2. Add your Google-services.json file to the app/ directory.
3. Build and run the app on your Android device or emulator.

## Firebase Configuration
**Google Login**
1. Enable Google Sign-In in the Firebase console.
2. Add your Android app to the Firebase project settings.
3. Download the google-services.json file and add it to the app/ directory.

## Crashlytics
1. Enable Crashlytics in the Firebase console.
2. Add the Crashlytics dependency in your app/build.gradle:
```
implementation 'com.google.firebase:firebase-crashlytics:17.7.1'
```


## Push Notifications
1. Enable Cloud Messaging in the Firebase console.
2. Obtain your FCM Server Key and add it to your app's manifest:


```bash
<application>
    <!-- ... other configurations ... -->
    <meta-data
        android:name="com.google.firebase.messaging.default_notification_channel_id"
        android:value="@string/default_notification_channel_id" />
    <service
        android:name=".MyFirebaseMessagingService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>
</application>

```


## Usage
Explore the app to see the integration of Google login, Crashlytics, and push notifications in action.

## Contributing
We welcome contributions! If you'd like to contribute to the project, please follow the contributing guidelines.

## License
This project is licensed under the MIT License.


## Acknowledgments
Thanks to Firebase for providing excellent tools for app development.


Happy coding!


