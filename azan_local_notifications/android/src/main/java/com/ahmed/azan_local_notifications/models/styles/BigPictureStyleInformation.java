package com.ahmed.azan_local_notifications.models.styles;

import androidx.annotation.Keep;

import com.ahmed.azan_local_notifications.models.BitmapSource;

@Keep
public class BigPictureStyleInformation extends DefaultStyleInformation {
  public String contentTitle;
  public Boolean htmlFormatContentTitle;
  public String summaryText;
  public Boolean htmlFormatSummaryText;
  public Object largeIcon;
  public BitmapSource largeIconBitmapSource;
  public Object bigPicture;
  public BitmapSource bigPictureBitmapSource;
  public Boolean hideExpandedLargeIcon;

  public BigPictureStyleInformation(
      Boolean htmlFormatTitle,
      Boolean htmlFormatBody,
      String contentTitle,
      Boolean htmlFormatContentTitle,
      String summaryText,
      Boolean htmlFormatSummaryText,
      Object largeIcon,
      BitmapSource largeIconBitmapSource,
      Object bigPicture,
      BitmapSource bigPictureBitmapSource,
      Boolean hideExpandedLargeIcon) {
    super(htmlFormatTitle, htmlFormatBody);
    this.contentTitle = contentTitle;
    this.htmlFormatContentTitle = htmlFormatContentTitle;
    this.summaryText = summaryText;
    this.htmlFormatSummaryText = htmlFormatSummaryText;
    this.largeIcon = largeIcon;
    this.largeIconBitmapSource = largeIconBitmapSource;
    this.bigPicture = bigPicture;
    this.bigPictureBitmapSource = bigPictureBitmapSource;
    this.hideExpandedLargeIcon = hideExpandedLargeIcon;
  }
}
