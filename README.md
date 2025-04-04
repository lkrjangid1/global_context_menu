# 📱 Global Context Menu

A Flutter plugin that enables adding custom actions to Android's text selection toolbar using the `ACTION_PROCESS_TEXT` intent.

[![Pub](https://img.shields.io/pub/v/global_context_menu.svg)](https://pub.dev/packages/global_context_menu)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


## ✨ Features

- 🔠 Add custom actions to Android's text selection toolbar
- 📝 Process selected text from any app
- 🔄 Return modified text to replace the selection
- 🔒 Support for both read-only and editable text selections

## 📋 Requirements

- 📱 Android 6.0 (API level 23) or higher
- 💙 Flutter 2.5.0 or higher

## 📦 Installation

Add the plugin to your `pubspec.yaml`:

```yaml
dependencies:
  global_context_menu: any
```

Run:
```bash
flutter pub get
```

## 🛠️ Setup

### 1️⃣ Create a Flutter Activity to handle the text processing

First, create a Flutter activity in your app that will be launched when the user selects your text processing action.

Example activity implementation in Kotlin:

```kotlin
package com.example.yourapp

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

class YourProcessTextActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The GlobalContextMenu plugin handles the rest in your Flutter code
    }
}
```

### 2️⃣ Register the activity in your AndroidManifest.xml

Add the following to your app's `android/app/src/main/AndroidManifest.xml` file inside the `<application>` tag:

```xml
<activity
    android:name=".YourProcessTextActivity"
    android:label="Your Action Name"
    android:theme="@style/Theme.AppCompat.Light">
    <intent-filter>
        <action android:name="android.intent.action.PROCESS_TEXT" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
</activity>
```

> ⚠️ **Important notes:**
> - The `android:label` attribute will be shown as the action name in the text selection toolbar, so keep it short and descriptive.
> - You can add multiple activities with different labels for different text processing actions.
> - As of Android 6.0.1, avoid adding `android:exported="false"` as it can cause issues.

## 💻 Usage

### Check if the device supports the feature

```dart
bool isSupported = await GlobalContextMenu.isSupported();
if (!isSupported) {
  // Show fallback UI or message
}
```

### Get the selected text

```dart
try {
  final Map<String, dynamic> textData = await GlobalContextMenu.getProcessedText();
  final String selectedText = textData['text'] as String? ?? '';
  final bool isReadOnly = textData['isReadOnly'] as bool? ?? false;
  
  // Process the text as needed
  print('Selected text: $selectedText');
  print('Is read-only: $isReadOnly');
} catch (e) {
  print('Error getting text: $e');
}
```

### Return modified text

```dart
// Only works if text is not read-only
try {
  String modifiedText = "Modified: $originalText";
  await GlobalContextMenu.setProcessedText(modifiedText);
  await GlobalContextMenu.finishActivity();
} catch (e) {
  print('Error setting text: $e');
}
```

### Close the activity

```dart
// Close without modifying text
GlobalContextMenu.finishActivity();
```

## 📝 Complete Example

Here's a simple example of a text processor that converts selected text to uppercase:

```dart
import 'package:flutter/material.dart';
import 'package:global_context_menu/global_context_menu.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _selectedText = 'No text selected yet';
  bool _isReadOnly = false;
  bool _isSupported = false;

  @override
  void initState() {
    super.initState();
    checkSupport();
    getSelectedText();
  }
  
  Future<void> checkSupport() async {
    final isSupported = await GlobalContextMenu.isSupported();
    setState(() {
      _isSupported = isSupported;
    });
  }

  Future<void> getSelectedText() async {
    try {
      final textData = await GlobalContextMenu.getProcessedText();
      setState(() {
        _selectedText = textData['text'] as String? ?? 'No text selected';
        _isReadOnly = textData['isReadOnly'] as bool? ?? false;
      });
    } catch (e) {
      print('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Text Processor'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              if (!_isSupported)
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Container(
                    padding: EdgeInsets.all(8.0),
                    color: Colors.amber[100],
                    child: Text(
                      'This device does not support text processing. '
                      'Android 6.0+ required.',
                      style: TextStyle(color: Colors.red[900]),
                    ),
                  ),
                ),
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Text('Selected text: $_selectedText'),
              ),
              if (!_isReadOnly)
                ElevatedButton(
                  onPressed: () async {
                    final upperText = _selectedText.toUpperCase();
                    try {
                      await GlobalContextMenu.setProcessedText(upperText);
                      await GlobalContextMenu.finishActivity();
                    } catch (e) {
                      print("Error: $e");
                    }
                  },
                  child: Text('Convert to UPPERCASE'),
                ),
              ElevatedButton(
                onPressed: () {
                  GlobalContextMenu.finishActivity();
                },
                child: Text('Cancel'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
```

## 🔍 How It Works

1. 📲 The plugin registers activities with the `ACTION_PROCESS_TEXT` intent filter in AndroidManifest.xml
2. 👆 When a user selects text in any app and chooses your action, your activity is launched
3. 📊 The plugin retrieves the selected text and provides it to your Flutter app
4. 🔄 Your app processes the text and optionally returns modified text
5. 🏁 The activity finishes and returns to the calling app

## ⚠️ Limitations

- 📱 Only works on Android 6.0 (API level 23) or higher
- 🌐 Your action will appear in every text selection context in all apps
- 🔍 You cannot filter when your action appears based on text content
- 🚫 Services cannot be directly triggered by ACTION_PROCESS_TEXT

## 🔮 Use Cases

- 📚 Dictionary/definition lookup
- 🌐 Translation services
- 🔍 Search in your app
- 📊 Analysis or statistics
- 🔤 Text formatting/transformation

## 📱 Example Apps Using Similar Functionality

- 📖 Wikipedia (Search Wikipedia)
- 🌐 Google Translate (Translate)
- 🔍 Google Search (Web Search)

## 🚀 Planned Features

The following features are planned for future releases:

- 🎨 **Rich Text Support** - Handle formatted text (HTML, markdown) with styling preserved
- 🔄 **Batch Processing** - Process multiple text selections at once
- 🌐 **Enhanced Translation** - Built-in translation capabilities with multiple language support
- 📊 **Text Analytics** - Word count, readability scores, and other text metrics
- ⚙️ **Advanced Configuration** - More options to control when and how your action appears
- 🔒 **Enhanced Security** - Additional security features for sensitive text processing
- 📦 **Pre-built Processors** - Common text processors ready to use out of the box
- 📄 **Multi-platform Support** - Exploring options for similar functionality on other platforms

## 🐛 Troubleshooting

### Common Issues

- **Action doesn't appear**: Make sure you're running Android 6.0+ and have correctly configured your AndroidManifest.xml
- **SecurityException**: Avoid using `android:exported="false"` in your activity declaration
- **Activity not launching**: Ensure your activity class name matches exactly what's in the manifest

## 👨‍💻 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Contact

If you have any questions or feedback, please open an issue on the GitHub repository.

---

Made with ❤️ by [Lokesh Jangid]