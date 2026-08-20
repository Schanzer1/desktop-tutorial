import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap; 
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
class DND_02{
    public static void main(String[] args){
        Player sascha = new Player();  
        Welt nwfb = new Welt(sascha);
        
        Monster.sasch = sascha;
        sascha.world_that_is_inizialized_only_for_unholy_fire = nwfb;
        World_Time w_time = new World_Time();
        sascha.x = 50;
        sascha.y =50;      
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("game for fun");
        int size = Math.min(screenSize.width, screenSize.height);
        frame.setSize(size , size);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel slide = new camera(nwfb, sascha, w_time);
        frame.add(slide);
        frame.setVisible(true);
        System.out.println("hi weak player ");
        System.out.println("you are in a ****** world, where monsters walk on the earth face every night . You remember how a zombie king stoled your burger for 1 dollar, so u decide to kill him whatever it costs ");
        System.out.println("u prayed ,therefore u got something that u can use as a vessel for water to boil it");
        Scanner sc = new Scanner(System.in);
        Thread inputThread = new Thread(() -> {
        while (true) {
            try {
                if(!sascha.inVillage){
                System.out.println("What do u want?  \n 1. -> Check your Inventory \n 2. -> use smth from it \n 3. put the map back \n 4. -> look for an an apple? \n 5. -> look for wood/branch \n 6. -> start a fire to your left \n 7. -> get water in the vessel \n 8. boil the water");
                int value = Integer.parseInt(sc.nextLine());
                switch (value){
                    case 1: sascha.inventory.get_inventory(); break;
                    case 2: System.out.println("do u want to use your map ( 1 ) or smth else(write it)?");String s = sc.nextLine(); if(s.equals("1")){sascha.map_v = 0;}else{sascha.inventory.get_inventory(); String to_use = sc.nextLine(); for(What_An_Item unholy_game : What_An_Item.values()){if(to_use.equals(unholy_game.name())){if(sascha.inventory.count.getOrDefault(unholy_game, 0) >= 1){ unholy_game.use_item(sascha, sascha.inventory); sascha.inventory.inventory.remove(unholy_game); }}}}break;
                    case 3: sascha.map_v = 1;
                    case 4: String biom = nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x, 100 )][ Math.floorMod(sascha.y, 100)].bio.get_biom(); if(biom.equals("Forest") || biom.equals("Thick Forest")){w_time.time_went(1f); System.out.println(w_time.time);sascha.water_bal -- ; sascha.satiation--;int ap = new Random().nextInt(100) ; if( ap > 70){ sascha.inventory.inventory.add(What_An_Item.APPLE);  System.out.println("You got an apple"); }}break;
                    case 6: sascha.inventory.get_inventory();if(sascha.inventory.count.getOrDefault(What_An_Item.WOOD, 0) >= 2 && sascha.inventory.count.getOrDefault(What_An_Item.BRANCH, 0) >= 5){System.out.println("u did it");sascha.satiation--;if(Math.floorMod(sascha.x, 100 )== 0){nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 )-1, Math.floorDiv(sascha.y, 100)))[99][ Math.floorMod(sascha.y, 100)].bio = Biom.FIRE;sascha.fire_list.add(nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 )-1, Math.floorDiv(sascha.y, 100)))[99][ Math.floorMod(sascha.y, 100)]);} else{nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x -1, 100 )][ Math.floorMod(sascha.y, 100)].bio = Biom.FIRE; sascha.fire_list.add(nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x -1, 100 )][ Math.floorMod(sascha.y, 100)]);}sascha.inventory.inventory.remove(What_An_Item.WOOD);sascha.inventory.inventory.remove(What_An_Item.WOOD);sascha.inventory.inventory.remove(What_An_Item.BRANCH);sascha.inventory.inventory.remove(What_An_Item.BRANCH);sascha.inventory.inventory.remove(What_An_Item.BRANCH);sascha.inventory.inventory.remove(What_An_Item.BRANCH);sascha.inventory.inventory.remove(What_An_Item.BRANCH);}break;
                    case 5: String b = nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x, 100 )][ Math.floorMod(sascha.y, 100)].bio.get_biom();if(b.equals("Forest") || b.equals("Thick Forest")){sascha.water_bal -- ; sascha.satiation--;int ap = new Random().nextInt(100) ; if( ap > 70){ sascha.inventory.inventory.add(What_An_Item.BRANCH); w_time.time_went(0.25f); System.out.println("You got a branch");} else{sascha.inventory.inventory.add(What_An_Item.WOOD); w_time.time_went(0.25f); System.out.println("You got a wood");} }break;
                    case 7: String bb = nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x, 100 )][ Math.floorMod(sascha.y, 100)].bio.get_biom(); if(bb.equals("Water")){sascha.v_f = true;sascha.satiation--;} break;
                    case 8: if(sascha.is_here_fire() && sascha.v_f == true){sascha.v_f = false;sascha.water_bal += 20;System.out.println("Wow! how refreshering");}
                }
                }
                else{
                    System.out.println("Hello, dearly beloved, stranger . What are u looking for? \n 1. ->  Weapon \n 2. -> sdu potion \n 3. -> some food \n 4. ->  bondage \n 5. -> nothing, i want to sell item \n 6. -> go out of village" );
                    int value = Integer.parseInt(sc.nextLine());
                    switch (value){
                        case 1: System.out.println("there are some options"); for(What_An_Item proposition : What_An_Item.getAllByType(Type_Item.WEAPON)){System.out.println(proposition.name());} System.out.println("Therefore.... what are u choosing ? (write the name or 'another time')"); String purchase = sc.nextLine(); for(What_An_Item want_to_buy : What_An_Item.getAllByType(Type_Item.WEAPON)){if(purchase.equals(want_to_buy.name())){if(sascha.cash >= want_to_buy.cost*2){sascha.cash -= want_to_buy.cost*2; sascha.inventory.put_in_inventory(want_to_buy);}else{System.out.println("You dont have enough money , ****");}}} break;
                        case 2: System.out.println("haha, u know exactly what u need \n 1. -> POTION");  System.out.println("Therefore.... will u buy ? (write the name or 'another time' (actually , u can write whatever u want to))"); String purchase_Potion = sc.nextLine(); if(purchase_Potion.equals("POTION")){if(sascha.cash >= What_An_Item.POTION.cost*2){sascha.cash -= What_An_Item.POTION.cost*2; sascha.inventory.put_in_inventory(What_An_Item.POTION);}else{System.out.println("You dont have enough money , ****");}} break;
                        case 3: System.out.println("there are some options"); for(What_An_Item proposition_F : What_An_Item.getAllByType(Type_Item.FOOD)){System.out.println(proposition_F.name());} System.out.println("Therefore.... what are u choosing ? (write the name or 'another time' (actually , u can write whatever u want to))"); String purchase_f = sc.nextLine(); for(What_An_Item want_to_buy : What_An_Item.getAllByType(Type_Item.FOOD)){if(purchase_f.equals(want_to_buy.name())){if(sascha.cash >= want_to_buy.cost*2){sascha.cash -= want_to_buy.cost*2; sascha.inventory.put_in_inventory(want_to_buy);}else{System.out.println("You dont have enough money , ****");}}} break;
                        case 4: System.out.println("haha, u know exactly what u need \n 1. -> BANDAGE");  System.out.println("Therefore.... will u buy ? (write the name or 'another time' (actually , u can write whatever u want to))"); String purchase_B = sc.nextLine(); if(purchase_B.equals("BANDAGE")){if(sascha.cash >= What_An_Item.BANDAGE.cost*2){sascha.cash -= What_An_Item.BANDAGE.cost*2; sascha.inventory.put_in_inventory(What_An_Item.BANDAGE);}else{System.out.println("You dont have enough money , ****");}} break;
                        case 5: System.out.println("okey, what do u want to sell , i will grab it immedeately and trwon u your money"); sascha.inventory.get_inventory(); String to_sell = sc.nextLine(); for(What_An_Item unholy_game : What_An_Item.values()){if(to_sell.equals(unholy_game.name())){if(sascha.inventory.count.getOrDefault(unholy_game, 0) >= 1){System.out.println("u got " + unholy_game.cost); sascha.cash +=unholy_game.cost; sascha.inventory.inventory.remove(unholy_game); }}} break;
                        case 6: sascha.inVillage = false; sascha.x = sascha.last_field.xField; sascha.y = sascha.last_field.yField;
                    }
                }
            } 
            catch (Exception e) 
            {
                System.out.println("whether u did smth wrong or i wrote smth wrong");
            }
        }
        });
        camera c = (camera) slide;
        inputThread.setDaemon(true);
        inputThread.start();
        new Timer(250, e -> {
            if(!sascha.inVillage){
            if (nwfb.isNight) {

        for(int i = 0; i < sascha.allmonster.size(); i++){
            Monster m = sascha.allmonster.get(i);
            int dx = sascha.x - m.x;
            int dy = sascha.y - m.y;
            double distance = Math.sqrt(dx*dx + dy*dy);
            if(distance <= 15){ 
                if(distance <= 2){ 
                    m.get_attacked(sascha);
                    m.attack(sascha);
                    if(m.hp <= 0){
                        m.defeated(sascha);
                        sascha.allmonster.remove(i);
                        i--; 
                    }
                } else {
                    if(dx != 0) m.x += dx / Math.abs(dx); 
                    if(dy != 0) m.y += dy / Math.abs(dy); 
                    c.updateGridColors();
                    slide.repaint();
                    System.out.println("Monster at: " + m.x + " " + m.y + ", distance: " + distance);
                }
            }
        }
    }
            c.type = sascha.map_v;
            if(sascha.map_v != sascha.pre_map_v){
                c.updateGridColors();
            }
            if(!c.path.isEmpty() ){
                if(c.type!= 0 ){
                Field f = c.path.get(0);
                sascha.last_field = f;
                if(nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x, 100 )][ Math.floorMod(sascha.y, 100)].bio.get_biom().equals("SNOW_MOUNTAIN")){sascha.satiation--;}
                sascha.satiation--;
                if(w_time.time_a_lot){
                    sascha.water_bal--;
                }
                sascha.water_bal--;
                w_time.time_went(0.05f);
                if(Math.floorDiv(sascha.x, 100 ) ==  Math.floorDiv(f.xField, 100 ) ){}
                else if(Math.floorDiv(sascha.x, 100 ) <  Math.floorDiv(f.xField, 100 ) ){
                    nwfb.create_chank(new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )),new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )+1), new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )-1), new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )-2), new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )+1));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )-1));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )+2));
                }
                else{
                    nwfb.create_chank(new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )),new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )+1), new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )-1), new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )-2), new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )+1));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )-1));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )+1));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )-2));
                }
                if(Math.floorDiv(sascha.y, 100 ) ==  Math.floorDiv(f.yField, 100 ) ){}
                else if(Math.floorDiv(sascha.y, 100 ) <  Math.floorDiv(f.yField, 100 ) ){
                    nwfb.create_chank(new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )+2),new Pointchunk(Math.floorDiv(f.xField, 100 )+1, Math.floorDiv(f.yField, 100 )+2), new Pointchunk(Math.floorDiv(f.xField, 100 ), Math.floorDiv(f.yField, 100 )+2), new Pointchunk(Math.floorDiv(f.xField, 100 )-1, Math.floorDiv(f.yField, 100 )+2), new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-1, Math.floorDiv(sascha.y, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+1, Math.floorDiv(sascha.y, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )-2));
                }
                else{
                    nwfb.create_chank(new Pointchunk(Math.floorDiv(f.xField, 100 )+2, Math.floorDiv(f.yField, 100 )-2),new Pointchunk(Math.floorDiv(f.xField, 100 )+1, Math.floorDiv(f.yField, 100 )-2), new Pointchunk(Math.floorDiv(f.xField, 100 ), Math.floorDiv(f.yField, 100 )-2), new Pointchunk(Math.floorDiv(f.xField, 100 )-1, Math.floorDiv(f.yField, 100 )-2), new Pointchunk(Math.floorDiv(f.xField, 100 )-2, Math.floorDiv(f.yField, 100 )-2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-1, Math.floorDiv(sascha.y, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+1, Math.floorDiv(sascha.y, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )-2, Math.floorDiv(sascha.y, 100 )+2));
                    nwfb.world.remove(new Pointchunk(Math.floorDiv(sascha.x, 100 )+2, Math.floorDiv(sascha.y, 100 )+2));
                }
                sascha.x = f.xField;
                sascha.y =f.yField;  //fff
                sascha.monsters_r();
                c.path.remove(0);
                nwfb.make_vis_def(sascha);
                c.updateGridColors();
                w_time.time_a_lot = false;
                nwfb.isNight = (w_time.time >= 20.0f || w_time.time <= 8.0f);
                }
                sascha.what_a_hp();
            }
            else{
                if(nwfb.world.get(new Pointchunk(Math.floorDiv(sascha.x, 100 ), Math.floorDiv(sascha.y, 100)))[Math.floorMod(sascha.x, 100 )][ Math.floorMod(sascha.y, 100)].bio.get_biom().equals("VILLAGE")){
                    sascha.inVillage = true;
                }
            }
            c.updateGridColors();
            if(!sascha.fire_list.isEmpty()){
                Field fff = new Field();
                fff.xField = sascha.fire_list.get(0).xField;
                fff.yField = sascha.fire_list.get(0).yField;
                fff.bio = Biom.FIRE;
                sascha.fire_list.clear();
                sascha.fire_listt.add(fff);

            }

            if(sascha.hp_current <= 0){
                System.out.println("u are dead");
                ((Timer)e.getSource()).stop(); 
                inputThread.stop(); 
                
            }
            slide.repaint();
            }
        }).start();
    }
}
class Welt{ 
    boolean isNight; //= (time >= 20.0f || time <= 8.0f);
    public Pointchunk[] chanks = new Pointchunk[25];
    Player pl;
    int test = 0;
    public HashMap<Pointchunk, Field[][]> world = new HashMap<>();
    public Pointchunk[] chanksss = new Pointchunk[5];
    int period = 1;
    public Welt(Player pl){
        int index = 0;
        this.pl = pl;
        for(int dx = -2; dx <= 2; dx++){
            for(int dy = -2; dy <= 2; dy++){
                chanks[index++] = new Pointchunk(dx, dy);
            }
        }
        for(int i = 0 ; i < 5 ; i++){
            for(int j = 0 ; j< 5 ; j++){
                chanksss[j] = chanks[i*5 + j];
            }
            create_chank(chanksss[0],chanksss[1],chanksss[2],chanksss[3],chanksss[4]);
        }
    }
    public void make_vis_def(Player pl){
        for (int j= pl.y+49 ; j > pl.y-50; j--) {
            for (int i = pl.x-50; i < pl.x+49; i++) {
                Field f = world.get(new Pointchunk(Math.floorDiv(i, 100), Math.floorDiv(j,100)))[Math.floorMod(i,100)][Math.floorMod(j,100 )];
                f.obstacled = get_vis_r(f, pl)<(pl.vission+0.1) ? 1 : 0;
            }
        }
    }
    private double get_vis_r(Field f, Player pl){
        if(f.xField == pl.x && f.yField == pl.y){
            return 0;
        }
        Field f_r = world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField,100 )];
        Field f_l = world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField,100 )];
        Field f_t = world.get(new Pointchunk(Math.floorDiv(f.xField, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField,100)][Math.floorMod(f.yField+1,100 )];
        Field f_u = world.get(new Pointchunk(Math.floorDiv(f.xField, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField,100)][Math.floorMod(f.yField-1,100 )];
        Field f_r_t = world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField+1,100 )];
        Field f_l_t = world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField+1,100 )];
        Field f_l_u = world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField-1,100 )];
        Field f_r_u = world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField-1,100 )];
        find_r(f_t , pl);
        find_r(f_u , pl);
        find_r(f_l , pl);
        find_r(f_r , pl);
        if(f_t.r > f_u.r){
            if(f_r.r > f_l.r){
                return f.bio.diff + get_vis_r(f_l_u, pl)*1.2;
            }
            else if (f_r.r < f_l.r){
                return f.bio.diff + get_vis_r(f_r_u, pl)*1.2;
            }
            else{
                return f.bio.diff + get_vis_r(f_u, pl);
            }
        }
        else if (f_t.r < f_u.r){
            if(f_r.r > f_l.r){
                return f.bio.diff + get_vis_r(f_l_t, pl)*1.2;
            }
            else if (f_r.r < f_l.r){
                return f.bio.diff + get_vis_r(f_r_t, pl)*1.2;
            }
            else{
                return f.bio.diff + get_vis_r(f_t, pl);
            }
        }
        else{
            if(f_r.r > f_l.r){
                return f.bio.diff + get_vis_r(f_l, pl);
            }
            else if (f_r.r < f_l.r){
                return f.bio.diff + get_vis_r(f_r, pl);

            }
            else{
                return 7777777;
            }
        }
    }
    public void find_r(Field f, Player pl){
        int x_distance =pl.x - f.xField;
        int y_distance =pl.y - f.yField;
        f.r = Math.sqrt(x_distance*x_distance + y_distance*y_distance);
        if(f.bio.get_biom().equals("FIRE")){
            pl.everything_is_okey_dont_read_my_cod = Math.sqrt(x_distance*x_distance + y_distance*y_distance);
        }
    }
    public void create_chank(Pointchunk a1, Pointchunk a2,Pointchunk a3,Pointchunk a4,Pointchunk a5){
        Pointchunk[] chankss = {a1,a2,a3,a4,a5};
        for(Pointchunk a : chankss){
            Field[][] kwadrat = new Field[100][100];
            for(int i = 0; i < 100; i++){
                for(int j = 0; j < 100; j++){
                    kwadrat[i][j] = new Field();
                }
            }
            world.put(a, kwadrat);
        }
        //System.out.println("------");
        for(Pointchunk a : chankss){
            for (int j = 0; j < 100; j++) {
                for (int i = 0; i < 100; i++) {
                    world.get(a)[i][j].xField = i + 100*a.chank_x;
                    world.get(a)[i][j].yField = j + 100*a.chank_y;
                    Field f = world.get(a)[i][j];
                    int x = f.xField;
                    int y = f.yField;
                    f.height = (double)(90 + 35*Math.sin(x*0.0270623645*period)*Math.cos(y*0.03206553*period) + 55*Math.sin(x*0.02106534*period + y*0.01984532*period) - 15*Math.cos(x*0.032233645*period)*Math.sin(y*0.031586553*period) + 25*Math.cos(-x*0.0181623645*period)*Math.sin(y*0.019296553*period));
                    
                    if(  (j == 30 && i == 60)   ||  (j == 60 && i == 30) || (j == 70 && i == 60)   ||  (j == 0 && i == 99)){
                        int ap = new Random().nextInt(100) ;
                        int m = 0;
                        for(What_An_Monstr mm: What_An_Monstr.values()){
                            if(ap < m + mm.sp_chance){
                                Monster mst = new Monster(mm , i+100*a.chank_x , j + 100*a.chank_y, this);
                                pl.allmonster.add(mst);
                              //  f.is_monster = true;
                                f.monster = mm;
                             //   System.out.println(test++);
                                break;
                                
                            }
                            else{
                                m += mm.sp_chance;
                            }
                        }
                    }
                    if(f.height <= 0 ){
                        f.bio = Biom.WATER;
                       // f.bio.set_interactional_type();
                    }
                    else if(f.height > 0 && f.height < 24 ){
                        f.bio = Biom.FOREST;
                    }
                    else if(f.height > 24 && f.height < 53 ){
                        f.bio = Biom.THICK_FOREST;
                    }
                    else if(f.height > 53 && f.height < 67 ){
                        f.bio = Biom.FOREST;
                    }
                    else if(f.height > 67 && f.height < 67.005 ){
                        f.bio = Biom.VILLAGE;
                    //   System.out.println("TOWN is spawned");
                    }
                    else if(f.height > 67.0001 && f.height < 97){
                        f.bio = Biom.VALLEY;
                    }
                    else if(f.height > 97 && f.height < 113){
                        f.bio = Biom.FOREST;
                    }
                    else if(f.height > 113 && f.height < 126){
                        f.bio = Biom.THICK_FOREST;
                    }
                    else if(f.height > 126 && f.height < 149){
                        f.bio = Biom.FOREST;
                    }
                    else if(f.height > 149 && f.height < 180){
                        f.bio = Biom.BOTTOM_MOUNTAIN;
                    }
                    else{
                        f.bio = Biom.SNOW_MOUNTAIN;
                    }
                }
            }
        }
    }
}
class Pointchunk{
    public int chank_x ;
    public int chank_y ;
    public Pointchunk (int x, int y){
        chank_x = x;
        chank_y = y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pointchunk)) return false;
        Pointchunk p = (Pointchunk) o;
        return chank_x == p.chank_x && chank_y == p.chank_y;
    }
    @Override
    public int hashCode() {
        return 31 * chank_x + chank_y;
    }
}
class Field{
    public double f ;
    public Field par;
    public double g ;
    public double height;
    public int xField ;
    public int yField ;
    public int obstacled;
    public int obstacled_m;
    public double r;
    public What_An_Monstr monster;
    public double r_m;
   // public boolean is_monster = false;
    public Biom bio;
    public boolean visibility(){
        return (obstacled == 1) ? true : false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field f = (Field) o;
        return xField == f.xField && yField == f.yField;
    }
    @Override
    public int hashCode() {
        return 31 * xField + yField;
    }
}
enum What_An_Item{
    WOOD(Type_Item.MATERIAL,3,0,0,0,0,0,0,5),APPLE(Type_Item.FOOD, 1, 0,0,10,0,0,5,5),FISH(Type_Item.FOOD, 1, 0,0,40,0,0,10,10),LEATHER(Type_Item.LOOT, 2, 0,0,0,0,0,0,15),
    BEAF(Type_Item.FOOD, 1, 0,0,50,0,0,15,20),POTION(Type_Item. FOOD, 1, 0,0,10,5,1,-5,100),BRANCH(Type_Item.MATERIAL, 1, 0,0,0,0,0,0,1),  SMTH(Type_Item.FOOL , 1,1,1,1,1,1,1,1),
    SLIME(Type_Item.LOOT, 1, 0,0,0,0,0,0,5),CLAW(Type_Item.LOOT, 1, 0,0,0,0,0,0,10),FEATHER(Type_Item.LOOT, 1, 0,0,0,0,0,0,5),TALON(Type_Item.LOOT, 1, 0,0,0,0,0,0,15),
    OLD_SWORD(Type_Item.WEAPON, 3, 3,1,0,0,0,0,20),GOOD_SWORD(Type_Item.WEAPON, 5, 6,3,0,0,0,0,75),NORMAL_SWORD(Type_Item.WEAPON, 4, 4,2,0,0,0,0,30),
    GOOD_FOOD(Type_Item.FOOD, 1, 0,0,100,2,0,0,75),WATER_IN_BOTTLE(Type_Item.FOOD, 1, 0,0,0,0,0,-20,20),BANDAGE(Type_Item.EQUIPMENT, 1, 0,0,0,4,0,0,50); 
    Type_Item t_t;
    public int weight;
    public int damage;
    public int armour;
    public int satiation;
    public int heal;
    public int thirst;
    public int f = 0;
    public int cost;
    private What_An_Item(Type_Item t, int a , int b, int c, int d, int e , int f ,int g, int h) {
        t_t = t;
        weight = a;
        damage = b;
        armour = c;
        satiation = d;
        heal = e;
        thirst = g;
        cost = h;
        f = f;
    }
    public static What_An_Item[] getAllByType(Type_Item type) {
        return java.util.Arrays.stream(values())
                .filter(item -> item.t_t == type)
                .toArray(What_An_Item[]::new);
    }
    public void use_item(Player pl, Inventory inventory){
        switch(t_t){
        case MATERIAL:
            if(this == WOOD){
                inventory.inventory.remove(this);
                inventory.inventory.add(What_An_Item.BRANCH);
                inventory.inventory.add(What_An_Item.BRANCH);
                inventory.inventory.add(What_An_Item.BRANCH);
                System.out.println("- 1 wood");
                System.out.println("+ 1 branch");
                System.out.println("+ 1 branch");
                System.out.println("+ 1 branch");
            }
            break;
        case EQUIPMENT:
            if(this == BANDAGE){
                inventory.inventory.remove(this);
                if(pl.hp_current + heal >= pl.hp_max){
                    pl.hp_current = pl.hp_max;
                    System.out.println("You are cured");
                }
                else{
                    pl.hp_current += heal;
                    System.out.println("You still need more heal");
                } 
                System.out.println(this.name());
            }
            break;
        case LOOT:
            System.out.println("Hmmnmmm," + this.name() + "it might cost smth in a village");
            if(pl.inVillage){
                pl.cash += cost;
                pl.inventory.inventory.remove(this);
            }
            break;
        case WEAPON:
           
            if(!pl.weapon_using){
                pl.weapon_using = true;
                pl.damage += this.damage + (int) (  armour* 0.333 + cost *0.111  );
                pl.weapon = this;
                pl.strength += (int) (  armour* 0.333 + cost *0.111  ); 
                pl.armour += this.armour ;
                pl.what_a_hp();
                System.out.println(this.name() + "is equiped");
            }
            else{
                inventory.inventory.add(pl.weapon);
                
                pl.armour -= this.armour; 
                pl.damage -= this.damage + (int) (  armour* 0.333 + cost *0.111  );
                inventory.inventory.remove(this);
                pl.weapon = this;
                pl.what_a_hp();
                System.out.println(this.name() + "is desequiped");
            }
            break;
        case FOOD:
            int aa = (int)(Math.random()*2)+ 1;
            if(aa == 1 ){
                pl.strength += f;
                System.out.println("u are stronger");
            }
            else{
                System.out.println("cheer up! stay calm )");
            }
            pl.satiation += satiation;
            if(pl.hp_current + heal >= pl.hp_max){
                    pl.hp_current = pl.hp_max;
                }
            else{
                    pl.hp_current += heal;
            } 
            pl.water_bal += thirst;
            System.out.println("You are feeling better");
            if(pl.water_bal <= 40){
                System.out.println("You wanna drink some water");
            }
            break;
        }
    }
}
enum Type_Item{
    MATERIAL,
    EQUIPMENT,
    LOOT,
    WEAPON,
    FOOL,
    FOOD;
}
class World_Time{
    public double time = 12.0;
    public double pre_t = 12.0;
    public boolean time_a_lot = false;
    public boolean day_or = true;
    public boolean get_day_state(){
        return day_or;
    }
    public void time_went(float t){
        if(pre_t +0.5f <= time){time_a_lot = true;}
        pre_t = time;
        time = time + t;
        if(time - 24 >=0){
            time = time - 24;
        }
    }
}
enum Biom{
    WATER(new Color(40, 90, 140),6,1,50),       
    FOREST(new Color(60, 140, 60),13,2.5,1.5),     
    THICK_FOREST(new Color(20, 90, 20), 10,3.5,2),  
    VALLEY(new Color(120, 200, 120),15,1,1),   
    VILLAGE(new Color(0, 0, 0),20,1,1),       
    SNOW_MOUNTAIN(new Color(255,255,255),4,2,2.5),
    FIRE(new Color(200,30,30),5,2,100),
    BOTTOM_MOUNTAIN(new Color(167, 123, 80), -7, 1.5, 1.7);

