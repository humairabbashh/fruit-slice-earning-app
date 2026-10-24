# Fruit Slice Earning — Android app

A full-screen WebView shell for the Fruit Slice Earning web game, with
Google AdMob ads: a banner always visible at the bottom, and a full-screen
interstitial ad every 2 minutes.

- App name: Fruit Slice Earning
- Package: `com.earningdecode.fruitslice`
- Game URL loaded in the app: https://fruitsliceearningapp.netlify.app

## How to build it (phone only, no PC needed)

1. Create a new **PUBLIC** GitHub repository (public, because free GitHub
   Actions minutes are unlimited on public repos).
2. Upload ALL files from this project into the repo, keeping the folder
   structure exactly as it is (especially the `app/` folder and the
   `.github/` folder).
3. Push to the `main` branch. The build starts automatically. You can also
   start it by hand: repo page > Actions tab > "Build Android" >
   "Run workflow".
4. When the build finishes (green check mark), open that run, scroll down
   to **Artifacts**, and download:
   - `fruit-slice-apk` — the APK file, install it directly on a phone.
   - `fruit-slice-aab` — the AAB file, needed later for the Play Store.
5. Install the APK on your phone and test it.

## Where to put your real AdMob IDs later

Right now the app uses Google's official TEST IDs, so it shows test ads.
Before publishing with real ads, replace these 3 test IDs with your real
IDs from your AdMob account (AdMob console > Apps > your app):

1. **App ID** — in `app/src/main/AndroidManifest.xml`, the `APPLICATION_ID`
   meta-data value. Test value now:
   `ca-app-pub-3940256099942544~3347511713`
2. **Banner ad unit ID** — in `app/src/main/res/layout/activity_main.xml`,
   the `ads:adUnitId` on the AdView. Test value now:
   `ca-app-pub-3940256099942544/6300978111`
3. **Interstitial ad unit ID** — in
   `app/src/main/java/com/earningdecode/fruitslice/MainActivity.kt`,
   the `INTERSTITIAL_AD_UNIT_ID` constant. Test value now:
   `ca-app-pub-3940256099942544/1033173712`

After changing any ID, push again so Actions builds a new APK/AAB.

## Test ads vs real ads

- First you will see TEST ads (they say "Test Ad"). That is normal and
  proves the setup works.
- Real ads only start after your AdMob account is verified and Google
  reviews the app. That review can take from about a day to a couple of
  weeks. Full ad serving also needs the app listed in a supported store
  and linked in AdMob.
- Never click your own ads. AdMob bans accounts for self-clicking.

## Versions used

- Android Gradle Plugin 8.9.1, Gradle 8.11.1, Kotlin 2.0.21
- compileSdk 35, targetSdk 35, minSdk 26, Java 17
- Google Mobile Ads SDK 25.5.0
- AGP 8.5.1+ builds 16 KB page-size compliant APKs, so the app installs
  on modern Android phones (including Android 16).

## Troubleshooting

- Build fails in Actions: open the failed run, read the red error log.
  The most common cause is a typo when editing an ID — check the 3 spots
  above.
- If Gradle cannot resolve the ads SDK version 25.5.0, change it to
  23.6.0 in `app/build.gradle` and push again.
- The workflow generates the Gradle wrapper itself, so a missing
  `gradle-wrapper.jar` in this zip is normal and harmless.
