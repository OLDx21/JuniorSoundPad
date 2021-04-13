import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class Sound extends JFrame {
    static String knopka;
    Thread thread = null;
    static JTextField field;
    JSONObject jsonObject;
    static HashMap hashMap = new HashMap();
    static ObservableList<Infoclient> info = null;
    static Sound sound;
    static String directory = "";
    int row;
    Object val;
    private TablePosition tablePosition;
    public TableView.TableViewSelectionModel selectionModel;
    public ObservableList selectedCells;
    int checked = 0;
    int checked2 = 0;
    JSlider slider;

    public float seconds = 0;
    private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    static String namevalue;
    private TableColumn<Infoclient, String> clmnbind;
    private TableColumn<Infoclient, String> clmnsize;
    static JButton start;
    private TableColumn<Infoclient, String> clmnname;
    AudioInputStream audioInputStream;
    Clip clip;
    FloatControl gainControl;
    private ArrayList NameList = new ArrayList();

    private TableView<Infoclient> tableView;
    private TableColumn<Infoclient, Number> clmnid = new TableColumn<Infoclient, Number>("№");
    static JButton pause;

    static JFrame jFrame;

    Sound() {

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

        JPanel panel = new JPanel();
        jFrame.setContentPane(panel);
        panel.setLayout(null);
        jFrame.setTitle("JuniorSoundPad");


        //Menu
        JMenuBar menuBar = new JMenuBar();


        start = new JButton("Start");
        pause = new JButton("Stop");
        start.setSize(50, 30);
        JProgressBar progressBar = new JProgressBar();
        JToolBar toolBar = new JToolBar();

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
        JPopupMenu popupMenu = new JPopupMenu();
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

                    int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                    tableView.getItems().remove(selectedIndex);
                    checked2 = 0;
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

                    if (hashMap.containsKey(namevalue)) {
                        JOptionPane.showMessageDialog(jFrame, "Эта клавиша уже занята!", "Предупреждение!", ERROR_MESSAGE);
                        return;
                    }

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    return;
                }

                hashMap.put(namevalue, field.getText());
                info.get(row).setbind(field.getText());
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
                }
            }
        });


        JFXPanel jfxPanel = new JFXPanel();
        tableView = new TableView();

        jfxPanel.setBounds(0, 35, jFrame.getWidth() - 7, 330);


        jfxPanel.setComponentPopupMenu(popupMenu);

        tableView.setMaxHeight(330);
        tableView.setMaxWidth(jFrame.getWidth() - 7);


        clmnname = new TableColumn<Infoclient, String>("Тег");
        clmnsize = new TableColumn<Infoclient, String>("Длит.");
        clmnbind = new TableColumn<Infoclient, String>("Клавиша");

        clmnname.setStyle("-fx-alignment: CENTER-LEFT");
        clmnsize.setStyle("-fx-alignment: CENTER-LEFT");
        clmnid.setStyle("-fx-alignment: CENTER-LEFT");

        clmnid.setPrefWidth(30);
        clmnname.setPrefWidth(345);
        clmnbind.setPrefWidth(110);
        clmnbind.setEditable(true);
        clmnsize.setPrefWidth(60);
        tableView.getColumns().addAll(clmnid, clmnname, clmnsize, clmnbind);
        tableView.getStylesheets().add("styletable.css");
        Group group = new Group();
        Scene scene = new Scene(group);
        group.getChildren().addAll(tableView);
        jfxPanel.setScene(scene);
        thread = new Thread();
        thread.setName("yourname");

        jFrame.setJMenuBar(menuBar);
        panel.add(jfxPanel);
        panel.revalidate();
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sourceLine != null && sourceLine.isRunning()) {
                    clip.stop();
                    sourceLine.stop();
                    checked = 1;
                    progressBar.setValue(0);
                }

            }
        });

        start.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                checked = 0;
                if (namevalue == null) {

                    JOptionPane.showMessageDialog(jFrame, "конч?");

                    return;
                }

                if (sourceLine != null && sourceLine.isRunning()) {
                    clip.stop();
                    thread.stop();
                    progressBar.setMaximum(100);
                    progressBar.setValue(0);
                    sourceLine.stop();

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
                        for (int i = 0; i <= seconds; i++) {
                            if (checked == 1) {
                                break;
                            }
                            progressBar.setValue(i);
                            try {
                                Thread.sleep(1000 / 100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
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

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    if (checked2 == 1) {
                        return;
                    }
                    selectionModel = tableView.getSelectionModel();
                    selectedCells = selectionModel.getSelectedCells();
                    tablePosition = (TablePosition) selectedCells.get(0);
                    val = tablePosition.getTableColumn().getCellData(newValue);

                    namevalue = val.toString();
                    row = tablePosition.getRow();

                }
            }
        });

    }

    public static void main(String[] args) throws IOException {
        File file10 = new File("C:\\Games\\JuniorSoundPad");
        if (file10.exists() == false) {

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
            info = sound.settableinfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sound.arttable(info);


        System.gc();
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);


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

                            sound.playSound(new File(directory + "\\JuniorSoundPad\\Sound\\" + namevalue + ".wav"));


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

    public void arttable(ObservableList<Infoclient> info) {
        clmnname.setCellFactory(TextFieldTableCell.forTableColumn());
        clmnname.setCellValueFactory(cellData -> cellData.getValue().getname());

        clmnsize.setCellFactory(TextFieldTableCell.forTableColumn());
        clmnsize.setCellValueFactory(cellData -> cellData.getValue().getlengh());

        clmnbind.setCellFactory(TextFieldTableCell.forTableColumn());
        clmnbind.setCellValueFactory(cellData -> cellData.getValue().getbind());

        clmnid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Number>(tableView.getItems().indexOf(cellData.getValue()) + 1));
        tableView.setItems(info);
        System.gc();
    }

    public ObservableList<Infoclient> settableinfo() throws Exception {

        String file;
        String bind = "";
        String prod;
        ObservableList<Infoclient> info = FXCollections.observableArrayList();
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


            info.add(new Infoclient((String) NameList.get(i), String.format("%02d:%02d", ((int) Integer.parseInt(prod) % 3600) / 60, (int) Integer.parseInt(prod) % 60), bind));
            bind = "";
        }


        return info;
    }


}
