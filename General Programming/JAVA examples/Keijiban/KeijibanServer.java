import java.net.*;				// Socket, ServerSocket
import java.io.*;				// InputStream, InputStreamReader, BufferedReader etc

public class KeijibanServer {

	// メイン処理 -----------------------------------------------------------------------
	public static void main(String args[ ]) {
		KeijibanServer obj = new KeijibanServer( );
		obj.KeijibanProcess( );				// (new KeijibanServer( )).KeijibanProcess( );
	}
	// 掲示板処理 -----------------------------------------------------------------------
	void KeijibanProcess( ){

	 	BufferedReader netinput = null;					// ネットワーク経由入力
		try {
			// 指定ポートでサーバソケット作成
			ServerSocket serversocket = new ServerSocket(8005);
			System.out.println("socket=" + serversocket);

			while (true) {
				Socket socket = serversocket.accept( );// クライアントからの受信接続を待つ
				try {
					// ソケットから入力ストリームを作成
					InputStream inputstream = socket.getInputStream( );
					// 入力ストリームから行単位に入力できるバッファドリーダを作成
					netinput = new BufferedReader(new InputStreamReader(inputstream));					} catch(Exception e) {				// 例外処理
					System.out.println("check point 1 : " + e);
				}

				// クライアントからファイル名受信
				String filename = netinput.readLine( );		// ファイル名

				try {
					// ファイルに接続するFileInputStream作成
					FileInputStream fis = new FileInputStream(filename);
					// バッファリングされた文字型入力ストリームを作成
					BufferedReader br = 
						new BufferedReader(new InputStreamReader(fis));
					// ファイル名を指定して FileWriter オブジェクトを構築
					FileWriter fw = new FileWriter("temp");
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw);

					// クライアントからのデータを一時ファイルに保存
					while (true) {
 						// クライアントからのメッセージ受信
						String data = netinput.readLine( );
						if (data.equals("end"))
							break;
						else
							pw.println(data);
					}
					pw.flush( );					// バッファ内データを強制的に出力

					// 指定ファイルを一時ファイルに保存
					String line;
					while ((line = br.readLine( )) != null) {	// 1行入力
						pw.println(line);
					}
					br.close( );					// 指定ファイルのストリームをクローズ
					fis.close( ); 

					pw.close( );					// 一時ファイルのストリームをクローズ
					bw.close( );
					fw.close( );
				}catch(Exception e) {				// 例外処理
					System.out.println("check point 2 : " + e);
				}

				// 一時ファイルtempの名前を指定ファイル名に変更
				File f1 = new File("temp");			// ファイルオブジェクト生成
				File f2 = new File(filename);
				f2.delete( );						// ファイル削除
				f1.renameTo(f2);					// ファイル名変更
			}

		}catch(Exception e) {						// 例外処理
			System.out.println("check point 3 : " + e);
		}
	}
}