    public final Color color;
    public int temperature;
    public double diff;
    public double diff_m;
    public boolean interact_can_apple= false;

    Biom(Color color, int Temp, double diff, double diff_m) {
        this.color = color;
        this.temperature = Temp;
        this.diff = diff;
        this.diff_m = diff_m;
    }
    public String get_biom(){
        switch(this){
            case WATER: return "Water";
            case FOREST: return "Forest";
            case THICK_FOREST: return "Thick_Forest";
            case VALLEY: return "Valley";
            case SNOW_MOUNTAIN: return "Snow_Mountain";
            case BOTTOM_MOUNTAIN: return "Bottom_Mountain";
            case VILLAGE: return "Village"; 
            case FIRE : return "Fire";
        }
        return "wtf is this one";
    }
    //void set_interactional_type(){
     //   this.interact_can_apple = true;
   // }
}
class Inventory{
    public ArrayList<What_An_Item> inventory = new ArrayList<>();
    HashMap<String, What_An_Item> inv = new HashMap<>();
    EnumMap<What_An_Item, Integer> count = new EnumMap<>(What_An_Item.class);
    public void get_inventory(){
        count.clear();
        System.out.println("In your inventory are:");
        inv.clear();
        for (What_An_Item item : inventory) {
            count.put(item, count.getOrDefault(item, 0) + 1);
            inv.put(item.name()/* .toString()  */, item);
        }
        for (What_An_Item item : What_An_Item.values()) {
            System.out.println(item + " : " + count.getOrDefault(item, 0));
        }
    }
    public What_An_Item take_an_item(String s){
        if(inv.containsKey(s)){
            inventory.remove(inv.get(s));
            return inv.get(s);
        }
        else{
            return What_An_Item.SMTH;
        }
    }
    public void put_in_inventory(What_An_Item it){
        inventory.add(it);
    }
}
class Monster{
    public int x;
    public int y;
    public int st_x;
    public int st_y;
    public int hp;
    public static Player sasch;
    Welt world;
    public double r_pl;
    public ArrayList<Field> path = new ArrayList<>();
    public int vision = 20;
    public What_An_Monstr type; 
    /*public void see_player(){
        world.find_r_m(world.world.get(new Pointchunk(Math.floorDiv(sasch.x,100),Math.floorDiv(sasch.y , 100)))[Math.floorMod(sasch.x,100)][Math.floorMod(sasch.y , 100)], this);
        if(world.world.get(new Pointchunk(Math.floorDiv(sasch.x,100),Math.floorDiv(sasch.y , 100)))[Math.floorMod(sasch.x,100)][Math.floorMod(sasch.y , 100)].r_m <= vision){
            move_pl(this,sasch.x,sasch.y);
        }
    
    }*/
    public Monster(What_An_Monstr mnst, int x, int y, Welt world){
        type = mnst;
        this.hp = type.hp;
        this.st_x = x;
        this.x = x;
        this.st_y = y;
        this.y = y;
        this.world = world;
    }
    public void defeated(Player pl){
        if(this.hp <= 0){
            for(int i = 0; i < type.talon ; i++){
                System.out.println("+1 Talon");
                pl.inventory.put_in_inventory(What_An_Item.TALON);
            }
            for(int i = 0; i < type.slime ; i++){
                System.out.println("+1 Slime");
                pl.inventory.put_in_inventory(What_An_Item.SLIME);
            }
            for(int i = 0; i < type.leather ; i++){
                System.out.println("+1 Leather");
                pl.inventory.put_in_inventory(What_An_Item.LEATHER);
            }
            for(int i = 0; i < type.claw ; i++){
                System.out.println("+1 Claw");
                pl.inventory.put_in_inventory(What_An_Item.CLAW);
            }

        }
    }
    public String Monster_name(){
        return type.name();
    }
    public void attack(Player pl){
        int ap = new Random().nextInt(100);
        if(ap > type.miss_chance){
            pl.hp_current -= type.damage - pl.armour;
            System.out.println("u got damage");
        }
        else{
            System.err.println(type.name()  + " missed");
        }
    }
    public void get_attacked(Player pl){
        if(pl.damage > type.armour){
            hp -= pl.damage-type.armour;
            System.out.println("u attacked " + type.name());
        }
        else{
            System.out.println("GG, he has more armour then your damage even is");
        }
    }
}
enum What_An_Monstr{
    EAGLE(5, 0, 10, 20f, 5, 0 , 2, 1, 5), SLIME(3,0,7, 30f, 30, 5,0,0,0), WOLF(5,2,10, 5f, 20,0,5,3,0), GOBLIN(6,1,5,10f, 10,0,2,2,0),ZOMBIE(6,0,15,30f, 5,0,0,0,0), BOAR(7,4,20,25f,5,0,4,10,0), HOB_GOBLIN(10,4, 35, 5f,3,0,4,10,0), ZOMBIE_KING(15,6,80,0f,1,999,999,999,999);

