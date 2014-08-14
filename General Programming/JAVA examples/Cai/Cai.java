import java.awt.*;		// Graphics, Image, Font, Color, Frame, Button, TextField
import java.awt.event.*;// WindowListener, WindowEvent
import java.io.*;		// FileInputStream, BufferedReader, InputStreamReader, IOException
import java.util.StringTokenizer;	// StringTokenizer
import java.util.Random;

class Cai extends Frame implements WindowListener, ActionListener {
	int FrameWidth = 1024, FrameHeight = 768;		// フレームサイズ

	Label UserIdLabel, PasswordLabel;				// 表示ラベル
    TextField UserIdField = new TextField(30);		// ユーザID入力域
    TextField PasswordField = new TextField(30);	// パスワード入力域
	Button LoginButton;								// ログインボタン
	String UserId;									// ユーザID
	String Password;								// パスワード
	int LoginCount = 0;								// ログイン回数
	String CodedPassword = new String( );			// 暗号化されたパスワード

	String MondaiFile ="cai.txt";					// 問題ファイル
	int MondaiMax = 1000;							// 学習問題数(Max 1000)
	int Status[ ] = new int[MondaiMax];				// 学習状況テーブル
	int StatusPoint = 0;							// 学習状況ポイント

	int PassBasis = 3;								// 合格基準　3回連続正解で合格

	int StudyMax = 5;								// 学習サイズ（繰り返し学習する問題数）
	int Study[ ] = new int[StudyMax];				// 学習テーブル（学習番号格納）
	int StudyPoint = 0;								// 学習ポイント

	int LineMax = 20;								// 最大格納行数
	String Mondai[][]
				= new String[StudyMax][LineMax+1];	// 問題テーブル（問題文を格納）
	int KaitougunMax = 30;							// 解答群の最大数
	int KakkoMondaiMax = 20;						// （　）問題の最大数

	String Contents[] = new String[LineMax];		// 問題文
	String Kaitougun[] = new String[KaitougunMax];	// 解答群
	int Seikai[] = new int[MondaiMax];				// 正解番号
	int LineNo;										// 行カウンタ
	int	KakkoMondaiCount;							// （　）問題カウンタ
	int	KaitougunCount;								// 解答群カウンタ
	StringBuffer Delimiter = new StringBuffer( );	// 区切り文字
	TextField Kaitouran[] = new TextField[MondaiMax];// 解答欄

	Button SaitenButton, StudyButton, EndButton;	// 各種ボタン
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	Color BackColor = new Color(0, 140, 140);		// 背景色

	FileInputStream MondaiFis;						// 問題ファイル入力ストリーム
	BufferedReader MondaiBr;						// バッファード入力ストリーム

	boolean StudyFlag = false;						// 学習許可フラグ　初期は不可
	boolean StudyFinishFlag = false;				// 学習完了フラグ

