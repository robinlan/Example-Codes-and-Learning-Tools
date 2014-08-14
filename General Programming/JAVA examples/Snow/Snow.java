import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, MediaTracker

public class Snow extends Applet implements Runnable {
	Thread thread;									// スレッド
	Image SnowImage;								// 雪イメージ
	Image BackImage;								// 背景イメージ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int SnowWidth, SnowHeight;						// 雪の幅と高さ
	int MAX = 500;									// 設定可能な雪の最大数
	boolean SnowFlag[ ] = new boolean[MAX];			// 雪のフラグ
	int SnowMax = 100;								// 雪の最大デフォルト数(Max500)
	int SnowX[ ] = new int[MAX];					// 雪の位置
	int SnowY[ ] = new int[MAX];
	int SnowSpeed[ ] = new int[MAX];				// 雪の落ちるスピード
	int SnowYure[ ] = new int[MAX];					// 雪の横揺れ
	int Speed, Yure;								// 雪のスピードと横揺れ
	int SnowAppear = 2000;							// 雪の発生確率 SnowAppear分の1～1
	int SleepTime = 50;								// スレッドのスリープタイム

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		// パラメータ
		SnowMax = Integer.parseInt(getParameter("max"));	// 雪の最大数
		if (SnowMax > MAX)									// 配列の最大まで
			SnowMax = MAX;
		Speed = Integer.parseInt(getParameter("speed"));	// 雪のスピード
		Yure = Integer.parseInt(getParameter("yure"));		// 雪の横揺れ

		SnowImage = getImage(getCodeBase( ), getParameter("snowimage"));// 雪のイメージ
		BackImage = getImage(getCodeBase( ), getParameter("backimage"));// 背景イメージ

		// 入力画像をメディアトラッカにセット
		MediaTracker mediatracker = new MediaTracker(this);		// メディアトラッカ
		mediatracker.addImage(BackImage, 0);
		mediatracker.addImage(SnowImage, 0);
		try {
			mediatracker.waitForID(0);				// 画像の入力が完了するまで待つ
		}
		catch (InterruptedException e) {
			showStatus(" "+e);
		}

		// 画像の幅と高さ
		SnowWidth = SnowImage.getWidth(this);		// 雪の幅
		SnowHeight = SnowImage.getHeight(this);		// 雪の高さ

		for (int i = 0; i < SnowMax; i++)			// 雪の初期化
			SnowFlag[i] = false;
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			//作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			Making( );								// 雪の降るシーンを作成
			repaint( );
			try {
				thread.sleep(SleepTime);			// スレッドをスリープ
			} catch (InterruptedException e) {
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;
	}
	// シーン作成 -----------------------------------------------------------------------
	void Making( ) {
		if (BackImage == null) {					// 背景画像がない場合は背景を黒で描画
			WorkGraphics.setColor(Color.black);
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		}
		else
			WorkGraphics.drawImage(BackImage, 0, 0, this);	// 背景画像描画

		for (int p = 0; p < SnowMax; p++) {			// 雪の数だけループ
			if (SnowFlag[p] == true) {				// 雪の発生がオンの場合
				// スピードと横揺れを考慮して位置を計算
				int x = (int)(SnowX[p] + 
						Math.sin((SnowY[p]+SnowSpeed[p])*3.14/180) * SnowYure[p]);
				int y = SnowY[p];
				SnowY[p] += SnowSpeed[p];			// 雪を下に落とす
				if (SnowY[p] > AppletHeight)		// アプレット画面より下に落ちた場合
					SnowFlag[p] = false;			// 雪を消去

				WorkGraphics.drawImage(SnowImage, x, y, this);	// 雪を描画
			}
			else
				SnowMake(p);						// 新しく雪の位置等の計算をする
		}
	}
	// 雪作成 ---------------------------------------------------------------------------
	void SnowMake(int p) {
		if ((int)(Math.random( ) * SnowAppear) == 0) {
			// 雪をSnowAppear分の１の確率でランダムに発生
			SnowX[p] = AppletWidth * p / SnowMax;				// 発生水平位置
			SnowY[p] = -(int)(Math.random( ) * 100);			// 垂直位置乱数発生
			SnowSpeed[p] = Speed + (int)(Math.random( ) * 5);	// 落ちるスピード
			SnowYure[p] = (int)(Math.random( ) * Yure);			// 雪の横揺れ
			SnowFlag[p] = true;									// 雪の発生フラグオン
			if (SnowAppear > 1)						// 発生確率計算用の値が１より大の場合
				SnowAppear--;						// 雪の発生確率を高くする
		}
	}
}
