import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Color, Font, FontMetrics

public class TapeMessage extends Applet implements Runnable {
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int	AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int	DispPoint;									// 文字表示位置
	int	StringHeight;								// 文字の高さ
	String Message;									// メッセージ
	Thread thread = null;							// スレッド
	Font font;										// フォント
	FontMetrics fm;									// フォントメトリックス

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅と高さ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得
		Message = getParameter("message");					// MESSAGEパラメータ取得
		font = new Font("TimesRoman", Font.PLAIN, 20);		// フォント作成
		WorkGraphics.setFont(font);							// 描画フォントセット
		fm = getFontMetrics(font);							// フォントメトリックス取得
		StringHeight = fm.getAscent( ) + fm.getDescent( );	// 文字の高さ
		setBackground(Color.white);							// 背景を白色
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		WorkGraphics.setColor(Color.white);			// アプレットの色設定
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);		// アプレット消去
		WorkGraphics.setColor(Color.gray);							// テープの影の色設定
		int StringLength = fm.stringWidth((Message.substring(0, DispPoint)));
		WorkGraphics.fillRect(3, 3, StringLength, StringHeight);	// テープの影描画
		WorkGraphics.setColor(Color.yellow);						// テープの色設定
		WorkGraphics.fillRect(0, 0,	StringLength, StringHeight); 	// テープ描画
		WorkGraphics.setColor(Color.black);							// 文字の色設定
		WorkGraphics.drawString(Message.substring(0, DispPoint), 0, StringHeight);
													// MessageをDispPoint文字分表示
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行処理 -----------------------------------------------------------------
	public void run( ) {
		DispPoint = 0;								// 文字表示位置クリア
		while(thread != null) {		 				// スレッドが存在している間
			repaint( );								// 再描画
			if (DispPoint == Message.length( )) {	// メッセージの長さになった場合
				try {
					thread.sleep(3000);				// メッセージをすべて表示後，3秒停止
				} catch (InterruptedException e) {	// 他のスレッドの割り込み例外処理
					showStatus(" "+e);
				}
				DispPoint = 0;						// 文字表示位置クリア
			}
			else
				DispPoint++;						// 次の文字を表示できるようにする
			try {
				thread.sleep(300);					// 1文字表示ごとに300ミリ秒停止
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
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
