import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:global_context_menu/global_context_menu.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

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
    initPlatformState();
  }

  Future<void> checkSupport() async {
    final isSupported = await GlobalContextMenu.isSupported();
    setState(() {
      _isSupported = isSupported;
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // If this activity was launched via ACTION_PROCESS_TEXT, get the text
    try {
      final Map<String, dynamic> textData =
          await GlobalContextMenu.getProcessedText();
      setState(() {
        _selectedText = textData['text'] as String? ?? 'No text selected';
        _isReadOnly = textData['isReadOnly'] as bool? ?? false;
      });
    } on PlatformException catch (e) {
      // This will happen if the activity wasn't launched via ACTION_PROCESS_TEXT
      // which is expected when launching the app normally
      print('PlatformException: ${e.message}');
    } catch (e) {
      print('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Text Processor Example'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              if (!_isSupported)
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Container(
                    padding: const EdgeInsets.all(8.0),
                    color: Colors.amber[100],
                    child: Text(
                      'This device does not support ACTION_PROCESS_TEXT. '
                      'Android 6.0 (API level 23) or higher is required.',
                      style: TextStyle(color: Colors.red[900]),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
              const Padding(
                padding: EdgeInsets.all(16.0),
                child: Text(
                  'Selected text:',
                  style: TextStyle(fontSize: 16),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Text(
                  _selectedText,
                  style: const TextStyle(
                      fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              Text(
                'Is read-only: $_isReadOnly',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 20),
              if (!_isReadOnly)
                ElevatedButton(
                  onPressed: () async {
                    final modifiedText = _selectedText.toUpperCase();
                    try {
                      await GlobalContextMenu.setProcessedText(modifiedText);
                      await GlobalContextMenu.finishActivity();
                    } on PlatformException catch (e) {
                      print("Error: ${e.message}");
                    }
                  },
                  child: const Text('Convert to UPPERCASE and Return'),
                ),
              ElevatedButton(
                onPressed: () {
                  GlobalContextMenu.finishActivity();
                },
                child: const Text('Cancel'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
