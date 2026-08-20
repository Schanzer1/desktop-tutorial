import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
import javax.naming.NoInitialContextException;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class pvp_client {
    public static void main(String[] hi) throws Exception{
        Path targeted = get_current_file_position();
        //Path targeted = Paths.get("/Users/user/Documents/SASHA/game/pvp/");
        if(targeted == null) throw new NoInitialContextException();  // sounds cool
        System.out.println(targeted);
        System.out.println("do u want  grass to be rendered?(tipp yes/no)");
        String answer;
        BufferedImage dsd;
        Scanner sc = new Scanner(System.in);
        while (true) { 
            answer = sc.next();
            answer = answer.toLowerCase();
            //answer = answer.trim();
            if(answer.equals("yes") || answer.equals("no")){break;}
            else{System.out.println("are u dumb? let's try again");}
        }
        sc.close();
        targeted = targeted.resolve("objects");
        Files.list(targeted).forEach(entity_characteristics::write_in);
        boolean grass = answer.equals("yes");
        System.out.println(targeted.getParent());
        world map = new world(grass, targeted.getParent());
        int window_height = 666; 
        int window_width = 1000;
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress("192.168.2.100", 10000));
        




        ByteBuffer iSC = ByteBuffer.allocate(4);
        do { 
            socket.read(iSC);
        } while (iSC.hasRemaining());
        iSC.flip();
        int a = iSC.getInt();
        iSC.flip();
        System.out.println(a + " players will be in room");
        player[] enemy = new player[a-1];  
        for(int i = 0; i< a-1; i++){
            enemy[i] = new player(entity_characteristics.PLAYER,-5,-5);
        }
        camera camera = new camera(1,1,map,window_height,window_width, enemy , socket ); // 1 and 1 is standart,u cant put yourself whereever u desire
        BufferedImage image = new BufferedImage(window_width,window_height,BufferedImage.TYPE_INT_ARGB);
        gameWindow window = new gameWindow(camera,image);
        JFrame frame = new JFrame("shooter_from_china");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(window);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        window.requestFocusInWindow();
      //  for(entity_characteristics test: entity_characteristics.values()){System.out.println(test.poligons.size());} // if u wanna look how many poligons an object has
    }
    static Path get_current_file_position() throws Exception{
        Path start = Paths.get("/");
        AtomicReference<Path> result = new AtomicReference<>();
        System.out.println("looking for pvp_client.java");
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
                if(file.getFileName().toString().equals( "pvp_client.java")){
                    result.set(file.getParent());
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc){
                return FileVisitResult.CONTINUE; 
            }
        });
        return result.get();
    }
}
class gameWindow extends JPanel{
    BufferedImage hi_gays ;
    camera cam;
    //BufferedImage custom_cursor;
    Robot robot ;
    float mouse_sensation = 0.05f;
    volatile boolean w_down = false;
    volatile boolean s_down = false;
    volatile boolean a_down = false;
    volatile boolean d_down = false;
    volatile boolean lmk_down = false;
    int mouseX =0;
    int mouseY = 0;
    int widthImg ;
    int heightImg ;
    ArrayList<entity> bullets = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    ByteBuffer iSC = ByteBuffer.allocate(4);
    int deltaX ;
    int deltaY;
    int hp = 100;
    boolean skip = false;
    public gameWindow(camera camera, BufferedImage bi){
        //custom_cursor = new BufferedImage(16,16,BufferedImage.TYPE_INT_RGB);
        //custom_cursor.setRGB(6,7,0x00000000);custom_cursor.setRGB(7,7,0xFF000000);custom_cursor.setRGB(9,7,0xFF000000);custom_cursor.setRGB(10,7,0xFF000000);custom_cursor.setRGB(7,5,0xFF000000);custom_cursor.setRGB(7,6,0xFF000000);custom_cursor.setRGB(7,8,0xFF000000);custom_cursor.setRGB(7,9,0xFF000000);
        setFocusable(true);
        try {   
            robot = new Robot();
            robot.waitForIdle();
            
        } catch ( Exception e) { e.printStackTrace();
        }
        hi_gays = bi;
        widthImg = bi.getWidth()/2;
        heightImg = bi.getHeight()/2;
        cam = camera;
        setPreferredSize(new Dimension(bi.getWidth(),bi.getHeight()));
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image custom_cursor = toolkit.createImage(new byte[0]);
        Cursor blank = toolkit.createCustomCursor(custom_cursor, new Point(0, 0), "blank");
        setCursor(blank);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if(e.getButton() == MouseEvent.BUTTON1){
                    lmk_down= true;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e){
                if(e.getButton() == MouseEvent.BUTTON1) {
                    lmk_down = false;
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            
            @Override
            public void mouseMoved(MouseEvent e){
                handle(e);
            }
            @Override
            public void mouseDragged(MouseEvent e){
                handle(e);          
            }
            private void handle(MouseEvent e){
                if(skip) {skip = false;return;}
                mouseX = e.getX();
                mouseY = e.getY();
                lock.lock();
                deltaX = mouseX-widthImg;
                deltaY = -mouseY+heightImg;
                lock.unlock();
               // if (deltaX == 0 && deltaY == 0) {
              //      return; 
              //  }
                Point p= getLocationOnScreen();
                
                try {
                    skip = true;
                    robot.mouseMove(p.x+widthImg, p.y+heightImg);
                } catch (Exception ee) 
                { ee.printStackTrace();
                }
                
                
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> w_down = true;
                    case KeyEvent.VK_S -> s_down = true;
                    case KeyEvent.VK_A -> a_down = true;
                    case KeyEvent.VK_D -> d_down = true;
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> w_down = false;
                    case KeyEvent.VK_S -> s_down = false;
                    case KeyEvent.VK_A -> a_down = false;
                    case KeyEvent.VK_D -> d_down = false;
                }
            }
        });
        ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
        schedule.scheduleAtFixedRate(()->{
            repaint();
        }, 0,30, TimeUnit.MILLISECONDS);
    }
    @Override 
    protected void paintComponent(Graphics g){
        int amount = -1;
        bullets.clear();
        lock.lock();
        cam.degree_to_radian(mouse_sensation*deltaX,mouse_sensation*deltaY);
        lock.unlock();
        try {
            ByteBuffer keys = ByteBuffer.allocate(13);
            keys.put((byte)(w_down ? 1 : 0));
            keys.put((byte)(a_down ? 1 : 0));
            keys.put((byte)(s_down ? 1 : 0));
            keys.put((byte)(d_down ? 1 : 0));
            keys.put((byte)(lmk_down ? 1 : 0));
            keys.putFloat(cam.y_rotation_r);
            keys.putFloat(cam.z_rotation_r);
            keys.flip();
            while(keys.hasRemaining()){
            //    System.out.println("sending buttoms");
                cam.sch.write(keys);   
            }
        } catch (Exception e) {e.printStackTrace();}
        try {
            while (iSC.hasRemaining()){ 
            //    System.out.println("reading objects");
                cam.sch.read(iSC);
            } 
            iSC.flip();
            int a = iSC.getInt();
            iSC.clear();
            ByteBuffer buffer = ByteBuffer.allocate(a);
            while(buffer.hasRemaining()){
                cam.sch.read(buffer);
        //        System.out.println("writing down objects");
            }
            buffer.flip();
            while(buffer.hasRemaining()){
                int type = buffer.getInt();
                if(type == 1){
                    cam.x = buffer.getFloat();
                    cam.z = buffer.getFloat();
                    hp = buffer.getInt();
                }
                if(type == 2){
                    bullets.add(new entity(entity_characteristics.BULLET,buffer.getFloat(),buffer.getFloat(),buffer.getFloat()));
                }
                if(type == 3){
                    amount++;
                    cam.enemy[amount] = new player(entity_characteristics.PLAYER,buffer.getFloat(),buffer.getFloat());
                    buffer.getFloat();
                }            
            }        
        } catch (Exception e) { e.printStackTrace();}





        screen shot = cam.take_a_shot(amount,bullets);
        for(int i =-5; i<6;i++ ){
            shot.color[(int)(shot.color.length/2)+i][(int)(shot.color[0].length/2)] = 0xFF000000;
        }
        for(int i =-5; i<6;i++ ){
            shot.color[(int)(shot.color.length/2)][(int)(shot.color[0].length/2)+i] = 0xFF000000;
        }
        for(int i = 0; i< hp; i++){
            for(int j = 0; j<10; j++){
                shot.color[j][i] = 0x00FF00;
            }
        }
        if(hp <= 0){
            System.exit(0);
        }
        write_in(shot);
        super.paintComponent(g);
        g.drawImage(hi_gays,0,0,null);
    }
    private void write_in(screen sc){
        for(int y = 0; y<hi_gays.getHeight();y++){
            hi_gays.setRGB(0, y, sc.color[y].length, 1, sc.color[y], 0, sc.color[y].length);
        }
    }
    

}
class camera{
    public float y_rotation_r = (float)(3.14/2);
    public float z_rotation_r = 0;
    public float x;
    public float z;
    public final float y = 1.5f;
    public float observation;
    public Vector forward = new Vector();
    public Vector up = new Vector();
    public Vector right = new Vector();
    public float k = (3.14159f)/180;  // i use it cause i get angle in degrees ( that is ineffectively,but i just wanted it)
    private Vector world_up = new Vector(0,1,0);
    public world map;
    public int width;
    public int height;
    private screen screen ;
    public player[] enemy;
    private double AOFGV = 1/Math.tan(40*k);
    private float[][] xyzCam = new float[3][3];
    private Vector[] fT = {new Vector(0,0),new Vector(0,height),new Vector(width,height),new Vector(width,0)};
    int c = 0;
    SocketChannel sch;
    public camera(float x, float z, world map, int h, int w, player[] en ,  SocketChannel sch){
        this.x = x;
        this.z = z;
        this.map = map;
        width = w;
        height = h;
        enemy = en;
        for(int i = 0; i<en.length;i++){enemy[i] = new player();}
        screen = new screen(height,width);
        this.sch = sch;
    }
    public void degree_to_radian( float y_rotation_d,float z_rotation_d){
        if(z_rotation_r+z_rotation_d*k > (3.14/2) ){y_rotation_r+=y_rotation_d*k;return;}
        if(z_rotation_r+z_rotation_d*k< -(3.14/2) ){y_rotation_r+=y_rotation_d*k;return;}
        if(z_rotation_r > (3.14/2) ){z_rotation_r = 89*k;return;}    // bad way, it would be better if i used Math.max and min but i dont care
        if(z_rotation_r < -(3.14/2) ){z_rotation_r = -89*k;return;}
        y_rotation_r+=y_rotation_d*k;
        z_rotation_r+=z_rotation_d*k;
    }
    private void update_vectors(){
        forward.y = (float) Math.sin(z_rotation_r);
        forward.x = (float) (Math.cos(z_rotation_r) * Math.cos(y_rotation_r));
        forward.z = (float) (Math.cos(z_rotation_r) * Math.sin(y_rotation_r)); //Vectors are already normilized (+-), i dont care . Ben, Nicolas, falls es euch juckt , dann sollt ihr einfach float to double switchen 
        right = Vector.kreuz_product3(forward, world_up);
        up = Vector.kreuz_product3(forward, right);
        Vector.normilize_vector(up);
        Vector.normilize_vector(right);
        Vector.normilize_vector(forward);
    }
    private ArrayList<triangle> collect_and_distort(int quantity, ArrayList<entity> bullets){  // 3d -> 2d in near area ( triangles )
        ArrayList<entity> visible = new ArrayList<>();
        ArrayList<triangle> tr2d = new ArrayList<>();
        int cameraCellX = (int) Math.floor(x );
        int cameraCellZ = (int) Math.floor(z );
        int minX = Math.max(0, cameraCellX - 30);
        int maxX = Math.min(map.grid.length - 1, cameraCellX + 30);
        for (int i = minX; i <= maxX; i++) {
            int minZ = Math.max(0, cameraCellZ - 30);
            int maxZ = Math.min(map.grid[i].length - 1, cameraCellZ + 30);
            for (int j = minZ; j <= maxZ; j++) {
                visible.addAll(map.grid[i][j].there_are);
            }
        }
        if(quantity != -1){  
            for(int i = 0 ; i<= quantity; i++){
                visible.add(enemy[i]);
            }
        }
        if(bullets != null){
            visible.addAll(bullets);
        }
        for(entity en : visible){
            for(triangle tr : en.poligons ){
                tr2d.addAll(Arrays.asList(change_dimention(tr)));
            }
        }
        return tr2d;
    }   
    private triangle[] change_dimention(triangle tr){  
        for (int i = 0; i < 3; i++) {
            xyzCam[i][0] = tr.points[i].x - this.x;       
            xyzCam[i][1] = tr.points[i].y - this.y;   
            xyzCam[i][2] = tr.points[i].z - this.z;   
        }
        boolean[] within = {false,false,false};
        for(int i = 0; i<3;i++){
            tr.points[i].z2d = Vector.scalar(forward,xyzCam[i][0],xyzCam[i][1],xyzCam[i][2]);
            if(tr.points[i].z2d < 0.1f){
                return new triangle[0];    // for now !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! nah, i let it be
            }
            
            tr.points[i].y2d = (float)(Vector.scalar(up,xyzCam[i][0],xyzCam[i][1],xyzCam[i][2])/tr.points[i].z2d*AOFGV);
            tr.points[i].x2d = (float)(Vector.scalar(right,xyzCam[i][0],xyzCam[i][1],xyzCam[i][2])/tr.points[i].z2d*AOFGV);
            tr.points[i].x2d = (tr.points[i].x2d+1) * width/2;
            tr.points[i].y2d = (tr.points[i].y2d+1) * height/2;
            if(tr.points[i].x2d >= 0 && tr.points[i].x2d <= width && tr.points[i].y2d >= 0 && tr.points[i].y2d <= height) {within[i] = true;} 
        } 
        if(within[0]&&within[1]&&within[2]){
            return new triangle[] {tr};
        }
        else if (within[0]||within[1]||within[2]){
            
            int dif =1;
            if(within[0]==within[1]){
                dif =2;
            }
            else if(within[1] == within[2]){
                dif = 0;
            }
            if(within[dif]){
                triangle trr = new triangle();
                int marker = 1;
                for(int i = 0; i<3;i++){
                    if(i==dif) {trr.points[0] = tr.points[i];continue;}
                    draw_rest(tr.points[dif],tr.points[i]);
                    trr.points[marker++] = tr.points[i];
                }
                trr.sup = tr.sup;
                return new triangle[] {trr};
            }
            else{
                triangle[] trrs = new triangle[2];
                Vector ww = new Vector();
                ww.x2d=tr.points[dif].x2d;
                ww.y2d=tr.points[dif].y2d;
                ww.z2d=tr.points[dif].z2d;
                draw_rest(tr.points[(dif+1)%3],tr.points[dif]);
                trrs[0] = new triangle(tr.points[dif],tr.points[(dif+1)%3],tr.points[(dif+2)%3],tr.sup);
                draw_rest(tr.points[(dif+2)%3],ww);
                trrs[1] = new triangle(tr.points[dif],ww,tr.points[(dif+2)%3],tr.sup);
                return trrs;
            }
        }
        return new triangle[0];
    }
    private void draw_rest(Vector we_know, Vector damn_it_we_dont_know){
        float difX = damn_it_we_dont_know.x2d - we_know.x2d;
        float difY = damn_it_we_dont_know.y2d - we_know.y2d;
        float t = Float.POSITIVE_INFINITY;
        if (damn_it_we_dont_know.x2d < 0) {
            t = (0 - we_know.x2d) / difX;
        }
        else if (damn_it_we_dont_know.x2d > width) {
            t = (width - we_know.x2d) / difX;
        }
        if (damn_it_we_dont_know.y2d < 0) {
            t = Math.min(t, (0 - we_know.y2d) / difY);
        }
        else if (damn_it_we_dont_know.y2d > height) {
            t = Math.min(t, (height - we_know.y2d) / difY);
        }
        damn_it_we_dont_know.x2d = we_know.x2d + t * difX;
        damn_it_we_dont_know.y2d = we_know.y2d + t * difY;
        damn_it_we_dont_know.z2d =we_know.z2d + t * (damn_it_we_dont_know.z2d - we_know.z2d);
    }
    private void write_in(triangle tr){
        int minX = (int)Math.min((Math.min(tr.points[0].x2d,tr.points[1].x2d)),tr.points[2].x2d);
        int maxX = (int)Math.max((Math.max(tr.points[0].x2d,tr.points[1].x2d)),tr.points[2].x2d);
        int minY = (int)Math.min((Math.min(tr.points[0].y2d,tr.points[1].y2d)),tr.points[2].y2d);
        int maxY = (int)Math.max((Math.max(tr.points[0].y2d,tr.points[1].y2d)),tr.points[2].y2d);
        minX = Math.max(0, minX);
        maxX = Math.min(width - 1, maxX);
        minY = Math.max(0, minY);
        maxY = Math.min(height - 1, maxY);
        float[][] vec = {{tr.points[1].x2d-tr.points[0].x2d,tr.points[1].y2d-tr.points[0].y2d,tr.points[1].z2d-tr.points[0].z2d},
                        {tr.points[2].x2d-tr.points[1].x2d,tr.points[2].y2d-tr.points[1].y2d,tr.points[2].z2d-tr.points[1].z2d},
                        {tr.points[0].x2d-tr.points[2].x2d,tr.points[0].y2d-tr.points[2].y2d,tr.points[0].z2d-tr.points[2].z2d}
                        };
        float Ia = 1/tr.points[0].z2d;
        float Ib=1/tr.points[1].z2d;
        float Ic=1/tr.points[2].z2d;
        float A= Vector.area2d(tr.points[1],tr.points[2], tr.points[0].x2d,tr.points[0].y2d);
        for(int x = minX; x<maxX;x++){
            for(int y = minY; y<maxY;y++){
                boolean a1= Vector.kreuz_product2d(vec[0][0],vec[0][1], x-tr.points[0].x2d, y-tr.points[0].y2d)<0;
                boolean a2= Vector.kreuz_product2d(vec[1][0],vec[1][1], x-tr.points[1].x2d, y-tr.points[1].y2d)<0;
                boolean a3= Vector.kreuz_product2d(vec[2][0],vec[2][1], x-tr.points[2].x2d, y-tr.points[2].y2d)<0;
                if(!a1 && !a2 && !a3 || a1 && a2 && a3){
                    float zK = (Ia*Vector.area2d(tr.points[1],tr.points[2],x,y)+Ib*Vector.area2d(tr.points[2],tr.points[0],x,y)+Ic*Vector.area2d(tr.points[0],tr.points[1],x,y))/A;
                    if(screen.z[y][x] > (1/zK)){
                        screen.z[y][x] = 1/zK;
                        screen.color[y][x] = tr.sup.color;
                        c++;
                    }
                }
            }
        }
    }

