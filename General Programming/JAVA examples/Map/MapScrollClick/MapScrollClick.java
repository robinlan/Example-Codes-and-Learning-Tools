import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, MediaTracker
import java.awt.event.*;							// MouseListener, MouseEvent

public class MapScrollClick extends Applet implements MouseListener, Runnable {
	Image Map;										// 地図イメージ
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	int MapWidth, MapHeight;						// 画像のサイズ
	int DispX, DispY;								// 表示位置
	int MoveX=0, MoveY=0;							// 移動ステップ
	Thread thread = null;							// スレッド
	boolean MoveFlag = false;						// 移動フラグ

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		MediaTracker mediatracker = new MediaTracker(this);	// 入力監視メディアトラッカ
		Map = getImage(getCodeBase( ), "image/map.gif");
		mediatracker.addImage(Map, 0);				// メディアトラッカにセット
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);						// 例外処理エラー表示
		}

		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ
		MapWidth = Map.getWidth(this);				// マップの幅
		MapHeight = Map.getHeight(this);			// マップの高さ

		DispX = AppletWidth / 2 - MapWidth / 2;		// マップの中心をアプレットの中心に
		DispY = AppletHeight / 2 - MapHeight / 2;

		addMouseListener(this);						// マウスリスナー追加
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ){
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		DispX += MoveX;								// 表示位置を移動
		DispY += MoveY;
		// 地図がアプレット内すべてに表示されない場合
		if (DispX > 0 || DispX < (AppletWidth - MapWidth) ||
			DispY > 0 || DispY < (AppletHeight - MapHeight)){
			DispX -= MoveX;							// 移動した分、戻す
			DispY -= MoveY;
			MoveX = MoveY = 0;						// 移動停止
			MoveFlag = false;
		}
		g.drawImage(Map, DispX, DispY, this);
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ){
		while (thread != null) {					// スレッドが存在している間
			try{
				thread.sleep(100);					// スレッドスリープ
			}catch(InterruptedException e){			// スレッドの割り込み例外処理
				showStatus(" " + e);				// 例外処理エラー表示
			}
			repaint( );								// 再描画
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ){
		thread = null;								// スレッド無効
	}
	// MouseListenerインターフェースを実装----------------------------------------------
	public void mousePressed(MouseEvent evt) {
		int mouseX = evt.getX( );					// マウスの位置
		int mouseY = evt.getY( );
		if (MoveFlag == false) {					// 静止している場合
			MoveFlag = true;						// 移動開始
			// 画面を9等分して，8方向に移動
			if (mouseX < AppletWidth / 3)			// 左側をクリックした場合
				MoveX = +1;							// 画像の移動を右側へ
			else if(mouseX > AppletWidth / 3 * 2)	// 右側をクリックした場合
				MoveX = -1;							// 画像の移動を左側へ
			else
				MoveX = 0;
			if (mouseY < AppletHeight / 3)			// 上側をクリックした場合
				MoveY = +1;							// 画像の移動を下側へ
			else if(mouseY > AppletHeight / 3 * 2)	// 下側をクリックした場合
				MoveY = -1;							// 画像の移動を上側へ
			else
				MoveY = 0;
		}
		else {										// 動いている場合
			MoveFlag = false;						// 停止
			MoveX = MoveY = 0;						// 移動量0
		}
		repaint( );									// 再描画
	}
	public void mouseClicked(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
}
