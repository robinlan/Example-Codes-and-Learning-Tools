import java.applet.*;					// Applet, AudioClip
import java.awt.*;						// Graphics, Image, Color, MediaTracker
import java.awt.event.*;				// MouseListener, MouseMotionListener etc
import java.awt.image.*;				// MemoryImageSource, PixelGrabber

public class PuzzleMatch extends Applet implements MouseListener, MouseMotionListener {
	int AppletWidth, AppletHeight;					// アプレットのサイズ

	int Number = 20;								// ピースの枚数
	String FileName;								// ピースのファイル名
	Image Piece[ ] = new Image[Number];				// 各ピースの画像
	int PieceWidth, PieceHeight;					// ピースのサイズ
	int PieceX[ ] = new int[Number];				// ピースの位置
	int PieceY[ ] = new int[Number];
	int Priority[ ] = new int[Number];				// ピースの優先順位（最下位0～11）
	int ClickPieceNo;								// クリックしたピース番号
	int ClickDistanceX, ClickDistanceY;				// クリック位置と画像の描画位置の距離
	String BackgroundName = null;					// 背景ファイル名
	Image Background;								// 背景画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	Image CheckImage;								// チェックイメージ
	Graphics CheckGraphics;							// チェックグラフィック
	boolean MouseDownSw;							// マウスダウンスイッチ
	AudioClip ClickSound;							// クリック音
	int Pixels1[ ] = new int[200 * 200];			// ピクセルデータ保管(200x200)
	int Pixels2[ ] = new int[200 * 200 * Number];	// ピクセルデータ保管(200x200xNumber)

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットの幅
		AppletHeight = getSize( ).height;					// アプレットの高さ

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		CheckImage = createImage(1, 1);						// ピクセルカラー値取得作業領域
		CheckGraphics = CheckImage.getGraphics( );			// チェック作業グラフィックス取得

		// パラメータ取得
		Number = Integer.parseInt(getParameter("number"));	// ピースの枚数
		FileName = getParameter("filename");				// ピースの名前
		BackgroundName = getParameter("background");		// 背景

		// データ入力
        ClickSound = getAudioClip(getCodeBase( ), "sound/click.au");

		// 画像入力を監視するメディアトラッカ生成
		MediaTracker mediatracker = new MediaTracker(this);
		for (int i = 0; i < Number; i++) {					// 各ピースの画像入力
			Piece[i] = getImage(getCodeBase( ), FileName+i+".gif");
			mediatracker.addImage(Piece[i], 0);
			Priority[i] = i;								// 画像番号セット　優先順位設定
		}
		Background = getImage(getCodeBase( ), BackgroundName);	// 枠画像入力
		mediatracker.addImage(Background, 0);
		try {
			mediatracker.waitForID(0);						// 画像入力の完了を待つ
		} catch (InterruptedException e) {					// waitForIDに対する例外処理
			showStatus(" " + e);
		}

		PieceWidth = Piece[0].getWidth(this);				// ピースの幅と高さ
		PieceHeight = Piece[0].getHeight(this);

		addMouseListener(this);								// マウスリスナー追加
		addMouseMotionListener(this);						// マウスリスナー追加

