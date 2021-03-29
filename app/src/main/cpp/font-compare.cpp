#include <jni.h>
#include <string>
#include <codecvt>
#include <locale>
#include <android/font.h>
#include <android/font_matcher.h>
#include <android/system_fonts.h>
#include <android/log.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_netflix_webviewcustomfont_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_netflix_webviewcustomfont_MainActivity_hasFontSupportForText(JNIEnv *env, jobject thiz) {
    AFontMatcher* matcher = AFontMatcher_create();
    AFontMatcher_setLocales(matcher, "ja");
    std::u16string u16 = u"引用が長く続く場合";
    const auto* matchText = reinterpret_cast<const uint16_t*>(u16.data());
    uint32_t runLength;
    AFont *font = AFontMatcher_match(matcher, "sans-serif", matchText,
                                     u16.length(), &runLength);
    __android_log_print(ANDROID_LOG_VERBOSE, "Netflix", "Font file path: %s run length %d",
                        AFont_getFontFilePath(font), runLength);
    AFont_close(font);
    AFontMatcher_destroy(matcher);
    return u16.length() == runLength;
}
