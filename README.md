# AI-Powered Mock Interview Android Application

## 📌 Project Overview
The **AI-Powered Mock Interview Android Application** is designed to assist users in practicing and preparing for job interviews. Built using **Kotlin** and **Jetpack Compose**, the app integrates **Firebase Firestore** for seamless data storage and management. Users can practice answering common interview questions tailored to specific job roles, descriptions, and years of experience.

## 🚀 Features

### ✅ Personalized Interview Preparation
- **Tailored Questions:** Input your desired job role, description, and experience level to receive relevant interview questions.
- **Simulated Interviews:** Engage in mock interviews that mimic real-world scenarios.

### ✅ Real-time Feedback and Rating
- **Immediate Insights:** Receive instant feedback after each response.
- **Performance Metrics:** Understand strengths and areas for improvement through a comprehensive rating system.

### ✅ User-friendly Interface
- **Modern Design:** Leveraging **Jetpack Compose** for an intuitive and seamless user experience.
- **Responsive Layout:** Ensuring compatibility across various devices and screen sizes.

### ✅ Data Storage with Firebase Firestore
- **Secure Storage:** Safely store user data, including answers, ratings, and feedback.
- **Progress Tracking:** Monitor your improvement over time with historical data access.

## 🛠️ Tech Stack

| Technology            | Description                                                  |
|-----------------------|--------------------------------------------------------------|
| **Kotlin**            | Primary programming language for Android development.        |
| **Jetpack Compose**   | Modern UI toolkit for building native UIs in Android.        |
| **Firebase Firestore**| NoSQL cloud database for real-time data storage and retrieval.|
| **Hilt**              | Dependency injection framework for managing dependencies efficiently.      |
| **MVVM Architecture** | Ensures a clean architecture and maintainable codebase.      |


## 📂 Project Structure

```
📁 AiMockInterview
├── 📂 .idea                        # Project-specific settings and metadata
├── 📂 app
│   ├── 📂 src
│   │   ├── 📂 main
│   │   │   ├── 📂 java/com/ken/aimockinterview
│   │   │   │   ├── 📂 components   
│   │   │   │   ├── 📂 di    
│   │   │   │   ├── 📂 models   
│   │   │   │   ├── 📂 navigation        
│   │   │   │   ├── 📂 repository
│   │   │   │   ├── 📂 screens
│   │   │   │   ├── 📂 states
│   │   │   │   ├── 📂 ui.theme
│   │   │   │   ├── 📂 utils        
│   │   │   │   ├── 📂 viewmodels                    
│   │   │   │   └── MainActivity.kt # Entry point of the application
│   │   │   │   └── MyApplication.kt 
│   │   │   └── 📂 res              # Resources (layouts, drawables, values)
│   │   └── AndroidManifest.xml     # App manifest file
│   └── build.gradle.kts            # Module-level Gradle configuration
├── 📂 gradle                       # Gradle wrapper files
├── build.gradle.kts                # Project-level Gradle configuration
├── gradle.properties               # Gradle properties
├── gradlew                         # Gradle wrapper executable (Unix)
├── gradlew.bat                     # Gradle wrapper executable (Windows)
├── settings.gradle.kts             # Gradle settings
└── README.md                       # Project documentation
```

## 🎯 Roadmap
- [ ] Implement AI-driven question recommendations 🎯
- [ ] Add voice-based answering feature 🎙️
