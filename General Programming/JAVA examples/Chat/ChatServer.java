import java.net.*;					// Socket, ServerSocket
import java.io.*;					// InputStream, BufferedReader, InputStreamReader,
									// OutputStream, PrintStream

public class ChatServer {
	int MAX = 100;									// 最大接続数
	ChatClient chatclient[ ]
		= new ChatClient[MAX];						// チャットクラス配列
	Thread chatThread[ ] = new Thread[MAX];			// クライアントとの入出力スレッド
	String Member[ ] = new String[MAX];				// 参加者名

	// メイン処理 -----------------------------------------------------------------------
	public static void main(String args[ ]) {
		ChatServer obj = new ChatServer( );			// ChatServerオブジェクト生成
		obj.chatprocess( );							// チャット処理
	}
	// チャット処理 ---------------------------------------------------------------------
	void chatprocess( ){
		try {
			// 指定ポートでサーバソケット作成
			ServerSocket serversocket = new ServerSocket(8004);
			System.out.println("ServerSocket=" + serversocket);

			while (true) {							// 永久に繰り返す
				int p;
				for (p = 0; p < MAX; p++)			// 接続チェック
					if (chatclient[p] == null)		// 空いている場合
						break;
				if (p == MAX)						// 空いていない場合
					continue; 						// ループから脱出

				Socket socket = serversocket.accept( );	// クライアントからの接続を待つ

				chatclient[p] = new ChatClient(socket, this, p);	// チャット作成
				chatThread[p] = new Thread(chatclient[p]);			// スレッド作成
				chatThread[p].start( );				// スレッドスタート
			}
		}catch(Exception e) {						// 例外エラー処理
			System.out.println("check point 1 : " + e);
		}
	}
	// テキスト送信 ---------------------------------------------------------------------
	synchronized void sendalltext(String message) { // 排他的に同期をとって送信
		for (int i = 0; i < MAX; i++) {				// 全員に送る
			if (chatclient[i] != null) {			// 接続されている場合
				chatclient[i].sendMessage("T:" + message);// i番目のクライアントに送信
			}
		}
	}
	// メンバー一覧送信 -----------------------------------------------------------------
	synchronized void sendallmember( ) { 
		String message = "M:";						// メンバー作成
		for (int i = 0; i < MAX; i++) {
			if (Member[i] != null)					// 現在，参加している場合
				message = message + Member[i] + ":";// メンバーの名前を付加
		}
		for (int i = 0; i < MAX; i++) {				// 全員に送る
			if (chatclient[i] != null && Member[i] != null) {
				chatclient[i].sendMessage(message);// i番目のクライアントにメンバー送信
			}
		}
	}
	// チャット終了 ---------------------------------------------------------------------
	void chatquit(int clientnumber) {
		chatclient[clientnumber] = null;			// スレッド初期化
		chatThread[clientnumber] = null;			// スレッド初期化
	}
	// 入室処理 -------------------------------------------------------------------------
	void entermember(String name, int clientnumber) {
		Member[clientnumber] = name;				// メンバー登録
	}
	// 退出処理 -------------------------------------------------------------------------
	void exitmember(int clientnumber) {
		Member[clientnumber] = null;				// メンバー削除	
	}
}

// --------------------------------------------------------------------------------------
// チャットクライアント管理クラス
class ChatClient implements Runnable {
	BufferedReader NetInput;						// ネットワーク経由入力ストリーム
	PrintStream NetOutput;							// ネットワーク経由出力ストリーム
	Socket socket;									// ソケット
	ChatServer chatserver;							// ChatServerオブジェクト
	int clientnumber;								// クライアント番号

	// コンストラクタ -------------------------------------------------------------------
	ChatClient(Socket socket, ChatServer chatserver, int clientnumber) {
		// 引数の値をこのクラスの変数にセット
		this.socket = socket;
		this.chatserver = chatserver;
		this.clientnumber = clientnumber;

		try {
			// ソケットから入力ストリームを作成
			InputStream inputstream = socket.getInputStream( );
			// 入力ストリームから行単位に入力できるバッファドリーダを作成
			NetInput = new BufferedReader(new InputStreamReader(inputstream));

			// ソケットから出力ストリームを作成
			OutputStream outputstream = socket.getOutputStream( );
			// 出力ストリームからデータ出力ストリーム作成
			NetOutput = new PrintStream(outputstream);

		} catch(Exception e) {						// 例外処理
			System.out.println("->" + e);
		}
	}
	// メッセージ送信 -------------------------------------------------------------------
	synchronized void sendMessage(String message) {
		NetOutput.println(message);					// サーバからクライアントに送信
		NetOutput.flush( );							// バッファ内データを強制的に出力
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		String name = null;							// 名前
		String sendmessage = null;					// 送信メッセージ
		try {
			while (true) {
				sendmessage = NetInput.readLine( );	// クライアントからのメッセージ受信
				System.out.println(sendmessage);
				if (sendmessage.equals("chat_quit")) {			// chat_quitの場合
					chatserver.exitmember(clientnumber);		// メンバー退出
					chatserver.sendallmember( );				// 新メンバー一覧送信
					stop( );									// チャット終了
					break;
				} else if (name == null) {						// 名前が未設定の場合
					name = sendmessage;							// 受信メッセージを名前に
					chatserver.entermember(name, clientnumber);	// メンバー入室
					chatserver.sendallmember( );				// 新メンバー一覧送信
				} else {
					chatserver.sendalltext("<" + name + "> " + sendmessage);
				}
			}
		} catch(Exception e) {						// 例外処理
			System.out.println("check point 4 : " + e);
		}
	}
	// チャット終了 ---------------------------------------------------------------------
	public void stop( ) {
		try {
			NetInput.close( );						// ネット経由入力ストリームクローズ
			NetOutput.close( );						// ネット経由出力ストリームクローズ
			socket.close( );						// ソケットクローズ
		} catch (Exception e) {						// 例外処理
			System.out.println(e);
		}
		chatserver.chatquit(clientnumber);			// チャットスレッド解放
	}
}
