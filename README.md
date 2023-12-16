# AndroidGen
The Android code generator for your assets, fonts, colors, … — Get rid of all String-based APIs.

Inspired by [SwiftGen](https://github.com/SwiftGen/SwiftGen).

Inspired by [FlutterGen](https://github.com/FlutterGen/flutter_gen).

### Problem 

Using asset path string directly is not safe.


❌ Bad
What would happen if you made a typo?

```kotlin
Glide.with(this)
            .load(Uri.parse("file:///android_asset/images/flower.png"))
            .into(imageView)


// The following assertion was thrown resolving an image codec:
// Unable to load asset: assets/images/flower.png

```

⭕️ Good
We want to use it safely.

```kotlin 
Glide.with(this)
            .load(Uri.parse(BuildAssetManager.IMAGES_FLOWER))
            .into(imageView)
```

### Setup 

- Download file:  BuildAssets.jar
- Put it into root Android Project 
- Terminal > Run 
```
java -jar BuildAssets.jar assets_path package_path 
```

For example:  
```
java -jar BuildAssets.jar app/src/main/assets app/src/main/java/ttpho/io/chat/gen
```

File `BuildAssetManager.kt` is created on package ttpho.io.chat.gen


### Contributing

- Update `BuildAssets.java` 
- Build jar 

```
javac BuildAssets.java
jar cvfe BuildAssets.jar BuildAssets BuildAssets.class
```

### Why Java 
Write once, run anywhere
