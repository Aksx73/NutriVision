# 🥗 NutriVision – Food Nutrition Finder App  

**NutriVision** is an Android app built with **Kotlin** and **Jetpack Compose** that allows users to capture or select a food image and instantly get its nutritional values (calories, protein, fat, carbs, etc.) using the **Gemini Developer API**.  
The analyzed results are displayed and stored locally using **Room Database** for offline viewing.  


***Download and test latest version from release page👇***

[![Static Badge](https://img.shields.io/badge/NutriVision-APK-blue?style=for-the-badge&logo=android)](https://github.com/Aksx73/NutriVision/releases/)

<div class="markdown-heading" dir="auto"><h2 tabindex="-1" class="heading-element" dir="auto">Screenshots</h2><a id="user-content-screenshots" class="anchor" aria-label="Permalink: Screenshots" href="#screenshots"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z"></path></svg></a></div>
<table>
<thead>
<tr>
<th><a target="_blank" rel="noopener noreferrer" href="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093222_NutriVision.png"><img src="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093222_NutriVision.png?raw=true" alt="sound meter" style="max-width: 100%;"></a></th>
<th><a target="_blank" rel="noopener noreferrer" href="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093719.png"><img src="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093719.png?raw=true" alt="sound meter" style="max-width: 100%;"></a></th>
</tr>
</thead>
<thead>
<tr>
<th><a target="_blank" rel="noopener noreferrer" href="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093729.png"><img src="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093729.png?raw=true" alt="sound meter" style="max-width: 100%;"></a></th>
<th><a target="_blank" rel="noopener noreferrer" href="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093731.png"><img src="https://github.com/Aksx73/NutriVision/blob/master/screenshots/Screenshot_20251130-093731.png?raw=true" alt="sound meter" style="max-width: 100%;"></a></th>
</tr>
</thead>
</table>

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

