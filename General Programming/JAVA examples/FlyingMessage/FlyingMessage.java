import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Font, FontMetrics

public class FlyingMessage extends Applet implements Runnable {
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int	AppletWidth, AppletHeight;					// アプレットのサイズ
	int MessageNumber;								// メッセージの数
	String Message[ ] = new String[10];				// メッセージMAX10
	String FontName;								// フォント名
	Thread thread = null;							// スレッド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの横幅
		AppletHeight = getSize( ).height;			// アプレットの縦幅

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		// パラメータからデータ入力
		MessageNumber = Integer.parseInt(getParameter("number"));	// メッセージ数
		for (int i = 0; i < MessageNumber; i++)				// メッセージ数ループ
			Message[i] = getParameter("message" + i);		// テキストのパラメータ取込み
		FontName = getParameter("font");					// フォント名
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
		int x = 0, y = 0;							// 表示位置
		double step = 0.0;							// 移動間隔
		int sw = 1;									// 移動方向  sw  -1:左移動 +1:右移動
		int count = 0;								// メッセージ表示カウント
		int fontsize = 1;							// フォントサイズ
		Font font;									// フォント
		FontMetrics fontmetrics = null;				// フォントメトリックス

        while (thread != null) {					// スレッドが存在している間
			WorkGraphics.setColor(Color.black);		// 描画色を黒色に設定
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
			if (fontsize < AppletHeight -10) {
				// フォントサイズがアプレット枠に表示できるサイズより小さい場合
				font = new Font(FontName, Font.PLAIN, fontsize);	// フォント設定
				fontmetrics = getFontMetrics(font);	// フォントメトリックス取得
				WorkGraphics.setFont(font);			// フォント設定
				// メッセージが中央に表示されるように表示位置設定
				x = (AppletWidth - fontmetrics.stringWidth(Message[count])) / 2;
				y = AppletHeight / 2 
					+ (fontmetrics.getAscent( ) - fontmetrics.getDescent( )) / 2;
				fontsize += 2;						// フォントサイズ+2
			}
			else {	// swが示す方向にメッセージを加速的に移動させる
				x += (int)(step * sw);				// sw  -1:左移動   +1:右移動
				step += 1.0;						// 移動スピード徐々にアップ
			}

			WorkGraphics.setColor(Color.white);		// 描画色を白色に設定
			WorkGraphics.drawString(Message[count], x, y);	// メッセージ表示

			repaint( );								// 再描画

			// メッセージが画面左外に出た場合か，またはメッセージが画面右外に出た場合
			if (x < - fontmetrics.stringWidth(Message[count]) || x > AppletWidth) {
				count++;							// 次のメッセージへ
				if (count >= MessageNumber)			// 最後のメッセージの次
					count = 0;						// 最初のメッセージ
				fontsize = 1;						// 最小フォントサイズに設定
				step = 0;							// 移動ステップ0クリア
				sw *= -1;							// 移動方向を交互に変更
			}

			try {
				Thread.sleep(100);					// 100ミリ秒スリープ
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
