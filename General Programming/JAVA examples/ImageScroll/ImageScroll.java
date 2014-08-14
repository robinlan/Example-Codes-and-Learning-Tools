import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, MediaTracker

public class ImageScroll extends Applet implements Runnable {
	Thread thread = null;							// スレッド
	int DispX;										// 水平表示位置
	int DispN;										// 左端に表示する画像番号
	int Number;										// 画像数
	int	Size;										// 画像横サイズ
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	Image image[ ] = new Image[30];					// 画像データ数max30
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	MediaTracker mediatracker = new MediaTracker(this);	// メディアトトラッカ生成

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// アプレットサイズ
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得
		// パラメータ入力
		Number = Integer.parseInt(getParameter("number"));	// 画像数
		// データ入力
		for (int i = 0; i < Number; i++) {			// 画像読み込む
			image[i] = getImage(getCodeBase( ), getParameter("image" + i));
			mediatracker.addImage(image[i], 0);		// メディアトラッカにセット
		}
		// 初期設定
		DispX = 0;									// 表示スタートポイント
		DispN = 0;									// 表示画像番号
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		// メディアトラッカが完了していない場合（画像入力が完了していない場合）
	    if (mediatracker.statusID(0, true) != MediaTracker.COMPLETE) {
			g.drawString("Now loading ... ", 20, 20);
			return;
		}

		Size = image[0].getWidth(this);				// 画像横サイズ
		DispX -= 2;									// 画像を左に2ポイント移動
		// 左端の表示画像がアプレットの画面から左に出た場合
		if (DispX <= -Size) {
			DispX = 0;								// 水平表示位置を０
			DispN++;								// 表示する画像を次の画像にする
			if (DispN >= Number)					// 最終画像を超えた場合
				DispN = 0;							// 最初の画像にする
		}

		// 表示枠内に入る画像のみを表示
		for (int i = 0; i < Number && DispX + i * Size < AppletWidth; i++)
			WorkGraphics.drawImage(
				image[(DispN + i) % Number], DispX + i * Size + 1, 0, this);
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while(thread != null) {						// スレッドが存在している間
			repaint( );								// 再描画
			try {
				thread.sleep(100);					// スレッドを100ミリ秒スリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}
		}
		thread = null;
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
	}
}
