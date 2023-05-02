import javax.swing.*;
import javax.swing.plaf.LayerUI;

public class Window {
    Element[][] Board = new Element[11][15];
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createUI();
            }
        });
    }
    public static void createUI() {
        JFrame f = new JFrame("SJSU Minesweeper");

        JPanel panel = createPanel();
        LayerUI<JComponent> layerUI = new WallpaperLayerUI();
        JLayer<JComponent> jlayer = new JLayer<JComponent>(panel, layerUI);

        f.add (jlayer);

        f.setSize(1000, 800);
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo (null);
        f.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel p = new JPanel();
        return p;
    }

}