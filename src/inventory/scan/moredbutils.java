/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.scan;

/**
 *
 * @author cedson
 */

import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import javax.swing.table.TableModel;
import java.sql.ResultSet;

public class moredbutils
{
    public static TableModel resultSetToTableModel(final ResultSet set) {
        try {
            final ResultSetMetaData metaData = set.getMetaData();
            final int columnCount = metaData.getColumnCount();
            final Vector<String> columnNames = new Vector<String>();
            for (int i = 0; i < columnCount; ++i) {
                columnNames.addElement(metaData.getColumnLabel(i + 1));
            }
            final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (set.next()) {
                final Vector<Object> obj = new Vector<Object>();
                for (int j = 1; j <= columnCount; ++j) {
                    obj.addElement(set.getObject(j));
                }
                data.addElement(obj);
            }
            return new DefaultTableModel(data, columnNames);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    

}