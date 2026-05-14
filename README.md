# HireTimSF Android Portfolio

HireTimSF is a Kotlin Android portfolio app for Tim Baz, a Design Engineer based in the San Francisco Bay Area. The app presents profile details, portfolio projects, experience, contact links, and app settings in a modern Jetpack Compose interface.

[Visit hiretimsf.com](https://hiretimsf.com)

## Preview

[![Portfolio App preview](https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio2.0%2Fyoutube.png?alt=media&token=ba66ef7c-2e8e-4bb8-975b-061a764fc0b9)](https://youtu.be/YjVJyqcv5I8 "Portfolio App 2.0 - Click to watch")

## Screenshots

<img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-01.jpg?alt=media&token=18d7c84c-c2c1-43b1-8b61-fb975a86ed01" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-02.jpg?alt=media&token=68fec153-29be-4fd4-b974-bd9e13db4e58" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-03.jpg?alt=media&token=cfb5bae0-e8b8-4f0e-8b41-e7b375592750" width="250"/>
<img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-04.jpg?alt=media&token=a85353fa-9e66-4fea-ac43-acff3bb1d501" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-05.jpg?alt=media&token=2253acd9-849b-4e8b-821b-a2aef4bd08cf" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-06.jpg?alt=media&token=3a66bf8e-c2f2-4190-a427-047b94c70019" width="250"/>
<img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-08.jpg?alt=media&token=839efa39-3d0e-493e-80e8-299195a16d2d" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-09.jpg?alt=media&token=499a8499-e705-4e96-97da-daf41f6bb3ff" width="250"/> <img src="https://firebasestorage.googleapis.com/v0/b/portfolio-app-147b5.appspot.com/o/screenshots%2Fportfolio1.2%2Fs-10.jpg?alt=media&token=6be3e292-d77e-46ef-971d-92bdc6009dd9" width="250"/>

## Features

- Jetpack Compose UI for profile, portfolio, experience, settings, drawer, top bar, and bottom navigation.
- Compose Navigation with a single-activity app shell.
- Edge-to-edge layout support with Compose app chrome.
- Animated vector icons in navigation surfaces.
- Profile contact sheet with email and social links.
- Local portfolio data store with Kotlin serialization models.
- Hilt-powered ViewModels and dependency injection.

## Tech Stack

- Kotlin 2.3.21
- Jetpack Compose with Compose BOM 2025.12.00
- Navigation Compose 2.9.8
- Paging 3.5.0
- Hilt 2.59.2
- Retrofit 3.0.0 and OkHttp 5.3.2
- Kotlinx Serialization 1.9.0
- Firebase Analytics and Performance Monitoring
- Android Gradle Plugin 9.2.1

## Requirements

- JDK 17
- Android Studio with Android Gradle Plugin 9.2.1 support
- Gradle 9.5.0 wrapper
- Android SDK Platform 37
- Android SDK Build Tools for API 37
- Minimum supported Android version: API 23

## Build

```bash
./gradlew :app:assembleDebug
```

The application id and namespace are both:

```text
hiretimsf.com.app
```

## Project Structure

```text
app/src/main/java/hiretimsf/com/app/
|-- navigation/              # Compose route definitions
|-- repository/              # Local data, models, and repository layer
|-- screens/                 # Feature screens and shared app chrome
`-- utils/                   # Constants, extensions, DI, theme, and UI state
```

## License

```text
Copyright 2019 Tim Baz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
