import java.applet.*;		// Applet, AudioClip
import java.awt.*;			// Graphics, Image, Color, Font, MediaTracker, Button
import java.awt.event.*;	// ActionListener, ActionEvent, MouseListener, MouseEvent etc
import java.util.*;			// Date

public class Shooting extends Applet
		implements Runnable, ActionListener, MouseListener, MouseMotionListener {

	int GameWidth, GameHeight;						// ゲーム画面サイズ
	int Score = -1;									// スコア（まだしていない場合）
	Thread thread = null;							// スレッド

	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	Image TitleImage;								// タイトルイメージ
	Image BackgroundImage[ ] = new Image[2];		// 背景イメージ

	Image UFOImage[ ] = new Image[6];				// UFO回転イメージ

	Image TekiImage[ ] = new Image[5];				// ５種類の敵イメージ
	Image MissileImage[ ] = new Image[5];			// ５種類のミサイルイメージ
	Image ExplosionImage[ ] = new Image[6];			// 爆発イメージ

	AudioClip MissileSound[ ] = new AudioClip[5];	// ミサイルの発射音
	AudioClip ExplosionSound[ ] = new AudioClip[5];	// 爆発音

	Background background;							// 背景オブジェクト
	UFO ufo;										// UFOオブジェクト
	TekiManeger tekiManeger;						// 敵マネージャ（敵制御）
	MissileManeger missileManeger;					// ミサイルマネージャ（ミサイル制御）

	boolean ON = true, OFF = false;					// 論理定数
	boolean GameStart = OFF;						// ゲームスタートフラグ
	boolean GameOver = OFF;							// ゲームオーバー
	int CloseCount = 0;								// 画面をクローズするカウント

	Button StartButton;								// スタートボタン

	Font font = new Font("Courier", Font.BOLD, 20);	// フォント定義

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		GameWidth = getSize( ).width;				// ゲーム画面サイズ
		GameHeight = getSize( ).height;

		WorkImage = createImage(GameWidth, GameHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );	// 作業用グラフィックス取得
		WorkGraphics.setFont(font);					// フォント設定
		WorkGraphics.setColor(Color.white);			// 表示色

		setLayout(null);							// レイアウトを自由設定
		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー追加
		StartButton.setBounds(GameWidth/2-30, GameHeight-40, 60, 30);

		DataLoad( );								// データロード

		// 背景オブジェクト
		background = new Background(BackgroundImage, GameWidth, GameHeight, this);
		// UFOオブジェクト
		ufo = new UFO(UFOImage, ExplosionImage, ExplosionSound,
						GameWidth, GameHeight, this);
		// 敵マネージャ
		tekiManeger = 
			new TekiManeger(TekiImage, ExplosionImage, ExplosionSound, ufo, this);
		// ミサイルマネージャ
		missileManeger = 
			new MissileManeger(MissileImage, MissileSound,	tekiManeger, ufo, this);

		tekiManeger.information(missileManeger);	// 敵マネージャにミサイル情報

		addMouseListener(this);						// マウスリスナー追加
		addMouseMotionListener(this);				// マウスモーションリスナー追加
	}
	// データロード ---------------------------------------------------------------------
	public void DataLoad( ) {
		for (int i= 0; i < 5; i++) {
			MissileSound[i] = getAudioClip(getCodeBase( ),
				"sound/missile" + i + ".au");		// ミサイルの発射音入力
			ExplosionSound[i] = getAudioClip(getCodeBase( ),
				"sound/explosion" + i + ".au");		// 爆発音入力
		}

		MediaTracker mt = new MediaTracker(this);

		TitleImage = getImage(getCodeBase( ), "image/title.jpg");// タイトル画像入力
		mt.addImage(TitleImage, 1);					// メディアトラッカにセット

		for (int i = 0; i < 2; i++) {				// 背景イメージ入力
			BackgroundImage[i] = getImage(getCodeBase( ), "image/tree" + i + ".gif");
			mt.addImage(BackgroundImage[i], 1);		// メディアトラッカにセット
		}

		for (int i = 0; i < 6; i++) {				// 回転UFOイメージ入力
			UFOImage[i] = getImage(getCodeBase( ), "image/ufo" + i + ".gif");
			mt.addImage(UFOImage[i], 1);			// メディアトラッカにセット
		}

		for (int i = 0; i < 5; i++) {			  	// 敵とミサイルイメージ入力
			TekiImage[i] = getImage(getCodeBase( ), "image/teki" + i + ".gif");
			mt.addImage(TekiImage[i], 1);
			MissileImage[i] = getImage(getCodeBase( ), "image/missile"+i+".gif");
			mt.addImage(MissileImage[i], 1);
		}

		for (int i = 0; i < 6; i++) {				// 爆発イメージ入力
			ExplosionImage[i] = getImage(getCodeBase( ), "image/explosion" + i + ".gif");
			mt.addImage(ExplosionImage[i], 1);		// メディアトラッカにセット
		}

		try {
			mt.waitForAll( );						// 入力完了待ち
		} catch(InterruptedException e) {
		}
	}
	// ゲーム初期化 ---------------------------------------------------------------------
	private void GameInitialize( ) {
		GameStart = ON;								// ゲームスタートをオン
		GameOver = OFF;								// ゲームオーバーをオフ
		Score = 0;									// スコア
		ufo.initialize( );							// UFO初期化
		WorkGraphics.setFont(font);					// フォント設定
		WorkGraphics.setColor(Color.white);			// 表示色
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (GameStart == OFF) {						// ゲームが開始されていない場合
			// タイトル描画
			WorkGraphics.drawImage(TitleImage, 0, 0, GameWidth, GameHeight, this);
			if (Score != -1)
				WorkGraphics.drawString("SCORE:" + Score, 5, 25);	// スコア表示
		}
		else if (GameOver == ON) {					// ゲームオーバーの場合
			// タイトル描画
			WorkGraphics.drawImage(TitleImage, 0, 0, GameWidth, GameHeight, this);
			// 画面を閉じていく
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
				StartButton.setVisible(true);		// スタートボタンを表示
			}
		}
		else {										// ゲーム中の場合
			gameprocess( );							// ゲームプロセス
		}

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// ゲームプロセス -------------------------------------------------------------------
	public void gameprocess( ) {
		background.update(WorkGraphics);			// 背景更新
		tekiManeger.update(WorkGraphics);			// 敵マネージャが複数の敵を更新
		missileManeger.update(WorkGraphics);		// ミサイルマネージャがミサイル更新
		ufo.update(WorkGraphics);					// UFO更新
		WorkGraphics.setColor(Color.white);			// スコア表示
		WorkGraphics.drawString("SCORE:" + Score, 5, 25);
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			repaint( );								// 再描画
			try {
				thread.sleep(50);					// スレッドスリープ
			} catch (InterruptedException e){		// 他のスレッドの割り込み例外処理
				showStatus(" " + e);				// 例外エラー表示
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
	}
	// ゲームオーバー -------------------------------------------------------------------
	public void gameover( ) {
		GameOver = ON;								// ゲームオーバー
		CloseCount = 0;								// 画面を閉じるカウント用
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button button = (Button)evt.getSource( );
		if (button == StartButton) {				// スタートボタンの場合
			StartButton.setVisible(false);			// スタートボタンを隠す
			GameInitialize( );						// ゲーム初期化
		}
	}
	// MouseListenerインターフェースのメソッド定義 --------------------------------------
    public void mousePressed(MouseEvent evt) {		// マウスダウン処理
		if (GameStart == true)
			missileManeger.shoot( );				// ミサイル発射
	}
    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
	// MouseMotionListenerインターフェースのメソッド定義 --------------------------------
    public void mouseMoved(MouseEvent evt) {
		if (GameStart == true)
		ufo.MouseLocate(evt.getX( ), evt.getY( ));	// マウスの位置をUFOに送る
	}
    public void mouseDragged(MouseEvent evt) {
	}
}

