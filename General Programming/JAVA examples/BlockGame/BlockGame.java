import java.applet.*;			// Applet, AudioClip
import java.awt.*;				// Graphics, Image, Color, MediaTracker
import java.awt.event.*;		// KeyListener, KeyEvent, MouseListener, MouseEvent

public class BlockGame extends Applet 
		implements Runnable, KeyListener, ActionListener {

	Thread thread = null;							// アニメーション用スレッド
	Image BallImage, BlockImage, RacketImage;		// ボール，ブロック，ラケットイメージ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int BallX, BallY,								// ボールの位置
		BallPreX, BallPreY,							// ボールの前回位置
		BallXStep, BallYStep,						// ボールの移動ステップ値
		BallSize;									// ボールサイズ
	int BlockWidth, BlockHeight;					// ブロックの幅と高さ
	int RacketWidth, RacketHeight;					// ラケットサイズ
	int AppletWidth, AppletHeight;					// アプレットサイズ
	int Score;										// スコア
	int RacketX, RacketY;							// ラケットの位置
	int BlockX, BlockY;								// ブロック表示位置
	int BlockRow, BlockColumn;						// ブロックの行列数
	int Block[ ][ ] = new int[20][30];				// ブロックの確認用配列
	int BallCheckX[ ] = new int[8];					// ボールチェックポイント
	int BallCheckY[ ] = new int[8];
	int HantenX[ ] = new int[8];					// 反転計算値
	int HantenY[ ] = new int[8];
	int	KeyDownSw;									// KeyDown処理スイッチ
	int RaketStep = 10;								// ラケット移動値
	int PreRacketHit;								// 前回ラケット衝突値
	boolean GameStartSw = false;					// ゲームスタートスイッチ
	MediaTracker mt = new MediaTracker(this);		// 画像入力監視メディアトラッカ生成
    AudioClip HitSound;								// ヒットサウンド
	int BallCount = 3;								// ボールの数
	int BallSpeed = 1;								// ボールのスピード
	int ClearCount = 0;								// クリア回数
	int SleepTime = 10, WaitTime = 1000;			// スリープタイム

	Button StartButton;								// スタートボタン

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// パラメータ取得
		BlockX = Integer.parseInt(getParameter("BlockX"));			// ブロックの表示位置
		BlockY = Integer.parseInt(getParameter("BlockY"));
		BlockRow = Integer.parseInt(getParameter("BlockRow"));		// ブロックの行数
		BlockColumn = Integer.parseInt(getParameter("BlockColumn"));// ブロックの列数

		addKeyListener(this);						// キーリスナー追加

		//　サウンドと画像データ入力
		//　ボールは透明GIFで，サイズは奇数であること  理由：左右とセンター
		//　ブロックとラケットは矩形であること
        HitSound = getAudioClip(getCodeBase( ), "sound/hit.au");		// クリック音

		BallImage = getImage(getCodeBase( ), getParameter("ball"));		// ボール画像
		mt.addImage(BallImage, 0);					// 画像をメディアトラッカに設定
		BlockImage = getImage(getCodeBase( ), getParameter("block"));	// ブロック画像
		mt.addImage(BlockImage, 0);
		RacketImage = getImage(getCodeBase( ), getParameter("racket"));	// ラケット画像
		mt.addImage(RacketImage, 0);
		try {
			mt.waitForID(0);						// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);					// エラー表示
		}

		BallSize = BallImage.getWidth(this);		// ボールサイズ
		BlockWidth = BlockImage.getWidth(this);		// ブロックサイズ
		BlockHeight = BlockImage.getHeight(this);
		RacketWidth = RacketImage.getWidth(this);	// ラケットサイズ
		RacketHeight = RacketImage.getHeight(this);
		AppletWidth = getSize( ).width;				// アプレットサイズ
		AppletHeight = getSize( ).height;

		WorkImage = createImage(AppletWidth, AppletHeight); // 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		setLayout(null);							// レイアウトを自由設定
		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー追加
		StartButton.setBounds(AppletWidth/2-30, AppletHeight-40, 60, 30);

		RacketX = (AppletWidth - RacketWidth) / 2;	// ラケットの位置
		RacketY = AppletHeight - RacketHeight * 2;

		Score = 0;									// スコア
		CheckPointMake( );							// ボールの周囲のチェックポイント作成
		GameScreenDisplay( );						// ゲーム画面描画
		BallSet(1);									// ボール初期位置設定
	}
	// ボールの外周ポイント及び反転計算値設定 -------------------------------------------
	public void CheckPointMake( ) {					// ボール周囲８点調査
		int r = BallSize / 2;						// 半径
		int w = (int)(r * Math.sin(45 * 3.14 / 180));		// ｒ×sin(45)
		//　BallCheckX, BallCheckY：相対的な位置，HantenX,HantenY：衝突時の反転計算値
		BallCheckX[0]=r;         BallCheckY[0]=0;        HantenX[0]=1; HantenY[0]=-1;
		BallCheckX[1]=r+w;       BallCheckY[1]=r-w;      HantenX[1]=-1; HantenY[1]=-1;
		BallCheckX[2]=BallSize-1;BallCheckY[2]=r;        HantenX[2]=-1; HantenY[2]=1;
		BallCheckX[3]=r+w;       BallCheckY[3]=r+w;      HantenX[3]=-1; HantenY[3]=-1;
		BallCheckX[4]=r;         BallCheckY[4]=BallSize-1;HantenX[4]=1; HantenY[4]=-1;
		BallCheckX[5]=r-w;       BallCheckY[5]=r+w;      HantenX[5]=-1;HantenY[5]=-1;
		BallCheckX[6]=0;         BallCheckY[6]=r;        HantenX[6]=-1; HantenY[6]=1;
		BallCheckX[7]=r-w;       BallCheckY[7]=r-w;      HantenX[7]=-1; HantenY[7]=-1;
	}
	// ゲーム画面描画 -------------------------------------------------------------------
	public void GameScreenDisplay( ) {
		WorkGraphics.setPaintMode( );				// 上書きモード

		// 枠描画
		WorkGraphics.setColor(Color.white);			// 白色で塗りつぶす
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		WorkGraphics.setColor(Color.black);			// 外枠を作成
		WorkGraphics.drawRect(0, 0, AppletWidth - 1, AppletHeight - 1);

		// ブロック描画
		for (int i = 0; i < BlockRow; i++) {		// 縦方向　行
			for (int j = 0; j < BlockColumn; j++) {	// 横方向　列
				WorkGraphics.drawImage(BlockImage,	// 作業グラフィックにブロック描画
					BlockX + BlockWidth * j, 
					BlockY + BlockHeight * i, this);
				Block[i][j] = 1;					// 配列にブロックセット
			}
		}

		// ラケット描画
		WorkGraphics.drawImage(RacketImage,RacketX, RacketY, this);

		WorkGraphics.setXORMode(Color.white);		// 重ね描き背景色白モード
	}
	// ボールの設定 ---------------------------------------------------------------------
	public void BallSet(int UseFlag) {
		if (UseFlag == 1)							// 新しいボールを必要とする場合
			BallCount--;							// ボールの数を減らす
		if (BallCount < 0)							// ボールがなくなった場合
			return;

		BallX = BallPreX = (AppletWidth - BallSize) / 2;	// ボールの現在及び前回位置
		BallY = BallPreY = BlockY + BlockHeight * BlockRow;
		BallSpeed = ClearCount + 1;					// ボールスピード
		if (BallSpeed > 5)
			BallSpeed = 5;
		BallXStep = 1;								// 水平方向の移動スピード
		if ((int)(Math.random( ) * 2) == 0)			// ランダムに方向を左右に設定
			BallXStep = -1;
		BallYStep = 1;								// 垂直方向の移動スピード

		WorkGraphics.drawImage(BallImage, BallX, BallY, this);	// ボール描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
        while (thread != null && BallCount >= 0) {	// スレッドとボールがまだある場合
			if (GameStartSw) {						// ゲームスタートスイッチがtrue
				try {
					repaint( );						// 再描画 -> update( )
					thread.sleep(SleepTime - BallSpeed + WaitTime);	// スレッドスリープ
					if (WaitTime > 0)				// 最初だけの待ち時間
						WaitTime = 0;				// ボールが動けば待ち時間を0にする
				}
				catch (InterruptedException e) {	// sleepに対する例外処理
				}
				KeyDownSw = 0;						// KeyDown処理スイッチ不可 
				BallProcess( );						// ボール処理
				KeyDownSw = 1;						// KeyDown処理スイッチ可
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット終了 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッドを無効
	}
	// ボールの運動処理 -----------------------------------------------------------------
	public void BallProcess( ) {
		// setXORMode(Color.white)の設定によって，上書きで消す
		WorkGraphics.drawImage(BallImage, BallPreX, BallPreY, this);	// ボール消す

		// 前回の位置に移動量を加えてボールの位置を再設定
		BallX = BallPreX + BallXStep;				// ボールの位置再設定
		BallY = BallPreY + BallYStep;

		int HitSw = 0;								// 衝突スイッチを０に仮設定
		// ボールが左右の枠から外に出ていないかチェック  
		if (BallX < 1 || BallX > AppletWidth - BallSize) {
			BallXStep = -BallXStep;					// X方向の移動量を反転
			HitSw = 1;								// 衝突スイッチをONに設定
		}
		// ボールが上の枠から外に出ていないかチェック
		if (BallY < 1) {
			BallYStep = -BallYStep;					// Y方向の移動量を反転
			HitSw = 1;								// 衝突スイッチをONに設定
		}
		// 下のチェック
		if (BallY > AppletHeight - BallSize) {
			BallSet(1);								// 新しいボールをセット
			return;
		}

		int RacketHit = 0;
		// ボールが仮進行した場合のブロックチェック
		for (int i = 0; i < 8; i++) {				// ボールの周囲８点を調べる
			int xw = BallX + BallCheckX[i];			// ボールの周囲８点の位置
			int yw = BallY + BallCheckY[i];

			// ブロックの範囲内
			if (xw >= BlockX && xw < BlockX + BlockWidth * BlockColumn
			 && yw >= BlockY && yw < BlockY + BlockHeight * BlockRow) {

				// xw,ywに該当するブロックの配列番号(xp,yp)
				int xp = (xw - BlockX) / BlockWidth;
				int yp = (yw - BlockY) / BlockHeight;
				if (Block[yp][xp] == 1) {				// ブロックがある場合
					Block[yp][xp] = 0;					// 配列内でブロックを消す
					// setXORMode(Color.white)の設定によって，上書きで消す
					WorkGraphics.drawImage(BlockImage,	// グラフィック上でブロックを消す
						BlockX + BlockWidth * xp,
						BlockY + BlockHeight * yp, this);

					BallXStep = BallXStep * HantenX[i];	// ボールの移動変量を修正
					BallYStep = BallYStep * HantenY[i];
					HitSw = 1;							// 衝突スイッチオン
					Score++;							// スコアカウント
					showStatus("Score = " + Score);		// スコア表示

					// ブロックをすべてつぶした場合
					if (Score > 0 && Score % (BlockRow * BlockColumn) == 0) {
						WaitTime = 1000;				// スレッドの待ち時間設定
						ClearCount++;					// クリア回数
						GameScreenDisplay( );			// 新しいゲーム画面描画
						BallSet(0);						// 現在のボール位置設定
						return;
					}
				}
			}

			// ボールの周囲８点がラケット内かのチェック
			// 衝突した場合，衝突範囲から脱出するまでの衝突は無視する
			if (RacketX <= xw && xw <= RacketX+RacketWidth-1  
 			 && RacketY <= yw && yw <= RacketY + RacketHeight-1) {
				RacketHit = 1;							// ラケットヒットをオン
				if (PreRacketHit == 0) {				// 1つ前が衝突していない
					HitSw = 1;							// 衝突スイッチオン
					BallXStep = BallXStep * HantenX[i];	// 衝突後の移動値設定
					BallYStep = BallYStep * HantenY[i];
					break;
				}
			}
		}
		PreRacketHit = RacketHit;					// ラケット衝突を保管

		if (HitSw == 1) { 							// 衝突した場合
			HitSound.play( );						// ぶつかった音
			BallX = BallPreX;  BallY = BallPreY;	// 衝突前の位置に戻す
		}

		WorkGraphics.drawImage(BallImage, BallX, BallY, this);	// ボール表示
		BallPreX = BallX;		BallPreY = BallY;	// 今回の位置保管
	}
	// KeyListenerインタフェースの各メソッド定義 ----------------------------------------
	public void keyTyped(KeyEvent evt) { }
	public void keyReleased(KeyEvent evt) {  }
	public void keyPressed(KeyEvent evt) {
        evt.consume( );								// イベントを消費
		if (KeyDownSw == 0)							// KeyDown処理不可
			return;

		// setXORMode(Color.white)の設定によって，上書きで消す
		WorkGraphics.drawImage(RacketImage, RacketX, RacketY, this);	// ラケット消す

		int RacketXw = RacketX;						// ラケットの位置保管
		int RacketYw = RacketY;
		switch (evt.getKeyCode( )) {
			case KeyEvent.VK_UP	:								// 上のキー押下
				if (RacketY > BlockY + BlockHeight * BlockRow)	// 上移動上限
					RacketY -= RaketStep;						// ラケットを上に移動
				break;
			case KeyEvent.VK_DOWN:								// 下のキー押下
				if (RacketY < AppletHeight - RacketHeight * 2)	// 下移動下限
					RacketY += RaketStep;						// ラケットを下に移動
					break;
			case KeyEvent.VK_LEFT:								// 左のキー押下
				if (RacketX > 0)								// 左移動限界
					RacketX -= RaketStep;						// ラケット左に移動
			 	break;
			case KeyEvent.VK_RIGHT:								// 右のキー押下
				if (RacketX < AppletWidth - RacketWidth)	  	// 右移動限界
					RacketX += RaketStep;						// ラケット右に移動
				break;
		}

		// ラケットを移動した先にボールがあるか判定
		int flag = 0;								// チェックフラグ
		// ボールの周囲８点にラケットがあるかチェック
		for (int i = 0; i < 8 && flag == 0; i++) {	// フラグが0の場合，8点調べる
			int xw = BallX + BallCheckX[i];			// ボールの周囲8点の各位置
			int yw = BallY + BallCheckY[i];
			// ラケットの範囲内にボールがあるかチェック
			if (RacketX <= xw && xw <= RacketX + RacketWidth - 1
				&& RacketY <= yw && yw <= RacketY + RacketHeight - 1)
				flag = 1;							// ある場合フラグをオン
		}

		if (flag == 1) {	// ラケットを移動したところにボールがある場合
			HitSound.play( );						// ヒットサウンド
			RacketX = RacketXw;  RacketY = RacketYw;// 元の位置に戻す
		}

		WorkGraphics.drawImage(RacketImage, RacketX, RacketY, this);	// ラケット表示

		repaint( );
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button button = (Button)evt.getSource( );
		if (button == StartButton) {				// スタートボタンの場合
			StartButton.setVisible(false);			// スタートボタンを隠す
			GameStartSw = true;						// ゲームスタート
			requestFocus( );						// 入力フォーカスを要求
		}
	}
}
