package classifier;

import java.io.*;
import java.util.ArrayList;

public class RedesBayesianas implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    public GrafosOrientados g;
    public ArrayList<ArrayList<Double>> DFO;
    public Amostra a;
    public double s;

    public RedesBayesianas(GrafosOrientados g, Amostra a, double s) {
        //Método construtor que recebe um grafo orientado, um conjunto de dados e um double S e
        //retorna a rede de Bayes com as distribuições DFO amortizadas com pseudo-contagens S.
        this.g = g;
        this.a = a;
        this.s = s;
        this.DFO = new ArrayList<>();

        int m = a.length();
        int n = g.getDim() - 2;
        ArrayList<Integer> C = new ArrayList<>(1); C.add(n+1);

        for (int i = 0 ; i <= n ; i++) {

            ArrayList<Integer> I = new ArrayList<>(1); I.add(i); int Di = a.domain(I);
            ArrayList<Integer> dwc = new ArrayList<>(g.parents(i).size() + 2); dwc.add(i); dwc.addAll(g.parents(i)); dwc.addAll(C);
            ArrayList<Integer> wc = new ArrayList<>(g.parents(i).size() + 1); wc.addAll(g.parents(i)); wc.addAll(C);

            ArrayList<ArrayList<Integer>> all_comb = g.all_comb(a,dwc);

            ArrayList<Double> thetas = new ArrayList<>(a.domain(dwc));
            for (ArrayList<Integer> comb : all_comb) {
                double Tdw = a.count(dwc,comb);
                comb.remove(0);
                double Tw = a.count(wc,comb);
                thetas.add((Tdw + s) / (Tw + s * Di));
            }
            DFO.add(thetas);
        }

        ArrayList<ArrayList<Integer>> all_comb = g.all_comb(a,C);

        ArrayList<Double> thetaC = new ArrayList<>(a.domain(C));
        for (ArrayList<Integer> comb : all_comb) {
            double Tc = a.count(C,comb);
            thetaC.add(( Tc + s )/( m + s * a.domain(C) ));
        }
        DFO.add(thetaC);
    }

    public double prob(ArrayList<Integer> v) {
        //Recebe uma rede de Bayes e um vector e retorna a probabilidade desse vector.
        int n = g.getDim() - 2;
        ArrayList<Integer> C = new ArrayList<>(1); C.add(n+1);

        double Pr = DFO.get(n+1).get(v.get(n+1));
        for(int i = 0; i <= n ;i++) {
            ArrayList<Integer> dwc = new ArrayList<>(g.parents(i).size() + 2); dwc.add(i); dwc.addAll(g.parents(i)); dwc.addAll(C);
            ArrayList<Integer> val = new ArrayList<>(dwc.size()); for (int x : dwc) val.add(v.get(x));

            ArrayList<ArrayList<Integer>> all_comb = g.all_comb(a,dwc);

            Pr *= DFO.get(i).get(all_comb.indexOf(val));
        }
        return Pr;
    }

    public void savebn(String filename) throws IOException {
        FileOutputStream f = new FileOutputStream(filename);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(this);
        o.close();
        f.close();
    }

    public static RedesBayesianas openbn(String filename) throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(filename);
        ObjectInputStream o = new ObjectInputStream(f);
        RedesBayesianas BN =  (RedesBayesianas) o.readObject();
        o.close();
        f.close();
        return BN;
    }
}