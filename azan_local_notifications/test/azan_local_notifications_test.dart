import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:azan_local_notifications/azan_local_notifications.dart';

void main() {
  const MethodChannel channel = MethodChannel('azan_local_notifications');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  
}