    public int damage;
    public int armour;
    public int hp;
    public float miss_chance;
    public int sp_chance;
    public int talon;
    public int leather;
    public int claw;
    public int slime;
    public int x_m;
    public int y_m;
   
    What_An_Monstr(int dmg, int armour, int hp, float miss_chance, int spawn_chance , int sl, int cl, int leat, int tal){
        damage = dmg;
        this.armour = armour;
        this.hp = hp;
        this.miss_chance = miss_chance;
        this.talon = tal;
        this.sp_chance = spawn_chance;
        this.slime = sl;
        this.claw = cl;
        this.leather = leat;
    }
}

class Player {
    public int x;
    public Welt world_that_is_inizialized_only_for_unholy_fire;
    public int y;
    public int vission = 50;
    public int map_v = 1;
    public int pre_map_v = 1;
    public int hp_current = 20;
    public int hp_max = 20;
    public int stamina = 100;
    public int strength = 10;
    boolean inVillage = false;
    public int satiation = 100; 
    public boolean v_f = false;
    public int damage = 3;
    public int water_bal = 100;
    public int armour = 0;
    public ArrayList<Monster> allmonster = new ArrayList<>();
    What_An_Item weapon;
    Field last_field;
    ArrayList<Field> fire_list = new ArrayList<>();
    ArrayList<Field> fire_listt = new ArrayList<>();
    boolean weapon_using = false;
    public int exp = 0;
    public int lvl = 0;
    public int cash = 0;
    public Inventory inventory = new Inventory();
    public double everything_is_okey_dont_read_my_cod;
    public boolean is_here_fire(){
        for(Field f: fire_listt){
            world_that_is_inizialized_only_for_unholy_fire.find_r(f, this);
            if(everything_is_okey_dont_read_my_cod <= 1.3){
                return true;
            }
        }
        return false;
    }
    public double find_r(int x, int y){
        int x_distance =this.x - x;
        int y_distance =this.y - y;
         return Math.sqrt(x_distance*x_distance + y_distance*y_distance);
    }
    public void monsters_r(){
        for(Monster m : allmonster){
            m.r_pl = find_r(m.x , m.y);
        }
    }
    public void what_a_hp(){
        double a = (double ) hp_current / hp_max; 
        hp_max = 20 + strength;
        damage += strength;
        strength = 0;
        hp_current = (int)  (hp_max * a);
        weapon_using = true;
        if(water_bal < 0 ){
            hp_current = 0;
        }
        if(satiation < 0){
            hp_current = 0;
        }
    }
    public void check_level(){
        if (Math.floorDiv(exp, 100) == 1){
            exp = exp - 100;
            strength +=2;
        }
    }
}






