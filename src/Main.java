
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
    private boolean KR_DOWN = false;
    private boolean KF_DOWN = false;

    private boolean KE_DOWN = false;
    private boolean KQ_DOWN = false;
    private boolean KT_DOWN = false;
    private boolean KG_DOWN = false;
    private boolean KZ_DOWN = false;
    private boolean KC_DOWN = false;

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
            case KeyEvent.VK_R:
                KR_DOWN = true;
                break;
            case KeyEvent.VK_F:
                KF_DOWN = true;
                break;

            case KeyEvent.VK_E:
                KE_DOWN = true;
                break;
            case KeyEvent.VK_Q:
                KQ_DOWN = true;
                break;
            case KeyEvent.VK_T:
                KT_DOWN = true;
                break;
            case KeyEvent.VK_G:
                KG_DOWN = true;
                break;
            case KeyEvent.VK_Z:
                KZ_DOWN = true;
                break;
            case KeyEvent.VK_C:
                KC_DOWN = true;
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

            case KeyEvent.VK_T:
                KT_DOWN = false;
                break;
            case KeyEvent.VK_G:
                KG_DOWN = false;
                break;

            case KeyEvent.VK_Z:
                KZ_DOWN = false;
                break;
            case KeyEvent.VK_C:
                KC_DOWN = false;
                break;

        }
    }


    @Override
    public void paint(Graphics graphics) {
        // camera movement
        if (KW_DOWN) camera.move_forward(move_velocity);
        if (KS_DOWN) camera.move_forward(-move_velocity);
        if (KD_DOWN) camera.move_right(move_velocity);
        if (KA_DOWN) camera.move_right(-move_velocity);
        if (KR_DOWN) camera.move_up(move_velocity);
        if (KF_DOWN) camera.move_up(-move_velocity);

        // camera rotation
        if (KE_DOWN) camera.yaw(rot_velocity);
        if (KQ_DOWN) camera.yaw(-rot_velocity);
        if (KG_DOWN) camera.pitch(rot_velocity);
        if (KT_DOWN) camera.pitch(-rot_velocity);
        if (KZ_DOWN) camera.roll(rot_velocity);
        if (KC_DOWN) camera.roll(-rot_velocity);

        camera.render_perspective(renderables);
        graphics.drawImage(camera.getImage(), 0, 0, null);
        repaint();

        Sphere s1 = (Sphere)renderables.get(0);
        Sphere s2 = (Sphere)renderables.get(1);
        s1.setOrigin(s1.getOrigin().rotateZ(rot_velocity));
        s2.setOrigin(s2.getOrigin().rotateZ(rot_velocity));
    }

    private Main(int width, int height) {
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

        //Vec3 camera_pos = new Vec3(5f,5f,2f);
        Vec3 camera_pos = new Vec3(0, 0, 2);
        Vec3 camera_dir = new Vec3(1f,0f,0f).unit();
        Vec3 camera_up  = new Vec3(0f,0f,1f);

        Sphere s1 = new Sphere(1f, Color.GREEN, new Vec3(0f, -2.1f,2f));
        Sphere s2 = new Sphere(1f, Color.BLUE, new Vec3(0f, 2.1f,2f));
        MirrorSphere ms1 = new MirrorSphere(1f, new Vec3(0f, 0f, 2f));
        LensSphere ms3 = new LensSphere(1.2f, new Vec3(-7f, 0f, 2f));
        Plane plane = new Plane();

        renderables = new ArrayList<>();

        camera = new PinholeCamera(
                width, height,
                camera_pos,
                camera_dir,
                camera_up
        );

        renderables.add(s1);
        renderables.add(s2);
        renderables.add(plane);
        renderables.add(ms1);
        renderables.add(ms3);

        this.setFocusable(true);
        this.addKeyListener(this);
    }


    public static void image_demo() {
        int width = 600;
        int height =  width * 9 / 16;
        Main main = new Main(width, height);
    }

    public static void main(String[] args) {
        image_demo();
    }


}
