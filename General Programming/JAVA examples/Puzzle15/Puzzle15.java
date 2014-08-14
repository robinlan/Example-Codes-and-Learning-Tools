import java.applet.*;								// Applet, AudioClip
import java.awt.*;									// Graphics, Image, Color, MediaTracker
import java.awt.event.*;							// MouseListener, MouseEvent

public class Puzzle15 extends Applet implements MouseListener, Runnable {
	Image panel[ ] = new Image[16];					// パネル画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int Table[ ][ ] = new int[4][4];				// 画像番号格納テーブル
	AudioClip ClickSound, SeikaiSound;				// クリック音，正解音
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	Thread thread = null;							// スレッド
	int ClickTate, ClickYoko;						// クリックパネル位置
	int SpaceTate, SpaceYoko;						// スペースパネル位置
	int MovePoint;									// 動作ポイント
	boolean MoveFlag;								// 動作フラグ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
        ClickSound = getAudioClip(getCodeBase( ), "sound/click.au");	// クリック音
        SeikaiSound = getAudioClip(getCodeBase( ), "sound/seikai.au");	// 正解音

		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカ生成
		for (int i = 0; i < 16; i++) {
			panel[i] = getImage(getCodeBase( ), "image/panel"+i+".gif");
			mediatracker.addImage(panel[i], 0);		// 画像をメディアトラッカにセット
		}
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		}
		catch (InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}

		Shake( );									// パネルをランダムに混ぜる
		DispPanel( );								// パネル表示
		MoveFlag = false;							// 動作フラグ
		addMouseListener(this);						// マウスリスナ追加
	}
	// シェイク処理 ---------------------------------------------------------------------
	public void Shake( ) {							// パネルの配置をランダムにする
		int count = 1;
		for (int tate = 0; tate < 4; tate++)		// パネルの番号を順にセット
			for (int yoko = 0; yoko < 4; yoko++)
				Table[tate][yoko] = count++;
		Table[3][3] = 0;							// 右下のパネルは白色

		// 完成から逆に崩していく方法
		// 空いている所から四方をランダムに選んで交換
		int tate1 = 3, yoko1 = 3;
		int tate2 = 0, yoko2 = 0;
		for (int c = 1; c <= 100; c++) {			// 100回交換（実際は100以下）
			int w = (int)(Math.random( ) * 4);		// 0～3の乱数発生
			switch (w) {
				case 0: // 上と交換
					tate2 = tate1 - 1;  yoko2 = yoko1; break;
				case 1: // 下と交換
					tate2 = tate1 + 1;  yoko2 = yoko1; break;
				case 2: // 右と交換
					tate2 = tate1;      yoko2 = yoko1 + 1; break;
				case 3: // 左と交換
					tate2 = tate1;      yoko2 = yoko1 - 1; break;
			}
			if (tate2 >= 0 && tate2 <= 3 && yoko2 >= 0 && yoko2 <= 3) { // 範囲内
				Change(tate1, yoko1,  tate2, yoko2);
				tate1 = tate2;
				yoko1 = yoko2;
			}
		}
	}
	// パネル描画 -----------------------------------------------------------------------
	void DispPanel( ) {
		// 全パネルを描画
		for (int tate = 0; tate < 4; tate++)
			for (int yoko = 0; yoko < 4; yoko++)
					WorkGraphics.drawImage(panel[Table[tate][yoko]],
											yoko*50, tate*50, this);
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (MoveFlag == true) {						// 動作フラグがオン　動作中
			MovePoint += 5;							// パネル移動
			if (MovePoint > 50) {					// パネルサイズ分移動した場合
				MoveFlag = false;					// 動作を停止　フラグオフ
				Change(ClickTate, ClickYoko, SpaceTate, SpaceYoko);

				if (Check( ) == true) {				// チェックしてすべて正解の場合
					SeikaiSound.play( );			// 正解音
					WorkGraphics.setColor(Color.red);	// 赤色でＯＫと描画
					WorkGraphics.drawString("OK", 170, 180);
				}
			}
			else {									// 移動の範囲内
				// 移動している部分のみ描画
				// パネルが移動する２箇所にスペース描画
				WorkGraphics.drawImage(panel[Table[SpaceTate][SpaceYoko]],
										ClickYoko*50, ClickTate*50, this);
				WorkGraphics.drawImage(panel[Table[SpaceTate][SpaceYoko]],
										SpaceYoko*50, SpaceTate*50, this);
				// クリックした移動パネル描画
				WorkGraphics.drawImage(panel[Table[ClickTate][ClickYoko]],
					ClickYoko * 50 + (SpaceYoko - ClickYoko) * MovePoint,
					ClickTate * 50 + (SpaceTate - ClickTate) * MovePoint, this);
			}
		}
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			repaint( );								// 再描画
			try {
				thread.sleep(50);					// スレッドをスリープ
			} catch (InterruptedException e){		// 他のスレッドの割り込み例外処理
				showStatus(" " + e);				// 例外エラー表示
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット終了 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
	}

	// パネル交換処理 -------------------------------------------------------------------
	public void Change(int tate1, int yoko1, int tate2, int yoko2) {	// 交換処理
		int w = Table[tate1][yoko1];				// 空白位置とクリックしたパネルを交換
		Table[tate1][yoko1] = Table[tate2][yoko2];
		Table[tate2][yoko2] = w;
	}
	// 正解チェック ---------------------------------------------------------------------
	public boolean Check( ) {						// 全パネル位置が合っているかチェック
		int count = 1;								// 数字チェック用カウント 
		boolean flag = true;						// 正解フラグをtrueに仮設定
		for (int tate = 0; flag == true && tate < 4; tate++) {
			for (int yoko = 0; flag == true && yoko < 4; yoko++) {
				if (tate == 3 && yoko == 3)
					break;
				if (Table[tate][yoko] != count)		// 違った場合，フラグをfalseにする
					flag = false;
				count++;
			}
		}
		return flag;
	}
	// MouseListenerインターフェースを実装-----------------------------------------------
    public void mousePressed(MouseEvent evt) {
		if (MoveFlag == true)						// 動いている途中の場合
			return;

        evt.consume( );								// イベントを消費

		// クリックされた2次元配列上のパネル位置
		int yoko = evt.getX( ) / 50;				// グラフィック座標位置を
		int tate = evt.getY( ) / 50;				// 2次元配列の位置に変換

		// 上下左右に空きがあるかチェック
		// あれば，クリックしたパネルをその方向に移動
		ClickTate = tate; ClickYoko = yoko;			// クリックしたパネルの位置
		MovePoint = 0;								// ムーブポイントをクリア
		if (tate - 1 >= 0 && Table[tate - 1][yoko] == 0)				// 上チェック
			{ MoveFlag = true; SpaceTate = tate - 1; SpaceYoko = yoko; }
		else if (tate + 1 <= 3 && Table[tate + 1][yoko] == 0)			// 下チェック
			{ MoveFlag = true; SpaceTate = tate + 1; SpaceYoko = yoko; }
		else if (yoko - 1 >= 0 && Table[tate][yoko - 1] == 0)			// 左チェック
			{ MoveFlag = true; SpaceTate = tate; SpaceYoko = yoko - 1; }
		else if (yoko + 1 <= 3 && Table[tate][yoko + 1] == 0)			// 右チェック
			{ MoveFlag = true; SpaceTate = tate; SpaceYoko = yoko + 1; }
	
		ClickSound.play( );							// クリック音
	}
    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
} 
