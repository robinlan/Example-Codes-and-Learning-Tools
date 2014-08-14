import java.net.*;			// Socket, ServerSocket
import java.io.*;			// InputStream, InputStreamReader, BufferedReader,
							// IOException, OutputStream, PrintStream
import java.awt.*;			// Frame, Graphics, Image, Color, Font, Toolkit, MediaTracker,
							// Button, TextField, Checkbox, CheckboxGroup
import java.awt.event.*;	// MouseListener, MouseEvent, MouseMotionListener, 
							// WindowEvent, WindowListener, ActionListener, ActionEvent
public class NetworkShooting extends Frame 
			implements Runnable, ActionListener, 
					   MouseListener, MouseMotionListener, WindowListener {
													// 各インターフェース実装
	Thread thread = null;							// スレッド
	Image BaseImage;								// 基本ゲーム画面イメージ
	Image BackImage;								// 背景画面イメージ
	Image UFOImage[ ] = new Image[2];				// UFO画像イメージ
	Image BakudanImage;								// 爆弾画像イメージ
	Image ExplodeImage[ ] = new Image[5];			// 爆発イメージ

	Graphics BaseGraphics;							// ベースグラフィックス
	MediaTracker mt = new MediaTracker(this);		// 画像入力を監視するメディアトラッカ

	int UfoNo;										// 自分のUFO番号
	int UfoStatus[ ] = new int[10];					// 全体のUFO状態
	int MyUfoStatus;								// 自分のUFO状態
													// 1：存在，2 から 6 までは爆発状態
	int Team[ ] = new int[10];						// 各UFOのチーム
													// 0: Red team  1: Blue team
	int Existence[ ] = new int[2];					// 各チームの存在度合い
	int MyTeam;										// 自分のUFOのチーム
	int UfoMax = 10;								// UFOの最大数
	int UfoX[ ] = new int[UfoMax];					// 現在のUFOの位置
	int UfoY[ ] = new int[UfoMax];
	int UfoXBase[ ] = new int[UfoMax];				// 前回のUFOの位置
	int UfoYBase[ ] = new int[UfoMax];
	int Direction[ ] = new int[UfoMax];				// UFOからの爆弾発射方向
	int BakudanMax = 3;								// 爆弾数
	int GameInfoLen = 11 + BakudanMax * 7;			// 1ゲーム情報の長さ
	int BakudanWay[ ][ ] = new int[UfoMax][BakudanMax];	// 飛行中の爆弾方向
	int BakudanX[ ][ ] = new int[UfoMax][BakudanMax];	// 飛行中の爆弾位置
	int BakudanY[ ][ ] = new int[UfoMax][BakudanMax];
	int BackImageWidth, BackImageHeight;			// 背景画像サイズ
	int UFOImageWidth, UFOImageHeight;				// UFO画像サイズ
	int BakudanImageWidth, BakudanImageHeight;		// 爆弾画像サイズ
	int BakudanStatus = 0;							// 爆弾状況
													//  0:準備OK,1:発射,2:発射位置設定
	int FrameWidth = 500;							// フレーム幅
	int FrameHeight = 500;							// フレーム高さ
	int UfoCount;									// UFOの数(MAX:10)

	TextField IpAddressField;
	CheckboxGroup CheckBoxPartGroup, CheckBoxTeamGroup;
	Checkbox CheckBoxAdmini, CheckBoxPlayer, CheckBoxRedTeam, CheckBoxBlueTeam;
	Button OkButton;
	String NextUserIpAddress;

	boolean AdministratorFlag;						// 0:Player, 1:Administrator

	boolean GameStartFlag = false;					// ゲームスタートフラグ
	boolean GameOverFlag = false;

	int port = 7000;								// 通信ポート
	PrintStream NetOutput;							// 送信用ストリーム
	BufferedReader NetInput;						// インターフェース実装
	String GameInformation;							// ゲーム情報
	String NewGameInformation;						// 新ゲーム情報
	String ReceiveInformation;						// 受信ゲーム情報
	String SendInformation;							// 送信ゲーム情報

	// メイン----------------------------------------------------------------------------
	public static void main(String args[ ]) {
		NetworkShooting game;						// クラスオブジェクト
		game = new NetworkShooting("Network shooting game");
		game.init( );								// 初期化
		game.start( );								// スタート
		game.run( );								// スレッドの実行
		game.stop( );								// 終了処理
		game.ResultDisplay( );						// 結果表示
	}
	// フレーム作成 ---------------------------------------------------------------------
	public NetworkShooting(String title) {
		setTitle(title);
	}
	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// フレーム設定
		setBackground(Color.white);
		setForeground(Color.black);
		setSize(FrameWidth, FrameHeight);			// フレームサイズ設定
		show( );									// フレーム表示

		BaseImage = createImage(FrameWidth, FrameHeight);
		BaseGraphics = BaseImage.getGraphics( );

		BackImage = Toolkit.getDefaultToolkit( ).getImage("./image/sky.jpg");
		mt.addImage(BackImage, 0);					// メディアトラッカに画像をセット
		for (int i = 0; i < 2; i++) {
			UFOImage[i] = 
				Toolkit.getDefaultToolkit( ).getImage("./image/ufo" + i + ".gif");
			mt.addImage(UFOImage[i], 0);			// メディアトラッカに画像をセット
		}
		BakudanImage = 
				Toolkit.getDefaultToolkit( ).getImage("./image/bakudan.gif");
		mt.addImage(BakudanImage, 0);				// メディアトラッカに画像をセット
		for (int i = 0; i < 5; i++) {
			ExplodeImage[i] = 
				Toolkit.getDefaultToolkit( ).getImage("./image/explosion" + i + ".gif");
			mt.addImage(ExplodeImage[i], 0);		// メディアトラッカに画像をセット
		}
		try {
			mt.waitForID(0);						// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			System.out.println(e);
		}
		BackImageWidth = BackImage.getWidth(this);			// 背景サイズ
		BackImageHeight = BackImage.getHeight(this);
		UFOImageWidth = UFOImage[0].getWidth(this);			// UFOサイズ
		UFOImageHeight = UFOImage[0].getHeight(this);
		BakudanImageWidth = BakudanImage.getWidth(this);	// Bakudanサイズ
		BakudanImageHeight = BakudanImage.getHeight(this);

		for (int i = 0; i < UfoMax; i++)
			UfoStatus[i] = -1;						// UFOが存在しないと初期設定

		OpeningDisplay( );
 	}
	// ゲーム設定 -----------------------------------------------------------------------
	void OpeningDisplay( ) {
		setLayout(null);
		BaseGraphics.setColor(Color.black);
		BaseGraphics.setFont(new Font("TimesRoman", Font.BOLD, 30));
		BaseGraphics.drawString("Network Shooting Game", 52, 62);	// 影
		BaseGraphics.setColor(Color.red);
		BaseGraphics.drawString("Network Shooting Game", 50, 60);

		BaseGraphics.setColor(Color.black);
		BaseGraphics.setFont(new Font("TimesRoman", Font.BOLD, 12));
		BaseGraphics.drawString(
			"One administrator is necessary to play this game.", 60, 110);
		BaseGraphics.drawString(
			"The administrator is a starter, player, and checker of the gameover.", 
			60, 130);

		BaseGraphics.setColor(Color.black);
		BaseGraphics.setFont(new Font("TimesRoman", Font.BOLD, 18));
		BaseGraphics.drawString("Which part do you take in the game?", 80, 170);
		CheckBoxPartGroup = new CheckboxGroup( );
		CheckBoxAdmini = new Checkbox("Administrator", CheckBoxPartGroup, false);
		CheckBoxPlayer = new Checkbox("Player", CheckBoxPartGroup, true);
		add(CheckBoxAdmini);		CheckBoxAdmini.setBounds(100, 180, 100, 20);
		add(CheckBoxPlayer);		CheckBoxPlayer.setBounds(200, 180, 100, 20);

		BaseGraphics.drawString("Which team do you take?", 80, 230);
		CheckBoxTeamGroup = new CheckboxGroup( );
		CheckBoxRedTeam = new Checkbox("Red", CheckBoxTeamGroup, true);
		CheckBoxBlueTeam = new Checkbox("Blue", CheckBoxTeamGroup, false);
		add(CheckBoxRedTeam);		CheckBoxRedTeam.setBounds(100, 240, 100, 20);
		add(CheckBoxBlueTeam);		CheckBoxBlueTeam.setBounds(200, 240, 100, 20);

		BaseGraphics.drawString("What is the IP-address of next player ?", 80, 290);
		IpAddressField = new TextField(15);
		add(IpAddressField);	IpAddressField.setBounds(100, 300, 100, 20);

		OkButton = new Button("OK");
		add(OkButton);		OkButton.setBounds(80, 340, 50, 20);
		OkButton.addActionListener(this);			// リスナ設定

 		addMouseListener(this);						// マウスリスナ追加
		addMouseMotionListener(this);				// マウスモーションリスナ追加
		addWindowListener(this);					// ウィンドウリスナ付加

		repaint( );
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
	public void actionPerformed(ActionEvent evt) {	// イベント処理
		Button button = (Button)evt.getSource( );	// イベントソース

		if (button == OkButton) {

			if(CheckBoxAdmini.getState( ) == true)
				AdministratorFlag = true;			// Administrator
			else
				AdministratorFlag = false;			// Player

			if(CheckBoxRedTeam.getState( ) == true)
				MyTeam = 0;							// Red team
			else
				MyTeam = 1;							// Blue team

			NextUserIpAddress = IpAddressField.getText( );

			remove(OkButton);
			remove(CheckBoxAdmini);
			remove(CheckBoxPlayer);
			remove(CheckBoxRedTeam);
			remove(CheckBoxBlueTeam);
			remove(IpAddressField);

			if (AdministratorFlag == true) {		// 管理者の場合
				// 管理者の場合 ---------------------------------------------------------
				GameInformation = "";
				InitialGameInformationSet( );		// 初期ゲーム情報セット

				SendConnectProcess( );				// 送信接続
				SendGameInformation( );				// ゲーム情報送信

				ReceiveConnectProcess( );			// 受信接続

				// 一通り1週して，ゲーム参加者のデータを受信
				ReceiveGameInformation( );			// ゲーム情報受信
				GameInformationMemorySet( );		// ゲーム情報をメモリにセット
				NewGameInformation = GameInformation;	// ゲーム情報を送信情報にセット
				SendGameInformation( );				// ゲーム情報送信
			}
			else {
				// 参加者（Player）の場合 -----------------------------------------------
				ReceiveConnectProcess( );			// 受信接続
				ReceiveGameInformation( );			// ゲーム情報入力
				InitialGameInformationSet( );		// 初期ゲーム情報セット

				SendConnectProcess( );				// 送信接続
				SendGameInformation( );				// ゲーム情報送信
			}
		GameStartFlag = true;						// ゲームスタートフラグオン
		}
	}
	// 送信接続 -------------------------------------------------------------------------
	public void SendConnectProcess( ) {
		try {
			// ソケット作成
			Socket socket = new Socket(NextUserIpAddress, port);

			// ソケットから出力ストリームを作成
			OutputStream outputstream = socket.getOutputStream( );
			// 出力ストリームからデータ出力ストリーム作成
			NetOutput = new PrintStream(outputstream);

		} catch(Exception e) {						// 各種例外処理
			System.out.println("Send connect error : " + e);
		}
	}
	// 受信接続 -------------------------------------------------------------------------
	public void ReceiveConnectProcess( ) {
		try {
			ServerSocket server_socket = new ServerSocket(port);

			// accept( )でクライアントからの接続を待ち,socketを作成
			Socket socket = server_socket.accept( );

			// ソケットから入力ストリームを作成
			InputStream inputstream = socket.getInputStream( );
			// 入力ストリームからデータ入力ストリーム作成
			NetInput = new BufferedReader(new InputStreamReader(inputstream));

		} catch(Exception e) {						// 各種例外処理
			System.out.println("Receive connect error : " + e);
		}
	}
	// ゲーム情報送信 -------------------------------------------------------------------
	public void SendGameInformation( ) {
		SendInformation = NewGameInformation;
		try {
			NetOutput.println(SendInformation);
			NetOutput.flush( );
		} catch (Exception e) {						// 各種例外処理
			System.out.println("Send error!");
		}
	}
	// ゲーム情報受信 -------------------------------------------------------------------
	public void ReceiveGameInformation( ) {
		// 入力バッファとして使用する変数は，入力のみで使用すること。
		// 他の処理が行われている最中に，入力処理は特別に行われる。
		// したがって，他の処理の途中で切り替わる可能性がある。
		// 入力バッファが左辺にくるのは，この場所のみとする。
		try {
			ReceiveInformation = NetInput.readLine( );
		} catch (IOException e) {					// 入出力に対する例外処理
			System.out.println("Receive error!");
		}
		GameInformation=ReceiveInformation;
	}

	//　ゲーム情報(GameInformation)
	//　/U010100200110020023003003200200/U1 ～ /U2 ～ /U3 ～
	//　/U  : データの最初を表す
	//　0   : UFO番号
	//　1   : UFO状態（１：破損なし正常，２から６は爆破状態)
	//　0   : チーム
	//　100 : UFOのＸ位置
	//　200 : 　　　　Ｙ位置
	//　1   : 爆弾０の飛行方向
	//　100 : 　　　　　　Ｘ位置
	//　200 : 　　　　　　Ｙ位置
	//　2   : 爆弾１の飛行方向
	//　300 : 　　　　　　Ｘ位置
	//　300 : 　　　　　　Ｙ位置
	//　3   : 爆弾２の飛行方向
	//　200 : 　　　　　　Ｘ位置
	//　200 : 　　　　　　Ｙ位置

	// 各自のUFOと爆弾の初期情報セット--------------------------------------------------
	public void InitialGameInformationSet( ) {
		//  最後に登録したユーザのUFOデータの位置
		int p = GameInformation.lastIndexOf("/U");
		if (p == -1)
			UfoNo = 0;
		else
			UfoNo = 
				new Integer(GameInformation.substring(p + 2, p + 3)).intValue( ) + 1;

		MyUfoStatus = 1;							// UFO状態
		UfoStatus[UfoNo] = MyUfoStatus;				// UFO状態を配列にセット

		UfoX[UfoNo] = UfoNo * UFOImageWidth;

		Team[UfoNo] = MyTeam;
		if (MyTeam == 0) {							// Red Team
			Direction[UfoNo] = 5;					// South  爆弾発射の方向
			UfoX[UfoNo] = UFOImageWidth * (UfoNo+1);
			UfoY[UfoNo] = UFOImageHeight/2;
		}
		else {										// Blue Team
			Direction[UfoNo] = 1;					// North
			UfoX[UfoNo] = FrameWidth - UFOImageWidth * (UfoNo+1);
			UfoY[UfoNo] = FrameHeight - UFOImageHeight/2;
		}
		String ux = "00"+ String.valueOf(UfoX[UfoNo]);
		String uxp = ux.substring(ux.length( ) - 3);
		String uy = "00"+ String.valueOf(UfoY[UfoNo]);
		String uyp = uy.substring(uy.length( ) - 3);

		// 初期ゲーム情報作成
		String InitialGameInformation = "/U"
							+ String.valueOf(UfoNo) 
							+ String.valueOf(MyUfoStatus)
							+ String.valueOf(MyTeam)
							+ String.valueOf(uxp)
							+ String.valueOf(uyp);
		for (int i = 0; i < BakudanMax; i++)		// 方向とＸＹの座標値
			InitialGameInformation = InitialGameInformation + "0000000";

		// 初期ゲーム情報追加
		NewGameInformation = GameInformation + InitialGameInformation;
	}
	// ゲーム中のUFOと爆弾の情報セット -------------------------------------------------
	public void GameInformationSet( ) {
		String ux = "00"+ String.valueOf(UfoX[UfoNo]);
		String uxp = ux.substring(ux.length( ) - 3);
		String uy = "00"+ String.valueOf(UfoY[UfoNo]);
		String uyp = uy.substring(uy.length( ) - 3);

		String NowGameInformation =
				"/U" + String.valueOf(UfoNo)
				+ String.valueOf(MyUfoStatus)
				+ String.valueOf(MyTeam)
				+ String.valueOf(uxp)
				+ String.valueOf(uyp);

		// MyUfoStatusの値が2から6の場合は，UFOは爆発状態，爆発シーンを変化させる
		if (MyUfoStatus >= 2) {
			MyUfoStatus++;
			if (MyUfoStatus <= 6)
				UfoStatus[UfoNo] = MyUfoStatus;
			else
				UfoStatus[UfoNo] = MyUfoStatus = 0;	// 完全に爆発して消えた状態
		}

		for (int i = 0; i < BakudanMax; i++) {
			String mx = "00"+ String.valueOf(BakudanX[UfoNo][i]);
			String mxp = mx.substring(mx.length( ) - 3);
			String my = "00"+ String.valueOf(BakudanY[UfoNo][i]);
			String myp = my.substring(my.length( ) - 3);
			NowGameInformation = NowGameInformation
							+ String.valueOf(BakudanWay[UfoNo][i]) + mxp + myp;
		}

		String MyUFO = "/U" + String.valueOf(UfoNo);
		int p = GameInformation.indexOf(MyUFO);
		if (p != -1) {								// 存在する場合
			String mae = GameInformation.substring(0, p);
			String ato = GameInformation.substring(p + GameInfoLen);
			// 現在のゲーム情報セット
			NewGameInformation = mae + NowGameInformation + ato;
		}
		else										// 通信上，データ欠落で存在しない場合
			// 現在のゲーム情報追加
			NewGameInformation = GameInformation + NowGameInformation;
	}
	// ネットから取り込んだゲーム情報をメモリにセット -----------------------------------
	public void GameInformationMemorySet( ) {
		for (int i = 0; i < UfoMax; i++) {
			String ufo = "/U" + String.valueOf(i);
			int p = GameInformation.indexOf(ufo);
			if (p != -1 && i != UfoNo && GameInformation.length( ) >= p+GameInfoLen) {
				// 存在し，かつ自分以外の情報がある場合
				int n = new Integer(GameInformation.substring(
														p + 2, p + 3)).intValue( );
				UfoStatus[n]  = new Integer(GameInformation.substring(
														p +  3, p +  4)).intValue( );
				Team[n]  = new Integer(GameInformation.substring(
														p +  4, p +  5)).intValue( );
				UfoX[n] = new Integer(GameInformation.substring(
														p +  5, p +  8)).intValue( );
				UfoY[n] = new Integer(GameInformation.substring(
														p +  8, p + 11)).intValue( );
				int bp = p + 11;					// 爆弾ベースポイント
				for (int j = 0; j < 3; j++) {
					BakudanWay[n][j] = new Integer(
						GameInformation.substring(bp+7*j,   bp+7*j+1)).intValue( );
					BakudanX[n][j] = new Integer(
						GameInformation.substring(bp+7*j+1, bp+7*j+4)).intValue( );
					BakudanY[n][j] = new Integer(
						GameInformation.substring(bp+7*j+4, bp+7*j+7)).intValue( );
				}
			}
		}
		// 自分のUFOと他のUFOの発射された爆弾の衝突チェック
		// 当てられたかのチェック
		// 範囲内であれば，UFOを爆破初期状態にする
		if (MyUfoStatus == 1) {						// 自分のUFOが正常状態で存在する場合
			for (int i = 0; i < UfoMax; i++) {
				if (i != UfoNo && UfoStatus[i] != -1) {			// 自分以外UFOの爆弾情報
					for (int j = 0; j < 3; j++) {
						if (BakudanWay[i][j] != 0) {
 							// 飛行中の1～8　と　消滅準備9の場合
							if (CrashCheck(	UfoX[UfoNo] + 5,	// UFOの中の方
											UfoY[UfoNo] + 5,
											UFOImageWidth - 5,
											UFOImageHeight - 5,
											BakudanX[i][j],
											BakudanY[i][j],
											BakudanImageWidth,
											BakudanImageHeight) == 1)
								// 自分のUFOの状態値を爆破初期値２にする
								MyUfoStatus = UfoStatus[UfoNo] = 2;
						}
					}
				}
			}
		}
		// 自分の爆弾と他のUFOの衝突チェック（当てたかのチェック）
		for (int i = 0; i < 3; i++) {
			if (BakudanWay[UfoNo][i] == 9)			// 爆弾消滅準備の場合
				BakudanWay[UfoNo][i] = 0;			// 爆弾消滅

			if (BakudanWay[UfoNo][i] != 0) {		// 発射されて飛んでいる場合
				for (int j = 0; j < UfoMax; j++) {	// 他のUFOとの衝突チェック
					// 自分自身以外でUFOが正常状態である場合
					if (j != UfoNo && UfoStatus[j] == 1) {
						// 自分が発射した爆弾がUFOに激突した場合
						if (CrashCheck(	UfoX[j] + 5,		// UFOの中の方
										UfoY[j] + 5,
										UFOImageWidth - 5,
										UFOImageHeight - 5,
										BakudanX[UfoNo][i],
										BakudanY[UfoNo][i],
										BakudanImageWidth,
										BakudanImageHeight) == 1)
							BakudanWay[UfoNo][i] = 9;		// 爆弾の消滅準備
					}
				}
			}
		}
	}
	// 爆弾が自分のUFOの範囲内かチェック -----------------------------------------------
	public int CrashCheck( int ux, int uy, int uw, int uh, 
 						int mx, int my, int mw, int mh) {
		int cx[ ] = new int[4];
		int cy[ ] = new int[4];
		int crash = 0;				// 当り判定(0:当たっていない，1:当たった)

		cx[0] = cx[3] = mx;   cx[1] = cx[2] = mx + mw;
		cy[0] = cy[1] = my;   cy[2] = cy[3] = my + mh;
		for (int i = 0; i < 4; i++) {
			// UFOと爆弾が重なった場合（衝突した場合）
			if (cx[i] > ux && cx[i] < ux + uw && cy[i] > uy && cy[i] < uy + uh)
				crash = 1;
		}
		return crash;
	}
	// スレッド設定・開始 ---------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);
		thread.start( );
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (BaseImage != null)				// BaseImageが準備ＯＫで設定されている場合
			g.drawImage(BaseImage, 0, 0, this);
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		int endswitch = 0;
		while (GameStartFlag == false) {
			try {
				thread.sleep(100);					// スレッドをスリープ
			} catch(InterruptedException e) {		// 他のスレッドの割り込み例外処理
				System.out.println(e);
			}
		}

		MakeBackground( );
		repaint( );

		while(true) {

			ReceiveGameInformation( );				// ゲーム情報受信

			try {
				thread.sleep(50);					// 受信待ちスリープ
			} catch(InterruptedException e) {		// 他のスレッドの割り込み例外処理
				System.out.println(e);
			}

			// ネットから取り込んだゲーム情報をメモリにセット
			GameInformationMemorySet( );

			if (ReceiveInformation.equals("Gameover")) {	// ゲームオーバーの場合
				NewGameInformation = "Gameover";
				if (AdministratorFlag == false)		// 管理者以外は
					SendGameInformation( );			// ゲームオーバー情報を送る

				try {
					thread.sleep(3000);				// 送信が完了するまで3秒スリープ
				} catch(InterruptedException e) {	// 他のスレッドの割り込み例外処理
					System.out.println(e);
				}
				endswitch = 1;
			}
			else									// ゲームオーバーでない
				GameInformationSet( );				// 現在の自分の情報をセット

			if (endswitch == 1)
				break;

			if (GameOverFlag == true && AdministratorFlag == true)
				NewGameInformation = "Gameover";

			// NetOutputで次のマシンに送る
			SendGameInformation( );

			if (GameOverFlag == false && AdministratorFlag == true) {	
				// アドミニはゲームオーバーチェック
				Existence[0] = Existence[1] = 0;
				for (int i = 0; i < UfoMax; i++) { // アドミニの情報だけでチェック
					if (UfoStatus[i] == 1)
						Existence[Team[i]] = Existence[Team[i]] + 1;
				}
 				// どちらかが存在していない場合
				if (Existence[0] == 0 || Existence[1] == 0) {
					GameOverFlag = true;
				}
			}

			UFOMoveProcess( );						// 自分のUFO移動処理
			BakudanMoveProcess( );					// 自分の爆弾移動処理
			GameScreenDisplay( );					// ゲーム画面表示
		}
	}
	// 自分のUFO移動処理 ---------------------------------------------------------------
	void UFOMoveProcess( ) {
		if (UfoX[UfoNo] < UfoXBase[UfoNo])
			UfoX[UfoNo] += 1;
		else if (UfoX[UfoNo] > UfoXBase[UfoNo])
			UfoX[UfoNo] -= 1;
		if (UfoY[UfoNo] < UfoYBase[UfoNo])
			UfoY[UfoNo] += 1;
		else if (UfoY[UfoNo] > UfoYBase[UfoNo])
			UfoY[UfoNo] -= 1;
	}
	// 自分の爆弾移動処理 ---------------------------------------------------------------
	void BakudanMoveProcess( ) {
		for (int i = 0; i < BakudanMax; i++) {
			switch (BakudanWay[UfoNo][i]) {
				case 1:								// North
					BakudanY[UfoNo][i] -= 3;
					break;
				case 2:								// North East
					BakudanX[UfoNo][i] += 3;
					BakudanY[UfoNo][i] -= 3;
					break;
				case 3:								// East
					BakudanX[UfoNo][i] += 3;
					break;
				case 4:								// South East
					BakudanX[UfoNo][i] += 3;
					BakudanY[UfoNo][i] += 3;
					break;
				case 5:								// South
					BakudanY[UfoNo][i] += 3;
					break;
				case 6:								// South West
					BakudanX[UfoNo][i] -= 3;
					BakudanY[UfoNo][i] += 3;
					break;
				case 7:								// West
					BakudanX[UfoNo][i] -= 3;
					break;
				case 8:								// North West
					BakudanX[UfoNo][i] -= 3;
					BakudanY[UfoNo][i] -= 3;
					break;
			}

			if (BakudanX[UfoNo][i] < 0	// Frameの外に出たか四方チェック
				|| BakudanX[UfoNo][i] > FrameWidth
				|| BakudanY[UfoNo][i] < 0
				|| BakudanY[UfoNo][i] > FrameHeight) {
					BakudanWay[UfoNo][i] = 0;
					BakudanX[UfoNo][i] = BakudanY[UfoNo][i] = 0;
			}
			
		}
	}
	// ゲーム画面表示 -------------------------------------------------------------------
	public void GameScreenDisplay( ) {
		MakeBackground( );
		for (int UFO = 0; UFO < UfoMax; UFO++) {
			if (UfoStatus[UFO] == 1)
				BaseGraphics.drawImage(UFOImage[Team[UFO]], 
						UfoX[UFO]-UFOImageWidth/2, UfoY[UFO] -UFOImageHeight/2, this);
			else if (UfoStatus[UFO] >= 2 && UfoStatus[UFO] <= 6)
				BaseGraphics.drawImage(ExplodeImage[UfoStatus[UFO]-2], 
						UfoX[UFO]-UFOImageWidth/2, UfoY[UFO] -UFOImageHeight/2,
						UFOImageWidth, UFOImageHeight, this);

			if (UfoStatus[UFO] != -1) {
				for (int i = 0; i < BakudanMax; i++) {
					if (BakudanWay[UFO][i] >= 1 && BakudanWay[UFO][i] <= 8)
						BaseGraphics.drawImage(BakudanImage,
							BakudanX[UFO][i]-BakudanImageWidth/2,
							BakudanY[UFO][i] -BakudanImageHeight/2, this);
				}
			}
		}
		repaint( );
	}
	// 背景表示 -------------------------------------------------------------------------
	void MakeBackground( ) {
		
		for (int i = 0; i < FrameHeight / BackImageHeight + 1; i++)
			for (int j = 0; j < FrameWidth / BackImageWidth + 1; j++)
				BaseGraphics.drawImage(BackImage,
					j * BackImageWidth, i * BackImageHeight, this);
	}
	// MouseListenerインタフェースの各メソッドを定義 ------------------------------------
    public void mousePressed( MouseEvent evt ) {
		// 左ボタン && チームが赤または青
		if ((evt.getModifiers( ) & evt.BUTTON1_MASK) != 0 
			&& BakudanStatus == 0 && MyUfoStatus == 1)
			BakudanStatus = 1;										// 爆弾発射
		else if ((evt.getModifiers( ) & evt.BUTTON3_MASK) != 0) {	// 右ボタン
			Direction[UfoNo]++;										// UFOの回転
			if (Direction[UfoNo] > 8)
				Direction[UfoNo] = 1;
		}
	}
    public void mouseReleased( MouseEvent evt ) {
		if (BakudanStatus == 1) {					// 爆弾発射の場合
			for (int i = 0; i < BakudanMax; i++) {

				if (BakudanWay[UfoNo][i] == 0) {	// 爆弾準備OK
					// 爆弾の発射初期位置設定
					BakudanX[UfoNo][i] = UfoX[UfoNo];
					BakudanY[UfoNo][i] = UfoY[UfoNo];
					// 他のルーチン内でBakudanWayを先にチェックする場合があるので,
					// ここでは、BakudanX Y の設定の後で方向設置する
					BakudanWay[UfoNo][i] = Direction[UfoNo];	// 爆弾の方向設定
					break;
				}
			}
			BakudanStatus = 0;						// 爆弾発射準備OK
		}
	}
    public void mouseClicked( MouseEvent evt ) {  }
    public void mouseEntered( MouseEvent evt ) {  }
    public void mouseExited( MouseEvent evt ) {  }
	// MouseMotionListenerインターフェースを実装 ----------------------------------------
    public void mouseMoved(MouseEvent evt) {
		UfoXBase[UfoNo] = evt.getX( );
		UfoYBase[UfoNo] = evt.getY( );
    }
    public void mouseDragged(MouseEvent evt) { }

	// 結果表示 -------------------------------------------------------------------------
	public void ResultDisplay( ) {
		BaseGraphics.setFont(new Font("TimesRoman", Font.BOLD, 50));

		// アドミニか，またはプレイヤーにてチェックを行う
		// ラストの各情報は異なる，共通点は，存在している方の状態には１がある
		Existence[0] = Existence[1] = 0;
		for (int i = 0; i < UfoMax; i++) {
			if (UfoStatus[i] == 1)					// 存在している場合
				Existence[Team[i]] = 1;
		}
		if (Existence[0] == 1) {					// Red team が存在
			BaseGraphics.setColor(Color.red);
			BaseGraphics.drawString("Red team win !!!", 50, 300);
		}
		else {										// Blue team が存在
			BaseGraphics.setColor(Color.blue);
			BaseGraphics.drawString("Blue team win !!!", 50, 300);
		}
		repaint( );
	}
	// スレッド終了 ---------------------------------------------------------------------
	public void stop( ) {
		thread = null;
	}
	// WindowListenerインターフェースの各メソッドを定義 ---------------------------------
	public void windowClosing(WindowEvent evt) {
		dispose( );  								// フレームの廃棄
	}
	public void windowClosed(WindowEvent evt) {
		System.exit(1);								// プログラム終了
	}
	public void windowOpened(WindowEvent evt) { }
	public void windowIconified(WindowEvent evt) { }
	public void windowDeiconified(WindowEvent evt) { }
	public void windowActivated(WindowEvent evt) { }
	public void windowDeactivated(WindowEvent evt) { }
}
