import java.applet.*;	// Applet
import java.awt.*;		// Graphics, Image, Color, MediaTracker, Button, Label, Scrollbar
import java.awt.event.*;// ActionListener, AdjustmentListener, ActionEvent, AdjustmentEvent
						// ItemListener, ItemEvent

public class Bound extends Applet 
		implements Runnable, ActionListener, AdjustmentListener, ItemListener {
 		// Runnable, ActionListener, AdjustmentListener，ItemListenerインターフェース実装
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	Image Ball;										// ボールイメージ
	Button StartButton;								// 発射スタートボタン
	Scrollbar SpeedBar, AngleBar;					// スピードバー，角度バー
	Checkbox LocusCheckbox;							// 軌跡チェックボックス
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int BallRadius;									// ボールの半径
	double Speed = 60;								// 発射速度	デフォルト60
	double Angle = 80;								// 発射角度 デフォルト80度
	Thread thread;									// スレッド

	double t;										// 時間
	double BasePoint;								// バウンドの基点
	double Vx, Vy;									// 水平方向速度，垂直方向速度
	double PreVy;									// 前回の垂直方向速度
	double X, Y, PreX, PreY;						// ボールの位置，前回の位置
	double E;										// 跳ねかえり係数

	boolean FireSw = false;							// 発射スイッチ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作用用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		ScreenClear( );								// 画面クリア

		MediaTracker  mt = new MediaTracker(this);	// メディアトラッカ生成
		Ball = getImage(getCodeBase( ), "image/ball.gif");
		mt.addImage(Ball, 0);						// メディアトラッカにセット
		try {
			mt.waitForID(0);						// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);					// 例外エラー表示
		}
		// 画像の入力が完了した後で，サイズを調べる
		BallRadius = Ball.getWidth(this) / 2;		// ボールの半径
		X = Y = BallRadius;							// ボールの位置

		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー付加

		Label LabelSpeed = new Label("速度");
		Label LabelAngle = new Label("角度");

		// スクロールバー生成
		SpeedBar = new Scrollbar(Scrollbar.HORIZONTAL, (int)Speed, 0, 0, 100);
		AngleBar = new Scrollbar(Scrollbar.HORIZONTAL, (int)Angle, 0, 0, 90);
		SpeedBar.addAdjustmentListener(this);		// バーにリスナー付加
		AngleBar.addAdjustmentListener(this);
		add(LabelSpeed);							// ラベルをアプレットに付加
		add(SpeedBar);								// スピードバーをアプレットに付加
		add(LabelAngle);
		add(AngleBar);
		// 軌跡チェックボックス
		LocusCheckbox = new Checkbox("軌跡");		// 軌跡ボックスオブジェクト生成
		add(LocusCheckbox);							// 軌跡チェックボックスを付加
		LocusCheckbox.addItemListener(this);		// 軌跡チェックボックスにリスナー追加 
		LocusCheckbox.setState(false);				// 軌跡チェックボックスオフ

		setLayout(null);							// レイアウトを自由設定
		StartButton.setBounds(10, 10, 50, 20);		// ボタンの位置とサイズを再設定
		LabelSpeed.setBounds(70, 10, 30, 20);		// ラベルの位置とサイズを再設定
		SpeedBar.setBounds(100, 10, 100, 20);		// スピードバーの位置とサイズを再設定
		LabelAngle.setBounds(210, 10, 30, 20);		// ラベルの位置とサイズを再設定
		AngleBar.setBounds(240, 10, 100, 20);		// 角度の位置とサイズを再設定
		LocusCheckbox.setBounds(350, 10, 48, 20);	// 軌跡チェックボックスの位置とサイズ

		repaint( );
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 画面クリア -----------------------------------------------------------------------
	public void ScreenClear( ) {
		WorkGraphics.setColor(Color.white);
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		WorkGraphics.setColor(Color.black);
		WorkGraphics.drawRect(0, 0, AppletWidth-1, AppletHeight-1);
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (LocusCheckbox.getState( ) == false)		// 軌跡チェックがオフの場合
			ScreenClear( );							// 画面クリア
		// ボールを描画
		WorkGraphics.drawImage(Ball, (int)X - BallRadius,
						(int)(AppletHeight - Y - BallRadius), this);
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while(thread != null) {						// スレッドが存在している間
			try {
				thread.sleep(50);					// スレッドを指定ミリ秒スリープ
			} catch (InterruptedException ex) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+ex);
			}
			if (FireSw == true) {					// 発射ボタン　オンの場合
				t = t + 0.1;						// 時間を進める
				X = Vx * t + BasePoint;						// t時間後のx方向位置
				Y = Vy * t - 9.8 / 2 * t * t + BallRadius;	// t時間後のy方向位置

				if(Y > BallRadius) {				// ボールの中心がボールの半径より大
					PreX = X;  PreY = Y;			// 今回の位置を前回の位置として保管
				}
				else {
					Vy = (Vy - 9.8 * t) * E * (-1);	// バウンド時の初速度
					t = 0;							// 新しい飛行のために時間をクリア
					BasePoint = X;					// 次の放物運動の基準点

					if(Math.abs(PreVy - Vy) > 0.1)	// はずむ時の速度チェック
						PreVy = Vy;
					else							// 指定した値よりも小さくなった場合
						FireSw = false;				// 停止　発射スイッチ　オフ
				}

				if (X > AppletWidth + BallRadius)	// アプレットの右端から出た場合
					FireSw = false;					// 停止　発射スイッチ　オフ
				if (FireSw == false) {
					X = Y = BallRadius;
					try {
						thread.sleep(3000);				// スレッドを指定ミリ秒スリープ
					} catch (InterruptedException ex) {	// 他のスレッドの割り込み例外処理
						showStatus(" "+ex);
					}
				}
			}
			repaint( );
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッドを無効に
	}
 	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// ボタンアクション
		Button button = (Button)evt.getSource( );
		if(button == StartButton) { 				// 発射スタートボタンの場合
			t = 0;									// 時間
			BasePoint = BallRadius;					// バウンドの基点
			Vx = Speed * Math.cos(Angle * 3.14 / 180);	// 水平方向速度
			Vy = Speed * Math.sin(Angle * 3.14 / 180);	// 垂直方向速度
			PreVy = 0;								// 前回の垂直方向速度仮設定
			E = 0.8;								// 跳ね返り係数
			FireSw = true;							// 発射スイッチをオン
			ScreenClear( );							// 画面クリア
		}
	}
	// AdjustmentListenerインターフェースのメソッド定義 ---------------------------------
	public void adjustmentValueChanged(AdjustmentEvent evt) {	// バーの変化キャッチ
		Scrollbar scrollbar = (Scrollbar)evt.getSource( );
		if (scrollbar == SpeedBar)					// スピードバーの場合
			Speed = (double)SpeedBar.getValue( );	// スピードバーの値取得
		else if (scrollbar == AngleBar)				// 角度バーの場合
			Angle = (double)AngleBar.getValue( );	// 角度バーの値を取得

		showStatus("Speed=" + Speed +   " Angle=" + Angle);
	}
	// ItemListenerインターフェースのメソッドを定義
	public void itemStateChanged(ItemEvent evt) {	// 項目変更イベント 
	}
}
