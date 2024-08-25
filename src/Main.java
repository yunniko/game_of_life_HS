import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            Runnable initFrame = new Runnable() {
                @Override
                public void run() {
                    GameOfLife g = new GameOfLife();
                }
            };
            SwingUtilities.invokeAndWait(initFrame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
