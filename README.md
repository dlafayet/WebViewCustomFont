# WebViewCustomFont
This app demonstrates loading a custom font in a WebView using [`@font-face`](https://developer.mozilla.org/en-US/docs/Web/CSS/@font-face). 
The font file is stored locally in the app's assets folder. 
The app cycles through local html pages with the same font settings to demonstrate the performance of loading text in a WebView using custom fonts.

The font file used is based on [Noto CJK JP](https://www.google.com/get/noto/) subset to just unicode range U+3000-30FF.

![alt text](https://github.com/dlafayet/WebViewCustomFont/blob/main/images/screenshot.png "WebViewCustomFont app screenshot")

"Go back" button lets you go back one frame.

"Font-display" button toggles between `font-display: block` and `font-display: optional` settings.
