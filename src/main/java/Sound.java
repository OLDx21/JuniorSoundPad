import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import javatests.ProxyTests;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class Sound extends JFrame {

    AtomicInteger atomicInteger = new AtomicInteger(0);
    JPopupMenu popupMenu;
    HashMap<Integer, String[]> data = new HashMap<Integer, String[]>();
    JTable table;
    Thread thread = null;
    static JTextField field;
    JSONObject jsonObject;
    static ConcurrentHashMap hashMap = new ConcurrentHashMap();
    static Sound sound;
    static String directory = "";

    JPanel panel;
   // volatile  int checked = 0;
    int checked2 = 0;
    JSlider slider;
    DefaultTableModel defaultTableModel;
    public float seconds = 0;
    private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    static String namevalue;
    static JButton start;
    AudioInputStream audioInputStream;
    Clip clip;
    FloatControl gainControl;
    private ArrayList<String> NameList = new ArrayList<>();


    static JButton pause;

    static JFrame jFrame;

    Sound() {

//       String g =System.getProperty("user.dir");
//       System.out.println(g);
//        try {
//            Process myappProcess = Runtime.getRuntime().exec("powershell.exe Start-Process " +g+" -verb RunAs");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {

            FlatDarkPurpleIJTheme.install();

            UIManager.setLookAndFeel(new FlatDarkPurpleIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }


        jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width / 3 - 100, dimension.height / 3 - 100, 550, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL url2 = this.getClass().getResource("icons.png");
        try {
            jFrame.setIconImage(ImageIO.read(url2));
        } catch (IOException e) {
            e.printStackTrace();
        }

         panel = new JPanel();
        jFrame.setContentPane(panel);
        panel.setLayout(null);
        jFrame.setTitle("JuniorSoundPad");



        //Menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("File")).add(new JMenuItem("Record")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RecordClass recordClass = new RecordClass(url2);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        menuBar.getMenu(0).add(new JMenuItem("Synchronized")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                hashMap.forEach((k, v) -> deleteif((String) k));

                jsonObject = new JSONObject(hashMap);

                try {
                    FileWriter file = new FileWriter(directory + "\\JuniorSoundPad\\bind.json");
                    file.write(jsonObject.toString());
                    file.flush();
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(panel, "Successfully synced!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        start = new JButton("Start");
        pause = new JButton("Stop");
        start.setSize(50, 30);

        JToolBar toolBar = new JToolBar();
       JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        toolBar.add(start);
        toolBar.add(pause);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(progressBar);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        slider = new JSlider();
        toolBar.add(slider);
        slider.setValue(100);
        JLabel label = new JLabel("Volume: 100%");
        toolBar.add(label);
        panel.add(toolBar);


        slider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label.setText("Volume: " + String.valueOf(slider.getValue()) + "%");
            }
        });
        toolBar.setBounds(0, 0, 470, 30);
        start.setToolTipText("Start");
        pause.setToolTipText("Pause");
        progressBar.setToolTipText("Progress");
        //table
         popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem("Воспроизвести")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.doClick();
            }
        });


        popupMenu.add(new JMenuItem("Удалить")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int g = JOptionPane.showConfirmDialog(jFrame, "Удалить звук из коллекции? ", "Подтверди", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


                if (g == 0) {

                    checked2 = 1;
                    File f = new File(directory + "\\JuniorSoundPad\\Sound\\" + namevalue + ".wav");
                    f.delete();



                    checked2 = 0;
                    System.out.println(hashMap.size());
                    hashMap.remove(namevalue);
                    data.remove(table.getSelectedRow());
                    jsonObject = new JSONObject(hashMap);
                   defaultTableModel.removeRow(table.getSelectedRow());
                   for(int i = 0; i<data.size(); i++){
                       table.getModel().setValueAt(String.valueOf(i+1), i, 0);

                   }


                    try {
                        FileWriter file = new FileWriter(directory + "\\JuniorSoundPad\\bind.json");
                        file.write(jsonObject.toString());
                        file.flush();
                        file.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        JFrame parent = new JFrame();

        try {
            parent.setIconImage(ImageIO.read(url2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane optionPane = new JOptionPane();
        field = new JTextField();

        popupMenu.add(new JMenuItem("Задать гарячую клавишу")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {

                    field.setEditable(false);
                    optionPane.setMessage(new Object[]{"Нажмите клавишу", field});
                    optionPane.setMessageType(INFORMATION_MESSAGE);
                    optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                    JDialog dialog = optionPane.createDialog(parent, "Гарячая клавиша");
                    dialog.setVisible(true);


                    if ((int) optionPane.getValue() == 2) {
                        return;
                    }

                    if (hashMap.containsValue(field.getText())||field.getText().equals("NumPad-0")) {
                        JOptionPane.showMessageDialog(jFrame, "Эта клавиша уже занята!", "Предупреждение!", ERROR_MESSAGE);
                        return;
                    }

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    return;
                }

                hashMap.put(namevalue, field.getText());
                  table.getModel().setValueAt(field.getText(),table.getSelectedRow(),3);
                jsonObject = new JSONObject(hashMap);

                try {
                    FileWriter file = new FileWriter(directory + "\\JuniorSoundPad\\bind.json");
                    file.write(jsonObject.toString());
                    file.flush();
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });
        popupMenu.add(new JMenuItem("Удалить гарячую кнопку")).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hashMap.containsKey(namevalue)) {
                    hashMap.remove(namevalue);
                    jsonObject = new JSONObject(hashMap);

                    try {
                        FileWriter file = new FileWriter(directory + "\\JuniorSoundPad\\bind.json");
                        file.write(jsonObject.toString());
                        file.flush();
                        file.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    table.getModel().setValueAt("", table.getSelectedRow(), 3);

                }
            }
        });



        thread = new Thread();
        thread.setName("yourname");

       jFrame.setJMenuBar(menuBar);



        panel.revalidate();
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (sourceLine != null && sourceLine.isRunning()) {
                    clip.stop();
                    clip.flush();
                    sourceLine.stop();
                    atomicInteger.set(1);
                    progressBar.setValue(0);
                    sourceLine.drain();
                    sourceLine.flush();
                }

            }
        });


        start.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                atomicInteger.set(0);

                if (namevalue == null) {

                    JOptionPane.showMessageDialog(jFrame, "конч?");

                    return;
                }

                if (sourceLine != null && sourceLine.isRunning()||clip!=null&&clip.isRunning()) {
                    atomicInteger.set(1);
                    clip.stop();
                    clip.flush();
                    progressBar.setMaximum(100);
                    progressBar.setValue(0);
                    sourceLine.stop();
                    sourceLine.drain();
                    sourceLine.flush();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }


                File file = new File(directory + "\\JuniorSoundPad\\Sound\\" + namevalue + ".wav");
                try {

                    seconds = Methods.getDuration(file) * 100; // Maybe no need to divide if the input is in seconds
                    progressBar.setMaximum((int) seconds);


                } catch (Exception exx) {
                    exx.printStackTrace();
                }

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {



                        for (int i =0;i <= seconds; i++) {
                            if (atomicInteger.get()==1) {
                                break;
                            }

                            progressBar.setValue(i);

                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        atomicInteger.set(0);
                        progressBar.setValue(0);


                    }
                });
                thread.start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        playSound(file);
                    }
                }).start();


            }
        });



    }

    public static void main(String[] args) throws IOException {
        File file10 = new File("C:\\Games\\JuniorSoundPad");
        if (!file10.exists()) {

            file10.mkdirs();
            try {
                FileOutputStream fileOut = new FileOutputStream("C:\\Games\\JuniorSoundPad\\directory.txt");
                fileOut.close();

                fileOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        InputStream inputStream = new FileInputStream("C:\\Games\\JuniorSoundPad\\directory.txt");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null; ) {
            directory = line;
            RecordClass.directory = directory+ "\\JuniorSoundPad\\Sound";
        }

        inputStream.close();
        streamReader.close();
        reader.close();


        sound = new Sound();


        File file5 = new File("directory.txt");

        if (directory.length() <= 0 || directory == null) {

            directory = Methods.getdirectory();


            file5 = new File(directory + "\\JuniorSoundPad\\Sound");
            file5.mkdir();

            file5 = new File(directory + "\\JuniorSoundPad\\bind.json");
            if (file5.exists() == false) {
                try {
                    FileOutputStream fileOut = new FileOutputStream(directory + "\\JuniorSoundPad\\bind.json");
                    fileOut.close();

                    fileOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (directory.length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(directory + "\\JuniorSoundPad\\bind.json")));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line + "\n");

                }
                if (!(stringBuilder.length() == 0)) {
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    hashMap.putAll(Methods.toMap(jsonObject));

                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        try {
            sound.settableinfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sound.arttable();


        System.gc();

        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);


        keyboardHook.addKeyListener(new GlobalKeyAdapter() {

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                field.setText(KeyEvent.getKeyText(event.getVirtualKeyCode()));
                if (KeyEvent.getKeyText(event.getVirtualKeyCode()).equals("NumPad-0")) {
                    pause.doClick();

                }

                if (hashMap.containsValue(KeyEvent.getKeyText(event.getVirtualKeyCode()))) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (Object o : hashMap.keySet()) {
                                if (hashMap.get(o).equals(KeyEvent.getKeyText(event.getVirtualKeyCode()))) {
                                    namevalue = o.toString();
                                    break;
                                }
                            }

                            start.doClick();


                        }
                    }).start();

                }

            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {

            }
        });


    }

    public void playSound(File soundFile) {

        if (sourceLine != null && sourceLine.isRunning()) {
            return;

        }


        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        audioFormat = audioStream.getFormat();
        DataLine.Info infoIn = new DataLine.Info(SourceDataLine.class,
                audioFormat);
        try {
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer mixer = null;
            for (int i = 0; i < mixerInfos.length; i++) {

                if (mixerInfos[i].getName().contains(
                        "CABLE Input (VB-Audio Virtual Cable)")) {
                    mixer = AudioSystem.getMixer(mixerInfos[i]);
                    break;
                }
            }

            sourceLine = (SourceDataLine) mixer.getLine(infoIn);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }


        try {

            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.setFramePosition(0);
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        sourceLine.start();


        FloatControl vc = (FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN);

        int f;


        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {

                f = (16 * slider.getValue()) / 100;
                gainControl.setValue(((16 * slider.getValue()) / 100) - 10);
                vc.setValue(f - 10);
                nBytesRead = audioStream.read(abData, 0, abData.length);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        try {
            audioStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        abData = null;

        sourceLine.drain();
        sourceLine.close();
    }

    public void arttable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("№");
        defaultTableModel.addColumn("Тег");
        defaultTableModel.addColumn("Длит.");
        defaultTableModel.addColumn("Клавиша");




        for(int i =0; i<data.size(); i++){

            defaultTableModel.addRow(data.get(i));

        }


        table = new JTable(defaultTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setBounds(0, 35, jFrame.getWidth() - 7, 330);
        table.setForeground(Color.decode("#0ed9e0"));
        table.setGridColor(Color.lightGray);
        table.setRowHeight(25);
        table.setSelectionBackground(Color.decode("#005797"));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(345);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                try {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        Point point = e.getPoint();
                        int column = table.columnAtPoint(point);
                        int row = table.rowAtPoint(point);
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    namevalue = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                }catch (ArrayIndexOutOfBoundsException ex){
                    return;
                }


            }
        });
        table.setFillsViewportHeight(true);
          table.setComponentPopupMenu(popupMenu);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 35, jFrame.getWidth() - 7, 330);

        panel.add(scrollPane);


        System.gc();
    }

    public void settableinfo() throws Exception {

        String file;
        String bind = "";
        String prod;

        File dir = new File(directory + "\\JuniorSoundPad\\Sound");

        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);

        for (int i = 0; i < lst.size(); i++) {
            NameList.add(arrFiles[i].getName().replace(".wav", ""));

        }

        for (int i = 0; i < NameList.size(); i++) {
            file = arrFiles[i].toString();


            prod = String.valueOf(Methods.getDuration(new File(file)));

            prod = prod.substring(0, prod.indexOf("."));

            if (hashMap.containsKey(NameList.get(i))) {

                bind = (String) hashMap.get(NameList.get(i));
            }

            data.put(i, new String[]{String.valueOf(i+1),(String) NameList.get(i), String.format("%02d:%02d", ((int) Integer.parseInt(prod) % 3600) / 60, (int) Integer.parseInt(prod) % 60), bind});
            bind = "";
        }

    }
    public void deleteif(String key){

        if(!NameList.contains(key)){
            hashMap.remove(key);

        }

    }

}
