package apps;

import classifier.RedesBayesianas;

import java.awt.EventQueue;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

public class App2 {

    private RedesBayesianas BN;
    private JFrame main;
    private JButton SelectB;
    private JLabel error_label;
    private File file;
    private JButton backB;
    private JLabel file_name_label;
    private JTextField file_name;
    private JLabel sample_len;
    private JLabel n_variables;
    private JButton classifyB;
    private JTextField parameters;
    private JLabel cf;
    private JScrollPane scroll;
    private JTextArea cs;
    private final JFileChooser fc = new JFileChooser();
    private final File path = new File(new File(".").getCanonicalPath());
    private JLabel instructions;

    private void components(JFrame main) {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        main.getContentPane().setLayout(null);
        main.setBounds(100,100,750,500);
        main.setLocation(dim.width/2-main.getSize().width/2, dim.height/2-main.getSize().height/2);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.getContentPane().setBackground(Color.white);
        main.setTitle("Bayesian Network Classifier App");
        main.setResizable(false);

        error_label = new JLabel();
        error_label.setBorder(new LineBorder(Color.white));
        error_label.setBackground(Color.white);
        error_label.setFont(new Font("Tahoma", Font.PLAIN, 12));
        error_label.setHorizontalAlignment(SwingConstants.CENTER);
        error_label.setBounds(270, 300, 210, 20);
        main.getContentPane().add(error_label);

        JLabel Names = new JLabel("David Fontoura - 89792  |  J. Bernardo Marrafa - 89810  |  João Melo - 89814");
        Names.setBounds(10, 436, 504, 16);
        Names.setFont(new Font("Tahoma", Font.PLAIN, 13));
        main.getContentPane().add(Names);

        JLabel AppNumber = new JLabel("AMC - App 2 ");
        AppNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        AppNumber.setBounds(572, 433, 154, 22);
        AppNumber.setFont(new Font("Tahoma", Font.PLAIN, 13));
        main.getContentPane().add(AppNumber);

        JLabel ISTLogoL = new JLabel(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/ISTLogo.png"))));
        ISTLogoL.setBounds(623, 0, 113, 136);
        main.getContentPane().add(ISTLogoL);

        backB = new JButton(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/BackButton.png"))));
        backB.setBorder(null);
        backB.setVisible(false);
        backB.setBounds(10, 10, 36, 36);
        main.getContentPane().add(backB);

        file_name_label = new JLabel("Bayesian Network's Name:");
        file_name_label.setHorizontalAlignment(SwingConstants.CENTER);
        file_name_label.setBounds(270, 25, 210, 20);
        main.getContentPane().add(file_name_label);
        file_name_label.setVisible(false);

        file_name = new JTextField();
        file_name.setFont(new Font("Tahoma", Font.PLAIN, 18));
        file_name.setBorder(new LineBorder(new Color(0, 157, 255), 1, true));
        file_name.setBackground(Color.white);
        file_name.setHorizontalAlignment(SwingConstants.CENTER);
        file_name.setBounds(270, 45, 210, 46);
        main.getContentPane().add(file_name);
        file_name.setColumns(10);
        file_name.setVisible(false);
        file_name.setEditable(false);

        sample_len = new JLabel();
        sample_len.setHorizontalAlignment(SwingConstants.CENTER);
        sample_len.setBounds(375, 102, 230, 20);
        main.getContentPane().add(sample_len);
        sample_len.setVisible(false);

        n_variables = new JLabel();
        n_variables.setHorizontalAlignment(SwingConstants.CENTER);
        n_variables.setBounds(146, 102, 230, 20);
        main.getContentPane().add(n_variables);
        n_variables.setVisible(false);

        classifyB = new JButton("Classify");
        classifyB.setContentAreaFilled(false);
        classifyB.setBorderPainted(false);
        classifyB.setOpaque(false);
        classifyB.setBorder(new LineBorder(Color.white));
        classifyB.setFont(new Font("Tahoma", Font.PLAIN, 26));
        classifyB.setBounds(529, 287, 174, 57);
        main.getContentPane().add(classifyB);
        classifyB.setVisible(false);
        classifyB.setForeground(new Color(0, 157, 255));

        parameters = new JTextField();
        parameters.setFont(new Font("Tahoma", Font.PLAIN, 16));
        parameters.setHorizontalAlignment(SwingConstants.CENTER);
        parameters.setBounds(46, 158, 657, 46);
        main.getContentPane().add(parameters);
        parameters.setColumns(10);
        parameters.setVisible(false);

        cf = new JLabel();
        cf.setFont(new Font("Tahoma", Font.PLAIN, 22));
        cf.setBounds(94, 225, 420, 57);
        main.getContentPane().add(cf);
        cf.setVisible(false);
        cf.setForeground(new Color(0, 157, 255));

        SelectB = new JButton("<HTML><center>Select Bayesian <br>Network File</center><HTML>");
        SelectB.setContentAreaFilled(false);
        SelectB.setBorderPainted(false);
        SelectB.setOpaque(false);
        SelectB.setBorder(new LineBorder(Color.white));
        SelectB.setFont(new Font("Tahoma", Font.PLAIN, 26));
        SelectB.setBounds(165, 157, 420, 170);
        main.getContentPane().add(SelectB);

        cs = new JTextArea();
        cs.setFont(new Font("Tahoma", Font.PLAIN, 13));
        cs.setBackground(Color.WHITE);
        cs.setVisible(false);
        cs.setBounds(0, 0, 122, 78);
        cs.setEditable(false);
        scroll = new JScrollPane();
        scroll.setViewportView(cs);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(94, 287, 425, 125);
        main.getContentPane().add(scroll);
        scroll.setBorder(null);
        scroll.setBackground(Color.WHITE);
        scroll.setVisible(false);
        scroll.getViewport().setBackground(Color.WHITE);

        instructions = new JLabel();
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        instructions.setBounds(270, 133, 210, 22);
        main.getContentPane().add(instructions);
        instructions.setVisible(false);
    }

    private void change12() {
        SelectB.setVisible(false);
        error_label.setText("");
        backB.setVisible(true);
        file_name_label.setVisible(true);
        file_name.setText(file.getName().substring(0,file.getName().indexOf(".")));
        file_name.setVisible(true);
        sample_len.setText("Sample length: "+BN.a.length());
        sample_len.setVisible(true);
        n_variables.setText("Number of variables: "+(BN.a.element(0).size()-1));
        n_variables.setVisible(true);
        classifyB.setVisible(true);
        parameters.setVisible(true);
        instructions.setText("Var1 , Var2 , ... , Var" + (BN.a.element(0).size()-2) + " , Var" + (BN.a.element(0).size()-1));
        instructions.setVisible(true);
        cs.setVisible(true);
        cf.setVisible(true);
        scroll.setVisible(true);
    }
    private void change21() {
        SelectB.setVisible(true);
        error_label.setText("");
        backB.setVisible(false);
        file_name_label.setVisible(false);
        file_name.setText("");
        file_name.setVisible(false);
        sample_len.setText("");
        sample_len.setVisible(false);
        n_variables.setText("");
        n_variables.setVisible(false);
        instructions.setVisible(false);
        instructions.setText("");
        classifyB.setVisible(false);
        parameters.setVisible(false);
        cs.setVisible(false);
        cf.setVisible(false);
    }

    private int[] textftoarray(JTextField text) {
        String values = text.getText().replaceAll("\\s","");
        String[] strValues = values.split(",");
        int[] intValues = new int[strValues.length];
        for(int i = 0; i < strValues.length; i++) {
            try {
                intValues[i] = Integer.parseInt(strValues[i]);
            } catch (NumberFormatException nfe) {
                error_label.setText("Use only integers as variables >:(");
            }
        }
        return intValues;
    }

    private void initialize() {
        fc.setCurrentDirectory(path);
        main = new JFrame();
        components(main);

        SelectB.addActionListener(e -> {
            if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                try {
                    BN = RedesBayesianas.openbn(file.getName());
                    change12();
                } catch (Exception e1) {
                    error_label.setText("Could not import Bayesian Network :(");
                }
            }
        });

        backB.addActionListener(e -> {
            int b = JOptionPane.showConfirmDialog(null, "Are you sure you want to go back?\nThe Bayesian Network will be lost", "", JOptionPane.YES_NO_OPTION);
            if(b == JOptionPane.YES_OPTION){
                BN = null;
                change21();
            }
        });

        classifyB.addActionListener(e -> {
            error_label.setText("");
            cf.setText("");
            cs.setText("");
            try {
                int[] x = textftoarray(parameters);
                if (x.length == BN.a.element(0).size()-1) {
                    ArrayList<Integer> v = new ArrayList<>(); for (int y : x) v.add(y);
                    ArrayList<Integer> C = new ArrayList<>(); C.add(BN.g.dim-1);
                    ArrayList<Integer> p = new ArrayList<>();
                    ArrayList<Integer> cvalues = new ArrayList<>();
                    ArrayList<Double> probs = new ArrayList<>();

                    double total = 0;

                    for (int i = 0 ; i < BN.a.domain(C) ; i++) {
                        p.clear(); p.addAll(v); p.add(i);
                        double y = BN.prob(p);
                        cvalues.add(i); probs.add(y);
                        total += y;
                    }

                    DecimalFormat d = new DecimalFormat("#.#######");

                    double maxp = Collections.max(probs);
                    int i = cvalues.get(probs.indexOf(maxp));
                    cf.setText("P(C="+i+") = "+d.format(maxp*100/total)+" %");
                    cvalues.remove(probs.indexOf(maxp)); probs.remove(maxp);

                    while (!probs.isEmpty()) {
                        maxp = Collections.max(probs);
                        i = cvalues.get(probs.indexOf(maxp));
                        cs.setText(cs.getText()+"P(C="+i+") = "+d.format(maxp*100/total)+" %\n");
                        cvalues.remove(probs.indexOf(maxp)); probs.remove(maxp);
                    }
                } else if (x.length > BN.a.element(0).size()-1) error_label.setText("Too many variables!!!");
                else error_label.setText("Not enough variables :'(");
            } catch (Exception e1) {
                error_label.setText("Could not classify :(");
            }
        });
    }

    private App2() throws IOException {
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App2 window = new App2();
                window.main.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
