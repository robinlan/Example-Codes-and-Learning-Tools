import java.applet.*;								// Applet
import java.awt.*;									// Graphic, Image, Color, Font etc

public class Neon extends Applet implements Runnable{
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
    Thread thread = null;							// スレッド
	int AppletWidth, AppletHeight;					// アプレットのサイズ

	// 電球 -----------------------------------------------------------------------------
	int DenkyuSize = 20;							// 電球サイズ
	int DenkyuKazu = 0;								// 電球数
	int DenkyuX[ ] = new int[200];					// 電球位置
	int DenkyuY[ ] = new int[200];
	int DenkyuDisp[ ] = new int[200];				// 電球状態 1:点灯 0:消灯
	int DenkyuProcess = 0;							// 電球プロセス
	int DenkyuSyoriCount = 0;						// 電球処理カウント

	// 蛍光灯 ---------------------------------------------------------------------------
	int KeikoutouSize = 5;							// 蛍光灯横サイズ
	int KeikoutouKazu = 0;							// 蛍光灯数
	int KeikoutouX[ ] = new int[200];				// 蛍光灯位置
	int KeikoutouY[ ] = new int[200];
	int KeikoutouDisp[ ] = new int[200];			// 蛍光灯状態 1:点灯 0:消灯
	int KeikoutouProcess = 0;						// 蛍光灯プロセス
	int KeikoutouSyoriCount = 0;					// 蛍光灯処理カウント

	// 文字 -----------------------------------------------------------------------------
	String Message;									// メッセージの文字
	String Moji[ ] = new String[50];				// 1文字づつ格納する配列
	int Mojisu = 0;									// 文字の数
	int MojiX[ ] = new int[50];						// 文字の位置
	int MojiY[ ] = new int[50];
	int MojiDisp[ ] = new int[50];					// 文字の状態 1:点灯 0:消灯
	int MojiProcess = 0;							// 文字プロセス
	int MojiSyoriCount = 0;							// 文字処理カウント

