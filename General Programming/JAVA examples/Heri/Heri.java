import java.applet.*;
import java.awt.*;			// Graphic, Image, Color, Font, Canvas, Label, 
							// BorderLayout, Toolkit, MediaTracker, GraphicsConfiguration
import java.awt.event.*;	// MouseListener, MouseEvent
import javax.media.j3d.*;	//Transform3D, TransformGroup, Canvas3D, BranchGroup, View, GraphicsContext3D
import javax.vecmath.Vector3d;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Heri extends Applet implements Runnable, MouseListener {

	String DataFilename[ ] = { "heri.lws",			// Lightwaveで作成したヘリコプター
							   "map.lws"};			// Lightwaveで作成した地形
	Thread thread = null;							// スレッド
	int Score = 0;									// スコア
	Image MeterPanel, Direction;					// メータのパネル画像，方向イメージ

	Canvas3D GameCanvas = null;						// ゲームキャンバス
	Label InformationLabel;							// 情報表示ラベル

	MeterCanvas metercanvas;						// メータキャンバス

	static int FrameWidth = 800, FrameHeight = 600;	// フレームの幅と高さ

	int Kanseihokan = 20;							// 慣性補間回数
	int KanseihokanCount = 0;						// 慣性補間回数カウント
	int preparation  = 0;							// 準備フラグ
	boolean GameStartFlag = false;					// ゲームスタートフラグ

	double LocalXRotation = 0;						// ローカルの回転角度
	double LocalYRotation = 0;
	double GlobalXRotation = 0;						// グローバルの回転角度
	double GlobalYRotation = 0;

	double ViewX = 0, ViewY = 50, ViewZ = 0;		// ビューポイント初期設定

	double StarX[ ] = { -5000,  5000,  5000,  -5000,    0};	// map.lws内のstar.lwoの位置
	double StarY[ ] = {   100,   100,   100,    100,  100};
	double StarZ[ ] = {  5000,  5000, -5000,  -5000,    0};

	boolean HitFlag[ ] = { false, false, false, false, false };	// starヒットフラグ

    GraphicsContext3D graphicsContext3D = null;		// GraphicsContext3Dオブジェクト

	// 視点設定用------------------------------------------------------------------------
	TransformGroup ViewTransform = null;
	Transform3D ViewTrans = new Transform3D( );
	Transform3D ViewTrans_pos = new Transform3D( );
	Transform3D ViewTrans_rotx = new Transform3D( );
	Transform3D ViewTrans_roty = new Transform3D( );

	// メイン処理 -------------------------------------------------------------------------
    public static void main(String args[ ]) {
		new MainFrame(new Heri( ), FrameWidth, FrameHeight);
    }

	// コンストラクタ -------------------------------------------------------------------
    public Heri( ) {
 		MediaTracker  mt = new MediaTracker(this);	// メディアトラッカ生成
		MeterPanel = Toolkit.getDefaultToolkit( ).getImage("MeterPanel.jpg");
		Direction = Toolkit.getDefaultToolkit( ).getImage("Direction.jpg");
		mt.addImage(MeterPanel, 0);					// 画像をメディアトラッカにセット
		mt.addImage(Direction, 0);					// 画像をメディアトラッカにセット
		try {
			mt.waitForID(0);						// 画像入力の完了を待つ
		} catch(InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);					// 例外エラー表示
		}

		// Lightwave3Dのローダ構築，３Dデータ入力
		Loader lw3dLoader1 = new Lw3dLoader(Loader.LOAD_ALL);
		Scene loaderScene_heri = null;
		try {
		    loaderScene_heri = lw3dLoader1.load(DataFilename[0]);
		}
		catch (Exception e) {
		    System.err.println("Exception loading " + DataFilename[0] + " : " + e);
		    System.exit(1);
		}

		// Lightwave3Dのローダ構築，３Dデータ入力
		Loader lw3dLoader2 = new Lw3dLoader(Loader.LOAD_ALL);
		Scene loaderScene_map = null;
		try {
		    loaderScene_map = lw3dLoader2.load(DataFilename[1]);
		}
		catch (Exception e) {
		    System.err.println("Exception loading " + DataFilename[1] + " : " + e);
		    System.exit(1);
		}

		// Construct the applet canvas
		setLayout(new BorderLayout( ));
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration( );
		GameCanvas = new Canvas3D(config);
		add("Center", GameCanvas);

		// Create a basic universe setup and the root of our scene
		SimpleUniverse universe = new SimpleUniverse(GameCanvas);
		BranchGroup sceneRoot = new BranchGroup( );

		// Change the back clip distance; the default is small for
		// some lw3d worlds
		View theView = universe.getViewer( ).getView( );			// 視点
		theView.setBackClipDistance(50000f);

		// 視点設定----------------------------------------------------------------------
		ViewTrans.rotY(Math.PI / 180.0);
		ViewTrans_pos.setTranslation(new Vector3d(ViewX, ViewY, ViewZ));
		ViewTrans.mul(ViewTrans_pos);
		ViewTransform = universe.getViewingPlatform( ).getViewPlatformTransform( );
		ViewTransform.setTransform(ViewTrans);

		// ------------------------------------------------------------------------------
		TransformGroup sceneTransform = new TransformGroup( );
		TransformGroup sceneTransform_heri = new TransformGroup( );
		TransformGroup sceneTransform_map = new TransformGroup( );

		// ヘリ回転実験 -----------------------------------------------------------------
		Transform3D transform3D_heri = new Transform3D( );
		transform3D_heri.rotY(Math.PI / 180);
		Transform3D transform3D_heri_pos = new Transform3D( );
		transform3D_heri_pos.setTranslation(new Vector3d(0.0, 50.0, -50.0));
		transform3D_heri.mul(transform3D_heri_pos);
		sceneTransform_heri.setTransform(transform3D_heri);
		sceneTransform_heri.addChild(loaderScene_heri.getSceneGroup( ));

		// Map --------------------------------------------------------------------------
		sceneTransform_map.addChild(loaderScene_map.getSceneGroup( ));

		// ------------------------------------------------------------------------------
		sceneRoot.addChild(sceneTransform);
		sceneRoot.addChild(sceneTransform_heri);
		sceneRoot.addChild(sceneTransform_map);
	
		universe.addBranchGraph(sceneRoot);

		GameCanvas.addMouseListener(this);			// マウスリスナー追加

		thread = new Thread(this);
		thread.start( );

		setLayout(new BorderLayout( ));				// Frameのレイアウト設定

		InformationLabel = new Label("          ");		// ラベルオブジェクト生成
		InformationLabel.setBackground(Color.yellow);	// ラベルの背景色設定

		// メーターキャンバス作成
		metercanvas = new MeterCanvas(MeterPanel, Direction);	// キャンバス生成
		metercanvas.setSize(800, 100);				// キャンバスサイズ再設定
		metercanvas.canvas_make(1);					// キャンバスの生成完了

		// フレームに配置
		add ("North", InformationLabel);			// ラベルをアプレットに付加
		add("Center", GameCanvas);
		add("South", metercanvas);					// キャンバスをアプレットに付加

		preparation = 1;							// 準備OK
	}
	// レンダー -------------------------------------------------------------------------
	public void render( ) {
		if (preparation == 0)						// 準備がまだの場合
			return;

		if (GameStartFlag == false) {				// ゲームが始まっていない場合
			metercanvas.meter_disp(ViewX, ViewY, ViewZ, 0, 0, 0); 	// パネル表示
			return;
		}

		// ヴューポイント計算
		double move = 5;							// 単位移動距離
		double yw = move * Math.sin(GlobalXRotation);
		double xw = (-1) * Math.sin(GlobalYRotation) * Math.sqrt(move * move - yw * yw);
		double zw = Math.cos(GlobalYRotation) * Math.sqrt(move * move - yw * yw);
		ViewX += xw;
		ViewY += yw;
		ViewZ -= zw;

		// グローバルの回転方向　ラジアンを角度に変換
		int x_rot = (int)(GlobalXRotation * 180 / 3.14);
		int y_rot = (int)(GlobalYRotation * 180 / 3.14);

		String str = "[" + x_rot + ", " + y_rot + "]";
		InformationLabel.setText(str);

		// メータキャンバスに値を送り，パネル表示
		metercanvas.meter_disp(ViewX, ViewY, ViewZ, x_rot, y_rot, Score); 

		if (GameStartFlag == false)					// ゲームが始まっていない場合
			return;

		// 位置を設定
		ViewTrans.setTranslation(new Vector3d(ViewX, ViewY, ViewZ));

		// グローバル座標での進行方向
		GlobalXRotation += LocalXRotation / Kanseihokan;
		GlobalYRotation += LocalYRotation / Kanseihokan;

		// ワールドビューに補間した角度を設定
//		ViewTrans_rotx.rotX(LocalXRotation / Kanseihokan);		// X軸に対しての回転
		ViewTrans_roty.rotY(LocalYRotation / Kanseihokan);		// Y軸に対しての回転

//		ViewTrans.mul(ViewTrans_rotx);				// X軸の回転設定
		ViewTrans.mul(ViewTrans_roty);				// Y軸の回転設定

		ViewTransform.setTransform(ViewTrans);

		// 慣性補間処理
		KanseihokanCount--;

		if (KanseihokanCount == 0) {				// まっすぐに進む
			LocalXRotation = LocalYRotation = 0;	// X軸およびY軸の回転なし
			KanseihokanCount = Kanseihokan;			// 繰り返す
		}
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {
			render( );
			checkCollision(new Vector3d(ViewX, ViewY, ViewZ));
 			try {
				thread.sleep(50);					// スレッドをnミリ秒スリープ
        	} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
 			}
		}
	}
	// 当り判定 -------------------------------------------------------------------------
	void checkCollision(Vector3d pst) {
		double width = 10;
		for (int i = 0; i < 5; i++) {
			if ( Math.abs(pst.x - StarX[i]) < width
				&& Math.abs(pst.y - StarY[i]) < width
				&& Math.abs(pst.z - StarZ[i]) < width ) {
				HitFlag[i] = true;
			}
			else {
				if (HitFlag[i] == true) {
					Score++;
					Toolkit.getDefaultToolkit( ).beep( );	// ビープ音
				}
				HitFlag[i] = false;
			}
		}
	}
	// MouseListenerインターフェースを実装 ----------------------------------------------
    public void mousePressed(MouseEvent evt) {
		// クリックした位置がframeの中心からの距離
		int x = evt.getX( ) - FrameWidth / 2;
		int y = evt.getY( ) - FrameHeight / 2;

		// 長さ1000（frameの横幅の半分）のベクトルの先に目標点があるとする
		// 前方に進んでいることを考慮すると
		// その時点でのＸ軸及びＹ軸のローカルな回転は90度から－90度である
		// 画面上のローカル座標での進行方向
		LocalXRotation = Math.asin((double)y / 1000.0) * (-1);
		LocalYRotation = Math.asin((double)x / Math.sqrt(1000.0 * 1000.0 - y * y)) * (-1);

		GameStartFlag = true;
		KanseihokanCount = Kanseihokan;
	}
    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
}

