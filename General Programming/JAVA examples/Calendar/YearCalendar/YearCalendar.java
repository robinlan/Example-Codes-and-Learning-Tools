import java.applet.*;								// Applet
import java.awt.*;									// Graphics, Image, Color, Font, Button, TextField
import java.awt.event.*;							// ActionListener, ActionEvent
import java.util.*;									// Calendar, TimeZone

public class YearCalendar extends Applet implements ActionListener {
	int Days[ ] = { 0, 31, 28, 31, 30, 31, 30, 31, 31,	30, 31, 30, 31};	// 各月の日数
	int Year;
	TextField YearTextfield;						// 年テキストフィールド
	Button DisplayButton, LeftButton, RightButton;	// 各種ボタン
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int Holiday[ ] = {								// 休日設定
		 101, 		// 元旦
		 100, 		// 成人の日	------- 未定
		 211, 		// 建国記念日
		 300, 		// 春分の日	------- 未定
		 429, 		// みどりの日
		 503, 		// 憲法記念日
		 504, 		// 国民の休日
		 505, 		// 子供の日
		 720, 		// 海の日
		 915, 		// 敬老の日
		 900, 		// 秋分の日	------- 未定
		1000, 		// 体育の日	------- 未定
		1103, 		// 文化の日
		1123, 		// 勤労感謝の日
		1223  		// 天皇誕生日
		};

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		WorkImage = createImage(800, 500);			// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );	// 作業用グラフィックス取得
		Font font = new Font("Courier", Font.PLAIN, 12);	// フォント設定
		WorkGraphics.setFont(font);							//フォント設定

		setLayout(null);							// 自由レイアウト設定

		WorkGraphics.setColor(new Color(0, 100, 0));// ヘッダー作成
		WorkGraphics.fillRect(0, 0, 800, 20);
		WorkGraphics.setColor(new Color(255, 255, 255));
		WorkGraphics.drawString("Calendar", 40, 15);

		WorkGraphics.setColor(Color.white);			// カレンダー枠
		WorkGraphics.fillRect(0, 20, 800, 480);

		YearTextfield = new TextField(5);			// 年テキストフィールド
		YearTextfield.setBackground(Color.yellow);
		add(YearTextfield);
		YearTextfield.setBounds(370, 3, 35, 15);

		DisplayButton = new Button("Display");		// 表示ボタン
		add(DisplayButton);							// ボタンをアプレットに追加
		DisplayButton.setBounds(430, 3, 50, 15);	// ボタンの配置と大きさ設定
		DisplayButton.addActionListener(this);		// ボタンにリスナー追加

		LeftButton = new Button("<<");				// 前年表示ボタン
		add(LeftButton);
		LeftButton.setBounds(5, 3, 30, 15);			// ボタンの配置と大きさ設定
		LeftButton.addActionListener(this);			// ボタンにリスナー追加
		RightButton = new Button(">>");				// 来年表示ボタン
		add(RightButton);
		RightButton.setBounds(765, 3, 30, 15);		// ボタンの配置と大きさ設定
		RightButton.addActionListener(this);		// ボタンにリスナー追加

