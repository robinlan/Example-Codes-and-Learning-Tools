import java.applet.*;			// Applet
import java.awt.*;				// Font, TextField, TextArea, Label, Color, Button
import java.awt.event.*;		// ActionListener, ActionEvent
import java.io.*;				// InputStream, InputStreamReader, BufferedReader etc
import java.net.*;				// Socket, URL, URLConnection, UnknownHostException etc
import java.util.*; 			// Calendar

public class Keijiban extends Applet implements ActionListener {
	TextArea DisplayArea;							// 表示エリア
	TextField TitleField;							// タイトル入力フィールド
	TextField NameField;							// 投稿者名入力フィールド
	TextArea InputArea;								// 内容入力エリア
	Button SendButton;								// 送信ボタン
	String filename;								// 掲示板ファイル名

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		filename = getParameter("keijiban");		// HTMLパラメータ

		setLayout(null);							// 自由レイアウト設定

		Label keijibanlabel = new Label("掲　　示　　板");
		add(keijibanlabel);	keijibanlabel.setBounds(280, 10, 140, 20);
		keijibanlabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		// 表示エリア
		DisplayArea = new TextArea(20, 100);		add(DisplayArea);
		DisplayArea.setBounds(10, 40, 680, 330);
		DisplayArea.setEditable(false);				// 書込み禁止
		DisplayArea.setBackground(new Color(0, 102, 0));
		DisplayArea.setForeground(Color.white);
		// タイトルラベル
		Label titlelabel = new Label("タイトル");	add(titlelabel);
		titlelabel.setBounds(10, 380, 50, 20);
		// 	タイトル入力フィールド
		TitleField = new TextField(20);				add(TitleField);
		TitleField.setBounds(60, 380, 400, 20);
		TitleField.setBackground(new Color(153, 255, 204));
		// 名前ラベル
		Label namelabel = new Label("投稿者名");	add(namelabel);
		namelabel.setBounds(470, 380, 50, 20);
		// 名前入力フィールド
		NameField = new TextField(16);				add(NameField);	
		NameField.setBounds(530, 380, 100, 20);
		NameField.setBackground(new Color(153, 255, 204));
		// 内容ラベル
		Label naiyoulabel = new Label("内　　容");	add(naiyoulabel);
		naiyoulabel.setBounds(10, 410, 45, 15);
		// 内容入力エリア
		InputArea = new TextArea(5, 100);			add(InputArea);	
		InputArea.setBounds(60, 410, 570, 80);
		InputArea.setBackground(new Color(153, 255, 204));
		// 送信ボタン
		SendButton = new Button("送信");
		add(SendButton);
		SendButton.setBounds(640, 470, 50, 20);
		SendButton.addActionListener(this);			// ボタンにリスナー追加 

		URL hosturl = getCodeBase( );				// アプレット自身の基底URL取得
													// http://xxx/xxxx/
		String UrlFile = hosturl + filename;		// http://xxx/xxxx/keijiban.dat
		URL url = null;								// URLオブジェクト
		try {
			url = new URL(UrlFile);					// URLオブジェクト作成
		} catch (MalformedURLException e) {			// URLに対する例外処理
			showStatus("Not Found URL-File");
		}

