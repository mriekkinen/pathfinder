package pathfinder.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import pathfinder.io.GraphReader;
import pathfinder.logic.Graph;

/**
 * Shows the open file dialog, and reads the selected file
 * 
 */
public class OpenFileAction extends AbstractAction {

    private final UserInterface gui;
    private final Component parent;
    private final JFileChooser fileChooser;

    /**
     * Constructs an <code>OpenFileAction</code>
     * 
     * @param gui reference to the GUI object
     * @param parent reference to the frame. Used for positioning the file chooser.
     */
    public OpenFileAction(UserInterface gui, Component parent) {
        super("Open");
        putValue(SHORT_DESCRIPTION, "Open a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        this.gui = gui;
        this.parent = parent;
        this.fileChooser = new JFileChooser();
    }

    /**
     * Shows the open file dialog, and reads the selected file
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(parent);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path path = fileChooser.getSelectedFile().toPath();
        openFile(path);
    }

    private void openFile(Path path) {
        try {
            Graph g = GraphReader.readFile(path);
            gui.setGraph(g);
        } catch (IOException e) {
            showErrorMessage(e);
        }
    }

    private void showErrorMessage(IOException e) {
        String title = "Open file error";
        String msg = "Cannot open the file:\n\n" + e.toString();

        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE);
    }

}