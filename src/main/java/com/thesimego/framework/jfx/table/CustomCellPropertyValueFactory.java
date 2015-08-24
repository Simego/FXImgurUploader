package com.thesimego.framework.jfx.table;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author drafaelli
 * @param <E>
 * @param <O>
 */
public class CustomCellPropertyValueFactory<E, O> extends PropertyValueFactory<E, O> {

    public CustomCellPropertyValueFactory(String property) {
        super(property);
    }

    @Override
    public ObservableValue<O> call(TableColumn.CellDataFeatures<E, O> param) {
        return super.call(param);
    }

}