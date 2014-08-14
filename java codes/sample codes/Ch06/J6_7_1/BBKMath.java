class BBKMath {
    void GenRnd( int[] x, int min, int max, int rnd_no) {  
        int tot_no, rem_no, t_no;
        int i, j;
        tot_no = max - min + 1;
        int[] t = new int[tot_no];
        for(i = 0; i < tot_no; i++) {
            t[i] = min + i;
        }
        rem_no = tot_no;
        for( i = 0; i < rnd_no; i++) {
            t_no = (int)Math.floor(Math.random() * rem_no);
            x[i] = t[t_no];
            for(j = t_no; j < (rem_no - 1); j++) {
                t[j] = t[j+1];
            }
            rem_no--;
        }
    }
}

