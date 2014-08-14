import java.net.*; 	// ServerSocket, Socket
import java.io.*;	// InputStream, BufferedReader, InputStreamReader,
					// OutputStream, PrintStream, IOException
import java.sql.*;	// DriverManager ,Connection, Statement, ResultSet

public class ScheduleServer {

	// メイン処理 -----------------------------------------------------------------------
	public static void main(String argv[ ] ) {
		String GetMessage = null;					// 受信メッセージ
		int port = 8003;							// 送受信のポート番号
		Socket socket = null;						// 送受信用ソケット
		String order = null;						// 命令 Select Delete Insert Update
		String date = null;							// 日付
		String hour = null;							// 時間
		String content = null;						// 内容

		ScheduleServer obj = new ScheduleServer( );	// 自身のクラスのオブジェクト作成

		try {
			// 指定ポートでサーバソケット作成
			ServerSocket serversocket = new ServerSocket(port);
			System.out.println("ServerSocket=" + serversocket);

			while (true) {
				// accept( )でクライアントからの接続を待ち，接続があればsocketを作成
				socket = serversocket.accept( );

				// ソケットから入力ストリームを作成
				InputStream inputstream = socket.getInputStream( );
				// 入力ストリームから行単位に入力できるバッファドリーダを作成
				BufferedReader netinput
 					= new BufferedReader(new InputStreamReader(inputstream));

				// ソケットから出力ストリームを作成
				OutputStream outputstream = socket.getOutputStream( );
				// 出力ストリームからデータ出力ストリーム作成
				PrintStream netoutput = new PrintStream(outputstream);

				// クライアントから命令を入力
				try {
					GetMessage = netinput.readLine( );	// 1行分の内容を受信
				} catch (IOException e) {				// 入出力に対する例外処理
					System.out.println("read error");
				}

				// メッセージを分解 -----------------------------------------------------
				/* 受信メッセージ例
				Select#1999/11/25#
				Insert#1999/11/25#10#Meeting#
				Update#1999/11/25#14#Official trip#
				Delete#1999/11/25#10#
				*/
				int p1, p2;
				order = GetMessage.substring(0, 6); 		// データベースで処理する内容
				p1 = GetMessage.indexOf("#", 0);			// 最初の＃の位置
				p2 = GetMessage.indexOf("#", p1+1);			// 次の＃の位置
				date = GetMessage.substring(p1+1, p2);		// p1+1～p2の前までの文字列
															// 日付
				if (order.compareTo("Select") != 0) {		// Select以外
					p1 = p2;
					p2 = GetMessage.indexOf("#", p1+1);		// 次の＃の位置
					hour = GetMessage.substring(p1+1, p2);	// p1+1～p2の前までの文字列
															// 時間
					if (order.compareTo("Delete") != 0) {	// Delete以外
						p1 = p2;
						p2 = GetMessage.indexOf("#", p1+1);	// 次の＃の位置
						content = GetMessage.substring(p1+1, p2);	// 内容
					}
				}
				// クライアントからの要求をサーバの画面に表示
				System.out.println("Order=" + order
							+ " Date=" + date+" Hour=" + hour + " Content=" + content);

				// 各処理 ---------------------------------------------------------------
				if (order.compareTo("Insert") == 0) {		// orderがInsertの場合
					obj.Insert(date, hour, content);
				}
				else if (order.compareTo("Update") == 0) {	// orderがUpdateの場合
					obj.Update(date, hour, content);
				}
				else if (order.compareTo("Delete") == 0) {	// orderがDeleteの場合
					obj.Delete(date, hour);
				}
				String SendMessage = obj.Select(date);		// Selectおよびその他
				netoutput.println(SendMessage);		// メッセージをネット経由で出力
				netoutput.flush( );					// バッファ内データを強制的に出力
				socket.close( );					// ソケット閉じる
			}
		} catch(Exception e) {						// 各種例外処理
			System.out.println(e);
		}
	}
	// 検索・選択 -----------------------------------------------------------------------
	// 指定データをテーブルから検索して，SendMessageにセット
	String Select(String date) {
		int DATAMAX = 18;							// 6時から23時までの18個
		String contents[ ] = new String[DATAMAX];	// 各時刻の内容
		for (int i = 0; i < DATAMAX; i++) 			// 内容初期化
			contents[i] = " ";

		try {
			// ODBC-JDBC Bridge ドライバロード
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// ODBC DSNを介してデータベースに接続
			String url = "jdbc:odbc:scheduleDB";
			String user = "guest";
			String password = "007";
			Connection con = DriverManager.getConnection(url, user, password);
			// 指定日付のスケジュール内容をデータベースから取り出すSQL文作成・実行
			// フィールドdateはSQLの予約語であるので[ ]で囲んで使用
			String sql = "SELECT [date],hour,content FROM schedule WHERE "
							+ "date='" + date + "'";
			Statement stmt = con.createStatement( );	// DBオブジェクト作成
			ResultSet rs = stmt.executeQuery(sql);		// SQL文実行
			// 結果取得
			while(rs.next( )) {							// ResultSetの結果を順に取出す
				String hour = rs.getString("hour");		// hourフィールドの内容取得
				int p = Integer.parseInt(hour) - 6;		// 午前6時から
				contents[p] = rs.getString("content");	// スケジュール内容
			}
			// データベースクローズ（オープンした順とは逆に閉じていく）
			rs.close( );
			stmt.close( );
			con.close( );
		} catch (Exception e) {
			System.out.println("->"+e);
		}

		// SendMessage例  "1999/11/25#　#　#　#　#　#　#　#　#　#　#　#　#　#　#　#　#　#　"
		// 日付と6時から23時までの内容
		String SendMessage = date;
		for (int i = 0; i < DATAMAX; i++) 			// 内容設定
			SendMessage = SendMessage + "#" + contents[i];
		return SendMessage;
	}
	// 追加 -----------------------------------------------------------------------------
	// 指定データをテーブルに追加
	void Insert(String date, String hour, String content) {
		try {
			// ODBC-JDBC Bridge ドライバロード
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// ODBC DSNを介してデータベースに接続
			String url = "jdbc:odbc:scheduleDB";
			String user = "guest";
			String password = "007";
			Connection con = DriverManager.getConnection(url, user, password);
			Statement stmt = con.createStatement( );// DBオブジェクト作成
			// データベースに追加するSQL文作成
			// フィールドdateはSQLの予約語であるので[ ]で囲んで使用
			String sql = "INSERT INTO schedule([date], hour, content) VALUES ("
							+ "'" + date + "',"
							+ "'" + hour + "',"
							+ "'" + content + "'"+")";
			stmt.executeUpdate(sql);				// SQL文実行

			// データベースクローズ（オープンした順とは逆に閉じていく）
			stmt.close( );
			con.close( );
		} catch (Exception e) {
			System.out.println("Error->"+e);
		}
	}
	// 修正 -----------------------------------------------------------------------------
	// テーブルのデータを修正
	void Update(String date, String hour, String content) {
		try {
			// ODBC-JDBC Bridge ドライバロード
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// ODBC DSNを介してデータベースに接続
			String url = "jdbc:odbc:scheduleDB";
			String user = "guest";
			String password = "007";
			Connection con = DriverManager.getConnection(url, user, password);

			// 指定した日時のデータベースのスケジュールの内容を更新するSQL文作成・実行
			String sql = "UPDATE schedule SET "
							+ "content='" + content + "' "
							+ "WHERE date='" + date + "' AND hour='" + hour + "'";
			Statement stmt = con.createStatement( );	// DBオブジェクト作成
			stmt.executeUpdate(sql);					// SQL文実行

			// データベースクローズ（オープンした順とは逆に閉じていく）
			stmt.close( );
			con.close( );
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	// 削除 -----------------------------------------------------------------------------
	// テーブルのデータを削除
	void Delete(String date, String hour) {
		try {
			// ODBC-JDBC Bridge ドライバロード
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// ODBC DSNを介してデータベースに接続
			String url = "jdbc:odbc:scheduleDB";
			String user = "guest";
			String password = "007";
			Connection con = DriverManager.getConnection(url, user, password);

			//指定した日時のデータベースのスケジュールの内容を削除するSQL文作成・実行
			String sql = "DELETE FROM schedule WHERE "
 							+ "date='" + date + "' AND hour='" + hour + "'";
			Statement stmt = con.createStatement( );	// DBオブジェクト作成
			stmt.executeUpdate(sql);					// SQL文実行

			// データベースクローズ（オープンした順とは逆に閉じていく）
			stmt.close( );
			con.close( );
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
