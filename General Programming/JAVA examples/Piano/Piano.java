import java.applet.*;								// Applet, AudioClip
import java.awt.*;									// Graphics, Image, Color
import java.awt.event.*;							// MouseListener, MouseEvent

public class Piano extends Applet implements MouseListener {
    String KeyName[ ] = {"C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5", 	//各キーの名前
						 "C4S", "D4S", "F4S", "G4S", "A4S"};			 	//音ファイル名
	int Point[ ][ ] = new int[13][4];				// 鍵盤の位置
	int Keyboard[ ] = new int[13];					// 鍵盤の状態  0:押していない　1:押す
    AudioClip Sound[ ] = new AudioClip[13];			// 音
	int AppletWidth, AppletHeight;					// アプレットサイズ
	int PreHitKey = 0;								// 前回押したした鍵盤
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス

	// 初期化処理 -----------------------------------------------------------------------
    public void init( ) {
		for (int i = 0; i < 13; i++)				// 音データ入力
	        Sound[i] = getAudioClip(getCodeBase( ), "sound/"+KeyName[i]+".au");
		AppletWidth = getSize( ).width;				// アプレットの幅と高さ
		AppletHeight = getSize( ).height;
		WorkImage = createImage(AppletWidth, AppletHeight);	// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );			// 作業用グラフィックス取得
		addMouseListener(this);								// マウスリスナ追加

		// 鍵盤の座標，幅，高さ
		// C4
		Point[0][0] = (int)(AppletWidth *  0 / 40.0);  Point[0][1] = 0;
		Point[0][2] = (int)(AppletWidth *  5 / 40.0);  Point[0][3] = AppletHeight;
		// D4
		Point[1][0] = (int)(AppletWidth *  5 / 40.0);  Point[1][1] = 0;
		Point[1][2] = (int)(AppletWidth *  5 / 40.0);  Point[1][3] = AppletHeight;
		// E4
		Point[2][0] = (int)(AppletWidth * 10 / 40.0);  Point[2][1] = 0;
		Point[2][2] = (int)(AppletWidth *  5 / 40.0);  Point[2][3] = AppletHeight;
		// F4
		Point[3][0] = (int)(AppletWidth * 15 / 40.0);  Point[3][1] = 0;
		Point[3][2] = (int)(AppletWidth *  5 / 40.0);  Point[3][3] = AppletHeight;
		// G4
		Point[4][0] = (int)(AppletWidth * 20 / 40.0);  Point[4][1] = 0;
		Point[4][2] = (int)(AppletWidth *  5 / 40.0);  Point[4][3] = AppletHeight;
		// A4
		Point[5][0] = (int)(AppletWidth * 25 / 40.0);  Point[5][1] = 0;
		Point[5][2] = (int)(AppletWidth *  5 / 40.0);  Point[5][3] = AppletHeight;
		// B4
		Point[6][0] = (int)(AppletWidth * 30 / 40.0);  Point[6][1] = 0;
		Point[6][2] = (int)(AppletWidth *  5 / 40.0);  Point[6][3] = AppletHeight;
		// C5
		Point[7][0] = (int)(AppletWidth * 35 / 40.0);  Point[7][1] = 0;
		Point[7][2] = (int)(AppletWidth *  5 / 40.0);  Point[7][3] = AppletHeight;
		// C4S
		Point[8][0] = (int)(AppletWidth *  2 / 40.0);  Point[8][1] = 0;
		Point[8][2] = (int)(AppletWidth *  4 / 40.0);
		Point[8][3] = (int)(AppletHeight * 0.6);
		// D4S
		Point[9][0] = (int)(AppletWidth *  9 / 40.0);  Point[9][1] = 0;
		Point[9][2] = (int)(AppletWidth *  4 / 40.0);
		Point[9][3] = (int)(AppletHeight * 0.6);
		// F4S
		Point[10][0] = (int)(AppletWidth * 17 / 40.0);  Point[10][1] = 0;
		Point[10][2] = (int)(AppletWidth *  4 / 40.0);
		Point[10][3] = (int)(AppletHeight * 0.6);
		// G4S
		Point[11][0] = (int)(AppletWidth * 23 / 40.0);  Point[11][1] = 0;
		Point[11][2] = (int)(AppletWidth *  4 / 40.0);
		Point[11][3] = (int)(AppletHeight * 0.6);
		// A4S
		Point[12][0] = (int)(AppletWidth * 29 / 40.0);  Point[12][1] = 0;
		Point[12][2] = (int)(AppletWidth *  4 / 40.0);
		Point[12][3] = (int)(AppletHeight * 0.6);
	}
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g) {
		hitdraw( );									// 鍵盤描画
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
    }
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 打鍵描画 -------------------------------------------------------------------------
	void hitdraw( ) {
		for (int i = 0; i < 13; i++) {				// 白鍵盤から描く
			if (Keyboard[i] == 1)					// 鍵盤を押した場合
				WorkGraphics.setColor(new Color(255, 255, 153));
			else {
				if(i >= 8)
					WorkGraphics.setColor(Color.black);	// 黒鍵盤の場合
				else
					WorkGraphics.setColor(Color.white);	// 白鍵盤の場合
			}
			WorkGraphics.fillRect(Point[i][0], Point[i][1], Point[i][2], Point[i][3]);
			WorkGraphics.setColor(Color.black);
			WorkGraphics.drawRect(Point[i][0], Point[i][1],
								  Point[i][2]-1, Point[i][3]-1);
		}
	}
	// MouseListenerインターフェースを実装-----------------------------------------------
 	// ピアノのように，1回のマウスダウンに対して，１回鳴らす
	public void mousePressed(MouseEvent evt) {
		int mouseX = evt.getX( );					// クリックしたマウスの位置
		int mouseY = evt.getY( );
		for (int i = 12; i >= 0; i--) {				// 黒い鍵盤からチェック
			if (mouseX >= Point[i][0] && mouseX <= Point[i][0] + Point[i][2]
			 && mouseY >= Point[i][1] && mouseY <= Point[i][1] + Point[i][3]) {
				Keyboard[i] = 1;					// 押した鍵盤に１をセット
				Sound[i].play( );					// １回鳴らす
				PreHitKey = i;						// 今回の位置を前回の位置として記憶
				break;
			}
		}
        repaint( );									// 再描画
    }
	public void mouseReleased(MouseEvent evt) {
		Keyboard[PreHitKey] = 0;					// 前回押した鍵盤をクリア
        repaint( );									// 再描画
	}
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
}
