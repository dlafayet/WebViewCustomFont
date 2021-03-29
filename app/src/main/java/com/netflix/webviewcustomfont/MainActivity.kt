package com.netflix.webviewcustomfont

import android.app.ActivityManager
import android.content.Context
import android.graphics.fonts.SystemFonts
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    private var count = 37
    private lateinit var webView: WebView
    private lateinit var optionButton: TextView
    private lateinit var timer: Timer
    private val TAG = "Netflix"

    external fun stringFromJNI(): String
    external fun hasFontSupportForText(): Boolean

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = stringFromJNI()
        val previousButton = findViewById<Button>(R.id.buttonPrevious)
        webView = findViewById(R.id.webview)
        previousButton.setOnClickListener {
            webView.loadDataWithBaseURL("file:///android_asset/",
                    getHtmlString(--count),
                    "text/html",
                    "utf-8",
                    null)
            getMemoryInfo()
            val hasJapaneseFont = hasJapaneseFonts()
            Log.d(TAG, "Has Japanese fonts $hasJapaneseFont")

            val supported = hasFontSupportForText()
            Log.d(TAG, "hasFontSupportForText $supported")
        }

        optionButton = findViewById(R.id.buttonOption)
        optionButton.setOnClickListener {
            if (optionButton.text.equals(getString(R.string.font_display_optional))) {
                optionButton.text = getString(R.string.font_display_block)
            } else {
                optionButton.text = getString(R.string.font_display_optional)
            }
        }
        timer = Timer()

    }

    fun hasJapaneseFonts(): Boolean {
        var hasJaFont = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val fontSet = SystemFonts.getAvailableFonts()
            Log.d("Netflix", "Found ${fontSet.size} fonts")
            for (font in fontSet) {
                font.file?.let {
                    Log.d("Netflix", "Path: ${it.absolutePath}")
                }
                val languageTags = font.localeList.toLanguageTags()
                Log.d("Netflix", "Locale list: $languageTags")
                val tags = languageTags.split(",").toTypedArray()
                if (tags.isNotEmpty()) {
                    for (tag in tags) {
                        if (tag == "ja") {
                            hasJaFont = true
                        }
                    }
                }
            }
        }
        return hasJaFont
    }

    fun getMemoryInfo() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memClass = am.memoryClass
        val largeMem = am.largeMemoryClass
        val memInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memInfo)
        Log.d(TAG, "memory class: $memClass")
        Log.d(TAG, "large memory class: $largeMem")
        Log.d(TAG, "memory info: $memInfo")
    }

    override fun onStart() {
        super.onStart()
        val task = object : TimerTask() {
            override fun run() {
                var html = getHtmlString(++count)
                runOnUiThread {
                    webView.loadDataWithBaseURL("file:///android_asset/",
                            html,
                            "text/html",
                            "utf-8",
                            null)
                }
            }
        }
        timer.scheduleAtFixedRate(task,0, 1000)
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    fun getHtmlString(count: Int): String {
        val builder = StringBuilder()
        try {
            val path = "html/subtitles-ja-$count.html"
            val inputStream: InputStream = assets.open(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            while (inputStream.read(buffer) != -1) {
                builder.append(String(buffer))
            }
            inputStream.close()
        } catch (e: FileNotFoundException) {
            this.count = 0
        }

        var html = builder.toString()
        if (optionButton.text.equals(getString(R.string.font_display_optional))) {
            html = html.replace(getString(R.string.font_display_block), getString(R.string.font_display_optional))
        }

        return html
    }
}
