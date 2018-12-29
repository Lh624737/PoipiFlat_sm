package com.pospi.pai.yunpos.util;

import java.io.UnsupportedEncodingException;

public class ESCUtil {

	public static final byte ESC = 27;// Escape
	public static final byte FS = 28;// Text delimiter
	public static final byte GS = 29;// Group separator  
	public static final byte DLE = 16;// data link escape
	public static final byte EOT = 4;// End of transmission
	public static final byte ENQ = 5;// Enquiry character
	public static final byte SP = 32;// Spaces
	public static final byte HT = 9;// Horizontal list
	public static final byte LF = 10;//Print and wrap (horizontal orientation)
	public static final byte CR = 13;// Home key
	public static final byte FF = 12;// Carriage control (print and return to the standard mode (in page mode))
	public static final byte CAN = 24;// Canceled (cancel print data in page mode)

	// ------------------------Initialize the printer-----------------------------

	/**
	 * Initialize the printer
	 * @return
	 */
	public static byte[] init_printer() {
		byte[] result = new byte[2];
		result[0] = ESC;
		result[1] = 64;
		return result;
	}

	// ------------------------Wrap-----------------------------

	/**
	 * Wrap
	 * 
	 * @param lineNum  how many line do you want wrap
	 * @return
	 */
	public static byte[] nextLine(int lineNum) {
		byte[] result = new byte[lineNum];
		for (int i = 0; i < lineNum; i++) {
			result[i] = LF;
		}

		return result;
	}

	// ------------------------underline-----------------------------