// --------------------------------------------------------------------------------------
// MeterCanvas クラス
class MeterCanvas extends Canvas{
	int X, Y, Z;
	int RX, RY;
	int SCORE;
	int MAKE_FLAG = 0;
	Image MeterPanel, Direction;
	Image WorkImage = null, WorkDirection;			// 作業用イメージ
	Graphics wg = null, wd = null;					// 作業用グラフィックス
	// コンストラクタ -------------------------------------------------------------------
	public MeterCanvas (Image meterpanel, Image direction) {
		setBackground(Color.white);					// キャンバスの背景色
		MeterPanel = meterpanel;					// 操作パネルのイメージ
		Direction = direction;						// 方向パネルのイメージ
		X = Y = Z = 0;								// X・Y・Z軸座標値
		RX = RY = 0;								// X軸・Y軸に対する回転
		SCORE = 0;									// スコア
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (MAKE_FLAG == 1) {
			// 呼び出した方でsetsizeをした後で，createImageを行う 
			WorkImage = createImage(800, 100);
			wg = WorkImage.getGraphics( );	
			WorkDirection = createImage(100, 40);
			wd = WorkDirection.getGraphics( );
			MAKE_FLAG = 2;							// 完了	
			return;
		}

		// メーターパネル
		wg.drawImage(MeterPanel, 0, 0, this);

		// 方向メーター
		int w = RY % 360;
		if (w > 0)
			w = -(360 - w);
		wd.drawImage(Direction, w, 0, this);
		wg.drawImage(WorkDirection, 30, 40, 100, 40, this);
		wg.setColor(new Color(255,  0,  0));
		wg.drawLine(80, 35, 80, 85);				// 方向針

		// 高度メータ 170,50
 		wg.setFont(new  Font("Courier", Font.BOLD, 20));
		wg.setColor(new Color(255,  255,  255));
		wg.drawString(Y + " m", 170, 70);

		// ピッチ角度 290, 50
		wg.drawString(RX + "°", 290, 70);

		// 現在位置 410 , 530
		wg.drawString("" + X, 410, 70);
		wg.drawString("" + Z, 530, 70);

		// スコア 700, 50
		wg.setColor(new Color(255,  0,  0));
		wg.drawString("" + SCORE, 700, 70);

		// 作業イメージをキャンバスに描画
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	void canvas_make(int make_flag) {
		MAKE_FLAG = make_flag;
	}
	// メータ描画 -----------------------------------------------------------------------
	void meter_disp(double x, double y, double z, int rx, int ry, int Score) {
		X = (int)x; Y = (int)y; Z = (int)z; RX = rx; RY = ry; SCORE = Score;
		repaint( );									// メーター描画
	}
}