// ======================================================================================
// 背景クラス
class Background extends Applet {
	Applet applet;									// 表示アプレット(フレーム)
	int GameWidth, GameHeight;						// ゲーム画面サイズ

	static Image Tree[] = new Image[2];				// 木の画像
	int TreeWidth, TreeHeight;						// 木のサイズ
	int TreeMax = 100;								// 木の数
	int TreeType[] = new int[TreeMax];				// 木のタイプ
	Point TreePoint[] = new Point[TreeMax]; 		// 木の位置

	int MissyuCount = 0;							// 密集カウント
	int MissyuMax = 0;								// 密集最大数
	int MissyuKankaku = 0;							// 密集間隔

	int ScrollStep = 3;								// スクロールステップ

	// コンストラクタ ------------------------------------------------------------------
	public Background(Image BackgroundImage[ ],int GameWidth, int GameHeight,
						 Applet applet) {

		Tree = BackgroundImage;						// 木のイメージ
		this.GameWidth = GameWidth;					// ゲーム画面サイズ
		this.GameHeight = GameHeight;
		this.applet = applet;						// アプレット

		TreeWidth = Tree[0].getWidth(applet);		// 木の幅
		TreeHeight = Tree[0].getHeight(applet);		// 木の高さ

		MakeTreePoint( );							// 木の位置設定
	}
	// 木の位置設定 -------------------------------------------------------------------
	// UFOの進行方向は上方向，画面スクロールは逆の下方向
	void MakeTreePoint( ) {
		for (int i = 0; i < TreeMax; i++) {
			TreePoint[i] = new Point( );			// Pointクラスのオブジェクト生成
			TreePoint[i].x = (int)(Math.random() * GameWidth);	// ランダムに横方向設定
			TreePoint[i].y = GameHeight - i * TreeHeight;		// 下から上方向に作成
			TreeType[i] = (int)(Math.random() * 2);				// ランダムにタイプ設定
		}
	}

	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		MoveScreen( );								// 画面移動処理
		paint(g);									// paintのみでチラツキ防止
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.setColor(new Color(99,222,57));			// 草原の色で塗りつぶす
		g.fillRect(0, 0, GameWidth, GameHeight);

