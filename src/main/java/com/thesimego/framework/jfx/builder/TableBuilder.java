package com.thesimego.framework.jfx.builder;

import com.thesimego.framework.jfx.entity.GenericEN;
import com.thesimego.framework.jfx.table.CustomCellFactory;
import com.thesimego.framework.jfx.table.CustomCellPropertyValueFactory;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 *
 * @author drafaelli
 * @param <T>
 */
public class TableBuilder<T extends GenericEN> {

    private final TableView<T> table;

    private Tooltip showingTooltip;
    private boolean useRowTooltip;
    
    private EventHandler clickEvent;

    /**
     * 
     * @param table 
     */
    private TableBuilder(TableView<T> table) {
        this.table = table;
    }

//    public static <X extends GenericEN> void buildTooltipsAfterPopulate(TableView<X> table) {
//        table.setRowFactory((TableView<X> param) -> {
//            final TableRow<X> row = new TableRow<>();
//            final Tooltip tooltip = new Tooltip();
//            row.setOnMouseEntered((MouseEvent event) -> {
//                if (showingTooltip != null && showingTooltip != tooltip) {
//                    showingTooltip.hide();
//                }
//                if (tooltip.getGraphic() != null) {
//                    Point2D p = row.localToScreen(row.getLayoutBounds().getMaxX() * 0.7, row.getLayoutBounds().getMaxY() + 5);
//                    tooltip.show(row, p.getX(), p.getY());
//                    showingTooltip = tooltip;
//                }
//            });
//            row.setOnMouseExited((MouseEvent event) -> {
//                if (showingTooltip != null) {
//                    showingTooltip.hide();
//                }
//            });
//            row.itemProperty().addListener((ObservableValue<? extends X> observable, X oldValue, X newValue) -> {
//                if (newValue != null && newValue.getTableRowHoverNode() != null) {
//                    tooltip.setGraphic(newValue.getTableRowHoverNode());
//                }
//            });
//            return row;
//        });
//    }

    public static TableBuilder $() {
        TableBuilder builder = new TableBuilder(new TableView<>());
        builder.table.setEditable(true);
        return builder;
    }

