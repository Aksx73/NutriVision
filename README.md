# 🥗 NutriVision – Food Nutrition Finder App  

**NutriVision** is an Android app built with **Kotlin** and **Jetpack Compose** that allows users to capture or select a food image and instantly get its nutritional values (calories, protein, fat, carbs, etc.) using the **Gemini Developer API**.  
The analyzed results are displayed and stored locally using **Room Database** for offline viewing.  


***Download and test latest version from release page👇***

[![Static Badge](https://img.shields.io/badge/NutriVision-APK-blue?style=for-the-badge&logo=android)](https://github.com/Aksx73/NutriVision/releases/)


## 📱 Features  

- Capture a food image from gallery  
- AI-based food recognition using Gemini API  
- Displays nutrition details (calories, protein, carbs, fat, fiber, minerals, vitamins, etc.)  
- Saves analysis results locally (Room Database)  
- View history of previous food analyses  
- Clean modern UI with Jetpack Compose and Material 3  
- Follows MVVM architecture pattern  



## 🏗️ Tech Stack  

- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose (Material Design 3)  
- **Architecture:** MVVM + Repository pattern  
- **Database:** Room Persistence Library  
- **Networking:** Gemini Developer API (Google AI) using Firebase AI Logic SDK
- **Serialization:** kotlinx.serialization  
- **Asynchronous Tasks:** Kotlin Coroutines + Flow  



## 🧠 How It Works  

1. User captures or uploads a food image.  
2. App sends the image + curated prompt to Gemini API for nutrition analysis.  
3. Gemini returns structured nutrition data in JSON format.  
4. App parses and displays the data on the **Detail Screen**.  