		for (int i = TreeMax-1; i >= 0; i--)		// 後方（上）の木から描画 
			g.drawImage(Tree[TreeType[i]], TreePoint[i].x, TreePoint[i].y, this);

	}
	// 画面移動処理 ---------------------------------------------------------------------
	void MoveScreen( ) {
		// 前方（下）の木から順に移動してゲーム画面内の場合は，InputPointの位置に入れる
		int InputPoint = 0;									// セットする位置
		for (int i = 0; i < TreeMax; i++) {					// 前方（下）の木から
			TreePoint[i].y += ScrollStep;					// 木を移動
			if (TreePoint[i].y <= GameHeight) {				// ゲーム画面の範囲内
				TreePoint[InputPoint].x = TreePoint[i].x;	// InputPointの位置に入れる
				TreePoint[InputPoint].y = TreePoint[i].y;
				TreeType[InputPoint] = TreeType[i];
				InputPoint++;								// 入れる位置をずらす
			}
		}

		// InputPointの位置がTreeMaxの範囲内の場合，その位置に新しい木をセット
		for (int i = InputPoint; i < TreeMax; i++) {
			MissyuCount++;
			if (MissyuCount > MissyuMax) { 	// 密集数を超えた場合，新たに密集属性設定
				MissyuCount = 0;									// 密集カウント設定
				MissyuMax = (int)(Math.random() * TreeMax);			// 密集数設定
				MissyuKankaku = (int)(Math.random() * TreeHeight);	// 密集間隔設定
			}
			TreePoint[i].x = (int)(Math.random() * GameWidth);		// ランダム横方向設定
			TreePoint[i].y = TreePoint[i-1].y - (int)(Math.random() * MissyuKankaku);
												// 前の木の位置よりランダムに後ろにセット
			if (TreePoint[i].y > -TreeHeight)	// 突然画面に描画される場合
				TreePoint[i].y = -GameHeight/2 - (int)(Math.random() * MissyuKankaku);
												// 画面に描画されない位置に設定
			TreeType[i] = (int)(Math.random() * 2);					// 木のタイプ設定
		}
	}
}

