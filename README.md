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

### ✅ AI-Powered Features
- Utilizes Generative AI for enhanced question generation and intelligent feedback.
- Supports voice-based answering for a realistic interview experience.

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

## 📸 Screenshots

<h3>🚀 Splash & Onboarding</h3>
<p float="left">
  <img src="https://github.com/user-attachments/assets/9e02cf1e-1805-4d11-9cef-eaac545fc434" width="45%" />
  <img src="https://github.com/user-attachments/assets/0b228a06-8c64-4185-841d-bba85cb6c4db" width="45%" />
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/7ec448d5-23cb-4a69-a545-70d73782b410" width="45%" />
  <img src="https://github.com/user-attachments/assets/2638ba53-c01f-439d-9373-f3d2aae5ad16" width="45%" />
</p>

<h3>🔐 Register & Login</h3>
<p float="left">
  <img src="https://github.com/user-attachments/assets/e2035f93-8821-4d24-b844-e41f4ff8120e" width="45%" />
  <img src="https://github.com/user-attachments/assets/21d06a04-7246-4fb8-98e0-abec3cca3c39" width="45%" />
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/9419e49d-ad74-4c7f-b352-245a5c3cb063" width="45%" />
  <img src="https://github.com/user-attachments/assets/8aca91eb-efa3-4db9-9b1d-633b172399e5" width="45%" />
</p>

<h3>🏠 Home</h3>
<p float="left">
  <img src="https://github.com/user-attachments/assets/71fa6b81-d5dd-4661-8702-6ae2d564ddbd" width="45%" />
  <img src="https://github.com/user-attachments/assets/2ec8d313-2467-4aac-b3d8-97be2b1c4a70" width="45%" />
</p>

<h3>📝 Add Details</h3>
<p float="left">
  <img src="https://github.com/user-attachments/assets/05340e42-b32e-46d0-b5fe-2d59de41647a" width="45%" />
  <img src="https://github.com/user-attachments/assets/3e0ed0ee-0020-4a8f-8509-c5c163624bfa" width="45%" />
</p>

<h3>📋 Interview Details</h3>
<p float="left">
  <img src="https://github.com/user-attachments/assets/6029af23-6f3b-49da-ab9f-ee4664de6d81" width="45%" />
  <img src="https://github.com/user-attachments/assets/bc7cb336-7895-424c-af07-f6fd6838ed90" width="45%" />
</p>

<p float="left">
  <img src="https://github.com/user-attachments/assets/c867e796-1c3a-4497-811a-80182fad2723" width="45%" />
  <img src="https://github.com/user-attachments/assets/906df958-bba8-4632-8e62-57932435738f" width="45%" />
</p>

<p float="left">
  <img src="https://github.com/user-attachments/assets/f30c8eef-febb-42f4-8090-7256ee8f282f" width="45%" />
</p>