	// メイン処理 -----------------------------------------------------------------------
	public static void main(String args[ ]) {
		Cai frame = new Cai("Cai");					// フレーム作成
		frame.init( );								// 初期化処理
	}
	// コンストラクタ -------------------------------------------------------------------
	public Cai(String title) {
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
		setBackground(BackColor);

		// ログイン画面用ＧＵＩ ---------------------------------------------------------
		setLayout(null);							// 自由レイアウト設定
        UserIdLabel = new Label("UserID");
		UserIdLabel.setForeground(Color.white);		// 文字カラー設定
		UserIdLabel.setFont(new Font("courier", Font.BOLD, 20));// 入力文字のフォント設定
		UserIdLabel.setBounds(FrameWidth/2-150, FrameHeight*1/4, 150, 30);
		add(UserIdLabel);

		UserIdField.setBackground(Color.white);		// 背景カラー設定
		UserIdField.setFont(new Font("courier", Font.BOLD, 20));
		UserIdField.setBounds(FrameWidth/2, FrameHeight*1/4, 200, 30);
		add(UserIdField);
		UserIdField.requestFocus( );				// 入力フォーカスを要求

        PasswordLabel = new Label("Password");
		PasswordLabel.setForeground(Color.white);	// 文字カラー設定
		PasswordLabel.setFont(new Font("courier", Font.BOLD, 20));
		PasswordLabel.setBounds(FrameWidth/2-150, FrameHeight*2/4, 150, 30);
		add(PasswordLabel);

		PasswordField.setBackground(Color.white);	// 背景カラー設定
		PasswordField.setFont(new Font("courier", Font.BOLD, 20));
		PasswordField.setBounds(FrameWidth/2, FrameHeight*2/4, 200, 30);
		add(PasswordField);
		PasswordField.setEchoChar('*');

		add(LoginButton = new Button("ログイン"));
		LoginButton.setFont(new  Font("System", Font.PLAIN, 20));
		LoginButton.setBounds((FrameWidth - 100)/2, FrameHeight*3/4, 100, 30);
		LoginButton.addActionListener(this);		// ボタンにリスナー追加

		// 学習画面用ＧＵＩ -------------------------
		add(StudyButton = new Button("　学習　"));
		StudyButton.setFont(new  Font("System", Font.PLAIN, 20));
		StudyButton.setBounds(50, 50, 100, 30);
		StudyButton.addActionListener(this);
		StudyButton.setVisible(false);

		add(SaitenButton = new Button("　採点　"));
		SaitenButton.setFont(new  Font("System", Font.PLAIN, 20));
		SaitenButton.setBounds((FrameWidth - 100) / 2, 50, 100, 30);
		SaitenButton.addActionListener(this);
		SaitenButton.setVisible(false);

		add(EndButton = new Button("　終了　"));
		EndButton.setFont(new  Font("System", Font.PLAIN, 20));
		EndButton.setBounds(FrameWidth - 150, 50, 100, 30);
		EndButton.addActionListener(this);
		EndButton.setVisible(false);

		// 解答欄設定 -------------------------
		for (int i = 0; i < MondaiMax; i++) {
			Kaitouran[i] = new TextField(10);
			Kaitouran[i].setFont(new  Font("System", Font.PLAIN, 20));
		    Kaitouran[i].setBackground(new Color(200, 220, 200));
			add(Kaitouran[i]);
			Kaitouran[i].addActionListener(this);
			Kaitouran[i].setBounds(FrameWidth - 100, 127 + i * 30, 50, 20);
			Kaitouran[i].setVisible(false);
		}
	}
	// ログインチェック処理 -------------------------------------------------------------
	public void LoginCheck( ) {
		LoginCount++;								// ログインカウント

		UserId = UserIdField.getText( );			// ユーザID取得
		Password = PasswordField.getText( );		// パスワード取得

		// 入力したパスワードからランダム発生のシード作成
		long seed = 0;								// ランダム発生シード作成
		for (int i = 0; i < Password.length( ); i++)
			seed += (long)(Password.charAt(i));
		Random rand = new Random(seed);				// 乱数ジェネレータ生成
		// パスワードから作成したランダム発生シードを利用して
		// ユーザIDを暗号化されたパスワードに変換
		CodedPassword = "";							// 暗号化パスワード初期化
		for (int i = 0; i < UserId.length( ); i++) {
			char c = (char)(UserId.charAt(i) + rand.nextInt( ) % 20);
			CodedPassword += String.valueOf(c);
		}

		// 学習ファイル『ユーザID』を調べる
         File file = new File(UserId);				// ファイルオブジェクト生成

         if (file.exists( ) == true) {				// 学習ファイル『ユーザID』がある場合
	        try {
				// 入力ストリームfisを作成
				FileInputStream fis = new FileInputStream(UserId);
				// 入力ストリームからバイナリ形式で入力するためのデータ入力ストリーム作成
				DataInputStream dis = new DataInputStream(fis);

				String InputPassword = new String( );
				// 作成された暗号化パスワードはUserIdと同じ長さ
				for (int i = 0; i < UserId.length( ); i++)
					InputPassword += String.valueOf(dis.readChar( ));	// パスワード入力

				if (CodedPassword.compareTo(InputPassword) == 0) {		// 一致する場合
					// 次のデータからすべてを入力して学習状況テーブルに保存する
					try {
						MondaiMax = 0;								// 問題数０クリア
						while (true) {
				        	Status[MondaiMax] = dis.readInt( );		// 学習状況入力
							MondaiMax++;							// 問題数カウント
						}
					} catch(EOFException e) {		// ファイルの終端に達した場合の処理
					}
					StudyFlag = true;				// 学習許可
					ScreenClear( );					// 画面クリア
					StudyStart( );					// 学習スタート
				}
				else {								// 一致しない場合
					// 入力域をクリア
					UserIdField.setText("");
					PasswordField.setText("");
					UserIdField.requestFocus( );	// 入力フォーカスを要求

					// 再度ユーザIDとパスワードを入力する
					if (LoginCount == 3) {			// 3回間違えた場合，終了
						dis.close( );				// 入力ストリームをクローズ
						fis.close( );				// 入力ストリームをクローズ
						dispose( );					// フレームの廃棄
						System.exit(0);				// プログラム終了
					}
				}
				dis.close( );						// 入力ストリームをクローズ
				fis.close( );						// 入力ストリームをクローズ
		    } catch(IOException e) {				// 入出力に対する例外処理
	            System.err.println(e);
    		}
		}
		else {		// 学習ファイル『ユーザID』がない場合，新規ユーザ
			StudyFlag = true;						// 学習許可
			MondaiCount( );							// 問題数カウント
			ScreenClear( );							// 画面クリア
			StudyStart( );							// 学習スタート
		}
	}
	// 学習テスト数調査 -----------------------------------------------------------------
	void MondaiCount( ) {
		MondaiMax = 0;								// 問題数０クリア
        try {
			// 入力ストリームfisを作成
			FileInputStream fis = new FileInputStream(MondaiFile);
			// 入力ストリームから行単位に入力するためのバッファドリーダ作成
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			try {
				String str;
				while ((str = br.readLine( )) != null) {// 行単位に入力
					if (str.compareTo("／") == 0)	// テストの区切りチェック
						MondaiMax++;				// 問題数カウントアップ
				}
		    } catch(IOException e) {				// 入出力に対する例外処理
	            System.err.println(e);
    		}
            br.close( );							// バッファドリーダをクローズ
			fis.close( );							// 入力ストリームをクローズ
        } catch (IOException e) {					// 入出力に対する例外処理
            System.err.println(e);
        }
	}
	// 学習開始処理 ---------------------------------------------------------------------
	public void StudyStart( ) {
		// 学習ＧＵＩ　表示
		StudyButton.setVisible(true);
		SaitenButton.setVisible(true);
		EndButton.setVisible(true);

		Delimiter.append('，');						// 解答群の取り出し用デリミタ追加

		// 問題ファイルオープン
		try {
			MondaiFis = new FileInputStream(MondaiFile);
			MondaiBr = new BufferedReader(new InputStreamReader(MondaiFis));
	    } catch(FileNotFoundException e) {			// 入出力に対する例外処理
            System.err.println(e);
   		}

		// 学習テーブル（学習番号格納)を-1でクリア
		for (int i = 0; i < StudyMax; i++)
			Study[i] = -1;

		study( );									// 学習

	}
	// 学習 -----------------------------------------------------------------------------
	void study( ) {
		int line = 0;								// 問題文の行カウント

		if (Study[StudyPoint] == -1) {				// 学習番号が登録されていない場合
			// 未学習の問題を探す
			boolean foundflag = false;				// 未学習の問題検出フラグ
			do {
				// 学習メモリの問題文をクリア
				for(int i = 0; i < LineMax; i++)
					Mondai[StudyPoint][i] = "";

				// 問題文を学習にセット
				line = 0;
				try {
					String str;
					while ((str = MondaiBr.readLine( )) != null) {  // 行単位に入力
						Mondai[StudyPoint][line++] = str;	// 問題文をセット
						if (str.compareTo("／") == 0)		// 問題の区切りチェック
							break;
					}
			    } catch(IOException e) {			// 入出力に対する例外処理
    			}
				// 問題文のファイルがシーケンシャルファイルであるため
				// 学習状況に関係なく入力する
				if (line > 0) {						// 問題文を入力できた場合
					if(Status[StatusPoint] < PassBasis) {// 学習状況が合格基準未満の場合
						Study[StudyPoint] = StatusPoint; // 学習状況番号を学習テーブルに
						foundflag = true;			// 未達成の問題を見つける
					}
					StatusPoint++;					// 学習状況番号をカウントアップ
				}

				// 入力できたが未達成の問題が見つからない間
			} while (line > 0 && foundflag == false);

			// --------------------------------------------------------------------------

			if (line == 0) {						// 問題がなかった場合
				// 最後まで調べたが，合格基準未満が見つからなかった
				// この場合，現在学習中のStudy内で合格基準未満があるか調べる
				// それでもない場合は，学習完了とする

				int StudyPointKeep = StudyPoint;	// 現在のStudyPointを覚えておく
				do {
					StudyPoint++;					// 次を調べる
					if (StudyPoint == StudyMax)		// ラストの次は先頭
						StudyPoint = 0;
					if (Study[StudyPoint] != -1)	// 学習番号が登録されている場合
						break;
				} while (StudyPoint != StudyPointKeep);	// 最初のStudyNoPointでない場合

				if (StudyPoint == StudyPointKeep) {	// 最初のStudyPointの場合
					// 学習修了
					StudyFinishFlag = true;
					studysave( );					// 学習内容を保存
					ScreenClear( );					// 画面クリア
					WorkGraphics.drawString("全学習合格　修了",
											FrameWidth/2-100, FrameHeight/2);
					repaint( );						// 再描画
					return;
				}
			}
		}

		// 問題文表示
		MondaibunDisp( );

		// 解答欄表示
		KaitouranDisp( );

		repaint( );
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (StudyFlag == true) {					// 学習フラグがオンの場合
			g.drawImage(WorkImage, 0, 0, this);		// 作業イメージ表示
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 画面クリア -----------------------------------------------------------------------
	void ScreenClear( ) {
		// ログインＧＵＩ非表示
		UserIdLabel.setVisible(false);
		PasswordLabel.setVisible(false);
	    UserIdField.setVisible(false);
		PasswordField.setVisible(false);
		LoginButton.setVisible(false);
		// 学習ＧＵＩ非表示
		StudyButton.setVisible(false);
		SaitenButton.setVisible(false);
		EndButton.setVisible(false);
		for (int i = 0; i < MondaiMax; i++)
			Kaitouran[i].setVisible(false);
		// 画面を背景色でクリア
		WorkGraphics.setColor(BackColor);
		WorkGraphics.fillRect(0, 0, FrameWidth, FrameHeight);
		WorkGraphics.setColor(new Color(255, 255, 255));
	}
	// 問題文を表示 ---------------------------------------------------------------------
	void MondaibunDisp( ) {
		// 背景色で画面クリア
		WorkGraphics.setColor(BackColor);
		WorkGraphics.fillRect(0, 0, FrameWidth, FrameHeight);
		WorkGraphics.setColor(new Color(255, 255, 255));

		LineNo = 0;									// 行番号
		KakkoMondaiCount = 0;						// （　）問題数
		KaitougunCount = 0;							// 解答群の数

		for (int line=0; line < LineMax && KakkoMondaiCount < KakkoMondaiMax; line++){

			String str = Mondai[StudyPoint][line];	// StudyPointの問題のline行の文
			if (str.compareTo("／") == 0)			// 区切りの場合 
				break;								// ループ脱出
			if (str.compareTo("") == 0) {			// ヌルの場合
			LineNo++;								// 表示行番号をカウントアップ
				continue;							// 飛ばす
			}

			// strの中に（　があるかチェック
			int n, p;
			Contents[LineNo] = " ";					// 問題文の各行を初期化
			n = 0;									// 編集開始位置n
			while ((p = str.indexOf("（", n)) >= 0) {	// n番目から( を探す
			    String str2 = str.substring(n, p);	// n番目から( の手前までを取り出す

				Contents[LineNo] = Contents[LineNo]
								 + str2 + "  ( " + (KakkoMondaiCount+1) + " )  ";
				n = str.indexOf("）", p) + 1;		// ) を探す

				StringTokenizer st = new StringTokenizer(str.substring(p+1, n-1), 
														Delimiter.toString( ));

				int FirstSetFlag = 1;				// 先頭の正解をセットするためのフラグ
				while  (st.hasMoreTokens( ))  {		// トークンがある間繰り返す
					String s = st.nextToken( );		// 次のトークンを取り出す
					int flag = 0;					// 同一解答フラグ
					for (int i = 0; i < KaitougunCount; i++) // 登録されている解答群を調査
						if (s.compareTo(Kaitougun[i]) == 0) {	// 登録されている場合
							if (FirstSetFlag == 1) {// ( )内の解答群の先頭の場合
								Seikai[KakkoMondaiCount] = i;	// 正解番号をセット
								FirstSetFlag = 0;	// 正解をセット済とする
							}
							flag = 1;				// 登録済
						}
					if (flag == 0) {				// 登録されていない場合
						if (FirstSetFlag == 1) {	// ( )内の解答群の先頭の場合
							Seikai[KakkoMondaiCount] = KaitougunCount;// 正解番号をセット
							FirstSetFlag = 0;		// 正解をセット済とする
						}
						Kaitougun[KaitougunCount] = s;	// 解答群にセット
						KaitougunCount++;			// 解答群の数をカウントアップ
					}
				}
				KakkoMondaiCount++;					// （　）問題数カウントアップ
			}
			if (p == -1) {							// （　の検出がなくなった場合
				// n番目から最後までを問題文に加える
				Contents[LineNo] += str.substring(n);
			}

			// 問題文表示
			WorkGraphics.drawString(Contents[LineNo], 50, LineNo*24 + 120);
			LineNo++;								// 表示行番号をカウントアップ
		}
		// 解答群をランダムに交換，同時に正解番号も変更 ---------------------------------
		for (int i = 0; i < KaitougunCount; i++) {
			// 解答群のi番目とw番目を交換
			int w = (int)(Math.random( )*KaitougunCount);
			String strtemp = Kaitougun[i];
			Kaitougun[i] = Kaitougun[w];
			Kaitougun[w] = strtemp;

			// 正解にｉとｗがあれば，それぞれの値を変える
			for (int n = 0; n < KakkoMondaiCount; n++) {
				if (Seikai[n] == i)
					Seikai[n] = w;
				else if (Seikai[n] == w)
					Seikai[n] = i;
			}
		}
		// 解答群表示 -------------------------------------------------------------------
		LineNo++;
		WorkGraphics.drawString("解答群", 50, LineNo*24 + 120);	// 問題文表示
		LineNo++;
		int Column = 0;								// 表示カラム
		for (int i = 0; i < KaitougunCount; i++) {
			String KaitouStr = "("+(i+1)+")"+Kaitougun[i];

			// 解答群表示
			WorkGraphics.drawString(KaitouStr, 50+Column*18, LineNo*24 + 120);

			Column = Column + KaitouStr.length( );
			if (Column > 30) {						// 横に表示する最大文字数以上の場合
				LineNo++;							// 表示行を次の行とする
				Column = 0;							// 表示カラムを０クリア
			}
		}
	}
	// 解答欄表示 -----------------------------------------------------------------------
	void KaitouranDisp( ) {
		WorkGraphics.drawString("解答欄", FrameWidth - 150, 120);

		for (int i = 0; i < KakkoMondaiCount; i++) {	// （　）問題の数表示
			WorkGraphics.drawString("(" + (i+1) + ")",
					 FrameWidth - 150, i*30 + 145);	// (1)～表示
			Kaitouran[i].setText("");				// 内容クリア
			Kaitouran[i].setVisible(true);			// 表示
		}
		for (int i = KakkoMondaiCount; i < MondaiMax; i++) {	// 残りは非表示
			Kaitouran[i].setVisible(false);
		}

		Kaitouran[0].requestFocus( );				// 入力フォーカスを要求
	}
	// 採点 -----------------------------------------------------------------------------
	void saiten( ) {
		// 採点結果表示域を背景色でクリア
		WorkGraphics.setColor(BackColor);
		WorkGraphics.fillRect(960, 100, 1000, 600);
		WorkGraphics.setColor(new Color(255, 255, 255));

		boolean PerfectFlag = true;					// 全問正解フラグオン　仮設定

		for (int i = 0; i < KakkoMondaiCount; i++) {// （　）問題の数ループ
			int value;
	        try {
				// 入力した解答欄の値を整数化
				value = Integer.parseInt(Kaitouran[i].getText( ));
	        } catch (NumberFormatException e) {		// 数字以外の場合
    	        value = 0;							// 仮に0とする
        	}
			if (value == Seikai[i]+1)				// 表示を(1)～からとしたため，＋１
				WorkGraphics.drawString("○", FrameWidth - 40, i*30 + 145);	// 正解
			else {
				WorkGraphics.drawString("× "+(Seikai[i]+1),
											  FrameWidth - 40, i*30 + 145);	// 不正解
				PerfectFlag = false;				// 全問正解フラグ　オフ
			}
		}
		if (PerfectFlag == true) {					// 全問正解の場合
			Status[Study[StudyPoint]]++;			// 学習カウントアップ
			if (Status[Study[StudyPoint]] >= PassBasis)	// 合格基準に達した場合
				Study[StudyPoint] = -1;				// 学習番号をクリア
		}
		else
			Status[Study[StudyPoint]] = 0;			// 学習カウントクリア

		StudyPoint++;								// 学習番号を次にする
		if (StudyPoint == StudyMax)					// ラストの次の場合
			StudyPoint = 0;							// 学習番号を先頭にする

		repaint( );									// 採点結果を再描画
	}
	// 学習内容を記録 -------------------------------------------------------------------
	void studysave( ) {
        try {
			// 学習ファイルに対して出力ストリームを作成
            FileOutputStream fos = new FileOutputStream(UserId);
			// 出力ストリームにバイナリ形式で出力するためのデータ出力ストリームを作成
            DataOutputStream dos = new DataOutputStream(fos);

			dos.writeChars(CodedPassword);			// 暗号化されたパスワード出力

	        for (int i = 0; i < MondaiMax; i++)
	            dos.writeInt(Status[i]);			// 学習状況出力

			dos.close( );							// データ出力ストリームをクローズ

		} catch (IOException e) {					// 入出力に対する例外処理
			System.err.println(e);
		}
	}
	// ActionListenerインターフェースのメソッド定義 -------------------------------------
 	public void actionPerformed(ActionEvent evt) {
		Button button = (Button)evt.getSource( );

		if (button == LoginButton) {				// ログイン
			LoginCheck( );							// ログインチェック
		}
		if (button == StudyButton) {				// 学習
			study( );
		}
		if (button == SaitenButton) {				// 採点
			saiten( );
		}
		if (button == EndButton) {					// 終了
			studysave( );							// 学習記録
			dispose( );  							// フレームの廃棄
			System.exit(0);							// プログラム終了
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