// ======================================================================================
// UFOクラス
class UFO extends Applet {
	Applet applet;									// 表示対象（アプレット・フレーム）
	int GameWidth, GameHeight;						// ゲーム画面サイズ
	static Image UFOImage[ ] = new Image[6];		// UFOイメージ
	int Width, Height;								// UFOの幅と高さ
	int X, Y;										// UFOの中心位置
	int Current;									// UFOの表示番号
	int Step = 3;									// UFOの移動ステップ
	int UfoCount = 5;								// UFOの数
	int explosion = -1;								// UFO爆発状態(-1,0,1,・・,5)
	int MouseX, MouseY;								// マウスの位置
	Image ExplosionImage[ ] = new Image[6];			// 爆発イメージ
	AudioClip ExplosionSound[ ] = new AudioClip[5];	// 爆発音
	Shooting shooting;								// Shooting親クラス

	// コンストラクタ -------------------------------------------------------------------
	public UFO(Image UFOImage[ ], Image ExplosionImage[ ], AudioClip ExplosionSound[ ],
				int GameWidth, int GameHeight, Applet applet) {
		this.UFOImage = UFOImage;					// UFOイメージ
		this.ExplosionImage = ExplosionImage;		// 爆発イメージ
		this.ExplosionSound = ExplosionSound;		// 爆発音
		this.GameWidth = GameWidth;					// ゲーム画面サイズ
		this.GameHeight = GameHeight;
		this.applet = applet;						// アプレット
		Width = UFOImage[0].getWidth(applet);		// UFOの幅
		Height = UFOImage[0].getHeight(applet);		// UFOの高さ
		shooting = (Shooting)applet;				// Shooting親クラス
	}
	// イニシャライズ -------------------------------------------------------------------
	public void initialize( ) {
		UfoCount = 5;								// UFOの数
		X = GameWidth / 2;							// UFOの初期位置
		Y = GameHeight - Height / 2;
		Current = 0;								// UFOの表示番号
		Step = 3;									// UFOの移動ステップ
		explosion = -1;								// UFO爆発状態(-1,0,1,・・,5)
	}
	// マウスの位置 ---------------------------------------------------------------------
	public void MouseLocate(int MouseX, int MouseY) {
		this.MouseX = MouseX;						// マウスの位置
		this.MouseY = MouseY;
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		Current++;									// UFOアニメーション表示番号
		if (Current >= 6)							// 表示は0～5
			Current = 0;							// 最初の画像番号
		move( );									// UFO移動
		paint(g);									// UFO描画
	}
	// 移動処理 -------------------------------------------------------------------------
	public void move( ) {
		if (Math.abs(X - MouseX) > Step) {			// マウスとの距離が移動ステップより大
			if (X < MouseX)							// マウスより左側の場合
				X += Step;							// 右に移動
			else if (X > MouseX)					// マウスより右側の場合
				X -= Step;							// 左に移動
		}
		if (Math.abs(Y - MouseY) > Step) {			// マウスとの距離が移動ステップより大
			if (Y < MouseY)							// マウスより上側の場合
				Y += Step;							// 下に移動
			else if (Y > MouseY)					// マウスより下側の場合
				Y -= Step;							// 上に移動
		}
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (explosion >= 0 && explosion <= 5) {		// 爆発段階
			if (explosion == 0)						// 爆発の最初
				ExplosionSound[0].play( );			// 爆発音
			g.drawImage(ExplosionImage[explosion],
						X - Width, Y - Height, Width*2, Height*2, applet);
			explosion++;							// 爆破状態進行
			if (explosion > 5) {					// 爆発終了
				UfoCount--;							// UFOの残り数減少
				if (UfoCount == 0)
					shooting.gameover( );			// ゲームオーバー
				explosion = -1;						// 爆発していない状態
			}
		}
		g.drawImage(UFOImage[Current], X - Width / 2, Y - Height / 2, this);// UFO描画
		for (int i = 0; i < UfoCount; i++)			// UFO残り描画
			g.drawImage(UFOImage[0], i * Width, GameHeight - Height/2,
							 Width/2, Height/2, this);
	}
}

