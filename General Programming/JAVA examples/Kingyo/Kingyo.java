import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color
import java.awt.event.*;							// MouseListener, MouseEvent

public class Kingyo extends Applet implements Runnable, MouseListener {
	Image KingyoImage[ ] = new Image[16];			// 16方向のイメージ

	Thread thread = null;							// スレッド
	int Number = 50;								// 金魚数
	int Direction[ ] = new int[Number];				// 各金魚の方向
	int X[ ] = new int[Number];						// 各金魚の位置
	int Y[ ] = new int[Number];
	int MoveRadius = 4;								// 移動半径

	int FoodNumber = 10;							// 餌の数
	int Food[ ] = new int[FoodNumber];				// 餌 0:存在しない,1:存在する
	int FoodX[ ] = new int[FoodNumber];				// 餌の位置
	int FoodY[ ] = new int[FoodNumber];

	int KingyoWidth, KingyoHeight;					// イメージのサイズ
	int AppletWidth, AppletHeight;					// アプレットのサイズ

	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// パラメータから入力
		Number = Integer.parseInt(getParameter("number"));	// 金魚の数
		if (Number > 50)									// 最大数チェック
			Number = 50;

		MediaTracker  mt = new MediaTracker(this);			// メディアトラッカ定義

		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		for (int i = 0; i < 16; i++) {						// 金魚の16方向の画像入力
			KingyoImage[i] = getImage(getCodeBase( ), "image/kingyo"+(i+1)+".gif");
			mt.addImage(KingyoImage[i], 0);					// メディアトラッカにイメージセット
		}

		try {
			mt.waitForID(0);								// イメージ画像の入力完了を待つ
		} catch(InterruptedException e) {
			showStatus(" "+e);
		}

		KingyoWidth = KingyoImage[0].getWidth(this);		// 金魚の幅と高さ
		KingyoHeight = KingyoImage[0].getHeight(this);

		// 金魚の初期化
		for (int i = 0; i < Number; i++) {
			Direction[i] = (int)(Math.random( ) * 16);						// 方向
			X[i] = (int)(Math.random( ) * (AppletWidth - KingyoWidth));		// 位置
			Y[i] = (int)(Math.random( ) * (AppletHeight - KingyoHeight));
		}
		// 餌の初期化
		for (int i = 0; i < FoodNumber; i++)
			Food[i] = 0;									// 餌初期化　ない状態

		addMouseListener(this);								// マウスリスナー追加
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			Swimming( );							// 泳ぐ処理
			repaint( );								// 再描画
			try {
				Thread.sleep(100);					// スレッドスリープ
			} catch (InterruptedException e){
				showStatus(" "+e);
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッドを無効
	}
	// 泳ぐ処理 -------------------------------------------------------------------------
	void Swimming( ) {
		WorkGraphics.setColor(Color.white);
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);

		// 餌処理
		WorkGraphics.setColor(Color.black);
		for (int i = 0; i < FoodNumber; i++) {
			if (Food[i] == 1)
				WorkGraphics.fillOval(FoodX[i], FoodY[i], 3, 3);	// 餌表示
		}