		MouseDownSw = false;
		Shake( );											// シェイク
		repaint( );
	}
	// シェイク処理 ---------------------------------------------------------------------
	public void Shake( ) {							// 配置場所をランダムにする
		for (int i = 0; i < Number; i++) {
			PieceX[i] = (int)(Math.random( )*(AppletWidth - PieceWidth));
			PieceY[i] = (int)(Math.random( )*(AppletHeight - PieceHeight));
		}
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		if (BackgroundName == null) {				// 背景画像の指定がない場合
			WorkGraphics.setColor(Color.white);		// 描画を黒色で塗りつぶす
			WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);
		}
		else {
			WorkGraphics.drawImage(Background, 0, 0, this);		// 枠表示
		}

		// 各ピースを表示
		for (int i = 0; i < Number; i++) {
			int p = Priority[i];					// 優先順位
			WorkGraphics.drawImage(Piece[p], PieceX[p], PieceY[p], this);
		}
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// MouseListenerインターフェースを実装 ----------------------------------------------
	public void mousePressed(MouseEvent evt) {
		int mouseX = evt.getX( );
		int mouseY = evt.getY( );

		// その位置にピースがあるかどうかのチェック，ラストから行う
		for (int i = Number - 1; i >= 0 && MouseDownSw == false; i--) {
			int p = Priority[i];
			if (mouseX >= PieceX[p] && mouseX < PieceX[p] + PieceWidth 
				&& mouseY >= PieceY[p] && mouseY < PieceY[p] + PieceHeight) {

				// ポイントのカラー情報
				int color = GetPixels(Piece[p], mouseX-PieceX[p], mouseY-PieceY[p]);
				int alpha = (color >> 24) & 0xff; 	// 不透明度　0:透明～255:不透明

				if (alpha == 0xff) {				// 不透明，画像がある場合
					if ((evt.getModifiers( ) & InputEvent.BUTTON1_MASK) != 0)// 左ボタン
						MouseDownSw = true;			// ドラッグの開始スイッチ
					// マウスの位置と画像の描画位置との距離
					ClickDistanceX = mouseX - PieceX[p];
					ClickDistanceY = mouseY - PieceY[p];
					// 描画順位・チェック優先順位再設定 ----------------------------------
					int tempNo = p;
					for (int n = i + 1; n < Number; n++) {
						Priority[n-1] = Priority[n];
					}
					// クリックしたピースをラストに移動
					ClickPieceNo = Priority[Number - 1] = tempNo;
					ClickSound.play( );

					// 右ボタンの場合，画像を回転して，表示位置を変更
					if ((evt.getModifiers( ) & InputEvent.BUTTON3_MASK) != 0){ 
						// 回転画像作成
						// 原画からピクセル情報を取り込む
						GetPixelsSetArray(Piece[p], 0, 0,
										  PieceWidth, PieceHeight, Pixels1);

						// 右回転になるようにデータを入れ替える
						// それぞれの画像作成用の配列を確保しておく必要あり
						for (int tate = 0; tate < PieceHeight; tate++) {
							for (int yoko = 0; yoko < PieceWidth; yoko++) {
								Pixels2[200 * 200 * p
									 + PieceWidth * yoko + PieceWidth - tate - 1]
									= Pixels1[PieceWidth * tate + yoko];
							}
						}
						// 編集処理した配列のピクセル情報からイメージを作成
						Piece[p] = createImage(new MemoryImageSource(
							PieceWidth, PieceHeight, Pixels2, 200*200*p, PieceWidth));

						// 回転後の描画位置との距離
						int TempClickDistanceX = ClickDistanceX;
						ClickDistanceX = PieceHeight - ClickDistanceY;
						ClickDistanceY = TempClickDistanceX;

						// 回転後の描画位置
						PieceX[p] = mouseX - ClickDistanceX;
						PieceY[p] = mouseY - ClickDistanceY;
					}
					break;
				}
			}
		}
		repaint( );
	}
	// 画像イメージの各ピクセル情報を取得 -----------------------------------------------
	public void GetPixelsSetArray(Image img, int x, int y, int w, int h, int pixels[]) {
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
						// (画像イメージ，開始位置ｘ，ｙ，幅, 高さ，配列，格納位置，横幅)
        try  {
			pg.grabPixels( );						// ピクセル情報取込み
        }  catch  (InterruptedException  e)  {		// grabPixels( )に対する例外処理
            showStatus(" "+e);
        }
	}
	// ----------------------------------------------------------------------------------
	// 画像イメージimageの点(x, y)のカラー値取得
	public int GetPixels(Image image, int x, int y) {
		// 大きなサイズの画像を使うと，PixelGrabber，grabPixels( )の処理が遅くなる
		// サイズが1のグラフィック領域に対象ポイントを書き込み，その画像を処理する
		CheckGraphics.drawImage(image, -x, -y, this);

		int[ ] pixels = new int[1];
		PixelGrabber pg = new PixelGrabber(image, x, y, 1, 1, pixels, 0, 1);
        try  {
			pg.grabPixels( );
        }  catch  (InterruptedException  e)  {
        }
		return (pixels[0]);
	}
	public void mouseReleased(MouseEvent evt) {
		if (MouseDownSw == true) {
			MouseDownSw = false;					// マウスダウンスイッチをオフ
		}
	}
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
	// MouseMotionListenerインターフェースを実装 ----------------------------------------
    public void mouseDragged(MouseEvent evt) {
		if (MouseDownSw == true) {
			int mouseX = evt.getX( );
			int mouseY = evt.getY( );
			// 画像描画位置　＝　マウス位置 － クリック時の距離
			PieceX[ClickPieceNo] = mouseX - ClickDistanceX;
			PieceY[ClickPieceNo] = mouseY - ClickDistanceY;
			repaint( );
		}
	}
    public void mouseMoved(MouseEvent evt) { }
}