// ======================================================================================
// ミサイルマネージャクラス
class MissileManeger extends Applet {
	Applet applet;									// 表示アプレット(フレーム)
	static Image MissileImage[ ] = new Image[5];	// ミサイルイメージ
	private Missile missile[ ];						// ミサイルオブジェクト
	int MissileMax = 10;							// ミサイルの数
	int Kind;										// ミサイルの種類
	AudioClip MissileSound[ ] = new AudioClip[5];	// ミサイルの発射音
	Shooting shooting;								// Shooting親クラス

	// コンストラクタ -------------------------------------------------------------------
	public MissileManeger(Image MissileImage[ ], AudioClip MissileSound[ ],
			 TekiManeger tekiManeger, UFO ufo, Applet applet) {
		this.MissileImage = MissileImage;			// ミサイルイメージ
		this.MissileSound = MissileSound;			// ミサイルの発射音
		this.applet = applet;						// アプレット

		shooting = (Shooting)applet;				// Shooting親クラス

		missile = new Missile[MissileMax];			// ミサイルオブジェクト生成
		for (int i = 0; i < MissileMax; i++) {
			missile[i] = new Missile(MissileImage, tekiManeger, ufo, applet);
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		for (int i = 0; i < MissileMax; i++) {
			if (missile[i].Status == true) {		// ミサイル存在
				missile[i].move( );					// ミサイル移動
				missile[i].paint(g);				// ミサイル描画
			}
		}
	}
	// ミサイル発射 ---------------------------------------------------------------------
	public void shoot( ) {
		for (int i = 0; i < MissileMax; i++) {
			if (missile[i].Status == false) {		// ミサイルが発射されていない場合
				missile[i].shoot( );				// ミサイル発射
				MissileSound[missile[i].Kind].play( );	// ミサイル発射音
				if (shooting.Score > 0)				// 点数がある場合
					shooting.Score--;				// ミサイル発射につき減点
				break;
			}
		}
	}
	// ミサイル存在チェック -------------------------------------------------------------
	public boolean existcheck( ) {
		boolean flag = false;
		for (int i = 0; i < MissileMax; i++) {
			if (missile[i].Status == true) {		// ミサイル発射中
				flag = true;						// 存在フラグ　オン
				break;
			}
		}
		return flag;
	}	
}

// ======================================================================================
// ミサイルクラス
class Missile {
	Applet applet;									// アプレット
	static Image MissileImage[ ] = new Image[5];	// ミサイルイメージ
	int Width, Height;								// ミサイルの幅と高さ
	int Kind;										// ミサイルの種類
	int X, Y;										// ミサイルの位置
	int Step = 3;									// ミサイルの移動ステップ
	boolean Status;									// ミサイルの生存状態
	TekiManeger tekiManeger;						// 敵マネージャ
	UFO ufo;										// UFOクラスのオブジェクト
	Shooting shooting;								// Shooting親クラス

