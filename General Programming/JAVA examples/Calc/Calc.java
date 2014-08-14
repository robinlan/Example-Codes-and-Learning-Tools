import java.applet.*;								// Applet, AudioClip
import java.awt.*;									// Panel, Button, Label, Color, GridLayout, BorderLayout
import java.awt.event.*;							// ActionListener, ActionEvent

public class Calc extends Applet
				  implements ActionListener {		// ActionListenerインターフェース実装
	Label DisplayLabel;								// 表示ラベル
	String PreOpe;									// 前回の演算子
	StringBuffer InputBuffer; 						// 入力バッファ
	double PreValue, MemoryValue;					// 前回までの値，メモリ値
	int NewDigitInputSw;							// 新しい入力スイッチ
	AudioClip ClickSound;							// クリックサウンド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		String ButtonName[ ] = { " ",				// スペースはダミー
 								"C", "M+", "M-", "MR", 	// 名ボタン設定
								"7", "8", "9", "/",
								"4", "5", "6", "*",
								"1", "2", "3", "-",
								"0", ".", "=", "+" };
		Button CalcButton[ ] = new Button[21];		// ボタン配列生成
		ClickSound = getAudioClip(getCodeBase( ), "sound/click.au");	// クリック音
		setLayout(new BorderLayout( ));	   			// アプレットをボーダーレイアウト設定

		Panel DisplayPanel = new Panel( );			// 表示パネル設定
		DisplayLabel = new Label("", Label.RIGHT);	// 右揃え指定のラベル設定
		DisplayLabel.setBackground(Color.black);	// 背景色：黒
		DisplayLabel.setForeground(Color.green);	// 表示色：緑
		DisplayPanel.add(DisplayLabel);				// ラベルをパネルに追加
		Panel ButtonPanel = new Panel( );			// ボタンパネル作成
		ButtonPanel.setLayout(new GridLayout(5, 4));// 5行4列のグリッドレイアウト
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 4; j++) {
				int p = (i - 1) * 4 + j;
				CalcButton[p] = new Button(ButtonName[p]);
				ButtonPanel.add(CalcButton[p]);		// ボタンを生成，ボタンパネルに付加
				CalcButton[p].addActionListener(this);
			}
		}
		add("North", DisplayPanel);					// 表示パネルを北側に付加
		add("Center", ButtonPanel);					// ボタンパネルをセンターに付加
		InitialPro( );								// 計算初期化処理
	}
	// 計算初期化処理 -------------------------------------------------------------------
	void InitialPro( ) {
		PreValue = 0;								// 前回までの値
		PreOpe = "";								// 前回の演算子
		MemoryValue = 0;							// メモリ値
		InputBuffer = new StringBuffer( );			// 入力バッファ
		NewDigitInputSw = 0;						// 新しい入力スイッチ
		DisplayLabel.setText("                            0");
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// イベント処理
		ClickSound.play( );							// クリック音
		Button bt = (Button)evt.getSource( );
		String value = bt.getLabel( );
		if ("C".equals(value)) {					// クリアの場合
			InitialPro( );							// 計算初期化処理
		}
		else if ((".".equals(value)) || ("0".equals(value)) || 	// 数字キーの場合
				 ("1".equals(value)) || ("2".equals(value)) || 
				 ("3".equals(value)) || ("4".equals(value)) || 
				 ("5".equals(value)) || ("6".equals(value)) || 
				 ("7".equals(value)) || ("8".equals(value)) || 
				 ("9".equals(value)) ) {
			if (NewDigitInputSw == 0) {
				InputBuffer = new StringBuffer( );	// 入力バッファ生成
				InputBuffer.append(value);
				DisplayLabel.setText(value);
				NewDigitInputSw = 1;
			} else {
				InputBuffer.append(value);			// 入力値を追加
				DisplayLabel.setText(InputBuffer.toString( ));	// 文字列化
			}
		} else if (("+".equals(value)) || ("-".equals(value)) || 
				 ("*".equals(value)) || ("/".equals(value)) || 
				 ("=".equals(value)) ||
				 ("M+".equals(value)) || ("M-".equals(value)) ) {

			// ディスプレイラベルに表示されている文字列をdouble値に変換
			double Nowvalue =
					(Double.valueOf(DisplayLabel.getText( ))).doubleValue( );

			if ("+".equals(PreOpe))					// 前回の演算子が＋の場合
				PreValue = PreValue + Nowvalue;		// 前回の値に今回の値を加算
			else if ("-".equals(PreOpe))
				PreValue = PreValue - Nowvalue;
			else if ("*".equals(PreOpe))
				PreValue = PreValue * Nowvalue;
			else if ("/".equals(PreOpe))
				PreValue = PreValue / Nowvalue;
			else
				PreValue = Nowvalue;				// 現在の値を保管

			if ("M+".equals(value)) {
				MemoryValue = MemoryValue + PreValue;	// メモリに加算
				PreOpe = "";
			} else if ("M-".equals(value)) {
				MemoryValue = MemoryValue - PreValue;	// メモリから減算
				PreOpe = "";
			} else
				PreOpe = (String)value;				// 今回の演算子を記憶

			DisplayLabel.setText(""+PreValue);
			NewDigitInputSw = 0; 					// 新しい数字入力スイッチクリア
		} else if ("MR".equals(value)) {
			DisplayLabel.setText(""+MemoryValue);
			NewDigitInputSw = 0;					// 新しい数字入力スイッチクリア
		}
	}
}
