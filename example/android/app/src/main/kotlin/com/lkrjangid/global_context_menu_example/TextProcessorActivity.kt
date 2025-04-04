package com.lkrjangid.global_context_menu_example

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

/**
 * Activity that handles the ACTION_PROCESS_TEXT intent.
 *
 * This activity is launched when a user selects text in any app
 * and chooses "Process Text" from the text selection toolbar.
 *
 * The actual text processing logic is handled by the Flutter code
 * using the GlobalContextMenu plugin.
 */
class TextProcessorActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The GlobalContextMenu plugin will handle the rest in the Flutter code
    }
}