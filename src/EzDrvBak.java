import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EzDrvBak extends JFrame {
    private JLabel devlabel;
	private JButton backupBtn;
    private JButton restoreBtn;
    private JButton exitBtn;
//    private JProgressBar progressBar;

    public EzDrvBak() {
        setTitle("ezDrvBAK");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 90);
        setLayout(new FlowLayout());
        
        devlabel= new JLabel("장치 드라이버");
        backupBtn = new JButton("백업");
        restoreBtn = new JButton("복원");
        exitBtn = new JButton("종료");
//        backupBtn.setPreferredSize(new Dimension(170, 90));
//        restoreBtn.setPreferredSize(new Dimension(100, 190));
//        progressBar = new JProgressBar();

        backupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    executeCommand("pnputil.exe /export-driver * " + path, "백업");
                }
            }
        });

        restoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    executeCommand("for /f \"tokens=1 delims=\" %%d in ('dir /s /b " + path + "\\*.inf') do pnputil.exe -i -a %%d", "복원");
                }
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(devlabel);
        add(backupBtn);
        add(restoreBtn);
        add(exitBtn);
//        add(progressBar);

        setVisible(true);
    }

    private void executeCommand(String command, String taskName) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
            }
//            progressBar.setValue(100);
            JOptionPane.showMessageDialog(null, taskName + " 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EzDrvBak();
    }
}
