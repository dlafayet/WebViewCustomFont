package com.netflix.webviewcustomfont

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var webView: WebView
    private lateinit var optionButton: TextView
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextButton = findViewById<Button>(R.id.buttonNext)
        webView = findViewById(R.id.webview)
        nextButton.setOnClickListener {
            webView.loadDataWithBaseURL("file:///android_asset/",
                    getHtmlString(--count),
                    "text/html",
                    "utf-8",
                    null)
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
