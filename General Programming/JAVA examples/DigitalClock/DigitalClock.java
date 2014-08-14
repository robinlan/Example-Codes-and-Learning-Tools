import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, MediaTracker
import java.util.*;									// Calendar, TimeZone

public class DigitalClock extends Applet implements Runnable {
	Thread thread = null;
	Image Digit[ ] = new Image[11];					// 画像データ(0～9,:)
	int DigitWidth, DigitHeight;					// 画像データの幅と高さ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅と高さ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカー生成
		for(int i = 0; i < 11; i++) {				// 画像データを入力
			Digit[i] = getImage(getCodeBase( ), "image/" + i + ".gif");
			mediatracker.addImage(Digit[i], 1);		// メディアトラッカーに入力画像を設定
		}
		try {
			mediatracker.waitForID(1);				// 画像入力が完了するまで待つ
		} catch (InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}
		DigitWidth = Digit[0].getWidth(this);		// 画像サイズ
		DigitHeight = Digit[0].getHeight(this);
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
			DispTime( );							// 時刻描画
			repaint( );								// 再描画
			try{
				thread.sleep(100);					// スリープ
			} catch(InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 時間描画 -------------------------------------------------------------------------
	void DispTime( ) {
		// カレンダーオブジェクト作成
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		int hour = date.get(Calendar.HOUR);			// 現在時刻の時間取得
		int minute = date.get(Calendar.MINUTE);		// 現在時刻の分取得
		int second = date.get(Calendar.SECOND);		// 現在時刻の秒取得

		// 作業グラフィックに描画
		WorkGraphics.setColor(Color.white);
		WorkGraphics.fillRect(0, 0, DigitWidth*8, DigitHeight);
		int h1 = hour / 10,							// 時間の10の桁
			h2 = hour % 10;							// 時間の1の桁
		WorkGraphics.drawImage(Digit[h1],             0, 0, this);
		WorkGraphics.drawImage(Digit[h2], DigitWidth*1, 0, this);
		WorkGraphics.drawImage(Digit[10], DigitWidth*2, 0, this);
		int m1 = minute / 10,						// 分の10の桁
			m2 = minute % 10;						// 分の1の桁
		WorkGraphics.drawImage(Digit[m1], DigitWidth*3, 0, this);
		WorkGraphics.drawImage(Digit[m2], DigitWidth*4, 0, this);
		WorkGraphics.drawImage(Digit[10], DigitWidth*5, 0, this);
		int s1 = second / 10,						// 秒の10の桁
			s2 = second % 10;						// 秒の1の桁
		WorkGraphics.drawImage(Digit[s1], DigitWidth*6, 0, this);
		WorkGraphics.drawImage(Digit[s2], DigitWidth*7, 0, this);
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
			thread = null;							// スレッドを無効
	}
}
