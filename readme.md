# HttpClientでのcookie有効な通信

# わかったこと

* WebViewで取得したcookieをHttpClient(volley)で利用する方法
* HttpClient(volley)でcookie有効にする方法
* `CookieManager`クラスは **2種類** あるので使い分けに気を付ける

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

## 注意点

* `CookieManager`クラスは **2種類** ある
   * `android.webkit.CookieManager`
      * webviewでのcookieを制御するための物
   * `java.net.CookieManager`
      * HttpClientでのcookieを制御するための物
   * 管理データ自体は独立の様なので、気を付ける
* 想定アプリはおそらく、、、
   * ログイン系の処理 => HttpClient
   * 広告を出す系 => WebView
   * なので、双方向にcookieを共有する必要があるかもしれない

# アプリ概観

## やってること

* `WebView`側でcookieを取得しておいて、
* HttpClient (Volley) の通信で利用する

パッとcookieの中身まで把握してやり取りできるサーバが無かったので
通信処理が楽そうなwebviewでcookieを取得する様にした

## UIと機能

* [W/]ボタン
   * HttpClient(Volley)でCookie**有効**でgoogle.comを表示します
   * レスポンスはそのまま下部のwebviewで`loadData()`します
* [W/O]ボタン
   * HttpClient(Volley)でCookie**無効**でgoogle.comを表示します
   * レスポンスはそのまま下部のwebviewで`loadData()`します
   * **一度[W/]ボタンで通信した後だとcookie有効な結果が見えます**
      * Volleyのキャッシュが効いてるから、と想定
* [WEBVIEW]ボタン
   * webviewでcookie有効にgoogle.comにアクセス
   * googleアカウントにログインしてcookieを生成しておいてください
* [CLEAR]ボタン
   * webviewの表示をクリア(表示のクリアのみ)

## 確認内容と手順

* HttpClientでのcookie**有効**な通信
   * [WEBVIEW]ボタンで表示したviewでgoogleアカウントにログイン
   * [CLEAR]ボタンで表示を消す
   * [W/]ボタンを押す
      * googleアカウントに **ログインした状態** のgoogle.comを表示できる
* HttpClientでのcookie**無効**な通信
   * 一度アプリを終了させる（Volleyキャッシュを消すため）
   * [W/O]ボタンを押す
      * googleアカウントに **ログインしてない状態** のgoogle.comを表示できる
