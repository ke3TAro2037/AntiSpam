# スパム対策プラグイン

AntiSpam は、Minecraft サーバー向けのスパム対策プラグインです。このプラグインは、プレイヤーが同じメッセージを連続して送信することを防ぎ、スパム行為を検出して対処します。AdvancedBAN と連携して、スパム行為を行ったプレイヤーに対してミュートなどの処置を行います。

## 機能

- **チャットメッセージの監視**
  - プレイヤーが同じメッセージを3回連続で送信すると、プレイヤーを一時的にミュートします（デフォルトでは5分間）。

- **定期的なチャットログのリセット**
  - 5分ごとにチャットログを自動でリセットし、古いデータをクリアします。

## インストール方法

1. プラグインの JAR ファイルをダウンロードします。
2. ダウンロードした JAR ファイルを、Minecraft サーバーの `plugins` フォルダーにコピーします。
3. サーバーを再起動します。

## 使用方法

- プラグインは自動的に有効化され、チャットの監視を開始します。
- プレイヤーが同じメッセージを3回連続で送信すると、「tempmute {プレイヤー名} 5m 連投スパムを検出しました。」というコマンドが実行されます。

## 開発者向け情報

### イベントハンドリング

- `onPlayerCommandPreprocess`: プレイヤーがコマンドを入力した際に呼び出され、禁止コマンドのチェックを行います。
- `onPlayerChat`: プレイヤーがチャットメッセージを送信した際に呼び出され、スパムメッセージのチェックを行います。

### チャットログのリセット

- プラグインの有効化時に、5分ごとにチャットログをリセットするタスクがスケジュールされます。

### コンソールコマンドの実行

- `executeConsoleCommand`: 指定されたコマンドをコンソールから実行します。
