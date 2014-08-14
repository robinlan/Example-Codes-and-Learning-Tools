import java.applet.*;		// Applet, AudioClip
import java.awt.*;			// Graphics, Image, Color, MediaTracker, Button
import java.awt.event.*;	// KeyListener, KeyEvent, ActionListener, ActionEvent

public class Meiro extends Applet implements  Runnable, ActionListener, KeyListener {

	int GameWidth, GameHeight;						// ゲーム画面サイズ
	Thread thread = null;							// スレッド
	Image WorkImage, MeiroImage;					// 作業用と迷路イメージ
	Graphics WorkGraphics, MeiroGraphics;			// 作業用と迷路グラフィックス

	Image TitleImage;								// タイトルイメージ
	Image PenguinImage[ ] = new Image[8];			// ペンギンの歩行画像
	Image LionImage[ ] = new Image[8];				// ライオンの歩行画像
	Image block, takara, egg;						// 壁と宝物とたまご

	int TateMax = 25;								// 迷路の大きさ（奇数）
	int YokoMax = 25;
	int Map[ ][ ] = new int[TateMax][YokoMax];		// 迷路のマップデータ
	int Size;										// 壁のサイズ（他の画像も同じサイズ）

	int DispBaseX, DispBaseY;						// 表示ベースポジション	

	Penguin penguin;								// ペンギンオブジェクト
	int LionMax = 5;								// ライオンの数
	Lion lion[ ] = new Lion[LionMax];				// ライオンオブジェクト

	boolean ON = true, OFF = false;					// 論理定数
	boolean GameStart = OFF;						// ゲームスタートフラグ
	boolean GameClear = OFF;						// ゲームクリア
	boolean GameOver = OFF;							// ゲームオーバー
	int CloseCount = 0;								// 画面をクローズするカウント

	Button StartButton;								// スタートボタン

	AudioClip BGMSound;								// BGM

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		GameWidth = getSize( ).width;				// ゲーム画面サイズ
		GameHeight = getSize( ).height;

		WorkImage = createImage(GameWidth, GameHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );		// 作業用グラフィックス取得

		DataLoad( );								// データロード

		Size = block.getWidth(this);				// 壁サイズ

		MeiroImage = createImage(YokoMax * Size, TateMax * Size);	// 迷路イメージ作成
		MeiroGraphics = MeiroImage.getGraphics( );	// 迷路グラフィックス取得

		setLayout(null);							// レイアウトを自由設定
		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー追加
		StartButton.setBounds(GameWidth/2-30, GameHeight-40, 60, 30);

		// ペンギン生成
		penguin = new Penguin(PenguinImage, Size, this);
		// ライオン生成
		for (int i = 0; i < LionMax; i++)
			lion[i] = new Lion(LionImage, Size, penguin, this);

