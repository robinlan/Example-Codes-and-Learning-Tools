import java.applet.*;				// Applet, AudioClip
import java.awt.*;					// Graphics, Image, Color, Font, MediaTracker
import java.awt.event.*;			// MouseListener, MouseEvent, InputEvent

public class Othello extends Applet implements Runnable, MouseListener {
    int Row, Column;								// オセロ盤の行・列
	boolean PreparationFlag = false;				// 準備フラグ
	Image PlateImage;								// オセロ盤イメージ画像
	Image WorkImage;								// 作業用イメージ
	Graphics WorkGraphics;							// 作業用グラフィックス
	int Board[ ][ ] = new int[8][8];				// オセロ盤の内容
	int Work[ ][ ] = new int[8][8];					// 作業用盤の内容
	Thread thread;									// スレッド

	Image KomaImage[ ] = new Image[8];				// コマ回転イメージ（白０～黒７）
	int Koma = 0;									// 0:白の番, 7:黒の番
	int Change = 0;									// 変化量（白から黒は１，黒から白は-1）
	int Komacount[ ] = {2, 2};						// コマカウント(初期値）
    AudioClip ReverseSound;							// 反転サウンド

	// 初期化処理 -----------------------------------------------------------------------
    public void init( ) {
		addMouseListener(this);						// マウスリスナー追加

        ReverseSound = getAudioClip(getCodeBase( ), "sound/click.au");	// 反転音

		WorkImage = createImage(440, 440);			// 作業用イメージ作成
		WorkGraphics = WorkImage.getGraphics( );	// 作業用グラフィックス取得

		MediaTracker mediatracker = new MediaTracker(this);	// メディアトラッカ作成
		PlateImage = getImage(getCodeBase( ), "image/plate.gif");	// オセロ盤画像
		mediatracker.addImage(PlateImage, 0);		// 画像をメディアトラッカにセット

		for (int i = 0; i < 8; i++) {				// コマ画像取り込み
			KomaImage[i] = getImage(getCodeBase( ), "image/koma" + i +".gif");
			mediatracker.addImage(KomaImage[i], 0);	// 画像をメディアトラッカにセット
		}
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch (InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" " + e);
		}

		for (int i = 0; i < 8; i++)					// オセロ盤の配列に値を設定
			for (int j = 0; j < 8; j++)
				Board[i][j] = -1;					// -1：何も置いていない状態を表す
		Board[3][3] = Board[4][4] = 0;				// 0:白コマ
		Board[3][4] = Board[4][3] = 7;				// 7:黒コマ

