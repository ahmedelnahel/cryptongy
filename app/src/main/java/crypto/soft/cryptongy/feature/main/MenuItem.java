package crypto.soft.cryptongy.feature.main;

import java.io.Serializable;

/**
 * Created by Ajahar on 11/20/2017.
 */

public class MenuItem implements Serializable {
    int resourceId;
    String itemName;
    boolean isSelected;

    public MenuItem(int resourceId, String itemName, boolean isSelected) {
        this.resourceId = resourceId;
        this.itemName = itemName;
        this.isSelected=isSelected;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
