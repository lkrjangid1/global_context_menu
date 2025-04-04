import 'dart:async';

import 'package:flutter/services.dart';

/// A Flutter plugin for adding custom actions to Android's text selection toolbar.
class GlobalContextMenu {
  static const MethodChannel _channel = MethodChannel('global_context_menu');

  /// Checks if the device supports ACTION_PROCESS_TEXT (Android 6.0+)
  static Future<bool> isSupported() async {
    try {
      final bool result = await _channel.invokeMethod('isSupported');
      return result;
    } catch (e) {
      return false;
    }
  }

  /// Registers a text processing action.
  ///
  /// Note: This is a helper method for documentation purposes. The actual
  /// registration of an ACTION_PROCESS_TEXT intent filter must be done
  /// in the AndroidManifest.xml file. See the README for more details.
  ///
  /// [actionName] is the name that will appear in the text selection toolbar.
  /// [activityClass] is the fully qualified name of the Activity to handle the action.
  static Future<bool> registerProcessTextAction({
    required String actionName,
    required String activityClass,
  }) async {
    final bool result =
        await _channel.invokeMethod('registerProcessTextAction', {
      'actionName': actionName,
      'activityClass': activityClass,
    });
    return result;
  }

  /// Gets the processed text from the intent.
  ///
  /// Returns a map containing:
  /// - 'text': The selected text (String)
  /// - 'isReadOnly': Whether the text is read-only (bool)
  static Future<Map<String, dynamic>> getProcessedText() async {
    final Map<dynamic, dynamic> rawResult =
        await _channel.invokeMethod('getProcessedText');
    // Convert to Map<String, dynamic>
    final Map<String, dynamic> result = Map<String, dynamic>.from(rawResult);
    return result;
  }

  /// Sets the processed text to replace the selected text.
  ///
  /// [text] is the text to replace the selection with.
  ///
  /// Returns true if successful, or throws an exception if the text is read-only
  /// or there's no active process text intent.
  static Future<bool> setProcessedText(String text) async {
    final bool result = await _channel.invokeMethod('setProcessedText', {
      'text': text,
    });
    return result;
  }

  /// Finishes the activity and returns to the calling app.
  ///
  /// Call this after setting the processed text or when the user
  /// cancels the operation.
  static Future<bool> finishActivity() async {
    final bool result = await _channel.invokeMethod('finishActivity');
    return result;
  }
}
