package com.lkrjangid.global_context_menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

/**
 * Sample activity that can be used as a template for implementing
 * ACTION_PROCESS_TEXT handling. You can use this as a reference for
 * creating your own activity in your app.
 *
 * Remember to add this to your AndroidManifest.xml with the appropriate
 * intent filter as shown in the README.
 */
class ProcessTextActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the intent
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // Handle the intent (for singleTop launch mode)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            // Get the selected text
            val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            val isReadOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

            // Process the text according to your app's functionality
            // ...

            // For demonstration, we'll just add a prefix to the text
            if (!isReadOnly) {
                val modifiedText = "Processed: $text"

                // Return the modified text to the caller
                val resultIntent = Intent()
                resultIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, modifiedText)
                setResult(Activity.RESULT_OK, resultIntent)
            }

            // Finish the activity to return to the caller
            finish()
        }
    }
}