	/**
	 * draw a underline（1 pixel width）
	 * 
	 * @return
	 */
	public static byte[] underlineWithOneDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 1;
		return result;
	}

	/**
	 * draw a underline（2 pixel width）
	 * 
	 * @return
	 */
	public static byte[] underlineWithTwoDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 2;
		return result;
	}

	/**
	 * cancel draw a underline
	 * 
	 * @return
	 */
	public static byte[] underlineOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 0;
		return result;
	}

	// ------------------------bold-----------------------------

	/**
	 * select bold option
	 * 粗体
	 * @return
	 */
	public static byte[] boldOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0xF;
		return result;
	}

	/**
	 * cancel bold option
	 * 
	 * @return
	 */
	public static byte[] boldOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0;
		return result;
	}

	// ------------------------Align-----------------------------

	/**
	 * Align left
	 * 
	 * @return
	 */
	public static byte[] alignLeft() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 0;
		return result;
	}

	/**
	 * 居中对齐
	 * 
	 * @return
	 */
	public static byte[] alignCenter() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 1;
		return result;
	}

	/**
	 * Align right
	 * 
	 * @return
	 */
	public static byte[] alignRight() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 2;
		return result;
	}

	/**
	 * Horizontal move col columns to the right
	 * 
	 * @param col
	 * @return
	 */
	public static byte[] set_HT_position(byte col) {
		byte[] result = new byte[4];
		result[0] = ESC;
		result[1] = 68;
		result[2] = col;
		result[3] = 0;
		return result;
	}
	// ------------------------Font bigger-----------------------------

	/**
	 * Font bigger 5 times than normal
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] fontSizeSetBig(int num) {
		byte realSize = 0;
		switch (num) {
		case 1:
			realSize = 0;
			break;
		case 2:
			realSize = 17;
			break;
		case 3:
			realSize = 34;
			break;
		case 4:
			realSize = 51;
			break;
		case 5:
			realSize = 68;
			break;
		case 6:
			realSize = 85;
			break;
		case 7:
			realSize = 102;
			break;
		case 8:
			realSize = 119;
			break;
		}
		byte[] result = new byte[3];
		result[0] = 29;
		result[1] = 33;
		result[2] = realSize;
		return result;
	}

	// ------------------------Font smaller-----------------------------

	/**
	 * font smaller
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] fontSizeSetSmall(int num) {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 33;

		return result;
	}

	// ------------------------Paper cutting-----------------------------

	/**
	 * Paper cutting
	 * 
	 * @return
	 */
	public static byte[] feedPaperCutAll() {
		byte[] result = new byte[4];
		result[0] = GS;
		result[1] = 86;
		result[2] = 65;
		result[3] = 0;
		return result;
	}

	/**
	 * Paper cutting（the left leave some）
	 * 
	 * @return
	 */
	public static byte[] feedPaperCutPartial() {
		byte[] result = new byte[4];
		result[0] = GS;
		result[1] = 86;
		result[2] = 66;
		result[3] = 0;
		return result;
	}

	// ------------------------Cutting paper-----------------------------
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public static byte[] byteMerger(byte[][] byteList) {

		int length = 0;
		for (int i = 0; i < byteList.length; i++) {
			length += byteList[i].length;
		}
		byte[] result = new byte[length];

		int index = 0;
		for (int i = 0; i < byteList.length; i++) {
			byte[] nowByte = byteList[i];
			for (int k = 0; k < byteList[i].length; k++) {
				result[index] = nowByte[k];
				index++;
			}
		}
		for (int i = 0; i < index; i++) {
			// CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
		}
		return result;
	}

	// --------------------
	public static byte[] generateMockData() {
		try {
			byte[] next2Line = ESCUtil.nextLine(2);
			byte[] title = "The menu（launch）**wanda plaza".getBytes("gb2312");

			byte[] boldOn = ESCUtil.boldOn();
			byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
			byte[] center = ESCUtil.alignCenter();
			byte[] Focus = "Web 507".getBytes("gb2312");
			byte[] boldOff = ESCUtil.boldOff();
			byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);

			byte[] left = ESCUtil.alignLeft();
			byte[] orderSerinum = "Order No.11234".getBytes("gb2312");
			boldOn = ESCUtil.boldOn();
			byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
			byte[] FocusOrderContent = "Big hamburger(single)".getBytes("gb2312");
			boldOff = ESCUtil.boldOff();
			byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

			next2Line = ESCUtil.nextLine(2);

			byte[] priceInfo = "Receivable:$22  Discount：$2.5 ".getBytes("gb2312");
			byte[] nextLine = ESCUtil.nextLine(1);

			byte[] priceShouldPay = "Actual collection:$19.5".getBytes("gb2312");
			nextLine = ESCUtil.nextLine(1);

			byte[] takeTime = "Pickup time:2015-02-13 12:51:59".getBytes("gb2312");
			nextLine = ESCUtil.nextLine(1);
			byte[] setOrderTime = "Order time：2015-02-13 12:35:15".getBytes("gb2312");

			byte[] tips_1 = "Follow twitter\"**\"order for $1 discount".getBytes("gb2312");
			nextLine = ESCUtil.nextLine(1);
			byte[] tips_2 = "Commentary reward 50 cents".getBytes("gb2312");
			byte[] next4Line = ESCUtil.nextLine(4);

			byte[] breakPartial = ESCUtil.feedPaperCutPartial();

			byte[][] cmdBytes = { title, nextLine, center, boldOn, fontSize2Big, Focus, boldOff, fontSize2Small,
					next2Line, left, orderSerinum, nextLine, center, boldOn, fontSize1Big, FocusOrderContent, boldOff,
					fontSize1Small, nextLine, left, next2Line, priceInfo, nextLine, priceShouldPay, next2Line, takeTime,
					nextLine, setOrderTime, next2Line, center, tips_1, nextLine, center, tips_2, next4Line,
					breakPartial };

			return ESCUtil.byteMerger(cmdBytes);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
//	public static void beginPrint(Context context ,String maxNo ,String payWay ,String goods ,String shishou,String zhaolin ){
//		SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//		int receipt_num = sharedPreferences.getInt("receipt_num", 1);
//
//		// 1: Get BluetoothAdapter
//		BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
//		if (btAdapter == null) {
//			Log.i("test", "蓝牙未打开");
////			Toast.makeText(context, "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
//			return;
//		}
//		// 2: Get Sunmi's InnerPrinter BluetoothDevice
//		BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
//		if (device == null) {
//			Log.i("test", "未找到打印设备");
////			Toast.makeText(context, "Please Make Sure Bluetooth have InnterPrinter!",
////					Toast.LENGTH_LONG).show();
//			return;
//		}
//		// 3: Generate a order data
////				byte[] data = ESCUtil.generateMockData();
////		byte[] data = ESCUtil.printTob();
//		// 4: Using InnerPrinter print data
//		BluetoothSocket socket = null;
//		try {
//			socket = BluetoothUtil.getSocket(device);
//			BluetoothUtil.sendData(data, socket);
//		} catch (IOException e) {
//			if (socket != null) {
//				try {
//					socket.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//	}



}