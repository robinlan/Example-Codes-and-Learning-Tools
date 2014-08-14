import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, MediaTracker
import java.awt.image.*;							// MemoryImageSource, PixelGrabber

public class Kaleidoscope extends Applet implements Runnable {

	Image WorkImage1, WorkImage2;					// 作業用イメージ
	Graphics WorkGraphics1, WorkGraphics2;			// 作業用グラフィックス
	Image MirrorImage0, MirrorImage1, MirrorImage2, MirrorImage3;
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int ImageWidth, ImageHeight;					// 画像の幅と高さ
	Thread thread = null;							// スレッド
	int[ ]  Pixels1  =  new  int[100  *  100];		// ピクセルデータ保管(100 x 100)
	int[ ]  Pixels2  =  new  int[100  *  100];
	int[ ]  Pixels3  =  new  int[100  *  100];

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットのサイズ取得
		AppletHeight = getSize( ).height;
		// 第1段階用作業イメージ領域設定
		WorkImage1 = createImage(AppletWidth, AppletHeight);// 作業用イメージ作成
		WorkGraphics1 = WorkImage1.getGraphics( );			// 作業用グラフィックス取得
		// 第2段階用作業イメージ領域設定
		WorkImage2 = createImage(AppletWidth, AppletHeight);// 作業用イメージ作成
		WorkGraphics2 = WorkImage2.getGraphics( );			// 作業用グラフィックス取得

		MediaTracker mediatracker = new MediaTracker(this);		// メディアトラッカ生成
		String imagefile = getParameter("image");
		MirrorImage0 = getImage(getCodeBase( ), imagefile);		// 画像データ入力
		mediatracker.addImage(MirrorImage0, 0);		// 画像をメディアトラッカにセット
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch (InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}

		ImageWidth = MirrorImage0.getWidth(this);	// イメージの幅と高さ
		ImageHeight = MirrorImage0.getHeight(this);
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage2, 0, 0, this);		//第2段階の作業イメージを描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		int dispX = 0, dispY = 0;					// 表示位置
		int moveX = -1, moveY = -1;					// 移動ステップ

		while(thread != null) {						// スレッドが存在している間
			try {
				thread.sleep(100);					// スレッドスリープ
			} catch (InterruptedException e){		// 割り込み例外処理
				showStatus(" "+e);
			}

			MirrorProcess(dispX, dispY);			// ミラー処理

			dispX += moveX;							// x軸方向の画像の表示位置移動
			if (dispX <= -(ImageWidth - 100) || dispX >= 0)		// 表示位置が範囲外の場合
				moveX *= (-1);						// x方向の移動ステップを反転
			dispY += moveY;							// y軸方向の画像の表示位置移動
			if (dispY == -(ImageHeight - 100) || dispY == 0)	// 表示位置が範囲外の場合
				moveY *= (-1);						// y方向の移動ステップを反転

			repaint( );
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// ミラー処理 -----------------------------------------------------------------------
	public void MirrorProcess(int dispX, int dispY) {
		// 原画を第1段階の作業グラフィックスに描く
		WorkGraphics1.drawImage(MirrorImage0, dispX, dispY, this);

		// 原画からピクセル情報を取り込む
		GetPixels(MirrorImage0, -dispX, -dispY, 100, 100, Pixels1);
		// 左右対称になるようにデータを入れ替える
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100 / 2; j++) {
				int temp = Pixels1[i*100 + j];
				Pixels1[i*100 + j] = Pixels1[i*100 + 100 - 1 - j];
				Pixels1[i*100 + 100 - 1 - j] = temp;
			}
		}
		// 編集処理した配列のピクセル情報からイメージを作成
		MirrorImage1 = createImage(new MemoryImageSource(100, 100, Pixels1, 0, 100));
		// 作成したイメージを第1段階の作業グラフィックスの右上に描画
		WorkGraphics1.drawImage(MirrorImage1, 100, 0, this);

		// 作成した右上の画像からピクセル情報を取り込む
		GetPixels(MirrorImage1, 0, 0, 100, 100, Pixels2);
		// 上下対称になるようにデータを入れ替える
		for (int i = 0; i < 100 / 2; i++) {
			for (int j = 0; j < 100; j++) {
				int temp = Pixels2[i*100 + j];
				Pixels2[i*100 + j] = Pixels2[(100 - 1 - i)*100 + j];
				Pixels2[(100 - 1 - i)*100 + j] = temp;
			}
		}
		// 編集処理した配列のピクセル情報からイメージを作成
		MirrorImage2 = createImage(new MemoryImageSource(100, 100, Pixels2, 0, 100));
		// 作成したイメージを第1段階の作業グラフィックスの右下に描画
		WorkGraphics1.drawImage(MirrorImage2, 100, 100, this);

		// 作成した右下の画像からピクセル情報を取り込む
		GetPixels(MirrorImage2, 0, 0, 100, 100, Pixels3);
		// 左右対称になるようにデータを入れ替える
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100 / 2; j++) {
				int temp = Pixels3[i*100 + j];
				Pixels3[i*100 + j] = Pixels3[i*100 + 100 - 1 - j];
				Pixels3[i*100 + 100 - 1 - j] = temp;
			}
		}
		// 編集処理した配列のピクセル情報からイメージを作成
		MirrorImage3 = createImage(new MemoryImageSource(100, 100, Pixels3, 0, 100));
		// 作成したイメージを第1段階の作業グラフィックスの左下に描画
		WorkGraphics1.drawImage(MirrorImage3, 0, 100, this);

		// 第1段階の作業イメージを第2段階の作業グラフィックスに描画
		WorkGraphics2.drawImage(WorkImage1, 0, 0, this);
	}
	// 画像イメージの各ピクセル情報を取得 -----------------------------------------------
	public void GetPixels(Image img, int x, int y, int w, int h, int pixels[ ]) {
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
						// (画像イメージ，開始位置ｘ，ｙ，幅, 高さ，配列，格納位置，横幅)
        try  {
			pg.grabPixels( );						// ピクセル情報取込み
        }  catch  (InterruptedException  e)  {		// grabPixels( )に対する例外処理
            showStatus(" "+e);
        }
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
	}
}
