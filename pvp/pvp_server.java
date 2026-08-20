import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.naming.NoInitialContextException;

public class pvp_server {
    static ArrayList<coord> walls = new ArrayList<>();
    static ArrayList<bullet>  bullets  = new ArrayList<>();
    static PLAYER[] players;
    public static void main(String[] blablabla) throws Exception{
        Path targeted = get_current_file_position();
        if(targeted == null) throw new NoInitialContextException();  // sounds cool
        
        targeted = targeted.resolve("world.txt");
        BufferedReader bufR = Files.newBufferedReader(targeted);
        int y = 0;
        
        String line;
        while((line = bufR.readLine())!= null){
            for(int x=0;x<line.length();x++){
              if(line.charAt(x) == '1'){
                walls.add(new coord(y,x));
              }
            }
            y++;
        }
        bufR.close();
        
        int startMin;
        System.out.println("how many players ?");
        Scanner sc = new Scanner(System.in);
        while(true){
          String answer = sc.nextLine();
          if(answer != null && !answer.isEmpty()){
            answer = answer.trim();
            answer = answer.split("\\s+")[0];
            try{
              startMin = Integer.valueOf(answer);
              break;
            }
            catch(Exception e){
              e.printStackTrace();
              System.out.println("try again");
            }
          }
        }
        sc.close();

        players = new PLAYER[startMin];
        bullet.players=players;
        int k = 0;
        String str;
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 80);
        str = socket.getLocalAddress().getHostAddress();
        /*Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        String str = Collections.list(interfaces).stream().filter(e -> !e.isVirtual()).filter(e -> {
        try {
            return e.isUp(); 
        } catch (SocketException ex) {
            return false; 
        }
        }).flatMap(NetworkInterface::inetAddresses).filter(e -> !e.isLoopbackAddress()).filter(Inet4Address.class::isInstance).map(InetAddress::getHostAddress).findFirst().orElse(null);
        */
        System.out.println(str);
        ServerSocketChannel SSC = ServerSocketChannel.open();
        SSC.bind(new InetSocketAddress(str,10000));
        ByteBuffer b = ByteBuffer.allocate(4);
        for(int i = 0; i< startMin; i++){
            SocketChannel sch = SSC.accept();
            players[k] = new PLAYER(sch);
            b.putInt(startMin);
            b.flip();
            while (b.hasRemaining()) {
                sch.write(b);
            }
            b.clear();
            k++;
        }
        Thread[] threads = new Thread[startMin];
        for(int i = 0; i< startMin;i++){
          threads[i] = new Thread(players[i]::run);
          threads[i].start();
        }
        ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleAtFixedRate(()->{
          for(int i = 0; i< 7; i++){
          synchronized(bullets){
            Iterator<bullet> it = bullets.iterator();
            while(it.hasNext()){
              bullet bul = it.next();
              if(bul.fly()){
                it.remove();
              }
            }
          }
            }

   
          for(PLAYER pl : players){
            if(pl.hp > 0){
              pl.move();
            }
          }
          

        }, 0, 50, TimeUnit.MILLISECONDS);                                
  
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
class PLAYER {
  AtomicReference<Float[]> xz = new AtomicReference<>();
  AtomicReference<Float[]> yzR = new AtomicReference<>();
  volatile int hp = 100;
  float[] ys = {0.5f,1.25f,1.75f};
  ByteBuffer header = ByteBuffer.allocate(4);
  long time_shooted = 0;
  static long delay = 300 ;
  ByteBuffer get = ByteBuffer.allocate(13);
  volatile boolean w = false;
  volatile boolean a = false;
  volatile boolean s = false;
  volatile boolean d = false;
  volatile boolean l = false;
  int id;
  static int id_c = 0;
  SocketChannel sch;
  public PLAYER(SocketChannel s){
    xz.set(new Float[]{1f,1f});
    yzR.set(new Float[]{(float)(3.14/2),0f});
    sch = s;
    this.id = id_c++;
  }
  public void move(){
    Float[] magic = yzR.get();
    float dX = 0;
    float dZ = 0;
    if(w){    
      dX += (float) (Math.cos(magic[1]) * Math.cos(magic[0])/15);
      dZ += (float) (Math.cos(magic[1]) * Math.sin(magic[0])/15);
    }
    if(s){
        dX -= (float) (Math.cos(magic[1]) * Math.cos(magic[0])/15);
        dZ -= (float) (Math.cos(magic[1]) * Math.sin(magic[0])/15);
    }
    if(d){  // (-v1.z,0,v1.x)
        dX -= (float) (Math.cos(magic[1]) * Math.sin(magic[0])/15); 
        dZ += (float) (Math.cos(magic[1]) * Math.cos(magic[0])/15);
    }
    if(a){
        dX += (float) (Math.cos(magic[1]) * Math.sin(magic[0])/15); 
        dZ -= (float) (Math.cos(magic[1]) * Math.cos(magic[0])/15);     
    }
    if(l){
      shoot();
    }
    Float[] old = xz.get();
    int x ;
    int z;
    if((old[0]+dX - (int) (old[0]+dX)) > 0.5f){
        x = (int)Math.ceil(old[0]+dX);
    }
    else{ x = (int) (old[0]+dX);}
    if((old[1]+dZ - (int) (old[1]+dZ)) > 0.5f){
        z = (int)Math.ceil(old[1]+dZ);
    }
    else{ z = (int) (old[1]+dZ);}
    if(pvp_server.walls.contains(new coord(x, z))) return;
    xz.set(new Float[]{old[0]+dX,old[1]+dZ});
  }
  public void shoot(){
    if(System.currentTimeMillis()-time_shooted > delay){
      time_shooted = System.currentTimeMillis();
      Float[] magic = yzR.get();
      Float[] old = xz.get();
      synchronized(pvp_server.bullets){
        pvp_server.bullets.add(new bullet(old[0],old[1],magic[0],magic[1],this));
      }
      
    }
  }
  public void run() {
    try {
      while(true){
      //  System.out.println("running" + this);
        while(get.hasRemaining()){
           //    System.out.println("reading buttoms");
              if (sch.read(get) == -1) {
                System.out.println("disconnected");
                return;
              }
        }
        get.flip();
        w = (get.get() == 1);
        a = (get.get() == 1);
        s = (get.get() == 1);
        d = (get.get() == 1);
        l = (get.get() == 1);
        yzR.set(new Float[]{get.getFloat(), get.getFloat()});
        get.flip();
        ArrayList<bullet> finB = new ArrayList<>();
        ArrayList<PLAYER> finP = new ArrayList<>();
        int capacity = 20;ArrayList<bullet> bl;
        synchronized(pvp_server.bullets){
             bl= new ArrayList<>(pvp_server.bullets);
        }
        Float[] xz = this.xz.get();
        for(bullet bbb : bl){
          if(Math.sqrt((bbb.xyz.get()[0]-xz[0])*(bbb.xyz.get()[0]-xz[0])+(bbb.xyz.get()[1]-ys[1])*(bbb.xyz.get()[1]-ys[1]) +(bbb.xyz.get()[2]-xz[1])*(bbb.xyz.get()[2]-xz[1])) < 15f){
              finB.add(bbb);
              capacity += 16;
          }
        }
        for(PLAYER pl: pvp_server.players){
          if(pl.hp > 0 && this.id != pl.id){
            finP.add(pl);
            capacity += 16;
          }
        }
        ByteBuffer sender_buffer = ByteBuffer.allocate(capacity+1);
        sender_buffer.putInt(capacity-4);
        Float[] thisone = this.xz.get();
        sender_buffer.putInt(1);
        sender_buffer.putFloat(thisone[0]);
        sender_buffer.putFloat(thisone[1]);
        sender_buffer.putInt(this.hp);
        for(bullet b :finB){
            sender_buffer.putInt(2);
            Float[] smth = b.xyz.get();
            sender_buffer.putFloat(smth[0]);
            sender_buffer.putFloat(smth[2]);
            sender_buffer.putFloat(smth[1]-1.3f);
        }
        for(PLAYER pl : finP){
            sender_buffer.putInt(3);
            Float[] smth = pl.xz.get();
            sender_buffer.putFloat(smth[0]);
            sender_buffer.putFloat(smth[1]);
            sender_buffer.putFloat(pl.yzR.get()[0]);
        }
        sender_buffer.flip();
        while(sender_buffer.hasRemaining()){
          //  System.out.println("sending objects");
            sch.write(sender_buffer);
        }

      }

    } catch (Exception e) { e.printStackTrace();}

  }
}
class bullet{
    AtomicReference<Float[]> xyz = new AtomicReference<>();
    PLAYER owner;
    Vector forward;
    static PLAYER[] players ;
    public bullet(float x, float z, float yr, float zr, PLAYER pl){
        xyz.set(new Float[]{x,1.3f,z});
        forward = new Vector((float)(Math.cos(zr)*Math.cos(yr)/3),(float)(Math.sin(zr)/3),(float)(Math.cos(zr)*Math.sin(yr))/3); 
        owner = pl;
        /*ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            fly();
        }, 100, 100, TimeUnit.MILLISECONDS);*/
    } 
    public boolean fly(){
        Float[] old = xyz.get();
        float x = forward.x/8 +old[0];
        float y = forward.y/8 +old[1];
        float z = forward.z/8 +old[2];
        int xx ;
        int zz;
        if((x - (int) (x)) > 0.5f){
            xx = (int)Math.ceil(x);
        }
        else{ xx = (int) (x);}
        if((z - (int) (z) )> 0.5f){
            zz = (int)Math.ceil(z);
        }
        else{ zz = (int) z;}
        if(pvp_server.walls.contains(new coord(xx, zz))) return true;
        xyz.set(new Float[]{x,y,z});
        return collide();
    }
    private boolean collide(){
        for(PLAYER pl : players){
          if(pl == owner) continue;
            Float[] xz = pl.xz.get();
            Float[] xyz = this.xyz.get();
            for(float y: pl.ys){
              if (Math.sqrt((xyz[0]-xz[0])*(xyz[0]-xz[0])+(xyz[1]-y)*(xyz[1]-y) +(xyz[2]-xz[1])*(xyz[2]-xz[1])) < 0.250f) {
                pl.hp -=20;
                return true;
              }
              else if (Math.sqrt((xyz[0]-xz[0])*(xyz[0]-xz[0])+(xyz[1]-y)*(xyz[1]-y) +(xyz[2]-xz[1])*(xyz[2]-xz[1])) < 0.375f) {
                pl.hp -=15;
                return true;
              }
              else if(Math.sqrt((xyz[0]-xz[0])*(xyz[0]-xz[0])+(xyz[1]-y)*(xyz[1]-y) +(xyz[2]-xz[1])*(xyz[2]-xz[1])) < 0.525f){
                pl.hp -=10;
                return true;
              }
            }
        }
        return false;
    }
}

class coord{
  int x;
  int y;
  public coord(int x, int y){  // x = y and y = x actually
    this.x = x; 
    this.y = y;
  }
  @Override
  public boolean equals( Object o){
    if(o instanceof coord ){
        coord c = (coord) o;
        return (c.x==this.x&&c.y==this.y);
    }
    return false;
  }
    @Override
    public int hashCode() {
        return 300 * x + y;
    }
}
