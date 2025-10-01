package solarclock.calendar;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public class CalendarGeneration {
   
    public static boolean registerAllFonts() {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        try {
            // Obtenez le chemin du dossier où sont stockées les polices
            Path fontDirectory = Paths.get(CalendarGeneration.class.getClassLoader().getResource("solarclock/calendar/font").toURI());
            // Parcourir tous les fichiers TTF dans le répertoire
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(fontDirectory, "*.ttf")) {
                for (Path path : stream) {
                    try (InputStream is = Files.newInputStream(path)) {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                        System.out.println("register font: " + font.getName());
                        ge.registerFont(font);
                    } catch (IOException | FontFormatException e) {
                        System.err.println("Failed to load font: " + path.getFileName());
                    }
                }
            }
            return true;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
   
//    public static boolean createFont(final String fontName) {
//        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        try (InputStream is = CalendarGeneration.class.getClassLoader().getResourceAsStream("solarclock/calendar/font/" + fontName + ".ttf")) { 
//            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//            return ge.registerFont(font);
//        } catch (IOException | FontFormatException e) {
//            return false;
//        }
//    }
//        
    /**
     * This is the main method which start program execution.
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        JFrame mainFrame = new JFrame("Calendar Generator Tool");
           
        registerAllFonts();
//        createFont("ParmaPetit-Normal");
//        createFont("Elementary_Gothic_Bookhand");
//        createFont("Peachy Mochi");
//        createFont("Sunset Club Free Trial");
//        createFont("Neon Future 2.0 Demo");
                
        JLabel welcomeMessage = new JLabel();
        welcomeMessage.setText("Welcome to Calendar Generation App!");
        welcomeMessage.setBounds(10, 0, 500, 50);
        // enter name label
        JLabel label = new JLabel();
        label.setText("Enter Year(YYYY):");
        label.setBounds(10, 10, 100, 100);

        final JTextField yearEdit = new JTextField("2025");
        yearEdit.setBounds(110, 50, 130, 30);

        // submit button
        JButton generateCalendarButton = new JButton("Generate Calendar");
        generateCalendarButton.setBounds(20, 100, 140, 40);

        JButton displayCalendarButton = new JButton("Display Calendar");
        displayCalendarButton.setBounds(20, 100, 140, 40);

        // submit button
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(170, 100, 100, 40);

        // add to frame
        mainFrame.add(welcomeMessage);
        mainFrame.add(yearEdit);
        mainFrame.add(label);
        mainFrame.add(generateCalendarButton);
     //   mainFrame.add(displayCalendarButton);
        mainFrame.add(resetButton);
        mainFrame.setSize(300, 300);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // mainFrame.getContentPane().setBackground(Color.lightGray.brighter());
        // action listener
        resetButton.addActionListener((ActionEvent arg0) -> {
            yearEdit.setText("");
        });
        
        generateCalendarButton.addActionListener((ActionEvent arg0) -> {
            // Read Year from the Text Box
            final String year = yearEdit.getText();
            if (year.isEmpty() || "".equals(year) || !isNumeric(year)) {
                JOptionPane.showMessageDialog(null, "Wrong Input, Please correct!");

            } else if (Integer.valueOf(year) <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric Value is less than or Equal to 0, Please correct!");
            } else {
                JFrame frame = new JFrame("View Calendar For " + year);
                frame.setSize(600, 500);

                JScrollPane scrollPane = new JScrollPane(CalendarDrawer2025.generateCalendarUI(2025), //Integer.parseInt(year)),
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                scrollPane.setBounds(0, 0, 1000, 700);
                frame.setContentPane(scrollPane);
                frame.pack();
                frame.setVisible(true);
            }
        });
        displayCalendarButton.addActionListener((ActionEvent arg0) -> {
            // Read Year from the Text Box
                JFrame frame = new JFrame("View Calendar");
                frame.setSize(600, 500);

                JScrollPane scrollPane = new JScrollPane(PdfDrawer.generateCalendarUI(new File("C:\\Users\\sebastien.durand\\Desktop\\calendoc_2022.pdf")),
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                scrollPane.setBounds(0, 0, 1000, 700);
                frame.setContentPane(scrollPane);
                frame.pack();
                frame.setVisible(true);
            
        });
      //  CalendarGeneration cal = new CalendarGeneration();
    }

    /**
     * This method helps to check input is numeric number or not.
     *
     * @param s
     * @return
     */
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}
