package guilherme.tagtapmulti;

/**
 * Created by Destino Dublin on 01-Jul-17.
 */

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
