
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends JPanel implements KeyListener {
    private JFrame frame;
    private PinholeCamera camera;
    private ArrayList<Renderable> renderables;

    private boolean KW_DOWN = false;
    private boolean KA_DOWN = false;
    private boolean KS_DOWN = false;
    private boolean KD_DOWN = false;

    private boolean KE_DOWN = false;
    private boolean KQ_DOWN = false;
    private boolean KR_DOWN = false;
    private boolean KF_DOWN = false;

    private float move_velocity = 0.2f;
    private float rot_velocity = 0.1f;

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                KW_DOWN = true;
                break;
            case KeyEvent.VK_A:
                KA_DOWN = true;
                break;
            case KeyEvent.VK_S:
                KS_DOWN = true;
                break;
            case KeyEvent.VK_D:
                KD_DOWN = true;
                break;

            case KeyEvent.VK_E:
                KE_DOWN = true;
                break;
            case KeyEvent.VK_Q:
                KQ_DOWN = true;
                break;

            case KeyEvent.VK_R:
                KR_DOWN = true;
                break;
            case KeyEvent.VK_F:
                KF_DOWN = true;
                break;

            case KeyEvent.VK_SPACE:
                camera.space_default();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                KW_DOWN = false;
                break;
            case KeyEvent.VK_A:
                KA_DOWN = false;
                break;
            case KeyEvent.VK_S:
                KS_DOWN = false;
                break;
            case KeyEvent.VK_D:
                KD_DOWN = false;
                break;
            case KeyEvent.VK_E:
                KE_DOWN = false;
                break;
            case KeyEvent.VK_Q:
                KQ_DOWN = false;
                break;
            case KeyEvent.VK_R:
                KR_DOWN = false;
                break;
            case KeyEvent.VK_F:
                KF_DOWN = false;
                break;

        }
    }

    private Main(int width, int height, PinholeCamera camera, ArrayList<Renderable> renderables) {
        assert(width > 0 && height > 0);

        this.setSize(width, height);

        frame = new JFrame("Render test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setSize(width+2, height + 24);
        frame.setResizable(false);
        frame.setVisible(true);

        this.setVisible(true);
        this.camera = camera;
        this.renderables = renderables;

        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    public void paint(Graphics graphics) {
        // camera movement
        if (KW_DOWN) camera.move_forward(move_velocity);
        if (KS_DOWN) camera.move_forward(-move_velocity);
        if (KD_DOWN) camera.move_right(move_velocity);
        if (KA_DOWN) camera.move_right(-move_velocity);

        // camera rotation
        if (KE_DOWN) camera.yaw(rot_velocity);
        if (KQ_DOWN) camera.yaw(-rot_velocity);
        if (KR_DOWN) camera.pitch(rot_velocity);
        if (KF_DOWN) camera.pitch(-rot_velocity);


        camera.render_perspective(renderables);
        graphics.drawImage(camera.getImage(), 0, 0, null);
        repaint();
    }

    public static void image_demo() {
        int width = 800;
        int height =  width * 9 / 16;

        Vec3 camera_pos = new Vec3(5f,0f,2f);
        Vec3 camera_dir = new Vec3(-1f,0f,0f);
        Vec3 camera_up  = new Vec3(0f,0f,1f);

        Sphere s1 = new Sphere(1f, Color.RED, new Vec3(0f, -1f,1f));
        Sphere s2 = new Sphere(1f, Color.GREEN, new Vec3(0f, +1f,1f));
        Sphere s3 = new Sphere(1f, Color.BLUE, new Vec3(0f, 0f, (float)(1+2*Math.sin(Math.PI/3))));
        MirrorSphere ms = new MirrorSphere(1f, new Vec3(-4f, 0f, 2f));
        Plane plane = new Plane();

        ArrayList<Renderable> renderables = new ArrayList<>();

        PinholeCamera camera = new PinholeCamera(
                width, height,
                camera_pos,
                camera_dir,
                camera_up
        );

        renderables.add(s1);
        renderables.add(s2);
        renderables.add(s3);
        renderables.add(plane);
        renderables.add(ms);

        Main main = new Main(width, height, camera, renderables);
    }

    public static void main(String[] args) {
        image_demo();
    }


}
