
-verbose

# ignore external lib classes 
-keep class !com.kendao.adblock.**,** { *; }

# keep enums
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# example to ignore classes or packages
#-keep class com.kendao.adblock.screen.MainScreen { *; }
#-keep class com.kendao.adblock.api.dto.** { *; }
