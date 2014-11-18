import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 350z6233 on 18.11.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
    private String name="";
    private String project="";
    private PokerCardDeck complexity=PokerCardDeck.NOTSET;
    private PokerCardDeck userComplexity=PokerCardDeck.NOTSET;

    private Task() {

    }

    public Task(String name,
                String project) {
        this.name = name;
        this.project = project;
    }

    public Task(String name,
                String project,
                PokerCardDeck complexity,
                PokerCardDeck userComplexity) {
        this.name = name;
        this.project = project;
        this.complexity = complexity;
        this.userComplexity = userComplexity;
    }

    public String getName() {
        return name;
    }

    public String getProject() {
        return project;
    }

    public PokerCardDeck getComplexity() {
        return complexity;
    }

    public void setComplexity(PokerCardDeck complexity) {
        this.complexity = complexity;
    }

    public PokerCardDeck getUserComplexity() {
        return userComplexity;
    }

    public void setUserComplexity(PokerCardDeck userComplexity) {
        this.userComplexity = userComplexity;
    }
    public static Boolean checkName(String name){
        return name.length()>4;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", project='" + project + '\'' +
                ", complexity=" + complexity +
                ", userComplexity=" + userComplexity +
                '}';
    }
}
