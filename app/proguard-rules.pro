# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\HP\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
-keep class mp.** { *; }

-dontwarn java.lang.invoke.*
-keepattributes Signature

-keepattributes *Annotation*
-keep class trikita.*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keep public class * extends android.view.View
-keepclassmembers class * extends android.view.View {
	public <init>(android.content.Context);
}

-dontwarn com.fasterxml.jackson.**
-dontwarn sun.reflect.*
-dontwarn javax.annotation.**
-dontwarn javax.ws.rs.**

-keepclassmembers class * {
 @com.google.api.client.util.Key <fields>;
}
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault


-dontwarn org.joda.convert.**
-dontwarn com.google.**
-dontwarn com.google.auto.**
-dontwarn autovalue.shaded.com.**
-dontwarn sun.misc.Unsafe
-dontwarn javax.lang.model.element.Modifier
# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