	// コンストラクタ -------------------------------------------------------------------
	public Missile(Image MissileImage[ ],TekiManeger tekiManeger,
					UFO ufo, Applet applet) {
		this.MissileImage = MissileImage;			// ミサイルイメージ
		this.applet = applet;						// アプレット
		this.tekiManeger = tekiManeger;				// 敵マネージャクラス
		this.ufo = ufo;								// UFOクラスのオブジェクト
		Status = false;								// 発射されていない状態
		shooting = (Shooting)applet;				// Shooting親クラス
	}
	// 発射処理 -------------------------------------------------------------------------
	public void shoot( ) {
		Kind = tekiManeger.Kind;					// 発射の種類：敵の種類
		X = ufo.X;									// 発射位置　：UFOの位置
		Y = ufo.Y;
		Width = MissileImage[Kind].getWidth(applet);// 敵の幅と高さ
		Height = MissileImage[Kind].getHeight(applet);
		Status = true;								// ミサイル使用中　存在
	}
	// 移動処理 -------------------------------------------------------------------------
	public void move( ) {
		if (Y > -Height)							// アプレット内に表示される範囲内
			Y -= Step;								// 垂直方向移動
		else
			Status = false;							// ミサイル場外へ　消滅

		for (int i = 0; i < 5; i++) {				// 敵との接触処理
			if (tekiManeger.teki[i].Status == true	// 敵がアプレット内に存在
				&& tekiManeger.teki[i].Kind == Kind) { // 敵とミサイルが同じ種類
				// 敵との接触チェック
				if (Math.abs(X - tekiManeger.teki[i].X)
						< Width/2 + tekiManeger.teki[i].Width/2
				 && Math.abs(Y - tekiManeger.teki[i].Y) 
						< Height/2 + tekiManeger.teki[i].Height/2) {
					tekiManeger.teki[i].explosion = 0;	// 爆破開始
					Status = false;					// ミサイル消滅
					shooting.Score += 10;			// 命中得点
				}
			}
		}
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(MissileImage[Kind], X - Width / 2, Y - Height / 2, applet);
	}
}

// ======================================================================================
// 敵マネージャクラス
class TekiManeger extends Applet {
	Applet applet;									// 表示対象（アプレット・フレーム)
	Image TekiImage[ ] = new Image[5];				// 敵イメージ
	Teki teki[ ];									// 敵オブジェクト
	int Width, Height;								// 敵の幅と高さ
	int TekiMax = 5;								// 敵の数
	int X, Y;										// 敵の中心位置
	int Step;										// 移動ステップ
	int Kind;										// 敵の種類
	int Pattern;									// 敵のパターン
	long AttackTime;								// 攻撃開始時間
	MissileManeger missileManeger;					// ミサイルマネージャのオブジェクト
	Shooting shooting;								// Shooting親クラス

	// コンストラクタ -------------------------------------------------------------------
	public TekiManeger(Image TekiImage[ ], Image ExplosionImage[ ],
						AudioClip ExplosionSound[ ], UFO ufo , Applet applet) {

		this.TekiImage = TekiImage;					// 敵イメージ
		this.applet = applet;						// アプレット
		teki = new Teki[TekiMax];					// 敵オブジェクト生成
		for (int i = 0; i < TekiMax; i++) {
			teki[i] = new Teki(TekiImage, ExplosionImage, ExplosionSound, ufo, applet);
		}
		shooting = (Shooting)applet;				// Shooting親クラス
	}
	// ミサイル情報取得 -----------------------------------------------------------------
	public void information(MissileManeger missileManeger) {
		this.missileManeger = missileManeger;		// ミサイルマネージャオブジェクト
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		boolean TekiExist = false;					// 敵の存在フラグ
		for (int i = 0; i < TekiMax; i++) {
			if (teki[i].Status == true) {			// 敵存在
				teki[i].move( );					// 敵移動
				teki[i].paint(g);					// 敵描画
				TekiExist = true;					// 敵存在
			}
		}

		// 敵もミサイルも存在しない場合
		if (TekiExist == false && missileManeger.existcheck( ) == false) {
			Kind = (int)(Math.random( ) * TekiMax);		// 敵の種類
			Width = TekiImage[Kind].getWidth(applet);	// 敵の幅と高さ
			Height = TekiImage[Kind].getHeight(applet);

			Date now = new Date( );						// 攻撃時間設定
			long nowtime = now.getTime( );				// 現在時刻
			Pattern = (int)(Math.random( ) * 6);		// 攻撃パターン
			for (int i = 0; i < TekiMax; i++) {			// ５つの敵を生成
				X = shooting.GameWidth / (TekiMax + 1) * (i + 1);
				Y = - Height * 2;						// 敵の位置
				Step = (int)(Math.random( ) * 5) + 3;	// 移動ステップ
				// 攻撃パターンで攻撃開始時間設定
				switch (Pattern) {
					case 0 :	// 同時発進(すべて5秒後に発進)
							AttackTime = nowtime + 1000 * 5;
							break;
					case 1 :	// 左から順に発進(5,6,7,･･･秒後発進)
							AttackTime = nowtime + 1000 * (5 + i);
							break;
					case 2 : 	// 右から順に発進(5,6,7,･･･秒後発進)
							AttackTime = nowtime + 1000 * (5 + (4 -i));
							break;
					case 3 :	// 中央から順に発進(5,6,7,･･･秒後発進)
							AttackTime = nowtime + 1000 * (5 + Math.abs(2 - i));
						 	break;
					case 4 : 	// 端から順に発進(5,6,7,･･･秒後発進)
							AttackTime = nowtime + 1000 * (5 + 2 - Math.abs(2 - i)); 
						 	break;
					case 5 : 	// ランダムに発進
							AttackTime = nowtime + 1000 * (5 + (int)(Math.random( ) * 5));
						 	break;
				}
				teki[i].initialize(Kind, X, Y, AttackTime, Step);	// 敵の初期化生成
			}
		}
	}
}

// --------------------------------------------------------------------------------------
// 敵クラス
class Teki {
	Applet applet;									// アプレット
	static Image TekiImage[ ] = new Image[5];		// 敵イメージ
	int Width, Height;								// 敵の幅と高さ
	int Kind;										// 敵の種類
	int X, Y;										// 敵の中心位置
	int Step;										// 敵の移動ステップ
	boolean Status;									// 敵の生存状態
	long AttackTime;								// 攻撃開始時間
	Image ExplosionImage[ ] = new Image[6];			// 爆発イメージ
	int explosion;									// 爆破カウント
	AudioClip ExplosionSound[ ] = new AudioClip[5];	// 爆発音
	UFO ufo;										// UFOクラスのオブジェクト
	Shooting shooting;								// Shooting親クラス

