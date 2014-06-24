# study_marking_201403

##　問題

ファイルから勤怠データを読み込み勤務時間を求める

2013年11月度課題と同様だが、今回はファイル記載形式を変更する。  
その他仕様は前回と同様とする。  
なお、勤務時間計算のためのコードは前回のものを使用して良い。  

### ■Lv.1

ファイルには以下の形式で記述されているのとする。

例)
[{ 
  "date" : "20140101",   
  "start" : "0900",  
  "end" : "1800"  
},  
{     
  "date" : "20140102",   
  "start" : "0900",  
  "end" : "1830"  
},  
{   
  "date" : "20140103",   
  "start" : "0830",  
  "end" : "1930"  
},...]  


[S] Java -----------------------------------------------------  
package jp.ktsystem.kadai201403.xxxx // xxxxの部分は受講者のドメインアカウント名  
public class Kadai {  
    public static void parseWorkTimeData(String anInputPath, String anOutputPath) throws KadaiException;  
}  
[E] Java -----------------------------------------------------  

Lv.2対象者はLv.1に加えて以下も実装すること。  
### ■Lv.2   
ファイルには以下の形式で記述されているのとする。  

例)  
{  
  "201401": [ { "20140101" : { "start":"0900", "end":"1800" }, "20140102" : { "start":"0900", "end":"1800" },... } ],  
  "201402": [ { "20140201" : { "start":"0900", "end":"1800" }, "20140202" : { "start":"0900", "end":"1800" },... } ],  
        .  
	.  
	.  
}  

・ファイルには"yyyyMMdd"の部分を日付として出力する  
・日付でソート(昇順)されて出力されていること(Lv2のみ)  
・日付の値が重複する場合はエラーとすること(エラーコード:12)(Lv2のみ)  
・先頭の"yyyyMM"をファイル名とし、年月ごとにファイル出力(※フォーマットは共通仕様参照)すること  
  
[S] Java -----------------------------------------------------  
package jp.ktsystem.kadai201403.xxxx // xxxxの部分は受講者のドメインアカウント名  
public class Kadai {  
    public static void parseWorkTimeDataLv2(String anInputPath, String anOutputPath) throws KadaiException;  
}  
[E] Java -----------------------------------------------------  
  
#### ■共通仕様
・keyに"date"(Lv,1のみ), "start", "end"以外があった場合はエラーとする(エラーコード:2)  
・ファイル内に制御文字（改行、タブ、スペースを除く）が含まれる場合はエラーとする(エラーコード:11)  
・値に「null」とある場合はnullの場合として扱う(もちろんこの場合はエラー)  
・ファイル出力フォーマットは以下とする  
----------ここから----------------------------------  
[  
{"date":"20140101","workTime":480,"total":480},  
{"date":"20140102","workTime":480,"total":960},  
	.  
	.  
	.  
]  
----------ここまで----------------------------------  
  
### ■前回仕様(Lv.2)＋α  

日付(yyyyMMdd)と出社時刻、退社時刻(カンマ区切り)を記録したファイルを読み込み、  
「日付」、「その日の勤務時間」、「そこまでの勤務時間の累計」をファイルに出力。  
エラー発生時は途中打ち切りで、そこまでのデータがファイルに出力されていること。  

出社時刻：HHmm形式の文字列  
退社時刻：HHmm形式の文字列  
勤務時間：分単位で出力  
　
・出社時刻は0900以前が入力されていたら0900として扱う  
・出社時刻は2359を超えてはいけない  
・日をまたいだ退社時刻の場合は2400+退社時刻を入力する(※)。ただし、次の日の0859を超えてはいけない  
　※例：退社が午前1時の場合は2500  
・次の時間帯は休憩時間として勤務時間に含めない  
　　　1200-1300,1800-1830  
・ファイル出力は「上書き」とする。  

エラーの通知は例外をスローすることで行うこと。  

[S] Java -----------------------------------------------------  
package jp.ktsystem.kadai201403.xxxx // xxxxの部分は受講者のドメインアカウント名  
public class Kadai {  
    public static void calcWorkTimeForFile(String anInputPath, String anOutputPath) throws KadaiException;  
}  
[E] Java -----------------------------------------------------  
  
#### エラーコード
1：入力文字列がnull、または空文字  
2：入力文字列が不正  
3：入力文字列が出社時刻、退社時刻として不正  
4：退社時刻が出社時刻以前  
5：データのカラム数が不正  
6：入力ファイルのパスがnullまたは空文字  
7：入力ファイルが存在しない  
8：入力ファイルの読み込みに失敗した  
9：出力ファイルのパスがnullまたは空文字  
10：ファイルの出力に失敗した  