    public screen take_a_shot(int firstToAdd, ArrayList<entity> bullets){ // fuck, even not a whisky one, but a camera shot
        screen.reset();
        update_vectors();
        ArrayList<triangle> trs = collect_and_distort(firstToAdd, bullets);
        int hi = 0;
        for(triangle tr : trs){
            write_in(tr);
        }
        
        return  screen;
    }
}
class Vector{
    public float x = 0;
    public float y = 0;
    public float z = 0;
    public float x2d;
    public float y2d;
    public float z2d;    
    public Vector(){}
    public Vector(float x, float y, float z){
        this.x=x;this.y=y;this.z=z;
    }
    public Vector(float x, float y){
        x2d = x;
        y2d = y;
    }
    static float vector_length_calculate(float x, float y, float z){
        return (float) Math.sqrt(x*x+y*y+z*z);
    }
    static void normilize_vector(Vector v){
        float length = vector_length_calculate(v.x,v.y,v.z);
        v.x = v.x/length;
        v.y = v.y/length;
        v.z = v.z/length;
    }
    static Vector kreuz_product3(Vector v1, Vector v2){
        return new Vector(v1.y*v2.z-v1.z*v2.y,v1.z*v2.x-v1.x*v2.z,v1.x*v2.y-v1.y*v2.x);
    }
    static int kreuz_product2d(Vector v1, Vector v2){  // < 0   == 0    > 0
        return (int) (v1.x2d*v2.y2d-v1.y2d*v2.x2d);
    }
    static float kreuz_product2d(float v1x,float v1y, float v2x,float v2y){  // < 0   == 0    > 0
        return  (v1x*v2y-v1y*v2x);
    }
    static float scalar(Vector v , float f, float s , float t){
        return v.x*f+t*v.z+v.y*s;
    }
    static float area2d(Vector v1,Vector v2, float startx2, float starty2){
        return (v1.x2d-startx2)*(v2.y2d-starty2) - (v1.y2d-starty2)*(v2.x2d-startx2);
    }
}
class triangle{
    public Vector[] points = new Vector[3];
    public entity sup;
    public triangle(){}
        public triangle(Vector f, Vector s, Vector t){
        points[0] = f;points[1] = s;points[2] = t;
    }
    public triangle(Vector f, Vector s, Vector t,entity obj){
        points[0] = f;points[1] = s;points[2] = t;sup=obj;
    }
}
class world{
    public m_2[][] grid ;
    static final float TILE_SIZE = 2.0f;
    public world(boolean grass, Path path ){   
        System.out.println(path.resolve("world.txt"));
        path = path.resolve("world.txt");
        try (BufferedReader br = Files.newBufferedReader(path);){
            try (Stream<String> lines = Files.lines(path)) {
                long count = lines.count();
                grid = new m_2[(int)count][];
            }
            String line = null;
            int line_n = 0;
            while((line=br.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                line = line.replaceAll(" ", "");
                grid[line_n] = new m_2[line.length()];
                for(int i = 0; i<line.length(); i++){
                    char ch;
                    if((ch = line.charAt(i)) == '2'){
                        grid[line_n][i] = new m_2(grass,line_n,i);
                    }
                    else{
                        grid[line_n][i] = new m_2();
                        grid[line_n][i].put(entity_characteristics.WALL,line_n,i);
                    }
                }
                line_n++;
            }
        } catch (Exception e) {e.printStackTrace();}

    }
}
class m_2{
    ArrayList<entity> there_are = new ArrayList<>();
    m_2(){}
    m_2(boolean grass, int x, int z){
        if(grass==true){
            entity gr = new entity(entity_characteristics.GRASS, x, z);
            there_are.add(gr);
        }
        entity en = new entity(entity_characteristics.FLOOR, x, z);
        there_are.add(en);
    }
    public void put(entity_characteristics blueprint, float x, float z){
        entity en = new entity(blueprint, x, z);
        there_are.add(en);
    }
}
class entity{
    public int color;
    public ArrayList<triangle> poligons = new ArrayList<>();
   // public ArrayList<triangle> poligons2d = new ArrayList<>();
    public entity(){} 
    public entity(entity_characteristics blueprint, float x, float z){
        set_poligons(blueprint, x, z, 0);
    }
    public entity(entity_characteristics blueprint, float x, float z,float y){
        set_poligons(blueprint, x, z,y);
    }
    private void set_poligons(entity_characteristics blueprint, float x, float z, float y){
        this.color = blueprint.color;
        for(triangle tr: blueprint.poligons){
            Vector[] points = new Vector[3];
            for(int i = 0;i<3; i++){
                points[i] = new Vector(tr.points[i].x+x,tr.points[i].y+y,tr.points[i].z+z);
            }
            poligons.add(new triangle(points[0],points[1],points[2],this));
        }
    }
}
class player extends entity{  // must be synchronized with ReentrantLock, cause we change his rotation and coord.  ( nope, i will just write it in an other way)
    float x = 1;  
    float z = 1;
    float rotation = 0; // in radiant , ye , here radiant instead of degree , i am lazy to make it look good
    public player(){
        super(); //x z
    }
    public player(entity_characteristics bl, float h1, float h2){
        super(bl, h1, h2); //x z
    }
    public void set_player(float xx, float zz, float rad){
        move_poligons(-this.x,-this.z);
        rotate_to_the_radiant(rad);
        move_poligons(xx, zz);
        this.x = xx;
        this.z = zz;
    }
    public void go_away(){
        x = -50;
        z = -50;
        move_poligons(x, z);
    }
    private void move_poligons(float x_deviation, float z_deviation){
        for(triangle tr : poligons){
            for(Vector v:tr.points){
                v.x += x_deviation;
                v.z +=z_deviation;
            }
        }
    }
    public void rotate_to_the_radiant(float rad){
        rotate(-rotation+rad);
        rotation = rad;
    }
    private void rotate(float rad){
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        for(triangle tr : poligons){
            for(Vector v : tr.points){
                float oldX = v.x;
                v.x = oldX*cos-v.z*sin;
                v.z = v.z*cos+oldX*sin;
            }
        }
    }
}
enum entity_characteristics{
    WALL(0xFFB22222), PLAYER(0xFF00051A), FLOOR(0xFF654321), GRASS(0xFF00FF00) , BULLET(0xFFFFC107);
    int color;
    ArrayList<triangle> poligons;
    private entity_characteristics(int color) {
        this.color = color;
    }
    public static void write_in(Path p){
       // Files.readAttributes(p, BasicFileAttributes.class);
        try (BufferedReader br = Files.newBufferedReader(p)){
            String name = p.getFileName().toString().toUpperCase();
            name = name.substring(0, name.indexOf(".")==-1 ? name.length() : name.indexOf("."));
            ArrayList<Vector> points = new ArrayList<>();
            ArrayList<triangle> poligons = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                if((line =line.trim()).equals("")) continue;
                String[] parts = line.split("\\s+");
                if(parts[0].equals("v")){
                    points.add(new Vector(Float.parseFloat(parts[1])/2,Float.parseFloat(parts[2])/2,Float.parseFloat(parts[3])/2));
                }
                else if(parts[0].equals("f")){
                    poligons.add(new triangle(points.get(Integer.parseInt(parts[1])-1),points.get(Integer.parseInt(parts[2])-1),points.get(Integer.parseInt(parts[3])-1)));    
                }
            }
            //for(entity_characteristics blueprint : entity_characteristics.values()){
            //    if(blueprint.name().equals(name)){blueprint.poligons = poligons;}
            //}
            entity_characteristics.valueOf(name).poligons = poligons;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
class screen{
    int[][] color;
    float[][] z;
    public screen(int height, int width){
        color= new int[height][width];
        z= new float[height][width];
        reset();
    }
    public void reset(){
        for(int i = 0; i<color.length; i++){
            Arrays.fill(color[i],0xFF00FFFF);
        }
        for(int i = 0; i<color.length; i++){
            Arrays.fill(z[i],Float.MAX_VALUE);
        }
    }
}
/*int RED        = 0xFFFF0000; // красный
int CYAN         = 0xFF00FFFF; // сине-голубой
int GREEN        = 0xFF00FF00; // зеленый

int DARK_BROWN   = 0xFF654321; // темно-коричневый
int DARK_BLUE    = 0xFF00008B; // темно-синий
int BRICK        = 0xFFB22222; // кирпичный */ 