		PreparationFlag = true;						// 準備フラグ
    }
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
    public void paint(Graphics g) {
		// 作業領域がまだ確保されないか，または準備フラグがオフの場合
		if (WorkGraphics == null || PreparationFlag == false)
			return;

		WorkGraphics.drawImage(PlateImage, 0, 0, this);	// オセロ盤を描画

		for (int i = 0; i < 8; i++) {				// オセロ盤の状況を描画
			for (int j = 0; j < 8; j++) {
				if (Board[i][j] != -1)				// オセロ盤のi,jの位置が空でない場合
					WorkGraphics.drawImage(KomaImage[Board[i][j]],
											j*50+25, i*50+25, this);
			}
		}
		WorkGraphics.drawString(					// 取得コマ数表示
				"You(White) : " + Komacount[0] + " - Computer(Black) : " + Komacount[1],
				130, 435);

		g.drawImage(WorkImage, 0, 0, this);			// 作業イメージをアプレットに描画
    }
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		while (thread != null) {					// スレッドが存在している間
			repaint( );								// 再描画
			if (ChangeKoma(Change) == 0) {			// 変化なしの場合
				if (Koma == 1)						// 変化後，人間の番
					Koma =  0;						// 人間の番
				else if (Koma == 6) {				// 変化後，コンピュータ（黒コマ）の番
					Koma = 7;						// コンピュータ（黒コマ)の番
					Change = 1;						// 反転変化値＋１（白０から黒７へ）
					computerprocess( );				// コンピュータの思考処理
					try {
						thread.sleep(1000);			// 1秒待つ
					} catch (InterruptedException e) {	// 他のスレッドの割り込み例外処理
						break;
					}
					Koma = 1;						// 変化後，人間（白コマ)の番
				}
			}

			try {
				thread.sleep(100);					// スレッドを100ミリ秒スリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				break;
			}
		}
	}
	// コンピュータ側の思考処理 ---------------------------------------------------------
	void computerprocess( ) {
		int Row = -1, Column = -1;	// コンピュータが置くことができる盤の行と列の保管用
		boolean flag = false;

		// -----------------------------------------------------------------------------
		// ●隅に置くことができれば置く
		Board2work( );								// オセロ盤の内容を作業用の盤にコピー
		if (check(0, 0, Koma, Change, 0) > 0) {			// 左上隅に置ける場合
			flag = true;
			Row = 0;
			Column = 0;
		}
		else if (check(0, 7, Koma, Change, 0) > 0) {	// 右上隅に置ける場合
			flag = true;
			Row = 0;
			Column = 7;
		}
		else if (check(7, 0, Koma, Change, 0) > 0) {	// 左下隅に置ける場合
			flag = true;
			Row = 7;
			Column = 0;
		}
		else if (check(7, 7, Koma, Change, 0) > 0) {	// 右下隅に置ける場合
			flag = true;
			Row = 7;
			Column = 7;
		}

		// ------------------------------------------------------------------------------
		// ●置いた後，次の手で隅を取ることができる
		if (flag == false) {
			for (int i = 0; flag == false && i <= 7; i++) {
				for (int j = 0; flag == false && j <= 7; j++) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(i, j, Koma, Change, 1) > 0) {		// 置くことができる場合
						if (check(0, 0, Koma, Change, 0) > 0	// 隅に置ける場合
						 || check(0, 7, Koma, Change, 0) > 0
						 || check(7, 0, Koma, Change, 0) > 0
						 || check(7, 7, Koma, Change, 0) > 0) {
							flag = true;
							Row = i;
							Column = j;
						}
					}
				}
			}
		}

		// ------------------------------------------------------------------------------
		// ●周囲にコマを置いた後，相手に反転させられることがなく，さらに
		// 人間がそのラインの空いているところに置いて隅を直接取ることがない場合は置く
		// 0行目
		if (flag == false) {
			for (int j = 0; flag == false && j <= 7; j++) {
				Board2work( );						// オセロ盤の内容を作業用の盤にコピー
				if (check(0, j, 7, Change, 1) > 0) {// コンピュータが置けた場合
					WorkInsideClear( );				// workの内側クリア（端のライン調査)
					// 置いた後，端だけで人間が反転させないかチェック
					int	hanten = 0;					// 人間が反転した数
					for (int p = 0; hanten == 0 && p <= 7; p++) {
						hanten = check(0, p, 0, Change, 1);	// 人間が反転できるか
					}
					if (hanten > 0)					// 人間が反転させた場合
						continue;					// ここには置くことができない
					else {							// さらにチェック
						// 置いた後，全体的に端に人間が置けるかチェック
						flag = true;				// 大丈夫と仮定
						for (int p = 0; flag == true && p <= 7; p++) {
							// 再度，コンピュータが置いた場面を設定
							Board2work( );			// オセロ盤の内容を作業用の盤にコピー
							check(0, j, 7, Change, 1);//先の位置にコンピュータが置くとする
							if ((hanten = check(0, p, 0, Change, 1)) > 0) {	
								// 人間が置けた場合
								// さらにコンピュータが端に置くことができるかチェック
								WorkInsideClear( );	// workの内側クリア（端のライン調査)
								hanten = 0;
								for (int q = 0; hanten == 0 && q <= 7; q++)
									hanten = check(0, q, 7, Change, 1);
								// そのときに直接，人間が隅に置くことができるかチェック
								if (check(0, 0, 0, Change, 0) > 0
								 || check(0, 7, 0, Change, 0) > 0)
									flag = false;	// ここには置くことができない
							}
						}
						if (flag == true) {			// すべて調べて人間が端を取らない場合
							Row = 0;
							Column = j;
						}
					}
				}
			}
		}
		// 7行目
		if (flag == false) {
			for (int j = 0; flag == false && j <= 7; j++) {
				Board2work( );						// オセロ盤の内容を作業用の盤にコピー
				if (check(7, j, 7, Change, 1) > 0) {// コンピュータが置けた場合
					WorkInsideClear( );				// workの内側クリア（端のライン調査)
					// 置いた後，端だけで人間が反転させないかチェック
					int	hanten = 0;					// 人間が反転した数
					for (int p = 0; hanten == 0 && p <= 7; p++) {
						hanten = check(7, p, 0, Change, 1);	// 人間が反転できるか
					}
					if (hanten > 0)					// 人間が反転させた場合
						continue;					// ここには置くことができない
					else {							// さらにチェック
						// 置いた後，全体的に端に人間が置けるかチェック
						flag = true;				// 大丈夫と仮定
						for (int p = 0; flag == true && p <= 7; p++) {
							// 再度，コンピュータが置いた場面を設定
							Board2work( );			// オセロ盤の内容を作業用の盤にコピー
							check(7, j, 7, Change, 1);//先の位置にコンピュータが置くとする
							if ((hanten = check(7, p, 0, Change, 1)) > 0) {
								// 人間が置けた場合
								// さらにコンピュータが端に置くことができるかチェック
								WorkInsideClear( );	// workの内側クリア（端のライン調査)
								hanten = 0;
								for (int q = 0; hanten == 0 && q <= 7; q++)
									hanten = check(7, q, 7, Change, 1);
								// そのときに直接，人間が隅に置くことができるかチェック
								if (check(7, 0, 0, Change, 0) > 0
								 || check(7, 7, 0, Change, 0) > 0)
									flag = false;	// ここには置くことができない
							}
						}
						if (flag == true) {			// すべて調べて人間が端を取らない場合
							Row = 7;
							Column = j;
						}
					}
				}
			}
		}
		// 0列目
		if (flag == false) {
			for (int i = 0; flag == false && i <= 7; i++) {
				Board2work( );						// オセロ盤の内容を作業用の盤にコピー
				if (check(i, 0, 7, Change, 1) > 0) {// コンピュータが置けた場合
					WorkInsideClear( );				// workの内側クリア（端のライン調査)
					// 置いた後，端だけで人間が反転させないかチェック
					int	hanten = 0;					// 人間が反転した数
					for (int p = 0; hanten == 0 && p <= 7; p++) {
						hanten = check(p, 0, 0, Change, 1);	// 人間が反転できるか
					}
					if (hanten > 0)					// 人間が反転させた場合
						continue;					// ここには置くことができない
					else {							// さらにチェック
						// 置いた後，全体的に端に人間が置けるかチェック
						flag = true;				// 大丈夫と仮定
						for (int p = 0; flag == true && p <= 7; p++) {
							// 再度，コンピュータが置いた場面を設定
							Board2work( );			// オセロ盤の内容を作業用の盤にコピー
							check(i, 0, 7, Change, 1);//先の位置にコンピュータが置くとする
							if ((hanten = check(p, 0, 0, Change, 1)) > 0) {
								// 人間が置けた場合
								// さらにコンピュータが端に置くことができるかチェック
								WorkInsideClear( );	// workの内側クリア（端のライン調査)
								hanten = 0;
								for (int q = 0; hanten == 0 && q <= 7; q++)
									hanten = check(q, 0, 7, Change, 1);
								// そのときに直接，人間が隅に置くことができるかチェック
								if (check(0, 0, 0, Change, 0) > 0
								 || check(7, 0, 0, Change, 0) > 0)
									flag = false;	// ここには置くことができない
							}
						}
						if (flag == true) {			// すべて調べて人間が端を取らない場合
							Row = i;
							Column = 0;
						}
					}
				}
			}
		}
		// 7列目
		if (flag == false) {
			for (int i = 0; flag == false && i <= 7; i++) {
				Board2work( );						// オセロ盤の内容を作業用の盤にコピー
				if (check(i, 7, 7, Change, 1) > 0) {// コンピュータが置けた場合
					WorkInsideClear( );				// workの内側クリア（端のライン調査)
					// 置いた後，端だけで人間が反転させないかチェック
					int	hanten = 0;					// 人間が反転した数
					for (int p = 0; hanten == 0 && p <= 7; p++) {
						hanten = check(p, 7, 0, Change, 1);	// 人間が反転できるか
					}
					if (hanten > 0)					// 人間が反転させた場合
						continue;					// ここには置くことができない
					else {							// さらにチェック
						// 置いた後，全体的に端に人間が置けるかチェック
						flag = true;				// 大丈夫と仮定
						for (int p = 0; flag == true && p <= 7; p++) {
							// 再度，コンピュータが置いた場面を設定
							Board2work( );			// オセロ盤の内容を作業用の盤にコピー
							check(i, 7, 7, Change, 1);//先の位置にコンピュータが置くとする
							if ((hanten = check(p, 7, 0, Change, 1)) > 0) {
								// 人間が置けた場合
								// さらにコンピュータが端に置くことができるかチェック
								WorkInsideClear( );	// workの内側クリア（端のライン調査)
								hanten = 0;
								for (int q = 0; hanten == 0 && q <= 7; q++)
									hanten = check(q, 7, 7, Change, 1);
								// そのときに直接，人間が隅に置くことができるかチェック
								if (check(0, 7, 0, Change, 0) > 0
								 || check(7, 7, 0, Change, 0) > 0)
									flag = false;	// ここには置くことができない
							}
						}
						if (flag == true) {			// すべて調べて人間が端を取らない場合
							Row = i;
							Column = 7;
						}
					}
				}
			}
		}

		// ------------------------------------------------------------------------------
		// ●隅から置くことができれば置く
		// 一手で隅が取れる，または隅が自分のコマの場合は隅から初めて空いた場所に置く
		// 0,0→
		if (flag == false) {
			Row = 0; Column = 0;
			// 何も置いていないかまたは自分のコマの場合
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Column <= 7 && Board[Row][Column] != -1)
					Column++;
				// 隅から連続して置くことができる場合
				if (Column <= 7 && Board[Row][Column] == -1) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 7,0→
		if (flag == false) {
			Row = 7; Column = 0;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Column <= 7 && Board[Row][Column] != -1)
					Column++;
				// 隅から連続して置くことができる
				if (Column <= 7 && Board[Row][Column] == -1) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 0,7←
		if (flag == false) {
			Row = 0; Column = 7;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Column >= 0 && Board[Row][Column] != -1)
					Column--;
				// 隅から連続して置くことができる
				if (Column >= 0 && Board[Row][Column] == -1) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 7,7←
		if (flag == false) {
			Row = 7; Column = 7;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Column >= 0 && Board[Row][Column] != -1)
					Column--;
				// 隅から連続して置くことができる
				if (Column >= 0 && Board[Row][Column] == -1) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 0,0↓
		if (flag == false) {
			Row = 0; Column = 0;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Row <= 7 && Board[Row][Column] != -1)
					Row++;
				// 隅から連続して置くことができる
				if (Row <= 7 && Board[Row][Column] == -1)	{
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 0,7↓
		if (flag == false) {
			Row = 0; Column = 7;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Row <= 7 && Board[Row][Column] != -1)
					Row++;
				// 隅から連続して置くことができる
				if (Row <= 7 && Board[Row][Column] == -1)	{
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 7,0↑
		if (flag == false) {
			Row = 7; Column = 0;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Row >= 0 && Board[Row][Column] != -1)
					Row--;
				// 隅から連続して置くことができる
				if (Row >= 0 && Board[Row][Column] == -1)	{
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}
		// 7,7↑
		if (flag == false) {
			Row = 7; Column = 7;
			if (Board[Row][Column] == -1 || Board[Row][Column] == Koma) {
				while (Row >= 0 && Board[Row][Column] != -1)
					Row--;
				// 隅から連続して置くことができる
				if (Row >= 0 && Board[Row][Column] == -1)	{
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					if (check(Row, Column, Koma, Change, 0) > 0)
						flag = true;
				}
			}
		}

		// ------------------------------------------------------------------------------
		// ●最初に設定してあるコマの周囲をとる方法を取る
		if (flag == false) {
			int max = 1, w = 0;						// 最大反転数を0クリア
			for (int i = 2; i <= 5; i++) {
				for (int j = 2; j <= 5; j++) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					// 最大反転数以上に置くことができる場合
					if ((w = check(i, j, Koma, Change, 0)) >= max) {	
						// 最大反転数よりも多い，または最大数と同じで乱数が0.5以上の場合
						if (w > max || w == max && Math.random( ) >= 0.5) {
							max = w;				// 反転数を最大反転数とする
							Row = i;				// その位置の行と列を保管
							Column = j;
							flag = true;
						}
					}
				}
			}
		}

		// ------------------------------------------------------------------------------
		// ●周囲および四隅の内側以外で最も多く取れる位置を取る
		// 　反転最大数が同じ場合は，2分の1の確率でどちらかを選択する
		if (flag == false) {
			int max = 1, w = 0;						// 最大反転数を0クリア
			for (int i = 0; i <= 7; i++) {
				for (int j = 0; j <= 7; j++) {
					if (i == 0 || i == 7 || j == 0 || j == 7)  		// 周囲
						continue;
					if ((i == 1 && j == 1) || (i == 1 && j == 6)	// 四隅の斜め内側
					 || (i == 6 && j == 1) || (i == 6 && j == 6))
						continue;

					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					// 最大反転数以上に置くことができる場合
					if ((w = check(i, j, Koma, Change, 0)) >= max) {	
						// 最大反転数よりも多い，または最大数と同じで乱数が0.5以上の場合
						if (w > max || w == max && Math.random( ) >= 0.5) {
							max = w;				// 反転数を最大反転数とする
							Row = i;				// その位置の行と列を保管
							Column = j;
							flag = true;
						}
					}
				}
			}
		}

		// ------------------------------------------------------------------------------
		// ●最も多く取れる位置を取る
		// 　反転最大数が同じ場合は，2分の1の確率でどちらかを選択する
		if (flag == false) {
			int max = 1, w = 0;						// 最大反転数を0クリア
			for (int i = 0; i <= 7; i++) {
				for (int j = 0; j <= 7; j++) {
					Board2work( );					// オセロ盤の内容を作業用の盤にコピー
					// 最大反転数以上に置くことができる場合
					if ((w = check(i, j, Koma, Change, 0)) >= max) {
						// 最大反転数よりも多い，または最大数と同じで乱数が0.5以上の場合
						if (w > max || w == max && Math.random( ) >= 0.5) {
							max = w;				// 反転数を最大反転数とする
							Row = i;				// その位置の行と列を保管
							Column = j;
							flag = true;
						}
					}
				}
			}
		}

		// ------------------------------------------------------------------------------
		if (flag == true) {							// 置けた場合
			Board2work( );							// オセロ盤の内容を作業用の盤にコピー
			check(Row, Column, Koma, Change, 1);	// 反転処理
			work2Board( );							// 作業用配列の内容をオセロ盤にコピー
			repaint( );
		}

	}

	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット停止 -------------------------------------------------------------------
	public void stop( ) {
		thread = null;								// スレッドを無効
	}
	// 指定した位置にコマが置けるかチェック ---------------------------------------------
	int check(int Row, int Column, int Koma, int Change, int hanten) {
		int totalcount;								// 反転総数
		int count[ ] = new int[8];					// 各方向の反転数

		if (Work[Row][Column] != -1)				// すでにコマが置いてある場合
			return 0;

		count[0] = hantenshori(Row, Column, -1,  0, Koma, Change, hanten);// 上チェック
		count[1] = hantenshori(Row, Column, -1,  1, Koma, Change, hanten);// 右上チェック
		count[2] = hantenshori(Row, Column,  0,  1, Koma, Change, hanten);// 右チェック
		count[3] = hantenshori(Row, Column,  1,  1, Koma, Change, hanten);// 右下チェック
		count[4] = hantenshori(Row, Column,  1,  0, Koma, Change, hanten);// 下チェック
		count[5] = hantenshori(Row, Column,  1, -1, Koma, Change, hanten);// 左下チェック
		count[6] = hantenshori(Row, Column,  0, -1, Koma, Change, hanten);// 左チェック
		count[7] = hantenshori(Row, Column, -1, -1, Koma, Change, hanten);// 左上チェック

		totalcount = 0;								// 反転総数0クリア
		for (int i = 0; i < 8; i++)					// 8方向の反転総数
			totalcount += count[i];

		if (totalcount > 0 && hanten == 1) {		// 実際に置く場合，反転処理
			Work[Row][Column] = Koma;				// 作業盤の指定位置にコマを置く
		}

		return totalcount;							// 反転総数を返す
	}
	// 反転処理 -------------------------------------------------------------------------
	int hantenshori(int Row, int Column, int Rowstep, 
					int Columnstep, int Koma, int Change, int hanten) {
		int count;

		count = 1;									// 隣の位置からチェック
		// 行が範囲内，列が範囲内，空でなく，自分のコマでない間，繰り返す
		while (Row + Rowstep * count >= 0 && Row + Rowstep * count <= 7 &&
			   Column + Columnstep * count >= 0 && Column + Columnstep * count <= 7 &&
			   Work[Row + Rowstep * count][Column + Columnstep * count] != -1 &&
			   Work[Row + Rowstep * count][Column + Columnstep * count] != Koma) {
			count++;							// 次の位置をチェック
		}

		// 反転数が１より大きい，行が範囲内，列が範囲内，自分のコマの場合
		if (count > 1 && 																			Row + Rowstep * count >= 0 && Row + Rowstep * count <= 7 &&
			Column + Columnstep * count >= 0 && Column + Columnstep * count <= 7 &&
			Work[Row + Rowstep * count][Column + Columnstep * count] == Koma) {

			if (hanten == 1) {						// 反転指示が出ている場合
				ReverseSound.play( );				// 反転サウンド
				count = 1;							// 隣から

				// 自分のコマでない間，繰り返す
				while (Work[Row+Rowstep*count][Column+Columnstep*count] != Koma) {
					// コマの状態を１つ変化
					Work[Row + Rowstep * count][Column + Columnstep * count] += Change;
					count++;						// 次のコマ
				}
				count--;							// チェックが1つ行きすぎたため
			}
		}
		else
			count = 0;								// 反転できなかった場合

		return count;								// 反転数
	}
	// コマを反転させる処理 -------------------------------------------------------------
	int ChangeKoma(int Change) {
		int flag = 0;								// 状態変化フラグ

		Komacount[0] = Komacount[1] = 0;			// コマカウントクリア
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (Board[i][j] == 0)				// 白コマの場合
					Komacount[0]++;					// 白コマの総数
				else if (Board[i][j] == 7)			// 黒コマの場合
					Komacount[1]++;					// 黒コマの総数

				// 空の場所でなく，白コマでなく，黒コマでなく，変化途中のコマの場合
				if (Board[i][j] != -1 && Board[i][j] != 0 && Board[i][j] != 7) {
					Board[i][j] += Change;			// コマを変化させる
					flag = 1;						// 状態が変化した
				}
			}
		}
		return flag;								// 状態変化を返す
	}

	// オセロ盤の内容を作業配列workへコピー --------------------------------------------
	void Board2work( ) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				Work[i][j] = Board[i][j];
	}
	// 作業配列workの内容をオセロ盤の配列へコピー --------------------------------------
	void work2Board( ) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				Board[i][j] = Work[i][j];
	}
	// 作業配列workの内側をクリア ------------------------------------------------------
	void WorkInsideClear( ) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (!(i == 0 || i == 7 || j == 0 || j ==7))
					Work[i][j] = -1;
	}

	// MouseListenerインターフェースを実装 ----------------------------------------------
	// 人間側がマウスをクリックした時の処理
    public void mousePressed(MouseEvent evt) {
		if (Koma != 0)								// 人間の番でない場合
			return;

 		if ((evt.getModifiers( ) & InputEvent.BUTTON3_MASK) != 0) {	// 右ボタン，パス
			Koma = 6;								// 次は，コンピュータ側の番
			return;
		}

        Column = (evt.getX( ) - 20) / 50;			// マウスの位置をオセロ盤の行列に変換
		Row = (evt.getY( ) - 20) / 50;

		Change = -1;								// コマ変化量-1（黒７から白０に減少）
		Board2work( );								// オセロ盤の内容を作業用の盤にコピー
		if (check(Row, Column, Koma, Change, 1) != 0) {	// 置くことができる場合，反転処理
			work2Board( );							// 作業用配列の内容をオセロ盤にコピー
			Koma = 6;								// 変化後，コンピュータ側の番
		}
	}

    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
}
