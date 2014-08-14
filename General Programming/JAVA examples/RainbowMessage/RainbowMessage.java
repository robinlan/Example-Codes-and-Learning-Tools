import java.applet.*;								// Applet
import java.awt.*;									// Graphic, Image, Color, Font, FontMetrics

public class RainbowMessage extends Applet implements Runnable {
	Image WorkImage, RainbowImage;					// 作業用と虹イメージ
	Graphics WorkGraphics, RainbowGraphics;			// 作業用と虹グラフィックス
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	String Message;									// メッセージ
	int MessageDispX, MessageDispY;					// メッセージ表示位置
	int RainbowDispX;								// 虹表示位置
	Thread thread = null;							// スレッド

	// 初期化処理 -----------------------------------------------------------------------
    public void init( ) {
		AppletWidth = getSize( ).width;							// アプレットの幅
		AppletHeight = getSize( ).height;						// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);		// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );				// 作業用グラフィック取得

		RainbowImage = createImage(AppletWidth, AppletHeight);	// 虹イメージ作成
		RainbowGraphics = RainbowImage.getGraphics( );			// 虹グラフィックス取得

		// パラメータより各種データ設定
		Message = getParameter("message");					// 表示メッセージ
		int size = Integer.parseInt(getParameter("size"));	// 文字サイズ
		String fontname = getParameter("fontname");			// フォント名
		Font font = new Font(fontname, Font.PLAIN, size);	// フォント
		FontMetrics fontmetrics = getFontMetrics(font);		// フォントメトリックス取得
		WorkGraphics.setFont(font);							// フォント設定

		// 表示位置
		MessageDispX = (AppletWidth - fontmetrics.stringWidth(Message)) / 2;
		MessageDispY =
		 (AppletHeight + fontmetrics.getAscent( ) - fontmetrics.getDescent( ))/2;
		MakeRainbowImage( );						// 虹作成
		RainbowDispX = 0;							// 虹表示位置
	}
	// 虹の帯作成 -----------------------------------------------------------------------
    protected void MakeRainbowImage( ) {
		float hue = 0;								// 色相
		for(int i = 0; i < AppletWidth; i++) {		// 分割
			hue = (float)i / AppletWidth;			// 色相をAppletWidth分のi
			RainbowGraphics.setColor(Color.getHSBColor(hue, 1f, 1f));	// 色設定
			RainbowGraphics.fillRect(i, 0, 1, AppletHeight);
		}
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g) {
		RainbowDispX -= 1;							// 虹の表示位置を左に1移動
		if (RainbowDispX < -AppletWidth)
			RainbowDispX = 0;
		// 虹のイメージを描画
		WorkGraphics.setPaintMode( );				// 上書きモード設定
		WorkGraphics.drawImage(RainbowImage, RainbowDispX, 0, null);
		WorkGraphics.drawImage(RainbowImage, RainbowDispX + AppletWidth, 0, null);

		WorkGraphics.setColor(Color.black);								// 黒色に設定
		WorkGraphics.drawString(Message, MessageDispX, MessageDispY);	// メッセージ描画

		// 虹のイメージを描画
		WorkGraphics.setXORMode(Color.black);		// 交替モード設定(黒色)
		WorkGraphics.drawImage(RainbowImage, RainbowDispX, 0, null);
		WorkGraphics.drawImage(RainbowImage, RainbowDispX + AppletWidth, 0, null);

		g.drawImage(WorkImage, 0, 0, null);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ) {
        while (thread != null) {					// スレッドが存在している間
			repaint( );
			try {
				thread.sleep(100);					// スレッドスリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}
		}
	}
	// 描画更新処理 ---------------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッド無効
	}
}
