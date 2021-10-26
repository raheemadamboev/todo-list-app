# todo-list-app
Simple To-Do list tracking app. Latest technology used in developing

**To-Do List**

<a href="https://github.com/raheemadamboev/todo-list-app/blob/master/to-do-list.apk">Demo app</a>

Many say activity and fragment only should be responsible for updating UI and it should not make decisions and should not do business logic. So in this project all business logic is done by viewmodels. To respond events Kotlin Channel and Kotlin Flow is used. This app is robust and can survive configuration changes and process death. Fragments should be used over activities because they are light and has many features. Jetpack Navigation Component is used for dialog, fragments. As SharedPreferences deprecated, Jetpack DataStore Preferences is used.

**Screenshots:**

<img src="https://github.com/raheemadamboev/todo-list-app/blob/master/screenshots/feature_template.jpg" alt="Italian Trulli" width="869" height="416">

**Tech stack:**

- MVVM
- Dependency Injection (Hilt)
- Jetpack Navigation Component
- Kotlin Coroutines
- Room
- Jetpack DataStore Preferences
- ViewBinding
- Flow
- StateFlow
- Channel
- Livedate
- Process Death Handling
- Material Design
- Git