		Calendar date = Calendar.getInstance( );
		Year = date.get(Calendar.YEAR);				// 現在の年を取得
		YearTextfield.setText(String.valueOf(Year));// テキストフィールドに年設定
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		LeapYearCheck(Year);						// 閏年処理
		HolidaySyori( );							// 休日処理
		Display( );									// カレンダー描画処理
		g.drawImage(WorkImage, 0, 0, this);
	}
	// 描画更新処理 ---------------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 1年描画処理 ----------------------------------------------------------------------
	void Display( ) {
		WorkGraphics.setColor(Color.white);
		WorkGraphics.fillRect(0, 20, 800, 480);

		for (int i = 0; i < 3; i++)					// 縦3行
			for (int j = 0; j < 4; j++)				// 横4列
				MakeCalendar(Year, i * 4 + j + 1, i, j);	// 各月のカレンダー描画
		repaint( );
	}
	// 閏年処理 -------------------------------------------------------------------------
	void LeapYearCheck(int Year) {					// 閏年判定
		if (Year % 4 == 0 && Year % 100 != 0 || Year  % 400 == 0)
			Days[2] = 29;							// ２月の日数処理
		else
			Days[2] = 28;
	}
	// 休日処理 -------------------------------------------------------------------------
	void HolidaySyori( ) {
		// 成人の日　1月の第2月曜
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		date.set(Year, 1 - 1, 1);					// 1月1日にセット
		int col = date.get(Calendar.DAY_OF_WEEK);	// 曜日計算 1:日，2:月，3:火，・・7:土
		int count = 0;
		for (int c = 1; c <= 30; c++) {
			if (col == 2) {							// 月曜日の場合
				count++;
				if (count == 2) {					// 第2月曜日
					Holiday[1] = 1 * 100 + c;
					break;
				}
			}
			col++;									// 次の曜日にセット
			if (col > 7)							// 土曜日の次は
				col = 1;							// 日曜日
		}
		// 体育の日　10月の第2月曜
		date.set(Year, 10 - 1, 1);					// 10月1日にセット
		col = date.get(Calendar.DAY_OF_WEEK) % 7;	// 曜日計算
		count = 0;
		for (int c = 1; c <= 30; c++) {
			if (col == 2) {							// 月曜日の場合
				count++;
				if (count == 2) {					// 第2月曜日
					Holiday[11] = 10 * 100 + c;
					break;
				}
			}
			col++;									// 次の曜日にセット
			if (col > 7)							// 土曜日の次は
				col = 1;							// 日曜日
		}

		// 春分の日   03/??
		Holiday[3] = 3*100 + (int)(20.8431+0.242194*(Year-1980)-(int)((Year-1980)/4)); 

		// 秋分の日   09/??
		Holiday[10] = 9*100 + (int)(23.2488+0.242194*(Year-1980)-(int)((Year-1980)/4));

	}
	// 指定年・月のカレンダー作成 -------------------------------------------------------
	void MakeCalendar(int year, int month, int y, int x) {
        String weeks[ ] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		x = x * 200 + 24;							// 描画位置設定
		y = y * 160 + 30;

		WorkGraphics.setColor(Color.black);			// 各月のヘッダー作成
		WorkGraphics.drawString("" + month, x + 72, y);
		WorkGraphics.setColor(Color.red);
		WorkGraphics.drawString(weeks[0], x, y+15);
		WorkGraphics.setColor(Color.black);
		for (int i = 1; i <= 5; i++)
			WorkGraphics.drawString(weeks[i], x + i * 24, y + 15);
		WorkGraphics.setColor(Color.blue);
		WorkGraphics.drawString(weeks[6], x + 6 * 24, y + 15);
		WorkGraphics.setColor(Color.black);
		WorkGraphics.drawLine(x, y + 18, x + 7 * 24 - 6, y + 18);

		// 各月の最初の曜日計算
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		date.set(year, month - 1, 1);				// Year/Month/1にセット
		int w = date.get(Calendar.DAY_OF_WEEK);		// 曜日計算 1:日，2:月，3:火，・・7:土

		y = y + 35;									// 縦方向の表示位置設定
		int hurikae = 0;							// 振替クリア
		for (int d = 1; d <= Days[month]; d++) {
			// 曜日でカラー設定
			if (w == 1 || hurikae == 1)				// 日曜日または振替休日
				WorkGraphics.setColor(Color.red);
			else if (w == 7)						// 土曜日
				WorkGraphics.setColor(Color.blue);
			else									// 月曜日から金曜日
				WorkGraphics.setColor(Color.black);

			// 祝日　＆　振替休日
			hurikae = 0;								// 振替クリア
			for (int h = 0; h < Holiday.length; h++) {	// 祝日チェック
				if (month * 100 + d == Holiday[h]) {	// 祝日の場合
					WorkGraphics.setColor(Color.red);
					if (w == 1)							// 日曜の場合
						hurikae = 1;					// 振替セット
					break;
				}
			}

			if (d < 10)								// 表示日数が１桁
				WorkGraphics.drawString("" + d, x + w * 24 - 15, y);	// 表示位置設定
			else									// 表示日数が２桁
				WorkGraphics.drawString("" + d, x + w * 24 - 21, y);

			w++;
			if (w > 7) {							// 土曜日の次の場合
				w = 1;								// 日曜日に設定
				y += 14;
			}
		}
	}
	// ActionListenerインターフェースのメソッド定義 -------------------------------------
 	public void actionPerformed(ActionEvent evt) {
		Button button = (Button)evt.getSource( );
		if (button == DisplayButton) {				// 指定年描画
			String str = YearTextfield.getText( );	// テキストフィールドの値取得
			Year = new Integer(str).intValue( );	// 文字を整数化
		}
		else if (button == LeftButton)				// 前年描画
			Year--;
		else if (button == RightButton)				// 来年描画
			Year++;
		YearTextfield.setText(String.valueOf(Year));// テキストフィールドに値設定
		repaint( );									// 再描画
	}
}
