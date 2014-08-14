import java.applet.*;								// Applet, AudioClip
import java.awt.*;									// Graphics, Image, Color
import java.awt.event.*;							// MouseListener, MouseMotionListener, MouseEvent

public class MagicStar extends Applet
			implements MouseListener, MouseMotionListener, Runnable{
			// マウスリスナー，マウスモーションリスナー，ランナブル実装
    Thread thread = null;							// スレッド
	int AppletWidth, AppletHeight;					// アプレットの幅と高さ

	int N = 50;										// 星の数
	int Size[ ] = new int[N];						// 各星の大きさ
	int X[ ] = new int[N];							// 星の位置
	int Y[ ] = new int[N];
	float Hue[ ] = new float[N];					// 星の色相

	float HueStep = 0.01f;							// 星作成の色相変化デフォルト値
	int Radius = 30;								// 星作成のデフォルト最大半径
	int Range = 60;									// 星作成のデフォルト最大発生範囲

	int	MouseX, MouseY;								// マウスの位置

	Image WorkImage;								// 作業イメージ
	Graphics WorkGraphics;							// 作業グラフィックス

    String Key[ ] = {	"C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5"}; //各キーの名前
    AudioClip Sound[ ] = new AudioClip[8];			// 各キーに対するサウンド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// パラメータ入力
		Radius = Integer.parseInt(getParameter("radius"));		// 星作成の最大半径
		Range = Integer.parseInt(getParameter("range"));		// 星作成の最大発生範囲
		HueStep = new Float(getParameter("step")).floatValue( );// 色相変化値

		// 音データ入力
		for (int i = 0; i < 8; i++)								// 音データ入力
	        Sound[i] = getAudioClip(getCodeBase( ), "sound/"+Key[i]+".au");

		AppletWidth = getSize( ).width;							// アプレットのサイズ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);		// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );				// 作業用グラフィックス取得

		addMouseListener(this);									// マウスリスナー追加
		addMouseMotionListener(this);							// マウスモーションリスナー追加
	}
	// アプレット開始 -------------------------------------------------------------------
    public void start( ){
        thread = new Thread(this);					// スレッド生成
        thread.start( );							// スレッド開始
    }
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g){
		WorkGraphics.setColor(Color.black);			// 描画色を黒色に設定
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);	// クリア
		for (int i = 0; i < N; i++) {				// 星の数繰り返す
			if (Size[i] > 0) {						// 星の大きさがある場合
    			// 2つのラインで外側から中心に向かって描画
				for (int p = Size[i]; p > 0; p--) {	// 外側から中心に向かって星作成
					// 色設定（外側から中心へ明度を明るくする）
					WorkGraphics.setColor(
						Color.getHSBColor(Hue[i], 1f, (float)(Size[i] - p) / Size[i]));
					// ２つのラインで十字描画
					WorkGraphics.drawLine(X[i]  ,Y[i]-p, X[i]  , Y[i]+p);
					WorkGraphics.drawLine(X[i]-p,Y[i]  , X[i]+p, Y[i]  );
				}
				Size[i]--;							// 星のサイズを小さくする
			}
		}
		g.drawImage(WorkImage, 0, 0, this); 		// 作業イメージをアプレットに描画
    }
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ){
        while(thread != null){						// スレッドが存在している間
			MakeStar( );							// 星作成
            try{
               thread.sleep(50);					// スレッドスリープ
            }catch(InterruptedException e){			// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
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
	// スター作成 -----------------------------------------------------------------------
	void MakeStar(  ) {
		for (int i = 0; i < N; i++) {
			if (Size[i] <= 0) {
				Size[i] = (int)(Math.random( ) * Radius + 1);				// 大きさ
				X[i] = MouseX - Range + (int)(Math.random( ) * Range * 2);	// 発生x位置
				Y[i] = MouseY - Range + (int)(Math.random( ) * Range * 2);	// 発生y位置
				Hue[i] += HueStep;					// 色相変化
				if (Hue[i] > 1)
					Hue[i] = 0;
			}
		}
	}
	// MouseListenerインターフェースを実装 ----------------------------------------------
    public void mousePressed(MouseEvent evt) {
        int p = evt.getX( ) / (AppletWidth / 8);	// クリックした所が架空の鍵盤のｐ番目
		if (p >= 0 && p < 8)						// 1オクターブ内ならば
			Sound[p].play( );						// その音階を鳴らす
	}
    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }

	// MouseMotionListenerインターフェースを実装 ----------------------------------------
    public void mouseMoved(MouseEvent evt) {
		MouseX = evt.getX( );						// マウスの現在位置を保管
		MouseY = evt.getY( );
    }
    public void mouseDragged(MouseEvent evt) { }
}