class camera extends JPanel {
    public static int type  = -1;
    public ArrayList<Field> path = new ArrayList<>();
    public ArrayList<Field> path_m = new ArrayList<>();
    public Welt world; 
    World_Time w_t;
    boolean for_monster = false;
    Player pl;
    JButton[][] gridofbtn = new JButton[100][100];
    public camera(Welt world, Player pl, World_Time t) {
        this.world = world;
        this.pl =pl;
        w_t = t;
      //  type = pl.map_v;
        this.setLayout(new GridLayout(100, 100));
        initGrid();
    }
    public void build_path(Field f, int i , int j){
        Field f_final = world.world.get(new Pointchunk(Math.floorDiv(i, 100), Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)];
        Field current = f_final;
        if(for_monster){
            while(current != null){
            path_m.add(current);
            current = current.par; 
        }
        }
        else{
        while(current != null){
            path.add(current);
            current = current.par; 
        }
        if(!for_monster){
            Collections.reverse(path);
        }
        }
        
        
    }
    public void initGrid() {
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                JButton btn = new JButton();
                gridofbtn[i][j] = btn;
                int fi = i;
                int fj = j;
                btn.setBorderPainted(false);
                btn.addActionListener(e -> move_pl(pl,pl.x - 50 + fi,pl.y + 49 - fj));
                this.add(btn);
            }
        }
        world.make_vis_def(pl);
        updateGridColors();
    }
    public void do_smth(Field f1, Field f2, ArrayList<Field> can_be , ArrayList<Field> we_did ){
        if(f2.visibility() && !we_did.contains(f2)){
            double tentative_g = f1.g + f2.bio.diff_m;
            if(tentative_g < f2.g){
                f2.g = tentative_g;
                f2.f = f2.g + f2.r;
                f2.par = f1;
                if(!can_be.contains(f2)) can_be.add(f2);
            }
        }
    }
    public void move_pl(Player pl, int i, int j){

        if(world.world.get(new Pointchunk(Math.floorDiv(i, 100), Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)].bio.get_biom().equals("FIRE")){System.out.println("**** u, stupid god");}
        else{
        if(world.world.get(new Pointchunk(Math.floorDiv(i, 100), Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)].visibility()){
        path.clear();
        ArrayList<Field> can_be = new ArrayList<>();
        ArrayList<Field> we_did = new ArrayList<>();
        for (int dx = -50; dx < 50; dx++) {
            for (int dy = -50; dy < 50; dy++) {
                int wx = pl.x + dx;
                int wy = pl.y + dy;
                Field f = world.world.get(new Pointchunk(Math.floorDiv(wx, 100), Math.floorDiv(wy, 100)))[Math.floorMod(wx, 100)][Math.floorMod(wy, 100)];
                f.g = Double.MAX_VALUE;
                f.f = Double.MAX_VALUE;
                f.par = null;
            } //visibility
        }
        can_be.add( world.world.get(new Pointchunk(Math.floorDiv(pl.x, 100),Math.floorDiv(pl.y, 100)))[Math.floorMod(pl.x, 100)][Math.floorMod(pl.y, 100)]);
        world.world.get(new Pointchunk(Math.floorDiv(pl.x, 100),Math.floorDiv(pl.y, 100)))[Math.floorMod(pl.x, 100)][Math.floorMod(pl.y, 100)].f = world.world.get(new Pointchunk(Math.floorDiv(i, 100),Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)].r;
        world.world.get(new Pointchunk(Math.floorDiv(pl.x, 100),Math.floorDiv(pl.y, 100)))[Math.floorMod(pl.x, 100)][Math.floorMod(pl.y, 100)].g =0;
        int x = world.world.get(new Pointchunk(Math.floorDiv(i, 100),Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)].xField;
        int y = world.world.get(new Pointchunk(Math.floorDiv(i, 100),Math.floorDiv(j, 100)))[Math.floorMod(i, 100)][Math.floorMod(j, 100)].yField;
        while(!can_be.isEmpty()){
            double the_smallest = -1;
            Field f = null;
            int aa = -1;
            for(int a = 0; a < can_be.size(); a++){
                if(the_smallest == -1 || can_be.get(a).f < the_smallest){
                    the_smallest = can_be.get(a).f;
                    f = can_be.get(a);
                    aa = a;
                }
            }
            if(f.xField == x && f.yField == y){
                build_path(f, i, j);
                break; 
            }
            world.find_r(f, pl);
            can_be.remove(aa);
            we_did.add(f);
            Field f_r = world.world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField,100 )];
                world.find_r(f_r, pl);
                do_smth(f,f_r,can_be,we_did);
            Field f_l = world.world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField,100 )];
                world.find_r(f_l, pl);
                do_smth(f,f_l,can_be,we_did);
            Field f_t = world.world.get(new Pointchunk(Math.floorDiv(f.xField, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField,100)][Math.floorMod(f.yField+1,100 )];
                world.find_r(f_t, pl);
                do_smth(f,f_t,can_be,we_did);
            Field f_u = world.world.get(new Pointchunk(Math.floorDiv(f.xField, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField,100)][Math.floorMod(f.yField-1,100 )];
                world.find_r(f_u, pl);
                do_smth(f,f_u,can_be,we_did);
            Field f_r_t = world.world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField+1,100 )];
                world.find_r(f_r_t, pl);
                do_smth(f,f_r_t,can_be,we_did);
            Field f_l_t = world.world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField+1,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField+1,100 )];
                world.find_r(f_l_t, pl);
                do_smth(f,f_l_t,can_be,we_did);
            Field f_l_u = world.world.get(new Pointchunk(Math.floorDiv(f.xField-1, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField-1,100)][Math.floorMod(f.yField-1,100 )];
                world.find_r(f_l_u, pl);
                do_smth(f,f_l_u,can_be,we_did);
            Field f_r_u = world.world.get(new Pointchunk(Math.floorDiv(f.xField+1, 100), Math.floorDiv(f.yField-1,100)))[Math.floorMod(f.xField+1,100)][Math.floorMod(f.yField-1,100 )];
                world.find_r(f_r_u, pl);
                do_smth(f,f_r_u,can_be,we_did);
        }
        }
        }
    }
    public void updateGridColors() {
    for (int j = 0; j < 100; j++) {
        for (int i = 0; i < 100; i++) {
            int worldX = pl.x - 50 + i;
            int worldY = pl.y + 49 - j;
            Field f = world.world.get(new Pointchunk(Math.floorDiv(worldX,100),Math.floorDiv(worldY,100)))[Math.floorMod(worldX,100)][Math.floorMod(worldY,100)];
            JButton btn = gridofbtn[i][j];
            if (pl.x == worldX && pl.y == worldY) 
            {
                btn.setBackground(new Color(128,0,128));
            } 
            else if(type == 0){
                btn.setBackground(getGradientColor(f.height));
            }
            else if (type == -1) 
            {
                btn.setBackground(getBiomeColor(f.bio.color, f.bio));
            } 
            else if (f.visibility()) 
            {
                if(world.isNight){
                    btn.setBackground(getBiomeColor(f.bio.color, f.bio));
                    btn.setToolTipText(f.bio.get_biom());
                    for(Monster m : pl.allmonster){
                        if(f.xField == m.x && f.yField == m.y){
                            btn.setBackground(new Color(255,0,0));
                            btn.setToolTipText(m.Monster_name());
                        }
                    }
                    
                }
                else{
                    btn.setBackground(f.bio.color);
                    btn.setToolTipText(f.bio.get_biom());
                }
            } 
            else 
            {
                btn.setBackground(new Color(90,90,90));
                btn.setToolTipText(null);
            }
        }
    }
}
    public void redraw() {
        this.removeAll();
        initGrid();
        this.revalidate();
        this.repaint();
    }
    private Color getBiomeColor(Color baseColor, Biom bio) {
    boolean isNight = world.isNight;
    if(isNight && bio != Biom.FIRE) {
        float factor = 0.2f; 
        int r = (int)(baseColor.getRed() * factor);
        int g = (int)(baseColor.getGreen() * factor);
        int b = (int)(baseColor.getBlue() * factor);
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        return new Color(r, g, b);
    }
    return baseColor;
    }
    private Color getGradientColor(double height) {
        if(height <= 0) return Color.BLUE;
        if(height >= 200) return Color.RED;

        if(height < 50) return interpolateColor(Color.BLUE, new Color(0, 128, 0), (float)height / 50f);
        if(height < 100) return interpolateColor(new Color(0,128,0), Color.YELLOW, (float)(height - 50f) / 50f);
        if(height < 150) return interpolateColor(Color.YELLOW, Color.ORANGE, (float)(height - 100f) / 50f);
        return interpolateColor(Color.ORANGE, Color.RED, (float)(height - 150f) / 50f);
    }
    private Color interpolateColor(Color c1, Color c2, float t) {
        int r = (int)(c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = (int)(c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int)(c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }
}


