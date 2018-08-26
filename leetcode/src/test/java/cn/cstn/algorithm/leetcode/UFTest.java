package cn.cstn.algorithm.leetcode;

import cn.cstn.algorithm.commons.UF;
import cn.cstn.algorithm.commons.graph.MUF;
import cn.cstn.algorithm.commons.util.BeanUtil;
import org.junit.Test;

@SuppressWarnings("all")
public class UFTest {

    @Test
    public void testUF() {
        int[][] c = {
                {1, 1, 0, 0, 1, 1, 0},
                {0, 1, 0, 0, 0, 1, 1},
                {1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
        };
        int m = c.length, n = c[0].length, r = 500000;
        int[][] a = new int[m * r][n];
        for (int k = 0; k < r; k++)
            for (int i = 0; i < m; i++)
                System.arraycopy(c[i], 0, a[m * k + i], 0, n);
        m = a.length;
        n = a[0].length;
        int[][] b = new int[m][n];

        long s = System.currentTimeMillis();
        MUF muf = MUF.connectedComponent(a);
        long e = System.currentTimeMillis();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = muf.find(i, j);
                if (a[i][j] == 0) b[i][j] = 0;
            }
        }
        /*for (int i = 0; i < m; i++)
            ArrayHelper.println(b[i]);*/

        System.out.println("(0, 0) and (2, 1) connected status: " + muf.isConnected(0, 0, 2, 1));
        System.out.println("num of connected component:" + muf.getCount() + " MUF.connectedComponent costs " + (e - s) + "ms");

        s = System.currentTimeMillis();
        UF uf = connectedComponent(a);
        e = System.currentTimeMillis();

