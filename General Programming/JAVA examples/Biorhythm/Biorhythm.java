import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Button, TextField
import java.awt.event.*;							// ActionListener, ActionEvent
import java.util.*;									// Calendar, TimeZone

public class Biorhythm extends Applet
					implements ActionListener { 	// ActionListenerインターフェース実装
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int Days[ ] = { 0, 31, 28, 31, 30,  31, 30, 31, 31,  30, 31, 30, 31};	// 各月の日数
	int NowYear, NowMonth, NowDay;					// 現在の年月日
	int BirthYear, BirthMonth, BirthDay;			// 生まれた年月日
	int TotalDays;									// 総日数
	Button DisplayButton;							// 表示ボタン
	TextField BirthYearTextfield, BirthMonthTextfield, BirthDayTextfield;

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		setLayout(null);							// レイアウトを自由設定
		WorkImage = createImage(400, 240);			// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );	// 作業用グラフィックス取得
		WorkGraphics.setColor(new Color(0, 100, 0));// バイオリズムのベース画面作成
		WorkGraphics.fillRect(0, 0, 400, 20);
		WorkGraphics.setColor(new Color(0, 0, 0));
		WorkGraphics.fillRect(0, 20, 400, 201);
		WorkGraphics.setColor(new Color(0, 100, 0));
		WorkGraphics.fillRect(0, 221, 400, 20);

		BirthYearTextfield = new TextField(5);			// 年入力用テキストフィールド
		BirthMonthTextfield = new TextField(5);			// 月入力用テキストフィールド
		BirthDayTextfield = new TextField(5);			// 日入力用テキストフィールド
		BirthYearTextfield.setBackground(Color.yellow);	// テキストフィールドの背景色
		BirthMonthTextfield.setBackground(Color.yellow);
		BirthDayTextfield.setBackground(Color.yellow);
		add(BirthYearTextfield);						// テキストフィールドをアプレットに付加
		add(BirthMonthTextfield);
		add(BirthDayTextfield);

		BirthYearTextfield.setBounds(130, 3, 40, 15);	// テキストフィールド配置・サイズ設定
		BirthMonthTextfield.setBounds(240, 3, 40, 15);
		BirthDayTextfield.setBounds(325, 3, 40, 15);

		WorkGraphics.setColor(new Color(255, 255, 255));// 各タイトル描画
		WorkGraphics.drawString("Biorhythm", 3, 15);
		WorkGraphics.drawString("year", 100, 15);
		WorkGraphics.drawString("month", 200, 15);
		WorkGraphics.drawString("day", 300, 15);
		WorkGraphics.setColor(new Color(0, 255, 255));
		WorkGraphics.drawString("Physical", 10, 235);
		WorkGraphics.setColor(new Color(255, 255, 0));
		WorkGraphics.drawString("Sentiment", 90, 235);
		WorkGraphics.setColor(new Color(255, 255, 255));
		WorkGraphics.drawString("Intelligence", 170, 235);

		DisplayButton = new Button("Display");		// 表示ボタン生成
		add(DisplayButton);							// アプレットにボタンを付加
		DisplayButton.addActionListener(this);		// ボタンにリスナー追加
		DisplayButton.setBounds(340, 223, 50, 15);	// ボタンのサイズ・位置の再設定

		// 本日の日付
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		NowYear = date.get(Calendar.YEAR);
		NowMonth = date.get(Calendar.MONTH) + 1;
		NowDay   = date.get(Calendar.DATE);
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// アクションイベント処理
		Button button = (Button)evt.getSource( );

		if (button == DisplayButton){				// DisplayButtonが押された場合
			String str = BirthYearTextfield.getText( );	// 年のテキストフィールドの文字列
			int w = new Integer(str).intValue( );	// 文字列strを整数化
			BirthYear = w;							// 生まれた年
			str = BirthMonthTextfield.getText( );	// 月のテキストフィールドの文字列
			w = new Integer(str).intValue( );		// 文字列strを整数化
			BirthMonth = w;							// 生まれた月
			str = BirthDayTextfield.getText( );		// 日のテキストフィールドの文字列
			w = new Integer(str).intValue( );		// 文字列strを整数化
			BirthDay = w;							// 生まれた日

			TotalDaysSyori( );						// 生まれてから今日までの日数
			ScreenClear( );							// バイオリズム画面クリア
			DisplayBiorhythm( );					// バイオリズム表示

			repaint( );								// 再描画
		}
	}

	// 生まれてからの日数計算 -----------------------------------------------------------
	public void TotalDaysSyori( ) {					// 生まれてから今日までの日数
		// 生まれた年の1/1から誕生日までの日数
		int TotalDays1 = 0;
		Days[2] = 28 + LeapCheck(BirthYear);		// 生まれた年の2月の日数
		for (int n = 1; n < BirthMonth; n++)
			TotalDays1 += Days[n];					// 生まれた月の前月までの日数を加算
		TotalDays1 += BirthDay;						// 生まれた日を加算

		// 生まれた年の1/1から今日までの日数
		int TotalDays2 = 0;
		for (int n = BirthYear; n < NowYear; n++) {	// 生まれた年の前年までの日数を加算
			if (LeapCheck(n) == 1)					// n年が閏年の場合
				 TotalDays2 += 366;
			else
				 TotalDays2 += 365;
		}
		Days[2] = 28 + LeapCheck(NowYear);			// 今年の2月の日数
		for (int n = 1; n < NowMonth; n++)			// 生まれた月の前月までの日数を加算
			TotalDays2 += Days[n];
		TotalDays2 += NowDay;						// 今日の日を加算

		// 生まれてから今日までの日数
		TotalDays = TotalDays2 - TotalDays1;
	}
	// 閏年チェック ---------------------------------------------------------------------
	int LeapCheck(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			return 1;
		else
			return 0;
	}
	// バイオリズム画面クリア -----------------------------------------------------------
	public void ScreenClear( ) {
		WorkGraphics.setColor(new Color(0, 0, 0));
		WorkGraphics.fillRect(0, 20, 400, 201);

		// １日単位にグレーの縦線
		WorkGraphics.setColor(new Color(50, 50, 50));
		for (int x = 10; x <= 400; x += 10)
			WorkGraphics.drawLine(x, 20, x, 220);

		// ０レベルの水平線と本日を表わす縦線
		WorkGraphics.setColor(new Color(255, 255, 255));
		WorkGraphics.drawLine(200, 20, 200, 220);
		WorkGraphics.drawLine(0, 120, 400, 120);
		WorkGraphics.drawString("+", 202, 30);
		WorkGraphics.drawString("-", 202, 220);
	}
	// 指定範囲描画処理 -----------------------------------------------------------------
	public void DisplayBiorhythm( ) {				// バイオリズム表示
		int startday = TotalDays - 20;				// 20日前から開始

		for (int x = 1; x <= 400; x++) {			// 10pixelsで1日分
			// 身体（Physical）
			int y = (int)(120 - 100 * Math.sin(6.28 * (startday % 23 + x / 10.0) / 23));
			WorkGraphics.setColor(new Color(0, 255, 255));
			WorkGraphics.drawLine(x, y, x, y);
			// 感情(Sentiment）
			y = (int)(120 - 100 * Math.sin(6.28 * (startday % 28 + x / 10.0) / 28));
			WorkGraphics.setColor(new Color(255, 255, 0));
			WorkGraphics.drawLine(x, y, x, y);
			// 知性（Intelligence）
			y = (int)(120 - 100 * Math.sin(6.28 * (startday % 33 + x / 10.0) / 33));
			WorkGraphics.setColor(new Color(255, 255, 255));
			WorkGraphics.drawLine(x, y, x, y);
		}
		repaint( );
	}
}
