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

################### region for gradle_general
-verbose
-printmapping proguardMapping.txt
#################### end region

-dontwarn com.sun.mail.**
-dontwarn org.jetbrains.anko.**
-dontwarn com.alibaba.fastjson.**

################### region for gradle_ijkplayer
-dontwarn master.flame.danmaku.**
-keep class master.flame.danmaku.** { *; }
-keep class tv.danmaku.ijk.media.** { *; }
#################### end region

################### region for gradle_weex
-keep class com.taobao.weex.bridge.**{*;}
-keep class com.taobao.weex.dom.**{*;}
-keep class com.taobao.weex.adapter.**{*;}
-keep class com.taobao.weex.common.**{*;}
-keep class * implements com.taobao.weex.IWXObject{*;}
-keep class com.taobao.weex.ui.**{*;}
-keep class com.taobao.weex.ui.component.**{*;}
-keep class com.taobao.weex.utils.**{
    public <fields>;
    public <methods>;
    }
-keep class com.taobao.weex.view.**{*;}
-keep class com.taobao.weex.module.**{*;}
-keep public class * extends com.taobao.weex.common.WXModule{*;}
-keep public class com.taobao.weex.WXDebugTool{*;}
#################### end region

################### region for gradle_kotlin
-dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
#################### end region

################### region for gradle_xutils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
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

################### region for R
-keep public class com.fionera.demo.R$*{
    public static final int *;
}
#################### end region

################### region for gradle_sqlite
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }
#################### end region

################### region for gradle_eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#################### end region

################### region for gradle_getui
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#################### end region

################### region for gradle_ali
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
#################### end region

#################### region for gradle_umeng
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.**
-dontwarn com.sina.**

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class com.tencent.** {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class com.tencent.** {*;}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
### perhaps all keep
-keep class com.fionera.demo.** {*;}
#################### end region

################### region for gradle_rxjava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#################### end region

#################### region for gradle_webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#################### end region

################### region for gradle_support
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn android.support.v4.**
-dontwarn android.support.design.**

-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
#################### end region
