import java.applet.*;			// Applet, AudioClip
import java.awt.*;				// Graphics, Image, Color, MediaTracker, Button, Panel etc
import java.awt.event.*;		// ActionListener, ActionEvent

public class SlotMachine extends Applet implements Runnable, ActionListener {
								// Runnable とActionListenerインターフェース実装
	CanvasWindows CanvasWindow[ ];					// 表示キャンバス
	Button StopButton1, StopButton2, StopButton3;	// ストップボタン
	Button StartButton;								// スタートボタン
	int SlotMax = 5;								// 5種類のパネル
	Image image[ ] = new Image[SlotMax];			// パネルイメージ
	int imageWidth, imageHeight;					// イメージのサイズ
	Image panelImage;								// パネルイメージ
	Graphics panelGraphics;							// パネルグラフィック
	Thread thread;									// スレッド
	boolean RotateFlag;								// 回転フラグ
	int Y[ ] = new int[3];							// 垂直方向表示位置
	int Step[ ] = {30, 30, 30};						// スクロールステップ
	int StopButton[ ] = {0, 0, 0};					// ストップボタン
	AudioClip ClickSound, SeikaiSound;				// クリックサウンド，正解サウンド

	// 初期化処理 -----------------------------------------------------------------------
	public void init( ) {
    	ClickSound = getAudioClip(getCodeBase( ), "sound/click.au");	// クリック音
    	SeikaiSound = getAudioClip(getCodeBase( ), "sound/seikai.au");	// 正解音

		setLayout(null);							// コンポーネントの配置を自由設定

		Panel DisplayPanel = new Panel( );
		DisplayPanel.setLayout(new GridLayout(1, 3));

		// 画像入力を監視するメディアトラッカーを生成
		MediaTracker mediatracker = new MediaTracker(this);
		for (int i = 0; i < SlotMax; i++) {
			image[i] = getImage(getCodeBase( ), "image/slot"+i+".gif");
			mediatracker.addImage(image[i], 0);		// 画像をメディアトラッカにセット
		}
		try {
			mediatracker.waitForID(0);				// 画像入力の完了を待つ
		} catch (InterruptedException e) {			// waitForIDに対する例外処理
			showStatus(" "+e);
		}

		// 画像の入力が完了した後で，サイズを調べる
		imageWidth = image[0].getWidth(this);		// イメージの幅と高さ
		imageHeight = image[0].getHeight(this);

		// 描画パネルイメージ
		panelImage = createImage(imageWidth, imageHeight*(SlotMax+1));
		panelGraphics = panelImage.getGraphics( );
		panelGraphics.setColor(Color.black);
		for (int i = 0; i < SlotMax+1; i++) {		// 1枚余分に作成
			if (i < SlotMax)
				panelGraphics.drawImage(image[i], 0, i*imageHeight, this);
			else									// ラストを最初のパネルとする
				panelGraphics.drawImage(image[0], 0, i*imageHeight, this);
			panelGraphics.drawRect(0, i*imageHeight,
						imageWidth-1, imageHeight);
		}

		// クラス型データの配列
		CanvasWindow = new CanvasWindows[3];
		for (int i = 0; i < 3; i++) {
			// キャンバス生成
			CanvasWindow[i] = new CanvasWindows(panelImage);
			CanvasWindow[i].setSize(imageWidth, imageHeight);	// キャンバスのサイズ設定
			DisplayPanel.add(CanvasWindow[i]);		// キャンバスをパネルに付加
			Y[i] = -i * imageHeight;				// パネルの初期位置
		}

		Panel StopPanel = new Panel( );				// ストップパネル生成
		StopPanel.setLayout(new GridLayout(1, 3)); 	// ストップパネルのレイアウト設定
		StopButton1 = new Button("Stop1");			// ストップボタン生成
		StopButton2 = new Button("Stop2");
		StopButton3 = new Button("Stop3");
		StopPanel.add(StopButton1);					// ストップボタンを付加
		StopPanel.add(StopButton2);
		StopPanel.add(StopButton3);

		StartButton = new Button("Start");			// スタートボタン生成

		add(DisplayPanel);							// ディスプレイパネルをアプレット付加
		add(StopPanel);								// ストップパネルをアプレットに付加
  		add(StartButton);							// スタートボタンをアプレットに付加

		// ディスプレイパネルの設定
		DisplayPanel.setBounds(0, 0, imageWidth*3, imageHeight);
		// ストップパネルの設定
		StopPanel.setBounds(0, imageHeight, imageWidth*3, imageHeight/2);
		// スタートボタンの設定
		StartButton.setBounds(0,imageHeight+imageHeight/2,imageWidth*3,imageHeight/2); 
		StopButton1.addActionListener(this);		// ボタンにリスナーセット
		StopButton2.addActionListener(this);
		StopButton3.addActionListener(this);
		StartButton.addActionListener(this);
	}
	// アプレット開始 -------------------------------------------------------------------
	public void start( ) {
		thread = new Thread(this);					// スレッド生成
		thread.start( );							// スレッドスタート
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		// 今回，アプレット上に直接描画するものはなし。すべてキャンバス内で描画。
	}
	// スレッド実行 ---------------------------------------------------------------------
	public void run( ) {
		RotateFlag = true;
		while (thread != null) {					// スレッドが存在している間
			while(RotateFlag == true) {				// 回転している間
				for (int i = 0; i < 3; i++) {
					if (Y[i] <= -(SlotMax-1) * imageHeight)
						Y[i] = 0;
					if (Step[i] != 0) {
						CanvasWindow[i].DispPanel(Y[i]);	// パネル位置設定・描画
						Y[i] -= Step[i];					// 原点のｙ座標を上に移動
					}
					if (StopButton[i] == 1 && Step[i] > 1)	// ストップボタン押下後
						Step[i]--;							// 回転移動ステップ減少
					if (Step[i] == 1 && (Y[i] % imageHeight) == 0)
						Step[i] = 0;						// 区切のところでストップ
				}
				if (Step[0]+Step[1]+Step[2] == 0) {	// 三つのパネルが停止
					RotateFlag = false;				// 停止
					// 正解チェック
				    if (Y[0] == Y[1] && Y[1] == Y[2])
						SeikaiSound.play( );
				}
				try {
					thread.sleep(100);				// スレッドを100ミリ秒スリープ
				} catch (InterruptedException e) {	// 他のスレッドの割り込み例外処理
					showStatus(" "+e);
				}
			}
			try {
				thread.sleep(300);					// スレッドを300ミリ秒スリープ
			} catch (InterruptedException e) {		// 他のスレッドの割り込み例外処理
				showStatus(" "+e);
			}
		}
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	// 今回，アプレット内でrepaintしないため，このメソッドは必要なし
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// アプレット終了 -------------------------------------------------------------------
	public void stop( )	{
		thread = null;								// スレッドを無効
	}
	// ActionListenerインターフェースのメソッドを定義 -----------------------------------
    public void actionPerformed(ActionEvent evt) {	// イベント処理
		Button button = (Button)evt.getSource( );
		String label = button.getLabel( );
		ClickSound.play( );
		if (label.equals("Stop1"))					// Stop1が押された場合
			StopButton[0] = 1;
		else if (label.equals("Stop2"))				// Stop2が押された場合
			StopButton[1] = 1;
		else if (label.equals("Stop3"))				// Stop3が押された場合
			StopButton[2] = 1;
		else if (label.equals("Start")) {			// Startボタンが押された場合
			for (int i = 0; i < 3; i++) {
				StopButton[i] = 0;					// 各ストップボタンクリア
				Step[i] = 30;						// 回転ステップ数30
				RotateFlag = true;					// 回転フラグオン
			}
		}
	}
}
// ======================================================================================
// キャンバスウィンドウクラス
class CanvasWindows extends Canvas {
	Image image;
	int Y;
	// コンストラクタ -------------------------------------------------------------------
	public CanvasWindows(Image image) {
		this.image = image;
	}
	// パネル描画 -----------------------------------------------------------------------
	public void DispPanel(int Y) {
		this.Y = Y;
		repaint( );
	}
	// 描画更新処理再定義 ---------------------------------------------------------------
	public void update(Graphics g) {				// デフォルトのupdateを再定義
		paint(g);									// 背景色画面クリア削除，paintのみ
	}
	// 描画処理 -------------------------------------------------------------------------
	public void paint(Graphics g) {
		g.drawImage(image, 0, Y, this);
	}
}
