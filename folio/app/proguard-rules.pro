# Add project specific ProGuard rules here.
-keepattributes *Annotation*, Signature, Exception
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keep,includedescriptorclasses class com.folio.app.**$$serializer { *; }
-keepclassmembers class com.folio.app.** { *** Companion; }
