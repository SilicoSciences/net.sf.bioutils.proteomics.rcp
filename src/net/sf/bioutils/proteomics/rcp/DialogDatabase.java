package net.sf.bioutils.proteomics.rcp;

import org.apache.commons.lang3.CharUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.PluginTransfer;

public class DialogDatabase extends Dialog {

    private Text textdb;

    private Label labeldb;

    private Button buttonIsIso;

    private boolean redundant = false;

    private String db = "n/a";

    public DialogDatabase(final Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Select new database");
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout(2, true);
        container.setLayout(gridLayout);
        labeldb = new Label(container, SWT.NONE);
        labeldb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        labeldb.setText("Enter DB file path (or drag&drop) [fasta]");

        textdb = new Text(container, SWT.BORDER);
        textdb.setText("n/a");
        textdb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        textdb.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent e) {
                db = textdb.getText();
            }
        });

        buttonIsIso = new Button(container, SWT.CHECK);
        buttonIsIso.setText("Reduandant DB?");
        buttonIsIso.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        buttonIsIso.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
                redundant = false;
            }

            @Override
            public void widgetSelected(final SelectionEvent e) {
                redundant = !redundant;
            }
        });
        final DropTarget dt = new DropTarget(textdb, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
        dt.setTransfer(new Transfer[] { FileTransfer.getInstance(), PluginTransfer.getInstance() });
        dt.addDropListener(new DropTargetAdapter() {
            @Override
            public void drop(final DropTargetEvent event) {
                String fileList[] = null;
                final FileTransfer ft = FileTransfer.getInstance();
                if (ft.isSupportedType(event.currentDataType)) {
                    fileList = (String[]) event.data;
                }
                final StringBuilder sb = new StringBuilder();
                for (final String s : fileList) {
                    sb.append(s);
                    sb.append(CharUtils.LF);
                }
                textdb.setText(sb.toString());
            }
        });
        return container;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 200);
    }

    public String getResult() {
        return db.trim() + "," + redundant;
    }
}
