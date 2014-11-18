import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Created by 350z6_000 on 18.10.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({UsersTypes.class, User.class,Task.class})
public class Packet {
    public API func;
    public Object[] arguments;


    private Packet() {

    }

    public Packet(API func, Object... arguments) {
        this.func = func;
        this.arguments = arguments;
    }

    protected static String xmlGenerate(Packet packet) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Packet.class);
        Marshaller m = jc.createMarshaller();
        StringWriter sw = new StringWriter();
        m.marshal(packet, sw);
        return sw.toString();
    }

    protected static Packet xmlParse(String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Packet.class);
        Unmarshaller unm = jc.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        return (Packet) unm.unmarshal(sr);
    }

    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    protected String xmlGenerate() throws JAXBException {
        return Packet.xmlGenerate(this);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "func=" + func +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }

    public boolean checkArgCount() {
        int length;
        if (arguments != null)
            length = arguments.length;
        else
            length = 0;
        return (((func.getArgCount()>0)&&(func.getArgCount() == length))||((func.getArgCount()<=0)&&(func.getArgCount() <= length)));
    }

    public <T> T[] getArrayOfArgs(Class<? extends T[]> newType) {
        try {
            return Arrays.copyOf(arguments, arguments.length, newType);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
