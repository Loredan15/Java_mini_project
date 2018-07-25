import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UI extends JFrame {
    private JPanel catalogPanel = new JPanel();
    private JList fileList = new JList();
    private JScrollPane filesScroll = new JScrollPane(fileList);
    private JPanel buttonsPanel = new JPanel();
    private JButton addButton = new JButton("Создать папку");
    private JButton backButton = new JButton("Назад");
    private JButton delButton = new JButton("Удалить");
    private JButton renameButton = new JButton("Переименовать");
    private ArrayList <String>  dirsCache = new ArrayList<>();

    public UI(){
        super("Проводник");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        catalogPanel.setLayout(new BorderLayout(5, 5));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.setLayout(new GridLayout(1, 4, 5, 5));
        JDialog createNewDirDialog = new JDialog(UI.this, "Создание папки", true);
        JPanel createNewDirPanel = new JPanel();
        createNewDirDialog.add(createNewDirPanel);

        File discs[] = File.listRoots();

        filesScroll.setPreferredSize(new Dimension(400, 500));
        fileList.setListData(discs);
        fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Можно выбирать несколько элементов

        fileList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultListModel model = new DefaultListModel();
                    String selectedObject = fileList.getSelectedValue().toString();
                    String fullPath = toFullPath(dirsCache);
                    File selectedFile;
                    if (dirsCache.size() > 1) {
                        selectedFile = new File(fullPath, selectedObject);
                    } else {
                        selectedFile = new File(fullPath + selectedObject);
                    }

                    if (selectedFile.isDirectory()) {
                        String[] rootStr = selectedFile.list();
                        for (String str : rootStr) {
                            File checkObject = new File(selectedFile.getPath(), str);
                            if (!checkObject.isHidden()) {
                                if (checkObject.isDirectory()) {
                                    model.addElement(str);
                                } else {
                                    model.addElement("файл - " + str);
                                }
                            }
                        }
                        dirsCache.add(selectedObject);
                        fileList.setModel(model);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dirsCache.size() > 1) {
                    dirsCache.remove(dirsCache.size() - 1);
                    String backDir = toFullPath(dirsCache);
                    String[] objects = new File(backDir).list();
                    DefaultListModel backRootModel = new DefaultListModel();

                    for (String str : objects) {
                        File checkObject = new File(backDir, str);
                        if (!checkObject.isHidden()) {
                            if (checkObject.isDirectory()) {
                                backRootModel.addElement(str);
                            } else {
                                backRootModel.addElement("файл - " + str);
                            }
                        }
                    }
                    fileList.setModel(backRootModel);
                } else {
                    dirsCache.removeAll(dirsCache);
                    fileList.setListData(discs);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(delButton);
        buttonsPanel.add(renameButton);
        catalogPanel.setLayout(new BorderLayout());
        catalogPanel.add(filesScroll, BorderLayout.CENTER);
        catalogPanel.add(buttonsPanel, BorderLayout.SOUTH);
        getContentPane().add(catalogPanel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String toFullPath (List<String> file){
        String listPart = "";
        for (String str : file ) {
            listPart += str;
        }
        return listPart;
    }

}
