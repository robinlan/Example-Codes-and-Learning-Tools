import java.applet.*;			// Applet, AudioClip
import java.awt.*;				// Graphics, Image, Color, Font, Button etc
import java.awt.event.*;		// ActionListener, ActionEvent, KeyListener, KeyEvent

public class DropBlock extends Applet
					implements Runnable, KeyListener, ActionListener {
    Thread thread;									// スレッド
	int Block[ ][ ][ ] =							// ブロックの形状データ
		{
			{	{0, 0, 0},
				{0, 1, 0},
				{1, 1, 1}
			},
			{	{0, 1, 0},
				{0, 1, 0},
				{0, 1, 0}
			},		
			{	{0, 0, 0},
				{0, 0, 1},
				{1, 1, 1}
			},		
			{	{0, 0, 0},
				{1, 0, 0},
				{1, 1, 1}
			}
		};

	int VPoint,										// 垂直方向(vertical)の位置
		HPoint,										// 水平方向(horizontal)の位置
		Number;										// ブロック番号
	boolean EventFlag;								// イベント発生フラグ
	int StageWidth = 20;							// ステージの幅(Max:20)
	int StageHeight = 20;							// ステージの高さ(Max:20)
	int Stage[ ][ ]
		= new int[StageHeight][StageWidth];			// ステージの内容
	Image PanelImage[ ] = new Image[2];				// パネルイメージ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int OK = 1;										// OK値
	int Count;										// 点数
	int Size;										// パネルサイズ
	AudioClip MoveSound, RotateSound, DropSound;	// サウンド（移動音，回転音，落下音）

	Button StartButton;								// スタートボタン
	boolean GameFlag = false;						// true:ゲーム開始	false:ゲーム終了

	Font font = new Font("System", Font.PLAIN, 20);
	FontMetrics fontmetttrics = getFontMetrics(font);

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// パラメータ取得
		StageWidth = Integer.parseInt(getParameter("StageWidth"));		// ステージの横列
		StageHeight = Integer.parseInt(getParameter("StageHeight"));	// ステージの縦行

        MoveSound = getAudioClip(getCodeBase( ), "sound/click.au");		// 左右移動音
        RotateSound = getAudioClip(getCodeBase( ), "sound/kaiten.au");	// 回転音
        DropSound = getAudioClip(getCodeBase( ), "sound/drop.au");		// 落下音

		// 画像入力を監視するメディアトラッカー生成
		MediaTracker mediatracker = new MediaTracker(this);
		for (int i = 0; i < 2; i++) {				// ブロックと背景画像入力
			PanelImage[i] = getImage(getCodeBase( ), "image/panel"+i+".gif");
			mediatracker.addImage(PanelImage[i], 0);// メディアトラッカにセット
		}

		try {
			mediatracker.waitForID(0);				// メディアトラッカの入力完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}

		Size = PanelImage[0].getWidth(this);		// Panelサイズ
		// 作業イメージ領域確保
		WorkImage = createImage(StageWidth * Size, (StageHeight + 1) * Size);
		WorkGraphics = WorkImage.getGraphics( );
		WorkGraphics.setFont(font);

		StartButton = new Button("START");			// スタートボタン生成
		add(StartButton);							// ボタンをアプレットに付加
		StartButton.addActionListener(this);		// ボタンにリスナー追加
		setLayout(null);							// レイアウトを自由設定
		StartButton.setBounds((StageWidth * Size - 60) / 2,
							  (StageHeight * Size - 30) / 2,
							  60, 30);				// ボタンの位置とサイズ設定

		addKeyListener(this);						// キーリスナー追加
		StageDisp( );								// ステージ描画
	}
	// ゲーム初期化 ---------------------------------------------------------------------
	private void GameInitial( ) {
		Count = 0;									// 点数カウント	
		EventFlag = false;							// キーイベントフラグ
        VPoint = -2;								// 開始ポイントを画面の上にする
		HPoint = StageWidth / 2 - 1;				// 開始ポイントを中央にする
		Number = (int)(Math.random( )*4);			// ランダムにブロック番号(0～3)発生
		GameFlag = true;							// ゲームスタートフラグをオン
		requestFocus( );							// 入力フォーカスを要求
		StageClear( );								// ステージをクリア
		StageDisp( );								// ステージを描画
	}
	// ステージクリア -------------------------------------------------------------------
	private void StageClear( ) {
		for (int i = 0; i < StageHeight; i++)		// ステージ縦方向
			for (int j = 0; j < StageWidth; j++)	// 横方向
				Stage[i][j] = 0;					// 配列ステージを０クリア
	}
	// ステージ描画 ---------------------------------------------------------------------
	public void StageDisp( ) {
		for (int i = 0; i < StageHeight; i++)		// ステージ縦方向
			for (int j = 0; j < StageWidth; j++)	// 横方向
				WorkGraphics.drawImage(PanelImage[Stage[i][j]],j*Size, i*Size, this);
	}
	// アプレット開始 -------------------------------------------------------------------
    public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
    }
	// ブロック表示 ---------------------------------------------------------------------
    public void BlockDisp(int sw) { 				// 0:背景  1:ブロック
		for (int i = 0; i < 3; i++)					// 縦方向
			for (int j = 0; j < 3; j++)				// 横方向
				if (Block[Number][i][j] == 1)		// ブロックの場合 && VPoint + i >= 0)
					WorkGraphics.drawImage(PanelImage[sw],
						(HPoint +j)*Size, (VPoint + i)*Size, this);
    }
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g) {
		// 得点表示
		WorkGraphics.setColor(new Color(131, 147, 202));
		WorkGraphics.fillRect(0, StageHeight * Size, StageWidth * Size, Size);
		WorkGraphics.setColor(new Color(255, 255, 255));
		String information = Count + " Point";
		int MessageLength = fontmetttrics.stringWidth(information);
		WorkGraphics.drawString(information,		// 得点表示の右端をそろえる
			(StageWidth * Size - MessageLength) / 2, (StageHeight + 1) * Size - 5);

		// ステージ表示
		g.drawImage(WorkImage, 0, 0, this);
    }
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ) {
		while(thread != null) {						// スレッドが存在している間
			syori( );								// ブロック落下処理
			repaint( );
			try {
				if (Count > 400)					// 400点以上はゲームスピードを一定
					thread.sleep(100);
				else
					thread.sleep(500 - Count);		// ゲームスピードを徐々に速くする
	        } catch (InterruptedException e) {		// sleepに対しての割り込み例外処理
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
	// ブロック処理 ---------------------------------------------------------------------
	private void syori( ) {
		if (GameFlag == true) {						// ゲーム中の場合
			if (EventFlag == false) {				// キーが押されていない場合
				//  一つ下に落下した場合のチェック
				if (BlockCheck(HPoint, VPoint + 1) == OK) {  // ぶつからない場合
					BlockDisp(0);					// 前回位置クリア
					VPoint++;						// 一つ下に落下
					BlockDisp(1);					// 新しい位置でブロック表示
					repaint( );
				}
				else {								// ぶつかる場合

					boolean HitFlagInStage = true;	// ステージ内でぶつかったと仮定
					// ブロックを現在のステージの位置に配置
					for (int i = 0; i < 3; i++)	{
						for (int j = 0; j < 3; j++) {
							if (Block[Number][i][j] == 1) {
								if(VPoint + i >= 0)	// 垂直方向が場内の場合
									Stage[VPoint+i][HPoint+j] = 1;
								else {				// ステージ外の場合
									GameFlag = false;				// ゲームオーバー
									StartButton.setVisible(true);	// スタートボタン表示
									HitFlagInStage = false;// ステージ内の衝突フラグクリア
									break;
								}
							}
						}
					}

					if (HitFlagInStage == true) {		// ステージ内でぶつかった場合
						LineCheck( );					// ラインチェック
						// 新しいブロック作成
						VPoint = -3;					// 垂直方向の位置はステージの外
						HPoint = StageWidth / 2 - 1;	// 水平方向の位置はステージの中央
						Number = (int)(Math.random( )*4);// ランダムにブロック(0～3)発生
					}
				}
			}
        }
		EventFlag = false;							// イベントフラグをクリア
		// EventFlagがfalseであれば，キー入力がなかったということを表す
	}
	// ラインチェック -------------------------------------------------------------------
	public void LineCheck( ) {
		// 現在のブロックのベースポイント＋２から（ただし，StageHeight－１以下）
		// 上方向にラインを調べてブロックがあるかチェック
		int sp = VPoint + 2;
		if (sp > StageHeight - 1)
			sp = StageHeight - 1;

		for (int i = sp; i > 0; i--) {
			// 1ラインチェック
			int cnt = 0;
			for (int j = 0; j < StageWidth; j++)
				cnt += Stage[i][j];

			if (cnt == 0)							// すべてスペースの場合
				break;

			if (cnt == StageWidth) {
				Count += cnt;						// 得点カウント
				int flag = 1;

				// 1ラインすべてブロックの場合
				// その行の上のラインを下に落とす
				// すべてスペースのラインを落とした場合は，落とす処理を終了
				int p, sw;
				for (p = i; p > 0 && flag == 1; p--) {
					flag = 0;						// 1ラインすべてスペースであると仮定
					for (int j = 0; j < StageWidth; j++) {
						Stage[p][j] = Stage[p-1][j];
						if (Stage[p][j] == 1) {		// ブロックがあった場合
							sw = 1;
							flag = 1;  				// さらに上を繰り返す
						}
						else
							sw = 0;
						WorkGraphics.drawImage(PanelImage[sw], j*Size, p*Size, this);
					}
				}

				if (p == 0) {
					// 1段目までチェックしてスライドした場合,最上段にスペースを入れる
					for (int j = 0; j < StageWidth; j++) {
						Stage[0][j] = 0;
						WorkGraphics.drawImage(PanelImage[0], j * Size, p * Size, this);
					}
				}
			i++;		// １ライン落としたので,再度同じラインをチェックする
			}
		}
	}
	// KeyListenerインタフェースの各メソッドを定義 --------------------------------------
	public void keyTyped(KeyEvent evt) { }
	public void keyReleased(KeyEvent evt) {  }
	public void keyPressed(KeyEvent evt) {
		EventFlag = true;							// キーイベントフラグオン
		switch (evt.getKeyCode( )) {
	        case KeyEvent.VK_UP    : RotateSound.play( ); kaiten( ); break; // 上キー
		    case KeyEvent.VK_DOWN  : DropSound.play( ); Drop( );   break; 	// 下キー
	        case KeyEvent.VK_LEFT  : MoveSound.play( ); Left( );   break; 	// 左キー
		    case KeyEvent.VK_RIGHT : MoveSound.play( ); Right( );  break;	// 右キー
		}
	}
	// 回転処理 -------------------------------------------------------------------------
	public void kaiten( ) {
		int work[ ][ ] = new int [3][3];
		int temp[ ][ ] = new int [3][3];

		BlockDisp(0);								// ブロック消去
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				temp[i][j] = Block[Number][i][j];	// 保管
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				work[j][3 - i - 1] = Block[Number][i][j];// 作業領域に回転セット
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				Block[Number][i][j] = work[i][j];	// 回転した内容をセット
		if (BlockCheck(HPoint, VPoint) == OK) {		// ブロックが置けるかチェック
		}
		else {										// 置けない場合
			for (int i = 0; i < 3; i++)				// 元に戻す
				for (int j = 0; j < 3; j++)
					Block[Number][i][j] = temp[i][j];
		}
		BlockDisp(1);								// ブロック表示
		repaint( );									// 再描画
	}
	// ブロック落下 ---------------------------------------------------------------------
	public void Drop( ) {
		while (BlockCheck(HPoint, VPoint + 1) == OK) {	// １つ下にブロックが置ける場合
			BlockDisp(0);							// 現在の位置のブロックを消去
			VPoint++;								// 垂直方向の位置を1つ下に
			BlockDisp(1);							// ブロックを表示
			repaint( );								// 再描画
		}
	}
	// ブロックチェック -----------------------------------------------------------------
	public int BlockCheck(int hp, int vp) {
		int flag = 1;
		for (int i = 0; i < 3 && flag == 1; i++) {
			for (int j = 0; j < 3 && flag == 1; j++) {
				// 落下ブロックがステージのブロックとぶつかった場合
				if (vp + i >= 0 && vp + i < StageHeight &&
					hp + j >= 0 && hp + j < StageWidth) {
					if (Stage[vp + i][hp + j] == 1 && Block[Number][i][j] == 1)
						flag = 0;
				}

				// 落下ブロックがステージの場外に出た場合
				if (Block[Number][i][j] == 1 &&
					(hp + j < 0 || hp + j > StageWidth - 1 ||
					 vp + i > StageHeight - 1) )
					flag = 0;
			}
		}
		return(flag);
	}
	// 右移動処理 -----------------------------------------------------------------------
	public void Right( ) {
		if (BlockCheck(HPoint + 1, VPoint) == OK) {	// 右に移動できる場合
			BlockDisp(0);							// ブロック消去
			HPoint++;								// 水平方向右に移動
			BlockDisp(1);							// ブロック表示
			repaint( );								// 再描画
		}
	}
	// 左移動処理 -----------------------------------------------------------------------
	public void Left( ) {
		// 左に行ったときにステージのブロックとぶつからないかチェック
		// ブロックが場外に出ないかチェック
		if (BlockCheck(HPoint - 1, VPoint) == OK) {	// 左に移動できる場合
			BlockDisp(0);							// ブロック消去
			HPoint--;								// 水平方向左に移動
			BlockDisp(1);							// ブロック表示
			repaint( );								// 再描画
		}
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button bt = (Button)evt.getSource( );
		if (bt == StartButton) {					// スタートボタンが押された場合
			StartButton.setVisible(false);			// スタートボタンを隠す
			GameInitial( );							// ゲーム初期化処理
		}
	}
}
