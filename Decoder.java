import java.util.HashMap;

public class Decoder {
    /*
            [0] - infos retour
                * bit 7 : état AEU (0:inactif/1:actif)
                * bit 6 à 4 : N/C
                * bit 3 à 0 : code erreur
            [1] à [12] - données articulation
                * la position de chaque articulation est codée sur 2 octets
                * angle de 360° avec une résolution de 0.1° soit une position entre 0 et 3600
            [13] à [25] - courant consommé par les cartes
                * la consommation de chaque carte est codée sur deux octets en valeur entière
                * la valeur lue représente un courant exprimée en 1/10 de mA (ex: lecture de 530 = 53 mA)
            [26] à [39] - tension d'alimentation des cartes
                * comme pour le courant les valeurs sont codées sur 2 octets
                * la valeur lue représente une tension exprimée avec une résolution de 100 mV
                * la valeur retournée est une différence avec 24V (val négatives -> tension < 24 V)
            [40] à [51] - courant consommé par les moteurs
                * valeur codée sur deux octets en valeur entière
                * la valeur représente un courant exprimé en 1/10 de mA
            [52] à [57] - température des cartes
                * valeur codée sur un octet
                * la valeur représente une température exprimée en °C
            [58] à [63] - température des moteurs
                * valeur codée sur un octet
                * la valeur représente une température exprimée en °C
     */
    public HashMap<String,byte[]> decode(byte[] frame){

        // throw custom errors here

        boolean aruBit_OK = (frame[0]>>7) == 0;
        byte error_code = (byte) (frame[0] & 0x0F);
        byte[] joints_pose = getSlice(frame, 1, 12);
        byte[] boards_current = getSlice(frame, 13, 25);
        byte[] boards_voltage = getSlice(frame, 26, 39);
        byte[] motors_current = getSlice(frame, 40, 51);
        byte[] boards_temp = getSlice(frame, 52, 57);
        byte[] motors_temp = getSlice(frame, 58, 63);

        HashMap<String, byte[]> hash = new HashMap<>();
        hash.put("ARU_&_error_code",new byte[]{(byte) (aruBit_OK?1:0), error_code});
        hash.put("Joints_pose",joints_pose);
        hash.put("Boards_current",boards_current);
        hash.put("Boards_voltage",boards_voltage);
        hash.put("Motors_current",motors_current);
        hash.put("Boards_temp",boards_temp);
        hash.put("Motors_temp",motors_temp);

        return hash;
    }

    private byte[] getSlice(byte[] array, int startIndex, int stopIndex){
        byte[] newArr = new byte[array.length - (stopIndex-startIndex)];
        for(int i=startIndex;i<stopIndex;i++){
            newArr[i-startIndex] = array[i];
        }
        return newArr;
    }
}