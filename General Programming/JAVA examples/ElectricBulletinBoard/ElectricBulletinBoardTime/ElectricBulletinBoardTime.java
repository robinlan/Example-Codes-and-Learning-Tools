import java.applet.*;										// Applet
import java.awt.*;											// Graphics, Image, Color, Font, FontMetrics
import java.util.*;											// Calendar						

public class ElectricBulletinBoardTime extends Applet implements Runnable {
	Image WorkImage;										// 作業用イメージ
	Graphics WorkGraphics;									// 作業用グラフィックス
	int	AppletWidth, AppletHeight;							// アプレットの幅と高さ
	String Message[ ] = new String[11];						// メッセージ　Max10 + Time
	int MessageNumber,										// メッセージの数
		MessageLength,										// メッセージの長さ
		MessageCount;										// メッセージ表示カウント
	int	Step,												// メッセージの移動間隔
		Mesh,												// メッシュ間隔
		DispPoint;											// メッセージの表示水平位置
	String FontName;										// フォント名
	Font font;												// フォント
	FontMetrics fm;											// フォントメトリックス
	boolean PrepareFlag = false;							// 準備フラグ
	Thread thread = null;									// スレッド

	// 初期化処理 -------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅と高さ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得
		FontName = getParameter("font");					// フォント名
		font = new Font(FontName, Font.BOLD, AppletHeight * 8 / 10);
		fm = getFontMetrics(font);							// フォントメトリックス取得
		MessageNumber = Integer.parseInt(getParameter("number"));// メッセージ数
		for (int i = 0; i < MessageNumber; i++)
			Message[i] = getParameter("message" + i);		// メッセージ取得
		Step = Integer.parseInt(getParameter("step"));		// メッセージの移動間隔
		Mesh = Integer.parseInt(getParameter("mesh"));		// メッシュ間隔
	}
	// アプレット開始 ---------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);							// スレッド生成
		thread.start( );									// スレッドスタート
	}
	// 描画処理 ---------------------------------------------------------------------
	public void paint(Graphics g) {
		if (PrepareFlag == false)							// 準備ができていない場合
			return;

		// 作業グラフィックに描画
		WorkGraphics.setColor(Color.black);						// 背景の色設定
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);	// 背景描画
		WorkGraphics.setFont(font);								// フォント設定
		WorkGraphics.setColor(new Color(0, 255, 0));			// メッセージの色設定
		WorkGraphics.drawString(Message[MessageCount],			// メッセージ描画
								DispPoint, AppletHeight * 8 / 10);
		WorkGraphics.setColor(new Color(0, 50, 0));				// メッシュの色設定
		for (int x = 0; x < AppletWidth; x += Mesh)				// 縦にメッシュを入れる
			WorkGraphics.fillRect(x, 0, 1, AppletHeight);
		for (int y = 0; y < AppletHeight; y += Mesh)			// 横にメッシュを入れる
			WorkGraphics.fillRect(0, y, AppletWidth, 1);
		g.drawImage(WorkImage, 0, 0, this);					// 作業イメージをアプレットに描画
	}
	// スレッド実行 ------------------------------------------------------------------
	public void run( ) {
		// 表示番号と表示位置の初期設定
		MessageCount = 0;									// メッセージ表示カウントクリア
		DispPoint = AppletWidth;							// 表示位置を右端に設定
		MessageLength = fm.stringWidth(Message[MessageCount]); // メッセージの長さ

		PrepareFlag = true;									// 準備完了

        while (thread != null) {							// スレッドが存在している間
			try {
				thread.sleep(50);							// 50ミリ秒スリープ
			} catch(InterruptedException e) {				// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}
			DispPoint -= Step;								// 表示位置をstep分ずらす
			if (DispPoint < -MessageLength) {				// メッセージが画面の外に出た場合
				MessageCount++;								// 次のメッセージ番号
				if (MessageCount == MessageNumber)			// ラストの場合						
					Message[MessageCount] = NowTime( );		// 現在の時刻をメッセージに		
				if (MessageCount > MessageNumber)			// メッセージ数を超えた場合			
					MessageCount = 0;						// 最初のメッセージ					
				DispPoint = AppletWidth;					// 表示位置を右端にセット
				MessageLength = fm.stringWidth(Message[MessageCount]);// メッセージの長さ
			}

			repaint( );										// 再描画
		}
	}
	// 時間メッセージ生成 --------------------------------------------------------	
	String NowTime( ) {																
        String Weeks[ ] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };		
		Calendar date = Calendar.getInstance( );									
		int Year = date.get(Calendar.YEAR);						// 年				
		int Month = date.get(Calendar.MONTH) + 1;				// 月				
		int Day = date.get(Calendar.DAY_OF_MONTH);				// 日				
		String Week = Weeks[date.get(Calendar.DAY_OF_WEEK)-1];	// 曜日				
		String Hour = "0" + date.get(Calendar.HOUR);			// 時間
		Hour = Hour.substring(Hour.length( ) - 2);				
		String Minute = "0" + date.get(Calendar.MINUTE);		// 分
		Minute = Minute.substring(Minute.length( ) - 2);
		return Year+"/"+Month+"/"+Day+"("+Week+")"+Hour+":"+Minute;					
	}	
	// 描画更新処理再定義 -----------------------------------------------------------
	public void update(Graphics g) {						// デフォルトのupdateを再定義
		paint(g);											// paintのみでチラツキ防止（背景色描画削除）
	}
	// アプレット停止 ---------------------------------------------------------------
	public void stop( ) {
		thread = null;										// スレッド無効
	}
}
