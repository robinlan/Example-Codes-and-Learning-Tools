import java.awt.*;				// Frame, Image, Graphics, Toolkit
import java.awt.event.*;		// WindowListener, WindowEvent
import java.io.*;				// FileInputStream, BufferedReader,   InputStreamReader, IOException
import java.net.*;				// Socket
import java.util.*;				// Calendar, StringTokenizer

class Schedule extends Frame implements WindowListener, ActionListener {
	int FrameWidth = 500, FrameHeight = 600;		// フレームサイズ
	StringBuffer Delimiter = new StringBuffer( );	// 区切り文字
	int ContentsMAX = 18;							// 内容の数（6時から23時）
	Label DateLabel;								// 日付
	Label HourLabel[ ] = new Label[ContentsMAX];	// 時間
	Label Contents[ ] = new Label[ContentsMAX];		// 内容
	TextField DateField = new TextField(10);		// 日付テキストフィールド
	TextField HourField = new TextField(3);			// 時間テキストフィールド
	TextField ContentField = new TextField(40);		// 内容テキストフィールド

	int PreparationFlag = 0;						// 準備フラグ
	String SendMessage = null,						// 送信メッセージ
		   ReceiveMessage = null;					// 受信メッセージ

	Button BackButton, NextButton,					// 前日，翌日ボタン
		   DispButton, InsertButton, 				// 表示・登録ボタン
           DeleteButton, UpdateButton;				// 削除・更新ボタン

	Color BackColor = new Color(255, 210, 200);		// 背景色

	// 本日の日付
	Calendar DispDay = Calendar.getInstance( );		// 表示日付
	int Year, Month, Day;							// 年，月，日

	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	// メイン処理 -----------------------------------------------------------------------
	public static void main(String args[ ]) {
		Schedule frame = new Schedule("Schedule");	// フレーム作成
		frame.init( );								// フレーム初期化
	}
	// コンストラクタ -------------------------------------------------------------------
	public Schedule(String title) {
		setTitle(title);							// フレームにタイトル設定
	}
	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// フレーム設定
		setSize(FrameWidth, FrameHeight);			// フレームサイズ設定
		show( );									// フレーム表示

		addWindowListener(this);					// ウィンドウリスナー追加

		// 作業用グラフィック領域設定（フレームの作成後に行うこと）
		WorkImage = createImage(FrameWidth, FrameHeight);
		WorkGraphics = WorkImage.getGraphics( );
		WorkGraphics.setFont(new  Font("ＭＳ 明朝", Font.PLAIN, 20)); 	// フォント設定

		setLayout(null);							// 自由レイアウト

