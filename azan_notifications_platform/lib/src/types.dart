/// The available intervals for periodically showing notifications.
enum RepeatInterval {
  /// An interval for every minute.
  everyMinute,
  every5Minute,
  every10Minute,
  every15Minute,
  every20Minute,
  every30Minute,
  every40Minute,
  every50Minute,

  /// Hourly interval.
  hourly,
  hour2,
  hour4,
  hour6,
  hour12,

  /// Daily interval.
  daily,

  /// Weekly interval.
  weekly
}

/// Details of a pending notification that has not been delivered.
class PendingNotificationRequest {
  /// Constructs an instance of [PendingNotificationRequest].
  const PendingNotificationRequest(
      this.id, this.title, this.body, this.payload);

  /// The notification's id.
  final int id;

  /// The notification's title.
  final String? title;

  /// The notification's body.
  final String? body;

  /// The notification's payload.
  final String? payload;
}
