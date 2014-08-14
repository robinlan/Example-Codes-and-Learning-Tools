import java.applet.*;										// Applet
import java.awt.*;											// Graphics, Image, Color
import java.util.*;											// Calendar, TimeZone
import java.lang.Math;										// sin, cos

public class AnalogClock extends Applet 
						 implements Runnable {
	Thread thread = null;									// スレッド宣言
	Image WorkImage;										// 作業用イメージ
	Graphics WorkGraphics;									// 作業用グラフィックス
	int CenterX, CenterY;									// 時計の中心
	int Radius;												// 時計の半径
	int AppletWidth, AppletHeight;							// アプレットの幅と高さ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅と高さ
		AppletHeight = getSize( ).height;

		WorkImage = createImage(AppletWidth, AppletHeight); // 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		// 時計の位置と半径
		CenterX = AppletWidth / 2;
		CenterY = AppletHeight / 2;
		if (AppletWidth < AppletHeight)						// 縦横，短い方を時計の半径
			Radius = (int)(AppletWidth / 2 * 0.8);			// 時計の半径
		else
			Radius = (int)(AppletHeight / 2 * 0.8);
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);							// スレッド生成
		thread.start( );									// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);					// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
        while (thread != null) {							// スレッドが存在している間
			DispTime( );									// 時間描画
			repaint( );  									// 再描画
			try{
				thread.sleep(100);							// スリープ
			} catch(InterruptedException e) {				// 他のスレッドの割り込み例外処理
				showStatus(" "+e);							// ブラウザのステータスバーに表示
			}
		}
	}
	// 描画更新処理再定義 --------------------------------------------------------------
	public void update(Graphics g) {						// デフォルトのupdateを再定義
		paint(g);											// 背景色画面クリア削除，paintのみ
	}
	// 時間描画 -------------------------------------------------------------------------
	void DispTime( ) {
		// 作業グラフィックスに描画
		WorkGraphics.setColor(Color.white);
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);

		// 角度を6度づつ（60分で360度，1分は6度)
 		for (int kakudo = 0; kakudo < 360; kakudo += 6) {
			// 目盛りを線で表現
			double RD = kakudo * Math.PI / 180;				// 角度をラジアンに変換
			int x1 = CenterX + (int)(Math.sin(RD) * Radius);// 目盛りの外側の点の位置
			int y1 = CenterY - (int)(Math.cos(RD) * Radius);
			int radius2;
			if (kakudo % 30 == 0)							// 5分刻み
	 			radius2 = Radius - 8;						// 長さ8の目盛り
			else
	 			radius2 = Radius;							// 長さ0の目盛り（つまり点)

			int x2 = CenterX + (int)(Math.sin(RD) * radius2);	// 目盛りの内側の点の位置
			int y2 = CenterY - (int)(Math.cos(RD) * radius2);
			WorkGraphics.setColor(Color.black);
	 		WorkGraphics.drawLine(x1, y1, x2, y2);			// 目盛りラインを引く
		}

		// 現在の時刻
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		int hour = date.get(Calendar.HOUR);					// 現在時刻の時間取得
		int minute = date.get(Calendar.MINUTE);				// 現在時刻の分取得
		int second = date.get(Calendar.SECOND);				// 現在時刻の秒取得

		// 時間の針描画
		MakeHand(CenterX, CenterY,
				 hour * 30 + minute / 60.0 * 30, Radius * 0.8, 1.2, Color.black);
		// 分の針描画
		MakeHand(CenterX, CenterY, minute * 6.0, Radius * 0.9, 0.7, Color.gray);

		// 秒の針描画
		double RD = second * 6 * Math.PI / 180;					// 秒の角度をラジアンに変換
		int sx = CenterX + (int)(Math.sin(RD) * Radius * 0.9);	// 秒の針の先端の位置
		int sy = CenterY - (int)(Math.cos(RD) * Radius * 0.9);
		WorkGraphics.setColor(Color.red);
		WorkGraphics.drawLine(CenterX, CenterY, sx, sy);
	}
	//  太い針を描画するメソッド --------------------------------------------------------
	void MakeHand(int cx, int cy,
				  double kakudo, double nagasa, double hutosa, Color color) {
		int px[ ] = new int[4];
		int py[ ] = new int[4];
		double RD = kakudo * Math.PI / 180;
		px[0] = cx + (int)(Math.sin(RD) * nagasa);
		py[0] = cy - (int)(Math.cos(RD) * nagasa);
		for (int i = 1; i < 4; i++) {
			RD = (kakudo + i * 90) * Math.PI / 180;
			px[i] = cx + (int)(Math.sin(RD) * nagasa * hutosa / 10);
			py[i] = cy - (int)(Math.cos(RD) * nagasa * hutosa / 10);
		}
		WorkGraphics.setColor(color);
		WorkGraphics.fillPolygon(px, py, 4);
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( )	{
		thread = null;										// スレッド無効
	}
}