		addKeyListener(this);						// リスナー追加
		requestFocus( );							// キー入力フォーカスを要求
	}
	// データロード ---------------------------------------------------------------------
	public void DataLoad( ) {
		BGMSound = getAudioClip(getCodeBase( ), "sound/bgm.au");

		MediaTracker mt = new MediaTracker(this);
		TitleImage = getImage(getCodeBase( ), "image/title.jpg");// タイトル画像入力
		mt.addImage(TitleImage, 0);					// メディアトラッカにセット

		for (int i = 0; i < 8; i++) {				// 8種類の歩行画像
			PenguinImage[i] = getImage(getCodeBase( ), "image/penguin"+i+".gif");
													// ペンギン歩行画像



			mt.addImage(PenguinImage[i], 0);
			LionImage[i] = getImage(getCodeBase( ), "image/lion"+i+".gif");
													// ライオン歩行画像



			mt.addImage(LionImage[i], 0);
		}
		block = getImage(getCodeBase( ), "image/block.gif");	// 壁画像


		mt.addImage(block, 0);
		takara = getImage(getCodeBase( ), "image/takara.gif");	// 宝物画像


		mt.addImage(takara, 0);
		egg = getImage(getCodeBase( ), "image/egg.gif");		// たまご画像

		mt.addImage(egg, 0);

		try {
			mt.waitForID(0);						// イメージ画像の入力完了を待つ
		} catch(InterruptedException e) {
			showStatus(" "+e);
		}
	}
	// ゲーム初期化 ---------------------------------------------------------------------
	private void GameInitialize( ) {
			MakeMeiro( );							// 迷路作成
			DispBaseX = DispBaseY = 0;				// 表示基本位置
			GameStart = ON;							// ゲームスタートをオン
			GameClear = OFF;						// ゲームクリアをオフ
			GameOver = OFF;							// ゲームオーバーをオフ
			requestFocus( );						// キー入力フォーカスを要求
			penguin.initialize( );					// ペンギン初期化
			for (int i = 0; i < LionMax; i++)
				lion[i].intialize ( );				// ライオン初期化処理
			BGMSound.loop( );						// BGM
			StartButton.setVisible(OFF);			// スタートボタンを隠す
	}
	// 迷路マップ作成 -------------------------------------------------------------------
	private void MakeMeiro( ) {
		// マップを空白でクリア
		for (int Tate = 0; Tate < TateMax; Tate++)
			for (int Yoko = 0; Yoko < YokoMax; Yoko++)
				Map[Tate][Yoko] = 0;				// 空白クリア
		// 外周の壁作成
		for (int i = 0; i < YokoMax; i++) {			// 横壁作成
			Map[0][i] = 1;							// 上の段の横の壁
			Map[TateMax-1][i] = 1;					// 下の段の横の壁
		}
		for (int i = 0; i < TateMax; i++) {			// 縦壁
			Map[i][0] = 1; 							// 左の縦壁
			Map[i][YokoMax-1] = 1;					// 右の縦壁
		}
		// 規定の内壁と連結する壁を作成
		for (int Yoko = 2; Yoko < YokoMax - 2; Yoko += 2) {
			for (int Tate = 2; Tate < TateMax - 2; Tate += 2) {
				Map[Tate][Yoko] = 1;				// 規定の内壁

				// 内壁から4方向に壁を置く
				// その１　置く位置に壁があれば，置けるところ探して置く
				//       この場合は，常に一通りしかない迷路ができあがります。
				// 　　　言いかえると「右手法で必ず解ける迷路」となります。
				int flag = 0;						// 置けるかのチェックフラグ
				while (flag == 0) {					// 壁が置けない間，繰り返す
					// 四方の方向をランダムに発生　0:上  1:右  2:下  3:左
					int w = (int)(Math.random( )*4);

					switch  (w) {
						case 0: if (Map[Tate-1][Yoko] == 0) {// 上方向の位置が空白の場合
									Map[Tate-1][Yoko] = 1;		// 壁を置く
									flag = 1;					// 置くことができた
								}
							    break;
						case 1: if (Map[Tate][Yoko+1] == 0) {// 右方向の位置が空白の場合
									Map[Tate][Yoko+1] = 1;		// 壁を置く
									flag = 1;					// 置くことができた
								}
							    break;
						case 2: if (Map[Tate+1][Yoko] == 0) {// 下方向の位置が空白の場合
									Map[Tate+1][Yoko] = 1;		// 壁を置く
									flag = 1;					// 置くことができた
								}
							    break;
						case 3: if (Map[Tate][Yoko-1] == 0) {// 左方向の位置が空白の場合
									Map[Tate][Yoko-1] = 1;		// 壁を置く
									flag = 1;					// 置くことができた
								}
							    break;
					}
				}
				/* ======================================================================
				// その２　四方ランダムに壁を置く。２重に置く場合が生じる。
				// 　　　この場合は，空間が生まれ，何通りかの迷路ができる。
				// 四方の方向をランダムに発生　0:上  1:右  2:下  3:左
				int w = (int)(Math.random( )*4);
				switch  (w) {
					case 0: Map[Tate-1][Yoko] = 1;	// 上方向の位置を壁にする
						    break;
					case 1: Map[Tate][Yoko+1] = 1;	// 右方向の位置を壁にする
						    break;
					case 2: Map[Tate+1][Yoko] = 1;	// 下方向の位置を壁にする
						    break;
					case 3: Map[Tate][Yoko-1] = 1;	// 左方向の位置を壁にする
						    break;
				}
			========================================================================== */
			}
		}

		// 宝物を置く
		Map[TateMax-2][YokoMax-2] = 2;				// 迷路の右下に宝物を置く

		// 迷路画像作成
		MeiroGraphics.setColor(Color.white);		// 白色で塗りつぶす
		MeiroGraphics.fillRect(0, 0, YokoMax*Size, TateMax*Size);
		for (int Tate = 0; Tate < TateMax; Tate++) {		// 縦方向ループ
			for (int Yoko = 0; Yoko < YokoMax; Yoko++) {	// 横方向ループ
				if (Map[Tate][Yoko] == 1)			// 壁の場合　壁描画
					MeiroGraphics.drawImage(block, Yoko*Size, Tate*Size, this);
				else if (Map[Tate][Yoko] == 2)		// 宝物の場合　宝物描画
					MeiroGraphics.drawImage(takara, Yoko*Size, Tate*Size, this);
			}
		}
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (GameStart == OFF)						// ゲームがスタートされていない場合
			// タイトル描画
			WorkGraphics.drawImage(TitleImage, 0, 0, GameWidth, GameHeight, this);
		else if (GameOver == ON) {					// ゲームオーバー
			// タイトル描画
			WorkGraphics.drawImage(TitleImage, 0, 0, GameWidth, GameHeight, this);
			// 画面を閉じていく処理
			if (!(GameWidth - CloseCount*2 <= 0
				|| GameHeight - CloseCount*2 <= 0)) {
				// クリップ領域設定
				WorkGraphics.clipRect(CloseCount, CloseCount,
					GameWidth - CloseCount*2, GameHeight - CloseCount*2);

				gameprocess( );						// ゲームプロセス

				WorkGraphics = WorkImage.getGraphics( ); // 再度グラフィックス取得
				CloseCount += 3;					// 閉じていく幅を増やす
			}
			else {									// 画面のクローズ完了
				GameStart = OFF;					// ゲームスタート　オフ
				StartButton.setVisible(ON);			// スタートボタンを表示
			}
		}
		else {										// ゲーム中
			gameprocess( );							// ゲームプロセス
		}
		WorkGraphics.setColor(Color.black);
		WorkGraphics.drawRect(0, 0, GameWidth-1, GameHeight-1);	// 外枠作成

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// ゲームプロセス -------------------------------------------------------------------
	public void gameprocess( ) {
			WorkGraphics.drawImage(MeiroImage, DispBaseX, DispBaseY, this);	// 迷路画面
			penguin.update(WorkGraphics);			// ペンギン描画
			if (GameClear == OFF) {					// ゲームクリアしていない
				for (int i = 0; i < LionMax; i++)
					lion[i].paint(WorkGraphics);	// ライオン描画
			}
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			repaint( );								// 再描画
			if (GameClear == OFF) {					// ゲームクリアしていない
				for (int i = 0; i < LionMax; i++)
					lion[i].move( );				// ライオン移動
			}
			try {
				thread.sleep(100);					// スレッドスリープ : ゲームスピード
													// つまりライオンのスピード
			} catch (InterruptedException e){		// 他のスレッドの割り込み例外処理
				showStatus(" " + e);				// 例外エラー表示
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// ゲームオーバー -------------------------------------------------------------------
	public void gameover( ) {
		GameOver = ON;								// ゲームオーバー
		CloseCount = 0;								// 画面を閉じるカウント用
		BGMSound.stop( );							// BGM
	}
	// ゲームクリア ---------------------------------------------------------------------
	public void gameclear( ) {
		GameClear = ON;								// ゲームクリア
		StartButton.setVisible(ON);					// スタートボタンを隠す
		BGMSound.stop( );							// BGM
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
		BGMSound.stop( );							// BGM
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button button = (Button)evt.getSource( );
		if (button == StartButton) {				// スタートボタンの場合
			GameInitialize( );						// ゲーム初期化
		}
	}
	// KeyListenerインタフェースの各メソッドを定義 --------------------------------------
	public void keyPressed(KeyEvent evt) {
		if (GameOver == ON)							// ゲームオーバーの場合
			return;
		// ペンギンを動かす
		switch (evt.getKeyCode( )) {				// 入力キーの値で分岐
		    case KeyEvent.VK_DOWN  :				// 下キー
				// 違う方向を向いているときは，方向転換
				if (penguin.Status != 0)			// 下方向を向いていない場合
					penguin.Status = 0;				// ペンギンの方向を下方向にする
				// 道の真中で，下が壁でなければ進む
				else if (penguin.X % Size == 0
					 && Map[(penguin.Y+Size)/Size][penguin.X/Size] != 1) {
					penguin.Y += penguin.Step;		// 下に進む
					// 表示ベースポイントを変化させる
					// y方向の表示基準位置がまだ上方向に行くことができ，
					// かつ基準位置から現在の位置Yがアプレットの2分の1より大きい場合
					if (DispBaseY > -TateMax*Size+GameHeight
					 && penguin.Y + DispBaseY > GameHeight / 2)
						DispBaseY -= penguin.Step;	// 表示基準位置をStep分上に移動
													// このことによって迷路の下方が表示
				}
				break;
	        case KeyEvent.VK_UP    :				// 上キー
				if (penguin.Status != 1)			// 上方向を向いていない場合
					penguin.Status = 1;				// ペンギンの方向を上方向にする
				// 道の真中で，上が壁でなければ進む
				else if (penguin.X % Size == 0 &&
						Map[(penguin.Y-penguin.Step)/Size][penguin.X/Size] != 1) {
					penguin.Y -= penguin.Step;		// 上に進む
					// 表示ベースポイントを変化させる
					if (DispBaseY < 0 && penguin.Y < TateMax*Size - GameHeight / 2)
						DispBaseY += penguin.Step;	// 表示基準位置をStep分下に移動
				}
				break;
		    case KeyEvent.VK_RIGHT : 				// 右キー
				if (penguin.Status != 2)			// 右方向を向いていない場合
					penguin.Status = 2;				// ペンギンの方向を右方向にする
				// 道の真中で，右が壁でなければ進む
				else if (penguin.Y % Size == 0 && 
						Map[penguin.Y/Size][(penguin.X+Size)/Size] != 1) { 
					penguin.X += penguin.Step;		// 右に進む
					// 表示ベースポイントを変化させる
					if (DispBaseX > -YokoMax*Size+GameWidth
					 && penguin.X + DispBaseX > GameWidth / 2)
						DispBaseX -= penguin.Step;	// 表示基準位置をStep分左に移動
				}
				break;
	        case KeyEvent.VK_LEFT  : 				// 左キー
				if (penguin.Status != 3)			// 左方向を向いていない場合
					penguin.Status = 3;				// ペンギンの方向を左方向にする
				// 道の真中で，左が壁でなければ進む
				else if (penguin.Y % Size == 0 &&
						Map[penguin.Y/Size][(penguin.X-penguin.Step)/Size] != 1) {
					penguin.X -= penguin.Step;		// 左に進む
					// 表示ベースポイントを変化させる
					if (DispBaseX < 0 && penguin.X < YokoMax*Size - GameWidth / 2)
						DispBaseX += penguin.Step;	// 表示基準位置をStep分右に移動
				}
				break;
	    }
		repaint( );
	}
	public void keyReleased(KeyEvent evt) { }
	public void keyTyped(KeyEvent evt) { }
}

// ======================================================================================
// ペンギンクラス
class Penguin extends Applet {
	Applet applet;									// 表示アプレット
	static Image PenguinImage[ ] = new Image[6];	// ペンギンイメージ
	int X, Y;										// ペンギンの位置
	int Step;										// 移動ステップ
	int Status;										// 状態　0↓  1↑  2→  3←
	int Change;										// アニメーション変化用
	int Size;										// 迷路の壁サイズ

	Meiro meiro;									// 親クラス

	// コンストラクタ -------------------------------------------------------------------
	public Penguin(Image PenguinImage[ ], int Size, Applet applet) {
		this.PenguinImage = PenguinImage;			// ペンギンイメージ
		this.applet = applet;						// アプレット
		this.Size = Size;
		meiro = (Meiro)applet;						// 親クラス
	}
	// イニシャライズ -------------------------------------------------------------------
	public void initialize( ) {
		X = Y = Size;								// ペンギンの初期位置
		Status = 0;									// 状態　0↓  1↑  2→  3←
		Change = 0;									// アニメーション変化用
		Step = Size / 10;							// スムーズ移動間隔(Size 壁サイズ)
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		Change++;									// ペンギンアニメーションの状態を変化

 		// 作業用グラフィックにペンギン描画
		g.drawImage(PenguinImage[Status * 2 + Change % 2],
					X + meiro.DispBaseX, Y + meiro.DispBaseY, this);

		// たまごをマップに登録し，迷路グラフィックにたまごを描画
		if (X % Size == 0 && Y % Size == 0 && meiro.Map[Y / Size][X / Size] == 0) {
			// 迷路の各ブロックのちょうどの位置で何もない場合
			meiro.Map[Y / Size][X / Size] = 3;
			meiro.MeiroGraphics.drawImage(meiro.egg,
								 X / Size * Size, Y / Size * Size, applet);
		}

		// 宝物に到着
		if (meiro.Map[Y / Size][X / Size] == 2)		// 宝物に到着
			meiro.gameclear( );						// ゲームクリア
	}
}

// ======================================================================================
// ライオンクラス
class Lion {
	Applet applet;									// アプレット
	static Image LionImage[ ] = new Image[8];		// ライオンイメージ
	int X, Y;										// ライオンの位置
	int Status;										// 状態　0↓  1↑  2→  3←
	int Change;										// アニメーション変化用
	int Step;										// 移動ステップ
	int Size;										// 壁サイズ
	Meiro meiro;									// 親クラス
	Penguin penguin;

	// コンストラクタ -------------------------------------------------------------------
	public Lion(Image LionImage[ ], int Size, Penguin penguin, Applet applet) {
		this.LionImage = LionImage;					// ライオンイメージ
		this.Size = Size;							// 壁サイズ
		this.penguin = penguin;						// ペンギン
		this.applet = applet;						// アプレット
		meiro = (Meiro)applet;						// 親クラス
	}
	// イニシャライズ -------------------------------------------------------------------
	public void intialize( ) {
		X = (meiro.YokoMax - 2) * Size;				// ライオンの位置　宝物と同じ位置
		Y = (meiro.TateMax - 2) * Size;
		Status = (int)(Math.random( )*4);			// ライオンの状態  0北  1東 2南  3西
		Step = Size / 10;							// スムーズ移動間隔(Size 壁サイズ)
		Change = 0;									// アニメーション変化用
	}
	// 移動処理 -------------------------------------------------------------------------
	public void move( ) {
		int flag = 0;								// 行けるかどうかのチェック用フラグ
		switch (Status) {							// 0↓  1↑  2→  3←
			case 0:	// 0↓　ライオンは下方向
				// 0↓  2→  3←  何れか進行可能か調べる
				// ライオンのグラフィック上の位置を配列上での位置に変換して壁のチェック
				if (meiro.Map[(Y+Size)/Size][X/Size] != 1	// 下が壁でない
				 || meiro.Map[Y/Size][(X+Size)/Size] != 1	// 右が壁でない
				 || meiro.Map[Y/Size][(X-Step)/Size] != 1)	// 左が壁でない
					{
					// 進行方向の下が壁でなく，移動が配列の１マスの途中段階の場合
					if (meiro.Map[(Y+Size)/Size][X/Size] != 1
					 && Y % Size != 0) {
						flag = 1;					// 行けるフラグをオン
						Y += Step;					// 下方向に進む
						Status = 0;					// ライオンの状態を下方向にする
					}
					// 現在ライオンは下方向を向いているので下方向を先に判断
					// 下方向にたまごがある場合
					else if (meiro.Map[(Y+Size)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y += Step;					// 下方向に進む
						Status = 0;					// ライオンの状態を下方向にする
					}
					// 右方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X+Size)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X += Step;					// 右方向に進む
						Status = 2;					// ライオンの状態を右方向にする
					}
					// 左方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X-Step)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X -= Step;					// 左方向に進む
						Status = 3;					// ライオンの状態を左方向にする
					}
					while (flag == 0) {				// まだ行く方向が決まっていない場合
						// 下方向が壁でなくランダム確率2分の1でOKの場合
						if (meiro.Map[(Y+Size)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y += Step;				// 下方向に進む
							Status = 0;				// ライオンの状態を下方向にする
						}
						// 右方向が壁でなくランダム確率2分の1でOKの場合
						else if (meiro.Map[Y/Size][(X+Size)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X += Step;				// 右方向に進む
							Status = 2;				// ライオンの状態を右方向にする
						}
						// 左方向が壁でなくランダム確率2分の1でOKの場合
						else if (meiro.Map[Y/Size][(X-Step)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X -= Step;				// 左方向に進む
							Status = 3;				// ライオンの状態を左方向にする
						}
					}
				}
				else {			// 0↓  2→  3←  何れも進行できない場合　1↑	バック
					flag = 1;						// 行けるフラグをオン
					Y -= Step;						// 上方向に進む
					Status = 1;						// ライオンの状態を上方向にする
				}
				break;

			case 1:	// 1↑　ライオンは上方向
				// 1↑  2→  3←  何れか進行可能か調べる
				if (meiro.Map[(Y-Step)/Size][X/Size] != 1	// 上が壁でない
				 || meiro.Map[Y/Size][(X+Size)/Size] != 1	// 右が壁でない
				 || meiro.Map[Y/Size][(X-Step)/Size] != 1)	// 左が壁でない
					{
					// 進行方向の上が壁でなく，移動が配列の１マスの途中段階の場合
					if (meiro.Map[(Y-Step)/Size][X/Size] != 1
					 && Y % Size != 0) {
						flag = 1;					// 行けるフラグをオン
						Y -= Step;					// 上方向に進む
						Status = 1;					// ライオンの状態を上方向にする
					}
					// 現在ライオンは上方向を向いているので上方向を先に判断
					// 上方向にたまごがある場合
					else if (meiro.Map[(Y-Step)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y -= Step;					// 上方向に進む
						Status = 1;					// ライオンの状態を上方向にする
					}
					// 右方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X+Size)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X += Step;					// 右方向に進む
						Status = 2;					// ライオンの状態を右方向にする
					}
					// 左方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X-Step)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X -= Step;					// 左方向に進む
						Status = 3;					// ライオンの状態を左方向にする
					}
					// 進行方向が定まらない場合
					while (flag == 0) {
						// 上方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						if (meiro.Map[(Y-Step)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y -= Step;				// 上方向に進む
							Status = 1;				// ライオンの状態を上方向にする
						}
						// 右方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[Y/Size][(X+Size)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X += Step;				// 右方向に進む
							Status = 2;				// ライオンの状態を右方向にする
						}
						// 左方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[Y/Size][(X-Step)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X -= Step;				// 左方向に進む
							Status = 3;				// ライオンの状態を左方向にする
						}
					}
				}
				else {	// 0↓	上・左右に行けない場合，バックする
					flag = 1;						// 行けるフラグをオン
					Y += Step;						// 下方向に進む
					Status = 0;						// ライオンの状態を下方向にする
				}
				break;

			case 2:	// 2→　ライオンは右方向 
				// 2→  0↓　1↑　何れか進行可能か調べる
				if (meiro.Map[Y/Size][(X+Size)/Size] != 1		// 右が壁でない
				 || meiro.Map[(Y+Size)/Size][X/Size] != 1		// 下が壁でない
				 || meiro.Map[(Y-Step)/Size][X/Size] != 1) {	// 上が壁でない
					// 進行方向の右が壁でなく，移動が配列の１マスの途中段階の場合
					if (meiro.Map[Y/Size][(X+Size)/Size] != 1
					 && X % Size != 0) {
						flag = 1;					// 行けるフラグをオン
						X += Step;					// 右方向に進む
						Status = 2;					// ライオンの状態を右方向にする
					}
					// 右方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X+Size)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X += Step;					// 右方向に進む
						Status = 2;					// ライオンの状態を右方向にする
					}
					// 下方向にたまごがある場合
					else if (meiro.Map[(Y+Size)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y += Step;					// 下方向に進む
						Status = 0;					// ライオンの状態を下方向にする
					}
					// 上方向にたまごがある場合
					else if (meiro.Map[(Y-Step)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y -= Step;					// 上方向に進む
						Status = 1;					// ライオンの状態を上方向にする
					}
					// 進行方向が定まらない場合
					while (flag == 0) {
						// 右方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						if (meiro.Map[Y/Size][(X+Size)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X += Step;				// 右方向に進む
							Status = 2;				// ライオンの状態を右方向にする
						}
						// 下方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[(Y+Size)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y += Step;				// 下方向に進む
							Status = 0;				// ライオンの状態を下方向にする
						}
						// 上方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[(Y-Step)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y -= Step;				// 上方向に進む
							Status = 1;				// ライオンの状態を上方向にする
						}
					}
				}
				else {	// 3←　　右・上下に行けない場合，バックする
					flag = 1;						// 行けるフラグをオン
					X -= Step;						// 左方向に進む
					Status = 3;						// ライオンの状態を左方向にする
				}
				break;

			case 3:	// 3←　ライオンは左方向
				// 3←  0↓  1↑　何れか進行可能か調べる
				if (meiro.Map[Y/Size][(X-Step)/Size] != 1		// 左が壁でない
				 || meiro.Map[(Y+Size)/Size][X/Size] != 1		// 下が壁でない
				 || meiro.Map[(Y-Step)/Size][X/Size] != 1) {	// 上が壁でない
					// 進行方向の左が壁でなく，移動が配列の１マスの途中段階の場合
					if (meiro.Map[Y/Size][(X-Step)/Size] != 1
					 && X % Size != 0) {
						flag = 1;					// 行けるフラグをオン
						X -= Step;					// 左方向に進む
						Status = 3;					// ライオンの状態を左方向にする
					}
					// 左方向にたまごがある場合
					else if (meiro.Map[Y/Size][(X-Step)/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						X -= Step;					// 左方向に進む
						Status = 3;					// ライオンの状態を左方向にする
					}
					// 下方向にたまごがある場合
					else if (meiro.Map[(Y+Size)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y += Step;					// 下方向に進む
						Status = 0;					// ライオンの状態を下方向にする
					}
					// 上方向にたまごがある場合
					else if (meiro.Map[(Y-Step)/Size][X/Size] == 3) {
						flag = 1;					// 行けるフラグをオン
						Y -= Step;					// 上方向に進む
						Status = 1;					// ライオンの状態を上方向にする
					}
					// 進行方向が定まらない場合
					while (flag == 0) {
						// 左方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						if (meiro.Map[Y/Size][(X-Step)/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							X -= Step;				// 左方向に進む
							Status = 3;				// ライオンの状態を左方向にする
						}
						// 下方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[(Y+Size)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y += Step;				// 下方向に進む
							Status = 0;				// ライオンの状態を下方向にする
						}
						// 上方向が壁でなく，かつ2分の1の確率でランダム発生が0の場合
						else if (meiro.Map[(Y-Step)/Size][X/Size] != 1
						 && (int)(Math.random( )*2) == 0) {
							flag = 1;				// 行けるフラグをオン
							Y -= Step;				// 上方向に進む
							Status = 1;				// ライオンの状態を上方向にする
						}
					}
				}
				else {	// 2→　左・上下に行けない場合，バックする
					flag = 1;						// 行けるフラグをオン
					X += Step;						// 右方向に進む
					Status = 2;						// ライオンの状態を右方向にする
				}
				break;
			
		}
		Change++;									// アニメーション変化用

		if (meiro.Map[Y/Size][X/Size] == 3) {		// たまごの場合
			meiro.Map[Y/Size][X/Size] = 0;			// マップ上からたまごを消す
			meiro.MeiroGraphics.setColor(Color.white);	// 白色で塗りつぶす
			meiro.MeiroGraphics.fillRect(X/Size*Size, Y/Size*Size, Size, Size);
		}
		// ペンギンと同じ位置の場合
		if (penguin.X / Size == X / Size && penguin.Y / Size == Y / Size
			&& meiro.GameOver == false)
			meiro.gameover( );						// ゲームオーバー
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(LionImage[Status * 2 + Change % 2], 
					meiro.DispBaseX + X, meiro.DispBaseY + Y, applet);
	}
}