        System.out.println("****************************************************************************");
        System.out.println("uf.find(0): " + uf.find(0));
        System.out.println("uf.isConnected(0, 1): " + uf.isConnected(0, 1));
        System.out.println("num of connected component:" + uf.getCount() + " connectedComponent costs " + (e - s) + "ms");
    }

    private MUF connectedComponent(int[][] a) {
        int m = a.length, n = a[0].length;
        MUF muf = new MUF(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    muf.deCount();
                    continue;
                }
                if (i > 0 && j > 0 && a[i - 1][j - 1] != 0) muf.union(i, j, i - 1, j - 1);
                if (i > 0 && a[i - 1][j] != 0) muf.union(i, j, i - 1, j);
                if (i > 0 && j < n - 1 && a[i - 1][j + 1] != 0) muf.union(i, j, i - 1, j + 1);
                if (j > 0 && a[i][j - 1] != 0) muf.union(i, j, i, j - 1);
                if (j < n - 1 && a[i][j + 1] != 0) muf.union(i, j, i, j + 1);
                if (i < m - 1 && j > 0 && a[i + 1][j - 1] != 0) muf.union(i, j, i + 1, j - 1);
                if (i < m - 1 && a[i + 1][j] != 0) muf.union(i, j, i + 1, j);
                if (i < m - 1 && j < n - 1 && a[i + 1][j + 1] != 0) muf.union(i, j, i + 1, j + 1);
            }
        }

        return muf;
    }

    private UF _connectedComponent(int[][] a) {
        int m = a.length, n = a[0].length, count = 0;
        UF uf = new UF(m * n);

        if (a[0][0] != 0) {
            if (n > 1 && a[0][1] != 0) uf.union(0, 1);
            if (m > 1 && a[1][0] != 0) uf.union(0, n);
            if (m > 1 && n > 1 && a[1][1] != 0) uf.union(0, n + 1);
        } else count++;
        if (n > 1)
            if (a[0][n - 1] != 0) {
                if (a[0][n - 2] != 0) uf.union(n - 1, n - 2);
                if (m > 1 && a[1][n - 2] != 0) uf.union(n - 1, 2 * n - 2);
                if (m > 1 && a[1][n - 1] != 0) uf.union(n - 1, 2 * n - 1);
            } else count++;
        if (m > 1)
            if (a[m - 1][0] != 0) {
                if (a[m - 2][0] != 0) uf.union((m - 1) * n, (m - 2) * n);
                if (n > 1 && a[m - 2][1] != 0) uf.union((m - 1) * n, (m - 2) * n + 1);
                if (n > 1 && a[m - 1][1] != 0) uf.union((m - 1) * n, (m - 1) * n + 1);
            } else count++;
        if (m > 1 && n > 1)
            if (a[m - 1][n - 1] != 0) {
                if (a[m - 2][n - 2] != 0) uf.union(m * n - 1, (m - 1) * n - 2);
                if (a[m - 2][n - 1] != 0) uf.union(m * n - 1, (m - 1) * n - 1);
                if (a[m - 1][n - 2] != 0) uf.union(m * n - 1, m * n - 2);
            } else count++;

        for (int i = 1; i < m - 1; i++) {
            if (a[i][0] != 0) {
                if (a[i - 1][0] != 0) uf.union(i * n, (i - 1) * n);
                if (n > 1 && a[i - 1][1] != 0) uf.union(i * n, (i - 1) * n + 1);
                if (n > 1 && a[i][1] != 0) uf.union(i * n, i * n + 1);
                if (a[i + 1][0] != 0) uf.union(i * n, (i + 1) * n);
                if (n > 1 && a[i + 1][1] != 0) uf.union(i * n, (i + 1) * n + 1);
            } else count++;
            if (n > 1)
                if (a[i][n - 1] != 0) {
                    if (a[i - 1][n - 2] != 0) uf.union((i + 1) * n - 1, i * n - 2);
                    if (a[i - 1][n - 1] != 0) uf.union((i + 1) * n - 1, i * n - 1);
                    if (a[i][n - 2] != 0) uf.union((i + 1) * n - 1, (i + 1) * n - 2);
                    if (a[i + 1][n - 2] != 0) uf.union((i + 1) * n - 1, (i + 2) * n - 2);
                    if (a[i + 1][n - 1] != 0) uf.union((i + 1) * n - 1, (i + 2) * n - 1);
                } else count++;
        }
        for (int j = 1; j < n - 1; j++) {
            if (a[0][j] != 0) {
                if (a[0][j - 1] != 0) uf.union(j, j - 1);
                if (a[0][j + 1] != 0) uf.union(j, j + 1);
                if (m > 1 && a[1][j - 1] != 0) uf.union(j, n + j - 1);
                if (m > 1 && a[1][j] != 0) uf.union(j, n + j);
                if (m > 1 && a[1][j + 1] != 0) uf.union(j, n + j + 1);
            } else count++;
            if (m > 1)
                if (a[m - 1][j] != 0) {
                    if (a[m - 2][j - 1] != 0) uf.union((m - 1) * n + j, (m - 2) * n + j - 1);
                    if (a[m - 2][j] != 0) uf.union((m - 1) * n + j, (m - 2) * n + j);
                    if (a[m - 2][j + 1] != 0) uf.union((m - 1) * n + j, (m - 2) * n + j + 1);
                    if (a[m - 1][j - 1] != 0) uf.union((m - 1) * n + j, (m - 1) * n + j - 1);
                    if (a[m - 1][j + 1] != 0) uf.union((m - 1) * n + j, (m - 1) * n + j + 1);
                } else count++;
        }


        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (a[i][j] == 0) {
                    count++;
                    continue;
                }
                if (a[i - 1][j - 1] != 0) uf.union((i - 1) * n + j - 1, i * n + j);
                if (a[i - 1][j] != 0) uf.union((i - 1) * n + j, i * n + j);
                if (a[i - 1][j + 1] != 0) uf.union((i - 1) * n + j + 1, i * n + j);
                if (a[i][j - 1] != 0) uf.union(i * n + j - 1, i * n + j);
                if (a[i][j + 1] != 0) uf.union(i * n + j + 1, i * n + j);
                if (a[i + 1][j - 1] != 0) uf.union((i + 1) * n + j - 1, i * n + j);
                if (a[i + 1][j] != 0) uf.union((i + 1) * n + j, i * n + j);
                if (a[i + 1][j + 1] != 0) uf.union((i + 1) * n + j + 1, i * n + j);
            }
        }

        BeanUtil.setFieldValue(uf, "count", uf.getCount() - count);

        return uf;
    }

}
