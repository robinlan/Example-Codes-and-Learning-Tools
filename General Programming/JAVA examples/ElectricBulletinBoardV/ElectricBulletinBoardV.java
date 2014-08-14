import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Font

public class ElectricBulletinBoardV extends Applet implements Runnable {
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int	AppletWidth, AppletHeight;					// アプレットの幅と高さ
	String Message[ ] = new String[10];				// メッセージ
	int	MessageNumber;								// メッセージの数
	int	MessageCount;								// メッセージ表示カウント
	int	MessageLength;								// メッセージの長さ
	int FontHeight;									// 文字の高さ
	int FontSize;									// 文字のサイズ
	int	Step;										// メッセージの移動間隔
	int	DispPoint;									// メッセージの垂直表示位置
	boolean PrepareFlag = false;					// 準備フラグ
	Thread thread = null;							// スレッド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		MessageNumber = Integer.parseInt(getParameter("number"));	// メッセージ数
		for (int i = 0; i < MessageNumber; i++)						// メッセージ取得
			Message[i] = getParameter("message" + i);
		Step = AppletWidth / 20;									// メッセージ移動間隔
		FontSize = AppletWidth;										// フォントサイズ
		Font font = new Font("ＭＳ ゴシック", Font.PLAIN, FontSize);// フォント設定
		WorkGraphics.setFont(font);
		FontMetrics fm = getFontMetrics(font);				// フォントメトリックス
		FontHeight = fm.getAscent( ) + fm.getDescent( );	// 文字の高さ
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (PrepareFlag == false)					// 準備ができていない場合
			return;
		WorkGraphics.setColor(Color.black);						// 背景色
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);	// 背景塗りつぶし
		WorkGraphics.setColor(new Color(0, 255, 0));			// メッセージの色
		// メッセージの文字数分ループ(1文字づつ描画)
		for (int n = 0; n < Message[MessageCount].length( ); n++) {
			WorkGraphics.drawString(Message[MessageCount].substring(n, n+1), 
									0, DispPoint + n * FontHeight);
		}
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		MessageCount = 0;							// メッセージ表示カウント
		MessageLength = (Message[MessageCount].length( ) - 1) * FontSize;// メッセージ長
		DispPoint = AppletHeight + FontHeight;		// 表示位置設定
		PrepareFlag = true;							// 準備フラグをON
        while (thread != null) {					// スレッドが存在している間
			try {
				thread.sleep(50);
			} catch(InterruptedException e) {
			}
			DispPoint -= Step;						// 描画位置を上にstep移動
			if (DispPoint < -MessageLength) {		// メッセージが場外に出た場合
				DispPoint = AppletHeight + FontHeight;	// 表示位置設定
				MessageCount++;						// 次のメッセージ
				if (MessageCount >= MessageNumber)	// メッセージ数を超えた場合
					MessageCount = 0;				// 最初のメッセージに
				MessageLength = (Message[MessageCount].length( ) - 1) * FontSize;
													// メッセージ長
			}
			repaint( );
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
