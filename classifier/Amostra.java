package classifier;

import utils.ListNode;

import java.io.*;
import java.util.ArrayList;

public class Amostra implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public int len;
    public ListNode first;
    public ArrayList<Integer> domains = new ArrayList<>();

    public Amostra() {
        this.len = 0;
        this.first = null;
    }

    public Amostra(String filename) {

        BufferedReader br;
        String line;
        String csvSplit   = ",";
        try {
            br = new BufferedReader(new FileReader(filename));
            try {
                this.first = null;
                int m = 0;
                while ( (line = br.readLine()) != null ) {
                    String [] vals = line.split(csvSplit);
                    ArrayList<Integer> valint = new ArrayList<>(vals.length);
                    for (int i = 0; i < vals.length; i++) {
                        valint.add(i,Integer.parseInt(vals[i]));
                    }
                    add(valint);
                    m++;
                }
                this.len = m;
                for (int i = 0; i < first.V.size() ; i++) {
                    int domain = 1;
                    ListNode node = first;
                    int auxdomain = node.V.get(i);
                    for (node = node.next ; node.next != null; node = node.next)
                        if (node.V.get(i) > auxdomain) auxdomain = node.V.get(i);
                    domain *= (auxdomain+1);
                    domains.add(domain);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void add(ArrayList<Integer> x) {
        // Recebe um vector e acrescenta o vector à amostra.
        first = new ListNode(x,first); len++;
    }

    public int length() {
        // Retorna o comprimento da amostra.
        return this.len;
    }

    public ArrayList<Integer> element(int k) {
        //Recebe uma posição e retorna o vector da amostra.
        int i = 0; ListNode node;
        for(node = first ; i < k ; node = node.next) i++;
        return node.V;
    }

    public int domain(ArrayList<Integer> position) {
        //Recebe uma amostra e um vector de posições e retorna o número de elementos possíveis desse vector de posições.
        int domain = 1;
        for (int p : position) {
            domain *= domains.get(p);
        }
        return domain;
    }

    public int count(ArrayList<Integer> variables, ArrayList<Integer> values) {
        //Recebe um vector de variáveis e um vector de valores e retorna o n. de ocorrências desses valores para essas variáveis na amostra.
        int count = 0;
        for (ListNode node = first ; node != null ; node = node.next) {
            boolean diff = false;
            for (int i = 0; i < variables.size(); i++)
                if (!values.get(i).equals(node.V.get(variables.get(i)))) {
                    diff = true;
                    break;
                }
            if (!diff) count += 1;
        }
        return count;
    }
}