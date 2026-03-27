# NAS Reels (Android)

TikTok-liknande Android-app (Kotlin) som spelar vertikala videor från en NAS på lokalt nätverk.

## Funktioner

- Vertikal scroll via `RecyclerView` + `PagerSnapHelper`
- Autoplay på synlig video
- Loop av varje video
- Inga spelkontroller i UI
- Inställningar för:
  - NAS IP-adress
  - Antal videor
- URL-format genereras automatiskt:
  - `http://<ip>/videos/1.mp4`
  - `http://<ip>/videos/2.mp4`
  - osv.

## Krav

- Android Studio (Ladybug eller nyare rekommenderas)
- Android SDK 34
- En NAS/server tillgänglig på samma WiFi/LAN

## Köra appen

1. Öppna projektet i Android Studio.
2. Låt Gradle synka beroenden.
3. Kör appen på fysisk Android-enhet (samma nätverk som NAS).
4. Öppna inställningar i appen och ange rätt IP + antal videor.

## Bygga APK

I Android Studio:

- `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`

Eller via terminal (om Gradle wrapper finns):

```bash
./gradlew assembleDebug
```

APK hamnar normalt i:

`app/build/outputs/apk/debug/app-debug.apk`
