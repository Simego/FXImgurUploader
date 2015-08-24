package com.thesimego.framework.jfx.table;

import com.thesimego.framework.jfx.builder.TextFieldBuilder;
import com.thesimego.framework.jfx.entity.GenericEN;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 *
 * @author drafaelli
 */
public class CustomCellFactory {

    private Callback<Object, Boolean> isEditable;
    
    public CustomCellFactory() {
    }

    public CustomCellFactory(Callback<Object, Boolean> editable) {
        this.isEditable = editable;
    }

    /**
     *
     * @author drafaelli
     * @param <E> Entity Class
     * @param <O> Parameter Type
     */
    public class StringCellFactory<E, O extends String> implements Callback<TableColumn<E, O>, TableCell<E, O>> {

        private String placeholder;
        private Integer maxlength;
        
        public StringCellFactory() {
        }

        public StringCellFactory(Integer maxlength) {
            this.maxlength = maxlength;
        }

        public StringCellFactory(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        public TableCell<E, O> call(TableColumn<E, O> param) {
            TableCell<E, O> cell = new TableCell<E, O>() {

                private TextField textField;

                @Override
                public void startEdit() {
                    super.startEdit();
                    if (isEditable() && (isEditable == null || isEditable.call(getTableRow().getItem()))) {
                        if (textField == null) {
                            createTextField();
                        }

                        setGraphic(textField);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        textField.setText(getString());
                        textField.requestFocus();
                        textField.selectAll();
                    }
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    textField.setText(null);
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }

                @Override
                public void commitEdit(O newValue) {
                    final TableView<E> table = getTableView();
                    if (table != null) {
                        // Inform the TableView of the edit being ready to be committed.
                        TableColumn.CellEditEvent editEvent = new TableColumn.CellEditEvent(
                                table,
                                table.getEditingCell(),
                                TableColumn.editCommitEvent(),
                                newValue
                        );

                        Event.fireEvent(getTableColumn(), editEvent);
                    }

                    if (table != null) {
                        table.edit(-1, null);
                    }
                    updateItem(newValue, false);
                }

                @Override
                public void updateItem(final O item, boolean empty) {
                    if (empty) {
                        setText(null);
                        setGraphic(textField);
                    } else {
                        if (isEditing()) {
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            setGraphic(textField);
                        } else {
                            setContentDisplay(ContentDisplay.TEXT_ONLY);
                            setText(getString() == null && placeholder != null ? placeholder : getString());
                        }
                    }
                }

                private void createTextField() {
                    textField = TextFieldBuilder.$().maxlength(maxlength).get();
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                    textField.setOnKeyPressed(t -> {
                        if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
                            commitEdit((O) textField.getText());
                        } else if (t.getCode() == KeyCode.ESCAPE) {
                            cancelEdit();
                        }
                    });
                    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (!newValue) {
                            cancelEdit();
                        }
                    });
                }

                private String getString() {
                    O item = (O) getTableColumn().getCellData(getIndex());
                    if (item == null) {
                        return null;
                    } else {
                        return item;
                    }
                }

            };
            cell.setEditable(true);
            return cell;
        }
    }

    /**
     *
     * @author drafaelli
     * @param <E> Entity Class
     * @param <O> Parameter Type
     */
    public class NumberCellFactory<E, O extends Number> implements Callback<TableColumn<E, O>, TableCell<E, O>> {

        private int minFractionDigits = 0;
        private int maxFractionDigits = 2;

        public NumberCellFactory() {
        }

        public NumberCellFactory(int maxFractionDigits) {
            this.maxFractionDigits = maxFractionDigits;
        }

        @Override
        public TableCell<E, O> call(TableColumn<E, O> param) {
            TableCell<E, O> cell = new TableCell<E, O>() {
                @Override
                public void updateItem(final O item, boolean empty) {
                    if (item != null) {
                        Number value = (Number) item;
                        if (item instanceof Integer || item instanceof Long) {
                            NumberFormat format = NumberFormat.getIntegerInstance();
                            format.setGroupingUsed(true);
                            setText(format.format(value));
                        } else if (item instanceof Double) {
                            NumberFormat format = DecimalFormat.getNumberInstance();
                            format.setGroupingUsed(true);
                            format.setRoundingMode(RoundingMode.HALF_UP);
                            format.setMinimumFractionDigits(minFractionDigits);
                            format.setMaximumFractionDigits(maxFractionDigits);
                            setText(format.format(value));
                        } else {
                            setText(item.toString());
                        }
                    }
                }
            };
            return cell;
        }
    }

    /**
     *
     * @author drafaelli
     * @param <E> Entity Class
     * @param <O> Parameter Type
     */
    public class DateCellFactory<E, O extends Date> implements Callback<TableColumn<E, O>, TableCell<E, O>> {

        private DateFormat df;
        private String format;
        private Integer dateFormat;
        private Integer hourFormat;

        public DateCellFactory() {
        }

        public DateCellFactory(String format) {
            this.format = format;
        }

        public DateCellFactory(Integer dateFormat, Integer hourFormat) {
            this.dateFormat = dateFormat;
            this.hourFormat = hourFormat;
        }

        @Override
        public TableCell<E, O> call(TableColumn<E, O> param) {
            TableCell<E, O> cell = new TableCell<E, O>() {
                @Override
                public void updateItem(final O item, boolean empty) {
                    if (item != null) {
                        Date value = (Date) item;
                        if (format == null) {
                            df = DateFormat.getDateTimeInstance(
                                    dateFormat == null ? DateFormat.MEDIUM : dateFormat,
                                    hourFormat == null ? DateFormat.SHORT : hourFormat
                            );
                        } else {
                            df = new SimpleDateFormat(format);
                        }
                        setText(df.format(value));
                    }
                }
            };
            return cell;
        }
    }

}
