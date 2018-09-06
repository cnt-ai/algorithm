package cn.cstn.algorithm.corporation.bytedance;

import cn.cstn.algorithm.commons.util.StringUtil;

import java.util.Scanner;

/**
 * 头条8.25             3. 双生词
 * description :        双生词是指满足如下条件的两个字符串：（假设两个字符串分别为S和S`）
 *                          1.字符串S与S`长度相同
 *                          2.将字符串S首尾相接绕成环，再选一个位置切开，顺时针或逆时针能够得到字符串S`
 *                      容易得到，若S与S`为双生词，则S`与S也为双生词。
 *                      给定一批仅由英文小写字母组成的字符串，询问他们之中是否存在双生词。
 *                      输入描述：
 *                          首先给出测试组数t，表示一共有多少组数据。
 *                          对于每组数据，第一行为一个整数n，表示一共有多少个字符串。接下来n行，每行一
 *                          个字符串。
 *                      输出描述：
 *                          对于每组数据，若存在双生词，输出Yeah。若不存在双生词，输出Sad。
 *                      示例1：
 *                          输入：
 *                          3
 *                          2
 *                          helloworld
 *                          hdlrowolle
 *                          2
 *                          helloworld
 *                          worldhello
 *                          2
 *                          abcde
 *                          acbde
 *                          输出：
 *                          Yeah
 *                          Yeah
 *                          Sad
 *                      备注：
 *                          对于40%的数据，n<1000
 *                          1<=t<=10,n<100000,字符串长度在1-32之间。
 * @author :            zhaohq
 * date :               2018/9/5 0005 19:57
 */
public class PairsOfWords {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            String[] ss = new String[n];
            for (int j = 0; j < n; j++)
                ss[j] = sc.next();
            System.out.println(existPairOfWords(ss));
        }
    }

    private static String existPairOfWords(String[] ss) {
        for (int i = 0; i < ss.length - 1; i++)
            for (int j = i + 1; j < ss.length; j++)
                if (StringUtil.isPairOfWords(ss[i], ss[j])) return "Yeah";
        return "Sad";
    }
}