		// 金魚処理
		for (int i = 0; i < Number; i++) {

			// 金魚を現在向いている方向に進める     (3.14 * 2 / 16)  360度を16等分
			double radian = Direction[i] * 3.14 * 2 / 16;			// 金魚の方向角度
			int xp = X[i] + (int)(Math.sin(radian) * MoveRadius);	// 金魚の位置
			int yp = Y[i] + (int)(-Math.cos(radian) * MoveRadius);
			// 場外に出ないようにチェック
			if (xp >= 0 && xp <= AppletWidth - KingyoWidth &&
				yp >= 0 && yp <= AppletHeight - KingyoHeight) {
				X[i] = xp;
				Y[i] = yp;
			}

			WorkGraphics.drawImage(KingyoImage[Direction[i]], xp, yp, this);

			// まだ餌があるかチェック
			int foodFlag = 0;
			for (int e = 0; e < FoodNumber; e++) {
				if (Food[e] == 1)
					foodFlag = 1;					// 餌がある
			}

			// 次の方向セット
			if (foodFlag == 0) {
				// 餌がない場合，乱数を発生して，０：左，１or２：まっすぐ，３：右
				int w = (int)(Math.random( )*4);
				if (w == 0)
					Direction[i]--;
				if (w == 3)
					Direction[i]++;
				if (Direction[i] < 0)
					Direction[i] = 15;
				if (Direction[i] > 15)
					Direction[i] = 0;
			}
			else {
				// 餌がある場合
				// 金魚の現在の場所から最も近い餌を探す
				int minDistance = 9999;
				int nearPoint = -1;
				int distance;
				for (int p = 0; p < FoodNumber; p++) {
					if (Food[p] == 1) {
						double temp = (X[i] - FoodX[p]) * (X[i] - FoodX[p])
									+ (Y[i] - FoodY[p]) * (Y[i] - FoodY[p]);
						if (temp > 0)	// 正の場合は，平行根計算
							distance = (int)(Math.sqrt(temp));
						else
							distance = 0;
						if (minDistance > distance) {
							minDistance = distance;
							nearPoint = p;
						}
					}
				}

				// 左，まっすぐ，右のどちらに行った方が距離が短くなるか調べる
				minDistance = 9999;
				int bestDirection = -1;
				int tempDirection = -1;
				for (int p = -1; p <= 1; p++) {
					tempDirection = Direction[i] + p;
					if (tempDirection < 0)
						tempDirection = 15;
					if (tempDirection > 15)
						tempDirection = 0;
					radian = tempDirection * 3.14 * 2 / 16;		// 金魚の方向角度
												// (3.14 * 2 / 16)は360度を16等分した値
					xp = X[i] + (int)(Math.sin(radian) * MoveRadius);
					yp = Y[i] + (int)(-Math.cos(radian) * MoveRadius);
					if (xp >= 0 && xp <= AppletWidth - KingyoWidth &&
						yp >= 0 && yp <= AppletHeight - KingyoHeight) {

						double temp = (xp - FoodX[nearPoint]) * (xp - FoodX[nearPoint])
									+ (yp - FoodY[nearPoint]) * (yp - FoodY[nearPoint]);
						if (temp > 0)	// 正の場合は，平行根計算
							distance = (int)(Math.sqrt(temp));
						else
							distance = 0;
						if (minDistance > distance) {
							minDistance = distance;
							bestDirection = tempDirection;
						}
					}
				}
				if (bestDirection != -1)			// 最短方向があった場合
					Direction[i] = bestDirection;
				else
					Direction[i] = tempDirection;

				// 餌が金魚の画像内の場合，餌を削除
				if (X[i] <= FoodX[nearPoint] && FoodX[nearPoint] <= X[i] + KingyoWidth &&
					Y[i] <= FoodY[nearPoint] && FoodY[nearPoint] <= Y[i] + KingyoHeight)
					Food[nearPoint] = 0;
			}
		}
	}
	// MouseListenerインターフェースを実装 ----------------------------------------------
    public void mousePressed(MouseEvent evt) {
       	int mouseX = evt.getX( );					// マウスの位置保管
		int	mouseY = evt.getY( );

		// 餌を蒔く
		for (int i = 0; i < FoodNumber; i++) {
			if (Food[i] == 0) {
				Food[i] = 1;
				// 餌の位置  マウスをクリックした位置を中心に半径50の範囲にランダムに撒く
				FoodX[i] = mouseX + (int)(Math.random( ) * 100 - 50);
				FoodY[i] = mouseY + (int)(Math.random( ) * 100 - 50);
				// 円形にランダムに
				if (FoodX[i] <= 0 || FoodX[i] >= AppletWidth ||	// 範囲外の場合
					FoodY[i] <= 0 || FoodY[i] >= AppletHeight)
					Food[i] = 0;
			}
		}
	}
    public void mouseClicked(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
}
