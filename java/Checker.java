package enshud.s4.compiler;

import java.util.*;
import java.io.*;

public class Checker {
	// グローバル変数
	ArrayList<String> gyou = new ArrayList<String>();
	ArrayList<String> program = new ArrayList<String>();
	ArrayList<String> token = new ArrayList<String>();
	ArrayList<String> id = new ArrayList<String>();
	ArrayList<String> line = new ArrayList<String>();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> globalname = new ArrayList<String>();
	ArrayList<String> globaltype = new ArrayList<String>();
	ArrayList<String> globallength = new ArrayList<String>();
	ArrayList<String> kariparaname = new ArrayList<String>();
	ArrayList<String> kariparatype = new ArrayList<String>();
	ArrayList<String> kariparalength = new ArrayList<String>();
	ArrayList<String> kariparaplace = new ArrayList<String>();
	ArrayList<String> Allkariparatype = new ArrayList<String>();
	ArrayList<String> Allkariparaplace = new ArrayList<String>();
	ArrayList<String> scopename = new ArrayList<String>();
	ArrayList<String> scopetype = new ArrayList<String>();
	ArrayList<String> scopelength = new ArrayList<String>();
	ArrayList<String> fukuprogram = new ArrayList<String>();
	ArrayList<String> Allvarname = new ArrayList<String>();
	ArrayList<String> Allvarplace = new ArrayList<String>();
	ArrayList<String> Allvartype = new ArrayList<String>();
	ArrayList<String> Allvarlength = new ArrayList<String>();
	ArrayList<Integer> index = new ArrayList<Integer>();
	ArrayList<String> varList = new ArrayList<String>();
	ArrayList<String> expList = new ArrayList<String>();

	String nowprogram;
	String type;
	String length;
	String exptype;
	String vartype;

	int nowline = 0;
	int semline = 0;
	boolean semflag = false;
	boolean compiler=false;
	int offset = 0;
	int enzan = -1; // 0:関係演算子 1:算術演算子 2:論理演算子

	public static void main(final String[] args) {
		// sem
		// new Checker().run("data/ts/semerr02.ts");

		/*new Checker().run("data/ts/semerr01.ts");
		new Checker().run("data/ts/semerr02.ts");
		new Checker().run("data/ts/semerr03.ts");
		new Checker().run("data/ts/semerr04.ts");
		new Checker().run("data/ts/semerr05.ts");
		new Checker().run("data/ts/semerr06.ts");
		new Checker().run("data/ts/semerr07.ts");
		new Checker().run("data/ts/semerr08.ts");*/

		// normal
		 //new Checker().run("data/ts/normal12.ts");

		/*new Checker().run("data/ts/normal01.ts");
		new Checker().run("data/ts/normal02.ts");
		new Checker().run("data/ts/normal03.ts");
		new Checker().run("data/ts/normal04.ts");
		new Checker().run("data/ts/normal05.ts");
		new Checker().run("data/ts/normal06.ts");
		new Checker().run("data/ts/normal07.ts");
		new Checker().run("data/ts/normal08.ts");
		new Checker().run("data/ts/normal09.ts");

		new Checker().run("data/ts/normal11.ts");
		new Checker().run("data/ts/normal12.ts");
		new Checker().run("data/ts/normal13.ts");
		new Checker().run("data/ts/normal14.ts");
		new Checker().run("data/ts/normal15.ts");
		new Checker().run("data/ts/normal16.ts");
		new Checker().run("data/ts/normal17.ts");
		new Checker().run("data/ts/normal18.ts");
		new Checker().run("data/ts/normal19.ts");
		new Checker().run("data/ts/normal20.ts");*/
		
		// synerr

		/*new Checker().run("data/ts/synerr01.ts");
		new Checker().run("data/ts/synerr02.ts");
		new Checker().run("data/ts/synerr03.ts");
		new Checker().run("data/ts/synerr04.ts");
		new Checker().run("data/ts/synerr05.ts");
		new Checker().run("data/ts/synerr06.ts");
		new Checker().run("data/ts/synerr07.ts");
		new Checker().run("data/ts/synerr08.ts");*/

	}

