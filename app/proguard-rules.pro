# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/rodrigomacedo/Documents/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dontoptimize
-keepattributes *Annotation*
-keep class org.jetbrains.** { *; }
-keepclassmembers class org.jetbrains.** { *; }
-keepattributes EnclosingMethod
-keepattributes Signature
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

-keep class com.google.gson.** { *; }
-keep class org.apache.** { *; }
-keep class com.google.android.** { *; }
-keep class com.google.android.maps.** { *; }
-dontwarn com.google.android.maps.**

-keep class android.view.accessibility.** { *; }
-keep class com.google.android.gms.** { *; }
 -dontwarn com.google.android.gms.**


-keepattributes InnerClasses
 -keep class br.com.libertyseguros.mobile.beans.ChangeEmailSendBeans**
 -keep class br.com.libertyseguros.mobile.beans.ClaimBeans**
 -keep class br.com.libertyseguros.mobile.beans.ClubBeans**
 -keep class br.com.libertyseguros.mobile.beans.DetailPolicyBeans**
 -keep class br.com.libertyseguros.mobile.beans.ExtendsBeans**
 -keep class br.com.libertyseguros.mobile.beans.HomeBeans**
 -keep class br.com.libertyseguros.mobile.beans.InstallmentsBeans**
 -keep class br.com.libertyseguros.mobile.beans.InsuranceStatusBeans**
 -keep class br.com.libertyseguros.mobile.beans.ListVehicleAccidentBeans**
 -keep class br.com.libertyseguros.mobile.beans.LoginBeans**
 -keep class br.com.libertyseguros.mobile.beans.MessageBeans**
 -keep class br.com.libertyseguros.mobile.beans.MessageTypeTwoBeans**
 -keep class br.com.libertyseguros.mobile.beans.NavDrawerItem**
 -keep class br.com.libertyseguros.mobile.beans.NotificationBeans**
 -keep class br.com.libertyseguros.mobile.beans.NumberWarningVehicleAccidentBeans**
 -keep class br.com.libertyseguros.mobile.beans.ParcelsBeans**
 -keep class br.com.libertyseguros.mobile.beans.PaymentBeans**
 -keep class br.com.libertyseguros.mobile.beans.PolicyBeans**
 -keep class br.com.libertyseguros.mobile.beans.QuestionBeans**
 -keep class br.com.libertyseguros.mobile.beans.QuestionIDBeans**
 -keep class br.com.libertyseguros.mobile.beans.QuestionSendBeans**
 -keep class br.com.libertyseguros.mobile.beans.Salesman**
 -keep class br.com.libertyseguros.mobile.beans.SalesmanBeans**
 -keep class br.com.libertyseguros.mobile.beans.TicketBeans**
 -keep class br.com.libertyseguros.mobile.beans.UploadFileBeans**
 -keep class br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans**
 -keep class br.com.libertyseguros.mobile.beans.VehicleAccidentStatusBeans**
 -keep class br.com.libertyseguros.mobile.beans.VehicleAccidentUploadBeans**
 -keep class br.com.libertyseguros.mobile.beans.WorkshopBeans**
 -keep class br.com.libertyseguros.mobile.beans.FacebookBeans**
 -keep class br.com.libertyseguros.mobile.beans.PolicyBeansV2**
 -keep class br.com.libertyseguros.mobile.beans.HomeBeansV2**
 -keep class br.com.libertyseguros.mobile.beans.HomeBeansV1**
 -keep class br.com.libertyseguros.mobile.beans.HomeBeans**
 -keep class br.com.libertyseguros.mobile.beans.BarCodeBeans**
 -keep class br.com.libertyseguros.mobile.beans.DocumentBase64Beans**
 -keep class br.com.libertyseguros.mobile.beans.DocumentsBeans**
 -keep class br.com.libertyseguros.mobile.beans.DocumentsDeleteBeans**
 -keep class br.com.libertyseguros.mobile.beans.DocumentsUploadBeans**
 -keep class br.com.libertyseguros.mobile.beans.InfoPhoneBeans**
 -keep class br.com.libertyseguros.mobile.beans.PaymentPriceBeans**
 -keep class br.com.libertyseguros.mobile.beans.RegisterActivationBeans**
 -keep class br.com.libertyseguros.mobile.beans.VerifyBeans**
 -keep class br.com.libertyseguros.mobile.beans.HomePaymentsBeans**
 -keep class br.com.libertyseguros.mobile.beans.CitiesBeans**
 -keep class br.com.libertyseguros.mobile.beans.SecondCopyPlicy**
 -keepclassmembers class br.com.libertyseguros.mobile.beans.** { *;}


-dontwarn com.datami.**
# Keep classes under smisdk
-keep class com.datami.** {
*;
}
-keep class com.d.c.** {
*;
}
#-keep public class org.jsoup.** {
#public *;
#}
#



-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.**


# if excluding AltBeacon Library
-dontwarn org.altbeacon.**
# if excluding GMS library
-dontwarn com.google.android.gms.**

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-dontwarn okhttp3.internal.platform.*
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn org.jetbrains.annotations.NotNull
-dontwarn org.jetbrains.annotations.Nullable

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}