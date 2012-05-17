package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.usc.chla.vpicu.explorer.BaseProvider;

public class OccurrenceTablePanel extends JPanel implements OccurrenceListener {

  private static final long serialVersionUID = 1L;

  private static final String[] COLUMNS = { "ID", "Label", "Count" };

  private final JTable table;

  private final JTextField filterField;
  private final JCheckBox matchCase;

  private final VocabTableModel model;

  public OccurrenceTablePanel(BaseProvider provider) {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    Box filterBar = Box.createHorizontalBox();
    filterBar.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
    filterBar.add(new JLabel("Filter"));
    filterField = new JTextField();
    filterBar.add(filterField);
    matchCase = new JCheckBox("Match Case");
    filterBar.add(matchCase);

    add(filterBar, BorderLayout.NORTH);

    model = new VocabTableModel(new ArrayList<Object[]>(), COLUMNS);
    filterField.getDocument().addDocumentListener(model);
    matchCase.addActionListener(model);
    table = new JTable(model);
    add(new JScrollPane(table));
  }

  public void setData(List<Object[]> data) {
    model.setData(data, COLUMNS);
    filterField.setText("");
  }

  private class VocabTableModel extends FilterTableModel implements DocumentListener, ActionListener {

    private static final long serialVersionUID = 1L;

    public VocabTableModel(List<Object[]> data, Object[] columns) {
      super(data, columns);
    }

    @Override
    protected boolean accept(Object[] row) {
      String filter = filterField.getText();
      String value = row[1].toString();
      return row.length > 2 &&
          (matchCase.isSelected() ? value.contains(filter)
              : value.toLowerCase().contains(filter.toLowerCase()));
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      filter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      filter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      filter();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      filter();
    }
  }

  @Override
  public void occurrenceQueryPerformed(List<Object[]> results) {
    setData(results);
  }

}