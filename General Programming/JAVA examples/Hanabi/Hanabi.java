import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color

public class Hanabi extends Applet implements Runnable{
    Thread thread = null;							// スレッド
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int sleeptime = 30;								// スリープタイム（実行スピード)

	int AngleRange = 50;							// 打ち上げ角度（垂直方向に対して）
	int SpeedMax = 90;								// 打ち上げスピード
	int ExplosionSpeed = 50;						// 爆発スピード
	int XP, YP;										// 発射の位置
	int HMAX = 30;									// 花火の数
	int TMAX = 200;									// 花火の中の玉数
	int LEN = 4;									// 火の長さ
	int SIZE = 1;									// 玉のサイズ
	int COUNT = 30;									// 花火の爆発時間カウント
	int X[ ][ ] = new int[HMAX][TMAX];				// 花火の玉の位置
	int Y[ ][ ] = new int[HMAX][TMAX];
	double Angle[ ][ ] = new double[HMAX][TMAX];	// 発射角度（ラジアン) 
	double Speed[ ][ ] = new double[HMAX][TMAX];	// 発射スピード
	double V0x[ ][ ] = new double[HMAX][TMAX];		// 水平方向の初速度	
	double V0y[ ][ ] = new double[HMAX][TMAX];		// 垂直方向の初速度
	int Process[ ] = new int[HMAX];					// 花火のプロセス（準備，飛行，爆発）
	int Time[ ] = new int[HMAX];					// 時間カウント
	float Hue[ ] = new float[HMAX];					// 花火の色
	double Rad = Math.PI / 180;						// ラジアン

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;								// アプレットのサイズ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);			// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );					// 作業用グラフィックス取得
		WorkGraphics.setColor(Color.black);							// 描画色を黒色に設定
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);		// 画面クリア

		// 各種パラメータ入力
		AngleRange = Integer.parseInt(getParameter("anglerange"));	// 垂直発射角度幅
		SpeedMax = Integer.parseInt(getParameter("speedmax"));		// 発射スピード
		HMAX = Integer.parseInt(getParameter("max"));				// 発射数量
		if (HMAX > 30)
			HMAX = 30;

		// 花火プロセスを0クリア（発射していない段階)
		for (int i = 0; i < HMAX; i++)
			Process[i] = 0;							// 0:準備，1:発射，2:爆発，3～30消滅
		// 発射位置
		XP = AppletWidth / 2;
		YP = AppletHeight;
	}
	// アプレット開始 -------------------------------------------------------------------
    public void start( ){
        thread = new Thread(this);					// スレッド生成
        thread.start( );							// スレッド開始
    }
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g){
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
    }
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ){
        while(thread != null){						// スレッドが存在している間
			MakingHanabi( );						// 花火作成
            try{
               thread.sleep(sleeptime);				// 指定ミリ秒スレッドスリープ
            }catch(InterruptedException e){			// 他のスレッドの割り込み例外処理
			}
        	repaint( );								// 再描画
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
	// 花火作成 -------------------------------------------------------------------------
	void MakingHanabi( ) {
		for (int n = 0; n < HMAX; n++) {
			if (Process[n] == 0) {					// 花火の準備段階
				Angle[n][0] = 90 - AngleRange/2 + (int)(Math.random( )*AngleRange);
																		// 発射角度
				Speed[n][0] = SpeedMax + (int)(Math.random( )*20);		// 花火のスピード
				X[n][0] = AppletWidth / 2;								// 位置
				Y[n][0] = AppletHeight;
				V0x[n][0] = Speed[n][0] * Math.cos(Angle[n][0] * Rad);	// 初速度x
				V0y[n][0] = Speed[n][0] * Math.sin(Angle[n][0] * Rad);	// 初速度y
				Time[n] = 0;											// 時間カウント
				Process[n] = 1;											// 発射段階
				Hue[n] = (float)Math.random( );							// 花火の色
			}
			else if (Process[n] == 1) {					// 花火飛行段階
				double nowtime = Time[n] / 10.0;		// 時間カウントを時間に変更
				if(V0y[n][0] - 9.8 * nowtime >= 10.0) {	// 垂直スピードが10以上の場合
					WorkGraphics.setColor(Color.black);	// 前回位置消去
					WorkGraphics.drawLine(X[n][0], Y[n][0], X[n][0], Y[n][0]);
					Time[n] += 1;						// 時間カウント
					nowtime = Time[n] / 10.0;			// 時間カウントを時間に変更
					int xt = (int)(V0x[n][0] * nowtime);
					int yt = (int)(V0y[n][0] * nowtime - 9.8/2 * nowtime * nowtime);
					X[n][0] = XP + xt;					// 発射位置からの飛行距離加算
					Y[n][0] = YP - yt;
					WorkGraphics.setColor(Color.white);	// 花火描画
					WorkGraphics.fillRect(X[n][0], Y[n][0], 1, 1);
				}
				else {				// 垂直スピードが10より遅くなった場合，爆発セット
					WorkGraphics.setColor(Color.black);	// 前回位置消去
					WorkGraphics.drawLine(X[n][0], Y[n][0], X[n][0], Y[n][0]);
					Process[n] = 2;						// 爆発段階にセット
					Time[n] = 0;						// 爆発スタート　時間クリア
					for (int i = 1; i < TMAX; i++) {	// 各玉の設定
						Angle[n][i] = (int)(Math.random( ) * 360);			// 発射角度
						Speed[n][i] = (int)(ExplosionSpeed * Math.random( ));// スピード
						X[n][i] = X[n][0];									// 位置
						Y[n][i] = Y[n][0];
						V0x[n][i] = Speed[n][i] * Math.cos(Angle[n][i] * Rad);// 初速度x
						V0y[n][i] = Speed[n][i] * Math.sin(Angle[n][i] * Rad);// 初速度y
					}
				}
			}
			else if (Process[n] >= 2) {					// 爆発段階
				Time[n] += 1;							// 時間カウント

				for (int w = 1; w < TMAX; w++) {		// 玉の数だけ処理
					if (Time[n] >= LEN) {				// 火の長さ分前の位置を消去
						double backtime = (Time[n] - LEN) / 10.0;	// 火の長さ分前の時間
						WorkGraphics.setColor(Color.black);
						WorkGraphics.fillRect(X[n][0] + (int)(V0x[n][w] * backtime),
						 Y[n][0] - (int)(V0y[n][w] * backtime - 9.8/2 * backtime * backtime),
						 SIZE, SIZE);
					}
					double nowtime = Time[n] / 10.0;	// 時間カウントを時間に変換
					int xt = (int)(V0x[n][w] * nowtime);// 位置計算
					int yt = (int)(V0y[n][w] * nowtime - 9.8/2 * nowtime * nowtime);
					X[n][w] = X[n][0] + xt;				// 爆発位置からの飛行距離加算
					Y[n][w] = Y[n][0] - yt;
					if (COUNT - Process[n] > LEN)	{	// 設定した火の長さより大きい場合
						if (Process[n] < COUNT / 2)		// 指定回数の半分までは最大明度
							WorkGraphics.setColor(Color.getHSBColor(Hue[n], 1f, 1f));
						else							// 後半は明度を徐々に落とす
							WorkGraphics.setColor(Color.getHSBColor(Hue[n], 1f,
								1f * (COUNT - Process[n]) / (COUNT / 2)));
					}
					else
						WorkGraphics.setColor(Color.getHSBColor(Hue[n], 1f, 0));// 黒色
					WorkGraphics.fillRect(X[n][w], Y[n][w],	SIZE, SIZE);	// 火を描画
				}
				Process[n]++;						// プロセスを変化
				if (Process[n] > COUNT)				// 指定段階を超えた場合
					Process[n] = 0;					// 最初の段階に戻す
			}
		}
	}
}
