import java.applet.*;				// Applet
import java.awt.*;					// Graphics, Image, MediaTracker, Color
import java.awt.*;					// Graphics , addMouseListener, addMouseMotionListener
import java.awt.event.*;			// MouseListener, MouseMotionListener, MouseEvent

// マップズームキャンバスクラス ---------------------------------------------------------
public class MapZoomCanvas extends Applet {
	MapCanvas mapcanvas;
	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカ生成
		Image Map = getImage(getCodeBase( ), "image/map.gif");
		mediatracker.addImage(Map, 0);
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);					// 例外処理エラー表示
		}

		Image WorkImage = createImage(200, 200);	// 作業用イメージ作成
		mapcanvas = new MapCanvas(Map, WorkImage);	// 地図表示用のキャンバス生成
		mapcanvas.setSize(200, 200);				// キャンバスのサイズ設定

		setLayout(null);							// 自由レイアウト
		add(mapcanvas);								// キャンバスをアプレットに付加
		mapcanvas.setBounds(50, 50, 200, 200);		// キャンバスの位置サイズ設定
	}
}
// --------------------------------------------------------------------------------------
// マップキャンバスクラス
class MapCanvas extends Canvas implements MouseListener, MouseMotionListener {
	Image Map;										// 地図イメージ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int CanvasWidth, CanvasHeight;					// キャンバスサイズ
	int MapWidth, MapHeight;						// マップのサイズ	
	int MouseX, MouseY;								// マウスの位置
	int DispX, DispY;								// マップの描画位置
	double Per;										// 描画倍率

	// コンストラクタ -------------------------------------------------------------------
	public MapCanvas(Image map, Image workimage) {
		Map = map;
		WorkImage = workimage;

		CanvasWidth = workimage.getWidth(this);		// キャンバスイメージサイズ
		CanvasHeight = workimage.getHeight(this);
		MapWidth = Map.getWidth(this);				// マップの幅と高さ
		MapHeight = Map.getHeight(this);
		Per = (double)CanvasWidth / MapWidth;		// 初期倍率
		DispX = DispY = 0;							// マップ描画位置

		WorkGraphics = WorkImage.getGraphics( );	// 作業用グラフィック取得

		addMouseListener(this);						// マウスリスナー追加
		addMouseMotionListener(this);				// マウスモーションリスナ
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		WorkGraphics.setColor(Color.black);
		WorkGraphics.fillRect(0, 0, CanvasWidth, CanvasHeight);	// 背景描画

		WorkGraphics.drawImage(Map, DispX, DispY,
			(int)(Per * MapWidth), (int)(Per * MapHeight), this);

		WorkGraphics.drawLine(MouseX, 0, MouseX, CanvasHeight);	// 垂直ライン描画
		WorkGraphics.drawLine(0, MouseY, CanvasWidth, MouseY);	// 水平ライン描画

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージを描画
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// MouseListenerインターフェースを実装-----------------------------------------------
	public void mousePressed(MouseEvent evt) {
		MouseX = evt.getX( );						// 現在のマウスの位置
		MouseY = evt.getY( );
		evt.consume( );								// イベント消費
 		if ((evt.getModifiers( ) & InputEvent.BUTTON1_MASK) != 0) {			// 左ボタン
			// 拡大  ズームイン
			DispX = (int)(MouseX - (MouseX - DispX) * (Per + 0.1) / Per);
			DispY = (int)(MouseY - (MouseY - DispY) * (Per + 0.1) / Per);
			Per += 0.1;								// 倍率アップ
		}
 		else if ((evt.getModifiers( ) & InputEvent.BUTTON3_MASK) != 0) {	// 右ボタン
			// 縮小　ズームアウト
			DispX = (int)(MouseX - (MouseX - DispX) * (Per - 0.1) / Per);
			DispY = (int)(MouseY - (MouseY - DispY) * (Per - 0.1) / Per);
			Per -= 0.1;								// 倍率ダウン
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
