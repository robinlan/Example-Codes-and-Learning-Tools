import java.applet.*;							// Applet
import java.awt.*;								// Graphics, Image, MediaTracker, Scrollbar
import java.awt.event.*;						// AdjustmentListener, AdjustmentEvent

public class MapScrollbar extends Applet 
			implements AdjustmentListener {		// AdjustmentListenerインターフェース実装

	int	AppletWidth, AppletHeight;					// アプレットのサイズ
	Image Map;										// マップイメージ
	int MapWidth, MapHeight;						// マップのサイズ
	int DispX, DispY;								// マップを描画する位置
	int	HscrollbarMax, VscrollbarMax;				// 各スクロールバーの最大値
	Scrollbar Hscrollbar, Vscrollbar;				// 水平バー，垂直バー

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		MediaTracker mediatracker = new MediaTracker(this);		// メディアトラッカ生成
		Map = getImage(getCodeBase( ), getParameter("name"));	// 画像データ入力
		mediatracker.addImage(Map, 0);				// メディアトラッカにセット
		try {
			mediatracker.waitForID(0);				// 入力が完了するまで待つ
		} catch(InterruptedException e) {
		}

		MapWidth = Map.getWidth(this);				// マップサイズ
		MapHeight = Map.getWidth(this);
		AppletWidth = getSize( ).width;				// アプレットサイズ
		AppletHeight = getSize( ).height;
		HscrollbarMax = MapWidth - AppletWidth;		// スクロールmax
		VscrollbarMax = MapHeight - AppletHeight;

		setLayout(null);							// 自由配置
		// スクロールバー作成
		Hscrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, HscrollbarMax+1);
		Vscrollbar = new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, VscrollbarMax+1);
		Hscrollbar.setBounds(20, 0, AppletWidth - 20, 20);	// バーの再設定
		Vscrollbar.setBounds(0, 20, 20, AppletHeight - 20);
		add(Hscrollbar);							// バーをアプレットに付加
		add(Vscrollbar);
		Hscrollbar.addAdjustmentListener(this);		// バーにリスナーセット
		Vscrollbar.addAdjustmentListener(this);
		DispX = DispY = 0;							// 表示位置初期値設定
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(Map, DispX, DispY, this);
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// AdjustmentListenerインターフェースのメソッド定義 ---------------------------------
	public void adjustmentValueChanged(AdjustmentEvent evt) {	// バーの変化キャッチ
		Scrollbar scrollbar = (Scrollbar)evt.getSource( );
		if (scrollbar == Hscrollbar)				// 水平バーの場合
			DispX = -Hscrollbar.getValue( );		// 水平方向での地図の描画位置
		else if (scrollbar == Vscrollbar)			// 垂直バーの場合
			DispY = -Vscrollbar.getValue( );		// 垂直方向での地図の描画位置
		repaint( );
	}
}