	// コンストラクタ -------------------------------------------------------------------
	public Teki(Image TekiImage[ ], Image ExplosionImage[ ], AudioClip ExplosionSound[ ],
			UFO ufo, Applet applet) {
		this.TekiImage = TekiImage;					// 敵イメージ
		this.ExplosionImage = ExplosionImage;		// 爆発イメージ
		this.ExplosionSound = ExplosionSound;		// 爆発音
		this.ufo = ufo;								// UFOクラス
		this.applet = applet;						// アプレット
		Status = false;								// 敵存在
		shooting = (Shooting)applet;				// Shooting親クラス
	}
	// 初期化処理 -----------------------------------------------------------------------
	public void initialize(int Kind, int X, int Y, long AttackTime, int Step) {
		this.Kind = Kind;							// 敵の種類
		this.X = X;									// 敵の中心位置
		this.Y = Y;
		this.AttackTime = AttackTime;				// 攻撃開始時間
		this.Step = Step;							// 攻撃移動ステップ
		Status = true;								// 敵の生存状態
		explosion = -1;								// 爆破カウント
		Width = TekiImage[Kind].getWidth(applet);	// 敵の幅と高さ
		Height = TekiImage[Kind].getHeight(applet);
	}
	// 移動処理 -------------------------------------------------------------------------
	public void move( ) {
		Date now = new Date( );						// 攻撃時間設定
		long nowtime = now.getTime( );				// 現在時刻
		if (Y < Height)								// スタートラインに達していない場合
			Y += Step;								// 垂直方向移動
		if (nowtime > AttackTime) {					// 攻撃時間に達した場合
			Y += Step;								// 垂直方向移動
			if (X < ufo.X)							// UFOより左側
				X += 1;								// 右に移動
			else if (X > ufo.X)						// UFOより右側
				X -= 1;								// 左に移動
		}
		if (Y > shooting.GameHeight + Height)		// ゲーム画面の外に出た場合
			Status = false;							// 敵存在せず
		// UFOとの接触処理
		if (Math.abs(X - ufo.X) < Width/2 + ufo.Width/2
		 && Math.abs(Y - ufo.Y) < Height/2 + ufo.Height/2) {
			ufo.explosion = 0;						// UFO爆破開始
			Status = false;							// 敵存在せず 消滅
		}
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(TekiImage[Kind], X - Width / 2, Y - Height / 2, applet);

		if (explosion >= 0 && explosion <= 5) {		// 爆発状態の場合
			if (explosion == 0)						// 爆発の最初
				ExplosionSound[Kind].play( );		// 爆発音
			g.drawImage(ExplosionImage[explosion],
						X - Width, Y - Height, Width*2, Height*2, applet);
			explosion++;							// 爆発状態進行
			if (explosion > 5) {					// 爆発終了
				Status = false;						// 敵存在せず 消滅
			}
		}
	}
}
