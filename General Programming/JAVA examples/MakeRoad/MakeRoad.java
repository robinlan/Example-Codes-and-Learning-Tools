import java.applet.*;		// Applet, AudioClip
import java.awt.*;			// Graphics, Image, Color, MediaTracker, Button
import java.awt.event.*;	// KeyListener, KeyEvent, ActionListener, ActionEvent

public class MakeRoad extends Applet
						implements KeyListener, Runnable, ActionListener {

	Image Ufo;										// Ufo画像
    Image panel[ ] = new Image[7];					// 7種類のパネル画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	int SpaceRow, SpaceColumn;						// スペースの行と列
	int Base;										// 表示ベースポイント
	int UfoX, UfoY,									// UFOのグラフィック上での位置
        UfoRow, UfoColumn, 							// UFOのパネル上での行と列
        UfoStepCount,       						// 各パネル内でのステップカウント
		UfoInX,										// UFOが入るときのX方向  1:→  -1:←
		UfoInY,										// UFOが入るときのY方向  1:↓  -1:↑
		UfoSize;									// UFOサイズ

	int Map[ ][ ] = new int[4][4];					// マップ

	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int PanelSize;									// パネルサイズ
	int Speed = 10;									// ゲームスピード(10 ～　100)

	boolean GameStartFlag = false;					// ゲームスタートフラグ
    boolean GameOverFlag = true;					// ゲームオーバーフラグ
	boolean GameClearFlag = false;					// ゲームクリアフラグ

	Thread thread = null;							// スレッド

	Button StartButton;								// スタートボタン

    AudioClip BGM, GameoverSound;					// BGMとゲームオーバーサウンド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
        BGM = getAudioClip(getCodeBase( ), "sound/loop.au");			// ＢＧＭ
        GameoverSound = getAudioClip(getCodeBase( ), "sound/gong.au");	// ゲームオーバーサウンド

		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		imageLoad( );								// 画像データ入力

		mapset( );									// マップ作成

		UfoInitial( );								// UFOの初期化

		setLayout(null);							// レイアウトを自由設定
		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー追加
		StartButton.setBounds(Base-60, Base-30, 60, 30);	// ボタンの配置とサイズ設定

		addKeyListener(this);						// リスナー追加
		requestFocus( );							// キー入力フォーカスを要求
	}
	// 画像ロード処理 -------------------------------------------------------------------
	public void imageLoad( ) {
		MediaTracker  mt = new MediaTracker(this);	// 画像入力を監視するメディアトラッカ
		for (int i = 0; i < 7; i++) {				// 7種類のパネル画像入力
			panel[i] = getImage(getCodeBase( ), "image/panel"+i+".gif");
			mt.addImage(panel[i], 0);				// メディアトラッカ にイメージセット
		}
		Ufo = getImage(getCodeBase( ), "image/Ufo.gif");// UFO画像入力
		mt.addImage(Ufo, 0);						// メディアトラッカ にイメージセット

		try {
			mt.waitForID(0);						// イメージ画像の入力完了を待つ
		} catch(InterruptedException e) {
			showStatus(" "+e);
		}

		PanelSize = PanelSize = panel[0].getWidth(this);	// パネルサイズ
        Base = (AppletWidth - PanelSize*4)/2;		// 表示基本位置 パネルを中央に表示
		UfoSize = Ufo.getWidth(this);				// UFOサイズ
	}
	// マップ作成 -----------------------------------------------------------------------
	public void mapset( ) {
		int p = 0;
		for (int r = 0; r < 4; r++)					// 行Row方向ループ
			for (int c = 0; c < 4; c++)				// 列Column方向ループ
				Map[r][c] = p++ / 2; 				// 画像番号設定
				
		Map[3][2] = (int)(Math.random( )*7);		// ランダムにもう一枚追加
		Map[3][3] = -1;								// 空白
		SpaceRow = SpaceColumn = 3;					// 空白の位置（行Row と 列Column）
		// ランダムにマスのパネルを入れ替える
		for (int r = 0; r < 4; r++) {				// 行方向にループ
			for (int c = 0; c < 4; c++) {			// 列方向にループ
				int rp = (int)(Math.random( )*4);	// ランダム行ポイント　0 ～ 3
				int cp = (int)(Math.random( )*4);	// ランダム列ポイント　0 ～ 3
				// スタート地点とゴール地点は交換しない
				if (!(r+c == 0 || rp+cp == 0 || r*c == 9 || rp*cp == 9)) {
					int temp = Map[r][c];			// 交換処理
					Map[r][c] = Map[rp][cp];
					Map[rp][cp] = temp;
				}
			}
		}
    }
	// UFOの初期化 ----------------------------------------------------------------------
	private void UfoInitial( ) {
		// UFOのグラフィック上での位置
		UfoX = Base - UfoSize / 2;
		UfoY = Base + PanelSize / 2 - UfoSize / 2;
		// UFOのパネル上での位置
        UfoRow = UfoColumn = 0;
		// UFOが各パネルに入ってからの処理タイムカウントを０クリア
        UfoStepCount = 0;
		// UFOがパネルに入る方向　右方向
		UfoInX = 1;
		UfoInY = 0;
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this); 					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g) {
		WorkGraphics.setColor(Color.black);						// 背景色
        WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);	// 作業領域を塗りつぶす

        WorkGraphics.drawImage(panel[0], Base-PanelSize, Base, this);// 入口のパネル描画
		// マップ内容を描画
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				if (Map[r][c] != -1)				// 空白でない場合
					// パネルを描画
			     	WorkGraphics.drawImage(panel[Map[r][c]],
										Base+c*PanelSize, Base+r*PanelSize, this);
			}
		}
		WorkGraphics.setColor(Color.white);
		WorkGraphics.drawString("ゴール",
					 4*PanelSize+Base + 15, 3*PanelSize+Base - 5);	// ゴール表示
        WorkGraphics.drawImage(panel[0], 4*PanelSize+Base, 3*PanelSize+Base, this);	
        WorkGraphics.drawImage(Ufo, UfoX, UfoY, this);				// UFO描画

		WorkGraphics.setColor(Color.yellow);
		if (GameStartFlag == true && GameClearFlag == true)		// ゲームクリアチェック
			WorkGraphics.drawString("Game CLear", Base, Base+PanelSize*4+10);
		if (GameStartFlag == true && GameOverFlag == true)		// ゲームオーバーチェック
			WorkGraphics.drawString("Game over", Base, Base+PanelSize*4+10);
      
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
    }
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ) {
		while(thread != null) {						// スレッドが存在している間
			if (GameStartFlag == true && GameOverFlag == false)	// ゲームフラグオン
				move( );							// UFO移動処理
			try {
				thread.sleep(300 - Speed);			// スレッドスリープ
			} catch(InterruptedException evt) {
			}
			repaint( );								// 再描画
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット終了 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッドをヌルにして無効にする
		BGM.stop( );								// ＢＧＭ終了
	}
	// 移動処理 -------------------------------------------------------------------------
    private void move( ) {
		if (UfoRow == 3 && UfoColumn == 4) {		// 出口に到着 ゲームクリア
			gameclearProcess( );					// ゲームクリア処理
			return;
		}
		else if (UfoRow < 0 || UfoRow > 3 || UfoColumn < 0 || UfoColumn > 3) {
			// マップから飛び出した場合，ゲームオーバー
			gameoverProcess( );						// ゲームオーバー処理
			return;
		}
		// パネル - or +  入る方向 →   --------------------------------------------------
		if ((Map[UfoRow][UfoColumn] == 0 || Map[UfoRow][UfoColumn] == 2)




				&& UfoInX == 1 && UfoInY == 0) {	// 入る方向 →
			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= PanelSize)			// パネルの範囲内の場合
				UfoX=UfoX+1;						// Ｘ方向に＋１
			if (UfoStepCount == PanelSize) {		// パネルの端に到達
				UfoColumn++;						// 右のパネルに移動
				UfoInX = 1;							// 次のパネルに入る方向　→
				UfoInY = 0;	
				UfoStepCount = 0;					// ステップカウントを０クリア
			}
		}
		// パネル - or +  入る方向 ←   --------------------------------------------------
		else if ((Map[UfoRow][UfoColumn] == 0 || Map[UfoRow][UfoColumn] == 2)




				&& UfoInX == -1 && UfoInY == 0) {	// 入る方向 ←
			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= PanelSize)			// パネルの範囲内の場合
				UfoX=UfoX-1;						// Ｘ方向に-１
			if (UfoStepCount == PanelSize) {		// パネルの端に到達
				UfoColumn--;						// 左のパネルに移動
				UfoInX = -1;						// 次のパネルに入る方向　←
				UfoInY = 0;	
				UfoStepCount = 0;					// ステップカウントを０クリア
			}
		}
		// パネル | or +  入る方向 ↓   --------------------------------------------------
		else if ((Map[UfoRow][UfoColumn] == 1 || Map[UfoRow][UfoColumn] == 2)




				&& UfoInX == 0 && UfoInY == 1) {	// 入る方向 ↓
			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= PanelSize)			// パネルの範囲内の場合
				UfoY=UfoY+1;						// Y方向に+１
			if (UfoStepCount == PanelSize) {		// パネルの端に到達
				UfoRow++;							// 下のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↓
				UfoInY = 1;	
				UfoStepCount = 0;					// ステップカウントを０クリア
			}
		}
		// パネル | or +  入る方向 ↑   --------------------------------------------------
		else if ((Map[UfoRow][UfoColumn] == 1 || Map[UfoRow][UfoColumn] == 2)




				&& UfoInX == 0 && UfoInY == -1) {	// 入る方向 ↑
			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= PanelSize)			// パネルの範囲内の場合
				UfoY=UfoY-1;						// Y方向-１
			if (UfoStepCount == PanelSize) {		// パネルの端に到達
				UfoRow--;							// 上のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↑
				UfoInY = -1;
				UfoStepCount = 0;					// ステップカウントを０クリア
			}
		}
		// パネル 円形右上  	入る方向→  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 3 && UfoInX == 1 && UfoInY == 0) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoX=UfoX+1;						// Ｘ方向＋１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX+1;						// Ｘ方向＋１
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoRow++;							// UFOを下のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↓
				UfoInY = 1;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形右上  	入る方向↑  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 3 && UfoInX == 0 && UfoInY == -1) {




			UfoStepCount++;							// ステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoY=UfoY-1;						// Ｙ方向―１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX-1;						// Ｘ方向―１
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoX=UfoX-1;						// Ｘ方向―１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoColumn--;						// UFOを左のパネルに移動
				UfoInX = -1;						// 次のパネルに入る方向　←
				UfoInY = 0;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形右下  	入る方向↓  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 4 && UfoInX == 0 && UfoInY == 1) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoY=UfoY+1;						// Ｙ方向＋１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX-1;						// Ｘ方向―１
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoX=UfoX-1;						// Ｘ方向―１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoColumn--;						// UFOを左のパネルに移動
				UfoInX = -1;						// 次のパネルに入る方向　←
				UfoInY = 0;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形右下  	入る方向→  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 4 && UfoInX == 1 && UfoInY == 0) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoX=UfoX+1;						// Ｘ方向＋１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX+1;						// Ｘ方向＋１
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoRow--;							// UFOを上のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↑
				UfoInY = -1;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形左下  	入る方向↓  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 5 && UfoInX == 0 && UfoInY == 1) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoY=UfoY+1;						// Ｙ方向＋１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX+1;						// Ｘ方向＋１
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoX=UfoX+1;						// Ｘ方向＋１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoColumn++;						// UFOを右のパネルに移動
				UfoInX = 1;							// 次のパネルに入る方向　→
				UfoInY = 0;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形左下  	入る方向←  --------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 5 && UfoInX == -1 && UfoInY == 0) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoX=UfoX-1;						// Ｘ方向―１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX-1;						// Ｘ方向―１
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoRow--;							// UFOを上のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↑
				UfoInY = -1;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形左上  	入る方向↑	--------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 6 && UfoInX == 0 && UfoInY == -1) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoY=UfoY-1;						// Ｙ方向―１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX+1;						// Ｘ方向＋１
				UfoY=UfoY-1;						// Ｙ方向―１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoX=UfoX+1;						// Ｘ方向＋１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoColumn++;						// UFOを右のパネルに移動
				UfoInX = 1;							// 次のパネルに入る方向　→
				UfoInY = 0;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		// パネル 円形左上  	入る方向 ←	--------------------------------------------
		else if (Map[UfoRow][UfoColumn] == 6 && UfoInX == -1 && UfoInY == 0) {




			UfoStepCount++;							// パネル内のステップカウント
			if (UfoStepCount <= 11)					// ステップカウント11まで
				UfoX=UfoX-1;						// Ｘ方向―１
			else if (UfoStepCount <= 31) {			// ステップカウント12～31まで
				UfoX=UfoX-1;						// Ｘ方向―１
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			else if (UfoStepCount <= 41) {			// ステップカウント32～41まで
				UfoY=UfoY+1;						// Ｙ方向＋１
			}
			if (UfoStepCount == 41) {				// パネルの端に到達
				UfoRow++;							// UFOを下のパネルに移動
				UfoInX = 0;							// 次のパネルに入る方向　↓
				UfoInY = 1;
				UfoStepCount = 0;					// ステップカウント０クリア
			}
		}
		else										// 入る方向とパネルが一致しない場合
			gameoverProcess( );						// ゲームオーバー処理
	}
	// ゲームクリア処理 -----------------------------------------------------------------
	private void gameclearProcess( ) { 
			GameClearFlag = true;					// ゲームクリア
			BGM.stop( );							// BGMストップ
			if (Speed < 100)						// スピードが100より小さい場合
				Speed += 10;						// スピードアップ
	}
	// ゲームオーバー処理 ---------------------------------------------------------------
	private void gameoverProcess( ) {	
			GameOverFlag = true;					// ゲームオーバーフラグをオン
			BGM.stop( );							// BGMストップ
			GameoverSound.play( );					// ゲーム終了ゴング
	}

	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button bt = (Button)evt.getSource( );
		if (bt == StartButton) {					// スタートボタン
			if (GameOverFlag == true || GameClearFlag == true)
				// ゲームオーバーまたはゲームクリアの場合
				gameStart( );						// ゲームスタート
		}
		requestFocus( );							// キー入力フォーカスを要求
	}
	// ゲーム開始処理 -------------------------------------------------------------------
	private void gameStart( ) {
		GameOverFlag = false;						// ゲームオーバーフラグ設定
		GameClearFlag = false;						// ゲームクリアフラグ設定
		GameStartFlag = true;						// ゲームスタートフラグ設定
		mapset( );									// 新しいマップ設定
		UfoInitial( );								// UFO再初期化
		requestFocus( );							// キー入力フォーカスを要求
		BGM.loop( );								// ＢＧＭスタート
	}
   	// KeyListenerインタフェースの各メソッドを定義 --------------------------------------
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode( )) {	
		    case KeyEvent.VK_DOWN  :				// 下キー
                change(SpaceRow+1, SpaceColumn);	// 下と交換
                break;
	        case KeyEvent.VK_UP    :				// 上キー
                change(SpaceRow-1, SpaceColumn);	// 上のパネルと交換
                break;
		    case KeyEvent.VK_RIGHT : 				// 右キー
                change(SpaceRow, SpaceColumn+1);	// 右のパネルと交換
                break;
	        case KeyEvent.VK_LEFT  : 				// 左キー
                change(SpaceRow, SpaceColumn-1);	// 左のパネルと交換
                break;
	    }
		repaint( );									// 再描画
	}
	public void keyReleased(KeyEvent evt) { }
	public void keyTyped(KeyEvent evt) { }
	// ターゲットのパネルをスペースに移動 -----------------------------------------------
	private void change(int tpR, int tpC) {
		// ターゲットの行と列がUfoの行と列でなく，mapの範囲内であれば
		if (!(tpR == UfoRow && tpC == UfoColumn)
		 && tpR >= 0 && tpR <= 3 && tpC >= 0 && tpC <= 3) {
			int temp = Map[tpR][tpC];				// ターゲットのパネルとスペースを交換
			Map[tpR][tpC] = Map[SpaceRow][SpaceColumn];
			Map[SpaceRow][SpaceColumn] = temp;
			SpaceRow = tpR;							// ターゲット位置をスペース位置に設定
			SpaceColumn = tpC;
		}
	}
}
