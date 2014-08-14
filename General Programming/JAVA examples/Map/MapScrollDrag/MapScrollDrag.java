import java.applet.*;					// Applet
import java.awt.*;						// Graphics, Image, Color, MediaTracker
import java.awt.event.*;				// MouseListener, MouseEvent, MouseMotionListener

public class MapScrollDrag extends Applet
							implements MouseListener, MouseMotionListener {

	int	AppletWidth, AppletHeight;					// アプレットのサイズ
	Image WorkImage;								// 作業イメージ
	Graphics WorkGraphics;							// 作業グラフィックス
	Image Map;										// マップ
	int	MapWidth, MapHeight;						// マップのサイズ
	int DispX, DispY;								// 描画位置
	int	DistanceX, DistanceY;						// マウスと画像との距離
	boolean DragFlag;								// ドラッグフラグ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカ生成
		Map = getImage(getCodeBase( ), "image/map.gif");	// 画像イメージ取り込み
		mediatracker.addImage(Map, 0);				// メディアトラッカにセット
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch (InterruptedException e) {			// waitForIDに対する例外処理
		}

		AppletWidth = getSize( ).width;				// アプレットサイズ
		AppletHeight = getSize( ).height;
		MapWidth = Map.getWidth(this);				// マップの幅と高さ
		MapHeight = Map.getHeight(this);
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		DispX = AppletWidth / 2 - MapWidth / 2;		// 初期表示位置（中央表示）
		DispY = AppletHeight / 2 - MapHeight / 2;
		DragFlag = false;							// ドラッグフラグオフ
		repaint( );
		addMouseListener(this);						// マウスリスナー追加
		addMouseMotionListener(this);				// マウスリスナー追加
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		WorkGraphics.setColor(Color.black);
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		WorkGraphics.drawImage(Map, DispX, DispY, this);

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// MouseListenerインターフェースを実装----------------------------------------------
	public void mousePressed(MouseEvent evt) {
		int mouseX = evt.getX( );					// マウスの位置
		int mouseY = evt.getY( );
		// クリックした位置がマップの範囲内の場合
		if (mouseX >= DispX && mouseX < DispX + MapWidth
		 && mouseY >= DispY && mouseY < DispY + MapHeight) {
			DragFlag = true;						// ドラッグ開始
			DistanceX = mouseX - DispX;				// 画像の表示位置とマウスとの距離
			DistanceY = mouseY - DispY;
		}
	}
	public void mouseReleased(MouseEvent evt) {
		if (DragFlag == true) {						// ドラッグの場合のみ
			DragFlag = false;						// ドラッグ終了
			int mouseX = evt.getX( );				// マウスの位置
			int mouseY = evt.getY( );
			// 画像描画位置=現マウスの位置 - 相対距離
			DispX = mouseX - DistanceX;
			DispY = mouseY - DistanceY;
		}
	}
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
	// MouseMotionListenerインターフェースを実装 ----------------------------------------
	public void mouseDragged(MouseEvent evt) {
		if (DragFlag == true) {						// ドラッグ中の場合
			int mouseX = evt.getX( );				// マウスの位置
			int mouseY = evt.getY( );
			DispX = mouseX - DistanceX;				// 画像の描画位置
			DispY = mouseY - DistanceY;
			repaint( );								// 再描画
		}
	}
	public void mouseMoved(MouseEvent evt) { }
}
