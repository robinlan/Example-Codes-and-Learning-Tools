import java.applet.*;						// Applet
import java.awt.*;							// Graphics, Image, Color, Font, FontMetrics

public class WaveMessage extends Applet implements Runnable {
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int	AppletWidth, AppletHeight;					// アプレットのサイズ
	String Message;									// メッセージ
	Thread thread;									// スレッド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		Message = getParameter("message");			// パラメータよりメッセージ取得

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得
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
		int angle = 0;								// 最初の文字の初期角度
		int step = 30;								// 各文字の表示角度間隔
		Font font = new Font("TimesRoman", Font.PLAIN, 40);	// フォント
		FontMetrics fontmetrics = getFontMetrics(font);		// フォントメトリックス
		int haba =									// 上下幅
		 (AppletHeight- (fontmetrics.getAscent( )+ fontmetrics.getDescent( ))) / 2;
		int base = haba + fontmetrics.getAscent( );	// 表示ベースラインのポイント	
		WorkGraphics.setFont(font);					// フォント設定
        while (thread != null) {					// スレッドが存在している間
			WorkGraphics.setColor(Color.white);		// 背景を白色で塗りつぶし
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
			WorkGraphics.setColor(Color.black);		// 描画色を黒色
			angle = (angle + step) % 360;			// 最初の文字の表示初期角度
			for (int i = 0; i < Message.length( ); i++) {	// 文字数分ループ
				int x = AppletWidth / Message.length( ) * i;
				int y = (int)(base + haba * Math.sin((angle + step * i) * 3.14 / 180));
				// i番目の文字表示
				WorkGraphics.drawString(Message.substring(i, i + 1), x, y);
			}
			repaint( );								// 再描画
			try {
				thread.sleep(200);					// 200ミリ秒スリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);					// エラーをステータスバーに表示
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
}