		// 画面設定 --------------------------------------------
		DateLabel = new Label("yyyy/mm/dd(***)");				// ラベル生成
		add(DateLabel);											// 付加
		DateLabel.setBounds((FrameWidth - 200)/2, 30, 200, 30);	// 配置・サイズ設定
		DateLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));// フォント設定
		DateLabel.setBackground(BackColor);						// 背景色設定

		// ＜・＞ボタン設定 ------------------------------------
		add(BackButton = new Button("＜"));						// ボタン生成・付加
		BackButton.setFont(new  Font("System", Font.PLAIN, 20));// フォント設定
		BackButton.setBounds(10, 30, 30, 30);					// 配置・サイズ設定
		BackButton.addActionListener(this);						// リスナー追加

		add(NextButton = new Button("＞"));						// ボタン生成・付加
		NextButton.setFont(new  Font("System", Font.PLAIN, 20));// フォント設定
		NextButton.setBounds(FrameWidth - 40, 30, 30, 30);		// 配置・サイズ設定
		NextButton.addActionListener(this);						// リスナー追加

		// 内容表示領域設定 ------------------------------------
		int BasePoint = 80;										// 上から80pixels
		for (int i = 0; i < ContentsMAX; i++) {					// 6時から23時の18個
			if (i + 6 < 10)
				HourLabel[i] = new Label(" " + (i+6));			// 時刻ラベル
			else
				HourLabel[i] = new Label("" + (i+6));
			add(HourLabel[i]);	HourLabel[i].setBounds(30, BasePoint + i * 20, 20, 20);
			HourLabel[i].setFont(new Font("TimesRoman", Font.BOLD, 20));
			HourLabel[i].setBackground(BackColor);

			Contents[i] = new Label("                              ");	// 内容をクリア
			Contents[i].setFont(new  Font("System", Font.PLAIN, 16));	// フォント設定
			if (i % 2 == 0)												// 交互に色変化
			    Contents[i].setBackground(new Color(255, 250, 200));	// 背景設定
			else
			    Contents[i].setBackground(new Color(255, 255, 255));	// 背景設定
			add(Contents[i]);											// 付加
			Contents[i].setBounds(50, BasePoint + i * 20, 400, 20);		// 配置
		}

		// 入力領域設定
		DateField.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		DateField.setBackground(new Color(100, 250, 250));			// 背景色
		add(DateField);												// フレームに付加
		DateField.setBounds(50, 460, 110, 30);						// 配置・サイズ設定
		DateField.setText("yyyy/mm/dd");							// 年月日
		DateField.addActionListener(this);							// リスナー付加

		HourField.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		HourField.setBackground(new Color(100, 250, 250));			// 背景色
		add(HourField);												// フレームに付加
		HourField.setBounds(200, 460, 50, 30);						// 配置・サイズ設定
		HourField.setText("Hour");									// 時間
		HourField.addActionListener(this);							// リスナー付加

		ContentField.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		ContentField.setBackground(new Color(150, 250, 250));		// 背景色
		add(ContentField);											// フレームに付加
		ContentField.setBounds(50, 500, 400, 30);					// 配置・サイズ設定
		ContentField.setText("Content");							// 内容
		ContentField.addActionListener(this);						// リスナー付加

		// 表示・追加・更新・削除ボタン
		add(DispButton = new Button("Disp"));						// 表示ボタン生成付加
		DispButton.setFont(new  Font("System", Font.PLAIN, 20));	// フォント設定
		DispButton.setBounds(66, 550, 80, 30);						// 配置・サイズ設定
		DispButton.addActionListener(this);							// リスナー付加

		add(InsertButton = new Button("Insert"));					// 追加ボタン生成付加
		InsertButton.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		InsertButton.setBounds(162, 550, 80, 30);					// 配置・サイズ設定
		InsertButton.addActionListener(this);						// リスナー付加

		add(DeleteButton = new Button("Delete"));					// 削除ボタン生成付加
		DeleteButton.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		DeleteButton.setBounds(258, 550, 80, 30);					// 配置・サイズ設定
		DeleteButton.addActionListener(this);						// リスナー付加

		add(UpdateButton = new Button("Update"));					// 更新ボタン生成付加
		UpdateButton.setFont(new  Font("System", Font.PLAIN, 20)); 	// フォント設定
		UpdateButton.setBounds(354, 550, 80, 30);					// 配置・サイズ設定
		UpdateButton.addActionListener(this);						// リスナー付加

		// ------------------------------------------------------------------------------
		Delimiter.append('#');						// 取り出し用デリミタ作成

		PreparationFlag = 1;						// 準備フラグ
		repaint( );

		YearMonthDay( );							// 本日DispDayの年月日
		DateLabel.setText(NowDay( )+NowWeek( ));	// 年月日(曜日)をラベルに設定

		SendMessage = "Select#" + NowDay( ) + "#";	// Select#yyyy/mm/dd#
		System.out.println(SendMessage);
		SendProcess( );								// サーバに本日の情報の入力を要求

		SetMessage( );								// サーバからの情報を表示
	}
	// メッセージ設定 -------------------------------------------------------------------
	// ReceiveMessageの内容をテキストフィールドに設定
	// ReceiveMessage = "1999/11/6# # # # # #Meeting# # # # # # # # # # # # ";
	void SetMessage( ) {
		int p1, p2;														// ポイント
		p1 = ReceiveMessage.indexOf("/", 0);							// 最初の/の位置
		Year = Integer.parseInt(ReceiveMessage.substring(0, p1));		// 年
		p2 = ReceiveMessage.indexOf("/", p1+1);							// 次の/の位置
		Month = Integer.parseInt(ReceiveMessage.substring(p1+1, p2));	// 月
		p1 = p2;
		p2 = ReceiveMessage.indexOf("#", p1+1);							// ＃の位置
		Day = Integer.parseInt(ReceiveMessage.substring(p1+1, p2));		// 日

		DateLabel.setText(NowDay( )+NowWeek( ));	// 日付(曜日)表示

		// Delimiter 区切り文字＃で区切り，6時から23字までの内容を取り出す
		int c = 0;
		StringTokenizer st =
			new StringTokenizer(ReceiveMessage.substring(p2), Delimiter.toString( ));
		while  (st.hasMoreTokens( ))  {				// トークンがある間繰り返す
			String str = st.nextToken( );			// 次のトークンを取り出す
			Contents[c].setText(str);				// 内容表示ラベルに設定
			c++;
		}
	}
	// 年月日 ---------------------------------------------------------------------------
	void YearMonthDay( ) {
		Year = DispDay.get(DispDay.YEAR);			// 年
		Month = DispDay.get(DispDay.MONTH) + 1;		// 月
		Day = DispDay.get(DispDay.DAY_OF_MONTH);	// 日
	}
	// 日付を文字列 ---------------------------------------------------------------------
	String NowDay( ) {
		DispDay.set(Year, Month - 1, Day);			// 日付セット
		YearMonthDay( );							// DispDayのYear,Month,Day
		return Year+"/"+Month+"/"+Day;
	}
	// 曜日を返す -----------------------------------------------------------------------
	String NowWeek( ) {
        String Weeks[ ] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		DispDay.set(Year, Month - 1, Day);			// 日付セット
		return "("+Weeks[DispDay.get(Calendar.DAY_OF_WEEK)-1]+")";
	}
	// SendMessageをサーバに送る --------------------------------------------------------
	void SendProcess( ) {
		try {
			String ip = "192.168.1.1";				// サーバのIPアドレス
			int port = 8003;						// 送受信ポート番号

			// ソケット作成
			Socket socket = new Socket(ip, port);	// ipアドレスとポート番号指定

			// ソケットから入力ストリームを作成
			InputStream inputstream = socket.getInputStream( );
			// 入力ストリームから行単位に入力できるバッファドリーダ作成
			BufferedReader netinput 
 				= new BufferedReader(new InputStreamReader(inputstream));

			// ソケットから出力ストリームを作成
			OutputStream outputstream = socket.getOutputStream( );
			// 出力ストリームからデータ出力ストリーム作成
			PrintStream netoutput = new PrintStream(outputstream);
			// コンソール入力ストリームから行単位に入力できるバッファドリーダを作成
			BufferedReader consoleinput  =
				new BufferedReader(new InputStreamReader(System.in));

			// サーバに命令を送る
			netoutput.println(SendMessage);
			netoutput.flush( );						// バッファ内データを強制的に出力

			// サーバから情報を受信
			try {
				ReceiveMessage = netinput.readLine( );	// 1行分のメッセージ入力
			} catch (IOException  e) {					// 入出力に対する例外処理
				System.out.println("read error");
			}
		} catch(Exception e) {						// 各種例外処理
			System.out.println(e);
		}
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (PreparationFlag == 1) {					// 準備フラグがONの場合
			WorkGraphics.setColor(BackColor);
			WorkGraphics.fillRect(0, 0, FrameWidth, FrameHeight);
			g.drawImage(WorkImage, 0, 0, this);		// 作業イメージ表示
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
 	// ActionListenerインターフェースのメソッド定義 -------------------------------------
 	public void actionPerformed(ActionEvent evt) {
		Button button = (Button)evt.getSource( );
		if (button == BackButton || button == NextButton) {	// 前日・次の日の場合
			if (button == BackButton)
				Day--;								// 前日
			else
				Day++;								// 翌日
			DispDay.set(Year, Month - 1, Day);		// DispDayの日付セット
			YearMonthDay( );						// Year，Month，Dayを取得
			DateLabel.setText(NowDay( )+NowWeek( ));	// 日付(曜日)表示
			SendMessage = "Select#" + NowDay( ) +"#";	// 指定日の情報
			SendProcess( );							// サーバに送信，およびサーバから受信
			SetMessage( );							// サーバからの情報をテキストフィールドに設定
		}
		else if (button == DispButton) {			// 表示
			SendMessage = "Select#" + DateField.getText( ) + "#";
			SendProcess( );							// サーバに送信，およびサーバから受信
			SetMessage( );							// サーバからの情報をテキストフィールドに設定
		}
		else if (button == InsertButton) {			// 追加登録
			SendMessage = "Insert#" + DateField.getText( ) + "#" 
					+ HourField.getText( ) + "#" + ContentField.getText( ) + "#";
			SendProcess( );							// サーバに送信，およびサーバから受信
			SetMessage( );							// サーバからの情報をテキストフィールドに設定
		}
		else if (button == DeleteButton) {			// 削除
			SendMessage = "Delete#" + DateField.getText( ) + "#" 
					+ HourField.getText( ) + "#" + ContentField.getText( ) + "#";
			SendProcess( );							// サーバに送信，およびサーバから受信
			SetMessage( );							// サーバからの情報をテキストフィールドに設定
		}
		else if (button == UpdateButton) {			// 更新
			SendMessage = "Update#" + DateField.getText( ) + "#" 
					+ HourField.getText( ) + "#" + ContentField.getText( ) + "#";
			SendProcess( );							// サーバに送信，およびサーバから受信
			SetMessage( );							// サーバからの情報をテキストフィールドに設定
		}
	}
	// WindowListenerインターフェースの各メソッドを定義 ---------------------------------
	public void windowOpened(WindowEvent evt) { }
	public void windowClosing(WindowEvent evt) {
		dispose( );  								// フレームの廃棄
	}
	public void windowClosed(WindowEvent evt) {
		System.exit(0);								// プログラム終了
	}
	public void windowIconified(WindowEvent evt) { }
	public void windowDeiconified(WindowEvent evt) { }
	public void windowActivated(WindowEvent evt) { }
	public void windowDeactivated(WindowEvent evt) { }
}
