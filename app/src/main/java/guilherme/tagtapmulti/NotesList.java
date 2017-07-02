package guilherme.tagtapmulti;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Destino Dublin on 02-Jul-17.
 */

public class NotesList extends ArrayAdapter<DailyNotes> {

    private Activity context;
    List<DailyNotes> Daily_Notes;

    public NotesList(Activity context, List<DailyNotes> Daily_Notes) {
        super(context, R.layout.layout_notes_list, Daily_Notes);
        this.context = context;
        this.Daily_Notes = Daily_Notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_notes_list, null, true);
        TextView textViewNotes = (TextView) listViewItem.findViewById(R.id.textViewNotes);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.textViewCategory);
        DailyNotes dailynotes = Daily_Notes.get(position);
        textViewNotes.setText(dailynotes.getNotes());
        textViewCategory.setText(dailynotes.getCategory());
        return listViewItem;
    }


}
