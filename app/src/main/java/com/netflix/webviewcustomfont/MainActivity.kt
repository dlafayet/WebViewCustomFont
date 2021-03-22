package com.netflix.webviewcustomfont

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var webView: WebView
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextButton = findViewById<Button>(R.id.button)
        webView = findViewById<WebView>(R.id.webview)
        nextButton.setOnClickListener {
            --count;
            webView.loadDataWithBaseURL("file:///android_asset/",
                    getHtmlString(--count),
                    "text/html",
                    "utf-8",
                    null)
        }
        timer = Timer()
    }

    override fun onStart() {
        super.onStart()
        val task = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    webView.loadDataWithBaseURL("file:///android_asset/",
                            getHtmlString(++count),
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
        return builder.toString()
    }
}
