import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiDevice;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.util.concurrent.CompletableFuture;


public class RecordClass {
    static String directory;
    int check = 0;

    JavaSoundRecorder recorder = new JavaSoundRecorder();

    RecordClass(URL url) throws IOException {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        frame.setAlwaysOnTop(true);
        frame.setTitle("Record");
        frame.setBounds(0, 0, 300, 250);
        frame.setIconImage(ImageIO.read(url));

        JButton button = new CircleBtn("Record");
        button.setBounds(100, 50, 100, 100);
        button.setBackground(Color.MAGENTA);
        JTextField textField = new HintTextField("Name");
        textField.setBounds(70, 0, 150, 25);
        textField.setEditable(true);
button.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {

            button.setBackground(Color.GREEN);

        super.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(check==0)
        button.setBackground(Color.MAGENTA);
        super.mouseExited(e);
    }
});
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (check == 0) {
                    if (textField.getText().equals("Name")) {

                        return;
                    }
                    check = 1;
                    button.setBackground(Color.GREEN);

                    CompletableFuture.runAsync(() -> {
                        JavaSoundRecorder.wavFile = new File(directory + "\\" + textField.getText() + ".wav");
                        recorder.start();


                    });

                    CompletableFuture.runAsync(() -> {
                        for (int i = 0; (check != 0); i++) {


                            button.setText(String.format("%d:%02d", i / 60, i % 60));
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }

                        CompletableFuture.runAsync(() -> {
                            button.setEnabled(false);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            button.setEnabled(true);
                        });
                    });
                } else {
                    recorder.finish();
                    button.setBackground(Color.MAGENTA);
                    button.setText("Record");
                    check = 0;
                }
            }
        });
        panel.add(textField);
        panel.add(button);
        panel.revalidate();
        frame.setResizable(false);
        frame.setVisible(true);

    }

}

class CircleBtn extends JButton {
    public CircleBtn(String text) {
        super(text);

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.GREEN);
            setForeground(Color.BLACK);
        } else {
            g.setColor(getBackground());
            setForeground(Color.BLACK);
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }
}
