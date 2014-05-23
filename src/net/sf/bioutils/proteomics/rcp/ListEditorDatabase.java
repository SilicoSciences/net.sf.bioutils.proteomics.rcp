package net.sf.bioutils.proteomics.rcp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

public class ListEditorDatabase extends ListEditor {

    public ListEditorDatabase() {
        super();
    }

    public ListEditorDatabase(final String name, final String labelText, final Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    public String createList(final String[] items) {
        final StringBuilder sb = new StringBuilder();
        for (final Object o : items) {
            sb.append(o.toString());
            sb.append(";");
        }
        return sb.toString();
    }

    public List<File> getList(final String string) {
        final List<File> kv = new ArrayList<File>();
        final String[] list = parseString(string);
        for (final String s : list) {
            final String[] ss = s.split(",");
            if (ss.length != 1) {
                throw new RuntimeException();
            }
            kv.add(new File(ss[0]));
        }
        return kv;
    }

    @Override
    protected String getNewInputObject() {
        final DialogDatabase d = new DialogDatabase(getShell());
        final int r = d.open();
        if (r == Window.OK)
            return d.getResult();
        else
            return null;
    }

    @Override
    public String[] parseString(final String stringList) {
        return stringList.split(";");
    }

}
