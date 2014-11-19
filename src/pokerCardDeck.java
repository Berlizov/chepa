import java.util.ArrayList;

/**
 * Created by 350z6233 on 18.11.2014.
 */
public enum PokerCardDeck {
    NOTSET(Integer.MAX_VALUE),
    ZERO(0),
    HALF(0.5),
    ONE(1),
    TWO(2),
    THREE(3),
    FIVE(5),
    EIGHT(8),
    THIRTEEN(13),
    TWENTY(20),
    FORTY(40),
    HUNDRED(1000);
    final double value;
    PokerCardDeck(double val)
    {
        this.value=val;
    }
    public static PokerCardDeck getNearest(double t){
        double min=Double.MAX_VALUE;
        PokerCardDeck p=PokerCardDeck.ZERO;
        for(PokerCardDeck card: PokerCardDeck.values()){
            if(Math.abs(card.value - t)<min)
            {
                p=card;
                min=Math.abs(p.value-t);
            }
        }
        return p;
    }
    public static String[] getModel(){
        String[] str=new String[values().length];
        for (int i = 0; i <str.length; i++) {
            str[i]=values()[i].toString();
        }
        return str;
    }
    public static PokerCardDeck valueOf(int val){
        for (int i = 0; i < values().length; i++) {
            if(values()[i].ordinal()==val){
                return values()[i];
            }
        }
        return NOTSET;
    }
    public static PokerCardDeck getNearest(ArrayList<PokerCardDeck> val){
        double c=0;
        boolean f=false;
        for (PokerCardDeck aVal : val) {
            c += aVal.value;
            if(aVal!=NOTSET)
                f=true;
        }
        c/=val.size();
        if(f) {
            return getNearest(c);
        }else {
            return NOTSET;
        }
    }
}
