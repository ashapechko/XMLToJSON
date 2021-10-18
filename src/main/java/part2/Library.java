package part2;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;

@XmlRootElement(name = "library")
@XmlType
public class Library {
    private ArrayList<Author> authorList;

    @Override
    public String toString() {
        return "Library{" +
                "authorList=" + authorList +
                '}';
    }

    @JsonProperty("author")
    public ArrayList<Author> getAuthorList() {
        return authorList;
    }

    @XmlElement(name = "author")
    public void setAuthorList(ArrayList<Author> authorList) {
        this.authorList = authorList;
    }
}



