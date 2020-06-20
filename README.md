[![Build Status](https://app.bitrise.io/app/70be2acc146e73a1/status.svg?token=cq4Pey7mOguQjFXR5J3JyA)](https://app.bitrise.io/app/70be2acc146e73a1) [![codecov](https://codecov.io/gh/kevalpatel2106/Fx-Rates-App/branch/master/graph/badge.svg)](https://codecov.io/gh/kevalpatel2106/Fx-Rates-App) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/c89fb64883a64b9fad8ba25b5a9839de)](https://www.codacy.com/manual/kevalpatel2106/Fx-Rates-App?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kevalpatel2106/Fx-Rates-App&amp;utm_campaign=Badge_Grade) [![API](https://img.shields.io/badge/API-21%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=21)

<img src="https://raw.githubusercontent.com/kevalpatel2106/Currency-Converter-App/master/.github/ic_launcher-playstore.png" width=200/>

# Currency Converter App

#### Demo application for converting one currencies into other.

This app will list 20+ different currencies and their rates. You can select any currency adn enter the amount of that currency to convert and app will show converted amount for all 20+ currencies simultaneously. 
This app polls the server to get the latest currency rates every 1 second. If any error occurs and API call fails (due to server error or no network connection), the polling mechanism tries to recover from error automatically. 

## Architecture Highlights:
- Follows [MVVM architecture pattern](https://raw.githubusercontent.com/kevalpatel2106/Currency-Converter-App/master/.github/arch.png).
- Uses android architecture components such as livedata and ViewModel. 
- Uses dagger2 for dependency injection. This is to provide clear separation between different architecture layers. Helps to write testable code.
- Single activity application.
- Uses retrofit as networking client.


## How to run tests?
- Make sure you connect physical device or run an emulator before running the UI tests.
- To run tests, run following command:
```bash
./gradlew cAT
```

## Acknowledgement:
- This is a sample app and it is not guaranteed that it will show latest rates for each currency. Use it as a demo purposes only. 

## Screenshots 📸:

| Portrait | Landscape | 
|---|---|
| ![](.github/portrait.jpg) | ![](./.github/landscape.jpg) |

## Videos 🎥:

| Orientation Handling | Error Handling| 
|---|---|
| ![](.github/orientation-handling.gif) | ![](./.github/error-handling.gif) |

## What to try this out?
- Want the APK from any specific commit? Download it from the [CI artifacts](https://app.bitrise.io/app/70be2acc146e73a1). 

## License:
Copyright 2020 Keval Patel

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

<div align="center">
<img src="https://cloud.githubusercontent.com/assets/370176/26526332/03bb8ac2-432c-11e7-89aa-da3cd1c0e9cb.png">
</div>
