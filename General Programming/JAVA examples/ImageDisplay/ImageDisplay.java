import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, MediaTracker
import java.awt.image.*;							// MemoryImageSource

public class ImageDisplay extends Applet implements Runnable {
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	Thread thread = null;							// スレッド	
	int Number;										// 画像数
	Image image[ ] = new Image[30];					// 描画画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	Image[ ] FadeImage = new Image[16];				// 白色フェードイメージ（透明16段階）

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;				// アプレットの幅
		AppletHeight = getSize( ).height;			// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業グラフィックス取得

		// パラメータよりデータ入力
		Number = Integer.parseInt(getParameter("number"));	// 画像数
		MediaTracker mediatracker = new MediaTracker(this);
		for (int i = 0; i < Number; i++) {					// 画像データ入力
			image[i] = getImage(getCodeBase( ), getParameter("image" + i));
			mediatracker.addImage(image[i], 0);
		}
		try {
			mediatracker.waitForID(0);				// メディアトラッカで入力監視
		}
		catch (InterruptedException e) {
		}

		makeFadeImages( );							// フェードのイメージ作成
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージを描画
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		int current = 0,							// 現在の画像番号
			next;									// 次の画像番号
		while (thread != null) {					// スレッドが存在している間
			do {
				next = (int)(Math.random( ) * Number);	// 表示画像をランダムに設定
			} while (current == next);					// 表示画像が同じ間

			int effect = (int)(Math.random( ) * 21);	// 表示パターンをランダムに設定

			switch (effect) {
				case  0: 	// 左からスクロールイン
							Scroll_In_Display(image[current], image[next],  2,  0);
						 	break;
				case  1: 	// 上から
							Scroll_In_Display(image[current], image[next],  0,  2);
						 	break;
				case  2: 	// 右から
							Scroll_In_Display(image[current], image[next], -2,  0);
						 	break;
				case  3: 	// 下から
							Scroll_In_Display(image[current], image[next],  0, -2);
						 	break;

				case  4: 	// 右にスクロールアウト
							Scroll_Out_Display(image[current], image[next],  2,  0);
						 	break;
				case  5: 	// 下に
							Scroll_Out_Display(image[current], image[next],  0,  2);
						 	break;
				case  6: 	// 左に
							Scroll_Out_Display(image[current], image[next], -2,  0);
						 	break;
				case  7: 	// 上に
							Scroll_Out_Display(image[current], image[next],  0, -2); 
						 	break;

				case  8: 	// 左から剥ぎ取る
							Tear_Display(image[current], image[next],  2,  0);
						 	break;
				case  9: 	// 上から
							Tear_Display(image[current], image[next],  0,  2);
						 	break;
				case 10: 	// 右から
							Tear_Display(image[current], image[next], -2,  0);
						 	break;
				case 11: 	// 下から
							Tear_Display(image[current], image[next],  0, -2);
							break;

				case 12: 	// 中央から左右にオープン
							Open_Close_Display(image[current], image[next],  2,  0);
						 	break;
				case 13: 	// 中央から上下に
							Open_Close_Display(image[current], image[next],  0,  2);
						 	break;
				case 14: 	// 左右の端から中央に
							Open_Close_Display(image[current], image[next], -2,  0);
							break;
				case 15: 	// 上下の端から中央に
							Open_Close_Display(image[current], image[next],  0, -2);
							break;
				case 16: 	// 中央から全体に
							Open_Close_Display(image[current], image[next],  2,  2);
						 	break;
				case 17: 	// ４隅の端から中央に
							Open_Close_Display(image[current], image[next], -2, -2);
						 	break;

				case 18: 	// フェードアウト・イン
							Fade_Display(image[current], image[next]);
						 	break;

				case 19: 	// モザイク調に
							Mosaic_Display(image[current], image[next]);
						 	break;

				case 20: 	// ブライド調に
							Blind_Display(image[current], image[next], 2);
						 	break;
			}
			current = next;							// 次の画像を描画番号とする

			try {									// 次の画像表示までの待ち時間
				Thread.sleep(3000);					// スレッドスリープ　3秒間，画像表示
			} catch (InterruptedException e) {
			}
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
	// スクロールイン -------------------------------------------------------------------
	// 画像の縦横のサイズは偶数であること
	void Scroll_In_Display(Image current, Image next, int dx, int dy) {
		// current:現在の画像 next:次の画像 dx:x方向移動ステップ dy:ｙ方向移動ステップ

		int x = 0, y = 0;							// 描画位置
		// 描画ステップによって描画初期位置設定
		if (dx > 0)									// 描画xステップ＋　右に移動
			x = -AppletWidth;						// 左側からスクロールイン
		else if (dx < 0)							// 描画xステップ－　左に移動
			x = AppletWidth;						// 右側からスクロールイン
		else if (dy > 0)							// 描画yステップ＋　下に移動
			y = -AppletHeight;						// 上側からスクロールイン
		else if (dy < 0)							// 描画yステップー　上に移動
			y = AppletHeight;						// 下側からスクロールイン

		do {
			x += dx;								// 描画位置を移動
			y += dy;
			WorkGraphics.drawImage(current, 0, 0, this);	// 今回の画像描画
			WorkGraphics.drawImage(next, x, y, this);		// 次回の画像描画
			repaint( );
			try {
				Thread.currentThread( ).sleep(50);
			} catch (InterruptedException e) {
			}
		} while (x != 0 || y != 0);
	}
	// スクロールアウト -----------------------------------------------------------------
	void Scroll_Out_Display(Image current, Image next, int dx, int dy) {
		// current:現在の画像 next:次の画像 dx:x方向移動ステップ dy:ｙ方向移動ステップ

		int x = 0, y = 0;									// 描画位置
		while (x >= -AppletWidth && x <= AppletWidth		// 描画位置がアプレット範囲内
			   && y >= -AppletHeight && y <= AppletHeight) {
			WorkGraphics.drawImage(next, 0, 0, this);		// 次回の画像を描画
			WorkGraphics.drawImage(current, x, y, this);	// 今回の画像を描画
			repaint( );
			try {
				Thread.currentThread( ).sleep(50);
			} catch (InterruptedException e) {
			}
			x += dx;										// 描画位置を移動
			y += dy;
		}
	}
	// 剥ぎ取るように表示（オーバーライト）----------------------------------------------
	void Tear_Display(Image current, Image next, int dx, int dy) {
		// current:現在の画像 next:次の画像 dx:x方向移動ステップ dy:ｙ方向移動ステップ

		WorkGraphics.drawImage(current, 0, 0, this);// 今回の画像を描画

		int x = 0, y = 0;							// 描画位置
		int w = AppletWidth;						// 描画幅
		int h = AppletHeight;						// 描画高さ
		if (dx > 0) {
			x = 0;  w = 0;
		}
		else if (dx < 0) {
			x = AppletWidth;  w = 0;
		}
		else if (dy > 0) {
			y = 0;  h = 0;
		}
		else if (dy < 0) {
			y = AppletHeight;  h = 0;
		}
		do {
			WorkGraphics.clipRect(x, y, w, h);  		// 描画領域をクリップ
			WorkGraphics.drawImage(next, 0, 0, null);	// 次の画像描画
			repaint( );
			try {
				Thread.currentThread( ).sleep(50);
			} catch (InterruptedException e) {
			}
			WorkGraphics = WorkImage.getGraphics( );	// 再度描画領域を取得

			if (dx > 0)								// 描画位置を移動
				w += dx;							// 下地を左から右に剥ぎ取る
			else if(dx < 0) {
				w -= dx; x += dx;					// 下地を右から左に剥ぎ取る
			}
			else if (dy > 0)						// 下地を上から下に剥ぎ取る
				h += dy;
			else if (dy < 0) {
				h -= dy; y += dy;					// 下地を下から上に剥ぎ取る
			}
		} while (w <= AppletWidth && h <= AppletHeight);
	}
	// センターに対してのオープンクローズ表示 -------------------------------------------
	void Open_Close_Display(Image current, Image next, int dx, int dy) {
		// current:現在の画像 next:次の画像 dx:x方向移動ステップ dy:ｙ方向移動ステップ

		int x = 0, y = 0;							// 描画位置
		int w = AppletWidth;						// 描画幅
		int h = AppletHeight;						// 描画高さ
		// dx,dyが正の場合，中心からオープンしながら表示
		//        負の場合，外側からクローズしながら背景に表示
		if (dx > 0) {
			x = AppletWidth / 2;  w = 0;
		}
		if (dy > 0) {
			y = AppletHeight / 2;  h = 0;
		}

		if (dx > 0 || dy > 0)
			WorkGraphics.drawImage(current, 0, 0, this);	// 現在の画像を描画

		int flag = 1;										// 繰り返しフラグ
		do {
			if (dx < 0 || dy < 0)  							// クローズしていく場合
				WorkGraphics.drawImage(next, 0, 0, this);

			WorkGraphics.clipRect(x, y, w, h);  			// 描画領域をクリップ

			if (dx > 0 || dy > 0)  							// 中心からオープンしていく場合
				WorkGraphics.drawImage(next, 0, 0, this);	// 次の画像が中央より現れる
			else
				WorkGraphics.drawImage(current, 0, 0, this);// 現在の画像が中央に狭まる

			repaint( );
			try {
				Thread.currentThread( ).sleep(50);
			} catch (InterruptedException e) {
			}

			WorkGraphics = WorkImage.getGraphics( );// 再度描画領域を取得

			x -= dx;  w += (dx * 2);
			y -= dy;  h += (dy * 2);

			// 処理を続けるかどうかのチェック
			flag = 0;
			if (dx > 0 && w <= AppletWidth || dy > 0 && h <= AppletHeight ||
				dx < 0 && w >= 0  || dy < 0 && h >= 0)
				flag = 1;

			// w, h がマイナスになる場合, またはサイズより大きくなる場合
			if (flag == 0) {	
				WorkGraphics.drawImage(next, 0, 0, this);
				repaint( );
			}
		} while (flag == 1);
	}
	// フェードアウト・イン -------------------------------------------------------------
	void Fade_Display(Image current, Image next) {
		// current:現在の画像 next:次の画像

		for (int i = 0; i < 16; i++) {						// 徐々に消す
			WorkGraphics.drawImage(current, 0, 0, this);	// 現在の画像を描画
			for (int y = 0; y < AppletHeight; y += 64) {
				for (int x = 0; x < AppletWidth; x += 64)
					WorkGraphics.drawImage(FadeImage[i], x, y, this);
			}

			repaint( );
			try {
				Thread.currentThread( ).sleep(100);
			} catch (InterruptedException e) { };
		}

		for (int i = 16-1; i >= 0; i--) {					// 徐々に表示
			WorkGraphics.drawImage(next, 0, 0, this);		// 次の画像を描画
			for (int y = 0; y < AppletHeight; y += 64)	{
				for (int x = 0; x < AppletWidth; x += 64)
					WorkGraphics.drawImage(FadeImage[i], x, y, this);
			}
			repaint( );
			try {
				Thread.currentThread( ).sleep(100);
			} catch (InterruptedException e) { }
		}
	}
	// フェードイメージ作成 -------------------------------------------------------------
	void makeFadeImages( ) {
		// 64 x 64の領域のアルファ値（不透明度）を16段階作成
		int[ ][ ] pix = new int[16][64 * 64];

		for (int i = 0; i < 16; i++) {				// 0x00 透明 <-----> 不透明 0xff
			int p = 0;
			for  (int  y  =  0;  y <  64;  y++) {
				for  (int  x  =  0;  x  <  64;  x++)
					pix[i][p++]  =  (255 * i / 16)  <<  24 | 0x0ffffff;
													// フェード色は0x0ffffff（白色)
			}
			MemoryImageSource mis = new MemoryImageSource(64, 64, pix[i], 0, 64);
			FadeImage[i] = createImage(mis);
		}
	}
/*
public MemoryImageSource(int  w, int  h, int  pix[ ],int  off, int  scan)				  
　int 型の配列からイメージオブジェクトのデータを作成するイメージプロデューサのオブジェクトを作成します。座標 (i, j) のピクセルは，配列のインデックス j × scan + i + offset の要素によって決定されます。	イメージはデフォルトの RGB カラーモデルを使用して作成されます。
	パラメータ
		w - イメージの幅
		h - イメージの高さ
		pix - ピクセル値の配列
		off - 最初のピクセルの配列内オフセット
		scan - 配列内での 1 行あたりのピクセル数

public static ColorModel getRGBdefault( )												  
　デフォルトの AWT (Abstract Window Toolkit) カラーモデルを返します。AWT は，32 ビットの整数でピクセル値を示します。24 ～ 31 ビットはアルファ値（不透明度），16 ～ 23 ビットは赤，8 ～ 15 ビットは緑，0 ～ 7 ビットは青の値です。このオブジェクトは同形式のピクセル値からアルファ値，赤，緑，および青の値を取り出すのに使用できます。
*/
	// モザイク調に表示 -----------------------------------------------------------------
	void Mosaic_Display(Image current, Image next) {
		// current:現在の画像 next:次の画像

		// モザイクに分ける縦と横の数
		int MSIZE = 32;								// モザイクの縦横のサイズ
		int row = AppletHeight / MSIZE;				// 行
		int col = AppletWidth / MSIZE;				// 列
		if (row * MSIZE < AppletHeight)				// 中途半端の場合切り上げ
			row++;
		if (col * MSIZE < AppletWidth)				// 中途半端の場合切り上げ
			col++;

		Point mosaic_point[ ] = new Point[row * col];

		for (int i = 0; i < mosaic_point.length; i++)	// 位置情報を設定
			mosaic_point[i] = new Point((i % col)*MSIZE, (i / col)*MSIZE);

		// モザイクに分けた各配列のポイントをランダムに入替える
		for (int i = 0; i < mosaic_point.length; i++) {
			int rp = (int)(Math.random( ) * mosaic_point.length);
			// i番目とrp番目を入れ替え
			Point temp = mosaic_point[i];
			mosaic_point[i] = mosaic_point[rp];
			mosaic_point[rp] = temp;
		}
		
		WorkGraphics.drawImage(current, 0, 0, this);

		for (int i = 0; i < mosaic_point.length; i++) {		// ランダムに入替えた内容を描画
			int xp = mosaic_point[i].x;
			int yp = mosaic_point[i].y;
			// クリップ領域設定
			WorkGraphics.clipRect(xp, yp, MSIZE, MSIZE);	// 描画領域をクリップ
			WorkGraphics.drawImage(next, 0, 0, null);		// 次の画像を描画
			repaint( );
			try {
				Thread.currentThread( ).sleep(50);
			} catch (InterruptedException e) {
			}
			WorkGraphics = WorkImage.getGraphics( ); 		// 再度描画領域を取得
		}
	}
	// ブラインド表示 -------------------------------------------------------------------
	void Blind_Display(Image current, Image next, int haba) {
		// current:現在の画像 next:次の画像 haba:ブラインド幅

		int left = 0;
		int right = AppletWidth - 1;
		WorkGraphics.drawImage(current, 0, 0, this);
		do {
			// クリップ領域設定
			WorkGraphics.clipRect(left, 0, 1, AppletHeight);	// 描画領域をクリップ
			WorkGraphics.drawImage(next, 0, 0, null);			// 左から次の画像を
			repaint( );
			WorkGraphics = WorkImage.getGraphics( ); 			// 再度描画領域を取得
			WorkGraphics.clipRect(right, 0, 1, AppletHeight);	// 描画領域をクリップ
			WorkGraphics.drawImage(next, 0, 0, null);			// 右から次の画像を
			repaint( );
			WorkGraphics = WorkImage.getGraphics( ); 			// 再度描画領域を取得
			try {
				Thread.currentThread( ).sleep(20);
			} catch (InterruptedException e) {
			}
			left += haba;										// 左の位置を右に移動
			right -= haba;										// 右の位置を左に移動
		} while (left < AppletWidth || right > 0);				// 外に出た場合
	}
}
