import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

class WallpaperLayerUI extends LayerUI<JComponent> {
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();

        int w = c.getWidth();
        int h = c.getHeight();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, (float) 0.8f));
        g2.setPaint(Color.decode("#005298"));
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}
