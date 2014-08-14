import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Font, MediaTracker

public class RollingMessage extends Applet implements Runnable {
	Image BackImage;								// 背景イメージ
	Image WorkImage; 								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int	AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int BackImageWidth, BackImageHeight;			// イメージの幅と高さ
	int	SleepTime;									// スリープタイム
	String Message;									// メッセージ
	Thread thread = null;							// スレッド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ領域確保
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		// パラメータより各種データ設定
		Message = getParameter("Message");		// メッセージのパラメータ取得
		SleepTime = Integer.parseInt(getParameter("SleepTime"));	// スリープタイム
		String imagefile = getParameter("BackImage");		// イメージファイル名取得
		BackImage = getImage(getCodeBase( ), imagefile);	// 画像イメージ取込み
		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカー生成
		mediatracker.addImage(BackImage, 0);		// メディアトラッカーにセット
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}
		BackImageWidth = BackImage.getWidth(this);	// 画像イメージの幅と高さ
		BackImageHeight = BackImage.getHeight(this);
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		Font font = new Font("TimesRoman", Font.PLAIN, 10);	// フォント設定
		WorkGraphics.setColor(Color.black);			// 作業グラフィックスの表示色を黒色
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight); // 黒色に塗りつぶす
		WorkGraphics.setColor(Color.white);			// 作業グラフィックスの表示色を白色
		int startAngle = 0;							// 描画開始角度
        while (thread != null) {					// スレッドが存在している間
			startAngle += 5;						// 描画開始角度を+5ずつ変える
		 	WorkGraphics.setColor(Color.black);		// 背景を黒で描画
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight); 
		 	WorkGraphics.setColor(Color.white);		// メッセージの描画色
			for (int n = 1; n <= 2; n++) {			// 裏側と表側の描画
				int DispPoint = 0;					// 描画文字の位置　先頭にセット
				for (int angle = startAngle; angle > 0; angle -= 5) {
					if ((n == 1 && ((angle % 360 < 90) || (angle % 360 > 270)))
													// 1回目は，裏側の文字を描画
					 ||	(n == 2 && ((angle % 360 >= 90) && (angle % 360 <= 270)))) {
													// 2回目は，表側の文字を描画
						float vs = (float)Math.sin(angle * 3.14 / 180);
						float vc = (float)Math.sin((angle - 90)* 3.14 / 180);
						WorkGraphics.drawString(
							Message.substring(DispPoint, DispPoint  + 1), 
							AppletWidth / 2 + (int)(vs * AppletWidth / 2 * 0.8),
							AppletHeight / 2 + (int)(vc * AppletHeight / 2 * 0.2));
					}
					DispPoint++;					// 次の文字に設定
					if (DispPoint >= Message.length( ))	// 最後の文字になった場合
						break;
				}
				if (n == 1)							// 1回目は画像イメージ描画
					WorkGraphics.drawImage(BackImage,
										(AppletWidth - BackImageWidth) / 2,
										(AppletHeight - BackImageHeight) / 2, this);
			}
			repaint( );								// 再描画
			try {
				Thread.sleep(SleepTime);			// スレッドsleeptimeミリ秒スリープ
			} catch (InterruptedException evt) {	// 他のスレッドの割り込み例外処理
				showStatus(" " + evt);
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
