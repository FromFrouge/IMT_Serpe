import java.util.HashMap;

// La classe gère la fabrication des trames envoyées au serveur TCP/IP
public class Encoder {
    // Déclaration des paramètres du premier octet de la trame
    // et de leurs getters/setters
    private boolean rst;
    private boolean aru;
    private boolean speed_mode;
    private boolean positioning_method;
    private boolean piloting_ON;

    public void setReset(boolean state){
        this.rst = state;
    }
    public boolean getReset(){
        return this.rst;
    }

    public void setARU(boolean state){
        this.aru = state;
    }
    public boolean getARU(){
        return this.aru;
    }

    public void setSpeed_modeMode(boolean state){
        this.speed_mode = state;
    }
    public boolean getSpeed_mode(){
        return this.speed_mode;
    }

    public void setPositioning_methodMethod(boolean state){
        this.positioning_method = state;
    }
    public boolean getPositioning_method(){
        return this.positioning_method;
    }

    public void setPiloting_ON(boolean state){
        this.piloting_ON = state;
    }
    public boolean getPiloting_ON(boolean state){
        return this.piloting_ON;
    }

    private byte initByte;

    public Encoder(){
        this.rst = false;
        this.aru=false;
        this.speed_mode=true;
        this.positioning_method=false;

        this.initByte = 64; // seulement le bit 'Pilotage_ON' d'activé
    }

    private void setInitByte(){
        this.initByte = (byte)((this.aru?1<<7:0) + (this.piloting_ON?1<<6:0) + (this.positioning_method?1<<5:0) +
                (this.speed_mode?1<<4:0) + (this.rst?1<<3:0));
    }

    public byte[] moveAbsolute(int idMotor, short rotation){
        //si la valeur de rotation fournie est supérieur à un tour entier,
        //alors on la transforme en une rotation plus petite
        if(rotation > 3600){
            rotation-=3600;
        }

        byte rot1 = (byte)(rotation & 0xFF);//valeurs faibles
        byte rot2 = (byte)(rotation>>8 & 0xFF);//valeurs fortes

        byte[] frame = new byte[]{this.initByte,0,0,0,0,0,0,0,(byte)idMotor,rot2, rot1};
        return frame;
    }

    public byte[] moveRelative(HashMap<Integer, Byte> speeds){
        byte[] frame = new byte[]{this.initByte,0,0,0,0,0,0,0,0,0};

        if(speeds.size()!=7){
            //throw error not enough/too many speeds given
        }

        byte speed = 0;
        for(Integer key : speeds.keySet()){
            speed = speeds.get(key);
            if(speed > 100) speed -= 100;
            else  if (speed < -100) speed += 100;
            frame[key+1] = speed;
        }

        return frame;
    }
    /*public byte[] moveRelative(boolean[] directions, int[]speeds_prct){
        byte[] frame = new byte[]{this.initByte,0,0,0,0,0,0,0,0,0};

        if(directions.length < 7 || speeds_prct.length < 7){
            //throw error not enough parameters given (7 required)
        }

        byte cmd_mot = 0;
        for(int i = 1; i <= directions.length; i++){
            //true : sens positif
            //false : sens négatif
            if(!directions[i]){
                cmd_mot += 1<<7 + 28;
                cmd_mot += 100-speeds_prct[i];
            }
            else{
                cmd_mot += speeds_prct[i];
            }
            frame[i] = cmd_mot;
        }
        return frame;
    }*/
}
