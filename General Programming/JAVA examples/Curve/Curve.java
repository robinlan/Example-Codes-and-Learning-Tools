import java.applet.*;						// Applet
import java.awt.*;							// Graphics, Color, Button, Label, TextField, Canvas
import java.awt.event.*;					// ActionListener, ActionEvent

public class Curve extends Applet implements ActionListener {
	CanvasMake canvas;								// グラフキャンバス
	TextField ValueField[ ] = new TextField[3];		// 各定数の入力域
 	Label label[ ] = new Label[3];					// 数式表示ラベル
	double Value[ ] = new double[3];				// 各定数
	Button DispButton;								// 表示ボタン

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		// ラベル生成
		label[0] = new Label("F(X)=");
		label[1] = new Label("X2 + ");
		label[2] = new Label("X + ");
		for (int i = 0; i < 3; i++) {
			add(label[i]);
			ValueField[i] = new TextField( );
			add(ValueField[i]);
		}

		DispButton = new Button("Disp");
		add(DispButton);
		DispButton.addActionListener(this);			// ボタンにリスナーセット

		canvas = new CanvasMake( );					// キャンバス生成
		canvas.setSize(200, 200);					// キャンバスサイズ再設定
		add(canvas);								// キャンバスをアプレットに付加
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// イベント処理
		Button button = (Button)evt.getSource( );
		if (button == DispButton) {
			for (int i = 0; i < 3; i++) {
				if (ValueField[i].getText( ).equals(""))		// 何も入力されていない場合
					Value[i] = 0;
				else
					Value[i] = new Double(ValueField[i].getText( )).floatValue( );
			}
			canvas.GraphDisp(Value);				// グラフ表示
		}
	}
}

// キャンバス作成クラス =================================================================
class CanvasMake extends Canvas{
	double Value[ ] = new double[3];
	int sw;
	// コンストラクタ -------------------------------------------------------------------
	public CanvasMake ( ) {
		setBackground(Color.lightGray);				// キャンバスの背景色
		sw = 0;
	}
	// グラフ描画 -----------------------------------------------------------------------
	void GraphDisp(double v[ ]) {					// 各定数
		// 引数をグローバル変数にセット
		for (int i = 0; i < 3; i++)
			Value[i] = v[i];
		sw = 1;										// 描画スイッチON
		repaint( );
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.setColor(Color.white);
		for (int p = 0; p < 200; p += 10) {			// 目盛り
			g.drawLine(0, p, 200, p);				// 横ライン
			g.drawLine(p, 0, p, 200);				// 縦ライン
		}

		g.setColor(Color.black);
		g.drawLine(0, 100, 200, 100);				// X軸
		g.drawLine(100, 0, 100, 200);				// Y軸

		if (sw == 0)
			return;

		g.setColor(Color.red);
		for (double x = -10.0; x <= 10.0; x += 0.01) {
			double f = Value[0]*x*x + Value[1]*x + Value[2];	// 関数の値計算
			int xp = (int)(x * 10)		+ 100;		// 座標(100,100)を原点
			int yp = (int)(f * 10)*(-1) + 100;		// ｙの正方向を上，xyそれぞれ10倍
			g.drawLine(xp, yp, xp, yp);
		}
	}
}
