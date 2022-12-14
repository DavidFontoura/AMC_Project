package apps;

import classifier.Amostra;
import classifier.GrafosOrientados;
import classifier.RedesBayesianas;
import utils.Utils;

import java.awt.EventQueue;
import java.awt.Dimension;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class App1 implements ActionListener, PropertyChangeListener {

    private Amostra a;
    private GrafosOrientados g;
    private JFrame main;
    private JButton SelectB;
    private JLabel error_label;
    private File file;
    private JButton backB;
    private JLabel file_name_label;
    private JTextField file_name;
    private String file_name_v;
    private JLabel sample_len;
    private JLabel n_variables;
    private JLabel n_parents_label;
    private JSpinner n_parents;
    private int n_parents_v;
    private JLabel n_graphs_label;
    private JTextField n_graphs;
    private int n_graphs_v;
    private JButton BNG;
    private long elapsed;
    private JProgressBar bar;
    private JButton backB2;
    private JLabel time_label;
    private JLabel MDL;
    private JLabel end;

    class Task extends SwingWorker<Void, Void> {
        public Void doInBackground() {
            setProgress(0);
            long startTime = System.nanoTime();

            int n = a.element(0).size();
            ArrayList<Double> MDLs = new ArrayList<>();
            ArrayList<GrafosOrientados> gs = new ArrayList<>();

            g = new GrafosOrientados(n); g.GHC(a,n_parents_v); gs.add(g);
            double MDL1 = g.MDL(a); MDLs.add(MDL1);
            setProgress(0);
            for (int i = 1 ; i < n_graphs_v ; i++) {
                setProgress(i*100/n_graphs_v);
                g = new GrafosOrientados(n, n_parents_v); g.GHC(a,n_parents_v); gs.add(g);
                double MDLi = g.MDL(a); MDLs.add(MDLi);
            }
            g = gs.get(MDLs.indexOf(Collections.min(MDLs)));
            RedesBayesianas BN = new RedesBayesianas(g, a, 0.5);

            long endTime = System.nanoTime();
            setProgress(100);

            elapsed = (endTime-startTime)/1000000;

            try {
                BN.savebn(file_name_v + "_" + n_parents_v +".bnf");
            } catch (IOException e) {
                error_label.setText("Could not create Bayesian Network file :(");
            }
            return null;
        }

        public void done() {
            change34();
//          Toolkit.getDefaultToolkit().beep();
            try {
                Utils.beep();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    private final JFileChooser fc = new JFileChooser();
    private final File path = new File(new File(".").getCanonicalPath());

    private void components(JFrame main) {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        main.getContentPane().setLayout(null);
        main.setBounds(100,100,750,500);
        main.setLocation(dim.width/2-main.getSize().width/2, dim.height/2-main.getSize().height/2);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.getContentPane().setBackground(Color.white);
        main.setTitle("Bayesian Network Learning App");
        main.setResizable(false);

        JLabel Names = new JLabel("David Fontoura - 89792  |  J. Bernardo Marrafa - 89810  |  João Melo - 89814");
        Names.setBounds(10, 436, 504, 16);
        Names.setFont(new Font("Tahoma", Font.PLAIN, 13));
        main.getContentPane().add(Names);

        JLabel AppNumber = new JLabel("AMC - App 1 ");
        AppNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        AppNumber.setBounds(572, 433, 154, 22);
        AppNumber.setFont(new Font("Tahoma", Font.PLAIN, 13));
        main.getContentPane().add(AppNumber);

        JLabel ISTLogoL = new JLabel(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/ISTLogo.png"))));
        ISTLogoL.setBounds(623, 0, 113, 136);
        main.getContentPane().add(ISTLogoL);

        SelectB = new JButton("Select Sample File\r\n");
        SelectB.setContentAreaFilled(false);
        SelectB.setBorderPainted(false);
        SelectB.setOpaque(false);
        SelectB.setBorder(new LineBorder(Color.white));
        SelectB.setFont(new Font("Tahoma", Font.PLAIN, 26));
        SelectB.setBounds(165, 157, 420, 170);
        main.getContentPane().add(SelectB);

        backB = new JButton(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/BackButton.png"))));
        backB.setBorder(null);
        backB.setVisible(false);
        backB.setBounds(10, 10, 36, 36);
        backB.setVisible(false);
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

        sample_len = new JLabel("");
        sample_len.setHorizontalAlignment(SwingConstants.CENTER);
        sample_len.setBounds(375, 102, 230, 20);
        main.getContentPane().add(sample_len);
        sample_len.setVisible(false);

        n_variables = new JLabel("");
        n_variables.setHorizontalAlignment(SwingConstants.CENTER);
        n_variables.setBounds(146, 102, 230, 20);
        main.getContentPane().add(n_variables);
        n_variables.setVisible(false);

        n_parents_label = new JLabel("Maximum Number of Parents");
        n_parents_label.setHorizontalAlignment(SwingConstants.LEFT);
        n_parents_label.setFont(new Font("Tahoma", Font.PLAIN, 18));
        n_parents_label.setBounds(182, 168, 260, 40);
        main.getContentPane().add(n_parents_label);
        n_parents_label.setVisible(false);

        n_parents = new JSpinner();
        n_parents.setFont(new Font("Tahoma", Font.PLAIN, 24));
        n_parents.setBorder(new LineBorder(new Color(0, 157, 255), 1, true));
        n_parents.setBounds(452, 166, 75, 40);
        main.getContentPane().add(n_parents);
        n_parents.setVisible(false);

        n_graphs_label = new JLabel("Number of Tested Graphs");
        n_graphs_label.setHorizontalAlignment(SwingConstants.LEFT);
        n_graphs_label.setFont(new Font("Tahoma", Font.PLAIN, 18));
        n_graphs_label.setBounds(182, 219, 260, 40);
        main.getContentPane().add(n_graphs_label);
        n_graphs_label.setVisible(false);

        n_graphs = new JTextField();
        n_graphs.setFont(new Font("Tahoma", Font.PLAIN, 24));
        n_graphs.setBorder(new LineBorder(new Color(0, 157, 255), 1, true));
        n_graphs.setBounds(452, 217, 75, 40);
        main.getContentPane().add(n_graphs);
        n_graphs.setColumns(10);
        n_graphs.setVisible(false);

        BNG = new JButton("<HTML><center>Create<br>Bayesian Network</center><HTML>");
        BNG.setFont(new Font("Tahoma", Font.PLAIN, 20));
        BNG.setForeground(new Color(0, 157, 255));
        BNG.setBorderPainted(false);
        BNG.setContentAreaFilled(false);
        BNG.setOpaque(false);
        BNG.setVisible(false);
        BNG.setBounds(270, 332, 210, 50);
        main.getContentPane().add(BNG);
        BNG.addActionListener(this);

        error_label = new JLabel();
        error_label.setBorder(new LineBorder(Color.white));
        error_label.setBackground(Color.white);
        error_label.setFont(new Font("Tahoma", Font.PLAIN, 12));
        error_label.setHorizontalAlignment(SwingConstants.CENTER);
        error_label.setBounds(270, 300, 210, 20);
        main.getContentPane().add(error_label);

        bar = new JProgressBar();
        bar.setBounds(165, 220, 420, 60);
        main.getContentPane().add(bar);
        bar.setVisible(false);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setBorderPainted(false);
        bar.setForeground(new Color(0, 157, 255));
        bar.setBackground(Color.white);

        backB2 = new JButton(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/BackButton.png"))));
        backB2.setBorder(null);
        backB2.setVisible(false);
        backB2.setBounds(10, 10, 36, 36);
        backB2.setVisible(false);
        main.getContentPane().add(backB2);

        time_label = new JLabel("");
        time_label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        time_label.setBounds(270, 374, 210, 36);
        time_label.setHorizontalAlignment(SwingConstants.CENTER);
        main.getContentPane().add(time_label);
        time_label.setVisible(false);

        MDL = new JLabel("New label");
        MDL.setBounds(270, 133, 210, 14);
        MDL.setHorizontalAlignment(SwingConstants.CENTER);
        main.getContentPane().add(sample_len);
        MDL.setVisible(false);
        main.getContentPane().add(MDL);

        end = new JLabel("New label");
        end.setHorizontalAlignment(SwingConstants.CENTER);
        end.setIcon(new ImageIcon(Objects.requireNonNull(App1.class.getResource("./Resources/DoneIcon.png"))));
        end.setBounds(280, 160, 200, 222);
        main.getContentPane().add(end);
        end.setVisible(false);
    }

    private void change12() {
        SelectB.setVisible(false);
        error_label.setText("");
        backB.setVisible(true);
        file_name_label.setVisible(true);
        file_name.setText(file.getName().substring(0,file.getName().indexOf(".")));
        file_name.setVisible(true);
        sample_len.setText("Sample length: "+a.length());
        sample_len.setVisible(true);
        n_parents.setModel(new SpinnerNumberModel(1, 1, a.element(0).size()-1, 1));
        n_variables.setText("Number of variables: "+(a.element(0).size()-1));
        n_variables.setVisible(true);
        n_parents.setVisible(true);
        n_parents_label.setVisible(true);
        n_graphs.setVisible(true);
        n_graphs_label.setVisible(true);
        BNG.setVisible(true);
    }
    private void change23() {
        backB.setVisible(false);
        file_name_label.setVisible(false);
        file_name_v = file_name.getText();
        file_name.setVisible(false);
        sample_len.setVisible(false);
        n_variables.setVisible(false);
        n_parents.setVisible(false);
        n_parents_label.setVisible(false);
        n_graphs.setVisible(false);
        n_graphs_label.setVisible(false);
        BNG.setVisible(false);
        error_label.setText("");
        bar.setVisible(true);
    }
    private void change34() {
        error_label.setVisible(false);
        backB2.setVisible(true);
        bar.setVisible(false);
        MDL.setText("MDL = "+g.MDL(a));
        MDL.setVisible(true);
        time_label.setText("Elapsed time: "+Utils.totime(elapsed));
        time_label.setVisible(true);
        file_name_label.setVisible(true);
        file_name.setText(file_name_v + "_" + n_parents_v +".bnf");
        file_name.setVisible(true);
        file_name.setEditable(false);
        sample_len.setVisible(true);
        n_variables.setVisible(true);
        error_label.setText("");
        bar.setVisible(false);
        end.setVisible(true);
    }
    private void change42() {
        error_label.setVisible(true);
        error_label.setText("");
        backB2.setVisible(false);
        MDL.setText("");
        MDL.setVisible(false);
        time_label.setText("");
        time_label.setVisible(false);
        backB2.setVisible(false);
        end.setVisible(false);
        error_label.setText("");
        backB.setVisible(true);
        file_name_label.setVisible(true);
        file_name.setText(file_name_v);
        file_name.setVisible(true);
        file_name.setEditable(true);
        sample_len.setVisible(true);
        n_variables.setVisible(true);
        n_parents.setVisible(true);
        n_parents_label.setVisible(true);
        n_graphs.setVisible(true);
        n_graphs_label.setVisible(true);
        BNG.setVisible(true);
    }
    private void change21() {
        backB.setVisible(false);
        file_name_label.setVisible(false);
        file_name.setText("");
        file_name.setVisible(false);
        sample_len.setText("");
        sample_len.setVisible(false);
        n_variables.setText("");
        n_variables.setVisible(false);
        n_parents_label.setVisible(false);
        n_parents.setVisible(false);
        n_graphs_label.setVisible(false);
        n_graphs.setVisible(false);
        BNG.setVisible(false);
        SelectB.setVisible(true);
        error_label.setText("");
    }

    private void initialize() {
        fc.setCurrentDirectory(path);
        main = new JFrame();
        components(main);

        SelectB.addActionListener(e -> {
            if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                try {
                    a = new Amostra(""+file);
                    change12();
                } catch (Exception e1) {
                    error_label.setText("Could not import sample :(");
                }
            }
        });

        backB.addActionListener(e -> {
            int b = JOptionPane.showConfirmDialog(null, "Are you sure you want to go back?\nAll dataset will be lost", "", JOptionPane.YES_NO_OPTION);
            if(b == JOptionPane.YES_OPTION){
                a = new Amostra(); change21();
            }
        });

        backB2.addActionListener(e -> change42());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            bar.setValue(progress);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            n_parents_v = (int) n_parents.getValue();
            n_graphs_v = Integer.parseInt(n_graphs.getText());
            change23();
            Task task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        } catch (NumberFormatException e2) {
            error_label.setText("Could not create Bayesian Network :(");
        }
    }

    public App1() throws IOException {
        initialize();
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App1 window = new App1();
                window.main.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