	// 初期化処理 -----------------------------------------------------------------------
    public void init( ){
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		Message = getParameter("message");			// パラメータよりメッセージ取得

	// 電球のセット -----------------------------------------------------------------
		DenkyuKazu = 0;
		// 上の左から配置
		for (int i = 0; i <= AppletWidth - DenkyuSize; i += DenkyuSize) {
			DenkyuX[DenkyuKazu] = i;
			DenkyuY[DenkyuKazu] = 0;
			DenkyuDisp[DenkyuKazu] = 0;
			DenkyuKazu++;
		}
		// 右の上から配置
		for (int i = DenkyuSize; i <= AppletHeight - DenkyuSize; i += DenkyuSize) {
			DenkyuX[DenkyuKazu] = AppletWidth - DenkyuSize;
			DenkyuY[DenkyuKazu] = i;
			DenkyuDisp[DenkyuKazu] = 0;
			DenkyuKazu++;
		}
		// 右下から左へ配置
		for (int i = AppletWidth - DenkyuSize * 2; i >= 0; i -= DenkyuSize) {
			DenkyuX[DenkyuKazu] = i;
			DenkyuY[DenkyuKazu] = AppletHeight - DenkyuSize;
			DenkyuDisp[DenkyuKazu] = 0;
			DenkyuKazu++;
		}
		// 左下から上へ配置
		for (int i = AppletHeight - DenkyuSize * 2; i >= DenkyuSize; i -= DenkyuSize) {
			DenkyuX[DenkyuKazu] = 0;
			DenkyuY[DenkyuKazu] = i;
			DenkyuDisp[DenkyuKazu] = 0;
			DenkyuKazu++;
		}

		// 蛍光灯のセット ---------------------------------------------------------------
		KeikoutouKazu = 0;
		for (int i = DenkyuSize; i < AppletWidth - DenkyuSize; i += KeikoutouSize * 2) {
			KeikoutouX[KeikoutouKazu] = i;
			KeikoutouY[KeikoutouKazu] = DenkyuSize;
			KeikoutouDisp[KeikoutouKazu] = 0;
			KeikoutouKazu++;
		}

		// 文字 -------------------------------------------------------------------------
		int MojiSize = (AppletWidth - DenkyuSize*2) / Message.length( );
		// System Helvetica  TimesRoman  Courier  Dialog  Symbol 'ＭＳ 明朝'
		Font font = new Font("System", Font.BOLD, MojiSize);
		WorkGraphics.setFont(font);
		FontMetrics fontmetrics = getFontMetrics(font);	// フォントメトリックス取得
		int MojiHeight = fontmetrics.getAscent( ); 		// + fm.getDescent( );//文字の高さ
		Mojisu = 0;
		for (int i = 0; i < Message.length( ); i++) {
			Moji[Mojisu] = Message.substring(i, i+1);// 文字を1文字ずつセット
			MojiX[Mojisu] = DenkyuSize*2 + i * MojiSize;
			MojiY[Mojisu] = (AppletHeight + MojiHeight) / 2;
			MojiDisp[Mojisu] = 0;
			Mojisu++;
		}
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッド開始
	}
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g){
		if (WorkGraphics == null)
			return;

		// 全面クリア
		WorkGraphics.setColor(new Color(0, 0, 0));
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);

		// 電球描画
		for (int i = 0; i < DenkyuKazu; i++) {
			if (DenkyuDisp[i] == 1) {				// 点灯
				for (int r = DenkyuSize; r > 0; r-=2) {
					WorkGraphics.setColor(new Color(250 - r*9, 250 - r*9, 0));
					WorkGraphics.fillOval(DenkyuX[i] + (DenkyuSize - r) / 2,
										 DenkyuY[i] + (DenkyuSize - r) / 2, r, r);
				}
			}
			else {									// 消灯
				for (int r = DenkyuSize; r > 0; r-=2) {
					WorkGraphics.setColor(new Color(125 - r*5, 125 - r*5, 0));
					WorkGraphics.fillOval(DenkyuX[i] + (DenkyuSize - r) / 2,
										 DenkyuY[i] + (DenkyuSize - r) / 2, r, r);
				}
			}
		}

		// 蛍光灯描画
		for (int i = 0; i < KeikoutouKazu; i++) {
			if (KeikoutouDisp[i] == 1)				// 点灯
				WorkGraphics.setColor(new Color(0, 100, 250));
			else									// 消灯
				WorkGraphics.setColor(new Color(0, 0, 80));
			WorkGraphics.fillRect(KeikoutouX[i], KeikoutouY[i],
									KeikoutouSize, AppletHeight - 20*2);
		}

		// 文字描画
		for (int i = 0; i < Mojisu; i++) {
			if (MojiDisp[i] == 1)					// 点灯
				WorkGraphics.setColor(new Color(240, 240, 240));
			else									// 消灯
				WorkGraphics.setColor(new Color(30, 30, 30));
			WorkGraphics.drawString(Moji[i], MojiX[i], MojiY[i]);
		}

		g.drawImage(WorkImage, 0, 0, this);			// アプレットのグラフィックに描画
    }
	// スレッド実行 ---------------------------------------------------------------------
    public void run( ){
        while(thread != null){						// スレッドが存在している間
			DenkyuShori( );							// 電球描画処理
			KeikoutouShori( );						// 蛍光灯描画処理
			MojiShori( );							// 文字描画処理
			repaint( );								// 再描画
            try{
               thread.sleep(200);					// スレッドを指定ミリ秒スリープ
            }catch(InterruptedException e){}
        }
    }
	// 電球処理 -------------------------------------------------------------------------
    void DenkyuShori( ){
		switch (DenkyuProcess) {
			case 0:	DenkyuRotation(30);	break;		// 電球ローテーション点灯
			case 1:	DenkyuFlush(5);		break;		// 電球フラッシュ
			default: DenkyuProcess = 0;
		}
	}
	// 電球回転点灯 ---------------------------------------------------------------------
	void DenkyuRotation(int kaisu) {
		for (int i = 0; i < DenkyuKazu; i++) {
			if ((i + DenkyuSyoriCount) % 8 == 0)	// 8個おきに消灯
				DenkyuDisp[i] = 0;					// 消灯
			else
				DenkyuDisp[i] = 1;					// 点灯
		}
		DenkyuSyoriCount++;							// 電球処理カウント
		if (DenkyuSyoriCount == kaisu) {			// 指定回数になるまで
			DenkyuProcess++;						// 次のプロセスへ
			DenkyuSyoriCount = 0;					// 電球処理カウントクリア
		}
	}
	// 電球フラッシュ -------------------------------------------------------------------
	void DenkyuFlush(int kaisu) {
		for (int i = 0; i < DenkyuKazu; i++) {
			if (DenkyuSyoriCount % 2 == 0)			// 2回に1回
				DenkyuDisp[i] = 0;					// 消灯
			else
				DenkyuDisp[i] = 1;					// 点灯
		}
		DenkyuSyoriCount++;							// 電球処理カウント
		if (DenkyuSyoriCount == kaisu) {			// 指定回数になるまで
			DenkyuProcess++;						// 次のプロセスへ
			DenkyuSyoriCount = 0;					// 電球処理カウントクリア
		}
	}
	// 蛍光灯処理 -----------------------------------------------------------------------
    void KeikoutouShori( ){
		switch (KeikoutouProcess) {
			case 0:	KeikoutouSideDisp(1);	break;	// サイドから点灯
			case 1:	KeikoutouSideDisp(0);	break;	// サイドから消灯
			case 2:	KeikoutouLeftDisp(1);	break;	// 左から点灯
			case 3: KeikoutouLeftDisp(0);	break;	// 左から消灯
			case 4: KeikoutouFlush(5);		break;	// フラッシュ
			case 5: KeikoutouRightDisp(1);	break;	// 右から点灯
			case 6: KeikoutouFlush(3);		break;	// フラッシュ
			default: KeikoutouProcess = 0;
		}
	}
	// 蛍光灯左から点灯・消灯 -----------------------------------------------------------
	void KeikoutouLeftDisp(int sw) {
		KeikoutouDisp[KeikoutouSyoriCount] = sw;	// 1:点灯　0:消灯
		KeikoutouSyoriCount++;						// 蛍光灯処理カウント
		if (KeikoutouSyoriCount == KeikoutouKazu) {	// 電機棒の数まで
			KeikoutouProcess++;						// 次のプロセスへ
			KeikoutouSyoriCount = 0;				// 蛍光灯処理カウントクリア
		}
	}
	// 蛍光灯右から点灯・消灯 -----------------------------------------------------------
	void KeikoutouRightDisp(int sw) {
		KeikoutouDisp[KeikoutouKazu - KeikoutouSyoriCount - 1] = sw;	// 1:点灯　0:消灯
		KeikoutouSyoriCount++;						// 蛍光灯処理カウント
		if (KeikoutouSyoriCount == KeikoutouKazu) {	// 電機棒の数まで
			KeikoutouProcess++;						// 次のプロセスへ
			KeikoutouSyoriCount = 0;				// 蛍光灯処理カウントクリア
		}
	}
	// 蛍光灯サイドから点灯・消灯 -------------------------------------------------------
	void KeikoutouSideDisp(int sw) {
		KeikoutouDisp[KeikoutouSyoriCount] = sw;						// 左から
		KeikoutouDisp[KeikoutouKazu - KeikoutouSyoriCount - 1] = sw;	// 右から
		KeikoutouSyoriCount++;						// 蛍光灯処理カウント
		if (KeikoutouSyoriCount == (KeikoutouKazu / 2)) {	// 中央の半分まで
			KeikoutouProcess++;						// 次のプロセスへ
			KeikoutouSyoriCount = 0;				// 蛍光灯処理カウントクリア
		}
	}
	// 蛍光灯フラッシュ -----------------------------------------------------------------
	void KeikoutouFlush(int kaisu) {
		for (int i = 0; i < KeikoutouKazu; i++) {
			if (KeikoutouSyoriCount % 2 == 0) 		// 2回に1回
				KeikoutouDisp[i] = 1;				// 点灯
			else
				KeikoutouDisp[i] = 0;				// 消灯
		}
		KeikoutouSyoriCount++;						// 蛍光灯処理カウント
		if (KeikoutouSyoriCount == kaisu * 2) {		// 点灯・消灯を行うため2倍
			KeikoutouProcess++;						// 次のプロセスへ
			KeikoutouSyoriCount = 0;				// 蛍光灯処理カウントクリア
		}
	}

	// 文字処理 -------------------------------------------------------------------------
    void MojiShori( ){
		switch (MojiProcess) {
			case 0:	MojiLeftDisp(1);	break;		// 左から点灯
			case 1:	MojiLeftDisp(0);	break;		// 左から消灯
			case 2:	MojiFlush(5);		break;		// 文字フラッシュ
			default: MojiProcess = 0;
		}
	}
	// 文字左から点灯・消灯 -------------------------------------------------------------
	void MojiLeftDisp(int sw) {
		MojiDisp[MojiSyoriCount] = sw;				// 1:点灯　0:消灯
		MojiSyoriCount++;							// 文字処理カウント
		if (MojiSyoriCount == Mojisu) {				// 指定回数まで
			MojiProcess++;							// 次のプロセスへ
			MojiSyoriCount = 0;						// 文字処理カウントクリア
		}
	}
	// 文字フラッシュ -------------------------------------------------------------------
	void MojiFlush(int kaisu) {
		for (int i = 0; i < Mojisu; i++) {
			if (MojiSyoriCount % 2 == 0) 			// 2回に1回
				MojiDisp[i] = 0;					// 消灯
			else
				MojiDisp[i] = 1;					// 点灯
		}
		MojiSyoriCount++;							// 文字処理カウント
		if (MojiSyoriCount == kaisu) {				// 指定回数まで
			MojiProcess++;							// 次のプロセスへ
			MojiSyoriCount = 0;						// 文字処理カウントクリア
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
