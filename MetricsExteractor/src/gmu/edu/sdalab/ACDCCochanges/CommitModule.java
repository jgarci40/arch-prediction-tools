package gmu.edu.sdalab.ACDCCochanges;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ehsan
 * Date: 4/18/12
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommitModule {
    //private String name;
    private String author;
    private long revision;
    private Date date;
    private String message;
    private List<String> changedFiles;
    //private int index;

    public CommitModule(String a, long r, Date d, String m)
    {
        //name = n;
        author = a;
        revision = r;
        date = d;
        message = m;
    }

    public void setModifiedFilesList(List<String> s)
    {
        changedFiles = new ArrayList<String>(s);
    }

   // public void setIndex(int i) {index = i;}

   //public String getName(){return name;}
    public String getAuthor() {return author;}
    public long getRevision() {return revision;}
    public Date getDate() {return date;}
    public String getMessage() {return message;}
    public List<String> getModifiedFilesList() {return changedFiles;}
   // public int getIndex() {return index;}

    public String toString()
    {
        return  author + " " + revision + " " + date + " " + message + " " + changedFiles.toString();
    }
}
