package guilherme.tagtapmulti;
/**
 * Created by Destino Dublin on 01-Jul-17.
 */

public class DailyNotes {

    private String notesId;
    private String notes;
    private String category;

    //constructor required
    public DailyNotes(){
    }

    public DailyNotes(String notesId, String notes, String category) {
        this.notesId = notesId;
        this.notes = notes;
        this.category = category;
    }

    public String getNotesId() {
        return notesId;
    }

    public String getNotes() {
        return notes;
    }

    public String getCategory() {
        return category;
    }
}
