# Changelog

## [0.0.1] - 2025-04-04

### Added

- Initial release of the plugin
- Support for adding custom actions to Android's text selection toolbar using ACTION_PROCESS_TEXT
- Methods to retrieve selected text from other apps
- Support for returning modified text to replace selections
- Support for both read-only and editable text
- Added isSupported() method to check if the device supports ACTION_PROCESS_TEXT (Android 6.0+)
- Sample implementation with a text processor activity
- Comprehensive documentation and examples

### Security

- Proper handling of Android permission requirements
- Safe type conversions between Kotlin and Dart