	public void run(final String inputFileName) {
		try {
			FileReader fr = new FileReader(inputFileName);
			BufferedReader br = new BufferedReader(fr);
			String lineBuffer = br.readLine();

			while (lineBuffer != null) {
				gyou.add(lineBuffer);
				 //System.out.println(lineBuffer);
				String[] str = lineBuffer.split("\t", 0);
				program.add(str[0]);
				id.add(str[2]);
				line.add(str[3]);
				lineBuffer = br.readLine();
			}

			int search = PROGRAM(0);
			
			if (search == 0 && semflag == false) {
				compiler=true;
				System.out.println("OK");
			} else if (search == 0 && semflag == true) {
				// System.out.println("nowline : " + program.get(nowline));
				compiler=false;
				System.err.println("Semantic error: line " + semline);
			} else {
				// System.out.println("nowline : " + program.get(nowline));
				compiler=false;
				System.err.println("Syntax error: line " + line.get(nowline));
			}
			br.close();

		} catch (FileNotFoundException e) {
			System.err.println("File not found");
		} catch (IOException e) {
			System.err.println("Error");
		}
	}

	public int PROGRAM(int comp) {

		if (comp != 0) {
			return -10;
		}
		comp = equal("17", comp); // program
		comp = equal("43", comp); // 名前
		if (comp == 0) {
			nowprogram = "main";
		}
		comp = equal("37", comp); // ;
		comp = BLOCK(comp, nowprogram);
		if (comp >= 0) {
			scopename.addAll(globalname);
			scopetype.addAll(globaltype);
			scopelength.addAll(globallength);
		}
		comp = FUKUGOBUN(comp, 0);
		comp = equal("42", comp); // .
		return comp;
	}

	public int BLOCK(int comp, String varplace) {
		if (comp != 0) {
			return -10;
		}
		comp = VARIABLESENGEN(comp, varplace, 0);
		comp = PROGRAM2(comp);

		return comp;
	}

	public int IOSENTENCE(int comp, int flag) {

		int lbuf = nowline;
		if (comp != 0) {
			return -10;
		} else {
			comp = equal("18", comp); // readln
			comp = equal("33", comp); // (
			comp = VARIABLE2(comp, flag);
			if (comp == 0 && flag >= 0) {
				int i = 0;
				while (i < varList.size()) {
					if (!(varList.get(i).equals("integer")) && !(varList.get(i).equals("char"))
							&& !(varList.get(i).equals("char[]"))) {
						semline = Integer.parseInt(line.get(nowline));
						// System.out.println("out26");
						semflag = true;
					}
					i++;
				}
			}
			comp = equal("34", comp); // )
			if (comp == 0) {

				return 0;
			} else {

				nowline = lbuf;
				comp = 0;
				comp = equal("23", comp); // writeln
				comp = equal("33", comp); // (
				comp = EXPRESSION2(comp, flag);
				if (comp == 0 && flag >= 0) {
					int i = 0;
					while (i < expList.size()) {
						if (!(expList.get(i).equals("integer")) && !(expList.get(i).equals("char"))
								&& !(expList.get(i).equals("char[]"))) {
							semline = Integer.parseInt(line.get(nowline));
							// System.out.println("out27");
							semflag = true;
						}
						i++;
					}
				}

				comp = equal("34", comp); // )
				return comp;
			}
		}
	}

	public int STANDARDSENTENCE(int comp, int flag) {

		if (comp != 0) {
			return -10;
		} else {
			int lbuf = nowline;
			if (ASSIGNMENTSENTENCE(comp, -1) == 0) {
				nowline = lbuf;
				comp = ASSIGNMENTSENTENCE(comp, flag);
				return comp;
			} else {
				nowline = lbuf;
				if (CALLSENTENCE(comp, -1) == 0) {
					nowline = lbuf;
					comp = CALLSENTENCE(comp, flag);
					return comp;
				} else {
					nowline = lbuf;
					if (IOSENTENCE(comp, -1) == 0) {
						nowline = lbuf;
						comp = IOSENTENCE(comp, flag);
						return comp;
					} else {
						nowline = lbuf;
						comp = FUKUGOBUN(comp, flag);
						return comp;
					}
				}
			}
		}
	}

