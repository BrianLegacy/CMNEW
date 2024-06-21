package org.mspace.clientmanager.api.model;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *
 * @author olal
 */

public class TSmsApiKeyDataModel extends ListDataModel<TSmsApiKey> implements SelectableDataModel<TSmsApiKey> {

    public TSmsApiKeyDataModel() {
    }

    public TSmsApiKeyDataModel(List<TSmsApiKey> data) {
        super(data);
    }

    @Override
    public TSmsApiKey getRowData(String rowKey) {
        List<TSmsApiKey> keys = (List<TSmsApiKey>) getWrappedData();

        for (TSmsApiKey key : keys) {
            if (key.getId() == Integer.parseInt(rowKey)) {
                return key;
            }
        }

        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public String getRowKey(TSmsApiKey key) {
        String keyId = Integer.toString(key.getId());
        return keyId;
    }

    @Override
    public void forEach(Consumer<? super TSmsApiKey> action) {
        super.forEach(action); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Spliterator<TSmsApiKey> spliterator() {
        return super.spliterator(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
