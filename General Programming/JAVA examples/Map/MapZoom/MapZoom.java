import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, MediaTracker, Color
import java.awt.event.*;							// MouseListener, MouseMotionListener etc

public class MapZoom extends Applet implements MouseListener, MouseMotionListener {

	Image Map;										// 地図画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ
	int MapWidth, MapHeight;						// 地図の幅と高さ
	int MouseX, MouseY;								// マウスの位置
	int DispX, DispY;								// 表示位置
	double Per;										// 表示倍率

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカ生成
		Map = getImage(getCodeBase( ), "image/map.gif");	// 地図画像入力
		mediatracker.addImage(Map, 0);				// メディアトラッカにセット
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);					// 例外処理エラー表示
		}

		AppletWidth = getSize( ).width;				// アプレットサイズ
		AppletHeight = getSize( ).height;
		MapWidth = Map.getWidth(this);				// マップサイズ
		MapHeight = Map.getHeight(this);
		Per = (double)AppletWidth / MapWidth;		// 初期倍率
		DispX = DispY = 0;							// 表示位置

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィック取得

		addMouseListener(this);						// マウスリスナー追加
		addMouseMotionListener(this);				// マウスモーションリスナ
		repaint( );
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		WorkGraphics.setColor(Color.black);
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);

		WorkGraphics.drawImage(Map, DispX, DispY,
			(int)(Per*MapWidth), (int)(Per*MapHeight), this);

		WorkGraphics.drawLine(MouseX, 0, MouseX, AppletHeight);	// 垂直ライン描画
		WorkGraphics.drawLine(0, MouseY, AppletWidth, MouseY);	// 水平ライン描画

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージを描画
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// MouseListenerインターフェースを実装-----------------------------------------------
	public void mousePressed(MouseEvent evt) {
		MouseX = evt.getX( );						// マウスの位置
		MouseY = evt.getY( );

       evt.consume( );								// イベント消費

 		if ((evt.getModifiers( ) & InputEvent.BUTTON1_MASK) != 0) {			// 左ボタン
			// 拡大　ズームイン
			DispX = (int)(MouseX - (MouseX - DispX) * (Per + 0.1) / Per);
			DispY = (int)(MouseY - (MouseY - DispY) * (Per + 0.1) / Per);
			Per += 0.1;
		}
 		else if ((evt.getModifiers( ) & InputEvent.BUTTON3_MASK) != 0) {	// 右ボタン
			// 縮小　ズームアウト
			DispX = (int)(MouseX - (MouseX - DispX) * (Per - 0.1) / Per);
			DispY = (int)(MouseY - (MouseY - DispY) * (Per - 0.1) / Per);
			Per -= 0.1;
		} 
		repaint( );
	}
	public void mouseClicked(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
	// MouseMotionListenerインターフェースを実装 ----------------------------------------
	public void mouseMoved(MouseEvent evt) {
		MouseX = evt.getX( );						// 現在のマウスの位置
		MouseY = evt.getY( );
		repaint( );
	}
	public void mouseDragged(MouseEvent evt) { }
}
