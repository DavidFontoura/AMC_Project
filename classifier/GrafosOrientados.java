package classifier;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GrafosOrientados implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public int dim;
    ArrayList<ArrayList<Integer>> des;

    public GrafosOrientados(int dim) {
        //Método construtor recebe um natural n e retorna o grafo com n nós e sem arestas.
        this.dim = dim;
        this.des = new ArrayList<>(dim);
        for (int i = 0 ; i < dim ; i++)
            des.add(new ArrayList<>());
    }

    public GrafosOrientados(int dim, int k) {
        this.dim = dim;
        this.des = new ArrayList<>(dim);
        for (int i = 0 ; i < dim ; i++)
            des.add(new ArrayList<>());

        Random r = new Random();
        List<Integer> perm;
        perm = IntStream.range(0, this.des.size() - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(perm);
        for (int i = 0; i < this.des.size() - 2; i++) {
            List<Integer> nodes;
            nodes = IntStream.range(i+1, this.des.size() - 1).boxed().collect(Collectors.toList());
            int rand = r.nextInt(nodes.size());
            Collections.shuffle(nodes);
            for (int j = 0; j < rand; j++)
                if (this.parents(perm.get(nodes.get(j))).size() < k)
                    this.add_edge(perm.get(i), perm.get(nodes.get(j)));
        }
    }

    public int getDim() {
        return dim;
    }

    public boolean edgeQ(int pai, int fil) {
        return des.get(fil).contains(pai);
    }

    public void add_edge(int pai, int fil) {
        //Recebe dois nós (a,b) e adiciona ao grafo uma aresta do nó a para o nó b.
        des.get(fil).add(pai);
    }

    public void remove_edge(int pai, int fil) {
        //Recebe dois nós (a,b) e retira ao grafo uma aresta do nó a para o nó b.
        des.get(fil).remove((Integer)pai);
    }

    public void invert_edge(int pai, int fil) {
        //Recebe dois nós (a,b) e inverte no grafo a aresta do nó b para o nó a.
        remove_edge(pai,fil); add_edge(fil,pai);
    }

    public boolean connected(int pai, int fil) {
        //Recebe dois nós (a,b) e verifica se há um caminho (não vazio) do nó a para o nó b.
        if (des.get(fil).contains(pai)) return true;
        boolean[] visited = new boolean[getDim()];
        ArrayList<Integer> queue = new ArrayList<>(); queue.add(fil);
        while (!queue.isEmpty()) {
            int auxpai = queue.remove(0);
            if (auxpai == pai) return true;
            if (!visited[auxpai]) visited[auxpai] = true;
            queue.addAll(parents(auxpai));
        }
        return false;
    }

    public ArrayList<Integer> parents(int fil) {
        //Recebe um nó e retorna a lista de nós que são pais do nó.
        return des.get(fil);
    }

    public ArrayList<ArrayList<Integer>> all_comb(Amostra a, ArrayList<Integer> positions) {

        ArrayList<Integer> domains = new ArrayList<>();
        for (int j : positions) {
            ArrayList<Integer> aux = new ArrayList<>(); aux.add(j);
            domains.add(a.domain(aux));
        }
        int num_comb = a.domain(positions);
        ArrayList<ArrayList<Integer>> all_comb = new ArrayList<>();
        for (int k= 0 ; k < num_comb ; k++) {
            int l = k;
            ArrayList<Integer> comb = new ArrayList<>();
            for (int j = domains.size() - 1 ; j >= 0 ; j--) {
                int x = l;
                int domain = domains.get(j);
                l = l % domain;
                comb.add(0,l);
                l = (x - l) / domain;
            }
            all_comb.add(comb);
        }
        return all_comb;
    }

    public double IT(Amostra a, int i) {
        int m = a.length();
        int n = dim - 2;
        ArrayList<Integer> C = new ArrayList<>(); C.add(n+1);
        ArrayList<Integer> dwc = new ArrayList<>(); dwc.add(i); dwc.addAll(parents(i)); dwc.addAll(C);
        ArrayList<Integer> dc = new ArrayList<>(); dc.add(i); dc.addAll(C);
        ArrayList<Integer> wc = new ArrayList<>(); wc.addAll(parents(i)); wc.addAll(C);

        ArrayList<ArrayList<Integer>> all_comb = all_comb(a,dwc);

        double IT = 0;
        for (ArrayList<Integer> comb : all_comb) {

            double Tdwc = a.count(dwc,comb); if (Tdwc == 0) continue;

            int p = comb.remove(0);
            double Twc = a.count(wc,comb);

            ArrayList<Integer> comb2 = new ArrayList<>();
            comb2.add(p); comb2.add(comb.get(comb.size()-1));
            double Tdc = a.count(dc,comb2);

            comb2.remove(0);
            double Tc = a.count(C,comb2);
//			System.out.println(i + " " + (Tdwc * Math.log( (Tdwc * Tc) / (Tdc * Twc) ))/(m * Math.log(2)));
            IT += (Tdwc * Math.log( (Tdwc * Tc) / (Tdc * Twc) ))/(m * Math.log(2));
        }
        return IT;
    }

    public double MDL(Amostra a) {
        //Recebe uma amostra e retorna o MDL score da amostra.
        int m = a.length();
        int n = dim - 2;
        ArrayList<Integer> C = new ArrayList<>(); C.add(n+1);

        //LL
        double ITsum = 0;
        for (int i = 0 ; i <= n ; i++) {
            ITsum += IT(a,i);
//			System.out.println(i+" "+IT(a,i));
        }
        double LL = m * ITsum;

        //Theta
        int theta_sum = 0;
        int domainc = a.domain(C);
        for (int i = 0; i <= n ; i++) {
            ArrayList<Integer> filp = new ArrayList<>(); filp.add(i);
            theta_sum += (a.domain(filp) - 1) * a.domain(parents(i)) * domainc;
        }
        int theta = domainc - 1 + theta_sum;

        //MDL
        return - LL + theta * Math.log(m) / Math.log(2) / 2;
    }

    public boolean MDL_delta(Amostra a, int pai, int fil, int opr) {
        //Current for boolean MDLdelta6. Used on GHC.
        int m = a.length();
        double LL_delta;
        double theta_delta;

        //LL
        double IT = IT(a,fil);

        //Theta
        ArrayList<Integer> C = new ArrayList<>(); C.add(dim - 1);
        ArrayList<Integer> filp = new ArrayList<>(); filp.add(fil);
        int wdomain_fil = a.domain(parents(fil));

        switch (opr) {
            case 0 -> {                                            //remover aresta

                remove_edge(pai, fil);

                //LL
                double new_IT0 = IT(a, fil);
                LL_delta = m * (new_IT0 - IT);

                //Theta
                int new_wdomain0 = a.domain(parents(fil));
                theta_delta = (a.domain(filp) - 1) * a.domain(C) * (new_wdomain0 - wdomain_fil);
                if (-LL_delta + theta_delta * Math.log(m) / Math.log(2) / 2 < 0) break;
                add_edge(pai, fil);
                return false;
            }
            case 1 -> {                                            //inverter aresta

                //LL
                double IT_pai = IT(a, pai);

                //Theta
                ArrayList<Integer> paip = new ArrayList<>();
                paip.add(pai);
                int wdomain_pai = a.domain(parents(pai));
                invert_edge(pai, fil);

                //LL
                double new_IT_fil = IT(a, fil);
                double new_IT_pai = IT(a, pai);
                LL_delta = m * (new_IT_fil - IT + new_IT_pai - IT_pai);

                //Theta
                int new_wdomain_pai = a.domain(parents(pai));
                int new_wdomain_fil = a.domain(parents(fil));
                theta_delta = ((a.domain(filp) - 1) * (new_wdomain_fil - wdomain_fil) +
                        (a.domain(paip) - 1) * (new_wdomain_pai - wdomain_pai)) * a.domain(C);
                if (-LL_delta + theta_delta * Math.log(m) / Math.log(2) / 2 < 0) break;
                invert_edge(fil, pai);
                return false;
            }
            case 2 -> {                                            //adicionar aresta

                add_edge(pai, fil);

                //LL
                double new_IT2 = IT(a, fil);
                LL_delta = m * (new_IT2 - IT);

                //Theta
                int new_wdomain2 = a.domain(parents(fil));
                theta_delta = (a.domain(filp) - 1) * a.domain(C) * (new_wdomain2 - wdomain_fil);
                if (-LL_delta + theta_delta * Math.log(m) / Math.log(2) / 2 < 0) break;
                remove_edge(pai, fil);
                return false;
            }
        }
        return true;
    }

    public void GHC(Amostra a, int k) {
        int changed;
        do {
            changed = 0;
            for (int i = 0 ; i < dim - 1 ; i++) {
                for (int j = i + 1 ; j < dim - 1 ; j++) {
                    //TODO: optimise by list of parents and list of non-parents, instead of using edgeQ
                    boolean m = true;
                    boolean n = true;
                    boolean o = true;

                    if (edgeQ(i,j) && MDL_delta(a,i,j,0)) {
                        changed += 1;
                        m = false;
                    }

                    if (edgeQ(j,i) && MDL_delta(a,j,i,0)) {
                        changed += 1;
                        n = false;
                    }

                    if (edgeQ(i, j)) {
                        remove_edge(i, j);
                        if (!connected(i, j)) {
                            add_edge(i, j);
                            if (MDL_delta(a, i, j, 1)) {
                                changed += 1;
                                o = false;
                            }
                        } else {
                            add_edge(i, j);
                        }
                    }

                    if (o && edgeQ(j,i)) {
                        remove_edge(j,i);
                        if (!connected(j,i)) {
                            add_edge(j,i);
                            if (MDL_delta(a,j,i,1)) {
                                changed += 1;
                            }
                        } else add_edge(j,i);
                    }

                    if (m && !edgeQ(i,j) && !connected(j,i) && parents(j).size() < k && MDL_delta(a,i,j,2)) {
                        changed += 1;

                    }

                    if (n && !edgeQ(j,i) && !connected(i,j) && parents(i).size() < k && MDL_delta(a,j,i,2)) {
                        changed += 1;
                    }
                }
            }
        } while (changed > 0);
    }
}