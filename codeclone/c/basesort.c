#include  <stdio.h>

void RadixSort(int x[ ], int n, int r);
void ShowData(int x[ ], int n);
void main(void);

#define NUM_DATA 10

static int rad[NUM_DATA];    /* 基数をしまう配列  */
static int y[NUM_DATA];      /* 作業用配列 */

/* 基数ソートを行う */
void RadixSort(int x[ ], int n, int r)  /* x[ ]：ソートするデータ */
{                                       /* n：データの数 */
    int i, j, k;                        /* r：基数を取り出す最大値 */
    int m = 1;                          /* 基数を取り出す桁 */

    while (m <= r) {
        for (i = 0; i < n; i++)
            rad[i] = (x[i] / m) % 10;   /* 基数を取り出し rad[i] に保存 */

        k = 0;                          /* 配列の添字として使う */
        for (i = 0; i <= 9; i++)        /* 基数は 0 から 9 */
            for (j = 0; j < n; j++)
                if (rad[j] == i)        /* 基数の小さいものから */
                    y[k++] = x[j];      /* y[ ] にコピー */

        for (i = 0; i < n; i++)
            x[i] = y[i];                /* x[ ] にコピーし直す */

          /*  途中経過を表示  */
        printf("\nソート中:\n");
        ShowData(x, n);

        m *= 10;                        /*  基数を取り出す桁を一つ上げる */
    }
}

  /* n 個のデータを表示する */
void ShowData(int x[ ], int n)
{
    int i;

    for (i = 0; i < n ; i++)
        printf("%d\t", x[i]);
}

void main(void)
{      /* ソートするデータ */
    int x[NUM_DATA] = { 5489, 1473, 7853, 1058, 9448,
                        1118, 7979, 2163, 1856, 3117
    };
    int n = 10;        /* ソートするデータ数 */
    int r = 1000;      /* ソートするデータから取り出す基数の上限 */

      /* ソート前のデータを表示 */
    printf("ソート前:\n");
    ShowData(x, n);
    printf("\n");

    RadixSort(x, n, r);

      /* ソート後のデータを表示 */
    printf("\n\nソート後:\n");
    ShowData(x, n);
}
