# HttpClientでのcookie有効な通信

# 参考URL(と、読んだ感想)

* https://stackoverflow.com/questions/3587254/how-do-i-manage-cookies-with-httpclient-in-android-and-or-java
   * **素のHttpClient** についての議論
   * 結局`HttpClient`より`volley`使うほうが通信の実装は楽そうなので`volley`についての情報が必要。。。
* https://qiita.com/YukiAsu/items/e4c92d0a9f4f9ba2ecdb
   * **Volleyの`RequestQueue`を継承したインスタンスでさばく。** 以上！
* http://techbooster.org/android/hacks/16474
   * Volleyとは？の詳細記事。読みましょう。
   * 注意点？
      * > 標準で5MBのディスクキャッシュを備え、キャッシュにあればネットワークへ問い合わることはありません
      * > リクエストの途中結果を知る手段がないため、ストリーミングには利用できません
         * => `Media`フレームワークを使う
      * > 大きいファイルのダウンロードにも向いていません。
         * => `DownloadManager`を使う
      * > RequestQueueを複数生成しての利用は推奨されません
         * > RequestQueueをVolleyクラスのnewRequestQueueメソッドで生成した場合は、ディスクキャッシュが”volley”ディレクトリと固定値で決まってしまうため、キャッシュ処理での排他処理ができていません。
   * Activityとの連携
      * `RequestQueue#stop`
      * `RequestQueue#start`
      * `RequestQueue#cancel`
   * デバッグ機能
      * `adb shell setprop log.tag.Volley VERBOSE`

# (まずそもそも)HttpClientでの通信の実装方法

* https://qiita.com/riversun/items/5f78d47a3d95f857d34f
   * まとまっていてよい
   * JSON周りの話にまで言及してくれている(後々役に立ちそう)

* 公式にも記載があるので`volley`を使うことにする
   * https://developer.android.com/training/volley/index.html
   * 注意点
      * > Volley は解析中のすべてのレスポンスをメモリに保持するので、大量のダウンロード操作やストリーミング操作には適していません
   * 現時点の仕様においては問題にならないと思っている
   * 使い方:
      * queueにrequestを追加
      * 非同期応答のcallbackを実装
      * 以上！(簡単だなー)

# cookie有効な通信への拡張

* [参考記事](https://qiita.com/YukiAsu/items/e4c92d0a9f4f9ba2ecdb)を元に実装する

