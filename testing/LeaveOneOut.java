package testing;

import utils.ListNode;
import classifier.Amostra;
import classifier.GrafosOrientados;
import classifier.RedesBayesianas;
import utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;



public class LeaveOneOut {

    static class AmostraOneOut extends Amostra {

        public ArrayList<Integer> v;

        public AmostraOneOut(String filename, int i, int m) throws Exception {
            BufferedReader br;
            br = new BufferedReader(new FileReader(filename));
            String line;
            String csvSplit = ",";

            this.first = null;

            for (int j = 0; j < i; j++) {
                line = br.readLine();
                String[] vals = line.split(csvSplit);
                ArrayList<Integer> valint = new ArrayList<>(vals.length);
                for (String val : vals) valint.add(Integer.parseInt(val));
                add(valint);
            }

            line = br.readLine();
            String[] vals = line.split(csvSplit);
            v = new ArrayList<>(vals.length);
            for (String val : vals) v.add(Integer.parseInt(val));

            for (int j = i + 1; j < m; j++) {
                line = br.readLine();
                vals = line.split(csvSplit);
                ArrayList<Integer> valint = new ArrayList<>(vals.length);
                for (String val : vals) valint.add(Integer.parseInt(val));
                add(valint);
            }

            add(v);
            for (int l = 0; l < this.first.V.size(); l++) {
                int domain = 1;
                ListNode node = this.first;
                int auxdomain = node.V.get(l);
                for (node = node.next; node.next != null; node = node.next)
                    if (node.V.get(l) > auxdomain) auxdomain = node.V.get(l);
                domain *= (auxdomain + 1);
                domains.add(domain);
            }
            remove();
        }

        public void remove() {
            first = first.next;
            len--;
        }
    }

    public static boolean prob(RedesBayesianas BN, ArrayList<Integer> values) {
        ArrayList<Integer> C = new ArrayList<>(); C.add(BN.g.dim-1);
        ArrayList<Integer> p = new ArrayList<>();
        ArrayList<Integer> cvalues = new ArrayList<>();
        ArrayList<Double> probs = new ArrayList<>();
        int c = values.remove(values.size()-1);
        for (int i = 0 ; i < BN.a.domain(C) ; i++) {
            p.clear(); p.addAll(values); p.add(i);
            cvalues.add(i); probs.add(BN.prob(p));
        }

        return cvalues.get(probs.indexOf(Collections.max(probs))) == c;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br;
        GrafosOrientados g;
        RedesBayesianas BN;
        int pais = 4;
        double S = 0.5;
        String[] Samples = {//"data/thyroid.csv",
                            "data/hepatitis.csv",
                            //"data/diabetes.csv",
                            //"data/bcancer.csv"
                            };
        for (String s : Samples) {
            long startTime = System.nanoTime();
            br = new BufferedReader(new FileReader(s));
            int m = 0;
            while (br.readLine() != null ) m++;

            double correct = 0;
            for (int i = 0 ; i < m ; i++) {
                AmostraOneOut a = new AmostraOneOut(s,i,m);
                g = new GrafosOrientados(a.element(0).size(), pais);
                g.GHC(a,pais);
                BN = new RedesBayesianas(g,a,S);
                if (prob(BN, a.v)) correct++;
            }
            long endTime = System.nanoTime();
            System.out.println("%("+s+") = "+correct*100/m+"%");
            System.out.println("	Ended in "+ Utils.totime((endTime-startTime)/1000000));
        }
    }
}
