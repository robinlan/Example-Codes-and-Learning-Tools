import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Font, MediaTracker
import java.awt.image.PixelGrabber;					// PixelGrabber
import java.awt.event.*;							// MouseMotionListener, MouseEvent

public class ImageInformation extends Applet implements MouseMotionListener {
	int AppletWidth, AppletHeight;					// アプレットのサイズ
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	int Number;										// データ数
	Image image[ ] = new Image[30];					// 画像イメージ
	int X[ ] = new int[30];							// 画像位置
	int Y[ ] = new int[30];
	int Width[ ] = new int[30];						// 画像のサイズ
	int Height[ ] = new int[30];
	String Text[ ] = new String[30];				// 表示テキスト
	int MouseX, MouseY;								// マウスの位置

	Image CheckImage;								// チェックイメージ領域
	Graphics CheckGraphics;							// チェックグラフィックス

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		AppletWidth = getSize( ).width;						// アプレットサイズ
		AppletHeight = getSize( ).height;

		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得

		WorkGraphics.setColor(new Color(255, 255, 255));	// 白色
		WorkGraphics.fillRect(0, 0, AppletWidth, AppletHeight);

		//各種パラメータ取得・画像データ入力・
		MediaTracker mt = new MediaTracker(this);			// 入力監視メディアトラッカー
		Number = Integer.parseInt(getParameter("number"));	// データ数
		for (int i = 0; i < Number; i++) {
			String temp = getParameter("data" + i);
			X[i] = Integer.parseInt(temp.substring(0, 3));	// 画像位置
			Y[i] = Integer.parseInt(temp.substring(4, 7));
			int p = temp.indexOf(",", 8);					// 8文字目以降の「，」の位置
			String imagefilename = temp.substring(8, p);
			image[i] = getImage(getCodeBase( ), imagefilename);
			mt.addImage(image[i], 0);
			Text[i] = temp.substring(p + 1);
		}
		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
		}
		for (int i = 0; i < Number; i++) {			// 画像を作業領域に描画
			Width[i] = image[i].getWidth(this);
			Height[i] = image[i].getHeight(this);
			WorkGraphics.drawImage(image[i], X[i], Y[i], this);	// イメージ描画
		}

		CheckImage = createImage(1, 1);				// ピクセルのカラー値取得作業領域
		CheckGraphics = CheckImage.getGraphics( );	// チェック作業グラフィック

		addMouseMotionListener(this);				// マウスモーションリスナー追加	
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画

		for (int i = 0; i < Number; i++) {
			if (X[i] < MouseX && MouseX < X[i] + Width[i] &&	// マウスが画像領域内
				Y[i] < MouseY && MouseY < Y[i] + Height[i]) {

				// ポイントのカラー情報
				int color = GetPixels(image[i], MouseX-X[i], MouseY-Y[i]);
				int alpha = (color >> 24) & 0xff; 	// 不透明度 0:透明～255：不透明

				String message = "x="+MouseX+" y="+MouseY+" Alpha="+alpha;
				showStatus(message);

				if (alpha == 0xff) {						// 不透明，画像がある場合
					int size = 12;							// 文字サイズ
					g.setColor(new Color(0, 200, 255));		// メッセージ枠
					g.fillRect(X[i], Y[i]+Height[i]+1, (Text[i].length( ))*size,size);
					g.setColor(new Color(0, 0, 0));
					g.setFont(new  Font("ＭＳ ゴシック",Font.PLAIN,size));	// フォント設定
					g.drawString(Text[i], X[i], Y[i]+Height[i]+size); 		// メッセージ描画
				}
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
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
	// MouseMotionListenerインターフェースを実装 ----------------------------------------
    public void mouseMoved(MouseEvent evt) {
        MouseX = evt.getX( );						// マウスの現在位置を保管
		MouseY = evt.getY( );
        repaint( );
    }
    public void mouseDragged(MouseEvent evt) { }
}
