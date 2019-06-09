package cn.cstn.algorithm.corporation.bytedance;

import cn.cstn.algorithm.commons.util.ArrayUtil;
import cn.cstn.algorithm.commons.util.CollectionUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 头条8.12             2.文章病句标识
 * description :        为了提高文章质量,每一篇文章(假设全部都是英文)都会有m名编辑进行审核,每个编辑独立工作,
 *                      会把觉得有问题的句子通过下标记录下来,比如[1,10],1表示病句的第一个字符,
 *                      10表示病句的最后一个字符。也就是从1到10这10个字符组成的句子,是有问题的。
 *                      现在需要把多名编辑有问题的句子合并起来,送给总编辑进行最终的审核。
 *                      比如编辑A指出的病句是[1,10],[32,45];B编辑指出的病句[5,16],[78,94],那么[1,10]和[5,16]
 *                      是有交叉的,可以合并成[1,16],[32,45],[78,94]
 *                      输入描述:
 *                          编辑数量m,之后每行是每个编辑的标记的下标集合,第一个和最后一个下标用英文逗号分隔,
 *                          每组下标之间用分号分隔
 *                      输出描述:
 *                          合并后的下标集合,第一个和最后一个下标用英文逗号分隔每组下标之间用分号分隔。
 *                          返回结果是从小到大递增排列
 *                      输入:
 *                          3
 *                          1,10;32,45
 *                          78,94;5,16
 *                          80,100;200,220;16,32
 *                      输出:
 *                          1,45;78,100;200,220
 *                      备注:
 *                          数据范围    m<2^16
 *                          下标数值 < 2^32
 *                          每个编辑记录的下标 < 2^16组
 * @author :            zhaohq
 * date :               2018/8/28 0028 19:10
 */
@SuppressWarnings("SuspiciousNameCombination")
public class ArticleIllFormedSent {
    public static void main(String[] args) {
        System.out.println("=====================input data=====================");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int m = Integer.parseInt(line);
        List<int[]> list = new ArrayList<>();
        List<Pair<Integer, Integer>> lts = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            line = sc.nextLine();
            String[] ls = line.split(";");
            for (String pos : ls) {
                String[] ps = pos.split(",");
                int x= Integer.parseInt(ps[0]), y = Integer.parseInt(ps[1]);
                list.add(new int[]{x, y});
                Pair<Integer, Integer> p = Pair.of(x, y);
                lts.add(p);
            }
        }

        //method1
        List<int[]> res = merge(list);
        CollectionUtil.print(res, "; ", "", "\n", ArticleIllFormedSent::print);
        //method2
        List<Pair<Integer, Integer>> mts = CollectionUtil.merge(lts);
        CollectionUtil.println(mts);
    }

    private static void print(int[] a) {
        ArrayUtil.print(a, ", ", "", "");
    }

    private static List<int[]> merge(List<int[]> list) {
        List<int[]> res = new ArrayList<>();
        list.sort((p, q) -> {
            if (p[0] > q[0]) return 1;
            else if (p[0] < q[0]) return -1;
            return 0;
        });
        System.out.println("=====================sorted list=====================");
        list.forEach(ArrayUtil::println);
        System.out.println("====================sorted output====================");

        int[] p = list.get(0);
        int s = p[0], e = p[1];
        for (int i = 1; i < list.size(); i++) {
            p = list.get(i);
            if (p[0] > e) {
                res.add(new int[]{s, e});
                s = p[0];
                e = p[1];
            }
            else e = Math.max(p[1], e);
        }
        res.add(new int[]{s, e});
        return res;
    }

}