    /**
     * 
     * @return 
     */
    public TableView<T> get() {
        
        table.setRowFactory((TableView<T> param) -> {
            final TableRow<T> row = new TableRow<>();
            
            if(clickEvent != null) {
                row.setOnMouseClicked(clickEvent);
            }
            
            if(useRowTooltip) {
                final Tooltip tooltip = new Tooltip();
                row.setOnMouseEntered((MouseEvent event) -> {
                    if (showingTooltip != null && showingTooltip != tooltip) {
                        showingTooltip.hide();
                    }
                    if (tooltip.getGraphic() != null) {
                        Point2D p = row.localToScreen(row.getLayoutBounds().getMaxX() * 0.7, row.getLayoutBounds().getMaxY() + 5);
                        tooltip.show(row, p.getX(), p.getY());
                        showingTooltip = tooltip;
                    }
                });
                row.setOnMouseExited((MouseEvent event) -> {
                    if (showingTooltip != null) {
                        showingTooltip.hide();
                    }
                });
                row.itemProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
                    if (newValue != null && newValue.getTableRowHoverNode() != null) {
                        tooltip.setGraphic(newValue.getTableRowHoverNode());
                    }
                });
            }
            return row;
        });
        
        return table;
    }

    /**
     * String Column, not editable.
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @return
     */
    public <O extends String> TableBuilder column(final String property, String title, int prefWidth) {
        return column(property, title, prefWidth, false, null);
    }

    /**
     * String Column, editable with maxlength textfield.
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @param editable
     * @param maxlength
     * @return
     */
    public <O extends String> TableBuilder column(final String property, String title, int prefWidth, boolean editable, Integer maxlength) {
        TableColumn<T, O> column = new TableColumn(title);
        column.setPrefWidth(prefWidth);
        column.setCellValueFactory(new CustomCellPropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory().new StringCellFactory<>(maxlength));
        column.setEditable(editable);
        column.setOnEditCommit((TableColumn.CellEditEvent<T, O> event) -> {
            try {
                FieldUtils.writeField(event.getRowValue(), property, event.getNewValue(), true);
                table.getItems().set(table.getItems().indexOf(event.getRowValue()), event.getRowValue());
                event.getRowValue().getDao().update(event.getRowValue());
                table.requestLayout();
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TableBuilder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TableBuilder.class.getName()).log(Level.SEVERE, "Error updating entity.", ex);
            }
        });
        table.getColumns().add(column);
        return this;
    }
    
    public <O extends String> TableBuilder column(final String property, String title, int prefWidth, Callback<Object, Boolean> editable, Integer maxlength) {
        TableColumn<T, O> column = new TableColumn(title);
        column.setPrefWidth(prefWidth);
        column.setCellValueFactory(new CustomCellPropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory(editable).new StringCellFactory<>(maxlength));
        column.setEditable(true);
        column.setOnEditCommit((TableColumn.CellEditEvent<T, O> event) -> {
            try {
                FieldUtils.writeField(event.getRowValue(), property, event.getNewValue(), true);
                table.getItems().set(table.getItems().indexOf(event.getRowValue()), event.getRowValue());
                event.getRowValue().getDao().update(event.getRowValue());
                table.requestLayout();
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TableBuilder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TableBuilder.class.getName()).log(Level.SEVERE, "Error updating entity.", ex);
            }
        });
        table.getColumns().add(column);
        return this;
    }

    /**
     * String Column with Placeholder for null cells.
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @param placeholder
     * @return
     */
    public <O extends String> TableBuilder column(final String property, String title, int prefWidth, String placeholder) {
        TableColumn<T, O> column = new TableColumn(title);
        column.setPrefWidth(prefWidth);
        column.setCellValueFactory(new CustomCellPropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory().new StringCellFactory<>(placeholder));
        column.setEditable(false);
        table.getColumns().add(column);
        return this;
    }

    /**
     * Date Column.
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @return
     */
    public <O extends Date> TableBuilder dateColumn(String property, String title, int prefWidth) {
        TableColumn<T, O> column = new TableColumn(title);
        column.setPrefWidth(prefWidth);
        column.setCellValueFactory(new CustomCellPropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory().new DateCellFactory<>(DateFormat.MEDIUM, DateFormat.MEDIUM));
        table.getColumns().add(column);
        return this;
    }

    /**
     * Generic Number Column (double, integer, long).
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @param decimalDigits
     * @return
     */
    public <O extends Number> TableBuilder numberColumn(String property, String title, int prefWidth, int decimalDigits) {
        TableColumn<T, O> column = new TableColumn(title);
        column.setPrefWidth(prefWidth);
        column.setCellValueFactory(new CustomCellPropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory().new NumberCellFactory<>(decimalDigits));
        table.getColumns().add(column);
        return this;
    }

    /**
     * Integer or Long Column.
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @return
     */
    public <O extends Integer> TableBuilder numberColumn(String property, String title, int prefWidth) {
        return numberColumn(property, title, prefWidth, 0);
    }

    /**
     * Decimal Column
     *
     * @param <O>
     * @param property
     * @param title
     * @param prefWidth
     * @return
     */
    public <O extends Double> TableBuilder decimalColumn(String property, String title, int prefWidth) {
        return numberColumn(property, title, prefWidth, 2);
    }

    /**
     * 
     * @return 
     */
    public TableBuilder useTooltipEvents() {
        useRowTooltip = true;
        return this;
    }
    
    /**
     * 
     * @param eventHandler
     * @return 
     */
    public TableBuilder onClick(EventHandler<? super MouseEvent> eventHandler) {
        this.clickEvent = eventHandler;
        return this;
    }

}
