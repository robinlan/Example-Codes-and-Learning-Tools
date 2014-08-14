import java.applet.*;								// Applet, AudioClip
import java.awt.*;									// Graphics, Image, MediaTracker, Color
import java.awt.event.*;							// MouseListener, MouseEvent

public class PanelMatch extends Applet implements Runnable, MouseListener {
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	Image panel[ ] = new Image[9];					// 8種類のパネルのイメージ
	int Size;										// パネルサイズ
	int Table[ ][ ] = new int[4][4];				// パネルの画像番号を格納したテーブル
	int Status[ ][ ] = new int[4][4];				// パネルの状態を格納したテーブル
	int OpenPanel[ ][ ] = new int[2][2];			// オープンした位置を保管
	int Count;										// めくった回数
	int Seikaisu;									// 正解数
	int GameFlag;									// ゲームフラグ
	Thread thread = null;							// スレッド
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
    AudioClip ClickSound, SeikaiSound;				// クリックサウンド，正解サウンド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィック取得
        ClickSound = getAudioClip(getCodeBase( ), "sound/click.au");		// クリック音
        SeikaiSound = getAudioClip(getCodeBase( ), "sound/seikai.au");	// 正解音

		MediaTracker mediatracker = new MediaTracker(this);		// メディアトラッカ
		for (int n = 0; n < 9; n++) {				// パネル画像入力
			panel[n] = getImage(getCodeBase( ), getParameter("panel" + n));
			mediatracker.addImage(panel[n], 0);		// 画像をメディアトラッカにセット
		}
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
		}
		Size = panel[0].getWidth(this);				// パネルサイズ

		addMouseListener(this);						// マウスリスナ追加
		shake( );									// 各パネルの位置をランダムに設定
		Count = 0;									// 2枚セットで裏返すときのカウント
		Seikaisu = 0;								// 正解パネル数
		GameFlag = 1;								// ゲームフラグ
	}
	// シェイク処理 ---------------------------------------------------------------------
	public void shake( ) {
		// PanelNumberに8種類のパネルの値1～8をセット
		int n = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				Table[i][j] = n;
				Table[i+2][j] = n++;
			}
		}

		// ランダムに並び替える
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int ip = (int)(Math.random( )*4);
				int jp = (int)(Math.random( )*4);
				int w = Table[i][j];				// 3ステップで交換
				Table[i][j] = Table[ip][jp];
				Table[ip][jp] = w;
			}
		}

		// 状態セット
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				Status[i][j] = 0;

		// 表示
		WorkGraphics.setColor(Color.black);			// 作業グラフィックスに描画
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		for (int i = 0; i < 4; i++)					// 隠しパネル描画
			for (int j = 0; j < 4; j++)
				WorkGraphics.drawImage(panel[0], j * Size, i * Size, this);
	}
	// アプレット開始 -------------------------------------------------------------------
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
		while (thread != null) {					// スレッドが存在している間
			try {
				thread.sleep(200);					// スレッド200ミリ秒スリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}

			if (Count == 2) {						// 2枚目を裏返したとき
				if (Table[OpenPanel[0][0]] [OpenPanel[0][1]]		// 一致した場合
					== Table[OpenPanel[1][0]] [OpenPanel[1][1]]) {
					SeikaiSound.play( );			// 正解音
					Seikaisu++;						// 正解パネルの種類の数
					if (Seikaisu == 8)
						thread = null;				// スレッドを無効にして，ゲーム終了
				}
				else {								// 一致しなかった場合
					try {
						thread.sleep(2000);			// スレッドを２秒間スリープ
					} catch (InterruptedException e) {	// 他のスレッドの割り込み例外処理
						showStatus(" "+e);
					}
					for (int n = 0; n < 2; n++) {	// 隠しパネルに戻す
						WorkGraphics.drawImage(panel[0],
							OpenPanel[n][1] * Size, OpenPanel[n][0] * Size, this);
						Status[OpenPanel[n][0]][ OpenPanel[n][1]] = 0;	// 元に戻す
					}
					repaint( );						// 再描画
				}
				Count = 0;							// 裏返しカウントをクリア
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
	// MouseListenerインターフェースを実装 ----------------------------------------------
    public void mouseReleased(MouseEvent evt) {
        evt.consume( );								// イベントを消費
		if (Count < 2) {							// countが0と1の場合
			int j = evt.getX( ) / Size;				// クリック位置を配列の位置に変換
			int i = evt.getY( ) / Size;
			if (Status[i][j] == 0) {				// 裏返っている場合
				Status[i][j] = 1;					// 表にする
				OpenPanel[Count][0] = i;			// オープンした位置を保管
				OpenPanel[Count][1] = j;
				// オープンした位置の画像を描画
				WorkGraphics.drawImage(panel[Table[i][j]],
										j * Size, i * Size, this);
				WorkGraphics.drawRect(j * Size, i * Size, Size, Size);
				ClickSound.play( );					// クリック音
				repaint( );							// アプレット画面を再描画
				Count++;							// 裏返しカウントを+1
			}
		}
	}
    public void mouseClicked(MouseEvent evt) { }
    public void mousePressed(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
}
