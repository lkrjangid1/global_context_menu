package com.lkrjangid.global_context_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

/** GlobalContextMenuPlugin */
class GlobalContextMenuPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private var activity: Activity? = null
  private var pendingResult: Result? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "global_context_menu")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  private fun isProcessTextSupported(): Boolean {
    return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    // Check for Android M or higher for ACTION_PROCESS_TEXT support
    if (!isProcessTextSupported() &&
            (call.method == "getProcessedText" || call.method == "setProcessedText")) {
      result.error(
              "UNSUPPORTED_ANDROID_VERSION",
              "ACTION_PROCESS_TEXT requires Android 6.0 (API level 23) or higher",
              null
      )
      return
    }

    when (call.method) {
      "isSupported" -> {
        result.success(isProcessTextSupported())
      }
      "registerProcessTextAction" -> {
        val actionName = call.argument<String>("actionName") ?: return result.error("MISSING_PARAM", "Action name is required", null)
        val activityClass = call.argument<String>("activityClass") ?: return result.error("MISSING_PARAM", "Activity class is required", null)

        try {
          registerAction(actionName, activityClass)
          result.success(true)
        } catch (e: Exception) {
          result.error("REGISTER_FAILED", "Failed to register text action: ${e.message}", null)
        }
      }
      "getProcessedText" -> {
        val intent = activity?.intent
        if (intent?.action == Intent.ACTION_PROCESS_TEXT) {
          // Handle potential API changes or null values safely
          val text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) ?: ""
          } else {
            ""
          }
          val isReadOnly = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
          } else {
            false
          }

          // Create a HashMap with string keys and ensure all values are of compatible types
          val resultMap = HashMap<String, Any?>()
          resultMap["text"] = text?.toString() ?: ""
          resultMap["isReadOnly"] = isReadOnly

          result.success(resultMap)
        } else {
          result.error("NO_PROCESS_TEXT", "No text processing intent found", null)
        }
      }
      "setProcessedText" -> {
        val text = call.argument<String>("text") ?: return result.error("MISSING_PARAM", "Text is required", null)

        val intent = activity?.intent
        if (intent?.action == Intent.ACTION_PROCESS_TEXT) {
          val isReadOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

          if (!isReadOnly) {
            val resultIntent = Intent()
            resultIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, text)
            activity?.setResult(Activity.RESULT_OK, resultIntent)
            result.success(true)
          } else {
            result.error("READ_ONLY", "The text is read-only and cannot be modified", null)
          }
        } else {
          result.error("NO_PROCESS_TEXT", "No text processing intent found", null)
        }
      }
      "finishActivity" -> {
        activity?.finish()
        result.success(true)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  private fun registerAction(actionName: String, activityClass: String) {
    // This would normally involve modifying the AndroidManifest.xml
    // which isn't possible at runtime. This method serves as a
    // placeholder to indicate that the developer needs to manually
    // add the intent filter to their manifest.
    // The actual implementation would be done in the README instructions.
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    // Handle any activity results if needed
    return false
  }
}