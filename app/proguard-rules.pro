# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/fionera/LinuxIDE/android/sdk/tools/proguard/proguard-android.txt
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

-dontwarn com.alibaba.fastjson.**
-dontwarn master.flame.danmaku.**
-keep class master.flame.danmaku.** { *; }
-dontwarn org.eclipse.mat.**
-dontwarn com.squareup.leakcanary.**
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

################### region for gradle_retrolambda
-dontwarn java.lang.invoke.*
#################### end region

################### region for gradle_kotlin
-dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
#################### end region

################### region for gradle_butterknife
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#################### end region

################### region for gradle_okhttp
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
#################### end region

################### region for gradle_okio
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-keep class sun.misc.Unsafe { *; }
#################### end region

################### region for gradle_glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#################### end region

################### region for gradle_gif_drawable
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
#################### end region

################### region for gradle_gson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
#################### end region

################### region for gradle_bean,R
-keep class com.bean.** { *; }
-keep public class com.centling.shenyou.R$*{
    public static final int *;
}
#################### end region

################### region for gradle_sqlite
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }
#################### end region

################### region for gradle_getui
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#################### end region

################### region for gradle_support
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn android.support.design.**
-keep class com.widget.** { *; }
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep class android.support.v7.widget.RoundRectDrawable { *; }
#################### end region

################### region for GRADLE_ALI
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
#################### end region