	public int CALLSENTENCE(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			String call = "";
			comp = equal("43", comp);
			if (comp == 0 && flag >= 0 && semline == 0) {
				if (!fukuprogram.contains(program.get(nowline - 1))) {// 宣言されていない副プログラム
					semline = Integer.parseInt(line.get(nowline - 1));
					// System.out.println("out8");
					semflag = true;
				} else {
					call = program.get(nowline - 1);
				}
			}

			int lbuf = nowline;
			int compbuf = comp;

			comp = equal("33", comp);
			if (comp == 0) {
				comp = EXPRESSION2(comp, flag);
				if (comp >= 0 && flag >= 0 && semline == 0) {
					int i = 0;
					int j = Allkariparaplace.indexOf(call);
					while (i < expList.size()) {
						if (!(expList.get(i).equals(Allkariparatype.get(j)))) {// 仮パラメータと実パラメータの型の不整合
							semline = Integer.parseInt(line.get(lbuf));
							// System.out.println("out37");
							semflag = true;
						}
						i++;
						j++;
					}
				}
				comp = equal("34", comp);
				return comp;
			} else {
				nowline = lbuf;
				return compbuf;
			}

		}
	}

	public int ASSIGNMENTSENTENCE(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {

			int lbuf = nowline;
			String hensuu = "";
			comp = VARIABLE(comp, flag, 0);
			if (comp == 0 && flag >= 0 && semline == 0) {
				hensuu = vartype;

				if (scopename.contains(program.get(lbuf))) {
					int i = scopename.indexOf(program.get(lbuf));
					int max = Integer.parseInt(scopelength.get(i));
					if (max != 0 && id.get(lbuf + 1).equals("40")) {// 左辺が配列型の純変数
						semline = Integer.parseInt(line.get(lbuf));
						// System.out.println("out9");
						semflag = true;
					}
				}
			}
			comp = equal("40", comp); // :=

			comp = EXPRESSION(comp, flag);
			if (comp == 0 && flag >= 0 && semline == 0) {
				String shiki = exptype;

				if (hensuu.contains("integer") && !shiki.contains("integer")
						|| hensuu.contains("char") && !shiki.contains("char")
						|| hensuu.contains("boolean") && !shiki.contains("boolean")) {// 代入文における型の不整合(変数と式)
					semline = Integer.parseInt(line.get(lbuf));
					// System.out.println("out31");
					semflag = true;
				}
			}

			return comp;
		}
	}

	public int EXPRESSION(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			String exp1 = "";
			comp = TANJUNEXPRESSION(comp, flag);
			if (comp == 0 && flag >= 0) {
				exp1 = exptype;
			}
			int lbuf = nowline;
			if (KANKEI(comp) == 0) {
				nowline = lbuf;
				comp = KANKEI(comp);
				comp = TANJUNEXPRESSION(comp, flag);
				if (comp == 0 && flag >= 0 && semline == 0) {
					String exp2 = exptype;

					if ((exp1.contains("integer") && exp2.contains("integer"))
							|| (exp1.contains("char") && exp2.contains("char"))
							|| (exp1.contains("boolean") && exp2.contains("boolean"))) {
						exptype = "boolean";
					} else {// 関係演算子と被演算子の型の不整合エラー
						// System.out.println("out23");
						semflag = true;
						semline = Integer.parseInt(line.get(lbuf));
					}
				}
				return comp;
			} else {
				nowline = lbuf;
				return comp;
			}
		}
	}

	public int EXPRESSION2(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			expList.clear();
			comp = EXPRESSION(comp, flag);
			if (comp == 0 && flag >= 0) {
				expList.add(exptype);
			}
			int lbuf = nowline;
			while (equal("41", comp) == 0) {
				nowline = lbuf;
				comp = equal("41", comp);
				comp = EXPRESSION(comp, flag);
				if (comp == 0 && flag >= 0) {
					expList.add(exptype);
				}
				lbuf = nowline;
			}
			return comp;
		}
	}

	public int TANJUNEXPRESSION(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			String exp1 = "";
			int lbuf = nowline;

			if (FUGO(comp) == 0) {
				nowline = lbuf;
				comp = FUGO(comp);
				comp = KOU(comp, flag);
				if (comp == 0 && flag >= 0) {
					exp1 = exptype;
				}
				int lbuf2 = nowline;
				while (KAHOU(comp) == 0) {
					nowline = lbuf2;
					comp = KAHOU(comp);
					comp = KOU(comp, flag);
					if (comp == 0 && flag >= 0 && semline == 0) {
						String exp2 = exptype;

						if (enzan == 1) {
							if (exp1.equals("integer") && exp2.equals("integer")) {
								exptype = "integer";
							} else {// 算術演算子と被演算子の型の不整合エラー
								// System.out.println("out17");
								semflag = true;
								semline = Integer.parseInt(line.get(lbuf));
							}
						} else if (enzan == 2) {
							if (exp1.equals("boolean") && exp2.equals("boolean")) {
								exptype = "boolean";
							} else {// 論理演算子と被演算子の型の不整合エラー
								// System.out.println("out18");
								semflag = true;
								semline = Integer.parseInt(line.get(lbuf));
							}
						}
					}
					lbuf2 = nowline;
				}
				return comp;
			} else {
				nowline = lbuf;
				comp = KOU(comp, flag);
				if (comp == 0 && flag >= 0) {
					exp1 = exptype;
				}
				int lbuf2 = nowline;
				while (KAHOU(comp) == 0) {
					nowline = lbuf2;
					comp = KAHOU(comp);
					comp = KOU(comp, flag);
					if (comp == 0 && flag >= 0 && semline == 0) {
						String exp2 = exptype;

						if (enzan == 1) {
							if (exp1.equals("integer") && exp2.equals("integer")) {
								exptype = "integer";
							} else {// 算術演算子と被演算子の型の不整合エラー
								// System.out.println("out19");
								semflag = true;
								semline = Integer.parseInt(line.get(lbuf));
							}
						} else if (enzan == 2) {
							if (exp1.equals("boolean") && exp2.equals("boolean")) {
								exptype = "boolean";
							} else {// 論理演算子と被演算子の型の不整合エラー
								// System.out.println("out20");
								semflag = true;
								semline = Integer.parseInt(line.get(lbuf));
							}
						}
					}
					lbuf2 = nowline;
				}
				return comp;
			}

		}
	}

	public int KOU(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			String exp1 = "";
			comp = INSHI(comp, flag);
			if (comp == 0 && flag >= 0) {
				exp1 = exptype;
			}
			int lbuf = nowline;
			while (JOUHOU(comp) == 0) {
				nowline = lbuf;
				comp = JOUHOU(comp);
				comp = INSHI(comp, flag);
				if (comp == 0 && flag >= 0 && semline == 0) {
					String exp2 = exptype;
					if (enzan == 1) {
						if (exp1.equals("integer") && exp2.equals("integer")) {
							exptype = "integer";
						} else {// 算術演算子における被演算子の型エラー
							// System.out.println("out15");
							semflag = true;
							semline = Integer.parseInt(line.get(lbuf));
						}
					} else if (enzan == 2) {
						if (exp1.equals("boolean") && exp2.equals("boolean")) {
							exptype = "boolean";
						} else {// 論理演算子における被演算子の型エラー
							// System.out.println("out16");
							semflag = true;
							semline = Integer.parseInt(line.get(lbuf));
						}
					}
				}
				lbuf = nowline;
			}
			return comp;

		}
	}

	public int INSHI(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			int lbuf = nowline;
			comp = VARIABLE(comp, flag, 1);
			if (comp == 0) {
				return 0;
			} else {
				comp = 0;
				nowline = lbuf;
				comp = TEISUU(comp);
				if (comp == 0) {
					return 0;
				} else {
					comp = 0;
					nowline = lbuf;
					comp = equal("33", comp); // (
					comp = EXPRESSION(comp, flag);
					comp = equal("34", comp); // )
					if (comp == 0) {
						return 0;
					} else {
						comp = 0;
						nowline = lbuf;
						comp = equal("13", comp); // not
						comp = INSHI(comp, flag);
						if (comp == 0 && flag >= 0 && semline == 0) {
							if (!(exptype.equals("boolean"))) {// 論理演算子と被演算子の型の不整合エラー
								// System.out.println("out14");
								semflag = true;
								semline = Integer.parseInt(line.get(lbuf + 1));
							}
						}
						return comp;
					}

				}
			}
		}
	}

	public int TEISUU(int comp) {
		if (comp != 0) {
			return -10;
		} else {
			if (Integer.parseInt(id.get(nowline)) == 44) {
				if (Integer.parseInt(program.get(nowline)) >= -32768
						&& Integer.parseInt(program.get(nowline)) <= 32767) {
					exptype = "integer";
				} else {
					// System.out.println("out13")
					semflag = true;
					semline = Integer.parseInt(line.get(nowline));
				}
				nowline++;
				return 0;

			} else if (Integer.parseInt(id.get(nowline)) == 45) {
				if (program.get(nowline).startsWith("'") && program.get(nowline).endsWith("'")) {
					exptype = "char";
				} else {
					// System.out.println("out14");
					semflag = true;
					semline = Integer.parseInt(line.get(nowline));
				}
				nowline++;
				return 0;

			} else if (Integer.parseInt(id.get(nowline)) == 9 || Integer.parseInt(id.get(nowline)) == 20) {
				exptype = "boolean";
				nowline++;
				return 0;
			} else {
				return -10;
			}
		}
	}

	public int VARIABLE(int comp, int flag, int flag2) {
		if (comp != 0) {
			return -10;
		} else {

			int lbuf = nowline;
			comp = equal("43", comp); // 名前
			comp = equal("35", comp); // [
			comp = EXPRESSION(comp, flag);
			String exp1 = exptype;
			comp = equal("36", comp); // ]
			if (comp == 0) {

				if (flag >= 0 && flag2 >= 0) {
					if (!exp1.contains("integer")) {// 配列型変数の添字がinteger型でない
						// System.out.println("out28");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					} else {
						int i = scopename.indexOf(program.get(lbuf));
						if (i >= 0) {
							if (flag2 == 0) {
								vartype = scopetype.get(i);
							} else {
								exptype = scopetype.get(i);
							}
						} else {// 宣言されていない変数
							// System.out.println("out33");
							semflag = true;
							semline = Integer.parseInt(line.get(nowline));
						}
					}

				}

				return comp;
			} else {
				nowline = lbuf;
				comp = 0;
				comp = equal("43", comp); // 名前
				if (comp == 0) {
					if (flag >= 0 && flag2 >= 0) {

						int i = scopename.indexOf(program.get(lbuf));
						if (i >= 0) {
							if (scopelength.get(i) == "0") {
								if (flag2 == 0) {
									vartype = scopetype.get(i);
								} else {
									exptype = scopetype.get(i);
								}
							} else {
								if (flag2 == 0) {
									vartype = scopetype.get(i) + "[]";
								} else {
									exptype = scopetype.get(i) + "[]";
								}
							}
						} else {// 宣言されていない変数
							// System.out.println("out32");
							semflag = true;
							semline = Integer.parseInt(line.get(nowline));
						}

					}
				}

				return comp;
			}
		}

	}

	public int VARIABLE2(int comp, int flag) {
		if (comp != 0) {
			return comp;
		} else {
			varList.clear();
			comp = VARIABLE(comp, flag, 0); // 変数名
			if (comp == 0 && flag >= 0) {
				varList.add(vartype);
			}
			int lbuf = nowline;
			while ((equal("41", comp)) == 0) { // ,
				nowline = lbuf;
				comp = equal("41", comp);
				comp = VARIABLE(comp, flag, 0);// 変数名
				if (comp == 0 && flag >= 0) {
					varList.add(vartype);
				}
				lbuf = nowline;
			}

			return comp;
		}
	}

	public int SENTENCE(int comp, int flag) {

		if (comp != 0) {
			return -10;
		}

		else {
			int lbuf = nowline;
			if (equal("10", comp) == 0) {// if
				nowline = lbuf;
				comp = equal("10", comp); // if
				comp = EXPRESSION(comp, flag);
				if (comp == 0 && flag >= 0 && semline == 0) {
					if (!exptype.equals("boolean")) {// 条件式がboolean型でない
						// System.out.println("out30");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					}
				}
				comp = equal("19", comp); // then
				comp = FUKUGOBUN(comp, flag);
				int lbuf2 = nowline;
				if (equal("7", comp) == 0) {
					nowline = lbuf2;
					comp = equal("7", comp); // else
					comp = FUKUGOBUN(comp, flag);
					return comp;
				}
				return comp;
			} else {
				nowline = lbuf;
				if (equal("22", comp) == 0) {// while
					nowline = lbuf;
					comp = equal("22", comp); // while
					comp = EXPRESSION(comp, flag);
					if (comp == 0 && flag >= 0 && semline == 0) {
						if (!exptype.equals("boolean")) {// 条件式がboolean型でない
							// System.out.println("out30");
							semflag = true;
							semline = Integer.parseInt(line.get(nowline));
						}
					}
					comp = equal("6", comp); // do
					comp = FUKUGOBUN(comp, flag);
					return comp;
				} else {
					nowline = lbuf;
					comp = STANDARDSENTENCE(comp, flag);
					return comp;
				}
			}

		}

	}

	public int SENTENCE2(int comp, int flag) {

		if (comp != 0) {
			return -10;
		}

		comp = SENTENCE(comp, flag);

		comp = equal("37", comp); // ;
		int lbuf = nowline;

		while ((SENTENCE(comp, -1)) == 0) {

			nowline = lbuf;
			comp = SENTENCE(comp, flag);
			comp = equal("37", comp);
			lbuf = nowline;
		}

		return comp;

	}

	public int FUKUGOBUN(int comp, int flag) {
		if (comp != 0) {
			return -10;
		}

		comp = equal("2", comp); // begin
		comp = SENTENCE2(comp, flag);
		comp = equal("8", comp); // end

		return comp;
	}

	public int VARIABLESENGEN(int comp, String varplace, int flag) {

		if (comp != 0) {
			return -10;
		} else {

			if (comp == 0 && flag >= 0) {
				scopename.addAll(globalname);// 参照できる変数を追加(グローバル変数)
				scopetype.addAll(globaltype);
				scopelength.addAll(globallength);

				if (flag == 1) {
					int i = 0;
					while (i < kariparaname.size()) {// 参照できる変数を追加(仮パラメーター)

						if (!scopename.contains(kariparaname.get(i))) {
							scopename.add(kariparaname.get(i));
							scopetype.add(kariparatype.get(i));
							scopelength.add(kariparalength.get(i));
						} else {
							int j = scopename.indexOf(kariparaname.get(i));
							scopetype.set(j, kariparatype.get(i));
							scopelength.set(j, kariparalength.get(i));
						}
						Allvarname.add(kariparaname.get(i));
						Allvarplace.add(varplace);
						Allvarlength.add(kariparalength.get(i));
						Allvartype.add(kariparatype.get(i));
						index.add(offset);
						if(kariparalength.get(i)!="0") {	
							offset+=Integer.parseInt(kariparalength.get(i));
						}else {
							offset+=1;
						}
						i++;
					}
				}
			}

			int lbuf = nowline;

			comp = equal("21", comp); // var
			if (comp == 0) {
				comp = VARIABLESENGEN2(comp, varplace, flag);
				return comp;
			} else {
				nowline = lbuf;
				return 0;
			}
		}

	}

	public int VARIABLESENGEN2(int comp, String varplace, int flag) {

		if (comp != 0) {
			return -10;
		}
		comp = NAMESENGEN(comp, varplace, 0);
		comp = equal("38", comp); // :
		comp = TYPE(comp);

		if (comp == 0 && flag == 0) {
			int i = 0;
			while (i < name.size()) {

				if (globalname.contains(name.get(i)) && semline == 0) {// 宣言の重複(グローバル変数同士)

					semflag = true;
					semline = Integer.parseInt(line.get(nowline));
					// System.out.println("out1");

				} else {
					globalname.add(name.get(i));
					globaltype.add(type);
					globallength.add(length);

				}
				Allvarname.add(name.get(i));
				Allvarplace.add(varplace);
				Allvartype.add(type);
				Allvarlength.add(length);
				index.add(offset);
				if(length!="0") {	
					offset+=Integer.parseInt(length);
				}else {
					offset+=1;
				}
				i++;

			}
		}

		else if (comp == 0 && flag == 1) {
			int i = 0;

			while (i < name.size()) {
				
				if (globalname.contains(name.get(i))) {// 参照できる変数の置き換え(グローバル変数→ローカル変数)
					int j = scopename.indexOf(name.get(i));
					scopetype.set(j, type);
					scopelength.set(j, length);
				} else if (scopename.contains(name.get(i)) && semline == 0) {// 宣言の重複(ローカル変数同士)(ローカル変数と仮パラメータ名)
					// System.out.println("out2");
					semflag = true;
					semline = Integer.parseInt(line.get(nowline));
				} else {
					scopename.add(name.get(i));
					scopetype.add(type);
					scopelength.add(length);
				}
				Allvarname.add(name.get(i));
				Allvarplace.add(varplace);
				Allvartype.add(type);
				Allvarlength.add(length);
				index.add(offset);
				if(length!="0") {	
					offset+=Integer.parseInt(length);
				}else {
					offset+=1;
				}
				i++;

			}

		}
		comp = equal("37", comp); // ;

		int lbuf = nowline;
		while (NAMESENGEN(comp, varplace, -1) == 0) {

			nowline = lbuf;
			comp = NAMESENGEN(comp, varplace, flag);
			comp = equal("38", comp); // :
			comp = TYPE(comp);
			if (comp == 0 && flag == 0) {
				int i = 0;
				while (i < name.size()) {
					if (globalname.contains(name.get(i)) && semline == 0) {// 宣言の重複(グローバル変数同士)
						// System.out.println("out3");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline - 1));
					} else {
						globalname.add(name.get(i));
						globaltype.add(type);
						globallength.add(length);
						Allvarname.add(name.get(i));
						Allvarplace.add(varplace);
						Allvartype.add(type);
						Allvarlength.add(length);
						index.add(offset);
						if(length!="0") {	
							offset+=Integer.parseInt(length);
						}else {
							offset+=1;
						}
					}
					i++;
				}
			} else if (comp == 0 && flag == 1) {
				int i = 0;

				while (i < name.size()) {
					if (scopename.contains(name.get(i)) && semline == 0) {// 宣言の重複(ローカル変数同士とローカル変数と仮パラメータ名)
						// System.out.println("out4");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					} else {
						scopename.add(name.get(i));
						scopetype.add(type);
						scopelength.add(length);
						Allvarname.add(name.get(i));
						Allvarplace.add(varplace);
						Allvartype.add(type);
						Allvarlength.add(length);
						index.add(offset);
						if(length!="0") {	
							offset+=Integer.parseInt(length);
						}else {
							offset+=1;
						}
					}
					i++;
				}

			}

			comp = equal("37", comp); // ;
			lbuf = nowline;
		}
		return comp;

	}

	public int NAMESENGEN(int comp, String varplace, int flag) {
		if (comp != 0) {
			return -10;
		}
		name.clear();
		comp = equal("43", comp); // 名前

		if (comp == 0 && flag >= 0) {
			name.add(program.get(nowline - 1));
		}
		int lbuf = nowline;
		while ((equal("41", comp)) == 0) {
			nowline = lbuf;
			comp = equal("41", comp);
			comp = equal("43", comp);

			if (comp == 0 && flag >= 0) {
				name.add(program.get(nowline - 1));

			}
			lbuf = nowline;
		}

		return comp;
	}

	public int TYPE(int comp) {
		if (comp != 0) {
			return -10;
		} else {
			length = "0";
			int lbuf = nowline;
			comp = STANDARDTYPE(comp);
			if (comp == 0) {
				return 0;
			} else {
				nowline = lbuf;
				comp = 0;
				comp = equal("1", comp); // array
				comp = equal("35", comp); // [
				comp = SEISUU(comp); // 整数
				if (comp == 0) {
					if (!vartype.equals("integer")) {
						// System.out.println("out36");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					}
				}
				comp = equal("39", comp); // ..
				comp = SEISUU(comp); // 整数
				if (comp == 0) {
					if (!vartype.equals("integer")) {
						// System.out.println("out37");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					} else {
						length = program.get(nowline - 1);
					}
				}

				comp = equal("36", comp); // ]
				comp = equal("14", comp); // of
				comp = STANDARDTYPE(comp);
				return comp;
			}

		}

	}

	public int STANDARDTYPE(int comp) {
		if (comp != 0) {
			return -10;
		} else {
			type = "";

			comp = equal("11", comp); // integer
			if (comp == 0) {
				type = program.get(nowline - 1);
				return 0;
			} else {
				comp = 0;
				comp = equal("4", comp); // char
				if (comp == 0) {
					type = program.get(nowline - 1);
					return 0;
				} else {
					comp = 0;
					comp = equal("3", comp); // bool
					if (comp == 0) {
						type = program.get(nowline - 1);
					}
					return comp;
				}
			}

		}
	}

	public int SEISUU(int comp) {
		if (comp != 0) {
			return -10;
		} else {
			int lbuf = nowline;
			comp = equal("30", comp); // +
			comp = equal("44", comp); // 符号なし整数
			if (comp == 0) {
				if (Integer.parseInt(program.get(nowline - 1)) >= -32768
						&& Integer.parseInt(program.get(nowline - 1)) <= 32767) {
					vartype = "integer";
				} else {
					// System.out.println("out33");
					semflag = true;
					semline = Integer.parseInt(line.get(nowline));
				}
				return 0;
			} else {
				nowline = lbuf;
				comp = 0;
				comp = equal("31", comp); // -
				comp = equal("44", comp); // 符号なし整数
				if (comp == 0) {
					if (Integer.parseInt(program.get(nowline - 1)) >= -32768
							&& Integer.parseInt(program.get(nowline - 1)) <= 32767) {
						vartype = "integer";
					} else {
						// System.out.println("out34");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					}
					return 0;
				} else {
					nowline = lbuf;
					comp = 0;
					comp = equal("44", comp); // 符号なし整数
					if (comp == 0) {
						if (Integer.parseInt(program.get(nowline - 1)) >= -32768
								&& Integer.parseInt(program.get(nowline - 1)) <= 32767) {
							vartype = "integer";
						} else {
							/// System.out.println("out35");
							semflag = true;
							semline = Integer.parseInt(line.get(nowline));
						}
					}
					return comp;

				}

			}
		}

	}

	public int PROGRAM2(int comp) {// 副プログラム宣言郡
		if (comp != 0) {
			return -10;
		}
		int lbuf = nowline;
		while (PROGRAMSENGEN(comp, -1) == 0) {
			nowline = lbuf;
			comp = PROGRAMSENGEN(comp, 1);
			comp = equal("37", comp);
			if (comp == 0 && semline == 0) {

				kariparaname.clear();
				kariparatype.clear();
				kariparalength.clear();
				kariparaplace.clear();
				scopename.clear();
				scopetype.clear();
				scopelength.clear();
			}
			lbuf = nowline;
		}
		return comp;
	}

	public int PROGRAMSENGEN(int comp, int flag) {
		if (comp != 0) {
			return -10;
		} else {

			comp = equal("16", comp);// procedure
			comp = equal("43", comp);// 名前
			String varplace = "";
			if (comp == 0 && flag >= 0 && semline == 0) {

				if (!(fukuprogram.contains(program.get(nowline - 1)))
						&& !(globalname.contains(program.get(nowline - 1)))) {// 宣言の重複（グローバル変数と手続名）
					varplace = program.get(nowline - 1);
					fukuprogram.add(varplace);

				} else {
					// System.out.println("out11");
					semflag = true;
					semline = Integer.parseInt(line.get(nowline - 1));
				}
			}

			comp = KARIPARAMETER(comp, varplace, flag);
			comp = equal("37", comp);
			comp = VARIABLESENGEN(comp, varplace, flag);
			comp = FUKUGOBUN(comp, flag);
			return comp;
		}
	}

	public int KARIPARAMETER(int comp, String varplace, int flag) {
		if (comp != 0) {
			return -10;
		} else {
			int lbuf = nowline;
			comp = equal("33", comp);
			if (comp == 0) {

				comp = KARIPARAMETER2(comp, varplace, flag);
				comp = equal("34", comp);
				return comp;
			} else {
				nowline = lbuf;
				comp = 0;
				return comp;
			}

		}
	}

	public int KARIPARAMETER2(int comp, String varplace, int flag) {
		if (comp != 0) {
			return -10;
		} else {

			comp = NAMESENGEN(comp, varplace, 0);
			comp = equal("38", comp);
			comp = STANDARDTYPE(comp);

			if (comp == 0 && flag >= 0) {
				int i = 0;

				while (i < name.size()) {

					if (kariparaname.contains(name.get(i)) && semline == 0) {
						// System.out.println("out5");
						semflag = true;
						semline = Integer.parseInt(line.get(nowline));
					} else {
						kariparaname.add(name.get(i));
						kariparatype.add(type);
						Allkariparatype.add(type);
						kariparalength.add(length);
						kariparaplace.add(varplace);
						Allkariparaplace.add(varplace);
						i++;
					}

				}
			}

			int lbuf = nowline;
			while (equal("37", comp) == 0) {
				nowline = lbuf;
				comp = NAMESENGEN(comp, varplace, 0);
				comp = equal("38", comp);
				comp = STANDARDTYPE(comp);

				if (comp == 0 && flag >= 0) {
					int i = 0;

					while (i < name.size()) {

						if (kariparaname.contains(name.get(i)) && semline == 0) {
							// System.out.println("out6");
							semflag = true;
							semline = Integer.parseInt(line.get(nowline));
						} else {
							kariparaname.add(name.get(i));
							kariparatype.add(type);
							Allkariparatype.add(type);
							kariparalength.add(length);
							kariparaplace.add(varplace);
							Allkariparaplace.add(varplace);
							i++;
						}

					}

				}

				lbuf = nowline;
			}
			return comp;
		}
	}

	public int KANKEI(int comp) {
		if (comp != 0) {
			return -10;
		}

		if (Integer.parseInt(id.get(nowline)) >= 24 && Integer.parseInt(id.get(nowline)) <= 29) {
			enzan = 0;
			nowline++;
			return 0;
		} else {
			return -10;
		}
	}

	public int KAHOU(int comp) {
		if (comp != 0) {
			return -10;
		}
		if (Integer.parseInt(id.get(nowline)) == 30 || Integer.parseInt(id.get(nowline)) == 31) {
			enzan = 1;
			nowline++;
			return 0;
		}

		else if (Integer.parseInt(id.get(nowline)) == 15) {
			enzan = 2;
			nowline++;
			return 0;
		} else {
			return -10;
		}
	}

	public int JOUHOU(int comp) {
		if (comp != 0) {
			return -10;
		}
		if (Integer.parseInt(id.get(nowline)) == 32 || Integer.parseInt(id.get(nowline)) == 5
				|| Integer.parseInt(id.get(nowline)) == 12) {
			enzan = 1;
			nowline++;
			return 0;
		} else if (Integer.parseInt(id.get(nowline)) == 0) {
			enzan = 2;
			nowline++;
			return 0;
		} else {
			return -10;
		}
	}

	public int FUGO(int comp) {
		if (comp != 0) {
			return -10;
		}
		if (Integer.parseInt(id.get(nowline)) == 30 || Integer.parseInt(id.get(nowline)) == 31) {
			nowline++;
			return 0;
		} else {
			return -10;
		}
	}

	public int equal(String str, int comp) {
		if (comp != 0) {
			return -10;
		}

		if (id.get(nowline).equals(str)) {

			if (nowline < (id.size() - 1)) {
				nowline++;
				// System.out.println("now : " + program.get(nowline));
			}
			return 0;
		} else {
			return -10;
		}
	}

}
