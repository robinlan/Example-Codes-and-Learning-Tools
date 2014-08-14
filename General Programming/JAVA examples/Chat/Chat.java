import java.applet.*;		// Applet
import java.io.*;			// InputStream, BufferedReader, InputStreamReader etc
import java.net.*;			// URL, Socket, UnknownHostException
import java.awt.*;			// Label, TextField, TextArea, Color, Button
import java.awt.event.*;	// ActionListener, ActionEvent
import java.util.*;			// StringTokenizer

public class Chat extends Applet implements Runnable, ActionListener {
							// Runnable, ActionListenerインターフェース実装

	Label TitleLabel;								// タイトルラベル
	TextField InputField;							// 入力フィールド
	Button SendButton;								// 送信ボタン
	Button QuitButton;								// 終了ボタン
	TextArea DisplayArea;							// 表示エリア
	TextArea MemberArea;							// メンバーエリア
	Thread thread;									// スレッド
	Socket socket;									// ソケット
	String Name = null;								// 名前
	AudioClip Chime;								// チャイム
 	BufferedReader NetInput;						// ネットワーク経由入力
	PrintStream NetOutput;							// ネットワーク経由出力

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
        Chime = getAudioClip(getCodeBase( ), "sound/chime.au");

		setLayout(null);							// 自由レイアウト設定

		// 各種GUI作成
		TitleLabel = new Label("ちゃっと  Your name");
		TitleLabel.setBounds(10, 10, 130, 20);

		InputField = new TextField("guest", 50);
		InputField.setBackground(new Color(153, 255, 204));
		InputField.setBounds(150, 10, 340, 20);

		SendButton = new Button("送信");
		SendButton.addActionListener(this);			// リスナー設定
		SendButton.setBounds(500, 10, 40, 20);

		QuitButton = new Button("退出");
		QuitButton.addActionListener(this);
		QuitButton.setBounds(550, 10, 40, 20);

		DisplayArea = new TextArea(20, 80);
		DisplayArea.setEditable(false);				// 書込み禁止
		DisplayArea.setBackground(new Color(0, 102, 0));
		DisplayArea.setForeground(Color.white);
		DisplayArea.setBounds(10, 40, 480, 250);

		MemberArea = new TextArea(20, 10);
		MemberArea.setEditable(false);				// 書込み禁止
		MemberArea.setBackground(new Color(153, 255, 51));
		MemberArea.setBounds(500, 40, 90, 250);

		add(TitleLabel);
		add(InputField);
		add(SendButton);
		add(QuitButton);
		add(DisplayArea);
		add(MemberArea);

		try {
			URL homeURL = getCodeBase( );			// 基本URL取得
			String host = homeURL.getHost( );		// ホスト名
			int port = 8004;						// 送受信のポート番号
			try {
				socket = new Socket(host, port);	// ストリームソケット生成
			} catch (Exception e) {
				DisplayArea.append("Not able to connect, sorry \n");
			}

			// ソケットから入力ストリームを作成
			InputStream inputstream = socket.getInputStream( );
			// 入力ストリームから行単位に入力できるバッファドリーダを作成
			NetInput = new BufferedReader(new InputStreamReader(inputstream));

			// ソケットから出力ストリームを作成
			OutputStream outputstream = socket.getOutputStream( );
			// 出力ストリームからデータ出力ストリーム作成
			NetOutput = new PrintStream(outputstream);

			thread = new Thread(this);				// スレッド生成
			thread.start( );						// スレッドスタート
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		String message;

		Chime.play( );								// チャイム再生
		try {
			while ((message = NetInput.readLine( )) != null) {
				Chime.play( );						// チャイム再生
				if ((message.substring(0,1)).equals("T"))
					DisplayArea.append(message.substring(2) + "\n");
				else {
					MemberArea.setText("");			// メンバーエリアクリア
					String members = message.substring(2);
					StringTokenizer st = 
						new StringTokenizer(members, ":");
					while(st.hasMoreTokens( ))  {
						String member = st.nextToken( );
						MemberArea.append(member + "\n");
					}
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// イベント処理
		String message;
		Button button = (Button)evt.getSource( );	// イベントソース

		if (button == SendButton) {					// 送信ボタン
			message = InputField.getText( );
			if (message == null || (message.substring(0,2)).equals("　"))
				return;
			if (Name == null) {
				Name = message;
				TitleLabel.setText("ちゃっと  Message");
			}
			NetOutput.println(message);				// ネットワーク経由でmesage出力
			NetOutput.flush( );						// バッファ内データを強制的に出力
			InputField.setText("");					// 入力フィールドをクリア
		} else if (button == QuitButton) {			// 退出ボタン
			SendButton.setVisible(false);			// 送信ボタンを隠す
			QuitButton.setVisible(false);			// 退出ボタンを隠す
			stop( );								// 処理を終了
		}
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		NetOutput.println("chat_quit");				// 終了メッセージ送信
		NetOutput.flush( );							// バッファ内データを強制的に出力

		try {
			thread.sleep(3000);						// 終了メッセージの送信完了待ち
		} catch (InterruptedException e) {
			System.out.println(e);
		}

		try {
			NetInput.close( );						// ネットワーク経由ストリームクローズ
			NetOutput.close( );
			socket.close( );						// ソケットクローズ
		} catch (Exception e) {
			System.out.println(e);
		}
		thread = null;								// スレッドを無効に
	}
}
