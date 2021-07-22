package enshud.s1.lexer;
import java.io.*;

public class Lexer {

	//グローバル変数
	static String lineBuffer;
	static String[] Symbol = {"+" , "-" , "*" , "/" , "="  , "<" , ">" , "(" , ")" , "[" , "]" , "." , "," ,  ":" , ";"} ;
	static String[] Num = { "0","1","2","3","4","5","6","7","8","9"};
	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {

		//run("data/pas/normal11.pas", "tmp/out1.ts");
		run("data/pas/normal20.pas", "tmp/out20.ts");
		//run("data/pas/synerr08.pas", "tmp/out8.ts");
	}

	/**
	 * TODO
	 *
	 * 開発対象となるLexer実行メソッド．
	 * 以下の仕様を満たすこと．
	 *
	 * 仕様:
	 * 第一引数で指定されたpasファイルを読み込み，トークン列に分割する．
	 * トークン列は第二引数で指定されたtsファイルに書き出すこと．
	 * 正常に処理が終了した場合は標準出力に"OK"を，
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 *
	 * @param inputFileName 入力pasファイル名
	 * @param outputFileName 出力tsファイル名
	 */
	public static void run(final String inputFileName, final String outputFileName) {
		try {
			FileReader fr = new FileReader( inputFileName );
			BufferedReader br = new BufferedReader(fr);
		 	FileWriter fw = new FileWriter( outputFileName );

		 	int line = 0;
		 	String[] Buffer;
		 	int i = 0;
		 	int flag = 0;//綴り記号→1,名前→0

		 	lineBuffer = br.readLine();
		 	while(lineBuffer!=null) {
		 		i = 0;
		 		String Token = "";
		 		line ++;
		 		Buffer = lineBuffer.split(""); //lineBufferを1文字ずつBufferに格納
		 		while(i < lineBuffer.length()) {
		 			flag=0;
		 			if(Buffer[i].equals(" ") || Buffer[i].equals("\t") || Buffer[i].equals("\n") || Buffer[i].equals(null) ){
		 				i++;
		 			}
		 			else if(isAlpha(Buffer,i)) {		//0-23,43
			 			if(Buffer[i].equals("a")) {
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("and")) {
			 					fw.write(Token + "\t" + "SAND" + "\t" + "0" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();		//Tokenの次の文字まで飛ばす
			 				}
			 				Token = Token(Buffer,i,5);
			 				if(Token.equals("array")) {
			 					fw.write(Token + "\t" + "SARRAY" + "\t" + "1" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("b")) {
			 				Token = Token(Buffer,i,5);
			 				if(Token.equals("begin")) {
			 					fw.write(Token + "\t" + "SBEGIN" + "\t" + "2" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}else {
			 					Token = Token(Buffer,i,7);
			 					if(Token.equals("boolean")) {
			 						fw.write(Token + "\t" + "SBOOLEAN" + "\t" + "3" + "\t" + line + "\n");
			 						flag = 1;
			 						i = i + Token.length();
			 					}
			 				}
			 			}else if(Buffer[i].equals("c")) {
			 				Token = Token(Buffer,i,4);
			 				if(Token.equals("char")) {
			 					fw.write(Token + "\t" + "SCHAR" + "\t" + "4" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("d")) {
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("div")) {  // '/' はisSymbolで
			 					fw.write(Token + "\t" + "SDIVD" + "\t" + "5" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("do")) {
			 					fw.write(Token + "\t" + "SDO" + "\t" + "6" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("e")) {
			 				Token = Token(Buffer,i,4);
			 				if(Token.equals("else")) {
			 					fw.write(Token + "\t" + "SELSE" + "\t" + "7" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("end")) {
			 					fw.write(Token + "\t" + "SEND" + "\t" + "8" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("f")) {
			 				Token = Token(Buffer,i,5);
			 				if(Token.equals("false")) {
			 					fw.write(Token + "\t" + "SFALSE" + "\t" + "9" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("i")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("if")) {
			 					fw.write(Token + "\t" + "SIF" + "\t" + "10" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,7);
			 				if(Token.equals("integer")) {
			 					fw.write(Token + "\t" + "SINTEGER" + "\t" + "11" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("m")) {
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("mod")) {
			 					fw.write(Token + "\t" + "SMOD" + "\t" + "12" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("n")) {
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("not")) {
			 					fw.write(Token + "\t" + "SNOT" + "\t" + "13" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("o")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("of")) {
			 					fw.write(Token + "\t" + "SOF" + "\t" + "14" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("or")) {
			 					fw.write(Token + "\t" + "SOR" + "\t" + "15" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("p")) {
			 				Token = Token(Buffer,i,9);
			 				if(Token.equals("procedure")) {
			 					fw.write(Token + "\t" + "SPROCEDURE" + "\t" + "16" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,7);
			 				if(Token.equals("program")) {
			 					fw.write(Token + "\t" + "SPROGRAM" + "\t" + "17" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("r")) {
			 				Token = Token(Buffer,i,6);
			 				if(Token.equals("readln")) {
			 					fw.write(Token + "\t" + "SREADLN" + "\t" + "18" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("t")) {
			 				Token = Token(Buffer,i,4);
			 				if(Token.equals("then")) {
			 					fw.write(Token + "\t" + "STHEN" + "\t" + "19" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 				Token = Token(Buffer,i,4);
			 				if(Token.equals("true")) {
			 					fw.write(Token + "\t" + "STRUE" + "\t" + "20" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("v")) {
			 				Token = Token(Buffer,i,3);
			 				if(Token.equals("var")) {
			 					fw.write(Token + "\t" + "SVAR" + "\t" + "21" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("w")) {
			 				Token = Token(Buffer,i,5);
		 					if(Token.equals("while")) {
		 						fw.write(Token + "\t" + "SWHILE" + "\t" + "22" + "\t" + line + "\n");
		 						flag = 1;
		 						i = i + Token.length();
		 					}
		 					Token = Token(Buffer,i,7);
		 					if(Token.equals("writeln")) {
		 						fw.write(Token + "\t" + "SWRITELN" + "\t" + "23" + "\t" + line + "\n");
		 						flag = 1;
		 						i = i + Token.length();
		 					}
			 			}if(flag == 0) {
			 				int j = 0;
			 				Token = "";
			 				while((i+j < lineBuffer.length()) && !(isSymbol(Buffer,i+j)) && (!Buffer[i+j].equals(" "))) {
			 					Token = Token + Buffer[i+j];
			 					j++;
			 					flag = 1;
			 				}
			 				fw.write(Token + "\t" + "SIDENTIFIER" + "\t" + "43" + "\t" + line + "\n");
	 						i = i + Token.length();
			 			}
		 			}else if(isSymbol(Buffer,i)) {		//24-42
		 				if(Buffer[i].equals("/")) {
		 					Token = Buffer[i];
		 					fw.write(Token + "\t" + "SDIVD" + "\t" + "5" + "\t" + line + "\n");
		 					i = i + Token.length();
		 				}else if(Buffer[i].equals("=")) {
			 				Token = Token(Buffer,i,1);
			 				fw.write(Token + "\t" + "SEQUAL" + "\t" + "24" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();		//Tokenの次の文字まで飛ばす
			 			}else if(Buffer[i].equals("<")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("<>")) {
			 					fw.write(Token + "\t" + "SNOTEQUAL" + "\t" + "25" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();		//Tokenの次の文字まで飛ばす
			 				}else if(Token.equals("<=")) {
			 					fw.write(Token + "\t" + "SLESSEQUAL" + "\t" + "27" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();		//Tokenの次の文字まで飛ばす
			 				}else{
			 					Token = Buffer[i];
			 					fw.write(Token + "\t" + "SLESS" + "\t" + "26" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals(">")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals(">=")) {
			 					fw.write(Token + "\t" + "SGREATEQUAL" + "\t" + "28" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();		//Tokenの次の文字まで飛ばす
			 				}else{
			 					Token = Buffer[i];
			 					fw.write(Token + "\t" + "SGREAT" + "\t" + "29" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals("+")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SPLUS" + "\t" + "30" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals("-")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SMINUS" + "\t" + "31" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals("*")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SSTAR" + "\t" + "32" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals("(")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SLPAREN" + "\t" + "33" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals(")")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SRPAREN" + "\t" + "34" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals("[")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SLBRACKET" + "\t" + "35" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals("]")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SRBRACKET" + "\t" + "36" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals(";")) {
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SSEMICOLON" + "\t" + "37" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}else if(Buffer[i].equals(":")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals(":=")) {
			 					fw.write(Token + "\t" + "SASSIGN" + "\t" + "40" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}else{
				 				Token = Buffer[i];
				 				fw.write(Token + "\t" + "SCOLON" + "\t" + "38" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals(".")) {
			 				Token = Token(Buffer,i,2);
			 				if(Token.equals("..")) {
			 				fw.write(Token + "\t" + "SRANGE" + "\t" + "39" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 				}else {
			 					Token = Buffer[i];
				 				fw.write(Token + "\t" + "SDOT" + "\t" + "42" + "\t" + line + "\n");
			 					flag = 1;
			 					i = i + Token.length();
			 				}
			 			}else if(Buffer[i].equals(",")){
			 				Token = Buffer[i];
			 				fw.write(Token + "\t" + "SCOMMA" + "\t" + "41" + "\t" + line + "\n");
		 					flag = 1;
		 					i = i + Token.length();
			 			}
		 			}else if(isNumber(Buffer,i)) {	//43
		 				int j = 1;
		 				Token = Buffer[i];
		 				while(i+j<lineBuffer.length() && isNumber(Buffer,i+j) ) {
		 					Token = Token + Buffer[i+j];
		 					j++;
		 				}
		 				fw.write(Token + "\t" + "SCONSTANT" + "\t" + "44" + "\t" + line + "\n");
 						i = i + Token.length();
		 			}else if(isString(Buffer,i)) {		//'文字列'
		 				int j = 1;
		 				Token = Buffer[i];				//前側の" ' "
		 				while(!Buffer[i+j].equals("'") && i+j<lineBuffer.length()) {
		 					Token = Token + Buffer[i+j];
		 					j++;
		 				}
		 				Token = Token + Buffer[i+j];		//後ろ側の" ' "追加
		 				fw.write(Token + "\t" + "SSTRING" + "\t" + "45" + "\t" + line + "\n");
 						i = i + Token.length();
		 			}else if(isComment(Buffer,i)) {		//注釈 {Comment}
		 				int j = 1;
		 				while(!(Buffer[i+j].equals("}")) && i+j<lineBuffer.length()) {
		 					j++;
		 				}
 						i = i+j+1;
		 			}
		 			if(i>=lineBuffer.length()) {
		 				break;
		 			}
		 		} //while(行)
		 		lineBuffer = br.readLine();
		 	}  //while(EOF)

		 	fr.close();
		 	br.close();
			fw.close();

			System.out.println("OK");
		}catch( FileNotFoundException e ){
			System.err.println("File not found");
		}catch( IOException e ){
			System.out.println("error");
		}
	}

	//method

	public static String Token(String[] Buffer , int i , int num) {
		String Token="";
		int j = 0;
		while(j<num && (i+j < lineBuffer.length())) {
			Token = Token + Buffer[i+j];
			j++;
		}
		return Token;
	}

	public static boolean isSymbol(String[] Buffer , int i) {

		for(int j=0 ;j < Symbol.length;j++) {
			if(Buffer[i].equals(Symbol[j])) {
				return true;
			}
		}
		return false;

	}

	public static int isSymbol(String[] Buffer , int i) {

		for(int j=0 ;j < Symbol.length;j++) {
			if(Buffer[i].equals(Symbol[j])) {
				return Integer.parseInt(Buffer[i]);
			}
		}
		return -1;

	}

	public static boolean isNumber(String[] Buffer , int i) {

		for(int j=0; j< 10;j++) {
			if(Buffer[i].equals(Num[j])){
				return true;
			}
		}


		return false;

	}

	public static boolean isAlpha(String[] Buffer , int i) {		//A-Z,a-zの時だけtrue
		if((Buffer[i].charAt(0) >= 'A' && Buffer[i].charAt(0) <= 'z') ) {
			if((Buffer[i].charAt(0) >= '[' && Buffer[i].charAt(0) <= '`')) {
				return false;
			}else {
				return true;
			}
		}
		return false;
	}


	public static boolean isString(String[] Buffer , int i) {
		if(Buffer[i].equals("'")) {
			return true;
		}
		return false;
	}

	public static boolean isComment(String[] Buffer , int i) {
		if(Buffer[i].equals("{")) {
			return true;
		}
		return false;
	}
}
