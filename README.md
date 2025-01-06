# Android TV News App


Welcome to the Android TV News App! This application is designed to fetch and display the latest news headlines in a user-friendly manner, optimized for TV screens. The app is built using modern Android development tools and libraries, including Jetpack Compose, Dagger Hilt, Coil, and Retrofit.


---


## Features


- **News API Integration**: Fetch real-time news headlines from a public API (e.g., NewsAPI).

- **Dynamic Content**: Support for changing the country and category of news.

- **User-Friendly Display**: A clean and readable UI tailored for TV screens using Jetpack Compose.

- **Refresh Functionality**: Refresh news headlines with a long press on DPAD-Down, with a loading indicator.

- **Modern Android Practices**: Dependency injection using Dagger Hilt, image loading with Coil, and efficient networking with Retrofit.


---


## Tech Stack


### Libraries and Tools Used


- **Jetpack Compose**: For building declarative and responsive UI components.

- **Dagger Hilt**: For dependency injection to manage application-level dependencies.

- **Retrofit**: For making network requests to fetch news data from the API.

- **Coil**: For efficient image loading and caching.

- **Coroutines and Flow**: For managing asynchronous tasks and reactive data streams.


---

## Video


https://github.com/user-attachments/assets/e1b08ad6-d24d-43cb-9817-79d9b60ed638



---


## Getting Started


### Prerequisites


1. Android Studio Arctic Fox or newer.

2. API key for the chosen news API (e.g., NewsAPI).

3. A physical or emulated Android TV device for testing.


### Installation


1. Clone this repository:

```bash

git clone https://github.com/ayush19sinha/newsapp.git

```

2. Open the project in Android Studio.

3. Add your API key in the `local.properties` file:

```

NEWS_API_KEY="your_api_key_here"

```

4. Build and run the app on an Android TV device or emulator.


---


## Usage


1. **Browse News**: Navigate through the displayed news headlines using the DPAD on your remote.

2. **Change Country/Category**: Access the settings menu to change the country or category of news.

3. **Refresh Headlines**: Long press DPAD-Down to refresh the news feed. A loading indicator will appear during the refresh.


---


## Architecture


The app follows the **MVVM (Model-View-ViewModel)** architecture pattern for better separation of concerns and testability.


### Key Components:


- **UI Layer**: Built with Jetpack Compose to provide a seamless and adaptive experience on TV screens.

- **Data Layer**: Uses Retrofit for API requests and Coil for image loading.

- **Dependency Injection**: Managed by Dagger Hilt for clean and testable code.
