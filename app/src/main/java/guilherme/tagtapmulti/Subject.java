package guilherme.tagtapmulti;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Destino Dublin on 01-Jul-17.
 */

@IgnoreExtraProperties
public class Subject {

    private String subjectId;
    private String subject;
    private int priority;

    public Subject() {
    }

    public Subject(String subjectId, String subject, int priority) {

        this.subject = subject;
        this.priority = priority;
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }



    public int getPriority() {
        return priority;
    }

}
