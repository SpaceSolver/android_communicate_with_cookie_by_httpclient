# HttpClientでのcookie有効な通信

# (まずそもそも)HttpClientでの通信の実装方法

* https://qiita.com/riversun/items/5f78d47a3d95f857d34f
   * まとまっていてよい
   * JSON周りの話にまで言及してくれている(後々役に立ちそう)

* 公式にも記載があるので`volley`を使うことにする
   * https://developer.android.com/training/volley/index.html
   * 注意点
      * > Volley は解析中のすべてのレスポンスをメモリに保持するので、大量のダウンロード操作やストリーミング操作には適していません
   * 現時点の仕様においては問題にならないと思っている

# cookie有効な通信への拡張

