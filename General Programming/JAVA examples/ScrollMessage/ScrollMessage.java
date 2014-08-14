import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, MediaTracker

public class ScrollMessage extends Applet implements Runnable {
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	String BackImageFile = null;					// 背景画像ファイル名
	Image BackImage;								// 背景イメージ
	Image Parts[ ] = new Image[6];					// 枠イメージ
	int PartsSize;									// 枠サイズ
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	String Message[ ] = new String[20];				// メッセージ
	int MessageNumber;								// メッセージ数
	int Step = 1;									// メッセージのスクロール間隔
	int DispPoint;									// メッセージ垂直方向表示位置
	int Size;										// フォントサイズ
	int RowHeight;									// 行の高さ
	Color color;									// 文字の色
	MediaTracker mediatracker = new MediaTracker(this);		// メディアトラッカ
	Thread thread = null;							// スレッド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);		// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );				// 作業用グラフィックス取得

		// パラメータよりデータ入力
		String str = getParameter("color");						// colorパラメータ取得
		int p1 = str.indexOf(",");								// 最初の「，」の位置調査
		int red = Integer.parseInt(str.substring(0, p1));		// 最初の「，」までの値
		int p2 = str.indexOf(",", p1+1);						// 次の「，」の位置調査
		int green = Integer.parseInt(str.substring(p1+1, p2));	// 次の値
		int blue = Integer.parseInt(str.substring(p2+1));		// 最後の値
		color = new Color(red, green, blue);					// 文字の色設定

		MessageNumber = Integer.parseInt(getParameter("number"));	// メッセージ数
		for (int i = 0; i < MessageNumber; i++)			// メッセージ入力
			Message [i] = getParameter("message" + i);
		BackImageFile = getParameter("backimage");		// 背景画像
		if (BackImageFile != null) {
			BackImage = getImage(getCodeBase( ), BackImageFile);// 背景画像入力
			mediatracker.addImage(BackImage, 0);	// メディアトラッカにセット
		}
		for (int i = 0; i < 6; i++) {
			Parts[i] = getImage(getCodeBase( ), "image/part"+i+".jpg");	// 枠画像
			mediatracker.addImage(Parts[i], 0);		// メディアトラッカにセット
		}
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {
		}
		PartsSize = Parts[0].getWidth(this);		// 枠サイズ

		Size = Integer.parseInt(getParameter("size"));			// 文字列を整数化
		Font font = new Font("ＭＳ ゴシック", Font.PLAIN, Size);// フォント設定
		WorkGraphics.setFont(font);
		FontMetrics fm = getFontMetrics(font);					// フォントメトリックス
		RowHeight = (int)((fm.getAscent( ) + fm.getDescent( ))*1.5);// 行の高さ 文字1.5倍
		DispPoint = AppletHeight + RowHeight;		// メッセージ表示垂直位置設定
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (BackImageFile != null)					// 背景画像を指定していた場合
			WorkGraphics.drawImage(BackImage, 0, 0, this);	// 背景画像描画
		else {
			WorkGraphics.setColor(Color.white);		// 背景を白く塗る
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		}
		WorkGraphics.setColor(color);				// メッセージの描画色
		for (int i = 0; i < MessageNumber; i++) 	// メッセージを表示
			WorkGraphics.drawString(Message[i], PartsSize, DispPoint + i * RowHeight);
		if (DispPoint + (MessageNumber - 1) * RowHeight < 0)	// ラストの表示位置が場外
			DispPoint = AppletHeight + RowHeight;	// 表示位置をアプレットの下に設定
		else
			DispPoint -= Step;						// 表示位置をstep分上に上げる

		if (BackImageFile == null) {				// 背景画像がない場合，枠作成
			for (int i = 0; i < AppletWidth; i += PartsSize) {
				WorkGraphics.drawImage(Parts[4], i, 0, this);
				WorkGraphics.drawImage(Parts[4], i, AppletHeight - PartsSize, this);
			}
			for (int i = 0; i < AppletHeight; i += PartsSize) {
				WorkGraphics.drawImage(Parts[5], 0, i, this);
				WorkGraphics.drawImage(Parts[5], AppletWidth - PartsSize, i, this);
			}
			WorkGraphics.drawImage(Parts[0], 0, 0, this);
			WorkGraphics.drawImage(Parts[1], AppletWidth - PartsSize, 0, this);
			WorkGraphics.drawImage(Parts[2], 0, AppletHeight - PartsSize, this);
			WorkGraphics.drawImage(Parts[3],
 				AppletWidth - PartsSize, AppletHeight - PartsSize, this);
		}

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			repaint( );								// update( )を実行
			try {
				Thread.sleep(100);					// 指定ミリ秒スリープ
			} catch (InterruptedException e) {		// 割り込み等の例外処理
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
