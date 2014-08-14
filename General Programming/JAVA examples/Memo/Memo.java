import java.awt.*;					// Graphics, Frame, TextArea, Color, FileDialog, Menu etc
import java.awt.event.*;			// ActionListener, ActionEvent
import java.io.*;					// FileReader, BufferedReader, FileWriter, PrintWriter, IOException
import java.util.*;					// StringTokenizer

// メモ帳クラス -------------------------------------------------------------------------
class Memo {
	public static void main(String args[ ]) {
		MemoFrame jed = new MemoFrame("Memo", 80, 50);
		jed.init( );
	}
}

// メモ帳フレームクラス -----------------------------------------------------------------
class MemoFrame extends Frame implements ActionListener, WindowListener {
								  // ActionListenerと WindowListenerインターフェース実装
	TextArea textarea;
	int width=500, height=300;						// テキストエリアのサイズ設定
	// コンストラクタ -------------------------------------------------------------------
	MemoFrame(String title, int Cols, int Rows) {
		setTitle(title);							// フレームのタイトル
		textarea = new TextArea(Cols, Rows);		// テキストエリアのサイズ
		textarea.setBackground(new Color(180, 220, 255));	// 背景色設定
		add(textarea);								// フレームにテキストエリヤ追加
		MenuBar menubar = new MenuBar( );			// メニューバー設定
		Menu menu_file = new Menu("File");			// メニューフィールド設定
		MenuItem menuItem_load = new MenuItem("Load");	// メニュー項目設定
		MenuItem menuItem_save = new MenuItem("Save");
		MenuItem menuItem_print = new MenuItem("Print");
		MenuItem menuItem_quit = new MenuItem("Quit");
		menuItem_load.addActionListener(this);		// メニュー項目にリスナー付加
		menuItem_save.addActionListener(this);
		menuItem_print.addActionListener(this);
		menuItem_quit.addActionListener(this);
		menu_file.add(menuItem_load);				// メニューフィールドにメニュー項目貼付け
		menu_file.add(menuItem_save);
		menu_file.add(menuItem_print);
		menu_file.add(menuItem_quit);
		menubar.add(menu_file);						// メニューフィールドをメニューバーに貼付
		setMenuBar(menubar);						// メニューバーをフレームに貼付
	}
	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
		show( );									// フレーム表示
		setSize(width, height);						// フレームサイズ設定
		addWindowListener(this);					// ウィンドウリスナー追加
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
   	public void actionPerformed(ActionEvent evt) {
 		MenuItem mi = (MenuItem)evt.getSource( );
 		String label = mi.getLabel( );
 		if (label.equals("Load")) {					// ロード
			// ファイルダイアログをオープン
			FileDialog dialog = new FileDialog(this, "OPEN", FileDialog.LOAD);
			dialog.show( );							// ダイアログ表示
			String filename = dialog.getFile( );	// ファイル名取得
			if (filename == null)
				return;
			filename = dialog.getDirectory( ) + 
 			System.getProperty("file.separator") + filename;
			try {
				textarea.setText("");				// テキストエリヤをクリア
				// 指定したファイル名でFileReaderのオブジェクト作成
				FileReader fr = new FileReader(filename);
				// 行単位に読込むためにBufferedReaderのオブジェクト作成
				BufferedReader br = new BufferedReader(fr);
				String buf;
				while ((buf = br.readLine( )) != null) {	// 行単位に読み込む
					// テキストエリヤに1行のデータと改行を追加挿入
					textarea.append(buf + System.getProperty("line.separator"));
				}
				br.close( );
			} catch (IOException e) {
			}
		}
		else if (label.equals("Save")) {			// セーブ
			// ファイルダイアログをオープン
			FileDialog dialog = new FileDialog(this, "OPEN", FileDialog.LOAD);
			dialog.show( );							// ファイルダイアログを表示
			String filename = dialog.getFile( );	// ファイル名取得
			if (filename == null)
				return;
			filename = dialog.getDirectory( ) +
						System.getProperty("file.separator") + filename;
			try {
				// 指定したファイル名でFileWriterのオブジェクト作成
				FileWriter fw = new FileWriter(filename);
				// 内部unicodeを自動コンバートするストリーム作成
				PrintWriter pw = new PrintWriter(fw);
				pw.print(textarea.getText( ));
				pw.close( );
			} catch (IOException e) {
			}
		}
		else if (label.equals("Print")) {
			// PrintJobのオブジェクト作成
			PrintJob printJob = 
 				getToolkit( ).getPrintJob(this, "Printing Text", null);
			if (printJob == null)
				return;
			Graphics printGraphics = null;
			setSize(width, height);					// フレームサイズ設定
			textarea.setSize(width, height);		// 印刷サイズ設定
			// テキストエリヤのテキスト取得
			String sentence = textarea.getText( );
			String delimiter = "\n";				// 区切り文字取得
			StringTokenizer st = new StringTokenizer(sentence, delimiter);
			int linecount = 0;						// 行カウント０クリア
			while  (st.hasMoreTokens( ))  {
				if (linecount % 50 == 0) {
					if (linecount > 0) {
						textarea.printAll(printGraphics);	// 印刷する
				       	printGraphics.dispose( );	// 用紙を排出する
					}
					textarea.setText("");			// テキストエリヤクリア
 					// PrintJobのグラフィックコンテキスト取得
					printGraphics =  printJob.getGraphics( );	
				}
				String s = st.nextToken( );			// 1行取り出し
				linecount++;						// 行をカウントアップ
				String number = "000" + String.valueOf(linecount);
 				// 行番号を3桁に編集
				String linenumber = number.substring(number.length( ) - 3);
 				// 行番号，1行文のデータと区切り
				textarea.append(linenumber + "|" + s + delimiter);
				}
			textarea.printAll(printGraphics);		// 印刷する
		   	printGraphics.dispose( );				// 用紙を排出する
	  		printJob.end( );						// 印刷終了
			textarea.setText(sentence);				// 文章を元に戻す
		}
		else if (label.equals("Quit")) {
			dispose( );								// フレームが使用した資源解放
			System.exit(1);							// プログラム終了
		}
	}
 	// WindowListenerインターフェースのメソッドを定義 -----------------------------------
	public void windowOpened(WindowEvent evt) { }
	public void windowClosing(WindowEvent evt) {
		dispose( );  								// フレームの廃棄
	}
	public void windowClosed(WindowEvent evt) {
		System.exit(0);								// プログラム終了
	}
	public void windowIconified(WindowEvent evt) { }
	public void windowDeiconified(WindowEvent evt) { }
	public void windowActivated(WindowEvent evt) { }
	public void windowDeactivated(WindowEvent evt) { }
}
