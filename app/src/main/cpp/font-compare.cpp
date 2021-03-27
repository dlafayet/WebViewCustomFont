#include <jni.h>
#include <string>
#include <codecvt>
#include <locale>
#include <android/font.h>
#include <android/font_matcher.h>
#include <android/system_fonts.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_netflix_webviewcustomfont_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_netflix_webviewcustomfont_MainActivity_hasFontSupportForText(JNIEnv *env, jobject thiz,
                                                                      jstring text) {
    // TODO: implement hasFontSupportForText()
    const char* utfText = env->GetStringUTFChars(text, NULL);

    //UTF-8 to UTF-16
    std::string source = std::string(utfText);
    std::wstring_convert<std::codecvt_utf8_utf16<char16_t>,char16_t> convert;
    const std::u16string dest = convert.from_bytes(source);

    AFontMatcher* matcher = AFontMatcher_create();
    AFontMatcher_setLocales(matcher, "ja");
    AFont *font = AFontMatcher_match(matcher, "sans-serif", dest, sizeof(dest) / sizeof(std::u16string), NULL);

    env->ReleaseStringUTFChars(text, utfText);
    return ()
}