		try {
			URLConnection uc = url.openConnection( );	// URLコネクションオブジェクト
			uc.setDoInput(true);						// URL接続を使用して入力を行う
			uc.setUseCaches(false);						// キャッシュを無視して情報を転送
			InputStream is = uc.getInputStream( );		// 入力ストリーム
			InputStreamReader isr = new InputStreamReader(is,"SJIS");// エンコーディング
			BufferedReader netinput = new BufferedReader(isr);	// バッファリング入力

			StringBuffer buf = new StringBuffer( );				// 文字列バッファ
			String line;
			while((line = netinput.readLine( )) != null) {		// １行入力
				buf.append(line+"\n");				// 入力データと\nをbufに付加
			}
			netinput.close( );						// ネット経由入力ストリームクローズ

			DisplayArea.setText(buf.toString( ));	// bufを掲示板エリアに表示
		}
		catch(IOException e) {						// IO例外処理
			System.out.println(e);
		}
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {		// アクションイベント処理
		Button button = (Button)evt.getSource( );		// イベントのソース取得
		if (button == SendButton) {						// イベントが送信ボタンの場合
			if ((NameField.getText( )).equals("")		// 名前入力がない場合
				|| (TitleField.getText( )).equals("")	// タイトル入力がない場合
				|| (InputArea.getText( )).equals(""))	// 内容入力がない場合
				return; 								// 何もせずに戻る

			StringBuffer buf = new StringBuffer( );		// StringBufferオブジェクト作成
			buf.append("『"+TitleField.getText( )+"』　　");	// タイトル付加
			buf.append(NameField.getText( )+"　　");	// 投稿者名付加
			buf.append(NowTime( )+"\n\n");				// 現在の時間付加
			buf.append(InputArea.getText( )+"\n");		// 内容付加

			for (int i = 1; i <= 100; i++)
				buf.append("-");						// 区切り線作成付加
			buf.append("\n");							// 改行付加

			SendProcess(filename, buf.toString( ));		// 作成したbufを送信
			NameField.setText(null);					// 名前入力フィールドクリア
			TitleField.setText(null);					// タイトル入力フィールドクリア
			InputArea.setText(null);					// 内容入力エリアクリア

			StringBuffer buf2 = new StringBuffer( );	// StringBufferオブジェクト作成
			buf2.append(buf.toString( ));				// 作成したbufをbuf2にセット
			buf2.append("\n");							// \n付加
			buf2.append(DisplayArea.getText( ));		// 表示エリアをbuf2にセット
			DisplayArea.setText(buf2.toString( ));		// buf2を表示エリアにセット
		}
	}
	// 現在の時刻 -----------------------------------------------------------------------
	String NowTime( ) {
        String Weeks[ ] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

		Calendar cal = Calendar.getInstance( );
		int Year = cal.get(Calendar.YEAR);						// 年
		int Month = cal.get(Calendar.MONTH) + 1;				// 月
		int Day = cal.get(Calendar.DAY_OF_MONTH);				// 日
		String Week = Weeks[cal.get(Calendar.DAY_OF_WEEK)-1];	// 曜日
		int Hour = cal.get(Calendar.HOUR);						// 時間
		int Minute = cal.get(Calendar.MINUTE);					// 分

		return Year + "/" + Month + "/" + Day + "(" + Week + ")" + Hour + ":" + Minute;
	}
	// 送信処理 -------------------------------------------------------------------------
	void SendProcess(String filename, String contents) {	// ファイル名，内容
		Socket socket = null;						// ソケット宣言
		PrintStream netoutput;						// ネットワーク経由出力

		try {
			URL url = getCodeBase( );				// アプレット自身の基底URL取得
			String host = url.getHost( );			// URLのホスト名
			int port = 8005;						// ポート 8005

			try {
				socket = new Socket(host, port);	// ソケット生成(ホスト，ポート)
			} catch (UnknownHostException e) {
				DisplayArea.append("Not able to connect, sorry \n");
			}

			// ソケットから出力ストリームを作成
			OutputStream outputstream = socket.getOutputStream( );
			// 出力ストリームからデータ出力ストリーム作成
			netoutput = new PrintStream(outputstream);

			// ファイル名を送信
			netoutput.println(filename);			// Stringデータを出力
			// 出力ストリームをフラッシュ(バッファに入っている出力バイトを強制的に出力)
			netoutput.flush( );

			// 内容送信
			netoutput.println(contents);			// Stringデータを UTF 形式で出力
			netoutput.flush( );						// バッファ内データを強制的に出力

			// 終了データ送信
			netoutput.println("end");				// Stringデータを UTF 形式で出力
			netoutput.flush( );						// バッファ内データを強制的に出力

			netoutput.close( );						// ネット経由出力ストリームクローズ
			socket.close( );						// ソケットクローズ
		}catch(IOException e) {						// 例外処理
		}
	}
}
